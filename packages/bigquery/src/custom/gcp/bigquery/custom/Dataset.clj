(ns gcp.bigquery.custom.Dataset
  (:require [gcp.global :as global]
            ;[gcp.bigquery.v2.BigQuery]
            [gcp.bigquery.DatasetInfo :as DatasetInfo])
  (:import (com.google.cloud.bigquery Dataset)))

(defn to-edn [^Dataset arg]
  (when arg
    (assoc (DatasetInfo/to-edn arg) :bigquery (.getBigQuery arg))))
