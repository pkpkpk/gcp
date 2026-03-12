;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.services.bigquery.model.RankingMetrics
  {:doc
     "Evaluation metrics used by weighted-ALS models specified by feedback_type=implicit.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.RankingMetrics"
   :gcp.dev/certification
     {:base-seed 1772390256223
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1772390256223 :standard 1772390256224 :stress 1772390256225}
      :protocol-hash
        "f22c161c7a00aa071f6a8b78764e5c684eaa491c76675456f17e0b44b2b8578c"
      :timestamp "2026-03-01T18:37:36.248206615Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model RankingMetrics]))

(defn ^RankingMetrics from-edn
  [arg]
  (global/strict! :gcp.bindings.services.bigquery.model/RankingMetrics arg)
  (let [o (new RankingMetrics)]
    (when (some? (get arg :averageRank))
      (.setAverageRank o (get arg :averageRank)))
    (when (some? (get arg :meanAveragePrecision))
      (.setMeanAveragePrecision o (get arg :meanAveragePrecision)))
    (when (some? (get arg :meanSquaredError))
      (.setMeanSquaredError o (get arg :meanSquaredError)))
    (when (some? (get arg :normalizedDiscountedCumulativeGain))
      (.setNormalizedDiscountedCumulativeGain
        o
        (get arg :normalizedDiscountedCumulativeGain)))
    o))

(defn to-edn
  [^RankingMetrics arg]
  {:post [(global/strict! :gcp.bindings.services.bigquery.model/RankingMetrics
                          %)]}
  (cond-> {}
    (.getAverageRank arg) (assoc :averageRank (.getAverageRank arg))
    (.getMeanAveragePrecision arg) (assoc :meanAveragePrecision
                                     (.getMeanAveragePrecision arg))
    (.getMeanSquaredError arg) (assoc :meanSquaredError
                                 (.getMeanSquaredError arg))
    (.getNormalizedDiscountedCumulativeGain arg)
      (assoc :normalizedDiscountedCumulativeGain
        (.getNormalizedDiscountedCumulativeGain arg))))

(def schema
  [:map
   {:closed true,
    :doc
      "Evaluation metrics used by weighted-ALS models specified by feedback_type=implicit.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.bindings.services.bigquery.model/RankingMetrics}
   [:averageRank
    {:getter-doc
       "Determines the goodness of a ranking by computing the percentile rank from the predicted\nconfidence and dividing it by the original rank.\n\n@return value or {@code null} for none",
     :setter-doc
       "Determines the goodness of a ranking by computing the percentile rank from the predicted\nconfidence and dividing it by the original rank.\n\n@param averageRank averageRank or {@code null} for none",
     :optional true} :double]
   [:meanAveragePrecision
    {:getter-doc
       "Calculates a precision per user for all the items by ranking them and then averages all the\nprecisions across all the users.\n\n@return value or {@code null} for none",
     :setter-doc
       "Calculates a precision per user for all the items by ranking them and then averages all the\nprecisions across all the users.\n\n@param meanAveragePrecision meanAveragePrecision or {@code null} for none",
     :optional true} :double]
   [:meanSquaredError
    {:getter-doc
       "Similar to the mean squared error computed in regression and explicit recommendation models\nexcept instead of computing the rating directly, the output from evaluate is computed against a\npreference which is 1 or 0 depending on if the rating exists or not.\n\n@return value or {@code null} for none",
     :setter-doc
       "Similar to the mean squared error computed in regression and explicit recommendation models\nexcept instead of computing the rating directly, the output from evaluate is computed against a\npreference which is 1 or 0 depending on if the rating exists or not.\n\n@param meanSquaredError meanSquaredError or {@code null} for none",
     :optional true} :double]
   [:normalizedDiscountedCumulativeGain
    {:getter-doc
       "A metric to determine the goodness of a ranking calculated from the predicted confidence by\ncomparing it to an ideal rank measured by the original ratings.\n\n@return value or {@code null} for none",
     :setter-doc
       "A metric to determine the goodness of a ranking calculated from the predicted confidence by\ncomparing it to an ideal rank measured by the original ratings.\n\n@param normalizedDiscountedCumulativeGain normalizedDiscountedCumulativeGain or {@code null} for none",
     :optional true} :double]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.services.bigquery.model/RankingMetrics schema}
    {:gcp.global/name "gcp.bindings.services.bigquery.model.RankingMetrics"}))