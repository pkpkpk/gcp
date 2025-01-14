(ns gcp.bigquery.v2.JobInfo
  (:require [gcp.bigquery.v2.JobConfiguration :as JobConfiguration]
            [gcp.bigquery.v2.JobId :as JobId]
            [gcp.bigquery.v2.JobStatistics :as JobStatistics]
            [gcp.bigquery.v2.JobStatus :as JobStatus]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery JobInfo]))

(defn ^JobInfo from-edn
  [{:keys [configuration jobId] :as arg}]
  (global/strict! :bigquery/JobInfo arg)
  (let [builder (JobInfo/newBuilder (JobConfiguration/from-edn configuration))]
    (when jobId
      (.setJobId builder (JobId/from-edn jobId)))
    (.build builder)))

(defn to-edn [^JobInfo arg]
  {:post [(global/strict! :bigquery/JobInfo %)]}
  (cond->
    {:configuration (JobConfiguration/to-edn (.getConfiguration arg))
     :statistics    (JobStatistics/to-edn (.getStatistics arg))
     :generatedId   (.getGeneratedId arg)
     :jobId         (JobId/to-edn (.getJobId arg))
     :status        (JobStatus/to-edn (.getStatus arg))
     :userEmail     (.getUserEmail arg)}
    (some? (.getEtag arg))
    (assoc :etag (.getEtag arg))))
