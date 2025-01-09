(ns gcp.bigquery.v2.BigQueryOptions
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery BigQuery BigQueryOptions]))

(defn ^BigQueryOptions from-edn
  [{:keys [location transportOptions useInt64Timestamps] :as arg}]
  (global/strict! :bigquery/BigQueryOptions arg)
  (let [builder (BigQueryOptions/newBuilder)]
    (when location
      (.setLocation builder location))
    (when transportOptions
      (.setTransportOptions builder transportOptions))
    (when useInt64Timestamps
      (.setUseInt64Timestamps builder useInt64Timestamps))
    (.build builder)))

(defn ^BigQuery get-service [arg]
  (if (instance? BigQueryOptions arg)
    (.getService arg)
    (.getService (from-edn arg))))