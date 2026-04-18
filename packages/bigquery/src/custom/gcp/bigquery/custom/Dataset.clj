(ns gcp.bigquery.custom.Dataset
  (:require
   [gcp.bigquery.DatasetInfo :as DatasetInfo]
   [gcp.global :as global])
  (:import
   (com.google.cloud.bigquery Dataset)))

(defn to-edn [^Dataset arg]
  (when arg
    (assoc (DatasetInfo/to-edn arg) :bigquery (.getBigQuery arg))))
