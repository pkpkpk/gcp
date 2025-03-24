(ns gcp.pubsub.v1.DeadLetterPolicy
  (:require [gcp.global :as g])
  (:import (com.google.pubsub.v1 DeadLetterPolicy)))

;;https://cloud.google.com/java/docs/reference/google-cloud-pubsub/latest/com.google.pubsub.v1.DeadLetterPolicy.Builder

(defn to-edn [^DeadLetterPolicy arg]
  {:post [(g/strict! :gcp/pubsub.DeadLetterPolicy %)]}
  {:topic (.getDeadLetterTopic arg)
   :maxDeliveryAttempts (.getMaxDeliveryAttempts arg)})

(defn ^DeadLetterPolicy from-edn [arg]
  (g/strict! :gcp/pubsub.DeadLetterPolicy arg)
  (let [builder (DeadLetterPolicy/newBuilder)]
    (when (contains? arg :topic)
      ;(str "projects/" project-id "/topics/" topic-id)
      (.setDeadLetterTopic arg (:topic arg)))
    (when (contains? arg :maxDeliveryAttempts)
      (.setMaxDeliveryAttempts builder (:maxDeliveryAttempts arg)))
    (.build builder)))