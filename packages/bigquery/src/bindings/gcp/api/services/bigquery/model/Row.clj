;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.api.services.bigquery.model.Row
  {:doc
     "A single row in the confusion matrix.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.Row"
   :gcp.dev/certification
     {:base-seed 1776499407903
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1776499407903 :standard 1776499407904 :stress 1776499407905}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:03:29.273892164Z"}}
  (:require [gcp.api.services.bigquery.model.BigqueryEntry :as BigqueryEntry]
            [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model Row]))

(declare from-edn to-edn)

(defn ^Row from-edn
  [arg]
  (global/strict! :gcp.api.services.bigquery.model/Row arg)
  (let [o (new Row)]
    (when (some? (get arg :actualLabel))
      (.setActualLabel o (get arg :actualLabel)))
    (when (some? (get arg :entries))
      (.setEntries o (mapv BigqueryEntry/from-edn (get arg :entries))))
    o))

(defn to-edn
  [^Row arg]
  {:post [(global/strict! :gcp.api.services.bigquery.model/Row %)]}
  (when arg
    (cond-> {}
      (some->> (.getActualLabel arg)
               (not= ""))
        (assoc :actualLabel (.getActualLabel arg))
      (seq (.getEntries arg))
        (assoc :entries (mapv BigqueryEntry/to-edn (.getEntries arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "A single row in the confusion matrix.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.api.services.bigquery.model/Row}
   [:actualLabel
    {:getter-doc
       "The original label of this row.\n\n@return value or {@code null} for none",
     :setter-doc
       "The original label of this row.\n\n@param actualLabel actualLabel or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:entries
    {:getter-doc
       "Info describing predicted label distribution.\n\n@return value or {@code null} for none",
     :setter-doc
       "Info describing predicted label distribution.\n\n@param entries entries or {@code null} for none",
     :optional true}
    [:sequential {:min 1} :gcp.api.services.bigquery.model/BigqueryEntry]]])

(global/include-schema-registry!
  (with-meta {:gcp.api.services.bigquery.model/Row schema}
    {:gcp.global/name "gcp.api.services.bigquery.model.Row"}))