(ns gcp.bigquery.custom.Job
  (:require [gcp.bigquery.v2.BigQuery]
            [gcp.bigquery.v2.JobInfo :as JobInfo]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery Job]))

(defn to-edn [^Job arg]
  (when arg
    (assoc (JobInfo/to-edn arg) :bigquery (.getBigQuery arg))))

(def schemas
  {:gcp.bigquery/Job
   [:and
    :gcp.bigquery/JobInfo
    [:map [:bigquery :gcp.bigquery/BigQuery]]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))