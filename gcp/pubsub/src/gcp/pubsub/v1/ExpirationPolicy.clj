(ns gcp.pubsub.v1.ExpirationPolicy
  (:require [gcp.global :as g]
            [gcp.protobuf :as p])
  (:import (com.google.pubsub.v1 ExpirationPolicy)))

;https://cloud.google.com/java/docs/reference/google-cloud-pubsub/latest/com.google.pubsub.v1.ExpirationPolicy.Builder

(defn to-edn [^ExpirationPolicy arg]
  (cond-> {}
          (.hasTtl arg)
          (assoc :ttl (.getTtl arg))))

(defn ^ExpirationPolicy from-edn [arg]
  (let [builder (ExpirationPolicy/newBuilder)]
    (when (contains? arg :ttl)
      (.setTtl builder (p/Duration-from-edn (:ttl arg))))
    (.build builder)))