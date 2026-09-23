;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.FieldList
  {:doc
     "Google BigQuery Table schema fields (columns). Each field has a unique name and index. Fields\nwith duplicate names are not allowed in BigQuery schema."
   :file-git-sha "18dc14519e12f458a2a3cb5fe34f5e14c16e1379"
   :fqcn "com.google.cloud.bigquery.FieldList"
   :gcp.dev/certification
     {:base-seed 1790034058931
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034058931 :standard 1790034058932 :stress 1790034058933}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:41:00.565648302Z"}}
  (:require [gcp.bigquery.custom :as custom]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery FieldList]))

(declare from-edn to-edn)

(defn ^FieldList from-edn
  [arg]
  (global/strict! :gcp.bigquery/FieldList arg)
  (FieldList/of (mapv custom/Field-from-edn arg)))

(defn to-edn
  [^FieldList arg]
  {:post [(global/strict! :gcp.bigquery/FieldList %)]}
  (when arg (mapv custom/Field-to-edn arg)))

(def schema
  [:sequential
   {:closed true,
    :doc
      "Google BigQuery Table schema fields (columns). Each field has a unique name and index. Fields\nwith duplicate names are not allowed in BigQuery schema.",
    :gcp/category :collection-wrapper,
    :gcp/key :gcp.bigquery/FieldList} :gcp.bigquery/Field])

(global/include-registry! "gcp.bigquery.FieldList"
                          {:gcp.bigquery/FieldList schema})