;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.api.services.bigquery.model.GlobalExplanation
  {:doc
     "Global explanations containing the top most important features after training.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.GlobalExplanation"
   :gcp.dev/certification
     {:base-seed 1776499490894
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1776499490894 :standard 1776499490895 :stress 1776499490896}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:04:52.238698168Z"}}
  (:require [gcp.api.services.bigquery.model.Explanation :as Explanation]
            [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model GlobalExplanation]))

(declare from-edn to-edn)

(defn ^GlobalExplanation from-edn
  [arg]
  (global/strict! :gcp.api.services.bigquery.model/GlobalExplanation arg)
  (let [o (new GlobalExplanation)]
    (when (some? (get arg :classLabel))
      (.setClassLabel o (get arg :classLabel)))
    (when (some? (get arg :explanations))
      (.setExplanations o (mapv Explanation/from-edn (get arg :explanations))))
    o))

(defn to-edn
  [^GlobalExplanation arg]
  {:post [(global/strict! :gcp.api.services.bigquery.model/GlobalExplanation
                          %)]}
  (when arg
    (cond-> {}
      (some->> (.getClassLabel arg)
               (not= ""))
        (assoc :classLabel (.getClassLabel arg))
      (seq (.getExplanations arg)) (assoc :explanations
                                     (mapv Explanation/to-edn
                                       (.getExplanations arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "Global explanations containing the top most important features after training.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.api.services.bigquery.model/GlobalExplanation}
   [:classLabel
    {:getter-doc
       "Class label for this set of global explanations. Will be empty/null for binary logistic and\nlinear regression models. Sorted alphabetically in descending order.\n\n@return value or {@code null} for none",
     :setter-doc
       "Class label for this set of global explanations. Will be empty/null for binary logistic and\nlinear regression models. Sorted alphabetically in descending order.\n\n@param classLabel classLabel or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:explanations
    {:getter-doc
       "A list of the top global explanations. Sorted by absolute value of attribution in descending\norder.\n\n@return value or {@code null} for none",
     :setter-doc
       "A list of the top global explanations. Sorted by absolute value of attribution in descending\norder.\n\n@param explanations explanations or {@code null} for none",
     :optional true}
    [:sequential {:min 1} :gcp.api.services.bigquery.model/Explanation]]])

(global/include-schema-registry!
  (with-meta {:gcp.api.services.bigquery.model/GlobalExplanation schema}
    {:gcp.global/name "gcp.api.services.bigquery.model.GlobalExplanation"}))