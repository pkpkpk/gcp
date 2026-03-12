;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.services.bigquery.model.ClusterInfo
  {:doc
     "Information about a single cluster for clustering model.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.ClusterInfo"
   :gcp.dev/certification
     {:base-seed 1772390234679
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1772390234679 :standard 1772390234680 :stress 1772390234681}
      :protocol-hash
        "f22c161c7a00aa071f6a8b78764e5c684eaa491c76675456f17e0b44b2b8578c"
      :timestamp "2026-03-01T18:37:14.686381109Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model ClusterInfo]))

(defn ^ClusterInfo from-edn
  [arg]
  (global/strict! :gcp.bindings.services.bigquery.model/ClusterInfo arg)
  (let [o (new ClusterInfo)]
    (when (some? (get arg :centroidId))
      (.setCentroidId o (get arg :centroidId)))
    (when (some? (get arg :clusterRadius))
      (.setClusterRadius o (get arg :clusterRadius)))
    (when (some? (get arg :clusterSize))
      (.setClusterSize o (get arg :clusterSize)))
    o))

(defn to-edn
  [^ClusterInfo arg]
  {:post [(global/strict! :gcp.bindings.services.bigquery.model/ClusterInfo %)]}
  (cond-> {}
    (.getCentroidId arg) (assoc :centroidId (.getCentroidId arg))
    (.getClusterRadius arg) (assoc :clusterRadius (.getClusterRadius arg))
    (.getClusterSize arg) (assoc :clusterSize (.getClusterSize arg))))

(def schema
  [:map
   {:closed true,
    :doc
      "Information about a single cluster for clustering model.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.bindings.services.bigquery.model/ClusterInfo}
   [:centroidId
    {:getter-doc "Centroid id.\n\n@return value or {@code null} for none",
     :setter-doc
       "Centroid id.\n\n@param centroidId centroidId or {@code null} for none",
     :optional true} :int]
   [:clusterRadius
    {:getter-doc
       "Cluster radius, the average distance from centroid to each point assigned to the cluster.\n\n@return value or {@code null} for none",
     :setter-doc
       "Cluster radius, the average distance from centroid to each point assigned to the cluster.\n\n@param clusterRadius clusterRadius or {@code null} for none",
     :optional true} :double]
   [:clusterSize
    {:getter-doc
       "Cluster size, the total number of points assigned to the cluster.\n\n@return value or {@code null} for none",
     :setter-doc
       "Cluster size, the total number of points assigned to the cluster.\n\n@param clusterSize clusterSize or {@code null} for none",
     :optional true} :int]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.services.bigquery.model/ClusterInfo schema}
    {:gcp.global/name "gcp.bindings.services.bigquery.model.ClusterInfo"}))