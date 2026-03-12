;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.JobConfiguration
  {:doc "Base class for a BigQuery job configuration."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.JobConfiguration"
   :gcp.dev/certification
     {:base-seed 1771685332861
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771685332861 :standard 1771685332862 :stress 1771685332863}
      :protocol-hash
        "cfb48d33a788d76da9372b104b81cdecf53036c75e13fa6ff2addb776e0fbbab"
      :timestamp "2026-02-21T14:51:14.786846151Z"}}
  (:require
    [gcp.bigquery.custom.QueryJobConfiguration :as QueryJobConfiguration]
    [gcp.bindings.bigquery.CopyJobConfiguration :as CopyJobConfiguration]
    [gcp.bindings.bigquery.ExtractJobConfiguration :as ExtractJobConfiguration]
    [gcp.bindings.bigquery.LoadJobConfiguration :as LoadJobConfiguration]
    [gcp.global :as global])
  (:import [com.google.cloud.bigquery JobConfiguration]))

(defn ^JobConfiguration from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/JobConfiguration arg)
  (case (get arg :type)
    "QUERY" (QueryJobConfiguration/from-edn arg)
    "LOAD" (LoadJobConfiguration/from-edn arg)
    "COPY" (CopyJobConfiguration/from-edn arg)
    "EXTRACT" (ExtractJobConfiguration/from-edn arg)))

(defn to-edn
  [^JobConfiguration arg]
  {:post [(global/strict! :gcp.bindings.bigquery/JobConfiguration %)]}
  (case (.name (.getType arg))
    "QUERY" (QueryJobConfiguration/to-edn arg)
    "LOAD" (LoadJobConfiguration/to-edn arg)
    "COPY" (CopyJobConfiguration/to-edn arg)
    "EXTRACT" (ExtractJobConfiguration/to-edn arg)))

(def schema
  [:or
   {:closed true,
    :doc "Base class for a BigQuery job configuration.",
    :gcp/category :union-abstract,
    :gcp/key :gcp.bindings.bigquery/JobConfiguration}
   :gcp.bigquery.custom/QueryJobConfiguration
   :gcp.bindings.bigquery/LoadJobConfiguration
   :gcp.bindings.bigquery/CopyJobConfiguration
   :gcp.bindings.bigquery/ExtractJobConfiguration])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/JobConfiguration schema}
    {:gcp.global/name "gcp.bindings.bigquery.JobConfiguration"}))