(ns gcp.bigquery.v2.AvroOptions
  (:import [com.google.cloud.bigquery AvroOptions])
  (:require [gcp.global :as g]))

(defn ^AvroOptions from-edn [arg] (throw (Exception. "unimplemented")))

(defn to-edn [^AvroOptions arg] (throw (Exception. "unimplemented")))