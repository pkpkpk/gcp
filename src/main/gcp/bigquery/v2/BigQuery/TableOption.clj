(ns gcp.bigquery.v2.BigQuery.TableOption
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery BigQuery$TableField BigQuery$TableMetadataView BigQuery$TableOption)))

(defn ^BigQuery$TableOption from-edn
  [{:keys [autoDetect fields tableMetadataView] :as arg}]
  (global/strict! :gcp/bigquery.BigQuery.TableOption arg)
  (if autoDetect
    (BigQuery$TableOption/autodetectSchema autoDetect)
    (if tableMetadataView
      (BigQuery$TableOption/tableMetadataView (BigQuery$TableMetadataView/valueOf tableMetadataView))
      (BigQuery$TableOption/fields (into-array BigQuery$TableField (map #(BigQuery$TableField/valueOf %) fields))))))
