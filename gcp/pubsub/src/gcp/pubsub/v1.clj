(ns gcp.pubsub.v1
  (:require [gcp.global :as g]))

(def registry
  ^{::g/name ::registry}
  {:gcp/pubsub.SubscriptionAdminClient       [:maybe {:from-edn 'gcp.pubsub.v1.SubscriptionAdminClient/from-edn}
                                              [:or
                                               :gcp/pubsub.SubscriptionAdminSettings
                                               :gcp/pubsub.SubscriberStub]]

   :gcp/pubsub.TopicAdminClient              [:any {:from-edn 'gcp.pubsub.v1.TopicAdminClient/from-edn}]

   :gcp/pubsub.SubscriptionAdminSettings     :any
   :gcp/pubsub.TopicAdminSettings            :any
   :gcp/pubsub.SubscriberStub                :any
   :gcp/pubsub.PublisherStub                 :any

   #!----------------------------------------------------------------
   #! ops

   :gcp/pubsub.synth.SubscriptionList        :any

   :gcp/pubsub.synth.SubscriptionCreate      :any

   :gcp/pubsub.synth.SubscriptionDelete      :any



   :gcp/pubsub.synth.TopicList
   [:and
    [:map [:topicAdminClient {:optional true} :gcp/pubsub.TopicAdminClient]]
    [:or
     [:map [:project [:or :gcp/pubsub.ProjectName :string]]]
     [:map [:request :gcp/pubsub.ListTopicsRequest]]]]

   :gcp/pubsub.synth.TopicGet
   [:and
    [:map [:topicAdminClient {:optional true} :gcp/pubsub.TopicAdminClient]]
    [:or
     [:map [:topic [:or :gcp/pubsub.TopicName
                        :gcp/pubsub.synth.TopicPath]]]
     [:map [:request :gcp/pubsub.GetTopicRequest]]]]

   :gcp/pubsub.synth.TopicCreate
   [:and
    [:map [:topicAdminClient {:optional true} :gcp/pubsub.TopicAdminClient]]
    [:or
     [:map [:topic [:or :gcp/pubsub.TopicName :gcp/pubsub.synth.TopicPath]]]
     [:map [:request :gcp/pubsub.Topic]]]]

   :gcp/pubsub.synth.TopicListSubscriptions
   [:and
    [:map [:topicAdminClient {:optional true} :gcp/pubsub.TopicAdminClient]]
    [:or
     [:map [:topic [:or :gcp/pubsub.TopicName :gcp/pubsub.synth.TopicPath]]]
     [:map [:request :gcp/pubsub.ListTopicSubscriptionsRequest]]]]

   :gcp/pubsub.synth.TopicDelete
   [:and
    [:map [:topicAdminClient {:optional true} :gcp/pubsub.TopicAdminClient]]
    [:or
     [:map [:topic [:or :gcp/pubsub.TopicName :gcp/pubsub.synth.TopicPath]]]
     [:map [:request :gcp/pubsub.DeleteTopicRequest]]]]

   #!----------------------------------------------------------------
   :gcp/pubsub.BigQueryConfig                :any
   :gcp/pubsub.CloudStorageConfig            :any
   :gcp/pubsub.ExpirationPolicy              :any
   :gcp/pubsub.DeadLetterPolicy              :any
   :gcp/pubsub.RetryPolicy                   :any

   :gcp/pubsub.DeleteTopicRequest            :any
   :gcp/pubsub.GetTopicRequest               :any
   :gcp/pubsub.ListTopicsRequest             :any
   :gcp/pubsub.ListTopicSubscriptionsRequest :any
   #!----------------------------------------------------------------

   :gcp/pubsub.ProjectName
   [:or
    :string
    [:map {:closed true} [:project :string]]]

   :gcp/pubsub.synth.TopicPath
   [:and
    :string
    [:fn
     {:error/message "string representations of topics must be in form 'projects/{project}/topics/{topic}'"}
     '(fn [s]
        (let [parts (clojure.string/split s (re-pattern "/"))]
          (and
            (= "projects" (nth parts 0))
            (some? (nth parts 1))
            (= "topics" (nth parts 2))
            (some? (nth parts 3)))))]]

   :gcp/pubsub.TopicName
   [:map {:closed true}
    [:project :string]
    [:topic :string]]

   :gcp/pubsub.Topic
   [:map
    [:name :gcp/pubsub.synth.TopicPath]]

   :gcp/pubsub.Publisher
   [:map {:closed true}
    [:topicName :gcp/pubsub.TopicName]
    [:batchSettings {:optional true} :gax/batching.BatchingSettings]]

   #!----------------------------------------------------------------
   #! TODO gcp.gax in global
   :gax/batching.FlowControlSettings         :any

   :gax/batching.BatchingSettings
   [:map {:closed true}
    [:delayThreshold {:optional true} :int]
    [:elementCountThreshold {:optional true} :int]
    [:requestByteThreshold {:optional true} :int]
    [:enabled {:optional true} :boolean]
    [:flowControlSettings {:optional true} :gax/batching.FlowControlSettings]]})

(g/include-schema-registry! registry)