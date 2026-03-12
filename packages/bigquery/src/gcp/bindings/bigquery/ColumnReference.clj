;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.ColumnReference
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.ColumnReference"
   :gcp.dev/certification
     {:base-seed 1771291738495
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771291738495 :standard 1771291738496 :stress 1771291738497}
      :protocol-hash
        "7068af39aa0d55cb4d0e4eaceead6fd12f374863b361a9717f08a69d4bd12910"
      :timestamp "2026-02-17T01:28:58.509359224Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery ColumnReference ColumnReference$Builder]))

(defn ^ColumnReference from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/ColumnReference arg)
  (let [builder (ColumnReference/newBuilder)]
    (when (some? (get arg :referencedColumn))
      (.setReferencedColumn builder (get arg :referencedColumn)))
    (when (some? (get arg :referencingColumn))
      (.setReferencingColumn builder (get arg :referencingColumn)))
    (.build builder)))

(defn to-edn
  [^ColumnReference arg]
  {:post [(global/strict! :gcp.bindings.bigquery/ColumnReference %)]}
  (cond-> {}
    (.getReferencedColumn arg) (assoc :referencedColumn
                                 (.getReferencedColumn arg))
    (.getReferencingColumn arg) (assoc :referencingColumn
                                  (.getReferencingColumn arg))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/ColumnReference}
   [:referencedColumn
    {:optional true, :setter-doc "The target column of this reference. *"}
    [:string {:min 1}]]
   [:referencingColumn
    {:optional true, :setter-doc "The source column of this reference. *"}
    [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/ColumnReference schema}
    {:gcp.global/name "gcp.bindings.bigquery.ColumnReference"}))