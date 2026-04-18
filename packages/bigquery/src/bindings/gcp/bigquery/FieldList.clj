;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.FieldList
  {:doc
     "Google BigQuery Table schema fields (columns). Each field has a unique name and index. Fields\nwith duplicate names are not allowed in BigQuery schema."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.FieldList"
   :gcp.dev/certification
     {:base-seed 1776499348420
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1776499348420 :standard 1776499348421 :stress 1776499348422}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:02:30.183934245Z"}}
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