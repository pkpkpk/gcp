;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.pubsub.v1.ProjectTopicName
  {:doc "AUTO-GENERATED DOCUMENTATION AND CLASS"
   :file-git-sha "51ea70c11720f66aa807ae6528dd6c0abc322d9f"
   :fqcn "com.google.pubsub.v1.ProjectTopicName"}
  (:require
   [gcp.global :as global])
  (:import
   (com.google.pubsub.v1 ProjectTopicName ProjectTopicName$Builder)))

(declare from-edn to-edn)

(defn ^ProjectTopicName from-edn
  [arg]
  (global/strict! :gcp.pubsub.v1/ProjectTopicName arg)
  (let [builder (ProjectTopicName/newBuilder)]
    (when (some? (get arg :project)) (.setProject builder (get arg :project)))
    (when (some? (get arg :topic)) (.setTopic builder (get arg :topic)))
    (.build builder)))

(defn to-edn
  [^ProjectTopicName arg]
  {:post [(global/strict! :gcp.pubsub.v1/ProjectTopicName %)]}
  (when arg
    (cond-> {}
      (seq (.getFieldValuesMap arg))
      (assoc :fieldValuesMap
        (into {} (map (fn [[k v]] [(keyword k) v])) (.getFieldValuesMap arg)))
      (some->> (.getProject arg)
               (not= ""))
      (assoc :project (.getProject arg))
      (some->> (.getTopic arg)
               (not= ""))
      (assoc :topic (.getTopic arg)))))

(def schema
  [:map
   {:closed true
    :doc "AUTO-GENERATED DOCUMENTATION AND CLASS"
    :gcp/category :accessor-with-builder
    :gcp/key :gcp.pubsub.v1/ProjectTopicName}
   [:fieldValuesMap {:optional true, :read-only? true}
    [:map-of [:or simple-keyword? [:string {:min 1}]]
     [:string {:min 1, :gen/max 1}]]]
   [:project {:optional true, :setter-doc nil} [:string {:min 1, :gen/max 1}]]
   [:topic {:optional true, :setter-doc nil} [:string {:min 1, :gen/max 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.pubsub.v1/ProjectTopicName schema}
    {:gcp.global/name "gcp.pubsub.v1.ProjectTopicName"}))
