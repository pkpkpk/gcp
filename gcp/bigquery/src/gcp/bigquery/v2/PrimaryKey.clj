(ns gcp.bigquery.v2.PrimaryKey
  (:import (com.google.cloud.bigquery PrimaryKey)))

(defn ^PrimaryKey from-edn [{:keys [columns]}]
  (let [builder (PrimaryKey/newBuilder)]
    (.setColumns builder (seq columns))
    (.build builder)))

(defn to-edn [^PrimaryKey arg]
  {:columns (vec (.getColumns arg))})