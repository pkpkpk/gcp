(ns gcp.bigquery.v2.Job
  (:refer-clojure :exclude [get])
  (:require [gcp.global :as global]
            [gcp.bigquery.v2.JobInfo :as JobInfo])
  (:import [com.google.cloud.bigquery Job]))

(defn ^Job from-edn [arg] (throw (Exception. "unimplemented")))

(defn to-edn [^Job arg]
  (when arg
    (assoc (JobInfo/to-edn arg) :bigquery (.getBigQuery arg))))