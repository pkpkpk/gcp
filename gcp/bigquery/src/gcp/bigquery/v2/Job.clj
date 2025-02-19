(ns gcp.bigquery.v2.Job
  (:require [gcp.bigquery.v2.JobInfo :as JobInfo])
  (:import [com.google.cloud.bigquery Job]))

(defn to-edn [^Job arg]
  (when arg
    (assoc (JobInfo/to-edn arg) :bigquery (.getBigQuery arg))))