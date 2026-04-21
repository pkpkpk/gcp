(ns gcp.bigquery.custom.Job
  (:require
   [gcp.bigquery.JobInfo :as JobInfo]
   [gcp.global :as g]
   [malli.util :as mu])
  (:import
   (com.google.cloud.bigquery Job)))

(defn to-edn [^Job arg]
  (when arg
    (assoc (JobInfo/to-edn arg) :bigquery (.getBigQuery arg))))

(def schema
  (mu/merge
    JobInfo/schema
    [:map [:bigquery {:optional true} :any]]
    (g/mopts)))

(g/include-schema-registry! (with-meta {:gcp.bigquery/Job schema}
                                       {:gcp.global/name "gcp.bigquery.Job"}))
