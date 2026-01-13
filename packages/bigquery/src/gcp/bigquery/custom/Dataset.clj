(ns gcp.bigquery.custom.Dataset
  (:require [gcp.global :as global]
            [gcp.bigquery.v2.BigQuery]
            [gcp.bigquery.v2.DatasetInfo :as DatasetInfo])
  (:import (com.google.cloud.bigquery Dataset)))

(defn to-edn [^Dataset arg]
  (when arg
    (assoc (DatasetInfo/to-edn arg) :bigquery (.getBigQuery arg))))

(def schemas
  {:gcp.bigquery/Dataset
   [:and
    :gcp.bigquery/DatasetInfo
    [:map [:bigquery :gcp.bigquery/BigQuery]]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))