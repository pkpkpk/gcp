;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.api.services.bigquery.model.IterationResult
  {:doc
     "Information about a single iteration of the training run.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.IterationResult"
   :gcp.dev/certification
     {:base-seed 1776499377198
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1776499377198 :standard 1776499377199 :stress 1776499377200}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:02:58.761173552Z"}}
  (:require [gcp.api.services.bigquery.model.ArimaResult :as ArimaResult]
            [gcp.api.services.bigquery.model.ClusterInfo :as ClusterInfo]
            [gcp.api.services.bigquery.model.PrincipalComponentInfo :as
             PrincipalComponentInfo]
            [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model IterationResult]))

(declare from-edn to-edn)

(defn ^IterationResult from-edn
  [arg]
  (global/strict! :gcp.api.services.bigquery.model/IterationResult arg)
  (let [o (new IterationResult)]
    (when (some? (get arg :arimaResult))
      (.setArimaResult o (ArimaResult/from-edn (get arg :arimaResult))))
    (when (some? (get arg :clusterInfos))
      (.setClusterInfos o (mapv ClusterInfo/from-edn (get arg :clusterInfos))))
    (when (some? (get arg :durationMs))
      (.setDurationMs o (long (get arg :durationMs))))
    (when (some? (get arg :evalLoss))
      (.setEvalLoss o (double (get arg :evalLoss))))
    (when (some? (get arg :learnRate))
      (.setLearnRate o (double (get arg :learnRate))))
    (when (some? (get arg :principalComponentInfos))
      (.setPrincipalComponentInfos o
                                   (mapv PrincipalComponentInfo/from-edn
                                     (get arg :principalComponentInfos))))
    (when (some? (get arg :trainingLoss))
      (.setTrainingLoss o (double (get arg :trainingLoss))))
    o))

(defn to-edn
  [^IterationResult arg]
  {:post [(global/strict! :gcp.api.services.bigquery.model/IterationResult %)]}
  (when arg
    (cond-> {}
      (.getArimaResult arg) (assoc :arimaResult
                              (ArimaResult/to-edn (.getArimaResult arg)))
      (seq (.getClusterInfos arg))
        (assoc :clusterInfos (mapv ClusterInfo/to-edn (.getClusterInfos arg)))
      (.getDurationMs arg) (assoc :durationMs (.getDurationMs arg))
      (.getEvalLoss arg) (assoc :evalLoss (.getEvalLoss arg))
      (.getLearnRate arg) (assoc :learnRate (.getLearnRate arg))
      (seq (.getPrincipalComponentInfos arg))
        (assoc :principalComponentInfos
          (mapv PrincipalComponentInfo/to-edn
            (.getPrincipalComponentInfos arg)))
      (.getTrainingLoss arg) (assoc :trainingLoss (.getTrainingLoss arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Information about a single iteration of the training run.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.api.services.bigquery.model/IterationResult}
   [:arimaResult
    {:getter-doc "Arima result.\n\n@return value or {@code null} for none",
     :setter-doc
       "Arima result.\n\n@param arimaResult arimaResult or {@code null} for none",
     :optional true} :gcp.api.services.bigquery.model/ArimaResult]
   [:clusterInfos
    {:getter-doc
       "Information about top clusters for clustering models.\n\n@return value or {@code null} for none",
     :setter-doc
       "Information about top clusters for clustering models.\n\n@param clusterInfos clusterInfos or {@code null} for none",
     :optional true}
    [:sequential {:min 1} :gcp.api.services.bigquery.model/ClusterInfo]]
   [:durationMs
    {:getter-doc
       "Time taken to run the iteration in milliseconds.\n\n@return value or {@code null} for none",
     :setter-doc
       "Time taken to run the iteration in milliseconds.\n\n@param durationMs durationMs or {@code null} for none",
     :optional true} :i64]
   [:evalLoss
    {:getter-doc
       "Loss computed on the eval data at the end of iteration.\n\n@return value or {@code null} for none",
     :setter-doc
       "Loss computed on the eval data at the end of iteration.\n\n@param evalLoss evalLoss or {@code null} for none",
     :optional true} :f64]
   [:learnRate
    {:getter-doc
       "Learn rate used for this iteration.\n\n@return value or {@code null} for none",
     :setter-doc
       "Learn rate used for this iteration.\n\n@param learnRate learnRate or {@code null} for none",
     :optional true} :f64]
   [:principalComponentInfos
    {:getter-doc
       "The information of the principal components.\n\n@return value or {@code null} for none",
     :setter-doc
       "The information of the principal components.\n\n@param principalComponentInfos principalComponentInfos or {@code null} for none",
     :optional true}
    [:sequential {:min 1}
     :gcp.api.services.bigquery.model/PrincipalComponentInfo]]
   [:trainingLoss
    {:getter-doc
       "Loss computed on the training data at the end of iteration.\n\n@return value or {@code null} for none",
     :setter-doc
       "Loss computed on the training data at the end of iteration.\n\n@param trainingLoss trainingLoss or {@code null} for none",
     :optional true} :f64]])

(global/include-schema-registry!
  (with-meta {:gcp.api.services.bigquery.model/IterationResult schema}
    {:gcp.global/name "gcp.api.services.bigquery.model.IterationResult"}))