;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.JobConfiguration
  {:doc "Base class for a BigQuery job configuration."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.JobConfiguration"
   :gcp.dev/certification
     {:base-seed 1779204686239
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1779204686239 :standard 1779204686240 :stress 1779204686241}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-05-19T15:31:31.892548581Z"}}
  (:require [gcp.bigquery.CopyJobConfiguration :as CopyJobConfiguration]
            [gcp.bigquery.ExtractJobConfiguration :as ExtractJobConfiguration]
            [gcp.bigquery.LoadJobConfiguration :as LoadJobConfiguration]
            [gcp.bigquery.custom.QueryJobConfiguration :as
             QueryJobConfiguration]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery JobConfiguration]))

(declare from-edn to-edn)

(defn ^JobConfiguration from-edn
  [arg]
  (global/strict! :gcp.bigquery/JobConfiguration arg)
  (case (get arg :type)
    "QUERY" (QueryJobConfiguration/from-edn arg)
    "LOAD" (LoadJobConfiguration/from-edn arg)
    "COPY" (CopyJobConfiguration/from-edn arg)
    "EXTRACT" (ExtractJobConfiguration/from-edn arg)))

(defn to-edn
  [^JobConfiguration arg]
  {:post [(global/strict! :gcp.bigquery/JobConfiguration %)]}
  (when arg
    (case (.name (.getType arg))
      "QUERY" (QueryJobConfiguration/to-edn arg)
      "LOAD" (LoadJobConfiguration/to-edn arg)
      "COPY" (CopyJobConfiguration/to-edn arg)
      "EXTRACT" (ExtractJobConfiguration/to-edn arg))))

(def schema
  [:or
   {:closed true,
    :doc "Base class for a BigQuery job configuration.",
    :gcp/category :union-abstract,
    :gcp/key :gcp.bigquery/JobConfiguration} :gcp.bigquery/QueryJobConfiguration
   :gcp.bigquery/LoadJobConfiguration :gcp.bigquery/CopyJobConfiguration
   :gcp.bigquery/ExtractJobConfiguration])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/JobConfiguration schema}
    {:gcp.global/name "gcp.bigquery.JobConfiguration"}))