;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.TimelineSample
  {:doc
     "A specific timeline sample. This instruments work progress at a given point in time, providing\ninformation about work units active/pending/completed as well as cumulative slot-milliseconds."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.TimelineSample"
   :gcp.dev/certification
     {:base-seed 0
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :protocol-hash
        "62616b045d3dd853f6e527d31a44a851f587c87ad57ad3f2927b4519e248d6c9"
      :reason :read-only
      :skipped true
      :timestamp "2026-02-25T16:23:03.469321121Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery TimelineSample TimelineSample$Builder]))

(defn ^TimelineSample from-edn
  [arg]
  (throw (Exception.
           "Class com.google.cloud.bigquery.TimelineSample is read-only")))

(defn to-edn
  [^TimelineSample arg]
  {:post [(global/strict! :gcp.bindings.bigquery/TimelineSample %)]}
  (cond-> {}
    (.getActiveUnits arg) (assoc :activeUnits (.getActiveUnits arg))
    (.getCompletedUnits arg) (assoc :completedUnits (.getCompletedUnits arg))
    (.getElapsedMs arg) (assoc :elapsedMs (.getElapsedMs arg))
    (.getPendingUnits arg) (assoc :pendingUnits (.getPendingUnits arg))
    (.getSlotMillis arg) (assoc :slotMillis (.getSlotMillis arg))))

(def schema
  [:map
   {:closed true,
    :doc
      "A specific timeline sample. This instruments work progress at a given point in time, providing\ninformation about work units active/pending/completed as well as cumulative slot-milliseconds.",
    :gcp/category :read-only,
    :gcp/key :gcp.bindings.bigquery/TimelineSample}
   [:activeUnits
    {:read-only? true,
     :doc "Returns the total number of work units currently being processed."}
    :int]
   [:completedUnits
    {:read-only? true,
     :doc "Returns the total number of work units completed by this query."}
    :int]
   [:elapsedMs
    {:read-only? true,
     :doc
       "Returns the sample time as milliseconds elapsed since the start of query execution."}
    :int]
   [:pendingUnits
    {:read-only? true,
     :doc
       "Returns the number of work units remaining for the currently active stages."}
    :int]
   [:slotMillis
    {:read-only? true,
     :doc "Returns the cumulative slot-milliseconds consumed by the query."}
    :int]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/TimelineSample schema}
    {:gcp.global/name "gcp.bindings.bigquery.TimelineSample"}))