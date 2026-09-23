;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.JobConfiguration
  {:doc "Base class for a BigQuery job configuration."
   :file-git-sha "36af39e222ccf992d15ddbf82148b98e377b40f3"
   :fqcn "com.google.cloud.bigquery.JobConfiguration"
   :gcp.dev/certification
     {:base-seed 1790034113969
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034113969 :standard 1790034113970 :stress 1790034113971}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:42:08.071419969Z"}}
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

(global/include-registry! "gcp.bigquery.JobConfiguration"
                          {:gcp.bigquery/JobConfiguration schema})