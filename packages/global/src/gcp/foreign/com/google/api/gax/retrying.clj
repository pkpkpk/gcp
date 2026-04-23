(ns gcp.foreign.com.google.api.gax.retrying
  (:require [gcp.global :as g])
  (:import (java.time Duration)
           (com.google.api.gax.retrying RetrySettings RetrySettings$Builder ResultRetryAlgorithm)
           (java.time.temporal TemporalUnit)))

(def RetrySettings-schema
  [:map
   [:initialRetryDelay {:optional true} 'number?]
   [:initialRpcTimeout {:optional true} 'number?]
   [:maxAttempts {:optional true} 'number?]
   [:maxRetryDelay {:optional true} 'number?]
   [:maxRpcTimeout {:optional true} 'number?]
   [:retryDelayMultiplier {:optional true} 'number?]
   [:rpcTimeoutMultiplier {:optional true} 'number?]
   [:totalTimeout {:optional true} 'number?]])

(defn RetrySettings-to-edn [^RetrySettings arg]
  (cond-> {}
    (.getInitialRetryDelay arg)    (assoc :initialRetryDelay (.getInitialRetryDelay arg))
    (.getInitialRpcTimeout arg)    (assoc :initialRpcTimeout (.getInitialRpcTimeout arg))
    (.getMaxAttempts arg)          (assoc :maxAttempts (.getMaxAttempts arg))
    (.getMaxRetryDelay arg)        (assoc :maxRetryDelay (.getMaxRetryDelay arg))
    (.getMaxRpcTimeout arg)        (assoc :maxRpcTimeout (.getMaxRpcTimeout arg))
    (.getRetryDelayMultiplier arg) (assoc :retryDelayMultiplier (.getRetryDelayMultiplier arg))
    (.getRpcTimeoutMultiplier arg) (assoc :rpcTimeoutMultiplier (.getRpcTimeoutMultiplier arg))
    (.getTotalTimeout arg)         (assoc :totalTimeout (.getTotalTimeout arg))))

(defn ^RetrySettings RetrySettings-from-edn [arg]
  (let [builder (RetrySettings/newBuilder)]
    (when (contains? arg :initialRetryDelay)
      (.setInitialRetryDelayDuration builder (Duration/ofSeconds (:initialRetryDelay arg))))
    (when (contains? arg :maxRetryDelay)
      (.setMaxRetryDelayDuration builder (Duration/ofSeconds (:maxRetryDelay arg))))
    (when (contains? arg :initialRpcTimeout)
      (.setInitialRpcTimeoutDuration builder (Duration/ofSeconds (:initialRpcTimeout arg))))
    (when (contains? arg :totalTimeout)
      (.setTotalTimeoutDuration builder (Duration/ofSeconds (:totalTimeout arg))))
    (when (contains? arg :maxRpcTimeout)
      (.setMaxRpcTimeoutDuration builder (Duration/ofSeconds (:maxRpcTimeout arg))))
    ;;
    (when (contains? arg :maxAttempts)
      (.setMaxAttempts builder (int (:maxAttempts arg))))
    (when (contains? arg :retryDelayMultiplier)
      (.setRetryDelayMultiplier builder (double (:retryDelayMultiplier arg))))
    (when (contains? arg :rpcTimeoutMultiplier)
      (.setRpcTimeoutMultiplier builder (double (:rpcTimeoutMultiplier arg))))

    (.build builder)))

(def registry
  (with-meta
    {::RetrySettings RetrySettings-schema}
    {::g/name :gcp.foreign.com.google.api.gax.retrying/registry}))

(g/include-schema-registry! registry)