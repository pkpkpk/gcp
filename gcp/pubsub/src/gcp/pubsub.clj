(ns gcp.pubsub
  (:require [gcp.global :as g]
            [gcp.pubsub.v1.GetSubscriptionRequest :as GetSubscriptionRequest]
            [gcp.pubsub.v1.ListTopicsRequest :as ListTopicsRequest]
            [gcp.pubsub.v1.ListTopicSubscriptionsRequest :as ListTopicSubscriptionsRequest]
            [gcp.pubsub.v1.Subscription :as Subscription]
            [gcp.pubsub.v1.SubscriptionAdminClient :as SAC]
            [gcp.pubsub.v1.TopicAdminClient :as TAC]
            [gcp.pubsub.v1.ProjectName :as ProjectName]
            [gcp.pubsub.v1.SubscriptionName :as SubscriptionName]
            [gcp.pubsub.v1.TopicName :as TopicName])
  (:import (com.google.api.gax.rpc NotFoundException)
           (com.google.cloud.pubsub.v1 SubscriptionAdminClient TopicAdminClient)))

(defonce ^:dynamic *subscription-admin-client* nil)

(defn subscription-admin-client
  ([] (subscription-admin-client nil))
  ([arg]
   (or *subscription-admin-client*
       (if (instance? SubscriptionAdminClient arg)
         arg
         (g/client :gcp/pubsub.SubscriptionAdminClient arg)))))

(defonce ^:dynamic *topic-admin-client* nil)

(defn topic-admin-client
  ([] (topic-admin-client nil))
  ([arg]
   (or *topic-admin-client*
       (if (instance? TopicAdminClient arg)
         arg
         (g/client :gcp/pubsub.TopicAdminClient arg)))))

#!----------------------------------------------------------------------------------------------------------------------
#! Topic Administration

(defn list-topics
  ([arg]
   (if (string? arg)
     (list-topics {:project arg})
     (let [{:keys [topicAdminClient project request]} (g/coerce :gcp/pubsub.synth.TopicList arg)
           response (if project
                      (.listTopics (topic-admin-client topicAdminClient) (ProjectName/from-edn project))
                      (.listTopics (topic-admin-client topicAdminClient) (ListTopicsRequest/from-edn request)))]
       (TAC/ListTopicsPagedResponse-to-edn response))))
  ([arg0 arg1]
   ;; TODO clientable, project|request
   (throw (Exception. "unimplemented"))))

#_
(let [topic-name (TopicName/ofProjectTopicName project-id topic-id)]
  (try
    (.getTopic client ^TopicName topic-name)
    (catch NotFoundException e
      nil)))
;getTopic(GetTopicRequest request)
;getTopic(ProjectTopicName topic) (deprecated)
;getTopic(TopicName topic)
;getTopic(String topic)
(defn get-topic
  ([arg]
   (throw (Exception. "unimplemented")))
  ([arg0 arg1]
   (throw (Exception. "unimplemented")))
  ([arg0 arg1 arg2]
   (throw (Exception. "unimplemented"))))

;createTopic(ProjectTopicName name) (deprecated)
;createTopic(Topic request)
;createTopic(TopicName name)
;createTopic(String name)
(defn create-topic
  ([arg]
   (throw (Exception. "unimplemented")))
  ([arg0 arg1]
   (throw (Exception. "unimplemented"))))

(defn list-topic-subscriptions
  ([arg]
   (if (g/valid? :gcp/pubsub.TopicName arg)
     (list-topic-subscriptions {:topicName arg})
     (let [{:keys [topicAdminClient topicName request]} (g/coerce :gcp/pubsub.synth.TopicListSubscriptions arg)
           client (topic-admin-client topicAdminClient)
           response (if topicName
                      (.listTopicSubscriptions client (TopicName/from-edn topicName))
                      (.listTopicSubscriptions client (ListTopicSubscriptionsRequest/from-edn request)))]
       (TAC/ListTopicSubscriptionsPagedResponse-to-edn response))))
  ([arg0 arg1]
   (if (string? arg0)
     (if (string? arg1)
       (list-topic-subscriptions {:topicName {:project arg0 :topic arg1}})))))

#!----------------------------------------------------------------------------------------------------------------------
#! Subscription Administration


;listSubscriptions(ListSubscriptionsRequest request)
;listSubscriptions(ProjectName project)
;listSubscriptions(String project)
(defn list-subscriptions
  ([arg]
   (if (string? arg)
     (list-subscriptions {:projectName arg})
     (let [{:keys [subscriptionAdminClient projectName request]} (g/coerce :gcp/pubsub.synth.SubscriptionList arg)
           client   (subscription-admin-client subscriptionAdminClient)
           response (if projectName
                      (.listSubscriptions client (ProjectName/from-edn projectName))
                      (.listSubscriptions client (ProjectName/from-edn request)))]
       (SAC/ListSubscriptionsPagedResponse-to-edn response))))
  ([arg0 arg1]
   ;;TODO clientable, project|request
   (throw (Exception. "unimplemented"))))

(defn get-subscription
  ([arg]
   (let [{:keys [subscriptionAdminClient subscription request]} (g/coerce :gcp/pubsub.synth.SubscriptionGet arg)
         client (subscription-admin-client subscriptionAdminClient)]
     (try
       (Subscription/to-edn
         (if request
           (.getSubscription client (GetSubscriptionRequest/from-edn request))
           (.getSubscription client (SubscriptionName/from-edn subscription))))
       (catch NotFoundException _
         nil))))
  ([arg0 arg1]
   (if (string? arg0)
     (if (string? arg1)
       (get-subscription {:subscription {:project arg0 :subscription arg1}})))))

(defn create-subscription
  ([arg]
   (if (g/valid? :gcp/pubsub.Subscription arg)
     (create-subscription {:subscription arg})
     (let [{:keys [subscription-admin subscription]} (g/coerce :gcp/pubsub.synth.SubscriptionCreate arg)
           client (subscription-admin-client subscription-admin)]
       (.createSubscription client (Subscription/from-edn subscription)))))
  ([arg0 arg1]
   (throw (Exception. "unimplemented"))))