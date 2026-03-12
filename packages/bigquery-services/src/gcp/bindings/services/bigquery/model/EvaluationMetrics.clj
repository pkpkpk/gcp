;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.services.bigquery.model.EvaluationMetrics
  {:doc
     "Evaluation metrics of a model. These are either computed on all training data or just the eval\ndata based on whether eval data was used during training. These are not present for imported\nmodels.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.EvaluationMetrics"
   :gcp.dev/certification
     {:base-seed 1772390258648
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1772390258648 :standard 1772390258649 :stress 1772390258650}
      :protocol-hash
        "f22c161c7a00aa071f6a8b78764e5c684eaa491c76675456f17e0b44b2b8578c"
      :timestamp "2026-03-01T18:37:40.603913525Z"}}
  (:require
    [gcp.bindings.services.bigquery.model.ArimaForecastingMetrics :as
     ArimaForecastingMetrics]
    [gcp.bindings.services.bigquery.model.BinaryClassificationMetrics :as
     BinaryClassificationMetrics]
    [gcp.bindings.services.bigquery.model.ClusteringMetrics :as
     ClusteringMetrics]
    [gcp.bindings.services.bigquery.model.DimensionalityReductionMetrics :as
     DimensionalityReductionMetrics]
    [gcp.bindings.services.bigquery.model.MultiClassClassificationMetrics :as
     MultiClassClassificationMetrics]
    [gcp.bindings.services.bigquery.model.RankingMetrics :as RankingMetrics]
    [gcp.bindings.services.bigquery.model.RegressionMetrics :as
     RegressionMetrics]
    [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model EvaluationMetrics]))

(defn ^EvaluationMetrics from-edn
  [arg]
  (global/strict! :gcp.bindings.services.bigquery.model/EvaluationMetrics arg)
  (let [o (new EvaluationMetrics)]
    (when (some? (get arg :arimaForecastingMetrics))
      (.setArimaForecastingMetrics o
                                   (ArimaForecastingMetrics/from-edn
                                     (get arg :arimaForecastingMetrics))))
    (when (some? (get arg :binaryClassificationMetrics))
      (.setBinaryClassificationMetrics o
                                       (BinaryClassificationMetrics/from-edn
                                         (get arg
                                              :binaryClassificationMetrics))))
    (when (some? (get arg :clusteringMetrics))
      (.setClusteringMetrics o
                             (ClusteringMetrics/from-edn
                               (get arg :clusteringMetrics))))
    (when (some? (get arg :dimensionalityReductionMetrics))
      (.setDimensionalityReductionMetrics
        o
        (DimensionalityReductionMetrics/from-edn
          (get arg :dimensionalityReductionMetrics))))
    (when (some? (get arg :multiClassClassificationMetrics))
      (.setMultiClassClassificationMetrics
        o
        (MultiClassClassificationMetrics/from-edn
          (get arg :multiClassClassificationMetrics))))
    (when (some? (get arg :rankingMetrics))
      (.setRankingMetrics o
                          (RankingMetrics/from-edn (get arg :rankingMetrics))))
    (when (some? (get arg :regressionMetrics))
      (.setRegressionMetrics o
                             (RegressionMetrics/from-edn
                               (get arg :regressionMetrics))))
    o))

(defn to-edn
  [^EvaluationMetrics arg]
  {:post [(global/strict!
            :gcp.bindings.services.bigquery.model/EvaluationMetrics
            %)]}
  (cond-> {}
    (.getArimaForecastingMetrics arg) (assoc :arimaForecastingMetrics
                                        (ArimaForecastingMetrics/to-edn
                                          (.getArimaForecastingMetrics arg)))
    (.getBinaryClassificationMetrics arg) (assoc :binaryClassificationMetrics
                                            (BinaryClassificationMetrics/to-edn
                                              (.getBinaryClassificationMetrics
                                                arg)))
    (.getClusteringMetrics arg) (assoc :clusteringMetrics
                                  (ClusteringMetrics/to-edn
                                    (.getClusteringMetrics arg)))
    (.getDimensionalityReductionMetrics arg)
      (assoc :dimensionalityReductionMetrics
        (DimensionalityReductionMetrics/to-edn
          (.getDimensionalityReductionMetrics arg)))
    (.getMultiClassClassificationMetrics arg)
      (assoc :multiClassClassificationMetrics
        (MultiClassClassificationMetrics/to-edn
          (.getMultiClassClassificationMetrics arg)))
    (.getRankingMetrics arg) (assoc :rankingMetrics
                               (RankingMetrics/to-edn (.getRankingMetrics arg)))
    (.getRegressionMetrics arg) (assoc :regressionMetrics
                                  (RegressionMetrics/to-edn
                                    (.getRegressionMetrics arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Evaluation metrics of a model. These are either computed on all training data or just the eval\ndata based on whether eval data was used during training. These are not present for imported\nmodels.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.bindings.services.bigquery.model/EvaluationMetrics}
   [:arimaForecastingMetrics
    {:getter-doc
       "Populated for ARIMA models.\n\n@return value or {@code null} for none",
     :setter-doc
       "Populated for ARIMA models.\n\n@param arimaForecastingMetrics arimaForecastingMetrics or {@code null} for none",
     :optional true}
    :gcp.bindings.services.bigquery.model/ArimaForecastingMetrics]
   [:binaryClassificationMetrics
    {:getter-doc
       "Populated for binary classification/classifier models.\n\n@return value or {@code null} for none",
     :setter-doc
       "Populated for binary classification/classifier models.\n\n@param binaryClassificationMetrics binaryClassificationMetrics or {@code null} for none",
     :optional true}
    :gcp.bindings.services.bigquery.model/BinaryClassificationMetrics]
   [:clusteringMetrics
    {:getter-doc
       "Populated for clustering models.\n\n@return value or {@code null} for none",
     :setter-doc
       "Populated for clustering models.\n\n@param clusteringMetrics clusteringMetrics or {@code null} for none",
     :optional true} :gcp.bindings.services.bigquery.model/ClusteringMetrics]
   [:dimensionalityReductionMetrics
    {:getter-doc
       "Evaluation metrics when the model is a dimensionality reduction model, which currently includes\nPCA.\n\n@return value or {@code null} for none",
     :setter-doc
       "Evaluation metrics when the model is a dimensionality reduction model, which currently includes\nPCA.\n\n@param dimensionalityReductionMetrics dimensionalityReductionMetrics or {@code null} for none",
     :optional true}
    :gcp.bindings.services.bigquery.model/DimensionalityReductionMetrics]
   [:multiClassClassificationMetrics
    {:getter-doc
       "Populated for multi-class classification/classifier models.\n\n@return value or {@code null} for none",
     :setter-doc
       "Populated for multi-class classification/classifier models.\n\n@param multiClassClassificationMetrics multiClassClassificationMetrics or {@code null} for none",
     :optional true}
    :gcp.bindings.services.bigquery.model/MultiClassClassificationMetrics]
   [:rankingMetrics
    {:getter-doc
       "Populated for implicit feedback type matrix factorization models.\n\n@return value or {@code null} for none",
     :setter-doc
       "Populated for implicit feedback type matrix factorization models.\n\n@param rankingMetrics rankingMetrics or {@code null} for none",
     :optional true} :gcp.bindings.services.bigquery.model/RankingMetrics]
   [:regressionMetrics
    {:getter-doc
       "Populated for regression models and explicit feedback type matrix factorization models.\n\n@return value or {@code null} for none",
     :setter-doc
       "Populated for regression models and explicit feedback type matrix factorization models.\n\n@param regressionMetrics regressionMetrics or {@code null} for none",
     :optional true} :gcp.bindings.services.bigquery.model/RegressionMetrics]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.services.bigquery.model/EvaluationMetrics schema}
    {:gcp.global/name
       "gcp.bindings.services.bigquery.model.EvaluationMetrics"}))