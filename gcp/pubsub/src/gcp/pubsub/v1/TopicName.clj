(ns gcp.pubsub.v1.TopicName
  (:require [gcp.global :as g])
  (:import (com.google.pubsub.v1 TopicName)))

(defn to-edn [^TopicName arg])

(defn ^TopicName from-edn
  [arg]
  (g/strict! :gcp/pubsub.TopicName arg)
  (let [builder (TopicName/newBuilder)]
    (some->> (:project arg) (.setProject builder))
    (some->> (:topic arg) (.setTopic builder))
    (.build builder)))