;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.DmlStats
  {:doc "Represents DML statistics information."
   :file-git-sha "19b3f01f6e68bb1a5f53ccb18989e9db8c28a475"
   :fqcn "com.google.cloud.bigquery.DmlStats"
   :gcp.dev/certification
     {:base-seed 1790034135868
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034135868 :standard 1790034135869 :stress 1790034135870}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:42:16.937273460Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery DmlStats DmlStats$Builder]))

(declare from-edn to-edn)

(defn ^DmlStats from-edn
  [arg]
  (global/strict! :gcp.bigquery/DmlStats arg)
  (let [builder (DmlStats/newBuilder)]
    (when (some? (get arg :deletedRowCount))
      (.setDeletedRowCount builder (long (get arg :deletedRowCount))))
    (when (some? (get arg :insertedRowCount))
      (.setInsertedRowCount builder (long (get arg :insertedRowCount))))
    (when (some? (get arg :updatedRowCount))
      (.setUpdatedRowCount builder (long (get arg :updatedRowCount))))
    (.build builder)))

(defn to-edn
  [^DmlStats arg]
  {:post [(global/strict! :gcp.bigquery/DmlStats %)]}
  (when arg
    (cond-> {}
      (.getDeletedRowCount arg) (assoc :deletedRowCount
                                  (.getDeletedRowCount arg))
      (.getInsertedRowCount arg) (assoc :insertedRowCount
                                   (.getInsertedRowCount arg))
      (.getUpdatedRowCount arg) (assoc :updatedRowCount
                                  (.getUpdatedRowCount arg)))))

(def schema
  [:map
   {:closed true,
    :doc "Represents DML statistics information.",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/DmlStats}
   [:deletedRowCount
    {:optional true,
     :getter-doc
       "Returns number of deleted Rows. populated by DML DELETE, MERGE and TRUNCATE statements.\n\n@return value or {@code null} for none",
     :setter-doc
       "Number of deleted Rows. populated by DML DELETE, MERGE and TRUNCATE statements.\n\n@param deletedRowCount deletedRowCount or {@code null} for none"}
    :i64]
   [:insertedRowCount
    {:optional true,
     :getter-doc
       "Returns number of inserted Rows. Populated by DML INSERT and MERGE statements.\n\n@return value or {@code null} for none",
     :setter-doc
       "Number of inserted Rows. Populated by DML INSERT and MERGE statements.\n\n@param insertedRowCount insertedRowCount or {@code null} for none"}
    :i64]
   [:updatedRowCount
    {:optional true,
     :getter-doc
       "Returns number of updated Rows. Populated by DML UPDATE and MERGE statements.\n\n@return value or {@code null} for none",
     :setter-doc
       "Number of updated Rows. Populated by DML UPDATE and MERGE statements.\n\n@param updatedRowCount updatedRowCount or {@code null} for none"}
    :i64]])

(global/include-registry! "gcp.bigquery.DmlStats"
                          {:gcp.bigquery/DmlStats schema})