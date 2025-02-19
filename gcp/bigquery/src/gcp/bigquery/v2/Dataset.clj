(ns gcp.bigquery.v2.Dataset
  (:require [gcp.bigquery.v2.DatasetInfo :as DatasetInfo])
  (:import (com.google.cloud.bigquery Dataset)))

(defn to-edn [^Dataset arg]
  (when arg
    (assoc (DatasetInfo/to-edn arg) :bigquery (.getBigQuery arg))))