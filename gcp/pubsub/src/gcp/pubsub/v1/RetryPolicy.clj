(ns gcp.pubsub.v1.RetryPolicy
  (:require [gcp.global :as g]
            [gcp.protobuf :as p])
  (:import (com.google.pubsub.v1 RetryPolicy)))

;https://cloud.google.com/java/docs/reference/google-cloud-pubsub/latest/com.google.pubsub.v1.RetryPolicy.Builder

(defn to-edn [^RetryPolicy arg]
  (cond-> {}
          (some? (.getMinimumBackoff arg))
          (assoc :minimumBackoff (.getMinimumBackoff arg))
          (some? (.getMaximumBackoff arg))
          (assoc :maximumBackoff (.getMaximumBackoff arg))))

(defn ^RetryPolicy from-edn [arg]
  (let [builder (RetryPolicy/newBuilder)]
    (when (contains? arg :minimumBackoff)
      (.setMinimumBackoff builder (p/Duration-from-edn (:minimumBackoff arg))))
    (when (contains? arg :maximumBackoff)
      (.setMaximumBackoff builder (p/Duration-from-edn (:maximumBackoff arg))))
    (.build builder)))