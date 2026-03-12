;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.services.bigquery.model.BigqueryEntry
  {:doc
     "A single entry in the confusion matrix.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.BigqueryEntry"
   :gcp.dev/certification
     {:base-seed 1772390236631
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1772390236631 :standard 1772390236632 :stress 1772390236633}
      :protocol-hash
        "f22c161c7a00aa071f6a8b78764e5c684eaa491c76675456f17e0b44b2b8578c"
      :timestamp "2026-03-01T18:37:16.640295641Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model BigqueryEntry]))

(defn ^BigqueryEntry from-edn
  [arg]
  (global/strict! :gcp.bindings.services.bigquery.model/BigqueryEntry arg)
  (let [o (new BigqueryEntry)]
    (when (some? (get arg :itemCount)) (.setItemCount o (get arg :itemCount)))
    (when (some? (get arg :predictedLabel))
      (.setPredictedLabel o (get arg :predictedLabel)))
    o))

(defn to-edn
  [^BigqueryEntry arg]
  {:post [(global/strict! :gcp.bindings.services.bigquery.model/BigqueryEntry
                          %)]}
  (cond-> {}
    (.getItemCount arg) (assoc :itemCount (.getItemCount arg))
    (.getPredictedLabel arg) (assoc :predictedLabel (.getPredictedLabel arg))))

(def schema
  [:map
   {:closed true,
    :doc
      "A single entry in the confusion matrix.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.bindings.services.bigquery.model/BigqueryEntry}
   [:itemCount
    {:getter-doc
       "Number of items being predicted as this label.\n\n@return value or {@code null} for none",
     :setter-doc
       "Number of items being predicted as this label.\n\n@param itemCount itemCount or {@code null} for none",
     :optional true} :int]
   [:predictedLabel
    {:getter-doc
       "The predicted label. For confidence_threshold > 0, we will also add an entry indicating the\nnumber of items under the confidence threshold.\n\n@return value or {@code null} for none",
     :setter-doc
       "The predicted label. For confidence_threshold > 0, we will also add an entry indicating the\nnumber of items under the confidence threshold.\n\n@param predictedLabel predictedLabel or {@code null} for none",
     :optional true} [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.services.bigquery.model/BigqueryEntry schema}
    {:gcp.global/name "gcp.bindings.services.bigquery.model.BigqueryEntry"}))