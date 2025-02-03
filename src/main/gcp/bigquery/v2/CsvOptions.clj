(ns gcp.bigquery.v2.CsvOptions
  (:import [com.google.cloud.bigquery CsvOptions])
  (:require [gcp.global :as g]))

(defn ^CsvOptions from-edn [arg] (throw (Exception. "unimplemented")))

(defn to-edn [^CsvOptions arg] (throw (Exception. "unimplemented")))