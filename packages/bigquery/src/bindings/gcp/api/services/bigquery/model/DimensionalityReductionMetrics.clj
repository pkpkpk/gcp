;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.api.services.bigquery.model.DimensionalityReductionMetrics
  {:doc
     "Model evaluation metrics for dimensionality reduction models.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.DimensionalityReductionMetrics"
   :gcp.dev/certification
     {:base-seed 1776499474452
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1776499474452 :standard 1776499474453 :stress 1776499474454}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:04:35.754402401Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model
            DimensionalityReductionMetrics]))

(declare from-edn to-edn)

(defn ^DimensionalityReductionMetrics from-edn
  [arg]
  (global/strict!
    :gcp.api.services.bigquery.model/DimensionalityReductionMetrics
    arg)
  (let [o (new DimensionalityReductionMetrics)]
    (when (some? (get arg :totalExplainedVarianceRatio))
      (.setTotalExplainedVarianceRatio
        o
        (double (get arg :totalExplainedVarianceRatio))))
    o))

(defn to-edn
  [^DimensionalityReductionMetrics arg]
  {:post [(global/strict!
            :gcp.api.services.bigquery.model/DimensionalityReductionMetrics
            %)]}
  (when arg
    (cond-> {}
      (.getTotalExplainedVarianceRatio arg) (assoc :totalExplainedVarianceRatio
                                              (.getTotalExplainedVarianceRatio
                                                arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Model evaluation metrics for dimensionality reduction models.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.api.services.bigquery.model/DimensionalityReductionMetrics}
   [:totalExplainedVarianceRatio
    {:getter-doc
       "Total percentage of variance explained by the selected principal components.\n\n@return value or {@code null} for none",
     :setter-doc
       "Total percentage of variance explained by the selected principal components.\n\n@param totalExplainedVarianceRatio totalExplainedVarianceRatio or {@code null} for none",
     :optional true} :f64]])

(global/include-schema-registry!
  (with-meta {:gcp.api.services.bigquery.model/DimensionalityReductionMetrics
                schema}
    {:gcp.global/name
       "gcp.api.services.bigquery.model.DimensionalityReductionMetrics"}))