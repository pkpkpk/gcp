;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.pubsub.v1.ListTopicSubscriptionsRequest
  {:doc "<pre>\nRequest for the `ListTopicSubscriptions` method.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.ListTopicSubscriptionsRequest}"
   :file-git-sha "a7edd5b705557bcf72ca47dc8219677ba8595f8c"
   :fqcn "com.google.pubsub.v1.ListTopicSubscriptionsRequest"
   :gcp.dev/certification {:base-seed 1777049728144
      :manifest "1278e47a-581c-5be4-ab42-9f07d3602a33"
      :passed-stages
        {:smoke 1777049728144 :standard 1777049728145 :stress 1777049728146}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-24T16:55:29.109372455Z"}}
  (:require
   [gcp.global :as global])
  (:import
   (com.google.pubsub.v1 ListTopicSubscriptionsRequest ListTopicSubscriptionsRequest$Builder)))

(declare from-edn to-edn)

(defn ^ListTopicSubscriptionsRequest from-edn
  [arg]
  (global/strict! :gcp.pubsub.v1/ListTopicSubscriptionsRequest arg)
  (let [builder (ListTopicSubscriptionsRequest/newBuilder)]
    (when (some? (get arg :pageSize))
      (.setPageSize builder (int (get arg :pageSize))))
    (when (some? (get arg :pageToken))
      (.setPageToken builder (get arg :pageToken)))
    (when (some? (get arg :topic)) (.setTopic builder (get arg :topic)))
    (.build builder)))

(defn to-edn
  [^ListTopicSubscriptionsRequest arg]
  {:post [(global/strict! :gcp.pubsub.v1/ListTopicSubscriptionsRequest %)]}
  (when arg
    (cond-> {:topic (.getTopic arg)}
      (.getPageSize arg) (assoc :pageSize (.getPageSize arg))
      (some->> (.getPageToken arg)
               (not= ""))
      (assoc :pageToken (.getPageToken arg)))))

(def schema
  [:map
   {:closed true
    :doc
    "<pre>\nRequest for the `ListTopicSubscriptions` method.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.ListTopicSubscriptionsRequest}"
    :gcp/category :protobuf-message
    :gcp/key :gcp.pubsub.v1/ListTopicSubscriptionsRequest}
   [:pageSize
    {:optional true
     :getter-doc
     "<pre>\nOptional. Maximum number of subscription names to return.\n</pre>\n\n<code>int32 page_size = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The pageSize."
     :setter-doc
     "<pre>\nOptional. Maximum number of subscription names to return.\n</pre>\n\n<code>int32 page_size = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The pageSize to set.\n@return This builder for chaining."}
    :i32]
   [:pageToken
    {:optional true
     :getter-doc
     "<pre>\nOptional. The value returned by the last `ListTopicSubscriptionsResponse`;\nindicates that this is a continuation of a prior `ListTopicSubscriptions`\ncall, and that the system should return the next page of data.\n</pre>\n\n<code>string page_token = 3 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The pageToken."
     :setter-doc
     "<pre>\nOptional. The value returned by the last `ListTopicSubscriptionsResponse`;\nindicates that this is a continuation of a prior `ListTopicSubscriptions`\ncall, and that the system should return the next page of data.\n</pre>\n\n<code>string page_token = 3 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The pageToken to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:topic
    {:getter-doc
     "<pre>\nRequired. The name of the topic that subscriptions are attached to.\nFormat is `projects/{project}/topics/{topic}`.\n</pre>\n\n<code>\nstring topic = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }\n</code>\n\n@return The topic."
     :setter-doc
     "<pre>\nRequired. The name of the topic that subscriptions are attached to.\nFormat is `projects/{project}/topics/{topic}`.\n</pre>\n\n<code>\nstring topic = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }\n</code>\n\n@param value The topic to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.pubsub.v1/ListTopicSubscriptionsRequest schema}
    {:gcp.global/name "gcp.pubsub.v1.ListTopicSubscriptionsRequest"}))
