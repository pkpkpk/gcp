(ns gcp.pubsub.v1.SubscriptionAdminClient
  (:require [clojure.string :as string]
            [gcp.global :as global]
            [gcp.pubsub.v1.SubscriberStub :as SubscriberStub]
            [gcp.pubsub.v1.Subscription :as Subscription]
            [gcp.pubsub.v1.SubscriptionAdminSettings :as SubscriptionAdminSettings])
  (:import [com.google.cloud.pubsub.v1 SubscriptionAdminClient SubscriptionAdminClient$ListSubscriptionsPagedResponse]))

(defn ^SubscriptionAdminClient from-edn
  ([]
   (from-edn nil))
  ([arg]
   (if (nil? arg)
     (if (System/getenv "PUBSUB_EMULATOR_HOST")
       (SubscriptionAdminClient/create (SubscriptionAdminSettings/from-edn arg))
       (SubscriptionAdminClient/create))
     (if (global/valid? :gcp.pubsub.v1/SubscriptionAdminSettings arg)
       (SubscriptionAdminClient/create (SubscriptionAdminSettings/from-edn arg))
       (SubscriptionAdminClient/create (SubscriberStub/from-edn arg))))))

(defn ListSubscriptionsPagedResponse:to-edn
  [^SubscriptionAdminClient$ListSubscriptionsPagedResponse arg]
  (if (string/blank? (.getNextPageToken arg))
    (mapv Subscription/to-edn (.iterateAll arg))
    (throw (Exception. "unimplemented"))))

;getStub()
;getSettings()
;isShutdown()
;isTerminated()
;close()
;awaitTermination(long duration, TimeUnit unit)
;shutdown()
;shutdownNow()

;createSubscription(Subscription request)
;createSubscription(SubscriptionName name, TopicName topic, PushConfig pushConfig, int ackDeadlineSeconds)
;createSubscription(SubscriptionName name, String topic, PushConfig pushConfig, int ackDeadlineSeconds)
;createSubscription(String name, TopicName topic, PushConfig pushConfig, int ackDeadlineSeconds)
;createSubscription(String name, String topic, PushConfig pushConfig, int ackDeadlineSeconds)

;deleteSubscription(DeleteSubscriptionRequest request)
;deleteSubscription(ProjectSubscriptionName subscription) (deprecated)
;deleteSubscription(SubscriptionName subscription)
;deleteSubscription(String subscription)

;updateSubscription(Subscription subscription, FieldMask updateMask)
;updateSubscription(UpdateSubscriptionRequest request)

;createSnapshot(CreateSnapshotRequest request)
;createSnapshot(SnapshotName name, SubscriptionName subscription)
;createSnapshot(String name, String subscription)

;deleteSnapshot(DeleteSnapshotRequest request)
;deleteSnapshot(ProjectSnapshotName snapshot) (deprecated)
;deleteSnapshot(SnapshotName snapshot)
;deleteSnapshot(String snapshot)

;getSnapshot(GetSnapshotRequest request)
;getSnapshot(ProjectSnapshotName snapshot) (deprecated)
;getSnapshot(SnapshotName snapshot)
;getSnapshot(String snapshot)

;listSnapshots(ListSnapshotsRequest request)
;listSnapshots(ProjectName project)
;listSnapshots(String project)

;updateSnapshot(Snapshot snapshot, FieldMask updateMask)
;updateSnapshot(UpdateSnapshotRequest request)

#!----------------------------------------------------------------------------------------------------------------------

;getIamPolicy(GetIamPolicyRequest request)
;getIamPolicy(String resource) (deprecated)

;setIamPolicy(SetIamPolicyRequest request)
;setIamPolicy(String resource, Policy policy) (deprecated)

;testIamPermissions(TestIamPermissionsRequest request)
;testIamPermissions(String resource, List<String> permissions) (deprecated)

#!----------------------------------------------------------------------------------------------------------------------

;acknowledge(AcknowledgeRequest request)
;acknowledge(ProjectSubscriptionName subscription, List<String> ackIds) (deprecated)
;acknowledge(SubscriptionName subscription, List<String> ackIds)
;acknowledge(String subscription, List<String> ackIds)

;modifyAckDeadline(ModifyAckDeadlineRequest request)
;modifyAckDeadline(SubscriptionName subscription, List<String> ackIds, int ackDeadlineSeconds)
;modifyAckDeadline(String subscription, List<String> ackIds, int ackDeadlineSeconds)

;modifyPushConfig(ModifyPushConfigRequest request)
;modifyPushConfig(ProjectSubscriptionName subscription, PushConfig pushConfig) (deprecated)
;modifyPushConfig(SubscriptionName subscription, PushConfig pushConfig)
;modifyPushConfig(String subscription, PushConfig pushConfig)

;pull(ProjectSubscriptionName subscription, int maxMessages) (deprecated)
;pull(PullRequest request)
;pull(SubscriptionName subscription, boolean returnImmediately, int maxMessages)
;pull(SubscriptionName subscription, int maxMessages)
;pull(String subscription, boolean returnImmediately, int maxMessages)
;pull(String subscription, int maxMessages)

;seek(SeekRequest request)

(def schemas
  {:gcp.pubsub.v1/SubscriptionAdminClient
   [:maybe {:from-edn 'gcp.pubsub.v1.SubscriptionAdminClient/from-edn}
    [:or
     :gcp.pubsub.v1/SubscriptionAdminSettings
     :gcp.pubsub.v1/SubscriberStub]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))
