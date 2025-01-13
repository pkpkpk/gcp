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
            [gcp.bigquery.v2.BigQueryOptions :as BQO]
            [gcp.bigquery.v2.Dataset :as Dataset]
            [gcp.bigquery.v2.DatasetId]
            [gcp.bigquery.v2.DatasetInfo :as DatasetInfo]
            [gcp.bigquery.v2.Job :as Job]
            [gcp.bigquery.v2.JobId :as JobId]
            [gcp.bigquery.v2.JobInfo :as JobInfo]
            [gcp.bigquery.v2.QueryJobConfiguration :as QJC]
            [gcp.bigquery.v2.Table :as Table]
            [gcp.bigquery.v2.TableId :as TableId]
            [gcp.bigquery.v2.TableInfo :as TableInfo]
            [gcp.global :as g])
  (:import (com.google.cloud.bigquery BigQuery BigQuery$DatasetDeleteOption BigQuery$DatasetListOption BigQuery$DatasetOption BigQuery$JobOption BigQuery$TableListOption BigQuery$TableOption DatasetId)))

(defn ^BigQuery client
  ([] (client nil))
  ([arg]
   (g/coerce :bigquery.synth/clientable arg)
   (if (instance? BigQuery arg)
     arg
     (g/client :bigquery.synth/client arg))))

#!-----------------------------------------------------------------------------
#! DATASETS https://cloud.google.com/bigquery/docs/datasets

(defn list-datasets
  ([] (list-datasets nil))
  ([{:keys [bigquery projectId options] :as arg}]
   (g/coerce :bigquery.synth/DatasetList arg)
   (let [opts     (into-array BigQuery$DatasetListOption (map DLO/from-edn options))
         datasets (if projectId
                    (.listDatasets (client bigquery) projectId opts)
                    (.listDatasets (client bigquery) opts))]
     (map Dataset/to-edn (seq (.iterateAll datasets))))))

(defn create-dataset [arg]
  (if (string? arg)
    (create-dataset {:datasetInfo {:datasetId {:dataset arg}}})
    (if (g/valid? :bigquery/DatasetInfo arg)
      (create-dataset {:datasetInfo arg})
      (let [{:keys [bigquery datasetInfo options]} (g/coerce :bigquery.synth/DatasetCreate arg)
            info (DatasetInfo/from-edn datasetInfo)
            opts ^BigQuery$DatasetOption/1 (into-array BigQuery$DatasetOption (map DO/from-edn options))]
        (Dataset/to-edn (.create (client bigquery) info opts))))))

(defn get-dataset [arg]
  (if (string? arg)
    (get-dataset {:datasetId {:dataset arg}})
    (if (g/valid? :bigquery/DatasetId arg)
      (get-dataset {:datasetId arg})
      (let [{:keys [bigquery datasetId options]} (g/coerce :bigquery.synth/DatasetGet arg)
            dataset-id (gcp.bigquery.v2.DatasetId/from-edn datasetId)
            opts ^BigQuery$DatasetOption/1 (into-array BigQuery$DatasetOption (map DO/from-edn options))]
        (Dataset/to-edn (.getDataset (client bigquery) dataset-id opts))))))

(defn update-dataset [arg]
  (if (string? arg)
    (update-dataset {:datasetInfo {:datasetId {:dataset arg}}})
    (if (g/valid? :bigquery/DatasetInfo arg)
      (update-dataset {:datasetInfo arg})
      (let [{:keys [bigquery datasetInfo options]} (g/coerce :bigquery.synth/DatasetUpdate arg)
            info (DatasetInfo/from-edn datasetInfo)
            opts ^BigQuery$DatasetOption/1 (into-array BigQuery$DatasetOption (map DO/from-edn options))]
        (Dataset/to-edn (.update (client bigquery) info opts))))))

(defn ^boolean delete-dataset
  "true if dataset was deleted, false if it was not found"
  [arg]
  (if (string? arg)
    (delete-dataset {:datasetId {:dataset arg}})
    (if (g/valid? :bigquery/DatasetId arg)
      (delete-dataset {:datasetId arg})
      (let [{:keys [bigquery datasetId options]} (g/coerce :bigquery.synth/DatasetDelete arg)
            dataset-id (gcp.bigquery.v2.DatasetId/from-edn datasetId)
            opts ^BigQuery$DatasetDeleteOption/1 (into-array BigQuery$DatasetDeleteOption (map DDO/from-edn options))]
        (.delete (client bigquery) dataset-id opts)))))

#!-----------------------------------------------------------------------------
#! TABLES https://cloud.google.com/bigquery/docs/tables

(defn list-tables [arg]
  (if (string? arg)
    (list-tables {:datasetId {:dataset arg}})
    (if (g/valid? :bigquery/DatasetId arg)
      (list-tables {:datasetId arg})
      (let [{:keys [bigquery datasetId options]} (g/coerce :bigquery.synth/TableList arg)
            opts       ^BigQuery$TableListOption/1 (into-array BigQuery$TableListOption (map TLO/from-edn options))
            dataset-id (gcp.bigquery.v2.DatasetId/from-edn datasetId)
            tables     (.listTables (client bigquery) dataset-id opts)]
        (map Table/to-edn (seq (.iterateAll tables)))))))

(defn create-table
  [{:keys [bigquery tableInfo options] :as arg}]
  (g/strict! :bigquery.synth/TableCreate arg)
  (let [info (TableInfo/from-edn tableInfo)
        opts ^BigQuery$TableOption/1 (into-array BigQuery$TableOption (map TO/from-edn options))]
    (Table/to-edn (.create (client bigquery) info opts))))

(defn get-table
  [{:keys [bigquery tableId options] :as arg}]
  (g/strict! :bigquery.synth/TableGet arg)
  (let [opts (into-array BigQuery$TableOption (map TO/from-edn options))]
    (Table/to-edn (.getTable (client bigquery) (TableId/from-edn tableId) opts))))

(defn update-table
  [{:keys [bigquery tableInfo options] :as arg}]
  (g/strict! :bigquery.synth/TableUpdate arg)
  (let [opts ^BigQuery$TableOption/1 (into-array BigQuery$TableOption (map TO/from-edn options))]
    (Table/to-edn (.update (client bigquery) (TableInfo/from-edn tableInfo) opts))))

(defn delete-table [arg]
  (if (string? arg)
    (delete-table {:tableId {:table arg}})
    (if (g/valid? :bigquery/TableId arg)
      (delete-table {:tableId arg})
      (let [{:keys [bigquery tableId]} (g/coerce :bigquery.synth/TableDelete arg)]
        (.delete (client bigquery) (TableId/from-edn tableId))))))

;listTableData(TableId tableId, BigQuery.TableDataListOption[] options)
;listTableData(TableId tableId, Schema schema, BigQuery.TableDataListOption[] options)
;listTableData(String datasetId, String tableId, BigQuery.TableDataListOption[] options)
;listTableData(String datasetId, String tableId, Schema schema, BigQuery.TableDataListOption[] options)

;setIamPolicy(TableId tableId, Policy policy, BigQuery.IAMOption[] options)
;testIamPermissions(TableId table, List<String> permissions, BigQuery.IAMOption[] options)

#!-----------------------------------------------------------------------------
#! JOBS https://cloud.google.com/bigquery/docs/jobs-overview

(defn list-jobs
  [{:keys [bigquery options] :as arg}]
  (g/strict! :bigquery.synth/JobList arg)
  (let [opts (into-array BigQuery$JobOption (map JO/from-edn options))]
    (.listJobs ^BigQuery bigquery opts)))

(defn create-job
  [{:keys [bigquery jobInfo options] :as arg}]
  (g/strict! :bigquery.synth/JobCreate arg)
  (let [opts ^BigQuery$JobOption/1 (into-array BigQuery$JobOption (map JO/from-edn options))]
    (Job/to-edn (.create ^BigQuery bigquery (JobInfo/from-edn jobInfo) opts))))

(defn get-job
  [{:keys [bigquery jobId options] :as arg}]
  (g/strict! :bigquery.synth/JobGet arg)
  (let [opts ^BigQuery$JobOption/1 (into-array BigQuery$JobOption (map JO/from-edn options))]
    (if (string? jobId)
      (.getJob ^BigQuery bigquery ^String jobId opts)
      (.getJob ^BigQuery bigquery (JobId/from-edn jobId) opts))))

(defn ^boolean cancel-job
  [{:keys [bigquery jobId]}]
  (if (string? jobId)
    (.cancel ^BigQuery bigquery ^String jobId)
    (.cancel ^BigQuery bigquery (JobId/from-edn jobId))))


; (defonce ^:dynamic *session-id* nil)
; (ConnectionProperty/of "session_id" *session-id*)

(defn query
  [{:keys [bigquery configuration options jobId] :as arg}]
  (g/strict! :bigquery.synth/Query arg)
  (let [opts (into-array BigQuery$JobOption (map JO/from-edn options))
        qjc (QJC/from-edn configuration)]
    (if jobId
      (.query bigquery qjc (JobId/from-edn jobId) opts)
      (.query bigquery qjc opts))))

;;TODO
;getQueryResults(JobId jobId, BigQuery.QueryResultsOption[] options)
;(defn dry-run [])
;(defn copy-table [])

(defn clone-table
  ([{:keys [bigquery configuration options] :as arg}]
   (let [cfg (merge configuration {:operationType    "CLONE",
                                   :writeDisposition "WRITE_EMPTY"
                                   :sourceTables     [],
                                   :destinationTable {:project "", :dataset "", :table ""}})]
     (create-job (assoc-in arg [:jobInfo :configuration] cfg))))
  ([arg src dst]
   ()))

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
#! MODELS https://cloud.google.com/bigquery/docs/bqml-introduction

;create(ModelInfo modelInfo, BigQuery.ModelOption[] options)
;delete(ModelId modelId)
;getModel(ModelId tableId, BigQuery.ModelOption[] options)
;getModel(String datasetId, String modelId, BigQuery.ModelOption[] options)
;listModels(DatasetId datasetId, BigQuery.ModelListOption[] options)
;listModels(String datasetId, BigQuery.ModelListOption[] options)
;update(ModelInfo modelInfo, BigQuery.ModelOption[] options)

#!-----------------------------------------------------------------------------
#! Other

;createConnection()
;createConnection(@NonNull ConnectionSettings connectionSettings)
;listPartitions(TableId tableId)
;insertAll(InsertAllRequest request)
