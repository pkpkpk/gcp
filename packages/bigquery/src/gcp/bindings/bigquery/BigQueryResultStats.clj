;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.BigQueryResultStats
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.BigQueryResultStats"
   :gcp.dev/certification
     {:base-seed 0
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :protocol-hash
        "f4effb663e7e6af6dfc9051b90dfeb17820045df78c3901c15d91ac97dbdc861"
      :reason :read-only
      :skipped true
      :timestamp "2026-03-03T00:19:20.890155237Z"}}
  (:require [gcp.bigquery.custom.JobStatistics :as JobStatistics]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery BigQueryResultStats]))

(declare from-edn to-edn)

(defn ^BigQueryResultStats from-edn
  [arg]
  (throw (Exception.
           "Class com.google.cloud.bigquery.BigQueryResultStats is read-only")))

(defn to-edn
  [^BigQueryResultStats arg]
  {:post [(global/strict! :gcp.bindings.bigquery/BigQueryResultStats %)]}
  (cond-> {}
    (.getQueryStatistics arg) (assoc :queryStatistics
                                (JobStatistics/QueryStatistics-to-edn
                                  (.getQueryStatistics arg)))
    (.getSessionInfo arg) (assoc :sessionInfo
                            (JobStatistics/SessionInfo-to-edn (.getSessionInfo
                                                                arg)))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :interface,
    :gcp/key :gcp.bindings.bigquery/BigQueryResultStats}
   [:queryStatistics
    {:read-only? true, :doc "Returns query statistics of a query job"}
    :gcp.bigquery.custom/JobStatistics.QueryStatistics]
   [:sessionInfo
    {:read-only? true,
     :doc
       "Returns SessionInfo contains information about the session if this job is part of one.\nJobStatistics2 model class does not allow setSessionInfo so this cannot be set as part of\nQueryStatistics when we use jobs.query API."}
    :gcp.bigquery.custom/JobStatistics.SessionInfo]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/BigQueryResultStats schema}
    {:gcp.global/name "gcp.bindings.bigquery.BigQueryResultStats"}))