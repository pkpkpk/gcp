(ns gcp.bigquery.custom.Model
  (:require [gcp.bigquery.v2.BigQuery]
            [gcp.bigquery.v2.ModelInfo :as ModelInfo]
            [gcp.global :as global])
  (:import (com.google.cloud.bigquery Model)))

(defn to-edn [^Model arg]
  (when arg
    (assoc (ModelInfo/to-edn arg) :bigquery (.getBigQuery arg))))

(def schemas
  {:gcp.bigquery/Model
   [:and
    :gcp.bigquery/ModelInfo
    [:map [:bigquery :gcp.bigquery/BigQuery]]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))