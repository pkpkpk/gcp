(ns gcp.bigquery.v2.BigQuery.DatasetListOption
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery BigQuery$DatasetListOption)))

(defn ^BigQuery$DatasetListOption from-edn
  [{:keys [labelFilter pageSize pageToken] :as arg}]
  (global/strict! :gcp/bigquery.BigQuery.DatasetListOption arg)
  (if (contains? arg :all)
    (BigQuery$DatasetListOption/all)
    (if labelFilter
      (BigQuery$DatasetListOption/labelFilter labelFilter)
      (if pageSize
        (BigQuery$DatasetListOption/pageSize pageSize)
        (BigQuery$DatasetListOption/pageToken pageToken)))))