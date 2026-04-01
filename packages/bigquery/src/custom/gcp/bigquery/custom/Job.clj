(ns gcp.bigquery.custom.Job
  (:require [gcp.bigquery.JobInfo :as JobInfo])
  (:import [com.google.cloud.bigquery Job]))

(defn to-edn [^Job arg]
  (when arg
    (assoc (JobInfo/to-edn arg) :bigquery (.getBigQuery arg))))
