;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.Schema
  {:doc
     "This class represents the schema for a Google BigQuery Table or data source."
   :file-git-sha "a0d873f9ec51e8614c61c1f5caf20eadd67aa294"
   :fqcn "com.google.cloud.bigquery.Schema"
   :gcp.dev/certification
     {:base-seed 1790034060880
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034060880 :standard 1790034060881 :stress 1790034060882}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:41:02.521861926Z"}}
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

(global/include-registry! "gcp.bigquery.Schema" {:gcp.bigquery/Schema schema})