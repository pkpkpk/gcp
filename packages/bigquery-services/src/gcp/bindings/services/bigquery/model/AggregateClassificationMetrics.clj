;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.services.bigquery.model.AggregateClassificationMetrics
  {:doc
     "Aggregate metrics for classification/classifier models. For multi-class models, the metrics are\neither macro-averaged or micro-averaged. When macro-averaged, the metrics are calculated for each\nlabel and then an unweighted average is taken of those values. When micro-averaged, the metric is\ncalculated globally by counting the total number of correctly predicted rows.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.AggregateClassificationMetrics"
   :gcp.dev/certification
     {:base-seed 1772390247258
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1772390247258 :standard 1772390247259 :stress 1772390247260}
      :protocol-hash
        "f22c161c7a00aa071f6a8b78764e5c684eaa491c76675456f17e0b44b2b8578c"
      :timestamp "2026-03-01T18:37:27.293500755Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model
            AggregateClassificationMetrics]))

(defn ^AggregateClassificationMetrics from-edn
  [arg]
  (global/strict!
    :gcp.bindings.services.bigquery.model/AggregateClassificationMetrics
    arg)
  (let [o (new AggregateClassificationMetrics)]
    (when (some? (get arg :accuracy)) (.setAccuracy o (get arg :accuracy)))
    (when (some? (get arg :f1Score)) (.setF1Score o (get arg :f1Score)))
    (when (some? (get arg :logLoss)) (.setLogLoss o (get arg :logLoss)))
    (when (some? (get arg :precision)) (.setPrecision o (get arg :precision)))
    (when (some? (get arg :recall)) (.setRecall o (get arg :recall)))
    (when (some? (get arg :rocAuc)) (.setRocAuc o (get arg :rocAuc)))
    (when (some? (get arg :threshold)) (.setThreshold o (get arg :threshold)))
    o))

(defn to-edn
  [^AggregateClassificationMetrics arg]
  {:post [(global/strict!
            :gcp.bindings.services.bigquery.model/AggregateClassificationMetrics
            %)]}
  (cond-> {}
    (.getAccuracy arg) (assoc :accuracy (.getAccuracy arg))
    (.getF1Score arg) (assoc :f1Score (.getF1Score arg))
    (.getLogLoss arg) (assoc :logLoss (.getLogLoss arg))
    (.getPrecision arg) (assoc :precision (.getPrecision arg))
    (.getRecall arg) (assoc :recall (.getRecall arg))
    (.getRocAuc arg) (assoc :rocAuc (.getRocAuc arg))
    (.getThreshold arg) (assoc :threshold (.getThreshold arg))))

(def schema
  [:map
   {:closed true,
    :doc
      "Aggregate metrics for classification/classifier models. For multi-class models, the metrics are\neither macro-averaged or micro-averaged. When macro-averaged, the metrics are calculated for each\nlabel and then an unweighted average is taken of those values. When micro-averaged, the metric is\ncalculated globally by counting the total number of correctly predicted rows.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key
      :gcp.bindings.services.bigquery.model/AggregateClassificationMetrics}
   [:accuracy
    {:getter-doc
       "Accuracy is the fraction of predictions given the correct label. For multiclass this is a\nmicro-averaged metric.\n\n@return value or {@code null} for none",
     :setter-doc
       "Accuracy is the fraction of predictions given the correct label. For multiclass this is a\nmicro-averaged metric.\n\n@param accuracy accuracy or {@code null} for none",
     :optional true} :double]
   [:f1Score
    {:getter-doc
       "The F1 score is an average of recall and precision. For multiclass this is a macro-averaged\nmetric.\n\n@return value or {@code null} for none",
     :setter-doc
       "The F1 score is an average of recall and precision. For multiclass this is a macro-averaged\nmetric.\n\n@param f1Score f1Score or {@code null} for none",
     :optional true} :double]
   [:logLoss
    {:getter-doc
       "Logarithmic Loss. For multiclass this is a macro-averaged metric.\n\n@return value or {@code null} for none",
     :setter-doc
       "Logarithmic Loss. For multiclass this is a macro-averaged metric.\n\n@param logLoss logLoss or {@code null} for none",
     :optional true} :double]
   [:precision
    {:getter-doc
       "Precision is the fraction of actual positive predictions that had positive actual labels. For\nmulticlass this is a macro-averaged metric treating each class as a binary classifier.\n\n@return value or {@code null} for none",
     :setter-doc
       "Precision is the fraction of actual positive predictions that had positive actual labels. For\nmulticlass this is a macro-averaged metric treating each class as a binary classifier.\n\n@param precision precision or {@code null} for none",
     :optional true} :double]
   [:recall
    {:getter-doc
       "Recall is the fraction of actual positive labels that were given a positive prediction. For\nmulticlass this is a macro-averaged metric.\n\n@return value or {@code null} for none",
     :setter-doc
       "Recall is the fraction of actual positive labels that were given a positive prediction. For\nmulticlass this is a macro-averaged metric.\n\n@param recall recall or {@code null} for none",
     :optional true} :double]
   [:rocAuc
    {:getter-doc
       "Area Under a ROC Curve. For multiclass this is a macro-averaged metric.\n\n@return value or {@code null} for none",
     :setter-doc
       "Area Under a ROC Curve. For multiclass this is a macro-averaged metric.\n\n@param rocAuc rocAuc or {@code null} for none",
     :optional true} :double]
   [:threshold
    {:getter-doc
       "Threshold at which the metrics are computed. For binary classification models this is the\npositive class threshold. For multi-class classification models this is the confidence\nthreshold.\n\n@return value or {@code null} for none",
     :setter-doc
       "Threshold at which the metrics are computed. For binary classification models this is the\npositive class threshold. For multi-class classification models this is the confidence\nthreshold.\n\n@param threshold threshold or {@code null} for none",
     :optional true} :double]])

(global/include-schema-registry!
  (with-meta
    {:gcp.bindings.services.bigquery.model/AggregateClassificationMetrics
       schema}
    {:gcp.global/name
       "gcp.bindings.services.bigquery.model.AggregateClassificationMetrics"}))