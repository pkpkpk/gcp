;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.DmlStats
  {:doc "Represents DML statistics information."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.DmlStats"
   :gcp.dev/certification
     {:base-seed 1772045859578
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1772045859578 :standard 1772045859579 :stress 1772045859580}
      :protocol-hash
        "b8a5eb17212acb18f49dcb7f15a243eac9b32f54c0f054a99be660a3a25e0315"
      :timestamp "2026-02-25T18:57:39.584809031Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery DmlStats DmlStats$Builder]))

(defn ^DmlStats from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/DmlStats arg)
  (let [builder (DmlStats/newBuilder)]
    (when (some? (get arg :deletedRowCount))
      (.setDeletedRowCount builder (get arg :deletedRowCount)))
    (when (some? (get arg :insertedRowCount))
      (.setInsertedRowCount builder (get arg :insertedRowCount)))
    (when (some? (get arg :updatedRowCount))
      (.setUpdatedRowCount builder (get arg :updatedRowCount)))
    (.build builder)))

(defn to-edn
  [^DmlStats arg]
  {:post [(global/strict! :gcp.bindings.bigquery/DmlStats %)]}
  (cond-> {}
    (.getDeletedRowCount arg) (assoc :deletedRowCount (.getDeletedRowCount arg))
    (.getInsertedRowCount arg) (assoc :insertedRowCount
                                 (.getInsertedRowCount arg))
    (.getUpdatedRowCount arg) (assoc :updatedRowCount
                                (.getUpdatedRowCount arg))))

(def schema
  [:map
   {:closed true,
    :doc "Represents DML statistics information.",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/DmlStats}
   [:deletedRowCount
    {:optional true,
     :getter-doc
       "Returns number of deleted Rows. populated by DML DELETE, MERGE and TRUNCATE statements.\n\n@return value or {@code null} for none",
     :setter-doc
       "Number of deleted Rows. populated by DML DELETE, MERGE and TRUNCATE statements.\n\n@param deletedRowCount deletedRowCount or {@code null} for none"}
    :int]
   [:insertedRowCount
    {:optional true,
     :getter-doc
       "Returns number of inserted Rows. Populated by DML INSERT and MERGE statements.\n\n@return value or {@code null} for none",
     :setter-doc
       "Number of inserted Rows. Populated by DML INSERT and MERGE statements.\n\n@param insertedRowCount insertedRowCount or {@code null} for none"}
    :int]
   [:updatedRowCount
    {:optional true,
     :getter-doc
       "Returns number of updated Rows. Populated by DML UPDATE and MERGE statements.\n\n@return value or {@code null} for none",
     :setter-doc
       "Number of updated Rows. Populated by DML UPDATE and MERGE statements.\n\n@param updatedRowCount updatedRowCount or {@code null} for none"}
    :int]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/DmlStats schema}
    {:gcp.global/name "gcp.bindings.bigquery.DmlStats"}))