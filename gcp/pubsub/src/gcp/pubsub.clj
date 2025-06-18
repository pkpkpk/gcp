(ns gcp.pubsub
  (:require [clojure.string :as string]
            [gcp.global :as g]
            gcp.pubsub.v1
            [gcp.pubsub.v1.DeleteTopicRequest :as DeleteTopicRequest]
            [gcp.pubsub.v1.GetSubscriptionRequest :as GSR]
            [gcp.pubsub.v1.GetTopicRequest :as GetTopicRequest]
            [gcp.pubsub.v1.ListTopicsRequest :as ListTopicsRequest]
            [gcp.pubsub.v1.ListTopicSubscriptionsRequest :as ListTopicSubscriptionsRequest]
            [gcp.pubsub.v1.Subscription :as Subscription]
            [gcp.pubsub.v1.SubscriptionAdminClient :as SAC]
            [gcp.pubsub.v1.Topic :as Topic]
            [gcp.pubsub.v1.TopicAdminClient :as TAC]
            [gcp.pubsub.v1.ProjectName :as ProjectName]
            [gcp.pubsub.v1.SubscriptionName :as SN]
            [gcp.pubsub.v1.TopicName :as TopicName])
  (:import (com.google.api.gax.rpc NotFoundException)
           (com.google.cloud.pubsub.v1 SubscriptionAdminClient TopicAdminClient)
           (com.google.pubsub.v1 GetSubscriptionRequest SubscriptionName)))

(defonce ^:dynamic *subscription-admin-client* nil)

(defn subscription-admin-client
  ([]
   (subscription-admin-client nil))
  ([arg]
   (or *subscription-admin-client*
       (if (instance? SubscriptionAdminClient arg)
         arg
         (g/client :gcp/pubsub.SubscriptionAdminClient arg)))))

(defonce ^:dynamic *topic-admin-client* nil)

(defn topic-admin-client
  ([]
   (topic-admin-client nil))
  ([arg]
   (or *topic-admin-client*
       (if (instance? TopicAdminClient arg)
         arg
         (g/client :gcp/pubsub.TopicAdminClient arg)))))

#!----------------------------------------------------------------------------------------------------------------------
#! Topic Administration

(defn list-topics
  ([]
   (list-topics nil))
  ([arg]
   (if (string? arg)
     (list-topics {:project arg})
     (let [{:keys [topicAdminClient project request]} (g/coerce :gcp/pubsub.synth.TopicList arg)
           client (topic-admin-client topicAdminClient)
           response (if project
                      (.listTopics client (ProjectName/from-edn project))
                      (.listTopics client (ListTopicsRequest/from-edn request)))]
       (TAC/ListTopicsPagedResponse-to-edn response))))
  ([arg0 arg1]
   ;; TODO clientable, project|request
   (throw (Exception. "unimplemented"))))


(defn get-topic
  ([arg]
   (cond
     (string? arg)
     (let [[_ project _ topic] (string/split arg #"/")]
       (get-topic {:project project :topic topic}))

     (and (map? arg) (contains? arg :name))
     (get-topic (get arg :name))

     (g/valid? [:or :gcp/pubsub.TopicName :gcp/pubsub.synth.TopicPath] arg) ;;TODO
     (get-topic {:topic arg})

     true
     (let [{:keys [topicAdminClient topic request]} (g/coerce :gcp/pubsub.synth.TopicGet arg)
           client (topic-admin-client topicAdminClient)]
       (try
         (Topic/to-edn
           (if topic
             (.getTopic client ^String (if (string? topic) topic (str (TopicName/from-edn topic))))
             (.getTopic client (GetTopicRequest/from-edn request))))
         (catch NotFoundException _
           nil)))))
  ([arg0 arg1]
   (if (string? arg0)
     (if (string? arg1)
       (get-topic {:topic {:project arg0 :topic arg1}})
       (throw (Exception. "unimplemented")))
     (throw (Exception. "unimplemented"))))
  ([arg0 arg1 arg2]
   ;; TODO client project topic
   (throw (Exception. "unimplemented"))))

;createTopic(Topic request)
;createTopic(TopicName name)
;createTopic(String name)
(defn create-topic
  ([arg]
   (if (g/valid? [:or :gcp/pubsub.TopicName :gcp/pubsub.synth.TopicPath] arg)
     (create-topic {:topic arg})
     (let [{:keys [topicAdminClient topic request]} (g/coerce :gcp/pubsub.synth.TopicCreate arg)
           client (topic-admin-client topicAdminClient)]
       (Topic/to-edn
         (if topic
           (.createTopic client ^String (if (string? topic) topic (str (TopicName/from-edn topic))))
           (.createTopic client (Topic/from-edn request)))))))
  ([arg0 arg1]
   (throw (Exception. "unimplemented"))))

(defn list-topic-subscriptions
  ([arg]
   (cond
     (string? arg)
     (let [[_ project _ topic] (string/split arg #"/")]
       (list-topic-subscriptions {:project project :topic topic}))

     (and (map? arg) (contains? arg :name))
     (list-topic-subscriptions (get arg :name))

     (g/valid? [:or :gcp/pubsub.TopicName :gcp/pubsub.synth.TopicPath] arg)
     (list-topic-subscriptions {:topic arg})

     true
     (let [{:keys [topicAdminClient topic request]} (g/coerce :gcp/pubsub.synth.TopicListSubscriptions arg)
           client (topic-admin-client topicAdminClient)
           response (if topic
                      (.listTopicSubscriptions client ^String (if (string? topic) topic (str (TopicName/from-edn topic))))
                      (.listTopicSubscriptions client (ListTopicSubscriptionsRequest/from-edn request)))]
       (TAC/ListTopicSubscriptionsPagedResponse-to-edn response))))
  ([arg0 arg1]
   (if (string? arg0)
     (if (string? arg1)
       (list-topic-subscriptions {:topic {:project arg0 :topic arg1}})))))

;; TODO :gcp/pubsub.Topic
(defn delete-topic
  ([arg]
   (if (g/valid? [:or :gcp/pubsub.TopicName :gcp/pubsub.synth.TopicPath] arg)
     (delete-topic {:topic arg})
     (let [{:keys [topicAdminClient topic request]} (g/coerce :gcp/pubsub.synth.TopicDelete arg)
           client (topic-admin-client topicAdminClient)]
       (if topic
         (.deleteTopic client ^String (if (string? topic) topic (str (TopicName/from-edn topic))))
         (.deleteTopic client (DeleteTopicRequest/from-edn request))))))
  ([arg0 arg1]
   (if (string? arg0)
     (if (string? arg1)
       (delete-topic {:topic {:project arg0 :topic arg1}})
       (throw (Exception. "unimplemented")))
     (throw (Exception. "unimplemented")))))

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
   (if (string? arg)
     (get-subscription {:subscription arg})
     (if (g/valid? :gcp/pubsub.SubscriptionName arg)
       (get-subscription {:subscription arg})
       (let [{:keys [subscriptionAdminClient subscription request]} (g/coerce :gcp/pubsub.synth.SubscriptionGet arg)
             client (subscription-admin-client subscriptionAdminClient)]
         (try
           (Subscription/to-edn
             (if request
               (.getSubscription client ^GetSubscriptionRequest (GSR/from-edn request))
               (if (string? subscription)
                 (.getSubscription client ^String subscription)
                 (.getSubscription client ^SubscriptionName (SN/from-edn subscription)))))
           (catch NotFoundException _
             nil))))))
  ([arg0 arg1]
   (if (string? arg0)
     (if (string? arg1)
       (get-subscription {:subscription {:project arg0 :subscription arg1}})
       (throw (Exception. "unimplemented")))
     (throw (Exception. "unimplemented")))))

(defn create-subscription
  ([arg]
   (if (g/valid? :gcp/pubsub.Subscription arg)
     (create-subscription {:subscription arg})
     (let [{:keys [subscription-admin subscription]} (g/coerce :gcp/pubsub.synth.SubscriptionCreate arg)
           client (subscription-admin-client subscription-admin)]
       (.createSubscription client (Subscription/from-edn subscription)))))
  ([arg0 arg1]
   (throw (Exception. "unimplemented"))))