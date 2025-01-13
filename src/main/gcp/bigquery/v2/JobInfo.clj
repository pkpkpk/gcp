(ns gcp.bigquery.v2.JobInfo
  (:require [gcp.global :as global]
            [gcp.bigquery.v2.JobConfiguration :as JobConfiguration]
            [gcp.bigquery.v2.JobId :as JobId])
  (:import [com.google.cloud.bigquery JobInfo JobInfo$Builder]))

(defn ^JobInfo from-edn
  [{:keys [configuration jobId] :as arg}]
  (global/strict! :bigquery/JobInfo arg)
  (let [builder (JobInfo/newBuilder (JobConfiguration/from-edn configuration))]
    (when jobId
      (.setJobId builder (JobId/from-edn jobId)))
    (.build builder)))

(defn to-edn [^JobInfo arg]
  {:post [(global/strict! :bigquery/JobInfo %)]}
  {:jobId (.getJobId arg)
   :configuration (JobConfiguration/to-edn (.getConfiguration arg))})
