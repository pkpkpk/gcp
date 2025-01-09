(ns gcp.bigquery.v2.BigQuery.DatasetDeleteOption
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery BigQuery$DatasetDeleteOption)))

(defn from-edn [arg]
  (global/strict! :bigquery.BigQuery/DatasetDeleteOption arg)
  (if (contains? arg :deleteContents)
    (BigQuery$DatasetDeleteOption/deleteContents)
    (throw (ex-info "bad arg to DatasetDeleteOption" {:arg arg}))))