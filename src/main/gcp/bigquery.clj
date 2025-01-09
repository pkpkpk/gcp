(ns gcp.bigquery
  (:require [gcp.bigquery.v2]
            [gcp.bigquery.v2.BigQuery.DatasetDeleteOption :as DDO]
            [gcp.bigquery.v2.BigQuery.DatasetListOption :as DLO]
            [gcp.bigquery.v2.BigQuery.DatasetOption :as DO]
            [gcp.bigquery.v2.BigQuery.IAMOption :as IAMOption]
            [gcp.bigquery.v2.BigQuery.JobListOption :as JLO]
            [gcp.bigquery.v2.BigQuery.JobOption :as JO]
            [gcp.bigquery.v2.BigQuery.ModelListOption :as MLO]
            [gcp.bigquery.v2.BigQuery.ModelOption :as MO]
            [gcp.bigquery.v2.BigQuery.QueryOption :as QO]
            [gcp.bigquery.v2.BigQuery.QueryResultOption :as QRO]
            [gcp.bigquery.v2.BigQuery.RoutineListOption :as RLO]
            [gcp.bigquery.v2.BigQuery.RoutineOption :as RO]
            [gcp.bigquery.v2.BigQuery.TableDataListOption :as TDLO]
            [gcp.bigquery.v2.BigQuery.TableListOption :as TLO]
            [gcp.bigquery.v2.BigQuery.TableOption :as TO]
            [gcp.bigquery.v2.BigQueryOptions :as bqo]
            [gcp.bigquery.v2.Dataset :as Dataset]
            [gcp.bigquery.v2.DatasetId :as DatasetId]
            [gcp.bigquery.v2.DatasetInfo :as DatasetInfo]
            [gcp.bigquery.v2.Job :as Job]
            [gcp.bigquery.v2.JobId :as JobId]
            [gcp.bigquery.v2.JobInfo :as JobInfo]
            [gcp.bigquery.v2.QueryJobConfiguration :as QJC]
            [gcp.bigquery.v2.Table :as Table]
            [gcp.bigquery.v2.TableId :as TableId]
            [gcp.bigquery.v2.TableInfo :as TableInfo]
            [gcp.global :as global])
  (:import (com.google.cloud.bigquery BigQuery BigQuery$DatasetDeleteOption BigQuery$DatasetListOption BigQuery$DatasetOption BigQuery$JobOption BigQuery$TableListOption BigQuery$TableOption)))

(defn client
  ([]
   (client {}))
  ([opts]
   (global/strict! :bigquery/BigQueryOptions opts)
   (bqo/get-service (bqo/from-edn opts))))

#!-----------------------------------------------------------------------------
#! DATASETS https://cloud.google.com/bigquery/docs/datasets

(defn list-datasets
  [{:keys [bigquery projectId options] :as arg}]
  (global/strict! :bigquery.synth/DatasetList arg)
  (let [opts (into-array BigQuery$DatasetListOption (map DLO/from-edn options))
        datasets (if projectId
                   (.listDatasets bigquery projectId opts)
                   (.listDatasets bigquery opts))]
    (map Dataset/to-edn (seq (.iterateAll datasets)))))

(defn create-dataset
  [{:keys [bigquery datasetInfo options] :as arg}]
  (global/strict! :bigquery.synth/DatasetCreate arg)
  (let [info (DatasetInfo/from-edn datasetInfo)
        opts ^BigQuery$DatasetOption/1 (into-array BigQuery$DatasetOption (map DO/from-edn options))]
    (Dataset/to-edn (.create bigquery info opts))))

(defn update-dataset
  [{:keys [bigquery datasetInfo options] :as arg}]
  (global/strict! :bigquery.synth/DatasetUpdate arg)
  (let [info (DatasetInfo/from-edn datasetInfo)
        opts ^BigQuery$DatasetOption/1 (into-array BigQuery$DatasetOption (map DO/from-edn options))]
    (Dataset/to-edn (.update bigquery info opts))))

(defn get-dataset
  [{:keys [bigquery datasetId options] :as arg}]
  (global/strict! :bigquery.synth/DatasetGet arg)
  (let [id (DatasetId/from-edn datasetId)
        opts ^BigQuery$DatasetOption/1 (into-array BigQuery$DatasetOption (map DO/from-edn options))]
    (Dataset/to-edn (.getDataset ^BigQuery bigquery id opts))))

(defn ^boolean delete-dataset
  "true if dataset was deleted, false if it was not found"
  [{:keys [bigquery datasetId options] :as arg}]
  (global/strict! :bigquery.synth/DatasetDelete arg)
  (let [opts ^BigQuery$DatasetDeleteOption/1 (into-array BigQuery$DatasetDeleteOption (map DDO/from-edn options))]
    (.delete ^BigQuery bigquery (DatasetId/from-edn datasetId) opts)))

#!-----------------------------------------------------------------------------
#! TABLES https://cloud.google.com/bigquery/docs/tables

(defn create-table
  [{:keys [bigquery tableInfo options] :as arg}]
  (global/strict! :bigquery.synth/TableCreate arg)
  (let [info (TableInfo/from-edn tableInfo)
        opts ^BigQuery$TableOption/1 (into-array BigQuery$TableOption (map TO/from-edn options))]
    (Table/to-edn (.create ^BigQuery bigquery info opts))))

(defn delete-table
  [{:keys [bigquery tableId] :as arg}]
  (global/strict! :bigquery.synth/TableDelete arg)
  (.delete ^BigQuery bigquery (TableId/from-edn tableId)))

(defn get-table
  [{:keys [bigquery tableId options] :as arg}]
  (global/strict! :bigquery.synth/TableGet arg)
  (let [opts (into-array BigQuery$TableOption (map TO/from-edn options))]
    (.getTable ^BigQuery bigquery (TableId/from-edn tableId) opts)))

(defn list-tables
  [{:keys [bigquery datasetId options] :as arg}]
  (global/strict! :bigquery.synth/TableList arg)
  (let [opts ^BigQuery$TableListOption/1 (into-array BigQuery$TableListOption (map TLO/from-edn options))]
    (.listTables ^BigQuery bigquery (DatasetId/from-edn datasetId) opts)))

(defn update-table
  [{:keys [bigquery tableInfo options] :as arg}]
  (global/strict! :bigquery.synth/TableUpdate arg)
  (let [opts ^BigQuery$TableOption/1 (into-array BigQuery$TableOption (map TO/from-edn options))]
    (.update ^BigQuery bigquery (TableInfo/from-edn tableInfo) opts)))

;listTableData(TableId tableId, BigQuery.TableDataListOption[] options)
;listTableData(TableId tableId, Schema schema, BigQuery.TableDataListOption[] options)
;listTableData(String datasetId, String tableId, BigQuery.TableDataListOption[] options)
;listTableData(String datasetId, String tableId, Schema schema, BigQuery.TableDataListOption[] options)

;setIamPolicy(TableId tableId, Policy policy, BigQuery.IAMOption[] options)
;testIamPermissions(TableId table, List<String> permissions, BigQuery.IAMOption[] options)

#!-----------------------------------------------------------------------------
#! JOBS https://cloud.google.com/bigquery/docs/jobs-overview

(defn create-job
  [{:keys [bigquery jobInfo options] :as arg}]
  (global/strict! :bigquery.synth/JobCreate arg)
  (let [opts ^BigQuery$JobOption/1 (into-array BigQuery$JobOption (map JO/from-edn options))]
    (Job/to-edn (.create ^BigQuery bigquery (JobInfo/from-edn jobInfo) opts))))

(defn ^boolean cancel-job
  [{:keys [bigquery jobId]}]
  (if (string? jobId)
    (.cancel ^BigQuery bigquery ^String jobId)
    (.cancel ^BigQuery bigquery (JobId/from-edn jobId))))

(defn get-job
  [{:keys [bigquery jobId options] :as arg}]
  (global/strict! :bigquery.synth/JobGet arg)
  (let [opts ^BigQuery$JobOption/1 (into-array BigQuery$JobOption (map JO/from-edn options))]
    (if (string? jobId)
      (.getJob ^BigQuery bigquery ^String jobId opts)
      (.getJob ^BigQuery bigquery (JobId/from-edn jobId) opts))))

(defn list-jobs
  [{:keys [bigquery options] :as arg}]
  (global/strict! :bigquery.synth/JobList arg)
  (let [opts (into-array BigQuery$JobOption (map JO/from-edn options))]
    (.listJobs ^BigQuery bigquery opts)))


; (defonce ^:dynamic *session-id* nil)
; (ConnectionProperty/of "session_id" *session-id*)

(defn query
  [{:keys [bigquery configuration options jobId] :as arg}]
  (global/strict! :bigquery.synth/Query arg)
  (let [opts (into-array BigQuery$JobOption (map JO/from-edn options))
        qjc (QJC/from-edn configuration)]
    (if jobId
      (.query bigquery qjc (JobId/from-edn jobId) opts)
      (.query bigquery qjc opts))))

;getQueryResults(JobId jobId, BigQuery.QueryResultsOption[] options)

#!-----------------------------------------------------------------------------
#! MODELS https://cloud.google.com/bigquery/docs/bqml-introduction

;create(ModelInfo modelInfo, BigQuery.ModelOption[] options)
;delete(ModelId modelId)
;getModel(ModelId tableId, BigQuery.ModelOption[] options)
;getModel(String datasetId, String modelId, BigQuery.ModelOption[] options)
;listModels(DatasetId datasetId, BigQuery.ModelListOption[] options)
;listModels(String datasetId, BigQuery.ModelListOption[] options)
;update(ModelInfo modelInfo, BigQuery.ModelOption[] options)

#!-----------------------------------------------------------------------------
#! ROUTINES https://cloud.google.com/bigquery/docs/routines

;create(RoutineInfo routineInfo, BigQuery.RoutineOption[] options)
;delete(RoutineId routineId)
;getRoutine(RoutineId routineId, BigQuery.RoutineOption[] options)
;getRoutine(String datasetId, String routineId, BigQuery.RoutineOption[] options)
;listRoutines(DatasetId datasetId, BigQuery.RoutineListOption[] options)
;listRoutines(String datasetId, BigQuery.RoutineListOption[] options)
;update(RoutineInfo routineInfo, BigQuery.RoutineOption[] options)

#!-----------------------------------------------------------------------------
#! Other

;createConnection()
;createConnection(@NonNull ConnectionSettings connectionSettings)
;listPartitions(TableId tableId)
;insertAll(InsertAllRequest request)
