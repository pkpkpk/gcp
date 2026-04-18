;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.Schema
  {:doc
     "This class represents the schema for a Google BigQuery Table or data source."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.Schema"
   :gcp.dev/certification
     {:base-seed 1776499350459
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1776499350459 :standard 1776499350460 :stress 1776499350461}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:02:32.173264703Z"}}
  (:require [gcp.bigquery.FieldList :as FieldList]
            [gcp.bigquery.custom :as custom]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery Schema]))

(declare from-edn to-edn)

(defn ^Schema from-edn
  [arg]
  (global/strict! :gcp.bigquery/Schema arg)
  (if (gcp.global/valid? [:sequential :gcp.bigquery/Field] arg)
    (Schema/of (mapv custom/Field-from-edn arg))
    (Schema/of (mapv custom/Field-from-edn (get arg :fields)))))

(defn to-edn
  [^Schema arg]
  {:post [(global/strict! :gcp.bigquery/Schema %)]}
  (when arg {:fields (FieldList/to-edn (.getFields arg))}))

(def schema
  [:or
   {:closed true,
    :doc
      "This class represents the schema for a Google BigQuery Table or data source.",
    :gcp/category :static-factory,
    :gcp/key :gcp.bigquery/Schema} :gcp.bigquery/FieldList
   [:map {:closed true} [:fields :gcp.bigquery/FieldList]]])

(global/include-schema-registry! (with-meta {:gcp.bigquery/Schema schema}
                                   {:gcp.global/name "gcp.bigquery.Schema"}))