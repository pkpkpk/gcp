(ns gcp.pubsub.v1.TopicAdminClient
  (:require [gcp.global :as g]
            [gcp.pubsub.v1.TopicAdminSettings :as TopicAdminSettings])
  (:import [com.google.cloud.pubsub.v1 TopicAdminClient]))

(defonce ^:dynamic *client* nil)

(defn ^TopicAdminClient client [arg]
  (or *client*
      (do
        (g/strict! :pubsub.synth/TopicAdminClientable arg)
        (if (instance? TopicAdminClient arg)
          arg
          (g/client :pubsub.synth/TopicAdminClient arg)))))

;;awaitTermination(long duration, TimeUnit unit)
;;close()
;;isShutdown()
;;isTerminated()
;;getSettings()
;;getStub()
;;shutdown()
;;shutdownNow()

;listTopics(ListTopicsRequest request)
;listTopics(ProjectName project)
;listTopics(String project)
;; https://cloud.google.com/java/docs/reference/google-cloud-pubsub/latest/com.google.pubsub.v1.ListTopicsRequest.Builder
(defn list-topics [arg]
  (if (g/valid? :pubsub.TopicAdminClient/ListTopics arg)
    (let [{:keys [topicAdminClient]} arg])))

;listTopicSnapshots(ListTopicSnapshotsRequest request)
;listTopicSnapshots(TopicName topic)
;listTopicSnapshots(String topic)
;listTopicSnapshotsCallable()
;listTopicSnapshotsPagedCallable()

;listTopicSubscriptions(ListTopicSubscriptionsRequest request)
;listTopicSubscriptions(ProjectTopicName topic) (deprecated)
;listTopicSubscriptions(TopicName topic)
;listTopicSubscriptions(String topic)
;listTopicSubscriptionsCallable()
;listTopicSubscriptionsPagedCallable()



;createTopic(ProjectTopicName name) (deprecated)
;createTopic(Topic request)
;createTopic(TopicName name)
;createTopic(String name)
;createTopicCallable()

;getTopic(GetTopicRequest request)
;getTopic(ProjectTopicName topic) (deprecated)
;getTopic(TopicName topic)
;getTopic(String topic)
;getTopicCallable()

;updateTopic(Topic topic, FieldMask updateMask)
;updateTopic(UpdateTopicRequest request)
;updateTopicCallable()

;deleteTopic(DeleteTopicRequest request)
;deleteTopic(ProjectTopicName topic) (deprecated)
;deleteTopic(TopicName topic)
;deleteTopic(String topic)
;deleteTopicCallable()

;detachSubscription(DetachSubscriptionRequest request)
;detachSubscriptionCallable()

;getIamPolicy(GetIamPolicyRequest request)
;getIamPolicy(String resource) (deprecated)
;getIamPolicyCallable()

;publish(PublishRequest request)
;publish(TopicName topic, List<PubsubMessage> messages)
;publish(String topic, List<PubsubMessage> messages)
;publishCallable()

;setIamPolicy(SetIamPolicyRequest request)
;setIamPolicy(String resource, Policy policy) (deprecated)
;setIamPolicyCallable()

;testIamPermissions(TestIamPermissionsRequest request)
;testIamPermissions(String resource, List<String> permissions) (deprecated)
;testIamPermissionsCallable()
