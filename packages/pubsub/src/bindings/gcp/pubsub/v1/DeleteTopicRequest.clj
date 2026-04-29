;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.pubsub.v1.DeleteTopicRequest
  {:doc "<pre>\nRequest for the `DeleteTopic` method.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.DeleteTopicRequest}"
   :file-git-sha "a7edd5b705557bcf72ca47dc8219677ba8595f8c"
   :fqcn "com.google.pubsub.v1.DeleteTopicRequest"
   :gcp.dev/certification {:base-seed 1777049555279
      :manifest "1278e47a-581c-5be4-ab42-9f07d3602a33"
      :passed-stages
        {:smoke 1777049555279 :standard 1777049555280 :stress 1777049555281}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-24T16:52:36.250836388Z"}}
  (:require
   [gcp.global :as global])
  (:import
   (com.google.pubsub.v1 DeleteTopicRequest DeleteTopicRequest$Builder)))

(declare from-edn to-edn)

(defn ^DeleteTopicRequest from-edn
  [arg]
  (global/strict! :gcp.pubsub.v1/DeleteTopicRequest arg)
  (let [builder (DeleteTopicRequest/newBuilder)]
    (when (some? (get arg :topic)) (.setTopic builder (get arg :topic)))
    (.build builder)))

(defn to-edn
  [^DeleteTopicRequest arg]
  {:post [(global/strict! :gcp.pubsub.v1/DeleteTopicRequest %)]}
  (when arg (cond-> {:topic (.getTopic arg)})))

(def schema
  [:map
   {:closed true
    :doc
    "<pre>\nRequest for the `DeleteTopic` method.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.DeleteTopicRequest}"
    :gcp/category :protobuf-message
    :gcp/key :gcp.pubsub.v1/DeleteTopicRequest}
   [:topic
    {:getter-doc
     "<pre>\nRequired. Name of the topic to delete.\nFormat is `projects/{project}/topics/{topic}`.\n</pre>\n\n<code>\nstring topic = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }\n</code>\n\n@return The topic."
     :setter-doc
     "<pre>\nRequired. Name of the topic to delete.\nFormat is `projects/{project}/topics/{topic}`.\n</pre>\n\n<code>\nstring topic = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }\n</code>\n\n@param value The topic to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.pubsub.v1/DeleteTopicRequest schema}
    {:gcp.global/name "gcp.pubsub.v1.DeleteTopicRequest"}))
