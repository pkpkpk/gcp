;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.pubsub.v1.DeleteSubscriptionRequest
  {:doc
     "<pre>\nRequest for the DeleteSubscription method.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.DeleteSubscriptionRequest}"
   :file-git-sha "a7edd5b705557bcf72ca47dc8219677ba8595f8c"
   :fqcn "com.google.pubsub.v1.DeleteSubscriptionRequest"
   :gcp.dev/certification
     {:base-seed 1777403413321
      :manifest "1278e47a-581c-5be4-ab42-9f07d3602a33"
      :passed-stages
        {:smoke 1777403413321 :standard 1777403413322 :stress 1777403413323}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-28T19:10:15.187763015Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.pubsub.v1 DeleteSubscriptionRequest
            DeleteSubscriptionRequest$Builder]))

(declare from-edn to-edn)

(defn ^DeleteSubscriptionRequest from-edn
  [arg]
  (global/strict! :gcp.pubsub.v1/DeleteSubscriptionRequest arg)
  (let [builder (DeleteSubscriptionRequest/newBuilder)]
    (when (some? (get arg :subscription))
      (.setSubscription builder (get arg :subscription)))
    (.build builder)))

(defn to-edn
  [^DeleteSubscriptionRequest arg]
  {:post [(global/strict! :gcp.pubsub.v1/DeleteSubscriptionRequest %)]}
  (when arg (cond-> {:subscription (.getSubscription arg)})))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nRequest for the DeleteSubscription method.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.DeleteSubscriptionRequest}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.pubsub.v1/DeleteSubscriptionRequest}
   [:subscription
    {:getter-doc
       "<pre>\nRequired. The subscription to delete.\nFormat is `projects/{project}/subscriptions/{sub}`.\n</pre>\n\n<code>\nstring subscription = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }\n</code>\n\n@return The subscription.",
     :setter-doc
       "<pre>\nRequired. The subscription to delete.\nFormat is `projects/{project}/subscriptions/{sub}`.\n</pre>\n\n<code>\nstring subscription = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }\n</code>\n\n@param value The subscription to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.pubsub.v1/DeleteSubscriptionRequest schema}
    {:gcp.global/name "gcp.pubsub.v1.DeleteSubscriptionRequest"}))