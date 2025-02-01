(ns gcp.bigquery.v2.BigQuery.DatasetOption
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery BigQuery$DatasetField BigQuery$DatasetOption)))

(defn ^BigQuery$DatasetOption from-edn
  [{:keys [fields] :as arg}]
  (global/strict! [:sequential :gcp/bigquery.BigQuery.DatasetField] arg)
  (let [fields (map #(BigQuery$DatasetField/valueOf %) fields)]
    (BigQuery$DatasetOption/fields (into-array BigQuery$DatasetField fields))))