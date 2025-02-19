(ns gcp.bigquery.v2.BigQuery
  (:require [gcp.global :as g]
            [gcp.bigquery.v2.BigQueryRetryConfig :as BigQueryRetryConfig]
            [gcp.core.RetryOption :as RO])
  (:import (com.google.cloud RetryOption)
           (com.google.cloud.bigquery BigQuery$DatasetDeleteOption BigQuery$DatasetField BigQuery$DatasetListOption BigQuery$DatasetOption BigQuery$IAMOption BigQuery$JobField BigQuery$JobListOption BigQuery$JobOption BigQuery$ModelListOption BigQuery$ModelOption BigQuery$QueryOption BigQuery$QueryResultsOption BigQuery$RoutineListOption BigQuery$RoutineOption BigQuery$TableDataListOption BigQuery$TableField BigQuery$TableMetadataView BigQuery$TableListOption BigQuery$TableOption JobStatus$State)))

(defn ^BigQuery$DatasetDeleteOption DatasetDeleteOption-from-edn [arg]
  (g/strict! :gcp/bigquery.BigQuery.DatasetDeleteOption arg)
  (if (contains? arg :deleteContents)
    (BigQuery$DatasetDeleteOption/deleteContents)
    (throw (ex-info "bad arg to DatasetDeleteOption" {:arg arg}))))

(defn ^BigQuery$DatasetListOption DatasetListOption-from-edn
  [{:keys [labelFilter pageSize pageToken] :as arg}]
  (g/strict! :gcp/bigquery.BigQuery.DatasetListOption arg)
  (if (contains? arg :all)
    (BigQuery$DatasetListOption/all)
    (if labelFilter
      (BigQuery$DatasetListOption/labelFilter labelFilter)
      (if pageSize
        (BigQuery$DatasetListOption/pageSize pageSize)
        (BigQuery$DatasetListOption/pageToken pageToken)))))

(defn ^BigQuery$DatasetOption DatasetOption-from-edn
  [{:keys [fields] :as arg}]
  (g/strict! [:sequential :gcp/bigquery.BigQuery.DatasetField] arg)
  (let [fields (map #(BigQuery$DatasetField/valueOf %) fields)]
    (BigQuery$DatasetOption/fields (into-array BigQuery$DatasetField fields))))

(defn ^BigQuery$IAMOption IAMOption-from-edn
  [{:keys [version]}]
  (BigQuery$IAMOption/requestedPolicyVersion (long version)))

(defn ^BigQuery$JobListOption JobListOption-from-edn
  [{:keys [fields
           maxCreationTime
           minCreationTime
           pageSize
           pageToken
           parentJobId
           stateFilters] :as arg}]
  (g/strict! :gcp/bigquery.BigQuery.JobListOption arg)
  (cond
    fields
    (BigQuery$JobListOption/fields (into-array BigQuery$JobField (map #(BigQuery$JobField/valueOf %) fields)))
    maxCreationTime
    (BigQuery$JobListOption/maxCreationTime (long maxCreationTime))
    minCreationTime
    (BigQuery$JobListOption/minCreationTime (long minCreationTime))
    pageSize
    (BigQuery$JobListOption/pageSize (long pageSize))
    pageToken
    (BigQuery$JobListOption/pageToken pageToken)
    parentJobId
    (BigQuery$JobListOption/parentJobId parentJobId)
    true
    (BigQuery$JobListOption/stateFilter (into-array JobStatus$State (map #(JobStatus$State/valueOf %) stateFilters)))))

(defn ^BigQuery$JobOption JobOption-from-edn [arg]
  (g/strict! :gcp/bigquery.BigQuery.JobOption arg)
  (if (contains? arg :fields)
    (BigQuery$JobOption/fields (into-array BigQuery$JobField (map #(BigQuery$JobField/valueOf %) (:fields arg))))
    (if (contains? arg :gcp/bigquery.BigQueryRetryConfig)
      (BigQuery$JobOption/bigQueryRetryConfig (BigQueryRetryConfig/from-edn (:gcp/bigquery.BigQueryRetryConfig arg)))
      (BigQuery$JobOption/retryOptions (into-array RetryOption (map RO/from-edn (:options arg)))))))

(defn ^BigQuery$ModelListOption ModelListOption-from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn ^BigQuery$ModelOption ModelOption-from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn ^BigQuery$QueryOption QueryOption-from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn ^BigQuery$QueryResultsOption QueryResultsOption-from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn ^BigQuery$RoutineListOption RoutineListOption-from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn ^BigQuery$RoutineOption RoutineOption-from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn ^BigQuery$TableDataListOption TableDataListOption-from-edn
  [{:keys [pageSize pageToken] :as arg}]
  (g/strict! :gcp/bigquery.BigQuery.TableListOption arg)
  (if pageSize
    (BigQuery$TableDataListOption/pageSize (long pageSize))
    (BigQuery$TableDataListOption/pageToken pageToken)))

(defn ^BigQuery$TableListOption TableListOption-from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn ^BigQuery$TableOption TableOption-from-edn
  [{:keys [autoDetect fields tableMetadataView] :as arg}]
  (g/strict! :gcp/bigquery.BigQuery.TableOption arg)
  (if autoDetect
    (BigQuery$TableOption/autodetectSchema autoDetect)
    (if tableMetadataView
      (BigQuery$TableOption/tableMetadataView (BigQuery$TableMetadataView/valueOf tableMetadataView))
      (BigQuery$TableOption/fields (into-array BigQuery$TableField (map #(BigQuery$TableField/valueOf %) fields))))))
