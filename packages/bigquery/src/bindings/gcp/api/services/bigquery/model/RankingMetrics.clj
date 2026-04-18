;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.api.services.bigquery.model.RankingMetrics
  {:doc
     "Evaluation metrics used by weighted-ALS models specified by feedback_type=implicit.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.RankingMetrics"
   :gcp.dev/certification
     {:base-seed 1776499479753
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1776499479753 :standard 1776499479754 :stress 1776499479755}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:04:41.028581190Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model RankingMetrics]))

(declare from-edn to-edn)

(defn ^RankingMetrics from-edn
  [arg]
  (global/strict! :gcp.api.services.bigquery.model/RankingMetrics arg)
  (let [o (new RankingMetrics)]
    (when (some? (get arg :averageRank))
      (.setAverageRank o (double (get arg :averageRank))))
    (when (some? (get arg :meanAveragePrecision))
      (.setMeanAveragePrecision o (double (get arg :meanAveragePrecision))))
    (when (some? (get arg :meanSquaredError))
      (.setMeanSquaredError o (double (get arg :meanSquaredError))))
    (when (some? (get arg :normalizedDiscountedCumulativeGain))
      (.setNormalizedDiscountedCumulativeGain
        o
        (double (get arg :normalizedDiscountedCumulativeGain))))
    o))

(defn to-edn
  [^RankingMetrics arg]
  {:post [(global/strict! :gcp.api.services.bigquery.model/RankingMetrics %)]}
  (when arg
    (cond-> {}
      (.getAverageRank arg) (assoc :averageRank (.getAverageRank arg))
      (.getMeanAveragePrecision arg) (assoc :meanAveragePrecision
                                       (.getMeanAveragePrecision arg))
      (.getMeanSquaredError arg) (assoc :meanSquaredError
                                   (.getMeanSquaredError arg))
      (.getNormalizedDiscountedCumulativeGain arg)
        (assoc :normalizedDiscountedCumulativeGain
          (.getNormalizedDiscountedCumulativeGain arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Evaluation metrics used by weighted-ALS models specified by feedback_type=implicit.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.api.services.bigquery.model/RankingMetrics}
   [:averageRank
    {:getter-doc
       "Determines the goodness of a ranking by computing the percentile rank from the predicted\nconfidence and dividing it by the original rank.\n\n@return value or {@code null} for none",
     :setter-doc
       "Determines the goodness of a ranking by computing the percentile rank from the predicted\nconfidence and dividing it by the original rank.\n\n@param averageRank averageRank or {@code null} for none",
     :optional true} :f64]
   [:meanAveragePrecision
    {:getter-doc
       "Calculates a precision per user for all the items by ranking them and then averages all the\nprecisions across all the users.\n\n@return value or {@code null} for none",
     :setter-doc
       "Calculates a precision per user for all the items by ranking them and then averages all the\nprecisions across all the users.\n\n@param meanAveragePrecision meanAveragePrecision or {@code null} for none",
     :optional true} :f64]
   [:meanSquaredError
    {:getter-doc
       "Similar to the mean squared error computed in regression and explicit recommendation models\nexcept instead of computing the rating directly, the output from evaluate is computed against a\npreference which is 1 or 0 depending on if the rating exists or not.\n\n@return value or {@code null} for none",
     :setter-doc
       "Similar to the mean squared error computed in regression and explicit recommendation models\nexcept instead of computing the rating directly, the output from evaluate is computed against a\npreference which is 1 or 0 depending on if the rating exists or not.\n\n@param meanSquaredError meanSquaredError or {@code null} for none",
     :optional true} :f64]
   [:normalizedDiscountedCumulativeGain
    {:getter-doc
       "A metric to determine the goodness of a ranking calculated from the predicted confidence by\ncomparing it to an ideal rank measured by the original ratings.\n\n@return value or {@code null} for none",
     :setter-doc
       "A metric to determine the goodness of a ranking calculated from the predicted confidence by\ncomparing it to an ideal rank measured by the original ratings.\n\n@param normalizedDiscountedCumulativeGain normalizedDiscountedCumulativeGain or {@code null} for none",
     :optional true} :f64]])

(global/include-schema-registry!
  (with-meta {:gcp.api.services.bigquery.model/RankingMetrics schema}
    {:gcp.global/name "gcp.api.services.bigquery.model.RankingMetrics"}))