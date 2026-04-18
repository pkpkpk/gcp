;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.JobConfiguration
  {:doc "Base class for a BigQuery job configuration."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.JobConfiguration"
   :gcp.dev/certification
     {:base-seed 1776499416957
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1776499416957 :standard 1776499416958 :stress 1776499416959}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:03:41.734310875Z"}}
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