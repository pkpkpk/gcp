;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.pubsub.v1.GetTopicRequest
  {:doc "<pre>\nRequest for the GetTopic method.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.GetTopicRequest}"
   :file-git-sha "a7edd5b705557bcf72ca47dc8219677ba8595f8c"
   :fqcn "com.google.pubsub.v1.GetTopicRequest"
   :gcp.dev/certification {:base-seed 1777049653747
      :manifest "1278e47a-581c-5be4-ab42-9f07d3602a33"
      :passed-stages
        {:smoke 1777049653747 :standard 1777049653748 :stress 1777049653749}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-24T16:54:14.667948445Z"}}
  (:require
   [gcp.global :as global])
  (:import
   (com.google.pubsub.v1 GetTopicRequest GetTopicRequest$Builder)))

(declare from-edn to-edn)

(defn ^GetTopicRequest from-edn
  [arg]
  (global/strict! :gcp.pubsub.v1/GetTopicRequest arg)
  (let [builder (GetTopicRequest/newBuilder)]
    (when (some? (get arg :topic)) (.setTopic builder (get arg :topic)))
    (.build builder)))

(defn to-edn
  [^GetTopicRequest arg]
  {:post [(global/strict! :gcp.pubsub.v1/GetTopicRequest %)]}
  (when arg (cond-> {:topic (.getTopic arg)})))

(def schema
  [:map
   {:closed true
    :doc
    "<pre>\nRequest for the GetTopic method.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.GetTopicRequest}"
    :gcp/category :protobuf-message
    :gcp/key :gcp.pubsub.v1/GetTopicRequest}
   [:topic
    {:getter-doc
     "<pre>\nRequired. The name of the topic to get.\nFormat is `projects/{project}/topics/{topic}`.\n</pre>\n\n<code>\nstring topic = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }\n</code>\n\n@return The topic."
     :setter-doc
     "<pre>\nRequired. The name of the topic to get.\nFormat is `projects/{project}/topics/{topic}`.\n</pre>\n\n<code>\nstring topic = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }\n</code>\n\n@param value The topic to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.pubsub.v1/GetTopicRequest schema}
    {:gcp.global/name "gcp.pubsub.v1.GetTopicRequest"}))
