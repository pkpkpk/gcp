;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.api.services.bigquery.model.ClusteringMetrics
  {:doc
     "Evaluation metrics for clustering models.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.ClusteringMetrics"
   :gcp.dev/certification
     {:base-seed 1776499466591
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1776499466591 :standard 1776499466592 :stress 1776499466593}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:04:28.292476756Z"}}
  (:require [gcp.api.services.bigquery.model.Cluster :as Cluster]
            [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model ClusteringMetrics]))

(declare from-edn to-edn)

(defn ^ClusteringMetrics from-edn
  [arg]
  (global/strict! :gcp.api.services.bigquery.model/ClusteringMetrics arg)
  (let [o (new ClusteringMetrics)]
    (when (some? (get arg :clusters))
      (.setClusters o (mapv Cluster/from-edn (get arg :clusters))))
    (when (some? (get arg :daviesBouldinIndex))
      (.setDaviesBouldinIndex o (double (get arg :daviesBouldinIndex))))
    (when (some? (get arg :meanSquaredDistance))
      (.setMeanSquaredDistance o (double (get arg :meanSquaredDistance))))
    o))

(defn to-edn
  [^ClusteringMetrics arg]
  {:post [(global/strict! :gcp.api.services.bigquery.model/ClusteringMetrics
                          %)]}
  (when arg
    (cond-> {}
      (seq (.getClusters arg)) (assoc :clusters
                                 (mapv Cluster/to-edn (.getClusters arg)))
      (.getDaviesBouldinIndex arg) (assoc :daviesBouldinIndex
                                     (.getDaviesBouldinIndex arg))
      (.getMeanSquaredDistance arg) (assoc :meanSquaredDistance
                                      (.getMeanSquaredDistance arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Evaluation metrics for clustering models.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.api.services.bigquery.model/ClusteringMetrics}
   [:clusters
    {:getter-doc
       "Information for all clusters.\n\n@return value or {@code null} for none",
     :setter-doc
       "Information for all clusters.\n\n@param clusters clusters or {@code null} for none",
     :optional true}
    [:sequential {:min 1} :gcp.api.services.bigquery.model/Cluster]]
   [:daviesBouldinIndex
    {:getter-doc
       "Davies-Bouldin index.\n\n@return value or {@code null} for none",
     :setter-doc
       "Davies-Bouldin index.\n\n@param daviesBouldinIndex daviesBouldinIndex or {@code null} for none",
     :optional true} :f64]
   [:meanSquaredDistance
    {:getter-doc
       "Mean of squared distances between each sample to its cluster centroid.\n\n@return value or {@code null} for none",
     :setter-doc
       "Mean of squared distances between each sample to its cluster centroid.\n\n@param meanSquaredDistance meanSquaredDistance or {@code null} for none",
     :optional true} :f64]])

(global/include-schema-registry!
  (with-meta {:gcp.api.services.bigquery.model/ClusteringMetrics schema}
    {:gcp.global/name "gcp.api.services.bigquery.model.ClusteringMetrics"}))