(ns gcp.bigquery.v2.JobStatus
  (:require [gcp.bigquery.v2.BigQueryError :as BigQueryError])
  (:import (com.google.cloud.bigquery JobStatus)))

(defn to-edn [^JobStatus arg]
  (cond-> {:state (.name (.getState arg))}
          (seq (.getExecutionErrors arg))
          (assoc :executionErrors (map BigQueryError/to-edn (.getExecutionErrors arg)))
          (some? (.getError arg))
          (assoc :error (BigQueryError/to-edn (.getError arg)))))