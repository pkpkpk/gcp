(ns gcp.bigquery
  (:require [gcp.bigquery.v2]
            [gcp.bigquery.v2.BigQuery :as BQ]
            [gcp.bigquery.v2.BigQueryException :as BQE]
            [gcp.bigquery.v2.RoutineId :as RoutineId]
            [gcp.bigquery.v2.RoutineInfo :as RoutineInfo]
            [gcp.bigquery.v2.TableResult :as TableResult]
            [gcp.bigquery.v2.Dataset :as Dataset]
            [gcp.bigquery.v2.DatasetId]
            [gcp.bigquery.v2.DatasetInfo :as DatasetInfo]
            [gcp.bigquery.v2.InsertAllResponse :as InsertAllResponse]
            [gcp.bigquery.v2.InsertAllRequest :as InsertAllRequest]
            [gcp.bigquery.v2.Job :as Job]
            [gcp.bigquery.v2.JobException :as JE]
            [gcp.bigquery.v2.JobId :as JobId]
            [gcp.bigquery.v2.JobInfo :as JobInfo]
            [gcp.bigquery.v2.QueryJobConfiguration :as QJC]
            [gcp.bigquery.v2.Routine :as Routine]
            [gcp.bigquery.v2.Table :as Table]
            [gcp.bigquery.v2.TableId :as TableId]
            [gcp.bigquery.v2.TableInfo :as TableInfo]
            [gcp.bigquery.v2.WriteChannelConfiguration :as WriteChannelConfiguration]
            [gcp.global :as g])
  (:import (com.google.cloud.bigquery BigQuery BigQuery$DatasetDeleteOption BigQuery$DatasetListOption BigQuery$DatasetOption BigQuery$JobListOption BigQuery$JobOption BigQuery$RoutineListOption BigQuery$RoutineOption BigQuery$TableListOption BigQuery$TableOption BigQueryException DatasetId JobException TableDataWriteChannel)))

(defonce ^:dynamic *client* nil)

(defn ^BigQuery client
  ([] (client nil))
  ([arg]
   (or *client*
       (do
         (g/strict! :gcp/bigquery.synth.clientable arg)
         (if (instance? BigQuery arg)
           arg
           (g/client :gcp/bigquery.synth.client arg))))))

#!-----------------------------------------------------------------------------
#! DATASETS https://cloud.google.com/bigquery/docs/datasets

(defn list-datasets
  ([] (list-datasets nil))
  ([arg]
   (if (string? arg)
     (list-datasets {:projectId arg})
     (if (g/valid? :gcp/bigquery.synth.DatasetList arg)
       (let [{:keys [bigquery projectId options]} arg
             opts     (into-array BigQuery$DatasetListOption (map BQ/DatasetListOption-from-edn options))
             datasets (if projectId
                        (.listDatasets (client bigquery) projectId opts)
                        (.listDatasets (client bigquery) opts))]
         (map Dataset/to-edn (seq (.iterateAll datasets))))
       (let [explanation (g/explain :gcp/bigquery.synth.DatasetList arg)]
         (throw (ex-info "cannot create :gcp/bigquery.synth.DatasetList from arg" {:arg         arg
                                                                                   :explanation explanation})))))))

(defn create-dataset [arg]
  (if (string? arg)
    (create-dataset {:datasetInfo {:datasetId {:dataset arg}}})
    (if (g/valid? :gcp/bigquery.DatasetInfo arg)
      (create-dataset {:datasetInfo arg})
      (let [{:keys [bigquery datasetInfo options]} (g/coerce :gcp/bigquery.synth.DatasetCreate arg)
            info (DatasetInfo/from-edn datasetInfo)
            opts ^BigQuery$DatasetOption/1 (into-array BigQuery$DatasetOption (map BQ/DatasetOption-from-edn options))]
        (Dataset/to-edn (.create (client bigquery) info opts))))))

(defn get-dataset [arg]
  (if (string? arg)
    (get-dataset {:datasetId {:dataset arg}})
    (if (g/valid? :gcp/bigquery.DatasetId arg)
      (get-dataset {:datasetId arg})
      (let [{:keys [bigquery datasetId options]} (g/coerce :gcp/bigquery.synth.DatasetGet arg)
            dataset-id (gcp.bigquery.v2.DatasetId/from-edn datasetId)
            opts ^BigQuery$DatasetOption/1 (into-array BigQuery$DatasetOption (map BQ/DatasetOption-from-edn options))]
        (Dataset/to-edn (.getDataset (client bigquery) dataset-id opts))))))

(defn update-dataset [arg]
  (if (string? arg)
    (update-dataset {:datasetInfo {:datasetId {:dataset arg}}})
    (if (g/valid? :gcp/bigquery.DatasetInfo arg)
      (update-dataset {:datasetInfo arg})
      (let [{:keys [bigquery datasetInfo options]} (g/coerce :gcp/bigquery.synth.DatasetUpdate arg)
            info (DatasetInfo/from-edn datasetInfo)
            opts ^BigQuery$DatasetOption/1 (into-array BigQuery$DatasetOption (map BQ/DatasetOption-from-edn options))]
        (Dataset/to-edn (.update (client bigquery) info opts))))))

(defn ^boolean delete-dataset
  "true if dataset was deleted, false if it was not found"
  [arg]
  (if (string? arg)
    (delete-dataset {:datasetId {:dataset arg}})
    (if (g/valid? :gcp/bigquery.DatasetId arg)
      (delete-dataset {:datasetId arg})
      (let [{:keys [bigquery datasetId options]} (g/coerce :gcp/bigquery.synth.DatasetDelete arg)
            dataset-id (gcp.bigquery.v2.DatasetId/from-edn datasetId)
            opts ^BigQuery$DatasetDeleteOption/1 (into-array BigQuery$DatasetDeleteOption (map BQ/DatasetDeleteOption-from-edn options))]
        (.delete (client bigquery) dataset-id opts)))))

#!-----------------------------------------------------------------------------
#! TABLES https://cloud.google.com/bigquery/docs/tables

(defn list-tables [arg]
  (if (string? arg)
    (list-tables {:datasetId {:dataset arg}})
    (if (g/valid? :gcp/bigquery.DatasetId arg)
      (list-tables {:datasetId arg})
      (let [{:keys [bigquery datasetId options]} (g/coerce :gcp/bigquery.synth.TableList arg)
            opts       ^BigQuery$TableListOption/1 (into-array BigQuery$TableListOption (map BQ/TableListOption-from-edn options))
            dataset-id (gcp.bigquery.v2.DatasetId/from-edn datasetId)
            tables     (.listTables (client bigquery) dataset-id opts)]
        (map Table/to-edn (seq (.iterateAll tables)))))))

(defn create-table [arg]
  (if (g/valid? :gcp/bigquery.TableInfo arg)
    (create-table {:tableInfo arg})
    (let [{:keys [bigquery tableInfo options]} (g/coerce :gcp/bigquery.synth.TableCreate arg)
          info (TableInfo/from-edn tableInfo)
          opts ^BigQuery$TableOption/1 (into-array BigQuery$TableOption (map BQ/TableOption-from-edn options))]
      (Table/to-edn (.create (client bigquery) info opts)))))

(defn get-table
  ([dataset table]
   (get-table {:tableId {:dataset dataset :table table}}))
  ([arg]
   (if (g/valid? :gcp/bigquery.TableId arg)
     (get-table {:tableId arg})
     (let [{:keys [bigquery tableId options]} (g/coerce :gcp/bigquery.synth.TableGet arg)
           opts (into-array BigQuery$TableOption (map BQ/TableOption-from-edn options))]
       (Table/to-edn (.getTable (client bigquery) (TableId/from-edn tableId) opts))))))

(defn update-table [arg]
  (if (g/valid? :gcp/bigquery.TableInfo arg)
    (update-table {:tableInfo arg})
    (let [{:keys [bigquery tableInfo options]} (g/coerce :gcp/bigquery.synth.TableUpdate arg)
          opts ^BigQuery$TableOption/1 (into-array BigQuery$TableOption (map BQ/TableOption-from-edn options))]
      (Table/to-edn (.update (client bigquery) (TableInfo/from-edn tableInfo) opts)))))

(defn ^boolean delete-table
  ([arg]
   (if (g/valid? :gcp/bigquery.TableId arg)
     (delete-table {:tableId arg})
     (let [{:keys [bigquery tableId]} (g/coerce :gcp/bigquery.synth.TableDelete arg)]
       (.delete (client bigquery) (TableId/from-edn tableId)))))
  ([dataset table]
   (delete-table (g/coerce :gcp/bigquery.TableId {:dataset dataset :table table}))))

#!-----------------------------------------------------------------------------
#! JOBS https://cloud.google.com/bigquery/docs/jobs-overview

(defn list-jobs
  ([] (list-jobs nil))
  ([{:keys [bigquery options] :as arg}]
   (g/strict! :gcp/bigquery.synth.JobList arg)
   (let [opts (into-array BigQuery$JobListOption (map BQ/JobListOption-from-edn options))]
     (map Job/to-edn (seq (.iterateAll (.listJobs (client bigquery) opts)))))))

(defn create-job
  [{:keys [bigquery jobInfo options] :as arg}]
  (g/strict! :gcp/bigquery.synth.JobCreate arg)
  (let [opts ^BigQuery$JobOption/1 (into-array BigQuery$JobOption (map BQ/JobOption-from-edn options))]
    (Job/to-edn (.create (client bigquery) (JobInfo/from-edn jobInfo) opts))))

(defn get-job [arg]
  (if (string? arg)
    (get-job {:jobId {:job arg}})
    (if (g/valid? :gcp/bigquery.JobId arg)
      (get-job {:jobId arg})
      (let [{:keys [bigquery jobId options]} (g/coerce :gcp/bigquery.synth.JobGet arg)
            opts ^BigQuery$JobOption/1 (into-array BigQuery$JobOption (map BQ/JobOption-from-edn options))]
        (Job/to-edn (.getJob (client bigquery) (JobId/from-edn jobId) opts))))))

(defn ^boolean cancel-job [arg]
  (if (string? arg)
    (.cancel (client) ^String arg)
    (if (g/valid? :gcp/bigquery.JobId arg)
      (cancel-job {:jobId arg})
      (let [{:keys [bigquery jobId]} (g/coerce [:map
                                                [:jobId :gcp/bigquery.JobId]
                                                [:bigquery :gcp/bigquery.synth.clientable]] arg)]
        (.cancel (client bigquery) (JobId/from-edn jobId))))))

(defn query
  ([arg]
   (if (string? arg)
     (query {:configuration {:type "QUERY" :query arg}})
     (if (contains? arg :query)
       (query {:configuration (assoc arg :type "QUERY")})
       (try
         (let [{:keys [bigquery configuration options jobId]} (g/coerce :gcp/bigquery.synth.Query arg)
               opts (into-array BigQuery$JobOption (map BQ/JobOption-from-edn options))
               qjc  (QJC/from-edn configuration)
               res  (if jobId
                      (.query (client bigquery) qjc (JobId/from-edn jobId) opts)
                      (.query (client bigquery) qjc opts))]
           (TableResult/to-edn res))
         (catch BigQueryException bqe
           (let [{[err :as es] :errors :as bqe} (BQE/to-edn bqe)
                 msg (if (= 1 (count es))
                       (get err :message)
                       (str "'" (subs (get err :message) 0 30) ", and " (dec (count es)) " more errors"))]
             (throw (ex-info msg bqe))))
         (catch JobException je
           (throw (ex-info "job exception" (JE/to-edn je))))))))
  ([arg0 arg1]
   (query {:configuration {:type "QUERY" :query arg0 :queryParameters arg1}})))

#!-----------------------------------------------------------------------------
#! ROUTINES https://cloud.google.com/bigquery/docs/routines

(defn list-routines
  ([arg]
   (if (g/valid? :gcp/bigquery.synth.RoutineList arg)
     (let [{:keys [bigquery datasetId options]} arg
           datasetId (gcp.bigquery.v2.DatasetId/from-edn datasetId)
           opts      ^BigQuery$RoutineListOption/1 (into-array BigQuery$RoutineListOption (map BQ/RoutineListOption-from-edn options))]
       (map Routine/to-edn (.iterateAll (.listRoutines (client bigquery) datasetId opts))))
     (if (string? arg)
       (list-routines {:datasetId {:dataset arg}})
       (if (g/valid? :gcp/bigquery.DatasetId arg)
         (list-routines {:datasetId arg})
         (throw (ex-info "cannot create a :dataset from arg" {:arg arg})))))))

(defn create-routine
  ([arg]
   (if (g/valid? :gcp/bigquery.synth.RoutineCreate arg)
     (let [{:keys [bigquery routineInfo options]} arg
           opts ^BigQuery$RoutineOption/1 (into-array BigQuery$RoutineOption (map BQ/RoutineOption-from-edn options))]
       (Routine/to-edn (.create (client bigquery) (RoutineInfo/from-edn routineInfo) opts)))
     (if (g/valid? :gcp/bigquery.RoutineInfo arg)
       (create-routine {:routineInfo arg})
       (throw (g/human-ex-info [:or :gcp/bigquery.RoutineInfo :gcp/bigquery.synth.RoutineCreate] arg))))))

(defn delete-routine
  ([arg]
   (if (g/valid? :gcp/bigquery.synth.RoutineDelete arg)
     (let [{:keys [bigquery routineId]} arg]
       (.delete (client bigquery) (RoutineId/from-edn routineId)))
     (if (g/valid? :gcp/bigquery.RoutineId arg)
       (delete-routine {:routineId arg})
       (throw (g/human-ex-info [:or :gcp/bigquery.RoutineId :gcp/bigquery.synth.RoutineDelete] arg)))))
  ([dataset routine]
   (if-not (and (string? dataset) (string? routine))
     (throw (ex-info "(delete-routine dataset routine) requires string arguments" {:dataset dataset :routine routine}))
     (delete-routine {:routineId {:dataset dataset :routine routine}}))))

(defn get-routine
  ([arg]
   (if (g/valid? :gcp/bigquery.synth.RoutineGet arg)
     (let [{:keys [bigquery routineId options]} arg
           opts ^BigQuery$RoutineOption/1 (into-array BigQuery$RoutineOption (map BQ/RoutineOption-from-edn options))]
       (Routine/to-edn (.getRoutine (client bigquery) (RoutineId/from-edn routineId) opts)))
     (if (g/valid? :gcp/bigquery.RoutineId arg)
       (get-routine {:routineId arg})
       (throw (g/human-ex-info [:or :gcp/bigquery.RoutineId :gcp/bigquery.synth.RoutineGet] arg)))))
  ([dataset routine & opts]
   (if-not (and (string? dataset) (string? routine))
     (throw (ex-info "(get-routine dataset routine) requires string arguments"
                     {:dataset dataset :routine routine :options opts}))
     (get-routine {:routineId {:dataset dataset :routine routine}
                   :options opts}))))

(defn update-routine
  ([arg]
   (if (g/valid? :gcp/bigquery.synth.RoutineUpdate arg)
     (let [{:keys [bigquery routineInfo options]} arg
           opts ^BigQuery$RoutineOption/1 (into-array BigQuery$RoutineOption (map BQ/RoutineOption-from-edn options))]
       (Routine/to-edn (.update (client bigquery) (RoutineInfo/from-edn routineInfo) opts)))
     (if (g/valid? :gcp/bigquery.RoutineInfo arg)
       (update-routine {:routineInfo arg})
       (throw (g/human-ex-info [:or :gcp/bigquery.RoutineInfo :gcp/bigquery.synth.RoutineUpdate] arg))))))

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

(defn ^TableDataWriteChannel writer
  ([arg]
   (if (g/valid? :gcp/bigquery.synth.WriterCreate arg)
     (let [{:keys [bigquery jobId writeChannelConfiguration]} arg
           cfg (WriteChannelConfiguration/from-edn writeChannelConfiguration)]
       (if (some? jobId)
         (.writer (client bigquery) (JobId/from-edn jobId) cfg)
         (.writer (client bigquery) cfg)))
     (if (g/valid? :gcp/bigquery.WriteChannelConfiguration arg)
       (writer {:writeChannelConfiguration arg})
       (throw (g/human-ex-info [:or :gcp/bigquery.WriteChannelConfiguration :gcp/bigquery.synth.WriterCreate] arg)))))
  ([jobId writeChannelConfiguration]
   (writer {:jobId jobId
            :writeChannelConfiguration writeChannelConfiguration})))

(defn insert-all
  ([arg]
   (if (g/valid? :gcp/bigquery.InsertAllRequest arg)
     (insert-all {:request arg})
     (let [{:keys [bigquery request]} (g/coerce :gcp/bigquery.synth.InsertAll arg)
           response (.insertAll (client bigquery) (InsertAllRequest/from-edn request))]
       (InsertAllResponse/to-edn response))))
  ([arg0 arg1]
   (throw (Exception. "unimplemented")))
  ([arg0 arg1 arg2]
   (if (string? arg0)
     (if (string? arg1)
       (if (g/valid? [:seqable :map] arg2)
         (insert-all {:request {:table {:dataset arg0 :table arg1} :rows arg2}})
         (if (map? arg2)
           (insert-all arg0 arg1 [arg2])
           (throw (Exception. "unimplemented"))))
       (throw (Exception. "unimplemented")))
     (throw (Exception. "unimplemented")))))