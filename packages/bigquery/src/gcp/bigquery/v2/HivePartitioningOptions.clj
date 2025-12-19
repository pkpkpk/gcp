(ns gcp.bigquery.v2.HivePartitioningOptions
  (:import [com.google.cloud.bigquery HivePartitioningOptions])
  (:require [gcp.global :as global]))

(defn ^HivePartitioningOptions from-edn
  [arg]
  (throw (Exception. "unimplemented")))

(defn to-edn
  [^HivePartitioningOptions arg]
  (throw (Exception. "unimplemented")))

(def schemas
  {:gcp.bigquery.v2/HivePartitioningOptions
   [:map {:closed true}
    [:fields {:optional false} [:sequential :string]]
    [:mode {:optional false} :string]
    [:requirePartitionFilter {:optional false} :boolean]
    [:sourceUriPrefix {:optional false} :string]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))