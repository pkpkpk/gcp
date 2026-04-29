;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.pubsub.v1.GetSubscriptionRequest
  {:doc "<pre>\nRequest for the GetSubscription method.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.GetSubscriptionRequest}"
   :file-git-sha "a7edd5b705557bcf72ca47dc8219677ba8595f8c"
   :fqcn "com.google.pubsub.v1.GetSubscriptionRequest"
   :gcp.dev/certification {:base-seed 1777049627135
      :manifest "1278e47a-581c-5be4-ab42-9f07d3602a33"
      :passed-stages
        {:smoke 1777049627135 :standard 1777049627136 :stress 1777049627137}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-24T16:53:48.023576561Z"}}
  (:require
   [gcp.global :as global])
  (:import
   (com.google.pubsub.v1 GetSubscriptionRequest GetSubscriptionRequest$Builder)))

(declare from-edn to-edn)

(defn ^GetSubscriptionRequest from-edn
  [arg]
  (global/strict! :gcp.pubsub.v1/GetSubscriptionRequest arg)
  (let [builder (GetSubscriptionRequest/newBuilder)]
    (when (some? (get arg :subscription))
      (.setSubscription builder (get arg :subscription)))
    (.build builder)))

(defn to-edn
  [^GetSubscriptionRequest arg]
  {:post [(global/strict! :gcp.pubsub.v1/GetSubscriptionRequest %)]}
  (when arg (cond-> {:subscription (.getSubscription arg)})))

(def schema
  [:map
   {:closed true
    :doc
    "<pre>\nRequest for the GetSubscription method.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.GetSubscriptionRequest}"
    :gcp/category :protobuf-message
    :gcp/key :gcp.pubsub.v1/GetSubscriptionRequest}
   [:subscription
    {:getter-doc
     "<pre>\nRequired. The name of the subscription to get.\nFormat is `projects/{project}/subscriptions/{sub}`.\n</pre>\n\n<code>\nstring subscription = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }\n</code>\n\n@return The subscription."
     :setter-doc
     "<pre>\nRequired. The name of the subscription to get.\nFormat is `projects/{project}/subscriptions/{sub}`.\n</pre>\n\n<code>\nstring subscription = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }\n</code>\n\n@param value The subscription to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.pubsub.v1/GetSubscriptionRequest schema}
    {:gcp.global/name "gcp.pubsub.v1.GetSubscriptionRequest"}))
