;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.api.services.bigquery.model.MultiClassClassificationMetrics
  {:doc
     "Evaluation metrics for multi-class classification/classifier models.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.MultiClassClassificationMetrics"
   :gcp.dev/certification
     {:base-seed 1775130987863
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1775130987863 :standard 1775130987864 :stress 1775130987865}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:56:29.117406497Z"}}
  (:require [gcp.api.services.bigquery.model.AggregateClassificationMetrics :as
             AggregateClassificationMetrics]
            [gcp.api.services.bigquery.model.ConfusionMatrix :as
             ConfusionMatrix]
            [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model
            MultiClassClassificationMetrics]))

(declare from-edn to-edn)

(defn ^MultiClassClassificationMetrics from-edn
  [arg]
  (global/strict!
    :gcp.api.services.bigquery.model/MultiClassClassificationMetrics
    arg)
  (let [o (new MultiClassClassificationMetrics)]
    (when (some? (get arg :aggregateClassificationMetrics))
      (.setAggregateClassificationMetrics
        o
        (AggregateClassificationMetrics/from-edn
          (get arg :aggregateClassificationMetrics))))
    (when (some? (get arg :confusionMatrixList))
      (.setConfusionMatrixList o
                               (map ConfusionMatrix/from-edn
                                 (get arg :confusionMatrixList))))
    o))

(defn to-edn
  [^MultiClassClassificationMetrics arg]
  {:post [(global/strict!
            :gcp.api.services.bigquery.model/MultiClassClassificationMetrics
            %)]}
  (when arg
    (cond-> {}
      (.getAggregateClassificationMetrics arg)
        (assoc :aggregateClassificationMetrics
          (AggregateClassificationMetrics/to-edn
            (.getAggregateClassificationMetrics arg)))
      (seq (.getConfusionMatrixList arg)) (assoc :confusionMatrixList
                                            (map ConfusionMatrix/to-edn
                                              (.getConfusionMatrixList arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "Evaluation metrics for multi-class classification/classifier models.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.api.services.bigquery.model/MultiClassClassificationMetrics}
   [:aggregateClassificationMetrics
    {:getter-doc
       "Aggregate classification metrics.\n\n@return value or {@code null} for none",
     :setter-doc
       "Aggregate classification metrics.\n\n@param aggregateClassificationMetrics aggregateClassificationMetrics or {@code null} for none",
     :optional true}
    :gcp.api.services.bigquery.model/AggregateClassificationMetrics]
   [:confusionMatrixList
    {:getter-doc
       "Confusion matrix at different thresholds.\n\n@return value or {@code null} for none",
     :setter-doc
       "Confusion matrix at different thresholds.\n\n@param confusionMatrixList confusionMatrixList or {@code null} for none",
     :optional true}
    [:sequential {:min 1} :gcp.api.services.bigquery.model/ConfusionMatrix]]])

(global/include-schema-registry!
  (with-meta {:gcp.api.services.bigquery.model/MultiClassClassificationMetrics
                schema}
    {:gcp.global/name
       "gcp.api.services.bigquery.model.MultiClassClassificationMetrics"}))