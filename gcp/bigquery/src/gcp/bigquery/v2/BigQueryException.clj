(ns gcp.bigquery.v2.BigQueryException
  (:require [gcp.bigquery.v2.BigQueryError :as BigQueryError]
            [gcp.global :as g])
  (:import (com.google.cloud.bigquery BigQueryException)))

; https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery.BigQueryException
(defn to-edn [^BigQueryException arg]
  (when-let [errors (not-empty (.getErrors arg))]
    (g/coerce :gcp/bigquery.BigQueryException {:errors (mapv BigQueryError/to-edn errors)})))