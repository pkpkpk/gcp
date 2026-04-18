;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.api.services.bigquery.model.CategoricalValue
  {:doc
     "Representative value of a categorical feature.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.CategoricalValue"
   :gcp.dev/certification
     {:base-seed 1776499452777
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1776499452777 :standard 1776499452778 :stress 1776499452779}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:04:14.087870211Z"}}
  (:require [gcp.api.services.bigquery.model.CategoryCount :as CategoryCount]
            [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model CategoricalValue]))

(declare from-edn to-edn)

(defn ^CategoricalValue from-edn
  [arg]
  (global/strict! :gcp.api.services.bigquery.model/CategoricalValue arg)
  (let [o (new CategoricalValue)]
    (when (some? (get arg :categoryCounts))
      (.setCategoryCounts o
                          (mapv CategoryCount/from-edn
                            (get arg :categoryCounts))))
    o))

(defn to-edn
  [^CategoricalValue arg]
  {:post [(global/strict! :gcp.api.services.bigquery.model/CategoricalValue %)]}
  (when arg
    (cond-> {}
      (seq (.getCategoryCounts arg)) (assoc :categoryCounts
                                       (mapv CategoryCount/to-edn
                                         (.getCategoryCounts arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "Representative value of a categorical feature.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.api.services.bigquery.model/CategoricalValue}
   [:categoryCounts
    {:getter-doc
       "Counts of all categories for the categorical feature. If there are more than ten categories, we\nreturn top ten (by count) and return one more CategoryCount with category \"_OTHER_\" and count\nas aggregate counts of remaining categories.\n\n@return value or {@code null} for none",
     :setter-doc
       "Counts of all categories for the categorical feature. If there are more than ten categories, we\nreturn top ten (by count) and return one more CategoryCount with category \"_OTHER_\" and count\nas aggregate counts of remaining categories.\n\n@param categoryCounts categoryCounts or {@code null} for none",
     :optional true}
    [:sequential {:min 1} :gcp.api.services.bigquery.model/CategoryCount]]])

(global/include-schema-registry!
  (with-meta {:gcp.api.services.bigquery.model/CategoricalValue schema}
    {:gcp.global/name "gcp.api.services.bigquery.model.CategoricalValue"}))