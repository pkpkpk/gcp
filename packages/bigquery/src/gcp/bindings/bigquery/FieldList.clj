;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.FieldList
  {:doc
     "Google BigQuery Table schema fields (columns). Each field has a unique name and index. Fields\nwith duplicate names are not allowed in BigQuery schema."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.FieldList"
   :gcp.dev/certification
     {:base-seed 1771538768479
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771538768479 :standard 1771538768480 :stress 1771538768481}
      :protocol-hash
        "fd7b5d18406f6daf27bb5e8bee0b70247e385349b8babf0da967700049550cbc"
      :timestamp "2026-02-19T22:06:43.570475534Z"}}
  (:require [gcp.bigquery.custom :as custom]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery FieldList]))

(defn ^FieldList from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/FieldList arg)
  (FieldList/of (map custom/Field-from-edn arg)))

(defn to-edn
  [^FieldList arg]
  {:post [(global/strict! :gcp.bindings.bigquery/FieldList %)]}
  (map custom/Field-to-edn arg))

(def schema
  [:sequential
   {:closed true,
    :doc
      "Google BigQuery Table schema fields (columns). Each field has a unique name and index. Fields\nwith duplicate names are not allowed in BigQuery schema.",
    :gcp/category :collection-wrapper,
    :gcp/key :gcp.bindings.bigquery/FieldList} :gcp.bigquery.custom/Field])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/FieldList schema}
    {:gcp.global/name "gcp.bindings.bigquery.FieldList"}))