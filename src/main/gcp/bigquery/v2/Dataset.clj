(ns gcp.bigquery.v2.Dataset
  (:refer-clojure :exclude [get])
  (:require [clojure.string :as string]
            [gcp.bigquery.v2.Acl :as Acl]
            [gcp.bigquery.v2.BigQuery.DatasetOption :as DatasetOption]
            [gcp.bigquery.v2.DatasetId :as DatasetId]
            [gcp.bigquery.v2.DatasetInfo :as DatasetInfo]
            [gcp.bigquery.v2.EncryptionConfiguration :as EncryptionConfiguration]
            [gcp.bigquery.v2.ExternalDatasetReference :as ExternalDatasetReference]
            [gcp.global :as global])
  (:import (com.google.cloud.bigquery Dataset)))

;https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery.Dataset
;https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery.DatasetInfo.Builder

;; Dataset == DatasetInfo (immut record) + client + w/ IO & mutation methods
; Dataset.create(<tableDef>)    ->   sugared bigquery.create(TableInfo)
; Dataset.delete(<tableId>)     ->   sugared bigquery.delete(TableInfo)
; Dataset.list()                ->   sugared bigquery.listTables(datasetId)
; Dataset.reload()              ->   sugared bigquery.get(datasetId)
; Dataset.update()              ->   sugared bigquery

(defn ^Dataset from-edn [arg] (throw (Exception. "unimplemented")))

(defn to-edn [^Dataset arg]
  {:post [(global/strict! :bigquery/Dataset %)]}
  (assoc (DatasetInfo/to-edn arg) :bigquery (.getBigQuery arg)))