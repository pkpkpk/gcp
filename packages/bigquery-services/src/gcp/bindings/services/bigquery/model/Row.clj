;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.services.bigquery.model.Row
  {:doc
     "A single row in the confusion matrix.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.Row"
   :gcp.dev/certification
     {:base-seed 1772390237220
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1772390237220 :standard 1772390237221 :stress 1772390237222}
      :protocol-hash
        "f22c161c7a00aa071f6a8b78764e5c684eaa491c76675456f17e0b44b2b8578c"
      :timestamp "2026-03-01T18:37:17.244906387Z"}}
  (:require [gcp.bindings.services.bigquery.model.BigqueryEntry :as
             BigqueryEntry]
            [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model Row]))

(defn ^Row from-edn
  [arg]
  (global/strict! :gcp.bindings.services.bigquery.model/Row arg)
  (let [o (new Row)]
    (when (some? (get arg :actualLabel))
      (.setActualLabel o (get arg :actualLabel)))
    (when (some? (get arg :entries))
      (.setEntries o (map BigqueryEntry/from-edn (get arg :entries))))
    o))

(defn to-edn
  [^Row arg]
  {:post [(global/strict! :gcp.bindings.services.bigquery.model/Row %)]}
  (cond-> {}
    (.getActualLabel arg) (assoc :actualLabel (.getActualLabel arg))
    (.getEntries arg) (assoc :entries
                        (map BigqueryEntry/to-edn (.getEntries arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "A single row in the confusion matrix.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.bindings.services.bigquery.model/Row}
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
    [:sequential {:min 1}
     :gcp.bindings.services.bigquery.model/BigqueryEntry]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.services.bigquery.model/Row schema}
    {:gcp.global/name "gcp.bindings.services.bigquery.model.Row"}))