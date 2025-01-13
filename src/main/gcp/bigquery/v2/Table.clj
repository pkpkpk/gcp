(ns gcp.bigquery.v2.Table
  (:require [gcp.global :as global]
            [gcp.bigquery.v2.TableInfo :as TableInfo])
  (:import (com.google.cloud.bigquery Table)))

; .copy()
; .delete()
; .exists()
; .extract()
; .insert()
; .list()
; .load()
; .update()
; .reload()

(defn ^Table from-edn [arg] (throw (Exception. "unimplemented")))

(defn to-edn [^Table arg]
  {:post [(global/strict! :bigquery/Table %)]}
  (assoc (TableInfo/to-edn arg) :bigquery (.getBigQuery arg)))