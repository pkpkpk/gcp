(ns gcp.bigquery.custom.Table
  (:require [gcp.bindings.bigquery.TableInfo :as TableInfo])
  (:import (com.google.cloud.bigquery Table)))

(defn to-edn [^Table arg]
  (when arg
    (assoc (TableInfo/to-edn arg) :bigquery (.getBigQuery arg))))

