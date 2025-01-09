(ns gcp.bigquery.v2.Clustering
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery Clustering)))

(defn ^Clustering from-edn [arg]
  (global/strict! :bigquery/Clustering arg)
  (let [builder (Clustering/newBuilder)]
    (.setFields builder (:fields arg))
    (.build builder)))