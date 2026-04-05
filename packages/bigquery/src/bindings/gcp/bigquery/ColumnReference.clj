;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.ColumnReference
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.ColumnReference"
   :gcp.dev/certification
     {:base-seed 1775130899792
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1775130899792 :standard 1775130899793 :stress 1775130899794}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:55:01.123927985Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery ColumnReference ColumnReference$Builder]))

(declare from-edn to-edn)

(defn ^ColumnReference from-edn
  [arg]
  (global/strict! :gcp.bigquery/ColumnReference arg)
  (let [builder (ColumnReference/newBuilder)]
    (when (some? (get arg :referencedColumn))
      (.setReferencedColumn builder (get arg :referencedColumn)))
    (when (some? (get arg :referencingColumn))
      (.setReferencingColumn builder (get arg :referencingColumn)))
    (.build builder)))

(defn to-edn
  [^ColumnReference arg]
  {:post [(global/strict! :gcp.bigquery/ColumnReference %)]}
  (when arg
    (cond-> {}
      (some->> (.getReferencedColumn arg)
               (not= ""))
        (assoc :referencedColumn (.getReferencedColumn arg))
      (some->> (.getReferencingColumn arg)
               (not= ""))
        (assoc :referencingColumn (.getReferencingColumn arg)))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/ColumnReference}
   [:referencedColumn
    {:optional true, :setter-doc "The target column of this reference. *"}
    [:string {:min 1}]]
   [:referencingColumn
    {:optional true, :setter-doc "The source column of this reference. *"}
    [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/ColumnReference schema}
    {:gcp.global/name "gcp.bigquery.ColumnReference"}))