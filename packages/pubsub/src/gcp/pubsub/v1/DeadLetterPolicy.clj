(ns gcp.pubsub.v1.DeadLetterPolicy
  (:require [gcp.global :as global]
            [gcp.pubsub.v1.Topic]
            [gcp.pubsub.v1.TopicName :as TopicName])
  (:import (com.google.pubsub.v1 DeadLetterPolicy)))

;;https://cloud.google.com/java/docs/reference/google-cloud-pubsub/latest/com.google.pubsub.v1.DeadLetterPolicy.Builder

(defn to-edn [^DeadLetterPolicy arg]
  {:post [(global/strict! :gcp.pubsub.v1/DeadLetterPolicy %)]}
  {:topic (.getDeadLetterTopic arg)
   :maxDeliveryAttempts (.getMaxDeliveryAttempts arg)})

(defn ^DeadLetterPolicy from-edn [arg]
  (global/strict! :gcp.pubsub.v1/DeadLetterPolicy arg)
  (let [builder (DeadLetterPolicy/newBuilder)]
    (when-let [topic (:topic arg)]
      (if (map? topic)
        (.setDeadLetterTopic builder (str (TopicName/from-edn topic)))
        (.setDeadLetterTopic builder topic)))
    (when (contains? arg :maxDeliveryAttempts)
      (.setMaxDeliveryAttempts builder (:maxDeliveryAttempts arg)))
    (.build builder)))

(def schemas
  {:gcp.pubsub.v1/DeadLetterPolicy
   [:map
    [:maxDeliveryAttempts :int]
    [:topic [:or
             :gcp.pubsub.v1/synth.TopicPath
             :gcp.pubsub.v1/TopicName]]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))