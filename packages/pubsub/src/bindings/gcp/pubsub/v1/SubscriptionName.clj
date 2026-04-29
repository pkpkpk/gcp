;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.pubsub.v1.SubscriptionName
  {:doc nil
   :file-git-sha "fe73614cdb6c2aca445c46e917c325cbfee3adfd"
   :fqcn "com.google.pubsub.v1.SubscriptionName"
   :gcp.dev/certification {:base-seed 1777049780440
      :manifest "1278e47a-581c-5be4-ab42-9f07d3602a33"
      :passed-stages
        {:smoke 1777049780440 :standard 1777049780441 :stress 1777049780442}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-24T16:56:21.503690741Z"}}
  (:require
   [gcp.global :as global])
  (:import
   (com.google.pubsub.v1 SubscriptionName SubscriptionName$Builder)))

(declare from-edn to-edn)

(defn ^SubscriptionName from-edn
  [arg]
  (global/strict! :gcp.pubsub.v1/SubscriptionName arg)
  (let [builder (SubscriptionName/newBuilder)]
    (when (some? (get arg :project)) (.setProject builder (get arg :project)))
    (when (some? (get arg :subscription))
      (.setSubscription builder (get arg :subscription)))
    (.build builder)))

(defn to-edn
  [^SubscriptionName arg]
  {:post [(global/strict! :gcp.pubsub.v1/SubscriptionName %)]}
  (when arg
    (cond-> {:project (.getProject arg), :subscription (.getSubscription arg)}
      (seq (.getFieldValuesMap arg)) (assoc :fieldValuesMap
                                       (into {}
                                             (map (fn [[k v]] [(keyword k) v]))
                                             (.getFieldValuesMap arg))))))

(def schema
  [:map
   {:closed true
    :doc nil
    :gcp/category :accessor-with-builder
    :gcp/key :gcp.pubsub.v1/SubscriptionName}
   [:fieldValuesMap {:optional true, :read-only? true}
    [:map-of [:or simple-keyword? [:string {:min 1}]]
     [:string {:min 1, :gen/max 1}]]]
   [:project {:getter-doc nil, :setter-doc nil} [:string {:min 1, :gen/max 1}]]
   [:subscription {:getter-doc nil, :setter-doc nil}
    [:string {:min 1, :gen/max 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.pubsub.v1/SubscriptionName schema}
    {:gcp.global/name "gcp.pubsub.v1.SubscriptionName"}))
