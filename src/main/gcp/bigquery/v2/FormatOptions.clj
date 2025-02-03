(ns gcp.bigquery.v2.FormatOptions
  (:import [com.google.cloud.bigquery FormatOptions])
  (:require [gcp.bigquery.v2.AvroOptions :as AvroOptions]
            [gcp.bigquery.v2.CsvOptions :as CsvOptions]
            [gcp.global :as g]))

(defn ^FormatOptions from-edn [arg] (throw (Exception. "unimplemented")))

(defn to-edn [^FormatOptions arg] (throw (Exception. "unimplemented")))