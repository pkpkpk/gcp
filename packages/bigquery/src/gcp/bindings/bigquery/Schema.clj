;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.Schema
  {:doc
     "This class represents the schema for a Google BigQuery Table or data source."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.Schema"
   :gcp.dev/certification
     {:base-seed 1771541232376
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771541232376 :standard 1771541232377 :stress 1771541232378}
      :protocol-hash
        "252de90e8ca865a90882a3ce2a304a741fa00a1b258f1c1b4080703841429c33"
      :timestamp "2026-02-19T22:47:53.654613197Z"}}
  (:require [gcp.bigquery.custom :as custom]
            [gcp.bindings.bigquery.FieldList :as FieldList]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery Schema]))

(defn ^Schema from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/Schema arg)
  (if (gcp.global/valid? [:sequential :gcp.bigquery.custom/Field] arg)
    (Schema/of (map custom/Field-from-edn arg))
    (Schema/of (map custom/Field-from-edn (get arg :fields)))))

(defn to-edn
  [^Schema arg]
  {:post [(global/strict! :gcp.bindings.bigquery/Schema %)]}
  {:fields (FieldList/to-edn (.getFields arg))})

(def schema
  [:or
   {:closed true,
    :doc
      "This class represents the schema for a Google BigQuery Table or data source.",
    :gcp/category :static-factory,
    :gcp/key :gcp.bindings.bigquery/Schema} :gcp.bindings.bigquery/FieldList
   [:map {:closed true} [:fields :gcp.bindings.bigquery/FieldList]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/Schema schema}
    {:gcp.global/name "gcp.bindings.bigquery.Schema"}))