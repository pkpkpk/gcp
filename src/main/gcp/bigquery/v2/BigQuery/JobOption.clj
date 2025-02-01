(ns gcp.bigquery.v2.BigQuery.JobOption
  (:require [gcp.core.RetryOption]
            [gcp.global :as global]
            [gcp.bigquery.v2.BigQueryRetryConfig :as BigQueryRetryConfig])
  (:import (com.google.cloud RetryOption)
           [com.google.cloud.bigquery BigQuery$JobField BigQuery$JobOption]))

(defn ^BigQuery$JobOption from-edn [arg]
  (global/strict! :gcp/bigquery.BigQuery.JobOption arg)
  (if (contains? arg :fields)
    (BigQuery$JobOption/fields (into-array BigQuery$JobField (map #(BigQuery$JobField/valueOf %) (:fields arg))))
    (if (contains? arg :gcp/bigquery.etryConfig)
      (BigQuery$JobOption/bigQueryRetryConfig (BigQueryRetryConfig/from-edn (:gcp/bigquery.etryConfig arg)))
      (BigQuery$JobOption/retryOptions (into-array RetryOption (map gcp.core.RetryOption/from-edn (:options arg)))))))
