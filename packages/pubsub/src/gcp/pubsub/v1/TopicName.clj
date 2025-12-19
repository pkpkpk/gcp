(ns gcp.pubsub.v1.TopicName
  (:require [gcp.global :as global])
  (:import (com.google.pubsub.v1 TopicName)))

(defn to-edn [^TopicName arg])

(defn ^TopicName from-edn
  [arg]
  (global/strict! :gcp.pubsub.v1/TopicName arg)
  (let [builder (TopicName/newBuilder)]
    (some->> (:project arg) (.setProject builder))
    (some->> (:topic arg) (.setTopic builder))
    (.build builder)))

(def schemas
  {:gcp.pubsub.v1/TopicName
   [:map {:closed true}
    [:project :string]
    [:topic :string]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))