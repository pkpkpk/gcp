;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.services.bigquery.model.DataSplitResult
  {:doc
     "Data split result. This contains references to the training and evaluation data tables that were\nused to train the model.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.DataSplitResult"
   :gcp.dev/certification
     {:base-seed 1772390261833
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1772390261833 :standard 1772390261834 :stress 1772390261835}
      :protocol-hash
        "f22c161c7a00aa071f6a8b78764e5c684eaa491c76675456f17e0b44b2b8578c"
      :timestamp "2026-03-01T18:37:41.888312471Z"}}
  (:require [gcp.bindings.services.bigquery.model.TableReference :as
             TableReference]
            [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model DataSplitResult]))

(defn ^DataSplitResult from-edn
  [arg]
  (global/strict! :gcp.bindings.services.bigquery.model/DataSplitResult arg)
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
  {:post [(global/strict! :gcp.bindings.services.bigquery.model/DataSplitResult
                          %)]}
  (cond-> {}
    (.getEvaluationTable arg)
      (assoc :evaluationTable (TableReference/to-edn (.getEvaluationTable arg)))
    (.getTestTable arg) (assoc :testTable
                          (TableReference/to-edn (.getTestTable arg)))
    (.getTrainingTable arg) (assoc :trainingTable
                              (TableReference/to-edn (.getTrainingTable arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Data split result. This contains references to the training and evaluation data tables that were\nused to train the model.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.bindings.services.bigquery.model/DataSplitResult}
   [:evaluationTable
    {:getter-doc
       "Table reference of the evaluation data after split.\n\n@return value or {@code null} for none",
     :setter-doc
       "Table reference of the evaluation data after split.\n\n@param evaluationTable evaluationTable or {@code null} for none",
     :optional true} :gcp.bindings.services.bigquery.model/TableReference]
   [:testTable
    {:getter-doc
       "Table reference of the test data after split.\n\n@return value or {@code null} for none",
     :setter-doc
       "Table reference of the test data after split.\n\n@param testTable testTable or {@code null} for none",
     :optional true} :gcp.bindings.services.bigquery.model/TableReference]
   [:trainingTable
    {:getter-doc
       "Table reference of the training data after split.\n\n@return value or {@code null} for none",
     :setter-doc
       "Table reference of the training data after split.\n\n@param trainingTable trainingTable or {@code null} for none",
     :optional true} :gcp.bindings.services.bigquery.model/TableReference]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.services.bigquery.model/DataSplitResult schema}
    {:gcp.global/name "gcp.bindings.services.bigquery.model.DataSplitResult"}))