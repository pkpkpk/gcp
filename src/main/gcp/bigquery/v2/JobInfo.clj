(ns gcp.bigquery.v2.JobInfo
  (:require [gcp.global :as global]
            [gcp.bigquery.v2.JobId :as JobId])
  (:import [com.google.cloud.bigquery JobInfo JobInfo$Builder]))

(defn ^JobInfo from-edn
  [{:keys [configuration jobId] :as arg}]
  (global/strict! :bigquery/JobInfo arg)
  (let [builder (JobInfo$Builder.)]
    (if (global/valid? :bigquery/CopyJobConfiguration configuration)
      (.setConfiguration builder configuration)
      (throw (Exception. "unimplemented")))
    (when jobId
      (.setJobId builder (JobId/from-edn jobId)))
    (.build builder)))
