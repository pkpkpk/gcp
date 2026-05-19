;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.api.services.bigquery.model.DimensionalityReductionMetrics
  {:doc
     "Model evaluation metrics for dimensionality reduction models.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.DimensionalityReductionMetrics"
   :gcp.dev/certification
     {:base-seed 1779204729659
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1779204729659 :standard 1779204729660 :stress 1779204729661}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-05-19T15:32:10.539949116Z"}}
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