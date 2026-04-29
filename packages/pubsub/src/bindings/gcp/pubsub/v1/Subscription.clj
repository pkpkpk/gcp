;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.pubsub.v1.Subscription
  {:doc
     "<pre>\nA subscription resource. If none of `push_config`, `bigquery_config`, or\n`cloud_storage_config` is set, then the subscriber will pull and ack messages\nusing API methods. At most one of these fields may be set.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.Subscription}"
   :file-git-sha "f69de7ee2cb5b96adbf3fc5b11e1f8a3bbbd0bee"
   :fqcn "com.google.pubsub.v1.Subscription"
   :gcp.dev/certification
     {:base-seed 1777403454615
      :manifest "1278e47a-581c-5be4-ab42-9f07d3602a33"
      :passed-stages
        {:smoke 1777403454615 :standard 1777403454616 :stress 1777403454617}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-28T19:10:56.512395361Z"}}
  (:require [gcp.foreign.com.google.protobuf :as protobuf]
            [gcp.global :as global]
            [gcp.pubsub.v1.BigQueryConfig :as BigQueryConfig]
            [gcp.pubsub.v1.BigtableConfig :as BigtableConfig]
            [gcp.pubsub.v1.CloudStorageConfig :as CloudStorageConfig]
            [gcp.pubsub.v1.DeadLetterPolicy :as DeadLetterPolicy]
            [gcp.pubsub.v1.ExpirationPolicy :as ExpirationPolicy]
            [gcp.pubsub.v1.MessageTransform :as MessageTransform]
            [gcp.pubsub.v1.PushConfig :as PushConfig]
            [gcp.pubsub.v1.RetryPolicy :as RetryPolicy])
  (:import [com.google.protobuf Duration]
           [com.google.pubsub.v1 Subscription
            Subscription$AnalyticsHubSubscriptionInfo
            Subscription$AnalyticsHubSubscriptionInfo$Builder
            Subscription$Builder Subscription$State]))

(declare from-edn
         to-edn
         State-from-edn
         State-to-edn
         AnalyticsHubSubscriptionInfo-from-edn
         AnalyticsHubSubscriptionInfo-to-edn)

(def State-schema
  [:enum
   {:closed true,
    :doc
      "<pre>\nPossible states for a subscription.\n</pre>\n\nProtobuf enum {@code google.pubsub.v1.Subscription.State}",
    :gcp/category :nested/enum,
    :gcp/key :gcp.pubsub.v1/Subscription.State} "STATE_UNSPECIFIED" "ACTIVE"
   "RESOURCE_ERROR"])

(defn
  ^Subscription$AnalyticsHubSubscriptionInfo AnalyticsHubSubscriptionInfo-from-edn
  [arg]
  (let [builder (Subscription$AnalyticsHubSubscriptionInfo/newBuilder)]
    (when (some? (get arg :listing)) (.setListing builder (get arg :listing)))
    (when (some? (get arg :subscription))
      (.setSubscription builder (get arg :subscription)))
    (.build builder)))

(defn
  AnalyticsHubSubscriptionInfo-to-edn
  [^Subscription$AnalyticsHubSubscriptionInfo arg]
  (when arg
    (cond-> {}
      (some->> (.getListing arg)
               (not= ""))
        (assoc :listing (.getListing arg))
      (some->> (.getSubscription arg)
               (not= ""))
        (assoc :subscription (.getSubscription arg)))))

(def AnalyticsHubSubscriptionInfo-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nInformation about an associated [Analytics Hub\nsubscription](https://cloud.google.com/bigquery/docs/analytics-hub-manage-subscriptions).\n</pre>\n\nProtobuf type {@code google.pubsub.v1.Subscription.AnalyticsHubSubscriptionInfo}",
    :gcp/category :nested/protobuf-message,
    :gcp/key :gcp.pubsub.v1/Subscription.AnalyticsHubSubscriptionInfo}
   [:listing
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The name of the associated Analytics Hub listing resource.\nPattern:\n\"projects/{project}/locations/{location}/dataExchanges/{data_exchange}/listings/{listing}\"\n</pre>\n\n<code>\nstring listing = 1 [(.google.api.field_behavior) = OPTIONAL, (.google.api.resource_reference) = { ... }\n</code>\n\n@return The listing.",
     :setter-doc
       "<pre>\nOptional. The name of the associated Analytics Hub listing resource.\nPattern:\n\"projects/{project}/locations/{location}/dataExchanges/{data_exchange}/listings/{listing}\"\n</pre>\n\n<code>\nstring listing = 1 [(.google.api.field_behavior) = OPTIONAL, (.google.api.resource_reference) = { ... }\n</code>\n\n@param value The listing to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:subscription
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The name of the associated Analytics Hub subscription resource.\nPattern:\n\"projects/{project}/locations/{location}/subscriptions/{subscription}\"\n</pre>\n\n<code>string subscription = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The subscription.",
     :setter-doc
       "<pre>\nOptional. The name of the associated Analytics Hub subscription resource.\nPattern:\n\"projects/{project}/locations/{location}/subscriptions/{subscription}\"\n</pre>\n\n<code>string subscription = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The subscription to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]])

(defn ^Subscription from-edn
  [arg]
  (global/strict! :gcp.pubsub.v1/Subscription arg)
  (let [builder (Subscription/newBuilder)]
    (when (some? (get arg :ackDeadlineSeconds))
      (.setAckDeadlineSeconds builder (int (get arg :ackDeadlineSeconds))))
    (when (some? (get arg :analyticsHubSubscriptionInfo))
      (.setAnalyticsHubSubscriptionInfo builder
                                        (AnalyticsHubSubscriptionInfo-from-edn
                                          (get arg
                                               :analyticsHubSubscriptionInfo))))
    (when (some? (get arg :bigqueryConfig))
      (.setBigqueryConfig builder
                          (BigQueryConfig/from-edn (get arg :bigqueryConfig))))
    (when (some? (get arg :bigtableConfig))
      (.setBigtableConfig builder
                          (BigtableConfig/from-edn (get arg :bigtableConfig))))
    (when (some? (get arg :cloudStorageConfig))
      (.setCloudStorageConfig builder
                              (CloudStorageConfig/from-edn
                                (get arg :cloudStorageConfig))))
    (when (some? (get arg :deadLetterPolicy))
      (.setDeadLetterPolicy builder
                            (DeadLetterPolicy/from-edn
                              (get arg :deadLetterPolicy))))
    (when (some? (get arg :detached))
      (.setDetached builder (get arg :detached)))
    (when (some? (get arg :enableExactlyOnceDelivery))
      (.setEnableExactlyOnceDelivery builder
                                     (get arg :enableExactlyOnceDelivery)))
    (when (some? (get arg :enableMessageOrdering))
      (.setEnableMessageOrdering builder (get arg :enableMessageOrdering)))
    (when (some? (get arg :expirationPolicy))
      (.setExpirationPolicy builder
                            (ExpirationPolicy/from-edn
                              (get arg :expirationPolicy))))
    (when (some? (get arg :filter)) (.setFilter builder (get arg :filter)))
    (when (seq (get arg :labels))
      (.putAllLabels
        builder
        (into {} (map (fn [[k v]] [(name k) v])) (get arg :putAllLabels))))
    (when (some? (get arg :messageRetentionDuration))
      (.setMessageRetentionDuration builder
                                    (protobuf/Duration-from-edn
                                      (get arg :messageRetentionDuration))))
    (when (seq (get arg :messageTransforms))
      (.addAllMessageTransforms builder
                                (mapv MessageTransform/from-edn
                                  (get arg :messageTransforms))))
    (when (some? (get arg :name)) (.setName builder (get arg :name)))
    (when (some? (get arg :pushConfig))
      (.setPushConfig builder (PushConfig/from-edn (get arg :pushConfig))))
    (when (some? (get arg :retainAckedMessages))
      (.setRetainAckedMessages builder (get arg :retainAckedMessages)))
    (when (some? (get arg :retryPolicy))
      (.setRetryPolicy builder (RetryPolicy/from-edn (get arg :retryPolicy))))
    (when (some? (get arg :state))
      (.setState builder (Subscription$State/valueOf (get arg :state))))
    (when (seq (get arg :tags))
      (.putAllTags
        builder
        (into {} (map (fn [[k v]] [(name k) v])) (get arg :putAllTags))))
    (when (some? (get arg :topic)) (.setTopic builder (get arg :topic)))
    (when (some? (get arg :topicMessageRetentionDuration))
      (.setTopicMessageRetentionDuration
        builder
        (protobuf/Duration-from-edn (get arg :topicMessageRetentionDuration))))
    (.build builder)))

(defn to-edn
  [^Subscription arg]
  {:post [(global/strict! :gcp.pubsub.v1/Subscription %)]}
  (when arg
    (cond-> {:name (.getName arg), :topic (.getTopic arg)}
      (.getAckDeadlineSeconds arg) (assoc :ackDeadlineSeconds
                                     (.getAckDeadlineSeconds arg))
      (.hasAnalyticsHubSubscriptionInfo arg)
        (assoc :analyticsHubSubscriptionInfo
          (AnalyticsHubSubscriptionInfo-to-edn (.getAnalyticsHubSubscriptionInfo
                                                 arg)))
      (.hasBigqueryConfig arg)
        (assoc :bigqueryConfig (BigQueryConfig/to-edn (.getBigqueryConfig arg)))
      (.hasBigtableConfig arg)
        (assoc :bigtableConfig (BigtableConfig/to-edn (.getBigtableConfig arg)))
      (.hasCloudStorageConfig arg) (assoc :cloudStorageConfig
                                     (CloudStorageConfig/to-edn
                                       (.getCloudStorageConfig arg)))
      (.hasDeadLetterPolicy arg) (assoc :deadLetterPolicy
                                   (DeadLetterPolicy/to-edn
                                     (.getDeadLetterPolicy arg)))
      (.getDetached arg) (assoc :detached (.getDetached arg))
      (.getEnableExactlyOnceDelivery arg) (assoc :enableExactlyOnceDelivery
                                            (.getEnableExactlyOnceDelivery arg))
      (.getEnableMessageOrdering arg) (assoc :enableMessageOrdering
                                        (.getEnableMessageOrdering arg))
      (.hasExpirationPolicy arg) (assoc :expirationPolicy
                                   (ExpirationPolicy/to-edn
                                     (.getExpirationPolicy arg)))
      (some->> (.getFilter arg)
               (not= ""))
        (assoc :filter (.getFilter arg))
      (seq (.getLabelsMap arg))
        (assoc :labels
          (into {} (map (fn [[k v]] [(keyword k) v])) (.getLabelsMap arg)))
      (.hasMessageRetentionDuration arg)
        (assoc :messageRetentionDuration
          (protobuf/Duration-to-edn (.getMessageRetentionDuration arg)))
      (seq (.getMessageTransformsList arg))
        (assoc :messageTransforms
          (mapv MessageTransform/to-edn (.getMessageTransformsList arg)))
      (.hasPushConfig arg) (assoc :pushConfig
                             (PushConfig/to-edn (.getPushConfig arg)))
      (.getRetainAckedMessages arg) (assoc :retainAckedMessages
                                      (.getRetainAckedMessages arg))
      (.hasRetryPolicy arg) (assoc :retryPolicy
                              (RetryPolicy/to-edn (.getRetryPolicy arg)))
      (.getState arg) (assoc :state (.name (.getState arg)))
      (seq (.getTagsMap arg))
        (assoc :tags
          (into {} (map (fn [[k v]] [(keyword k) v])) (.getTagsMap arg)))
      (.hasTopicMessageRetentionDuration arg)
        (assoc :topicMessageRetentionDuration
          (protobuf/Duration-to-edn (.getTopicMessageRetentionDuration arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nA subscription resource. If none of `push_config`, `bigquery_config`, or\n`cloud_storage_config` is set, then the subscriber will pull and ack messages\nusing API methods. At most one of these fields may be set.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.Subscription}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.pubsub.v1/Subscription}
   [:ackDeadlineSeconds
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The approximate amount of time (on a best-effort basis) Pub/Sub\nwaits for the subscriber to acknowledge receipt before resending the\nmessage. In the interval after the message is delivered and before it is\nacknowledged, it is considered to be _outstanding_. During that time\nperiod, the message will not be redelivered (on a best-effort basis).\n\nFor pull subscriptions, this value is used as the initial value for the ack\ndeadline. To override this value for a given message, call\n`ModifyAckDeadline` with the corresponding `ack_id` if using\nnon-streaming pull or send the `ack_id` in a\n`StreamingModifyAckDeadlineRequest` if using streaming pull.\nThe minimum custom deadline you can specify is 10 seconds.\nThe maximum custom deadline you can specify is 600 seconds (10 minutes).\nIf this parameter is 0, a default value of 10 seconds is used.\n\nFor push delivery, this value is also used to set the request timeout for\nthe call to the push endpoint.\n\nIf the subscriber never acknowledges the message, the Pub/Sub\nsystem will eventually redeliver the message.\n</pre>\n\n<code>int32 ack_deadline_seconds = 5 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The ackDeadlineSeconds.",
     :setter-doc
       "<pre>\nOptional. The approximate amount of time (on a best-effort basis) Pub/Sub\nwaits for the subscriber to acknowledge receipt before resending the\nmessage. In the interval after the message is delivered and before it is\nacknowledged, it is considered to be _outstanding_. During that time\nperiod, the message will not be redelivered (on a best-effort basis).\n\nFor pull subscriptions, this value is used as the initial value for the ack\ndeadline. To override this value for a given message, call\n`ModifyAckDeadline` with the corresponding `ack_id` if using\nnon-streaming pull or send the `ack_id` in a\n`StreamingModifyAckDeadlineRequest` if using streaming pull.\nThe minimum custom deadline you can specify is 10 seconds.\nThe maximum custom deadline you can specify is 600 seconds (10 minutes).\nIf this parameter is 0, a default value of 10 seconds is used.\n\nFor push delivery, this value is also used to set the request timeout for\nthe call to the push endpoint.\n\nIf the subscriber never acknowledges the message, the Pub/Sub\nsystem will eventually redeliver the message.\n</pre>\n\n<code>int32 ack_deadline_seconds = 5 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The ackDeadlineSeconds to set.\n@return This builder for chaining."}
    :i32]
   [:analyticsHubSubscriptionInfo
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. Information about the associated Analytics Hub subscription.\nOnly set if the subscription is created by Analytics Hub.\n</pre>\n\n<code>\n.google.pubsub.v1.Subscription.AnalyticsHubSubscriptionInfo analytics_hub_subscription_info = 23 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>\n\n@return The analyticsHubSubscriptionInfo."}
    [:ref :gcp.pubsub.v1/Subscription.AnalyticsHubSubscriptionInfo]]
   [:bigqueryConfig
    {:optional true,
     :getter-doc
       "<pre>\nOptional. If delivery to BigQuery is used with this subscription, this\nfield is used to configure it.\n</pre>\n\n<code>\n.google.pubsub.v1.BigQueryConfig bigquery_config = 18 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The bigqueryConfig.",
     :setter-doc
       "<pre>\nOptional. If delivery to BigQuery is used with this subscription, this\nfield is used to configure it.\n</pre>\n\n<code>\n.google.pubsub.v1.BigQueryConfig bigquery_config = 18 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.pubsub.v1/BigQueryConfig]
   [:bigtableConfig
    {:optional true,
     :getter-doc
       "<pre>\nOptional. If delivery to Bigtable is used with this subscription, this\nfield is used to configure it.\n</pre>\n\n<code>\n.google.pubsub.v1.BigtableConfig bigtable_config = 27 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The bigtableConfig.",
     :setter-doc
       "<pre>\nOptional. If delivery to Bigtable is used with this subscription, this\nfield is used to configure it.\n</pre>\n\n<code>\n.google.pubsub.v1.BigtableConfig bigtable_config = 27 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.pubsub.v1/BigtableConfig]
   [:cloudStorageConfig
    {:optional true,
     :getter-doc
       "<pre>\nOptional. If delivery to Google Cloud Storage is used with this\nsubscription, this field is used to configure it.\n</pre>\n\n<code>\n.google.pubsub.v1.CloudStorageConfig cloud_storage_config = 22 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The cloudStorageConfig.",
     :setter-doc
       "<pre>\nOptional. If delivery to Google Cloud Storage is used with this\nsubscription, this field is used to configure it.\n</pre>\n\n<code>\n.google.pubsub.v1.CloudStorageConfig cloud_storage_config = 22 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.pubsub.v1/CloudStorageConfig]
   [:deadLetterPolicy
    {:optional true,
     :getter-doc
       "<pre>\nOptional. A policy that specifies the conditions for dead lettering\nmessages in this subscription. If dead_letter_policy is not set, dead\nlettering is disabled.\n\nThe Pub/Sub service account associated with this subscriptions's\nparent project (i.e.,\nservice-{project_number}&#64;gcp-sa-pubsub.iam.gserviceaccount.com) must have\npermission to Acknowledge() messages on this subscription.\n</pre>\n\n<code>\n.google.pubsub.v1.DeadLetterPolicy dead_letter_policy = 13 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The deadLetterPolicy.",
     :setter-doc
       "<pre>\nOptional. A policy that specifies the conditions for dead lettering\nmessages in this subscription. If dead_letter_policy is not set, dead\nlettering is disabled.\n\nThe Pub/Sub service account associated with this subscriptions's\nparent project (i.e.,\nservice-{project_number}&#64;gcp-sa-pubsub.iam.gserviceaccount.com) must have\npermission to Acknowledge() messages on this subscription.\n</pre>\n\n<code>\n.google.pubsub.v1.DeadLetterPolicy dead_letter_policy = 13 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.pubsub.v1/DeadLetterPolicy]
   [:detached
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Indicates whether the subscription is detached from its topic.\nDetached subscriptions don't receive messages from their topic and don't\nretain any backlog. `Pull` and `StreamingPull` requests will return\nFAILED_PRECONDITION. If the subscription is a push subscription, pushes to\nthe endpoint will not be made.\n</pre>\n\n<code>bool detached = 15 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The detached.",
     :setter-doc
       "<pre>\nOptional. Indicates whether the subscription is detached from its topic.\nDetached subscriptions don't receive messages from their topic and don't\nretain any backlog. `Pull` and `StreamingPull` requests will return\nFAILED_PRECONDITION. If the subscription is a push subscription, pushes to\nthe endpoint will not be made.\n</pre>\n\n<code>bool detached = 15 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The detached to set.\n@return This builder for chaining."}
    :boolean]
   [:enableExactlyOnceDelivery
    {:optional true,
     :getter-doc
       "<pre>\nOptional. If true, Pub/Sub provides the following guarantees for the\ndelivery of a message with a given value of `message_id` on this\nsubscription:\n\n* The message sent to a subscriber is guaranteed not to be resent\nbefore the message's acknowledgment deadline expires.\n* An acknowledged message will not be resent to a subscriber.\n\nNote that subscribers may still receive multiple copies of a message\nwhen `enable_exactly_once_delivery` is true if the message was published\nmultiple times by a publisher client. These copies are  considered distinct\nby Pub/Sub and have distinct `message_id` values.\n</pre>\n\n<code>bool enable_exactly_once_delivery = 16 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The enableExactlyOnceDelivery.",
     :setter-doc
       "<pre>\nOptional. If true, Pub/Sub provides the following guarantees for the\ndelivery of a message with a given value of `message_id` on this\nsubscription:\n\n* The message sent to a subscriber is guaranteed not to be resent\nbefore the message's acknowledgment deadline expires.\n* An acknowledged message will not be resent to a subscriber.\n\nNote that subscribers may still receive multiple copies of a message\nwhen `enable_exactly_once_delivery` is true if the message was published\nmultiple times by a publisher client. These copies are  considered distinct\nby Pub/Sub and have distinct `message_id` values.\n</pre>\n\n<code>bool enable_exactly_once_delivery = 16 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@param value The enableExactlyOnceDelivery to set.\n@return This builder for chaining."}
    :boolean]
   [:enableMessageOrdering
    {:optional true,
     :getter-doc
       "<pre>\nOptional. If true, messages published with the same `ordering_key` in\n`PubsubMessage` will be delivered to the subscribers in the order in which\nthey are received by the Pub/Sub system. Otherwise, they may be delivered\nin any order.\n</pre>\n\n<code>bool enable_message_ordering = 10 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The enableMessageOrdering.",
     :setter-doc
       "<pre>\nOptional. If true, messages published with the same `ordering_key` in\n`PubsubMessage` will be delivered to the subscribers in the order in which\nthey are received by the Pub/Sub system. Otherwise, they may be delivered\nin any order.\n</pre>\n\n<code>bool enable_message_ordering = 10 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The enableMessageOrdering to set.\n@return This builder for chaining."}
    :boolean]
   [:expirationPolicy
    {:optional true,
     :getter-doc
       "<pre>\nOptional. A policy that specifies the conditions for this subscription's\nexpiration. A subscription is considered active as long as any connected\nsubscriber is successfully consuming messages from the subscription or is\nissuing operations on the subscription. If `expiration_policy` is not set,\na *default policy* with `ttl` of 31 days will be used. The minimum allowed\nvalue for `expiration_policy.ttl` is 1 day. If `expiration_policy` is set,\nbut `expiration_policy.ttl` is not set, the subscription never expires.\n</pre>\n\n<code>\n.google.pubsub.v1.ExpirationPolicy expiration_policy = 11 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The expirationPolicy.",
     :setter-doc
       "<pre>\nOptional. A policy that specifies the conditions for this subscription's\nexpiration. A subscription is considered active as long as any connected\nsubscriber is successfully consuming messages from the subscription or is\nissuing operations on the subscription. If `expiration_policy` is not set,\na *default policy* with `ttl` of 31 days will be used. The minimum allowed\nvalue for `expiration_policy.ttl` is 1 day. If `expiration_policy` is set,\nbut `expiration_policy.ttl` is not set, the subscription never expires.\n</pre>\n\n<code>\n.google.pubsub.v1.ExpirationPolicy expiration_policy = 11 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.pubsub.v1/ExpirationPolicy]
   [:filter
    {:optional true,
     :getter-doc
       "<pre>\nOptional. An expression written in the Pub/Sub [filter\nlanguage](https://cloud.google.com/pubsub/docs/filtering). If non-empty,\nthen only `PubsubMessage`s whose `attributes` field matches the filter are\ndelivered on this subscription. If empty, then no messages are filtered\nout.\n</pre>\n\n<code>string filter = 12 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The filter.",
     :setter-doc
       "<pre>\nOptional. An expression written in the Pub/Sub [filter\nlanguage](https://cloud.google.com/pubsub/docs/filtering). If non-empty,\nthen only `PubsubMessage`s whose `attributes` field matches the filter are\ndelivered on this subscription. If empty, then no messages are filtered\nout.\n</pre>\n\n<code>string filter = 12 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The filter to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:labels
    {:optional true,
     :getter-doc
       "<pre>\nOptional. See [Creating and managing\nlabels](https://cloud.google.com/pubsub/docs/labels).\n</pre>\n\n<code>map&lt;string, string&gt; labels = 9 [(.google.api.field_behavior) = OPTIONAL];</code>",
     :setter-doc
       "<pre>\nOptional. See [Creating and managing\nlabels](https://cloud.google.com/pubsub/docs/labels).\n</pre>\n\n<code>map&lt;string, string&gt; labels = 9 [(.google.api.field_behavior) = OPTIONAL];</code>"}
    [:map-of [:or simple-keyword? [:string {:min 1}]]
     [:string {:min 1, :gen/max 1}]]]
   [:messageRetentionDuration
    {:optional true,
     :getter-doc
       "<pre>\nOptional. How long to retain unacknowledged messages in the subscription's\nbacklog, from the moment a message is published. If `retain_acked_messages`\nis true, then this also configures the retention of acknowledged messages,\nand thus configures how far back in time a `Seek` can be done. Defaults to\n7 days. Cannot be more than 31 days or less than 10 minutes.\n</pre>\n\n<code>\n.google.protobuf.Duration message_retention_duration = 8 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The messageRetentionDuration.",
     :setter-doc
       "<pre>\nOptional. How long to retain unacknowledged messages in the subscription's\nbacklog, from the moment a message is published. If `retain_acked_messages`\nis true, then this also configures the retention of acknowledged messages,\nand thus configures how far back in time a `Seek` can be done. Defaults to\n7 days. Cannot be more than 31 days or less than 10 minutes.\n</pre>\n\n<code>\n.google.protobuf.Duration message_retention_duration = 8 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.foreign.com.google.protobuf/Duration]
   [:messageTransforms
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Transforms to be applied to messages before they are delivered to\nsubscribers. Transforms are applied in the order specified.\n</pre>\n\n<code>\nrepeated .google.pubsub.v1.MessageTransform message_transforms = 25 [(.google.api.field_behavior) = OPTIONAL];\n</code>",
     :setter-doc
       "<pre>\nOptional. Transforms to be applied to messages before they are delivered to\nsubscribers. Transforms are applied in the order specified.\n</pre>\n\n<code>\nrepeated .google.pubsub.v1.MessageTransform message_transforms = 25 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    [:sequential {:min 1, :gen/max 2} :gcp.pubsub.v1/MessageTransform]]
   [:name
    {:getter-doc
       "<pre>\nRequired. Identifier. The name of the subscription. It must have the format\n`\"projects/{project}/subscriptions/{subscription}\"`. `{subscription}` must\nstart with a letter, and contain only letters (`[A-Za-z]`), numbers\n(`[0-9]`), dashes (`-`), underscores (`_`), periods (`.`), tildes (`~`),\nplus (`+`) or percent signs (`%`). It must be between 3 and 255 characters\nin length, and it must not start with `\"goog\"`.\n</pre>\n\n<code>\nstring name = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.field_behavior) = IDENTIFIER];\n</code>\n\n@return The name.",
     :setter-doc
       "<pre>\nRequired. Identifier. The name of the subscription. It must have the format\n`\"projects/{project}/subscriptions/{subscription}\"`. `{subscription}` must\nstart with a letter, and contain only letters (`[A-Za-z]`), numbers\n(`[0-9]`), dashes (`-`), underscores (`_`), periods (`.`), tildes (`~`),\nplus (`+`) or percent signs (`%`). It must be between 3 and 255 characters\nin length, and it must not start with `\"goog\"`.\n</pre>\n\n<code>\nstring name = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.field_behavior) = IDENTIFIER];\n</code>\n\n@param value The name to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:pushConfig
    {:optional true,
     :getter-doc
       "<pre>\nOptional. If push delivery is used with this subscription, this field is\nused to configure it.\n</pre>\n\n<code>.google.pubsub.v1.PushConfig push_config = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The pushConfig.",
     :setter-doc
       "<pre>\nOptional. If push delivery is used with this subscription, this field is\nused to configure it.\n</pre>\n\n<code>.google.pubsub.v1.PushConfig push_config = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.pubsub.v1/PushConfig]
   [:retainAckedMessages
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Indicates whether to retain acknowledged messages. If true, then\nmessages are not expunged from the subscription's backlog, even if they are\nacknowledged, until they fall out of the `message_retention_duration`\nwindow. This must be true if you would like to [`Seek` to a timestamp]\n(https://cloud.google.com/pubsub/docs/replay-overview#seek_to_a_time) in\nthe past to replay previously-acknowledged messages.\n</pre>\n\n<code>bool retain_acked_messages = 7 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The retainAckedMessages.",
     :setter-doc
       "<pre>\nOptional. Indicates whether to retain acknowledged messages. If true, then\nmessages are not expunged from the subscription's backlog, even if they are\nacknowledged, until they fall out of the `message_retention_duration`\nwindow. This must be true if you would like to [`Seek` to a timestamp]\n(https://cloud.google.com/pubsub/docs/replay-overview#seek_to_a_time) in\nthe past to replay previously-acknowledged messages.\n</pre>\n\n<code>bool retain_acked_messages = 7 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The retainAckedMessages to set.\n@return This builder for chaining."}
    :boolean]
   [:retryPolicy
    {:optional true,
     :getter-doc
       "<pre>\nOptional. A policy that specifies how Pub/Sub retries message delivery for\nthis subscription.\n\nIf not set, the default retry policy is applied. This generally implies\nthat messages will be retried as soon as possible for healthy subscribers.\nRetryPolicy will be triggered on NACKs or acknowledgment deadline exceeded\nevents for a given message.\n</pre>\n\n<code>\n.google.pubsub.v1.RetryPolicy retry_policy = 14 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The retryPolicy.",
     :setter-doc
       "<pre>\nOptional. A policy that specifies how Pub/Sub retries message delivery for\nthis subscription.\n\nIf not set, the default retry policy is applied. This generally implies\nthat messages will be retried as soon as possible for healthy subscribers.\nRetryPolicy will be triggered on NACKs or acknowledgment deadline exceeded\nevents for a given message.\n</pre>\n\n<code>\n.google.pubsub.v1.RetryPolicy retry_policy = 14 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.pubsub.v1/RetryPolicy]
   [:state
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. An output-only field indicating whether or not the\nsubscription can receive messages.\n</pre>\n\n<code>\n.google.pubsub.v1.Subscription.State state = 19 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>\n\n@return The state."}
    [:enum {:closed true} "STATE_UNSPECIFIED" "ACTIVE" "RESOURCE_ERROR"]]
   [:tags
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Input only. Immutable. Tag keys/values directly bound to this\nresource. For example:\n\"123/environment\": \"production\",\n\"123/costCenter\": \"marketing\"\nSee https://docs.cloud.google.com/pubsub/docs/tags for more information on\nusing tags with Pub/Sub resources.\n</pre>\n\n<code>\nmap&lt;string, string&gt; tags = 26 [(.google.api.field_behavior) = INPUT_ONLY, (.google.api.field_behavior) = IMMUTABLE, (.google.api.field_behavior) = OPTIONAL];\n</code>",
     :setter-doc
       "<pre>\nOptional. Input only. Immutable. Tag keys/values directly bound to this\nresource. For example:\n\"123/environment\": \"production\",\n\"123/costCenter\": \"marketing\"\nSee https://docs.cloud.google.com/pubsub/docs/tags for more information on\nusing tags with Pub/Sub resources.\n</pre>\n\n<code>\nmap&lt;string, string&gt; tags = 26 [(.google.api.field_behavior) = INPUT_ONLY, (.google.api.field_behavior) = IMMUTABLE, (.google.api.field_behavior) = OPTIONAL];\n</code>"}
    [:map-of [:or simple-keyword? [:string {:min 1}]]
     [:string {:min 1, :gen/max 1}]]]
   [:topic
    {:getter-doc
       "<pre>\nRequired. The name of the topic from which this subscription is receiving\nmessages. Format is `projects/{project}/topics/{topic}`. The value of this\nfield will be `_deleted-topic_` if the topic has been deleted.\n</pre>\n\n<code>\nstring topic = 2 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }\n</code>\n\n@return The topic.",
     :setter-doc
       "<pre>\nRequired. The name of the topic from which this subscription is receiving\nmessages. Format is `projects/{project}/topics/{topic}`. The value of this\nfield will be `_deleted-topic_` if the topic has been deleted.\n</pre>\n\n<code>\nstring topic = 2 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }\n</code>\n\n@param value The topic to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:topicMessageRetentionDuration
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. Indicates the minimum duration for which a message is retained\nafter it is published to the subscription's topic. If this field is set,\nmessages published to the subscription's topic in the last\n`topic_message_retention_duration` are always available to subscribers. See\nthe `message_retention_duration` field in `Topic`. This field is set only\nin responses from the server; it is ignored if it is set in any requests.\n</pre>\n\n<code>\n.google.protobuf.Duration topic_message_retention_duration = 17 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>\n\n@return The topicMessageRetentionDuration."}
    :gcp.foreign.com.google.protobuf/Duration]])

(global/include-schema-registry!
  (with-meta {:gcp.pubsub.v1/Subscription schema,
              :gcp.pubsub.v1/Subscription.AnalyticsHubSubscriptionInfo
                AnalyticsHubSubscriptionInfo-schema,
              :gcp.pubsub.v1/Subscription.State State-schema}
    {:gcp.global/name "gcp.pubsub.v1.Subscription"}))