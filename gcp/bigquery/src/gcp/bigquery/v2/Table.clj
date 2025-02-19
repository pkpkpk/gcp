(ns gcp.bigquery.v2.Table
  (:require [gcp.bigquery.v2.TableInfo :as TableInfo])
  (:import (com.google.cloud.bigquery Table)))

(defn to-edn [^Table arg]
  (when arg
    (assoc (TableInfo/to-edn arg) :bigquery (.getBigQuery arg))))