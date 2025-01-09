(ns gcp.bigquery.v2.BigQuery.JobListOption
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery BigQuery$JobField BigQuery$JobListOption JobStatus$State)))

(defn from-edn
  [{:keys [fields
           maxCreationTime
           minCreationTime
           pageSize
           pageToken
           parentJobId
           stateFilters] :as arg}]
  (global/strict! :bigquery.BigQuery/JobListOption arg)
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