(ns gcp.bigquery.v2.Schema
  (:require [gcp.bigquery.v2.Field :as Field]
            [gcp.global :as global])
  (:import (com.google.cloud.bigquery Schema)))

(defn ^Schema from-edn
  [{fields :fields :as arg}]
  (global/strict! :gcp.bigquery.v2/Schema arg)
  (Schema/of (map Field/from-edn fields)))

(defn to-edn [^Schema arg]
  {:post [(global/strict! :gcp.bigquery.v2/Schema %)]}
  {:fields (mapv Field/to-edn (.getFields arg))})

(def schemas
  {:gcp.bigquery.v2/Schema
   [:map {:closed true}
    [:fields [:sequential :gcp.bigquery.v2/Field]]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))