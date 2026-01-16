(ns gcp.dev.toolchain.fuzz
  "Toolchain component for certifying generated bindings via property-based fuzzing.
   Implements a multi-stage fuzz protocol to verify correctness before emission."
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [clojure.test.check :as tc]
   [clojure.test.check.properties :as prop]
   [gcp.dev.digest :as digest]
   [gcp.dev.packages :as pkg]
   [gcp.dev.toolchain.emitter :as emitter]
   [gcp.dev.toolchain.fuzz.generators :as fg]
   [gcp.dev.util :as u]
   [gcp.gen :as gen]
   [gcp.global :as global]
   [taoensso.telemere :as tel])
  (:import
   (java.util.concurrent CancellationException)))

(def CERTIFICATION_PROTOCOL
  {:version "v1"
   :stages [{:name :smoke    :tests 10  :max-size 10}
            {:name :standard :tests 50  :max-size 30}
            {:name :stress   :tests 100 :max-size 50}]})

(def CERTIFICATION_HASH
  (digest/sha256 (str (pr-str CERTIFICATION_PROTOCOL)
                      (slurp (io/resource "gcp/dev/toolchain/fuzz.clj")))))

(defn- run-with-timeout [timeout-ms thunk]
  (let [f (future
            (try
              (thunk)
              (catch Exception e
                {:pass? false :error e})))]
    (try
      (deref f timeout-ms {:pass? false :reason :timeout})
      (catch CancellationException _
        {:pass? false :reason :cancelled})
      (finally
        (future-cancel f)))))

(defn check
  [property options]
  (let [num-tests (or (:num-tests options) (:tests options) 100)
        max-size (or (:max-size options) 200)
        seed (or (:seed options) (System/currentTimeMillis))
        timeout-ms (or (:timeout-ms options) 10000)
        result (run-with-timeout timeout-ms
                 (fn []
                   (tc/quick-check num-tests property
                                   :seed seed
                                   :max-size max-size)))]
    (if (:pass? result)
      (assoc result :seed seed)
      (assoc result :seed seed :pass? false))))

(defn load-class-forms! [fqcn forms]
  (try
    (eval (list* 'do forms))
    (catch Exception e
      (tel/log! :error ["Failed to eval forms for" fqcn ":" (.getMessage e)])
      (throw e))))

(defn- load-dependencies! [pkg-key fqcn]
  (let [deps (pkg/dependency-post-order pkg-key fqcn)]
    (doseq [dep (butlast deps)]
      (let [ana-node (pkg/analyze-class pkg-key fqcn)
            forms (emitter/compile-class-forms ana-node)]
        (load-class-forms! dep forms)))))

(defn- run-certification-protocol
  [sk generator verify-fn options]
  (let [base-seed (or (:seed options) (System/currentTimeMillis))
        timeout-ms (or (:timeout-ms options) 60000)
        property (prop/for-all [v generator] (verify-fn v))]
    (tel/log! :info ["Starting fuzz stages for" sk])
    (loop [stages (:stages CERTIFICATION_PROTOCOL)
           history []]
      (if-let [stage (first stages)]
        (let [stage-seed (+ base-seed (count history))
              res (check property (assoc stage :seed stage-seed :timeout-ms timeout-ms))]
          (if (:pass? res)
            (do
              (tel/log! :info ["Stage passed" (:name stage) "for" sk])
              (recur (rest stages) (conj history (merge stage {:seed stage-seed :result :pass}))))
            (do
              (tel/log! :error ["Certification Stage Failed" stage res "for" sk])
              (throw (ex-info "Certification Failed" {:stage stage :result res :schema sk})))))
        {:protocol-hash CERTIFICATION_HASH
         :base-seed base-seed
         :timestamp (str (java.time.Instant/now))
         :passed-stages (into {} (map (juxt :name :seed) history))}))))

(defn certify-class
  "Runs the certification protocol on a class.
   Returns the certification metadata map if successful, throws otherwise."
  [pkg-key fqcn options]
  (let [original-ns *ns*]
    (try
      (println "Certifying..." fqcn)
      ;; 1. Load Dependencies
      (load-dependencies! pkg-key fqcn)
      ;; 2. Load Target (fresh compilation)
      (let [node (pkg/lookup-class pkg-key fqcn)
            ana-node (pkg/analyze-class pkg-key node)
            forms (try
                    (emitter/compile-class-forms ana-node)
                    (catch Exception e
                      (throw (ex-info (str "failed to emit class: " (ex-message e))
                                      {:parser-node node
                                       :ana-node ana-node
                                       :cause e}))))
            _ (load-class-forms! fqcn forms)
            sk (u/schema-key (:package ana-node) (:className ana-node))
            generator (gen/generator sk)
            verify-fn (fn [edn]
                        (let [ns-sym (symbol (str (u/package-to-ns (:package ana-node)) "." (:className ana-node)))
                              from-edn (resolve (symbol (str ns-sym) "from-edn"))
                              to-edn (resolve (symbol (str ns-sym) "to-edn"))]
                          (if (and from-edn to-edn)
                            (let [obj (from-edn edn)
                                  rt-edn (to-edn obj)]
                              (if (global/valid? sk rt-edn)
                                true
                                (do
                                  (tel/log! :error ["Validation Failed" (global/humanize (global/explain sk rt-edn))])
                                  false)))
                            (throw (ex-info "Missing functions" {:ns ns-sym})))))]
        (run-certification-protocol sk generator verify-fn options))
      (finally
        (in-ns (ns-name original-ns))))))

(defn certify-foreign-namespace
  "Certifies all bindings in a foreign namespace.
   A foreign namespace is considered certified if all its exported Type-from/to-edn
   pairs pass the fuzzing protocol.
   
   Returns a map of Type -> CertificationMetadata."
  [ns-sym options]
  (tel/log! :info ["Certifying foreign namespace" ns-sym])
  (require ns-sym :reload)
  (let [source-hash (digest/compute-foreign-source-hash ns-sym)
        vars (u/foreign-vars ns-sym)
        registry (try (ns-resolve ns-sym 'registry) (catch Exception _ nil))
        registry-map (when registry @registry)
        types (->> vars
                   (keep #(second (re-find #"^(.+)-from-edn$" (name %))))
                   (filter #(contains? vars (symbol (str % "-to-edn"))))
                   (sort))]
    (if (empty? types)
      (do
        (tel/log! :warn ["No bindings found to certify in" ns-sym])
        {})
      (let [results (reduce
                      (fn [acc type-name]
                        (let [sk (keyword (name ns-sym) type-name)
                              _ (tel/log! :info ["Certifying foreign type" type-name "in" ns-sym])
                              ;; Check if schema exists, or try to register it from the registry map
                              _ (when-not (gen/generator sk)
                                  (if (and registry-map (contains? registry-map sk))
                                    (global/register-schema! sk (get registry-map sk))
                                    (tel/log! :warn ["Schema not found for" sk "and not in registry"])))
                              generator (try (gen/generator sk)
                                             (catch Exception e
                                               (tel/log! :error ["Failed to get generator for" sk (.getMessage e)])
                                               nil))
                              from-edn (ns-resolve ns-sym (symbol (str type-name "-from-edn")))
                              to-edn (ns-resolve ns-sym (symbol (str type-name "-to-edn")))
                              verify-fn (fn [edn]
                                          (let [obj (from-edn edn)
                                                rt-edn (to-edn obj)]
                                            (if (global/valid? sk rt-edn)
                                              true
                                              (do
                                                (tel/log! :error ["Validation Failed for foreign type" type-name (global/humanize (global/explain sk rt-edn))])
                                                false))))]
                          (if generator
                            (let [cert-res (run-certification-protocol sk generator verify-fn options)]
                              (assoc acc (keyword type-name) (assoc cert-res :source-hash source-hash)))
                            (do
                              (tel/log! :error ["Missing schema/generator for" type-name])
                              (assoc acc (keyword type-name) {:pass? false :reason :missing-schema})))))
                      {}
                      types)]
        (tel/log! :info ["Foreign namespace" ns-sym "certified" (keys results)])
        results))))

(defn update-foreign-namespace-certification
  "Updates the source file of a foreign namespace with certification metadata.
   Takes the namespace symbol and the certification results map."
  [ns-sym results]
  (if (every? #(not (false? (:pass? %))) (vals results))
    (let [path (str (u/get-gcp-repo-root) "/packages/global/src/" (string/replace (name ns-sym) #"\." "/") ".clj")
          source (slurp path)
          updated-source (u/update-ns-metadata source :gcp.dev/certification results)]
      (spit path updated-source)
      (tel/log! :info ["Updated source file" path "with certification metadata"])
      results)
    (do
      (tel/log! :error ["Certification failed for some types" (filter (comp false? :pass? val) results)])
      (throw (ex-info "Certification Failed" {:results results})))))
