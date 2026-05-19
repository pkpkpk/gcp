;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.api.services.bigquery.model.BinaryClassificationMetrics
  {:doc
     "Evaluation metrics for binary classification/classifier models.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.BinaryClassificationMetrics"
   :gcp.dev/certification
     {:base-seed 1779204728275
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1779204728275 :standard 1779204728276 :stress 1779204728277}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-05-19T15:32:09.148425441Z"}}
  (:require [gcp.api.services.bigquery.model.AggregateClassificationMetrics :as
             AggregateClassificationMetrics]
            [gcp.api.services.bigquery.model.BinaryConfusionMatrix :as
             BinaryConfusionMatrix]
            [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model
            BinaryClassificationMetrics]))

(declare from-edn to-edn)

(defn ^BinaryClassificationMetrics from-edn
  [arg]
  (global/strict! :gcp.api.services.bigquery.model/BinaryClassificationMetrics
                  arg)
  (let [o (new BinaryClassificationMetrics)]
    (when (some? (get arg :aggregateClassificationMetrics))
      (.setAggregateClassificationMetrics
        o
        (AggregateClassificationMetrics/from-edn
          (get arg :aggregateClassificationMetrics))))
    (when (some? (get arg :binaryConfusionMatrixList))
      (.setBinaryConfusionMatrixList o
                                     (mapv BinaryConfusionMatrix/from-edn
                                       (get arg :binaryConfusionMatrixList))))
    (when (some? (get arg :negativeLabel))
      (.setNegativeLabel o (get arg :negativeLabel)))
    (when (some? (get arg :positiveLabel))
      (.setPositiveLabel o (get arg :positiveLabel)))
    o))

(defn to-edn
  [^BinaryClassificationMetrics arg]
  {:post [(global/strict!
            :gcp.api.services.bigquery.model/BinaryClassificationMetrics
            %)]}
  (when arg
    (cond-> {}
      (.getAggregateClassificationMetrics arg)
        (assoc :aggregateClassificationMetrics
          (AggregateClassificationMetrics/to-edn
            (.getAggregateClassificationMetrics arg)))
      (seq (.getBinaryConfusionMatrixList arg))
        (assoc :binaryConfusionMatrixList
          (mapv BinaryConfusionMatrix/to-edn
            (.getBinaryConfusionMatrixList arg)))
      (some->> (.getNegativeLabel arg)
               (not= ""))
        (assoc :negativeLabel (.getNegativeLabel arg))
      (some->> (.getPositiveLabel arg)
               (not= ""))
        (assoc :positiveLabel (.getPositiveLabel arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Evaluation metrics for binary classification/classifier models.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.api.services.bigquery.model/BinaryClassificationMetrics}
   [:aggregateClassificationMetrics
    {:getter-doc
       "Aggregate classification metrics.\n\n@return value or {@code null} for none",
     :setter-doc
       "Aggregate classification metrics.\n\n@param aggregateClassificationMetrics aggregateClassificationMetrics or {@code null} for none",
     :optional true}
    :gcp.api.services.bigquery.model/AggregateClassificationMetrics]
   [:binaryConfusionMatrixList
    {:getter-doc
       "Binary confusion matrix at multiple thresholds.\n\n@return value or {@code null} for none",
     :setter-doc
       "Binary confusion matrix at multiple thresholds.\n\n@param binaryConfusionMatrixList binaryConfusionMatrixList or {@code null} for none",
     :optional true}
    [:sequential {:min 1, :gen/max 2}
     :gcp.api.services.bigquery.model/BinaryConfusionMatrix]]
   [:negativeLabel
    {:getter-doc
       "Label representing the negative class.\n\n@return value or {@code null} for none",
     :setter-doc
       "Label representing the negative class.\n\n@param negativeLabel negativeLabel or {@code null} for none",
     :optional true} [:string {:min 1, :gen/max 1}]]
   [:positiveLabel
    {:getter-doc
       "Label representing the positive class.\n\n@return value or {@code null} for none",
     :setter-doc
       "Label representing the positive class.\n\n@param positiveLabel positiveLabel or {@code null} for none",
     :optional true} [:string {:min 1, :gen/max 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.api.services.bigquery.model/BinaryClassificationMetrics
                schema}
    {:gcp.global/name
       "gcp.api.services.bigquery.model.BinaryClassificationMetrics"}))