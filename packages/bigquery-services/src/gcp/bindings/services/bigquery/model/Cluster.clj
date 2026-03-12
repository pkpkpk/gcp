;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.services.bigquery.model.Cluster
  {:doc
     "Message containing the information about one cluster.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.Cluster"
   :gcp.dev/certification
     {:base-seed 1772390239893
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1772390239893 :standard 1772390239894 :stress 1772390239895}
      :protocol-hash
        "f22c161c7a00aa071f6a8b78764e5c684eaa491c76675456f17e0b44b2b8578c"
      :timestamp "2026-03-01T18:37:19.999828886Z"}}
  (:require [gcp.bindings.services.bigquery.model.FeatureValue :as FeatureValue]
            [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model Cluster]))

(defn ^Cluster from-edn
  [arg]
  (global/strict! :gcp.bindings.services.bigquery.model/Cluster arg)
  (let [o (new Cluster)]
    (when (some? (get arg :centroidId))
      (.setCentroidId o (get arg :centroidId)))
    (when (some? (get arg :count)) (.setCount o (get arg :count)))
    (when (some? (get arg :featureValues))
      (.setFeatureValues o
                         (map FeatureValue/from-edn (get arg :featureValues))))
    o))

(defn to-edn
  [^Cluster arg]
  {:post [(global/strict! :gcp.bindings.services.bigquery.model/Cluster %)]}
  (cond-> {}
    (.getCentroidId arg) (assoc :centroidId (.getCentroidId arg))
    (.getCount arg) (assoc :count (.getCount arg))
    (.getFeatureValues arg)
      (assoc :featureValues (map FeatureValue/to-edn (.getFeatureValues arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Message containing the information about one cluster.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.bindings.services.bigquery.model/Cluster}
   [:centroidId
    {:getter-doc "Centroid id.\n\n@return value or {@code null} for none",
     :setter-doc
       "Centroid id.\n\n@param centroidId centroidId or {@code null} for none",
     :optional true} :int]
   [:count
    {:getter-doc
       "Count of training data rows that were assigned to this cluster.\n\n@return value or {@code null} for none",
     :setter-doc
       "Count of training data rows that were assigned to this cluster.\n\n@param count count or {@code null} for none",
     :optional true} :int]
   [:featureValues
    {:getter-doc
       "Values of highly variant features for this cluster.\n\n@return value or {@code null} for none",
     :setter-doc
       "Values of highly variant features for this cluster.\n\n@param featureValues featureValues or {@code null} for none",
     :optional true}
    [:sequential {:min 1} :gcp.bindings.services.bigquery.model/FeatureValue]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.services.bigquery.model/Cluster schema}
    {:gcp.global/name "gcp.bindings.services.bigquery.model.Cluster"}))