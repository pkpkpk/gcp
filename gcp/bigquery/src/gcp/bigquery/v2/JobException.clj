(ns gcp.bigquery.v2.JobException
  (:require [gcp.bigquery.v2.BigQueryException :as BigQueryException]
            [gcp.bigquery.v2.JobId :as JobId]
            [gcp.global :as g])
  (:import (com.google.cloud.bigquery JobException)))

(defn to-edn [^JobException arg]
  {:post [(g/strict! :gcp/bigquery.JobException %)]}
  {:id     (JobId/to-edn (.getId arg))
   :errors (mapv BigQueryException/to-edn (.getErrors arg))})