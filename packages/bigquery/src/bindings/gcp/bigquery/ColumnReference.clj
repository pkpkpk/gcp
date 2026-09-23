;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.ColumnReference
  {:doc nil
   :file-git-sha "90b1e1a8b33e2f0ceedc9cdba1410c0f0053f7e8"
   :fqcn "com.google.cloud.bigquery.ColumnReference"
   :gcp.dev/certification
     {:base-seed 1790034093837
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034093837 :standard 1790034093838 :stress 1790034093839}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:41:34.872839263Z"}}
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

(global/include-registry! "gcp.bigquery.ColumnReference"
                          {:gcp.bigquery/ColumnReference schema})