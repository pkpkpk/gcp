;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.services.bigquery.model.DimensionalityReductionMetrics
  {:doc
     "Model evaluation metrics for dimensionality reduction models.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.DimensionalityReductionMetrics"
   :gcp.dev/certification
     {:base-seed 1772390249912
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1772390249912 :standard 1772390249913 :stress 1772390249914}
      :protocol-hash
        "f22c161c7a00aa071f6a8b78764e5c684eaa491c76675456f17e0b44b2b8578c"
      :timestamp "2026-03-01T18:37:29.928351326Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model
            DimensionalityReductionMetrics]))

(defn ^DimensionalityReductionMetrics from-edn
  [arg]
  (global/strict!
    :gcp.bindings.services.bigquery.model/DimensionalityReductionMetrics
    arg)
  (let [o (new DimensionalityReductionMetrics)]
    (when (some? (get arg :totalExplainedVarianceRatio))
      (.setTotalExplainedVarianceRatio o
                                       (get arg :totalExplainedVarianceRatio)))
    o))

(defn to-edn
  [^DimensionalityReductionMetrics arg]
  {:post [(global/strict!
            :gcp.bindings.services.bigquery.model/DimensionalityReductionMetrics
            %)]}
  (cond-> {}
    (.getTotalExplainedVarianceRatio arg) (assoc :totalExplainedVarianceRatio
                                            (.getTotalExplainedVarianceRatio
                                              arg))))

(def schema
  [:map
   {:closed true,
    :doc
      "Model evaluation metrics for dimensionality reduction models.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key
      :gcp.bindings.services.bigquery.model/DimensionalityReductionMetrics}
   [:totalExplainedVarianceRatio
    {:getter-doc
       "Total percentage of variance explained by the selected principal components.\n\n@return value or {@code null} for none",
     :setter-doc
       "Total percentage of variance explained by the selected principal components.\n\n@param totalExplainedVarianceRatio totalExplainedVarianceRatio or {@code null} for none",
     :optional true} :double]])

(global/include-schema-registry!
  (with-meta
    {:gcp.bindings.services.bigquery.model/DimensionalityReductionMetrics
       schema}
    {:gcp.global/name
       "gcp.bindings.services.bigquery.model.DimensionalityReductionMetrics"}))