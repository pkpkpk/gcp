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
   [gcp.global :as g]
   [malli.core :as m]
   [taoensso.telemere :as tel])
  (:import
   (java.time Instant)
   (java.util.concurrent CancellationException)))

(def CERTIFICATION_PROTOCOL
  {:version "v1"
   :stages [{:name :smoke    :tests 10  :max-size 10 :timeout-ms    5000}
            {:name :standard :tests 50  :max-size 20 :timeout-ms  120000}
            {:name :stress   :tests 100  :max-size 30 :timeout-ms  300000}]})

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

(def TRIAL_BATCH_SIZE 10)

(defn check
  [property options]
  (let [num-tests (or (:num-tests options) (:tests options) 100)
        max-size (or (:max-size options) 200)
        seed (or (:seed options) (System/currentTimeMillis))
        result (run-with-timeout (or (:timeout-ms options) 60000)
                 (fn []
                   (loop [tests-run 0
                          current-seed seed]
                     (let [chunk-size (min (- num-tests tests-run) TRIAL_BATCH_SIZE)
                           res (tc/quick-check chunk-size property
                                               :seed current-seed
                                               :max-size max-size)]
                       (if (:pass? res)
                         (let [total-run (+ tests-run chunk-size)]
                           (if (< total-run num-tests)
                             (do
                               (System/gc) ;; Clear young/tenured memory between trial chunks
                               (recur total-run (hash [current-seed total-run])))
                             (assoc res :num-tests total-run :seed seed)))
                         res)))))]
    (if (:pass? result)
      (assoc result :seed seed)
      (assoc result :seed seed :pass? false))))

(defn load-class-code! [fqcn code]
  (try
    (let [forms (edamame.core/parse-string-all code {:regex true})
          ns-form (first forms)
          _ (assert (and (seq? ns-form) (= 'ns (first ns-form))))
          ns-name (second ns-form)]
      (eval ns-form)
      (doseq [f (rest forms)]
        (eval `(do (in-ns '~(symbol (name ns-name))) ~f)))
      (require ns-name))
    (catch Exception e
      (tel/log! :error ["Failed to load code for" fqcn ":" (.getMessage e)])
      (throw e))))

(defn run-certification-protocol
  [sk generator verify-fn options]
  (let [base-seed (or (:seed options) (System/currentTimeMillis))
        property (prop/for-all [v generator] (verify-fn v))]
    (tel/log! :info ["Starting fuzz stages for" sk])
    (loop [stages (:stages CERTIFICATION_PROTOCOL)
           history []]
      (if-let [stage (first stages)]
        (let [stage-seed (+ base-seed (count history))
              res (check property (assoc stage :seed stage-seed))]
          (if (:pass? res)
            (do
              (tel/log! :info ["Stage passed" (:name stage) "for" sk])
              (System/gc) ;; Force cleanup of test.check garbage before the next (larger) stage
              (recur (rest stages) (conj history (merge stage {:seed stage-seed :result :pass}))))
            (do
              (tel/log! :error ["Certification Stage Failed" stage res "for" sk])
              (throw (ex-info "Certification Failed" {:stage stage :result res :schema sk})))))
        {:protocol-hash CERTIFICATION_HASH
         :base-seed base-seed
         :timestamp (str (Instant/now))
         :passed-stages (into {} (map (juxt :name :seed) history))}))))

(defn- read-only-category? [cat]
  (contains? #{:read-only :nested/read-only :interface :variant-read-only :nested/variant-read-only} cat))

(def ^:dynamic *visited-schemas* #{})

(defn- prune-read-only-walker [schema _ children options]
  (let [type (m/type schema)
        props (m/properties schema)
        form (m/form schema options)]
    (cond
      (read-only-category? (:gcp/category props)) ::pruned

      (keyword? form)
      (if (read-only-category? (:gcp/category (g/properties form)))
        ::pruned
        (if (*visited-schemas* form)
          schema
          (if-let [child (g/get-schema form)]
            (binding [*visited-schemas* (conj *visited-schemas* form)]
              (m/walk child prune-read-only-walker options))
            schema)))

      (= :ref type)
      (let [key (if (map? (second form)) (nth form 2) (second form))]
        (if (and (keyword? key)
                 (read-only-category? (:gcp/category (g/properties key))))
          ::pruned
          (if (*visited-schemas* key)
            schema
            (if-let [child (g/get-schema key)]
              (binding [*visited-schemas* (conj *visited-schemas* key)]
                (m/walk child prune-read-only-walker options))
              schema))))

      (= :map type)
      (let [entries (filter #(not= (last %) ::pruned) children)]
        (m/into-schema type props entries options))

      :else
      (if (some #{::pruned} children)
        (case type
          (:vector :list :set :seqable :map-of :tuple) ::pruned
          :maybe [:enum nil]
          (:or :and)
          (let [valid (remove #{::pruned} children)]
            (if (empty? valid)
              ::pruned
              (m/into-schema type props valid options)))
          ::pruned)
        (m/into-schema type props children options)))))

(defn prune-read-only-schema [schema]
  (try
    (binding [*visited-schemas* #{}]
      (let [res (m/walk schema prune-read-only-walker (g/mopts))]
        (if (= res ::pruned) [:enum nil] res)))
    (catch Exception e
      (throw (ex-info (str "failed to prune schema: " (ex-message e))
                      {:schema schema
                       :cause e})))))

(defn- ns-sym->schema-key [ns-sym]
  (let [s (str ns-sym)
        last-dot (string/last-index-of s ".")
        ns (-> (subs s 0 last-dot)
               (string/replace ".custom" "")
               (string/replace ".bindings" ""))
        class (subs s (inc last-dot))]
    (keyword ns class)))

(defn certified?
  "Checks if the file at path is already certified.
   Returns the certification metadata if valid, nil otherwise."
  [file-or-path]
  (try
    (let [content (slurp (io/file file-or-path))
          meta (u/source-ns-meta content)]
      (:gcp.dev/certification meta))
    (catch Exception _ nil)))

(defn certification-stale?
  "Checks if the certification for the file is stale compared to the package manifest.
   Returns true if stale or uncertified, false if up-to-date."
  [file-or-path]
  (if-let [meta (certified? file-or-path)]
    (let [file    (io/file file-or-path)
          fqcn    (pkg/target-file->fqcn file)
          pkg-key (pkg/lookup-pkg-key fqcn)]
      (if pkg-key
        (let [manifest (pkg/manifest pkg-key)
              current-hash (u/hasch manifest)
              file-hash (:manifest meta)]
          (not= current-hash file-hash))
        true)) ;; Unknown package, assume stale
    true)) ;; Not certified

(defn certify-file
  "Certifies a generated file by loading it and running the fuzz certification protocol.
   Accepts a java.io.File or path string.
   On success, updates the file with certification metadata including manifest hash."
  ([file-or-path]
   (certify-file file-or-path nil))
  ([file-or-path options]
   (let [file    (io/file file-or-path)
         content (slurp file)
         ns-form (first (edamame.core/parse-string-all content {:regex true}))
         _       (assert (and (seq? ns-form) (= 'ns (first ns-form))) "File must start with ns declaration")
         ns-sym  (second ns-form)
         sk      (ns-sym->schema-key ns-sym)
         _ (println "schema-key: " sk)
         fqcn    (or (pkg/target-file->fqcn file)
                     (:fqcn (u/source-ns-meta content)))
         _ (assert (some? fqcn) (str "Could not determine fqcn for file: " file))
         _ (println "Certifying file..." file "for" fqcn)
         pkg-key (pkg/lookup-pkg-key fqcn)]
     (assert (some? pkg-key) (str "could not determine package-key for fqcn:" fqcn))
     (println "pkg:" pkg-key)
     (if (and pkg-key fqcn)
       (let [original-ns *ns*]
         (try
           (println "requiring " ns-sym)
           (require :reload ns-sym)
           (println "successfully required " ns-sym)
           (let [sk-props (g/properties sk)
                 top-level-cat (:gcp/category sk-props)]
             (if (read-only-category? top-level-cat)
               (do
                 (tel/log! :info ["Skipping certification for read-only file" file])
                 (let [manifest (pkg/manifest pkg-key)
                       manifest-hash (u/hasch manifest)
                       results {:protocol-hash CERTIFICATION_HASH
                                :base-seed 0
                                :timestamp (str (Instant/now))
                                :skipped true
                                :reason :read-only
                                :manifest manifest-hash}
                       sorted-results (into (sorted-map) results)
                       updated-source (u/update-ns-metadata content :gcp.dev/certification sorted-results)]
                   (spit file updated-source)
                   sorted-results))
               (binding [g/*registry* (fg/test-registry)]
                 (let [schema        (g/get-schema sk)
                       _             (println "pruning schema" sk)
                       pruned-schema (prune-read-only-schema schema)
                       _             (println "constructing generator for pruned schema")
                       generator     (try
                                       (gen/generator pruned-schema {:gen/elements 3})
                                       (catch Exception e
                                         (if (= ":malli.generator/no-generator" (ex-message e))
                                           (let [ex (ex-info (str "missing generator for schema") {:schema (:schema (:data (ex-data e)))})]
                                             (throw ex))
                                           (throw e))))
                       _             (println "successfully built generator")
                       from-edn      (ns-resolve ns-sym 'from-edn)
                       to-edn        (ns-resolve ns-sym 'to-edn)
                       _ (when-not (and from-edn to-edn)
                           (throw (ex-info "Missing functions" {:ns ns-sym})))
                       verify-fn     (fn [edn]
                                       (let [obj    (from-edn edn)
                                             rt-edn (to-edn obj)]
                                         (if (g/valid? sk rt-edn)
                                           true
                                           (do
                                             (tel/log! :error ["Validation Failed" (g/humanize (g/explain sk rt-edn))])
                                             false))))]
                   (let [manifest              (pkg/manifest pkg-key)
                         manifest-hash         (u/hasch manifest)
                         _                     (println "starting certification protocol")
                         results               (run-certification-protocol sk generator verify-fn options)
                         results-with-manifest (assoc results :manifest manifest-hash)
                         ;; Convert to sorted-map for deterministic output
                         sorted-results        (into (sorted-map) results-with-manifest)
                         ;; Update file metadata
                         updated-source        (u/update-ns-metadata content :gcp.dev/certification sorted-results)]
                     (spit file updated-source)
                     sorted-results)))))
           (finally
             (in-ns (ns-name original-ns)))))
       (throw (ex-info "Could not map file to known package/FQCN" {:file file :ns ns-sym :sk sk}))))))

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
                                    (g/register-schema! sk (get registry-map sk))
                                    (tel/log! :warn ["Schema not found for" sk "and not in registry"])))
                              sk-props (g/properties sk)
                              top-level-cat (:gcp/category sk-props)
                              generator (if (read-only-category? top-level-cat)
                                          nil
                                          (try (let [schema (g/schema sk)
                                                     pruned (prune-read-only-schema schema)]
                                                 (gen/generator pruned {:gen/elements 3}))
                                               (catch Exception e
                                                 (tel/log! :error ["Failed to get generator for" sk (.getMessage e)])
                                                 nil)))
                              from-edn (ns-resolve ns-sym (symbol (str type-name "-from-edn")))
                              to-edn (ns-resolve ns-sym (symbol (str type-name "-to-edn")))
                              verify-fn (fn [edn]
                                          (let [obj (from-edn edn)
                                                rt-edn (to-edn obj)]
                                            (if (g/valid? sk rt-edn)
                                              true
                                              (do
                                                (tel/log! :error ["Validation Failed for foreign type" type-name (g/humanize (g/explain sk rt-edn))])
                                                false))))]
                          (if (and (not (read-only-category? top-level-cat)) (nil? generator))
                            (do
                              (tel/log! :error ["Missing schema/generator for" type-name])
                              (assoc acc (keyword type-name) {:pass? false :reason :missing-schema}))
                            (if (read-only-category? top-level-cat)
                              (do
                                (tel/log! :info ["Skipping foreign read-only type" type-name])
                                (assoc acc (keyword type-name) {:protocol-hash CERTIFICATION_HASH
                                                                :base-seed 0
                                                                :timestamp (str (Instant/now))
                                                                :skipped true
                                                                :reason :read-only
                                                                :source-hash source-hash}))
                              (let [cert-res (run-certification-protocol sk generator verify-fn options)]
                                (assoc acc (keyword type-name) (assoc cert-res :source-hash source-hash)))))))
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
