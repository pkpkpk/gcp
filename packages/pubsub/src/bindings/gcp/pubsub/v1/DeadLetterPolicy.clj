;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.pubsub.v1.DeadLetterPolicy
  {:doc
     "<pre>\nDead lettering is done on a best effort basis. The same message might be\ndead lettered multiple times.\n\nIf validation on any of the fields fails at subscription creation/updation,\nthe create/update subscription request will fail.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.DeadLetterPolicy}"
   :file-git-sha "a7edd5b705557bcf72ca47dc8219677ba8595f8c"
   :fqcn "com.google.pubsub.v1.DeadLetterPolicy"
   :gcp.dev/certification
     {:base-seed 1777403442793
      :manifest "1278e47a-581c-5be4-ab42-9f07d3602a33"
      :passed-stages
        {:smoke 1777403442793 :standard 1777403442794 :stress 1777403442795}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-28T19:10:44.208479400Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.pubsub.v1 DeadLetterPolicy DeadLetterPolicy$Builder]))

(declare from-edn to-edn)

(defn ^DeadLetterPolicy from-edn
  [arg]
  (global/strict! :gcp.pubsub.v1/DeadLetterPolicy arg)
  (let [builder (DeadLetterPolicy/newBuilder)]
    (when (some? (get arg :deadLetterTopic))
      (.setDeadLetterTopic builder (get arg :deadLetterTopic)))
    (when (some? (get arg :maxDeliveryAttempts))
      (.setMaxDeliveryAttempts builder (int (get arg :maxDeliveryAttempts))))
    (.build builder)))

(defn to-edn
  [^DeadLetterPolicy arg]
  {:post [(global/strict! :gcp.pubsub.v1/DeadLetterPolicy %)]}
  (when arg
    (cond-> {}
      (some->> (.getDeadLetterTopic arg)
               (not= ""))
        (assoc :deadLetterTopic (.getDeadLetterTopic arg))
      (.getMaxDeliveryAttempts arg) (assoc :maxDeliveryAttempts
                                      (.getMaxDeliveryAttempts arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nDead lettering is done on a best effort basis. The same message might be\ndead lettered multiple times.\n\nIf validation on any of the fields fails at subscription creation/updation,\nthe create/update subscription request will fail.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.DeadLetterPolicy}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.pubsub.v1/DeadLetterPolicy}
   [:deadLetterTopic
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The name of the topic to which dead letter messages should be\npublished. Format is `projects/{project}/topics/{topic}`.The Pub/Sub\nservice account associated with the enclosing subscription's parent project\n(i.e., service-{project_number}&#64;gcp-sa-pubsub.iam.gserviceaccount.com) must\nhave permission to Publish() to this topic.\n\nThe operation will fail if the topic does not exist.\nUsers should ensure that there is a subscription attached to this topic\nsince messages published to a topic with no subscriptions are lost.\n</pre>\n\n<code>\nstring dead_letter_topic = 1 [(.google.api.field_behavior) = OPTIONAL, (.google.api.resource_reference) = { ... }\n</code>\n\n@return The deadLetterTopic.",
     :setter-doc
       "<pre>\nOptional. The name of the topic to which dead letter messages should be\npublished. Format is `projects/{project}/topics/{topic}`.The Pub/Sub\nservice account associated with the enclosing subscription's parent project\n(i.e., service-{project_number}&#64;gcp-sa-pubsub.iam.gserviceaccount.com) must\nhave permission to Publish() to this topic.\n\nThe operation will fail if the topic does not exist.\nUsers should ensure that there is a subscription attached to this topic\nsince messages published to a topic with no subscriptions are lost.\n</pre>\n\n<code>\nstring dead_letter_topic = 1 [(.google.api.field_behavior) = OPTIONAL, (.google.api.resource_reference) = { ... }\n</code>\n\n@param value The deadLetterTopic to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:maxDeliveryAttempts
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The maximum number of delivery attempts for any message. The\nvalue must be between 5 and 100.\n\nThe number of delivery attempts is defined as 1 + (the sum of number of\nNACKs and number of times the acknowledgment deadline has been exceeded\nfor the message).\n\nA NACK is any call to ModifyAckDeadline with a 0 deadline. Note that\nclient libraries may automatically extend ack_deadlines.\n\nThis field will be honored on a best effort basis.\n\nIf this parameter is 0, a default value of 5 is used.\n</pre>\n\n<code>int32 max_delivery_attempts = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The maxDeliveryAttempts.",
     :setter-doc
       "<pre>\nOptional. The maximum number of delivery attempts for any message. The\nvalue must be between 5 and 100.\n\nThe number of delivery attempts is defined as 1 + (the sum of number of\nNACKs and number of times the acknowledgment deadline has been exceeded\nfor the message).\n\nA NACK is any call to ModifyAckDeadline with a 0 deadline. Note that\nclient libraries may automatically extend ack_deadlines.\n\nThis field will be honored on a best effort basis.\n\nIf this parameter is 0, a default value of 5 is used.\n</pre>\n\n<code>int32 max_delivery_attempts = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The maxDeliveryAttempts to set.\n@return This builder for chaining."}
    :i32]])

(global/include-schema-registry!
  (with-meta {:gcp.pubsub.v1/DeadLetterPolicy schema}
    {:gcp.global/name "gcp.pubsub.v1.DeadLetterPolicy"}))