;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.services.bigquery.model.BinaryClassificationMetrics
  {:doc
     "Evaluation metrics for binary classification/classifier models.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.BinaryClassificationMetrics"
   :gcp.dev/certification
     {:base-seed 1772390248495
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1772390248495 :standard 1772390248496 :stress 1772390248497}
      :protocol-hash
        "f22c161c7a00aa071f6a8b78764e5c684eaa491c76675456f17e0b44b2b8578c"
      :timestamp "2026-03-01T18:37:28.596632331Z"}}
  (:require [gcp.bindings.services.bigquery.model.AggregateClassificationMetrics
             :as AggregateClassificationMetrics]
            [gcp.bindings.services.bigquery.model.BinaryConfusionMatrix :as
             BinaryConfusionMatrix]
            [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model
            BinaryClassificationMetrics]))

(defn ^BinaryClassificationMetrics from-edn
  [arg]
  (global/strict!
    :gcp.bindings.services.bigquery.model/BinaryClassificationMetrics
    arg)
  (let [o (new BinaryClassificationMetrics)]
    (when (some? (get arg :aggregateClassificationMetrics))
      (.setAggregateClassificationMetrics
        o
        (AggregateClassificationMetrics/from-edn
          (get arg :aggregateClassificationMetrics))))
    (when (some? (get arg :binaryConfusionMatrixList))
      (.setBinaryConfusionMatrixList o
                                     (map BinaryConfusionMatrix/from-edn
                                       (get arg :binaryConfusionMatrixList))))
    (when (some? (get arg :negativeLabel))
      (.setNegativeLabel o (get arg :negativeLabel)))
    (when (some? (get arg :positiveLabel))
      (.setPositiveLabel o (get arg :positiveLabel)))
    o))

(defn to-edn
  [^BinaryClassificationMetrics arg]
  {:post [(global/strict!
            :gcp.bindings.services.bigquery.model/BinaryClassificationMetrics
            %)]}
  (cond-> {}
    (.getAggregateClassificationMetrics arg)
      (assoc :aggregateClassificationMetrics
        (AggregateClassificationMetrics/to-edn
          (.getAggregateClassificationMetrics arg)))
    (.getBinaryConfusionMatrixList arg)
      (assoc :binaryConfusionMatrixList
        (map BinaryConfusionMatrix/to-edn (.getBinaryConfusionMatrixList arg)))
    (.getNegativeLabel arg) (assoc :negativeLabel (.getNegativeLabel arg))
    (.getPositiveLabel arg) (assoc :positiveLabel (.getPositiveLabel arg))))

(def schema
  [:map
   {:closed true,
    :doc
      "Evaluation metrics for binary classification/classifier models.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.bindings.services.bigquery.model/BinaryClassificationMetrics}
   [:aggregateClassificationMetrics
    {:getter-doc
       "Aggregate classification metrics.\n\n@return value or {@code null} for none",
     :setter-doc
       "Aggregate classification metrics.\n\n@param aggregateClassificationMetrics aggregateClassificationMetrics or {@code null} for none",
     :optional true}
    :gcp.bindings.services.bigquery.model/AggregateClassificationMetrics]
   [:binaryConfusionMatrixList
    {:getter-doc
       "Binary confusion matrix at multiple thresholds.\n\n@return value or {@code null} for none",
     :setter-doc
       "Binary confusion matrix at multiple thresholds.\n\n@param binaryConfusionMatrixList binaryConfusionMatrixList or {@code null} for none",
     :optional true}
    [:sequential {:min 1}
     :gcp.bindings.services.bigquery.model/BinaryConfusionMatrix]]
   [:negativeLabel
    {:getter-doc
       "Label representing the negative class.\n\n@return value or {@code null} for none",
     :setter-doc
       "Label representing the negative class.\n\n@param negativeLabel negativeLabel or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:positiveLabel
    {:getter-doc
       "Label representing the positive class.\n\n@return value or {@code null} for none",
     :setter-doc
       "Label representing the positive class.\n\n@param positiveLabel positiveLabel or {@code null} for none",
     :optional true} [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.services.bigquery.model/BinaryClassificationMetrics
                schema}
    {:gcp.global/name
       "gcp.bindings.services.bigquery.model.BinaryClassificationMetrics"}))