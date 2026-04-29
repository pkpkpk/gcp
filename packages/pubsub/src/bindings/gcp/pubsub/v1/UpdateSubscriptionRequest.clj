;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.pubsub.v1.UpdateSubscriptionRequest
  {:doc
     "<pre>\nRequest for the UpdateSubscription method.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.UpdateSubscriptionRequest}"
   :file-git-sha "a7edd5b705557bcf72ca47dc8219677ba8595f8c"
   :fqcn "com.google.pubsub.v1.UpdateSubscriptionRequest"
   :gcp.dev/certification
     {:base-seed 1777403457006
      :manifest "1278e47a-581c-5be4-ab42-9f07d3602a33"
      :passed-stages
        {:smoke 1777403457006 :standard 1777403457007 :stress 1777403457008}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-28T19:10:59.140732708Z"}}
  (:require [gcp.global :as global]
            [gcp.pubsub.v1.Subscription :as Subscription])
  (:import [com.google.pubsub.v1 UpdateSubscriptionRequest
            UpdateSubscriptionRequest$Builder]))

(declare from-edn to-edn)

(defn ^UpdateSubscriptionRequest from-edn
  [arg]
  (global/strict! :gcp.pubsub.v1/UpdateSubscriptionRequest arg)
  (let [builder (UpdateSubscriptionRequest/newBuilder)]
    (when (some? (get arg :subscription))
      (.setSubscription builder
                        (Subscription/from-edn (get arg :subscription))))
    (.build builder)))

(defn to-edn
  [^UpdateSubscriptionRequest arg]
  {:post [(global/strict! :gcp.pubsub.v1/UpdateSubscriptionRequest %)]}
  (when arg
    (cond-> {:subscription (Subscription/to-edn (.getSubscription arg))})))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nRequest for the UpdateSubscription method.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.UpdateSubscriptionRequest}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.pubsub.v1/UpdateSubscriptionRequest}
   [:subscription
    {:getter-doc
       "<pre>\nRequired. The updated subscription object.\n</pre>\n\n<code>\n.google.pubsub.v1.Subscription subscription = 1 [(.google.api.field_behavior) = REQUIRED];\n</code>\n\n@return The subscription.",
     :setter-doc
       "<pre>\nRequired. The updated subscription object.\n</pre>\n\n<code>\n.google.pubsub.v1.Subscription subscription = 1 [(.google.api.field_behavior) = REQUIRED];\n</code>"}
    :gcp.pubsub.v1/Subscription]])

(global/include-schema-registry!
  (with-meta {:gcp.pubsub.v1/UpdateSubscriptionRequest schema}
    {:gcp.global/name "gcp.pubsub.v1.UpdateSubscriptionRequest"}))