(ns gcp.bigquery.v2.Table
  (:require [gcp.bigquery.v2.BigQuery]
            [gcp.bigquery.v2.TableInfo :as TableInfo]
            [gcp.global :as global])
  (:import (com.google.cloud.bigquery Table)))

(defn to-edn [^Table arg]
  (when arg
    (assoc (TableInfo/to-edn arg) :bigquery (.getBigQuery arg))))

(def schemas
  {:gcp.bigquery.v2/Table
   [:and
    :gcp.bigquery.v2/TableInfo
    [:map [:bigquery :gcp.bigquery.v2/BigQuery]]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))