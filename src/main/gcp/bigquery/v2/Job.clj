(ns gcp.bigquery.v2.Job
  (:refer-clojure :exclude [get])
  (:require [gcp.global :as global]
            [gcp.bigquery.v2.JobInfo :as JobInfo]
            [gcp.bigquery.v2.BigQuery.JobOption :as JobOption])
  (:import [com.google.cloud.bigquery Job]))

(defn ^Job from-edn [arg] (throw (Exception. "unimplemented")))

(defn to-edn [^Job arg]
  {:post [(global/strict! :bigquery/Job %)]}
  (assoc (JobInfo/to-edn arg) :bigquery (.getBigQuery arg)))