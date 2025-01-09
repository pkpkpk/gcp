(ns gcp.bigquery.v2.BigQuery.IAMOption
  (:import (com.google.cloud.bigquery BigQuery$IAMOption)))

(defn from-edn
  [{:keys [version]}]
  (BigQuery$IAMOption/requestedPolicyVersion (long version)))