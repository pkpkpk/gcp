(ns gcp.dev.toolchain.fuzz
  "Toolchain component for certifying generated bindings via property-based fuzzing.
   Implements a multi-stage fuzz protocol to verify correctness before emission."
  (:require
   [clojure.test.check :as tc]
   [clojure.test.check.properties :as prop]
   [gcp.dev.digest :as digest]
   [gcp.dev.packages :as pkg]
   [gcp.dev.toolchain.analyzer :as ana]
   [gcp.dev.toolchain.emitter :as emitter]
   [gcp.dev.toolchain.fuzz.generators :as fg]
   [gcp.dev.util :as u]
   [gcp.gen :as gen]
   [gcp.global :as global]
   [gcp.protobuf]
   [taoensso.telemere :as tel])
  (:import
   (java.util.concurrent CancellationException)))

(def CERTIFICATION_PROTOCOL
  {:version "v1"
   :stages [{:name :smoke    :tests 10  :max-size 10}
            {:name :standard :tests 50  :max-size 30}
            {:name :stress   :tests 100 :max-size 50}]})

(def CERTIFICATION_HASH
  (digest/sha256 (pr-str CERTIFICATION_PROTOCOL)))

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
      (let [node (pkg/lookup-class pkg-key dep)
            ana-node (ana/analyze-class-node node)
            forms (emitter/compile-class-forms ana-node)]
        (load-class-forms! dep forms)))))

(defn certify-class
  "Runs the certification protocol on a class.
   Returns the certification metadata map if successful, throws otherwise.
   
   Protocol:
   1. Compiles and loads dependencies.
   2. Compiles and loads the target class (in-memory).
   3. Runs defined fuzz stages sequentially.
   4. Accumulates results."
  [pkg-key fqcn {:keys [seed timeout-ms] :or {timeout-ms 60000}}]
  (let [original-ns *ns*]
    (try
      (tel/log! :info ["Certifying" fqcn "with seed" seed])
      ;; 1. Load Dependencies
      (load-dependencies! pkg-key fqcn)
      ;; 2. Load Target (fresh compilation)
      (let [node (pkg/lookup-class pkg-key fqcn)
            ana-node (ana/analyze-class-node node)
            forms (emitter/compile-class-forms ana-node)]
        (load-class-forms! fqcn forms)
        ;; 3. Run Protocol
        (let [version (u/extract-version (:doc ana-node))
              sk (u/schema-key (:package ana-node) (:className ana-node) version)
              generator (gen/generator sk)
              ;; Verification Function
              verify-fn (fn [edn]
                          (let [ns-sym (symbol (str (u/package-to-ns (:package ana-node) version) "." (:className ana-node)))
                                from-edn (resolve (symbol (str ns-sym) "from-edn"))
                                to-edn (resolve (symbol (str ns-sym) "to-edn"))]
                            (if (and from-edn to-edn)
                              (let [obj (from-edn edn)
                                    rt-edn (to-edn obj)]
                                (if (global/valid? sk rt-edn)
                                  true
                                  (do
                                    (tel/log! :error ["Validation Failed" (global/humanize (global/geminize (global/explain sk rt-edn) sk))])
                                    false)))
                              (throw (ex-info "Missing functions" {:ns ns-sym})))))
              ;; Property
              property (prop/for-all [v generator] (verify-fn v))
              ;; Base Seed (if not provided, generate one)
              base-seed (or seed (System/currentTimeMillis))]
          (Thread/sleep 1)
          (tel/log! :info ["Starting fuzz stages"])
          (loop [stages (:stages CERTIFICATION_PROTOCOL)
                 history []]
            (if-let [stage (first stages)]              (let [stage-seed (+ base-seed (count history)) ;; Deterministic seed offset per stage
                                                              res (check property (assoc stage :seed stage-seed :timeout-ms timeout-ms))]
                                                          (if (:pass? res)
                                                            (do
                                                              (tel/log! :info ["Stage passed" (:name stage)])
                                                              (recur (rest stages) (conj history (merge stage {:seed stage-seed :result :pass}))))
                                                            (do
                                                              (tel/log! :error ["Certification Stage Failed" stage res])
                                                              (throw (ex-info "Certification Failed" {:stage stage :result res})))))
              ;; All Passed
              (do
                (tel/log! :info ["Certification passed for binding" fqcn])
                {:protocol-hash CERTIFICATION_HASH
                 :base-seed base-seed
                 :timestamp (str (java.time.Instant/now))
                 :passed-stages (into {} (map (juxt :name :seed) history))})))))
      (finally
        (in-ns (ns-name original-ns))))))
