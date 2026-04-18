;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.api.services.bigquery.model.BigqueryEntry
  {:doc
     "A single entry in the confusion matrix.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.BigqueryEntry"
   :gcp.dev/certification
     {:base-seed 1776499405270
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1776499405270 :standard 1776499405271 :stress 1776499405272}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:03:27.345895820Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model BigqueryEntry]))

(declare from-edn to-edn)

(defn ^BigqueryEntry from-edn
  [arg]
  (global/strict! :gcp.api.services.bigquery.model/BigqueryEntry arg)
  (let [o (new BigqueryEntry)]
    (when (some? (get arg :itemCount))
      (.setItemCount o (long (get arg :itemCount))))
    (when (some? (get arg :predictedLabel))
      (.setPredictedLabel o (get arg :predictedLabel)))
    o))

(defn to-edn
  [^BigqueryEntry arg]
  {:post [(global/strict! :gcp.api.services.bigquery.model/BigqueryEntry %)]}
  (when arg
    (cond-> {}
      (.getItemCount arg) (assoc :itemCount (.getItemCount arg))
      (some->> (.getPredictedLabel arg)
               (not= ""))
        (assoc :predictedLabel (.getPredictedLabel arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "A single entry in the confusion matrix.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.api.services.bigquery.model/BigqueryEntry}
   [:itemCount
    {:getter-doc
       "Number of items being predicted as this label.\n\n@return value or {@code null} for none",
     :setter-doc
       "Number of items being predicted as this label.\n\n@param itemCount itemCount or {@code null} for none",
     :optional true} :i64]
   [:predictedLabel
    {:getter-doc
       "The predicted label. For confidence_threshold > 0, we will also add an entry indicating the\nnumber of items under the confidence threshold.\n\n@return value or {@code null} for none",
     :setter-doc
       "The predicted label. For confidence_threshold > 0, we will also add an entry indicating the\nnumber of items under the confidence threshold.\n\n@param predictedLabel predictedLabel or {@code null} for none",
     :optional true} [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.api.services.bigquery.model/BigqueryEntry schema}
    {:gcp.global/name "gcp.api.services.bigquery.model.BigqueryEntry"}))