;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.api.services.bigquery.model.FeatureValue
  {:doc
     "Representative value of a single feature within the cluster.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.FeatureValue"
   :gcp.dev/certification
     {:base-seed 1779204716429
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1779204716429 :standard 1779204716430 :stress 1779204716431}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-05-19T15:31:57.294261622Z"}}
  (:require [gcp.api.services.bigquery.model.CategoricalValue :as
             CategoricalValue]
            [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model FeatureValue]))

(declare from-edn to-edn)

(defn ^FeatureValue from-edn
  [arg]
  (global/strict! :gcp.api.services.bigquery.model/FeatureValue arg)
  (let [o (new FeatureValue)]
    (when (some? (get arg :categoricalValue))
      (.setCategoricalValue o
                            (CategoricalValue/from-edn
                              (get arg :categoricalValue))))
    (when (some? (get arg :featureColumn))
      (.setFeatureColumn o (get arg :featureColumn)))
    (when (some? (get arg :numericalValue))
      (.setNumericalValue o (double (get arg :numericalValue))))
    o))

(defn to-edn
  [^FeatureValue arg]
  {:post [(global/strict! :gcp.api.services.bigquery.model/FeatureValue %)]}
  (when arg
    (cond-> {}
      (.getCategoricalValue arg) (assoc :categoricalValue
                                   (CategoricalValue/to-edn
                                     (.getCategoricalValue arg)))
      (some->> (.getFeatureColumn arg)
               (not= ""))
        (assoc :featureColumn (.getFeatureColumn arg))
      (.getNumericalValue arg) (assoc :numericalValue
                                 (.getNumericalValue arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Representative value of a single feature within the cluster.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.api.services.bigquery.model/FeatureValue}
   [:categoricalValue
    {:getter-doc
       "The categorical feature value.\n\n@return value or {@code null} for none",
     :setter-doc
       "The categorical feature value.\n\n@param categoricalValue categoricalValue or {@code null} for none",
     :optional true} :gcp.api.services.bigquery.model/CategoricalValue]
   [:featureColumn
    {:getter-doc
       "The feature column name.\n\n@return value or {@code null} for none",
     :setter-doc
       "The feature column name.\n\n@param featureColumn featureColumn or {@code null} for none",
     :optional true} [:string {:min 1, :gen/max 1}]]
   [:numericalValue
    {:getter-doc
       "The numerical feature value. This is the centroid value for this feature.\n\n@return value or {@code null} for none",
     :setter-doc
       "The numerical feature value. This is the centroid value for this feature.\n\n@param numericalValue numericalValue or {@code null} for none",
     :optional true} :f64]])

(global/include-schema-registry!
  (with-meta {:gcp.api.services.bigquery.model/FeatureValue schema}
    {:gcp.global/name "gcp.api.services.bigquery.model.FeatureValue"}))