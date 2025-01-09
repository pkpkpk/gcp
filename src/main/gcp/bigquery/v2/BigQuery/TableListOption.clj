(ns gcp.bigquery.v2.BigQuery.TableListOption
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery BigQuery$TableDataListOption)))

(defn ^BigQuery$TableDataListOption from-edn
  [{:keys [pageSize pageToken] :as arg}]
  (global/strict! :bigquery.BigQuery/TableListOption arg)
  (if pageSize
    (BigQuery$TableDataListOption/pageSize (long pageSize))
    (BigQuery$TableDataListOption/pageToken pageToken)))