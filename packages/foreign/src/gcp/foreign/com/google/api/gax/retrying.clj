(ns gcp.foreign.com.google.api.gax.retrying
  {:gcp.dev/certification
   {:RetrySettings
    {:protocol-hash "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
     :base-seed 1767570246927
     :timestamp "2026-01-04T23:44:06.966889344Z"
     :passed-stages {:smoke 1767570246927
                     :standard 1767570246928
                     :stress 1767570246929}
     :source-hash "0343e1776970fb9bfc71c09e039362d5b86fb80cc6505baa561bff014bb02a49"}}}
  (:require [gcp.global :as global]
            [gcp.foreign.org.threeten.bp :as bp])
  (:import (com.google.api.gax.retrying RetrySettings RetrySettings$Builder)))

(def registry
  (let [d-map (fn [min-s max-s]
                [:map
                 [:seconds [:int {:min min-s :max max-s}]]
                 [:nanos [:int {:min 0 :max 999999999}]]])]
    (with-meta
      {:gcp.foreign.com.google.api.gax.retrying/RetrySettings
       [:map
        [:initialRetryDelay (d-map 0 1)]
        [:initialRpcTimeout (d-map 0 1)]
        [:maxAttempts [:int {:min 1 :max 10}]]
        [:maxRetryDelay (d-map 2 5)]
        [:maxRpcTimeout (d-map 2 5)]
        [:retryDelayMultiplier [:double {:min 1.0 :max 2.0}]]
        [:rpcTimeoutMultiplier [:double {:min 1.0 :max 2.0}]]
        [:totalTimeout (d-map 6 10)]]}
      {:gcp.global/name :gcp.foreign.com.google.api.gax.retrying/registry})))

(global/include-schema-registry! registry)

; https://cloud.google.com/java/docs/reference/gax/latest/com.google.api.gax.retrying.RetrySettings

(defn RetrySettings-to-edn [^RetrySettings arg]
  {:initialRetryDelay        (bp/Duration-to-edn (.getInitialRetryDelay arg))
   :initialRpcTimeout        (bp/Duration-to-edn (.getInitialRpcTimeout arg))
   :maxAttempts              (.getMaxAttempts arg)
   :maxRetryDelay            (bp/Duration-to-edn (.getMaxRetryDelay arg))
   :maxRpcTimeout            (bp/Duration-to-edn (.getMaxRpcTimeout arg))
   :retryDelayMultiplier     (.getRetryDelayMultiplier arg)
   :rpcTimeoutMultiplier     (.getRpcTimeoutMultiplier arg)
   :totalTimeout             (bp/Duration-to-edn (.getTotalTimeout arg))})

(defn ^RetrySettings RetrySettings-from-edn
  ([arg]
   (RetrySettings-from-edn (RetrySettings/newBuilder) arg))
  ([builder arg]
   (assert (instance? RetrySettings$Builder builder))
   (when (contains? arg :initialRetryDelay)
     (.setInitialRetryDelay builder (bp/Duration-from-edn (:initialRetryDelay arg))))
   (when (contains? arg :initialRpcTimeout)
     (.setInitialRpcTimeout builder (bp/Duration-from-edn (:initialRpcTimeout arg))))
   (when (contains? arg :maxAttempts)
     (.setMaxAttempts builder (:maxAttempts arg)))
   (when (contains? arg :maxRetryDelay)
     (.setMaxRetryDelay builder (bp/Duration-from-edn (:maxRetryDelay arg))))
   (when (contains? arg :maxRpcTimeout)
     (.setMaxRpcTimeout builder (bp/Duration-from-edn (:maxRpcTimeout arg))))
   (when (contains? arg :retryDelayMultiplier)
     (.setRetryDelayMultiplier builder (:retryDelayMultiplier arg)))
   (when (contains? arg :rpcTimeoutMultiplier)
     (.setRpcTimeoutMultiplier builder (:rpcTimeoutMultiplier arg)))
   (when (contains? arg :totalTimeout)
     (.setTotalTimeout builder (bp/Duration-from-edn (:totalTimeout arg))))
   (.build builder)))
