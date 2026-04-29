(ns gcp.pubsub
  (:require
   [clojure.string :as string]
   [gcp.global :as g]
   [gcp.pubsub.SubscriptionAdmin :as SubscriptionAdmin]
   [gcp.pubsub.TopicAdmin :as TopicAdmin]))

(defonce ^:dynamic *subscription-admin-client* nil)

(def subscription-admin-client SubscriptionAdmin/client)

(def topic-admin-client TopicAdmin/client)

#!----------------------------------------------------------------------------------------------------------------------
#! Topic Administration

(defn list-topics
  "(call-record)
   (project)
   (ListTopicsRequest)
   (clientable, project)
   (clientable, ListTopicsRequest)"
  [& args]
  (TopicAdmin/execute! (TopicAdmin/->TopicList (vec args))))

(defn get-topic
  [& args]
  (TopicAdmin/execute! (TopicAdmin/->TopicGet (vec args))))

(defn list-topic-subscriptions
  [& args]
  (TopicAdmin/execute! (TopicAdmin/->TopicListSubscriptions (vec args))))

(defn create-topic
  "(call-record)
   (Topic)
   (clientable, Topic)
   (TopicName)
   (clientable, TopicName)
   (project, topic)
   (clientable, project, topic)
   (topicNameResourceString)
   (clientable, topicNameResourceString)"
  [& args]
  (TopicAdmin/execute! (TopicAdmin/->TopicCreate (vec args))))

(defn delete-topic
  "(call-record)
   (project, topic)
   (clientable, project, topic)
   (topicName)
   (clientable, topicName)
   (topicNameResourceString)
   (clientable, topicNameResourceString)
   (Topic)
   (clientable, Topic)"
  [& args]
  (TopicAdmin/execute! (TopicAdmin/->TopicDelete (vec args))))

#!----------------------------------------------------------------------------------------------------------------------
#! Subscription Administration

(defn list-subscriptions
  [& args]
  (SubscriptionAdmin/execute! (SubscriptionAdmin/->SubscriptionList (vec args))))

(defn get-subscription
  [& args]
  (SubscriptionAdmin/execute! (SubscriptionAdmin/->SubscriptionGet (vec args))))

(defn create-subscription
  [& args]
  (SubscriptionAdmin/execute! (SubscriptionAdmin/->SubscriptionCreate (vec args))))

(defn delete-subscription
  [& args]
  (SubscriptionAdmin/execute! (SubscriptionAdmin/->SubscriptionDelete (vec args))))
