;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.TimelineSample
  {:doc
     "A specific timeline sample. This instruments work progress at a given point in time, providing\ninformation about work units active/pending/completed as well as cumulative slot-milliseconds."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.TimelineSample"
   :gcp.dev/certification
     {:base-seed 0
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :reason :read-only
      :skipped true
      :timestamp "2026-04-09T22:56:40.295399681Z"}}
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

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/TimelineSample schema}
    {:gcp.global/name "gcp.bigquery.TimelineSample"}))