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
            [gcp.bigquery.v2.TableResult :as TableResult]
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
  (:import (com.google.cloud.bigquery BigQuery BigQuery$DatasetDeleteOption BigQuery$DatasetListOption BigQuery$DatasetOption BigQuery$JobListOption BigQuery$JobOption BigQuery$TableListOption BigQuery$TableOption DatasetId)))

(defonce ^:dynamic *client* nil)

(defn ^BigQuery client
  ([] (client nil))
  ([arg]
   (or *client*
       (do
         (g/strict! :bigquery.synth/clientable arg)
         (if (instance? BigQuery arg)
           arg
           (g/client :bigquery.synth/client arg))))))

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

(defn create-table [arg]
  (if (g/valid? :bigquery/TableInfo arg)
    (create-table {:tableInfo arg})
    (let [{:keys [bigquery tableInfo options]} (g/coerce :bigquery.synth/TableCreate arg)
          info (TableInfo/from-edn tableInfo)
          opts ^BigQuery$TableOption/1 (into-array BigQuery$TableOption (map TO/from-edn options))]
      (Table/to-edn (.create (client bigquery) info opts)))))

(defn get-table
  ([dataset table]
   (get-table {:tableId {:dataset dataset :table table}}))
  ([arg]
   (if (g/valid? :bigquery/TableId arg)
     (get-table {:tableId arg})
     (let [{:keys [bigquery tableId options]} (g/coerce :bigquery.synth/TableGet arg)
           opts (into-array BigQuery$TableOption (map TO/from-edn options))]
       (Table/to-edn (.getTable (client bigquery) (TableId/from-edn tableId) opts))))))

(defn update-table [arg]
  (if (g/valid? :bigquery/TableInfo arg)
    (update-table {:tableInfo arg})
    (let [{:keys [bigquery tableInfo options]} (g/coerce :bigquery.synth/TableUpdate arg)
          opts ^BigQuery$TableOption/1 (into-array BigQuery$TableOption (map TO/from-edn options))]
      (Table/to-edn (.update (client bigquery) (TableInfo/from-edn tableInfo) opts)))))

(defn delete-table
  ([arg]
   (if (g/valid? :bigquery/TableId arg)
     (delete-table {:tableId arg})
     (let [{:keys [bigquery tableId]} (g/coerce :bigquery.synth/TableDelete arg)]
       (.delete (client bigquery) (TableId/from-edn tableId)))))
  ([dataset table]
   (delete-table (g/coerce :bigquery/TableId {:dataset dataset :table table}))))

#!-----------------------------------------------------------------------------
#! JOBS https://cloud.google.com/bigquery/docs/jobs-overview

(defn list-jobs
  ([] (list-jobs nil))
  ([{:keys [bigquery options] :as arg}]
   (g/strict! :bigquery.synth/JobList arg)
   (let [opts (into-array BigQuery$JobListOption (map JLO/from-edn options))]
     (map Job/to-edn (seq (.iterateAll (.listJobs (client bigquery) opts)))))))

(defn create-job
  [{:keys [bigquery jobInfo options] :as arg}]
  (g/strict! :bigquery.synth/JobCreate arg)
  (let [opts ^BigQuery$JobOption/1 (into-array BigQuery$JobOption (map JO/from-edn options))]
    (Job/to-edn (.create (client bigquery) (JobInfo/from-edn jobInfo) opts))))

(defn get-job
  [{:keys [bigquery jobId options] :as arg}]
  (g/strict! :bigquery.synth/JobGet arg)
  (let [opts ^BigQuery$JobOption/1 (into-array BigQuery$JobOption (map JO/from-edn options))]
    (if (string? jobId)
      (Job/to-edn (.getJob (client bigquery) ^String jobId opts))
      (Job/to-edn (.getJob (client bigquery) (JobId/from-edn jobId) opts)))))

(defn ^boolean cancel-job
  [{:keys [bigquery jobId]}]
  (if (string? jobId)
    (.cancel (client bigquery) ^String jobId)
    (.cancel (client bigquery) (JobId/from-edn jobId))))

(defn query [arg]
  (if (string? arg)
    (query {:configuration {:type "QUERY" :query arg}})
    (if (contains? arg :query)
      (query {:configuration (assoc arg :type "QUERY")})
      (let [{:keys [bigquery configuration options jobId]} (g/coerce :bigquery.synth/Query arg)
            opts (into-array BigQuery$JobOption (map JO/from-edn options))
            qjc  (QJC/from-edn configuration)
            res  (if jobId
                   (.query (client bigquery) qjc (JobId/from-edn jobId) opts)
                   (.query (client bigquery) qjc opts))]
        (TableResult/to-edn res)))))

#! TODO
; (defn insert-rows [])
; (defn list-rows [])
; (defn load-table [])
; listTableData(TableId tableId, BigQuery.TableDataListOption[] options)
; listTableData(TableId tableId, Schema schema, BigQuery.TableDataListOption[] options)
; listTableData(String datasetId, String tableId, BigQuery.TableDataListOption[] options)
; listTableData(String datasetId, String tableId, Schema schema, BigQuery.TableDataListOption[] options)
; setIamPolicy(TableId tableId, Policy policy, BigQuery.IAMOption[] options)
; testIamPermissions(TableId table, List<String> permissions, BigQuery.IAMOption[] options)
; (defonce ^:dynamic *session-id* nil)
; (ConnectionProperty/of "session_id" *session-id*)
; (defn dry-run [])
; TODO offer resource string arg ie /$project/$dataset/$table?

(defn
  ^{:urls ["https://cloud.google.com/bigquery/docs/exporting-data"
           "https://cloud.google.com/bigquery/docs/reference/standard-sql/export-statements"
           "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery.ExtractJobConfiguration"]}
  extract-table
  ([table format compression dst & opts]
   (let [table (if (g/valid? :bigquery/TableId table)
                 table
                 (if (g/valid? :bigquery/TableInfo table)
                   (get table :tableId)
                   (throw (ex-info "must provide valid tableId" {:table table
                                                                 :format format
                                                                 :dst dst
                                                                 :opts opts}))))
         dst (if (string? dst)
               [dst]
               (if (g/valid? [:sequential :string] dst)
                 dst
                 (throw (ex-info "destination should be string uris" {:table table
                                                                      :format format
                                                                      :dst dst
                                                                      :opts opts}))))
         configuration {:type "EXTRACT"
                        :sourceTable (g/coerce :bigquery/TableId table)
                        :format format
                        :compression compression
                        :destinationUris dst}]
     (create-job {:bigquery (:bigquery table) ;; if Table, use same client
                  :jobInfo  {:configuration (g/coerce :bigquery/ExtractJobConfiguration configuration)}
                  :options  (not-empty opts)}))))

;;TODO split to clone-table-configuration so can allow opts?
(defn clone-table
  ([source destination]
   (let [source-tables (if (g/valid? [:sequential :bigquery/TableId] source)
                         source
                         (if (g/valid? :bigquery/TableId source)
                           [source]
                           (if (g/valid? :bigquery/TableInfo source)
                             [(:tableId source)]
                             (if (g/valid? [:sequential :bigquery/TableInfo] source)
                               (mapv :tableId source)
                               (throw (ex-info "cannot create clone source"
                                               {:source      source
                                                :destination destination}))))))
         destination-table (if (g/valid? :bigquery/TableId destination)
                             destination
                             (if (string? destination)
                               (if (= 1 (count source-tables))
                                 {:dataset destination
                                  :table   (get (first source-tables) :table)}
                                 (throw (ex-info "must provide name for composite destination table"
                                                 {:source source
                                                  :destination destination})))
                               (throw (ex-info "cannot create clone destination"
                                               {:source source
                                                :destination destination}))))
         configuration {:type "COPY"
                        :sourceTables     (g/coerce [:sequential :bigquery/TableId] source-tables)
                        :destinationTable (g/coerce :bigquery/TableId destination-table)
                        :operationType    "CLONE",
                        :writeDisposition "WRITE_EMPTY"}]
     (create-job {:jobInfo {:configuration (g/coerce :bigquery/CopyJobConfiguration configuration)}})))
  ([sourceDataset sourceTable destinationDataset]
   (let [source (g/coerce :bigquery/TableId {:dataset sourceDataset :table sourceTable})]
     (clone-table source destinationDataset)))
  ([sourceDataset sourceTable destinationDataset destinationTable]
   (let [source (g/coerce :bigquery/TableId {:dataset sourceDataset :table sourceTable})
         destination (g/coerce :bigquery/TableId {:dataset destinationDataset :table destinationTable})]
     (clone-table source destination))))

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
