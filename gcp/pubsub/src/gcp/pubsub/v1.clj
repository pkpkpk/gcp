(ns gcp.pubsub.v1
  (:require [gcp.global :as g]))

(def registry
  ^{::g/name ::registry}
  {:gcp/pubsub.SubscriptionAdminClient :any
   :gcp/pubsub.TopicAdminClient        :any
   #!----------------------------------------------------------------
   #! TODO gcp.gax in global
   :gax/batching.FlowControlSettings   :any

   :gax/batching.BatchingSettings
   [:map {:closed true}
    [:delayThreshold        {:optional true} :int]
    [:elementCountThreshold {:optional true} :int]
    [:requestByteThreshold  {:optional true} :int]
    [:enabled               {:optional true} :boolean]
    [:flowControlSettings   {:optional true} :gax/batching.FlowControlSettings]]
   #!----------------------------------------------------------------
   :gcp/pubsub.TopicName               [:map {:closed true}
                                        [:project :string]
                                        [:topic :string]]
   :gcp/pubsub.Publisher               [:map {:closed true}
                                        [:topicName :gcp/pubsub.TopicName]
                                        [:batchSettings {:optional true} :gax/batching.BatchingSettings]]})

(g/include-schema-registry! registry)