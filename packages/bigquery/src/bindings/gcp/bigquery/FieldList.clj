;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.FieldList
  {:doc
     "Google BigQuery Table schema fields (columns). Each field has a unique name and index. Fields\nwith duplicate names are not allowed in BigQuery schema."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.FieldList"
   :gcp.dev/certification
     {:base-seed 1775130858813
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1775130858813 :standard 1775130858814 :stress 1775130858815}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:54:20.853695089Z"}}
  (:require [gcp.bigquery.custom :as custom]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery FieldList]))

(declare from-edn to-edn)

(defn ^FieldList from-edn
  [arg]
  (global/strict! :gcp.bigquery/FieldList arg)
  (FieldList/of (map custom/Field-from-edn arg)))

(defn to-edn
  [^FieldList arg]
  {:post [(global/strict! :gcp.bigquery/FieldList %)]}
  (when arg (map custom/Field-to-edn arg)))

(def schema
  [:sequential
   {:closed true,
    :doc
      "Google BigQuery Table schema fields (columns). Each field has a unique name and index. Fields\nwith duplicate names are not allowed in BigQuery schema.",
    :gcp/category :collection-wrapper,
    :gcp/key :gcp.bigquery/FieldList} :gcp.bigquery/Field])

(global/include-schema-registry! (with-meta {:gcp.bigquery/FieldList schema}
                                   {:gcp.global/name "gcp.bigquery.FieldList"}))