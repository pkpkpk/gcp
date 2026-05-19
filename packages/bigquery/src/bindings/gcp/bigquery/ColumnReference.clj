;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.ColumnReference
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.ColumnReference"
   :gcp.dev/certification
     {:base-seed 1779204669417
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1779204669417 :standard 1779204669418 :stress 1779204669419}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-05-19T15:31:10.245615048Z"}}
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
    [:string {:min 1, :gen/max 1}]]
   [:referencingColumn
    {:optional true, :setter-doc "The source column of this reference. *"}
    [:string {:min 1, :gen/max 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/ColumnReference schema}
    {:gcp.global/name "gcp.bigquery.ColumnReference"}))