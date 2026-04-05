;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.api.services.bigquery.model.DataSplitResult
  {:doc
     "Data split result. This contains references to the training and evaluation data tables that were\nused to train the model.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.DataSplitResult"
   :gcp.dev/certification
     {:base-seed 1775130995154
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1775130995154 :standard 1775130995155 :stress 1775130995156}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:56:36.363009670Z"}}
  (:require [gcp.api.services.bigquery.model.TableReference :as TableReference]
            [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model DataSplitResult]))

(declare from-edn to-edn)

(defn ^DataSplitResult from-edn
  [arg]
  (global/strict! :gcp.api.services.bigquery.model/DataSplitResult arg)
  (let [o (new DataSplitResult)]
    (when (some? (get arg :evaluationTable))
      (.setEvaluationTable o
                           (TableReference/from-edn (get arg
                                                         :evaluationTable))))
    (when (some? (get arg :testTable))
      (.setTestTable o (TableReference/from-edn (get arg :testTable))))
    (when (some? (get arg :trainingTable))
      (.setTrainingTable o (TableReference/from-edn (get arg :trainingTable))))
    o))

(defn to-edn
  [^DataSplitResult arg]
  {:post [(global/strict! :gcp.api.services.bigquery.model/DataSplitResult %)]}
  (when arg
    (cond-> {}
      (.getEvaluationTable arg) (assoc :evaluationTable
                                  (TableReference/to-edn (.getEvaluationTable
                                                           arg)))
      (.getTestTable arg) (assoc :testTable
                            (TableReference/to-edn (.getTestTable arg)))
      (.getTrainingTable arg) (assoc :trainingTable
                                (TableReference/to-edn (.getTrainingTable
                                                         arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "Data split result. This contains references to the training and evaluation data tables that were\nused to train the model.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.api.services.bigquery.model/DataSplitResult}
   [:evaluationTable
    {:getter-doc
       "Table reference of the evaluation data after split.\n\n@return value or {@code null} for none",
     :setter-doc
       "Table reference of the evaluation data after split.\n\n@param evaluationTable evaluationTable or {@code null} for none",
     :optional true} :gcp.api.services.bigquery.model/TableReference]
   [:testTable
    {:getter-doc
       "Table reference of the test data after split.\n\n@return value or {@code null} for none",
     :setter-doc
       "Table reference of the test data after split.\n\n@param testTable testTable or {@code null} for none",
     :optional true} :gcp.api.services.bigquery.model/TableReference]
   [:trainingTable
    {:getter-doc
       "Table reference of the training data after split.\n\n@return value or {@code null} for none",
     :setter-doc
       "Table reference of the training data after split.\n\n@param trainingTable trainingTable or {@code null} for none",
     :optional true} :gcp.api.services.bigquery.model/TableReference]])

(global/include-schema-registry!
  (with-meta {:gcp.api.services.bigquery.model/DataSplitResult schema}
    {:gcp.global/name "gcp.api.services.bigquery.model.DataSplitResult"}))