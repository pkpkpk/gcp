(ns gcp.pubsub.v1.TopicAdminClient
  (:require [clojure.string :as string]
            [gcp.global :as g]
            [gcp.pubsub.v1.Topic :as Topic]
            [gcp.pubsub.v1.TopicAdminSettings :as TopicAdminSettings]
            [gcp.pubsub.v1.PublisherStub :as PublisherStub])
  (:import [com.google.cloud.pubsub.v1 TopicAdminClient TopicAdminClient$ListTopicSubscriptionsPagedResponse TopicAdminClient$ListTopicsPagedResponse]))

(defn ^TopicAdminClient from-edn
  ([] (from-edn nil))
  ([arg]
   (if (nil? arg)
     (if (System/getenv "PUBSUB_EMULATOR_HOST")
       (TopicAdminClient/create (TopicAdminSettings/from-edn arg))
       (TopicAdminClient/create))
     (if (g/valid? :gcp/pubsub.TopicAdminSettings arg)
       (TopicAdminClient/create (TopicAdminSettings/from-edn arg))
       (TopicAdminClient/create (PublisherStub/from-edn arg))))))

;;awaitTermination(long duration, TimeUnit unit)
;;close()
;;isShutdown()
;;isTerminated()
;;getSettings()
;;getStub()
;;shutdown()
;;shutdownNow()

(defn ListTopicsPagedResponse-to-edn
  ;;https://cloud.google.com/java/docs/reference/google-cloud-pubsub/latest/com.google.cloud.pubsub.v1.TopicAdminClient.ListTopicsPagedResponse
  [^TopicAdminClient$ListTopicsPagedResponse arg]
  (if (string/blank? (.getNextPageToken arg))
    (map Topic/to-edn (.iterateAll arg))
    (throw (Exception. "unimplemented"))))

(defn ListTopicSubscriptionsPagedResponse-to-edn
  ;;https://cloud.google.com/java/docs/reference/google-cloud-pubsub/latest/com.google.cloud.pubsub.v1.TopicAdminClient.ListTopicSubscriptionsPagedResponse
  [^TopicAdminClient$ListTopicSubscriptionsPagedResponse arg]
  (if (string/blank? (.getNextPageToken arg))
    (vec (.iterateAll arg))
    (throw (Exception. "unimplemented"))))

;listTopicSnapshots(ListTopicSnapshotsRequest request)
;listTopicSnapshots(TopicName topic)
;listTopicSnapshots(String topic)
;listTopicSnapshotsCallable()

;updateTopic(Topic topic, FieldMask updateMask)
;updateTopic(UpdateTopicRequest request)

;deleteTopic(DeleteTopicRequest request)
;deleteTopic(ProjectTopicName topic) (deprecated)
;deleteTopic(TopicName topic)
;deleteTopic(String topic)

;detachSubscription(DetachSubscriptionRequest request)
;detachSubscriptionCallable()

;getIamPolicy(GetIamPolicyRequest request)
;getIamPolicy(String resource) (deprecated)
;setIamPolicy(SetIamPolicyRequest request)
;setIamPolicy(String resource, Policy policy) (deprecated)
;testIamPermissions(TestIamPermissionsRequest request)
;testIamPermissions(String resource, List<String> permissions) (deprecated)

;publish(PublishRequest request)
;publish(TopicName topic, List<PubsubMessage> messages)
;publish(String topic, List<PubsubMessage> messages)
