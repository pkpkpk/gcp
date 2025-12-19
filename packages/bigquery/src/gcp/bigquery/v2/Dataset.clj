(ns gcp.bigquery.v2.Dataset
  (:require [gcp.global :as global]
            [gcp.bigquery.v2.BigQuery]
            [gcp.bigquery.v2.DatasetInfo :as DatasetInfo])
  (:import (com.google.cloud.bigquery Dataset)))

(defn to-edn [^Dataset arg]
  (when arg
    (assoc (DatasetInfo/to-edn arg) :bigquery (.getBigQuery arg))))

(def schemas
  {:gcp.bigquery.v2/Dataset
   [:and
    :gcp.bigquery.v2/DatasetInfo
    [:map [:bigquery :gcp.bigquery.v2/BigQuery]]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))