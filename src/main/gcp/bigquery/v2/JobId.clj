(ns gcp.bigquery.v2.JobId
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery JobId)))

(defn ^JobId from-edn
  [{:keys [jobId project] :as arg}]
  (global/strict! :bigquery/JobId arg)
  (if jobId
    (if project
      (JobId/of project jobId)
      (JobId/of jobId))
    (JobId/of)))