(ns gcp.gax.retrying.RetrySettings
  (:import
   (com.google.api.gax.retrying RetrySettings RetrySettings$Builder)))

; https://cloud.google.com/java/docs/reference/gax/latest/com.google.api.gax.retrying.RetrySettings
#_ (do (require :reload 'gcp.gax.retrying.RetrySettings) (in-ns 'gcp.gax.retrying.RetrySettings))

(defn to-edn [^RetrySettings arg]
  {:initialRetryDelay        (.getInitialRetryDelay arg)
   :initialRpcTimeout        (.getInitialRpcTimeout arg)
   :maxAttempts              (.getMaxAttempts arg)
   :maxRetryDelay            (.getMaxRetryDelay arg)
   :maxRpcTimeout            (.getMaxRpcTimeout arg)
   :retryDelayMultiplier     (.getRetryDelayMultiplier arg)
   :rpcTimeoutMultiplier     (.getRpcTimeoutMultiplier arg)
   :totalTimeout             (.getTotalTimeout arg)})

(defn ^RetrySettings from-edn
  ([arg]
   (from-edn (RetrySettings/newBuilder) arg))
  ([builder arg]
   (assert (instance? RetrySettings$Builder builder))
   (when (contains? arg :initialRetryDelay)
     (.setInitialRetryDelayDuration builder (:initialRetryDelay arg)))
   (when (contains? arg :initialRpcTimeout)
     (.setInitialRpcTimeoutDuration builder (:initialRpcTimeout arg)))
   (when (contains? arg :maxAttempts)
     (.setMaxAttempts builder (:maxAttempts arg)))
   (when (contains? arg :maxRetryDelay)
     (.setMaxRetryDelayDuration builder (:maxRetryDelay arg)))
   (when (contains? arg :maxRpcTimeout)
     (.setMaxRpcTimeoutDuration builder (:maxRpcTimeout arg)))
   (when (contains? arg :retryDelayMultiplier)
     (.setRetryDelayMultiplier builder (:retryDelayMultiplier arg)))
   (when (contains? arg :rpcTimeoutMultiplier)
     (.setRpcTimeoutMultiplier builder (:rpcTimeoutMultiplier arg)))
   (when (contains? arg :totalTimeout)
     (.setTotalTimeoutDuration builder (:totalTimeout arg)))
   (.build builder)))
