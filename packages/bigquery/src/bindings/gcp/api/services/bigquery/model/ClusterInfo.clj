;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.api.services.bigquery.model.ClusterInfo
  {:doc
     "Information about a single cluster for clustering model.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.ClusterInfo"
   :gcp.dev/certification
     {:base-seed 1779204658078
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1779204658078 :standard 1779204658079 :stress 1779204658080}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-05-19T15:30:58.909398607Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model ClusterInfo]))

(declare from-edn to-edn)

(defn ^ClusterInfo from-edn
  [arg]
  (global/strict! :gcp.api.services.bigquery.model/ClusterInfo arg)
  (let [o (new ClusterInfo)]
    (when (some? (get arg :centroidId))
      (.setCentroidId o (long (get arg :centroidId))))
    (when (some? (get arg :clusterRadius))
      (.setClusterRadius o (double (get arg :clusterRadius))))
    (when (some? (get arg :clusterSize))
      (.setClusterSize o (long (get arg :clusterSize))))
    o))

(defn to-edn
  [^ClusterInfo arg]
  {:post [(global/strict! :gcp.api.services.bigquery.model/ClusterInfo %)]}
  (when arg
    (cond-> {}
      (.getCentroidId arg) (assoc :centroidId (.getCentroidId arg))
      (.getClusterRadius arg) (assoc :clusterRadius (.getClusterRadius arg))
      (.getClusterSize arg) (assoc :clusterSize (.getClusterSize arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Information about a single cluster for clustering model.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.api.services.bigquery.model/ClusterInfo}
   [:centroidId
    {:getter-doc "Centroid id.\n\n@return value or {@code null} for none",
     :setter-doc
       "Centroid id.\n\n@param centroidId centroidId or {@code null} for none",
     :optional true} :i64]
   [:clusterRadius
    {:getter-doc
       "Cluster radius, the average distance from centroid to each point assigned to the cluster.\n\n@return value or {@code null} for none",
     :setter-doc
       "Cluster radius, the average distance from centroid to each point assigned to the cluster.\n\n@param clusterRadius clusterRadius or {@code null} for none",
     :optional true} :f64]
   [:clusterSize
    {:getter-doc
       "Cluster size, the total number of points assigned to the cluster.\n\n@return value or {@code null} for none",
     :setter-doc
       "Cluster size, the total number of points assigned to the cluster.\n\n@param clusterSize clusterSize or {@code null} for none",
     :optional true} :i64]])

(global/include-schema-registry!
  (with-meta {:gcp.api.services.bigquery.model/ClusterInfo schema}
    {:gcp.global/name "gcp.api.services.bigquery.model.ClusterInfo"}))