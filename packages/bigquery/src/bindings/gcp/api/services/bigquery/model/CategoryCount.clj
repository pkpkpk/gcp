;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.api.services.bigquery.model.CategoryCount
  {:doc
     "Represents the count of a single category within the cluster.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.CategoryCount"
   :gcp.dev/certification
     {:base-seed 1776499450946
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1776499450946 :standard 1776499450947 :stress 1776499450948}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:04:12.276514093Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model CategoryCount]))

(declare from-edn to-edn)

(defn ^CategoryCount from-edn
  [arg]
  (global/strict! :gcp.api.services.bigquery.model/CategoryCount arg)
  (let [o (new CategoryCount)]
    (when (some? (get arg :category)) (.setCategory o (get arg :category)))
    (when (some? (get arg :count)) (.setCount o (long (get arg :count))))
    o))

(defn to-edn
  [^CategoryCount arg]
  {:post [(global/strict! :gcp.api.services.bigquery.model/CategoryCount %)]}
  (when arg
    (cond-> {}
      (some->> (.getCategory arg)
               (not= ""))
        (assoc :category (.getCategory arg))
      (.getCount arg) (assoc :count (.getCount arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Represents the count of a single category within the cluster.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.api.services.bigquery.model/CategoryCount}
   [:category
    {:getter-doc
       "The name of category.\n\n@return value or {@code null} for none",
     :setter-doc
       "The name of category.\n\n@param category category or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:count
    {:getter-doc
       "The count of training samples matching the category within the cluster.\n\n@return value or {@code null} for none",
     :setter-doc
       "The count of training samples matching the category within the cluster.\n\n@param count count or {@code null} for none",
     :optional true} :i64]])

(global/include-schema-registry!
  (with-meta {:gcp.api.services.bigquery.model/CategoryCount schema}
    {:gcp.global/name "gcp.api.services.bigquery.model.CategoryCount"}))