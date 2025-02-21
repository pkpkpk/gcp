(ns gcp.bigquery.v2.JobId
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery JobId)))

(defn ^JobId from-edn
  [{:keys [job project location] :as arg}]
  (global/strict! :gcp/bigquery.JobId arg)
  (let [builder (JobId/newBuilder)]
    (if job
      (.setJob builder job)
      (.setRandomJob builder))
    (when project
      (.setProject builder project))
    (when location
      (.setLocation builder location))
    (.build builder)))

(defn to-edn [^JobId arg]
  {:post [(global/strict! :gcp/bigquery.JobId %)]}
  (cond->
    {:job     (.getJob arg)
     :project (.getProject arg)}
    (some? (.getLocation arg))
    (assoc :location (.getLocation arg))))