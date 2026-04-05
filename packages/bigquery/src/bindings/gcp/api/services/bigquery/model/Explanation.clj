;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.api.services.bigquery.model.Explanation
  {:doc
     "Explanation for a single feature.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.Explanation"
   :gcp.dev/certification
     {:base-seed 1775130996920
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1775130996920 :standard 1775130996921 :stress 1775130996922}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:56:38.073513893Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model Explanation]))

(declare from-edn to-edn)

(defn ^Explanation from-edn
  [arg]
  (global/strict! :gcp.api.services.bigquery.model/Explanation arg)
  (let [o (new Explanation)]
    (when (some? (get arg :attribution))
      (.setAttribution o (double (get arg :attribution))))
    (when (some? (get arg :featureName))
      (.setFeatureName o (get arg :featureName)))
    o))

(defn to-edn
  [^Explanation arg]
  {:post [(global/strict! :gcp.api.services.bigquery.model/Explanation %)]}
  (when arg
    (cond-> {}
      (.getAttribution arg) (assoc :attribution (.getAttribution arg))
      (some->> (.getFeatureName arg)
               (not= ""))
        (assoc :featureName (.getFeatureName arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Explanation for a single feature.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.api.services.bigquery.model/Explanation}
   [:attribution
    {:getter-doc
       "Attribution of feature.\n\n@return value or {@code null} for none",
     :setter-doc
       "Attribution of feature.\n\n@param attribution attribution or {@code null} for none",
     :optional true} :f64]
   [:featureName
    {:getter-doc
       "The full feature name. For non-numerical features, will be formatted like `.`. Overall size of\nfeature name will always be truncated to first 120 characters.\n\n@return value or {@code null} for none",
     :setter-doc
       "The full feature name. For non-numerical features, will be formatted like `.`. Overall size of\nfeature name will always be truncated to first 120 characters.\n\n@param featureName featureName or {@code null} for none",
     :optional true} [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.api.services.bigquery.model/Explanation schema}
    {:gcp.global/name "gcp.api.services.bigquery.model.Explanation"}))