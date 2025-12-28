(ns gcp.dev.fuzz
  (:require [clojure.test.check :as tc]
            [clojure.test.check.properties :as prop]
            [gcp.dev.packages :as pkg]
            [gcp.dev.analyzer :as ana]
            [gcp.dev.compiler :as compiler]
            [gcp.dev.malli :as malli]
            [gcp.dev.fuzz.generators :as fg]
            [gcp.dev.util :as u]
            [gcp.gen :as gen]
            [gcp.global :as global]
            [gcp.protobuf] 
            [taoensso.telemere :as tel])
  (:import (java.util.concurrent CancellationException)))

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
  "Runs a property-based test with a timeout.
   
   Args:
     property: The test.check property to test.
     options: Map containing:
       :num-tests (default 100)
       :seed (optional)
       :max-size (optional)
       :timeout-ms (default 10000)"
  [property options]
  (let [num-tests (:num-tests options 100)
        timeout-ms (:timeout-ms options 10000)
        seed (:seed options)
        max-size (:max-size options 200)
        tc-opts (cond-> {:num-tests num-tests}
                  seed (assoc :seed seed)
                  max-size (assoc :max-size max-size))]
    (tel/log! :info ["Starting fuzz test" tc-opts])
    (let [result (run-with-timeout timeout-ms
                   (fn []
                     (tc/quick-check num-tests property 
                                     :seed (or seed (System/currentTimeMillis))
                                     :max-size max-size)))]
      (if (:pass? result)
        (do
          (tel/log! :info ["Fuzz test passed" (:num-tests result) "tests"])
          result)
        (do
          (tel/log! :error ["Fuzz test failed" result])
          result)))))

(defn check-schema
  "Helper to fuzz a schema's roundtrip capability or other invariants.
   
   Args:
     generator: The generator to use (e.g. from malli or custom).
     check-fn: Function that takes a generated value and returns true/false.
     options: Same as check."
  [generator check-fn options]
  (let [prop (prop/for-all [v generator]
               (check-fn v))]
    (check prop options)))

(defn load-class! [pkg-key fqcn]
  (tel/log! :info ["Compiling and loading" fqcn])
  (let [node (pkg/lookup-class pkg-key fqcn)
        ana-node (ana/analyze-class-node node)
        forms (compiler/compile-class-forms ana-node)]
    (try
      (eval (list* 'do forms))
      (catch Exception e
        (tel/log! :error ["Failed to eval forms for" fqcn ":" (.getMessage e)])
        (throw e)))))

(defn load-dependencies! [pkg-key fqcn]
  (let [deps (pkg/dependency-post-order pkg-key fqcn)]
    (tel/log! :info ["Loading dependencies for" fqcn ":" (count deps) "classes"])
    (doseq [dep (butlast deps)]
      (load-class! pkg-key dep))))

(defn fuzz-class
  "Performs roundtrip fuzz testing on a generated class.
   
   Args:
     pkg-key: Package keyword (e.g. :vertexai).
     fqcn: Fully qualified class name.
     options: Fuzz options (num-tests, timeout-ms, etc.)"
  [pkg-key fqcn options]
  (load-dependencies! pkg-key fqcn)
  (load-class! pkg-key fqcn)
  (let [node (ana/analyze-class-node (pkg/lookup-class pkg-key fqcn))
        version (u/extract-version (:doc node))
        sk (u/schema-key (:package node) (:className node) version)
        
        ;; Use gcp.gen which handles global mopts automatically
        generator (gen/generator sk)]
    
    (check-schema generator
      (fn [edn]
        (let [ns-sym (symbol (str (u/package-to-ns (:package node) version) "." (:className node)))
              from-edn-sym (symbol (str ns-sym) "from-edn")
              to-edn-sym (symbol (str ns-sym) "to-edn")
              from-edn (resolve from-edn-sym)
              to-edn (resolve to-edn-sym)]
          (if (and from-edn to-edn)
            (let [obj (from-edn edn)
                  rt-edn (to-edn obj)]
              (if (global/valid? sk rt-edn)
                true
                (let [explanation (global/explain sk rt-edn)
                      geminized (global/geminize explanation sk)
                      human (global/humanize geminized)]
                  (tel/log! :error ["Schema Validation Failed" {:human human :geminized geminized}])
                  false)))
            (do
              (tel/log! :error ["Could not resolve functions for" fqcn])
              false))))
      options)))
