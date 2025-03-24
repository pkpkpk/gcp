(ns gcp.pubsub.v1
  (:require [gcp.global :as g]))

(def registry
  ^{::g/name ::registry}
  {:gcp/pubsub.SubscriptionAdminClient   [:maybe {:from-edn 'gcp.pubsub.v1.SubscriptionAdminClient/from-edn}
                                          [:or
                                           :gcp/pubsub.SubscriptionAdminSettings
                                           :gcp/pubsub.SubscriberStub]]

   :gcp/pubsub.TopicAdminClient          [:any {:from-edn 'gcp.pubsub.v1.TopicAdminClient/from-edn}]

   #!----------------------------------------------------------------
   #! ops

   :gcp/pubsub.synth.SubscriptionList    :any

   :gcp/pubsub.synth.SubscriptionCreate  :any

   :gcp/pubsub.synth.SubscriptionDelete  :any

   :gcp/pubsub.synth.TopicList
   [:and
    [:map [:topicAdminClient {:optional true} :gcp/pubsub.TopicAdminClient]]
    [:or
     [:map [:project [:or :gcp/pubsub.ProjectName :string]]]
     [:map [:request :gcp/pubsub.ListTopicsRequest]]]]

   :gcp/pubsub.synth.TopicListSubscriptions
   [:and
    [:map [:topicAdminClient {:optional true} :gcp/pubsub.TopicAdminClient]]
    [:or
     [:map [:topicName :gcp/pubsub.TopicName]]
     [:map [:request :gcp/pubsub.ListTopicSubscriptionsRequest]]]]

   :gcp/pubsub.synth.TopicCreate         :any

   :gcp/pubsub.synth.TopicDelete         :any

   #!----------------------------------------------------------------
   :gcp/pubsub.BigQueryConfig                :any
   :gcp/pubsub.CloudStorageConfig            :any
   :gcp/pubsub.ExpirationPolicy              :any
   :gcp/pubsub.DeadLetterPolicy              :any
   :gcp/pubsub.RetryPolicy                   :any

   :gcp/pubsub.SubscriptionAdminSettings     :any
   :gcp/pubsub.SubscriberStub                :any
   :gcp/pubsub.TopicAdminSettings            :any
   :gcp/pubsub.PublisherStub                 :any
   :gcp/pubsub.ListTopicsRequest             :any
   :gcp/pubsub.ListTopicSubscriptionsRequest :any
   #!----------------------------------------------------------------
   :gcp/pubsub.ProjectName         [:or
                                    :string
                                    [:map {:closed true} [:project :string]]]

   :gcp/pubsub.TopicName                 [:map {:closed true}
                                          [:project :string]
                                          [:topic :string]]
   :gcp/pubsub.Publisher                 [:map {:closed true}
                                          [:topicName :gcp/pubsub.TopicName]
                                          [:batchSettings {:optional true} :gax/batching.BatchingSettings]]

   #!----------------------------------------------------------------
   #! TODO gcp.gax in global
   :gax/batching.FlowControlSettings     :any

   :gax/batching.BatchingSettings
   [:map {:closed true}
    [:delayThreshold {:optional true} :int]
    [:elementCountThreshold {:optional true} :int]
    [:requestByteThreshold {:optional true} :int]
    [:enabled {:optional true} :boolean]
    [:flowControlSettings {:optional true} :gax/batching.FlowControlSettings]]})

(g/include-schema-registry! registry)