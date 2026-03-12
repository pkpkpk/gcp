;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.services.bigquery.model.RegressionMetrics
  {:doc
     "Evaluation metrics for regression and explicit feedback type matrix factorization models.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.RegressionMetrics"
   :gcp.dev/certification
     {:base-seed 1772390257454
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1772390257454 :standard 1772390257455 :stress 1772390257456}
      :protocol-hash
        "f22c161c7a00aa071f6a8b78764e5c684eaa491c76675456f17e0b44b2b8578c"
      :timestamp "2026-03-01T18:37:37.479824329Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model RegressionMetrics]))

(defn ^RegressionMetrics from-edn
  [arg]
  (global/strict! :gcp.bindings.services.bigquery.model/RegressionMetrics arg)
  (let [o (new RegressionMetrics)]
    (when (some? (get arg :meanAbsoluteError))
      (.setMeanAbsoluteError o (get arg :meanAbsoluteError)))
    (when (some? (get arg :meanSquaredError))
      (.setMeanSquaredError o (get arg :meanSquaredError)))
    (when (some? (get arg :meanSquaredLogError))
      (.setMeanSquaredLogError o (get arg :meanSquaredLogError)))
    (when (some? (get arg :medianAbsoluteError))
      (.setMedianAbsoluteError o (get arg :medianAbsoluteError)))
    (when (some? (get arg :rSquared)) (.setRSquared o (get arg :rSquared)))
    o))

(defn to-edn
  [^RegressionMetrics arg]
  {:post [(global/strict!
            :gcp.bindings.services.bigquery.model/RegressionMetrics
            %)]}
  (cond-> {}
    (.getMeanAbsoluteError arg) (assoc :meanAbsoluteError
                                  (.getMeanAbsoluteError arg))
    (.getMeanSquaredError arg) (assoc :meanSquaredError
                                 (.getMeanSquaredError arg))
    (.getMeanSquaredLogError arg) (assoc :meanSquaredLogError
                                    (.getMeanSquaredLogError arg))
    (.getMedianAbsoluteError arg) (assoc :medianAbsoluteError
                                    (.getMedianAbsoluteError arg))
    (.getRSquared arg) (assoc :rSquared (.getRSquared arg))))

(def schema
  [:map
   {:closed true,
    :doc
      "Evaluation metrics for regression and explicit feedback type matrix factorization models.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.bindings.services.bigquery.model/RegressionMetrics}
   [:meanAbsoluteError
    {:getter-doc
       "Mean absolute error.\n\n@return value or {@code null} for none",
     :setter-doc
       "Mean absolute error.\n\n@param meanAbsoluteError meanAbsoluteError or {@code null} for none",
     :optional true} :double]
   [:meanSquaredError
    {:getter-doc
       "Mean squared error.\n\n@return value or {@code null} for none",
     :setter-doc
       "Mean squared error.\n\n@param meanSquaredError meanSquaredError or {@code null} for none",
     :optional true} :double]
   [:meanSquaredLogError
    {:getter-doc
       "Mean squared log error.\n\n@return value or {@code null} for none",
     :setter-doc
       "Mean squared log error.\n\n@param meanSquaredLogError meanSquaredLogError or {@code null} for none",
     :optional true} :double]
   [:medianAbsoluteError
    {:getter-doc
       "Median absolute error.\n\n@return value or {@code null} for none",
     :setter-doc
       "Median absolute error.\n\n@param medianAbsoluteError medianAbsoluteError or {@code null} for none",
     :optional true} :double]
   [:rSquared
    {:getter-doc
       "R^2 score. This corresponds to r2_score in ML.EVALUATE.\n\n@return value or {@code null} for none",
     :setter-doc
       "R^2 score. This corresponds to r2_score in ML.EVALUATE.\n\n@param rSquared rSquared or {@code null} for none",
     :optional true} :double]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.services.bigquery.model/RegressionMetrics schema}
    {:gcp.global/name
       "gcp.bindings.services.bigquery.model.RegressionMetrics"}))