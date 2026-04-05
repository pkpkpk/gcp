;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.DmlStats
  {:doc "Represents DML statistics information."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.DmlStats"
   :gcp.dev/certification
     {:base-seed 1775130941085
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1775130941085 :standard 1775130941086 :stress 1775130941087}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:55:42.293299549Z"}}
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

(global/include-schema-registry! (with-meta {:gcp.bigquery/DmlStats schema}
                                   {:gcp.global/name "gcp.bigquery.DmlStats"}))