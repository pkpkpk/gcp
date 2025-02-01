(ns gcp.bigquery.v2.JobId
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery JobId)))

(defn ^JobId from-edn
  [{:keys [job project] :as arg}]
  (global/strict! :gcp/bigquery.JobId arg)
  (if job
    (if project
      (JobId/of project job)
      (JobId/of job))
    (JobId/of)))

(defn to-edn [^JobId arg]
  {:post [(global/strict! :gcp/bigquery.JobId %)]}
  (cond->
    {:job     (.getJob arg)
     :project (.getProject arg)}
    (some? (.getLocation arg))
    (assoc :location (.getLocation arg))))