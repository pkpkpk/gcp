(ns gcp.dev.toolchain.fuzz
  "Toolchain component for certifying generated bindings via property-based fuzzing.
   Implements a multi-stage fuzz protocol to verify correctness before emission."
  (:require
   [clojure.test.check :as tc]
   [clojure.test.check.properties :as prop]
   [gcp.dev.fuzz.generators :as fg]
   [gcp.dev.packages :as pkg]
   [gcp.dev.toolchain.analyzer :as ana]
   [gcp.dev.toolchain.emitter :as emitter]
   [gcp.dev.util :as u]
   [gcp.gen :as gen]
   [gcp.global :as global]
   [gcp.protobuf]
   [taoensso.telemere :as tel])
  (:import
   (java.util.concurrent CancellationException)))

(def PROTOCOL-STAGES
  "Standard certification protocol stages."
  [{:name :smoke    :tests 10  :max-size 10}
   {:name :standard :tests 50  :max-size 30}
   {:name :stress   :tests 100 :max-size 50}])

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
  [property {:keys [num-tests max-size seed timeout-ms] :or {timeout-ms 10000}}]
  (let [seed (or seed (System/currentTimeMillis))
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
          _ (println "DEBUG: Schema Key:" sk)
          generator (gen/generator sk)
          _ (println "DEBUG: Generator type:" (class generator))
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
      (loop [stages PROTOCOL-STAGES
             history []]
        (if-let [stage (first stages)]
          (let [stage-seed (+ base-seed (count history)) ;; Deterministic seed offset per stage
                res (check property (assoc stage :seed stage-seed :timeout-ms timeout-ms))]
            (if (:pass? res)
              (recur (rest stages) (conj history (merge stage {:seed stage-seed :result :pass})))
              (do
                (println "DEBUG: Certification Stage Failed:" stage)
                (println "DEBUG: Result:" res)
                (tel/log! :error ["Certification Stage Failed" stage res])
                (throw (ex-info "Certification Failed" {:stage stage :result res})))))
          ;; All Passed
          {:gcp.dev/certification
           {:protocol-version "v1"
            :base-seed base-seed
            :timestamp (str (java.time.Instant/now))
            :stages history}})))))
