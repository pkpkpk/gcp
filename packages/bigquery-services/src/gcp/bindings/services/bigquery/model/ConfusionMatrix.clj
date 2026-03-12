;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.services.bigquery.model.ConfusionMatrix
  {:doc
     "Confusion matrix for multi-class classification models.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.ConfusionMatrix"
   :gcp.dev/certification
     {:base-seed 1772390251045
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1772390251045 :standard 1772390251046 :stress 1772390251047}
      :protocol-hash
        "f22c161c7a00aa071f6a8b78764e5c684eaa491c76675456f17e0b44b2b8578c"
      :timestamp "2026-03-01T18:37:31.240378683Z"}}
  (:require [gcp.bindings.services.bigquery.model.Row :as Row]
            [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model ConfusionMatrix]))

(defn ^ConfusionMatrix from-edn
  [arg]
  (global/strict! :gcp.bindings.services.bigquery.model/ConfusionMatrix arg)
  (let [o (new ConfusionMatrix)]
    (when (some? (get arg :confidenceThreshold))
      (.setConfidenceThreshold o (get arg :confidenceThreshold)))
    (when (some? (get arg :rows))
      (.setRows o (map Row/from-edn (get arg :rows))))
    o))

(defn to-edn
  [^ConfusionMatrix arg]
  {:post [(global/strict! :gcp.bindings.services.bigquery.model/ConfusionMatrix
                          %)]}
  (cond-> {}
    (.getConfidenceThreshold arg) (assoc :confidenceThreshold
                                    (.getConfidenceThreshold arg))
    (.getRows arg) (assoc :rows (map Row/to-edn (.getRows arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Confusion matrix for multi-class classification models.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.bindings.services.bigquery.model/ConfusionMatrix}
   [:confidenceThreshold
    {:getter-doc
       "Confidence threshold used when computing the entries of the confusion matrix.\n\n@return value or {@code null} for none",
     :setter-doc
       "Confidence threshold used when computing the entries of the confusion matrix.\n\n@param confidenceThreshold confidenceThreshold or {@code null} for none",
     :optional true} :double]
   [:rows
    {:getter-doc
       "One row per actual label.\n\n@return value or {@code null} for none",
     :setter-doc
       "One row per actual label.\n\n@param rows rows or {@code null} for none",
     :optional true}
    [:sequential {:min 1} :gcp.bindings.services.bigquery.model/Row]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.services.bigquery.model/ConfusionMatrix schema}
    {:gcp.global/name "gcp.bindings.services.bigquery.model.ConfusionMatrix"}))