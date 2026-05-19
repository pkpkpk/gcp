;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.FieldList
  {:doc
     "Google BigQuery Table schema fields (columns). Each field has a unique name and index. Fields\nwith duplicate names are not allowed in BigQuery schema."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.FieldList"
   :gcp.dev/certification
     {:base-seed 1779204640846
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1779204640846 :standard 1779204640847 :stress 1779204640848}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-05-19T15:30:42.391969389Z"}}
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

(global/include-schema-registry! (with-meta {:gcp.bigquery/FieldList schema}
                                   {:gcp.global/name "gcp.bigquery.FieldList"}))