(ns gcp.bigquery.v2.Schema
  (:require [gcp.bigquery.v2.Field :as Field]
            [gcp.global :as global])
  (:import (com.google.cloud.bigquery Schema)))

(defn ^Schema from-edn
  [{fields :fields :as arg}]
  (global/strict! :gcp/bigquery.Schema arg)
  (Schema/of (map Field/from-edn fields)))

(defn to-edn [^Schema arg]
  {:post [(global/strict! :gcp/bigquery.Schema %)]}
  {:fields (mapv Field/to-edn (.getFields arg))})