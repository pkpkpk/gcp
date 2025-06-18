(ns gcp.pubsub.v1.DeadLetterPolicy
  (:require [gcp.global :as g]
            [gcp.pubsub.v1.TopicName :as TopicName])
  (:import (com.google.pubsub.v1 DeadLetterPolicy)))

;;https://cloud.google.com/java/docs/reference/google-cloud-pubsub/latest/com.google.pubsub.v1.DeadLetterPolicy.Builder

(defn to-edn [^DeadLetterPolicy arg]
  {:post [(g/strict! :gcp/pubsub.DeadLetterPolicy %)]}
  {:topic (.getDeadLetterTopic arg)
   :maxDeliveryAttempts (.getMaxDeliveryAttempts arg)})

(defn ^DeadLetterPolicy from-edn [arg]
  (g/strict! :gcp/pubsub.DeadLetterPolicy arg)
  (let [builder (DeadLetterPolicy/newBuilder)]
    (when-let [topic (:topic arg)]
      (if (map? topic)
        (.setDeadLetterTopic builder (str (TopicName/from-edn topic)))
        (.setDeadLetterTopic builder topic)))
    (when (contains? arg :maxDeliveryAttempts)
      (.setMaxDeliveryAttempts builder (:maxDeliveryAttempts arg)))
    (.build builder)))