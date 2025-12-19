(ns gcp.bigquery.v2.TimePartitioning
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery TimePartitioning)))

(defn ^TimePartitioning from-edn [arg] (throw (Exception. "unimplemented")))

(defn to-edn [^TimePartitioning arg] (throw (Exception. "unimplemented")))

(def schemas
  {:gcp.bigquery.v2/TimePartitioning
   [:map {:closed true}
    [:expirationMs :int]
    [:field :string]
    [:requiredPartitionFilter :boolean]]

   :gcp.bigquery.v2/TimePartitioning.Type [:enum "MONTH" "YEAR" "HOUR" "DAY"]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))