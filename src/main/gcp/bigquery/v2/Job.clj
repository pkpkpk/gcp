(ns gcp.bigquery.v2.Job
  (:refer-clojure :exclude [get])
  (:require [gcp.global :as global]
            [gcp.bigquery.v2.JobInfo :as JobInfo]
            [gcp.bigquery.v2.BigQuery.JobOption :as JobOption])
  (:import [com.google.cloud.bigquery Job]))

(defn to-edn [^Job arg] (throw (Exception. "unimplemented")))