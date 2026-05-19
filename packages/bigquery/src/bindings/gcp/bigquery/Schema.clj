;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.Schema
  {:doc
     "This class represents the schema for a Google BigQuery Table or data source."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.Schema"
   :gcp.dev/certification
     {:base-seed 1779204642651
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1779204642651 :standard 1779204642652 :stress 1779204642653}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-05-19T15:30:43.952600136Z"}}
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