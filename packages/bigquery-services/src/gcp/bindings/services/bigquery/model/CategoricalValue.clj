;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.services.bigquery.model.CategoricalValue
  {:doc
     "Representative value of a categorical feature.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.CategoricalValue"
   :gcp.dev/certification
     {:base-seed 1772390238510
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1772390238510 :standard 1772390238511 :stress 1772390238512}
      :protocol-hash
        "f22c161c7a00aa071f6a8b78764e5c684eaa491c76675456f17e0b44b2b8578c"
      :timestamp "2026-03-01T18:37:18.537900853Z"}}
  (:require [gcp.bindings.services.bigquery.model.CategoryCount :as
             CategoryCount]
            [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model CategoricalValue]))

(defn ^CategoricalValue from-edn
  [arg]
  (global/strict! :gcp.bindings.services.bigquery.model/CategoricalValue arg)
  (let [o (new CategoricalValue)]
    (when (some? (get arg :categoryCounts))
      (.setCategoryCounts o
                          (map CategoryCount/from-edn
                            (get arg :categoryCounts))))
    o))

(defn to-edn
  [^CategoricalValue arg]
  {:post [(global/strict! :gcp.bindings.services.bigquery.model/CategoricalValue
                          %)]}
  (cond-> {}
    (.getCategoryCounts arg) (assoc :categoryCounts
                               (map CategoryCount/to-edn
                                 (.getCategoryCounts arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Representative value of a categorical feature.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.bindings.services.bigquery.model/CategoricalValue}
   [:categoryCounts
    {:getter-doc
       "Counts of all categories for the categorical feature. If there are more than ten categories, we\nreturn top ten (by count) and return one more CategoryCount with category \"_OTHER_\" and count\nas aggregate counts of remaining categories.\n\n@return value or {@code null} for none",
     :setter-doc
       "Counts of all categories for the categorical feature. If there are more than ten categories, we\nreturn top ten (by count) and return one more CategoryCount with category \"_OTHER_\" and count\nas aggregate counts of remaining categories.\n\n@param categoryCounts categoryCounts or {@code null} for none",
     :optional true}
    [:sequential {:min 1}
     :gcp.bindings.services.bigquery.model/CategoryCount]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.services.bigquery.model/CategoricalValue schema}
    {:gcp.global/name "gcp.bindings.services.bigquery.model.CategoricalValue"}))