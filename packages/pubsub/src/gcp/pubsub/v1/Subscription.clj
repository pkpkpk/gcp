(ns gcp.pubsub.v1.Subscription
  (:require [clojure.string :as string]
            [gcp.global :as global]
            [gcp.foreign.com.google.protobuf :as p]
            [gcp.pubsub.v1.BigQueryConfig :as BigQueryConfig]
            [gcp.pubsub.v1.CloudStorageConfig :as CloudStorageConfig]
            [gcp.pubsub.v1.DeadLetterPolicy :as DeadLetterPolicy]
            [gcp.pubsub.v1.ExpirationPolicy :as ExpirationPolicy]
            [gcp.pubsub.v1.MessageTransform :as MessageTransform]
            [gcp.pubsub.v1.PushConfig :as PushConfig]
            [gcp.pubsub.v1.RetryPolicy :as RetryPolicy]
            [gcp.pubsub.v1.SubscriptionName :as SubscriptionName]
            [gcp.pubsub.v1.TopicName :as TopicName])
  (:import (com.google.pubsub.v1 Subscription Subscription$AnalyticsHubSubscriptionInfo Subscription$State)))

;https://cloud.google.com/java/docs/reference/google-cloud-pubsub/latest/com.google.pubsub.v1.Subscription

(defn AnalyticsHubInfo:to-edn
  [^Subscription$AnalyticsHubSubscriptionInfo arg]
  (throw (Exception. "unimplemented")))

(defn to-edn [^Subscription arg]
  (when arg
    (cond-> {:name                      (.getName arg)
             :topic                     (.getTopic arg)
             :detached                  (.getDetached arg)
             :enableExactlyOnceDelivery (.getEnableExactlyOnceDelivery arg)
             :enableMessageOrdering     (.getEnableMessageOrdering arg)
             :retainAckedMessages       (.getRetainAckedMessages arg)
             :ackDeadlineSeconds        (.getAckDeadlineSeconds arg)}

            (not (string/blank? (.getFilter arg)))
            (assoc :filter (.getFilter arg))

            (pos? (.getLabelsCount arg))
            (assoc :labels (.getLabelsMap arg))

            (.hasMessageRetentionDuration arg)
            (assoc :messageRetentionDuration (.getMessageRetentionDuration arg))

            (.hasAnalyticsHubSubscriptionInfo arg)
            (assoc :analyticsHubSubscriptionInfo (AnalyticsHubInfo:to-edn (.getAnalyticsHubSubscriptionInfo arg)))

            (.hasBigqueryConfig arg)
            (assoc :bigqueryConfig (BigQueryConfig/to-edn (.getBigqueryConfig arg)))

            (.hasCloudStorageConfig arg)
            (assoc :cloudStorageConfig (CloudStorageConfig/to-edn (.getCloudStorageConfig arg)))

            (.hasExpirationPolicy arg)
            (assoc :expirationPolicy (ExpirationPolicy/to-edn (.getExpirationPolicy arg)))

            (.hasRetryPolicy arg)
            (assoc :retryPolicy (RetryPolicy/to-edn (.getRetryPolicy arg)))

            (.hasDeadLetterPolicy arg)
            (assoc :deadLetterPolicy (DeadLetterPolicy/to-edn (.getDeadLetterPolicy arg)))

            (.hasPushConfig arg)
            (assoc :pushConfig (PushConfig/to-edn (.getPushConfig arg)))

            (.hasTopicMessageRetentionDuration arg)
            (assoc :topicMessageRetentionDuration (.getTopicMessageRetentionDuration arg))

            (not= "STATE_UNSPECIFIED" (.name (.getState arg)))
            (assoc :state (.name (.getState arg))))))

; https://cloud.google.com/pubsub/docs/reference/rest/v1/projects.subscriptions/create
; https://cloud.google.com/java/docs/reference/google-cloud-pubsub/latest/com.google.pubsub.v1.Subscription.Builder

(defn ^Subscription$AnalyticsHubSubscriptionInfo
  AnalyticsHubInfo:from-edn [arg] (throw (Exception. "unimplemented")))

(defn ^Subscription from-edn [arg]
  (global/strict! :gcp.pubsub.v1/Subscription arg)
  (let [builder (Subscription/newBuilder)]
    (.setName builder (if (map? (:name arg))
                        (str (SubscriptionName/from-edn (:name arg)))
                        (:name arg)))
    (.setTopic builder (if (map? (:topic arg))
                         (str (TopicName/from-edn (:topic arg)))
                         (:topic arg)))
    #!--optional --------
    (when (contains? arg :ackDeadlineSeconds)
      (.setAckDeadlineSeconds builder (global/coerce :int (:ackDeadlineSeconds arg))))
    (when-let [cfg (get arg :bigqueryConfig)]
      (.setBigqueryConfig builder (BigQueryConfig/from-edn cfg)))
    (when (contains? arg :cloudStorageConfig)
      (.setCloudStorageConfig builder (CloudStorageConfig/from-edn (:cloudStorageConfig arg))))
    (when (contains? arg :deadLetterPolicy)
      (.setDeadLetterPolicy builder (DeadLetterPolicy/from-edn (:deadLetterPolicy arg))))
    (when (contains? arg :expirationPolicy)
      (.setExpirationPolicy builder (ExpirationPolicy/from-edn (:expirationPolicy arg))))
    (when (contains? arg :retryPolicy)
      (.setRetryPolicy builder (RetryPolicy/from-edn (:retryPolicy arg))))
    (when-let [state (:state arg)]
      (.setState builder (if (number? state)
                           (Subscription$State/forNumber state)
                           (Subscription$State/valueOf ^String state))))
    (when (contains? arg :enableMessageOrdering)
      (.setEnableMessageOrdering builder (:enableMessageOrdering arg)))
    (when (contains? arg :enableExactlyOnceDelivery)
      (.setEnableExactlyOnceDelivery builder (:enableExactlyOnceDelivery arg)))
    (when (contains? arg :detached)
      (.setDetached arg (:detached arg)))
    (when (contains? arg :filter)
      (.setFilter builder (:filter arg)))
    (when (contains? arg :topicMessageRetentionDuration)
      (.setTopicMessageRetentionDuration builder (p/Duration-from-edn (:topicMessageRetentionDuration arg))))
    (when (contains? arg :retainAckedMessages)
      (.setRetainAckedMessages builder (:retainAckedMessages arg)))
    (when (contains? arg :analyticsHubSubscriptionInfo)
      (.setAnalyticsHubSubscriptionInfo builder (AnalyticsHubInfo:from-edn (:analyticsHubSubscriptionInfo arg))))
    (when (contains? arg :pushConfig)
      (.setPushConfig arg (PushConfig/from-edn (:pushConfig arg))))
    (when (contains? arg :messageRetentionDuration)
      (.setMessageRetentionDuration arg (p/Duration-from-edn (:messageRetentionDuration arg))))
    (when (contains? arg :messageTransforms)
      (.addAllMessageTransforms arg (map MessageTransform/from-edn (:messageTransforms arg))))
    (.build builder)))

(def schemas
  {:gcp.pubsub.v1/synth.SubscriptionPath
   [:and
    :string
    [:fn
     {:error/message "string representations of subscriptions must be in form 'projects/{project}/subscriptions/{subscription}'"}
     '(fn [s]
        (let [parts (clojure.string/split s #"/")]
          (and
            (= "projects" (nth parts 0 nil))
            (some? (nth parts 1 nil))
            (= "subscriptions" (nth parts 2 nil))
            (some? (nth parts 3 nil)))))]]
   :gcp.pubsub.v1/Subscription
   [:map
    [:name [:or
            :gcp.pubsub.v1/synth.SubscriptionPath
            :gcp.pubsub.v1/SubscriptionName]]
    [:topic [:or
             :gcp.pubsub.v1/synth.TopicPath
             :gcp.pubsub.v1/TopicName]]
    [:ackDeadlineSeconds {:optional true} :int]
    [:bigqueryConfig {:optional true} :gcp.pubsub.v1/BigQueryConfig]
    [:cloudStorageConfig {:optional true} :gcp.pubsub.v1/CloudStorageConfig]
    [:deadLetterPolicy {:optional true} :gcp.pubsub.v1/DeadLetterPolicy]
    [:expirationPolicy {:optional true} :gcp.pubsub.v1/ExpirationPolicy]
    [:retryPolicy {:optional true} :gcp.pubsub.v1/RetryPolicy]
    [:state {:optional true} [:enum "STATE_UNSPECIFIED" "ACTIVE" "RESOURCE_ERROR"]]
    [:enableMessageOrdering {:optional true} :boolean]
    [:enableExactlyOnceDelivery {:optional true} :boolean]
    [:detached {:optional true} :boolean]
    [:filter {:optional true} :string]
    [:topicMessageRetentionDuration {:optional true} :any]
    [:retainAckedMessages {:optional true} :boolean]
    [:analyticsHubSubscriptionInfo {:optional true} :any]
    [:pushConfig {:optional true} :gcp.pubsub.v1/PushConfig]
    [:messageRetentionDuration {:optional true} :any]
    [:messageTransforms {:optional true} [:sequential :gcp.pubsub.v1/MessageTransform]]
    ]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))