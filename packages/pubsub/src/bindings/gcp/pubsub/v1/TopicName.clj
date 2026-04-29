;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.pubsub.v1.TopicName
  {:doc nil
   :file-git-sha "fe73614cdb6c2aca445c46e917c325cbfee3adfd"
   :fqcn "com.google.pubsub.v1.TopicName"
   :gcp.dev/certification {:base-seed 1777031126250
      :manifest "1278e47a-581c-5be4-ab42-9f07d3602a33"
      :passed-stages
        {:smoke 1777031126250 :standard 1777031126251 :stress 1777031126252}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-24T11:45:27.361902168Z"}}
  (:require
   [gcp.global :as global])
  (:import
   (com.google.pubsub.v1 TopicName TopicName$Builder)))

(declare from-edn to-edn)

(defn ^TopicName from-edn
  [arg]
  (global/strict! :gcp.pubsub.v1/TopicName arg)
  (let [builder (TopicName/newBuilder)]
    (when (some? (get arg :project)) (.setProject builder (get arg :project)))
    (when (some? (get arg :topic)) (.setTopic builder (get arg :topic)))
    (.build builder)))

(defn to-edn
  [^TopicName arg]
  {:post [(global/strict! :gcp.pubsub.v1/TopicName %)]}
  (when arg
    (cond-> {:project (.getProject arg), :topic (.getTopic arg)}
      (seq (.getFieldValuesMap arg)) (assoc :fieldValuesMap
                                       (into {}
                                             (map (fn [[k v]] [(keyword k) v]))
                                             (.getFieldValuesMap arg))))))

(def schema
  [:map
   {:closed true
    :doc nil
    :gcp/category :accessor-with-builder
    :gcp/key :gcp.pubsub.v1/TopicName}
   [:fieldValuesMap {:optional true, :read-only? true}
    [:map-of [:or simple-keyword? [:string {:min 1}]]
     [:string {:min 1, :gen/max 1}]]]
   [:project {:getter-doc nil, :setter-doc nil} [:string {:min 1, :gen/max 1}]]
   [:topic {:getter-doc nil, :setter-doc nil} [:string {:min 1, :gen/max 1}]]])

(global/include-schema-registry! (with-meta {:gcp.pubsub.v1/TopicName schema}
                                   {:gcp.global/name
                                    "gcp.pubsub.v1.TopicName"}))
