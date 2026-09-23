;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.TimelineSample
  {:doc
     "A specific timeline sample. This instruments work progress at a given point in time, providing\ninformation about work units active/pending/completed as well as cumulative slot-milliseconds."
   :file-git-sha "36af39e222ccf992d15ddbf82148b98e377b40f3"
   :fqcn "com.google.cloud.bigquery.TimelineSample"
   :gcp.dev/certification
     {:base-seed 0
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :reason :read-only
      :skipped true
      :timestamp "2026-09-21T23:42:20.289592476Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery TimelineSample TimelineSample$Builder]))

(declare from-edn to-edn)

(defn ^TimelineSample from-edn
  [arg]
  (throw (Exception.
           "Class com.google.cloud.bigquery.TimelineSample is read-only")))

(defn to-edn
  [^TimelineSample arg]
  {:post [(global/strict! :gcp.bigquery/TimelineSample %)]}
  (when arg
    (cond-> {}
      (.getActiveUnits arg) (assoc :activeUnits (.getActiveUnits arg))
      (.getCompletedUnits arg) (assoc :completedUnits (.getCompletedUnits arg))
      (.getElapsedMs arg) (assoc :elapsedMs (.getElapsedMs arg))
      (.getPendingUnits arg) (assoc :pendingUnits (.getPendingUnits arg))
      (.getSlotMillis arg) (assoc :slotMillis (.getSlotMillis arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "A specific timeline sample. This instruments work progress at a given point in time, providing\ninformation about work units active/pending/completed as well as cumulative slot-milliseconds.",
    :gcp/category :read-only,
    :gcp/key :gcp.bigquery/TimelineSample}
   [:activeUnits
    {:read-only? true,
     :optional true,
     :doc "Returns the total number of work units currently being processed."}
    :i64]
   [:completedUnits
    {:read-only? true,
     :optional true,
     :doc "Returns the total number of work units completed by this query."}
    :i64]
   [:elapsedMs
    {:read-only? true,
     :optional true,
     :doc
       "Returns the sample time as milliseconds elapsed since the start of query execution."}
    :i64]
   [:pendingUnits
    {:read-only? true,
     :optional true,
     :doc
       "Returns the number of work units remaining for the currently active stages."}
    :i64]
   [:slotMillis
    {:read-only? true,
     :optional true,
     :doc "Returns the cumulative slot-milliseconds consumed by the query."}
    :i64]])

(global/include-registry! "gcp.bigquery.TimelineSample"
                          {:gcp.bigquery/TimelineSample schema})