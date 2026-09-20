(ns gcp.bigquery.core
  (:require
    [gcp.bigquery.BigQuery :as BQ]
    [gcp.bigquery.ConnectionSettings :as ConnectionSettings]
    [gcp.bigquery.DatasetId :as DatasetId]
    [gcp.bigquery.DatasetInfo :as DatasetInfo]
    [gcp.bigquery.InsertAllResponse :as InsertAllResponse]
    [gcp.bigquery.JobId :as JobId]
    [gcp.bigquery.JobInfo :as JobInfo]
    [gcp.bigquery.ModelId :as ModelId]
    [gcp.bigquery.ModelInfo :as ModelInfo]
    [gcp.bigquery.RoutineId :as RoutineId]
    [gcp.bigquery.RoutineInfo :as RoutineInfo]
    [gcp.bigquery.Schema :as Schema]
    [gcp.bigquery.TableId :as TableId]
    [gcp.bigquery.TableInfo :as TableInfo]
    [gcp.bigquery.WriteChannelConfiguration :as WriteChannelConfiguration]
    [gcp.bigquery.custom :as custom]
    [gcp.bigquery.custom.BigQueryException :as BigQueryException]
    [gcp.bigquery.custom.BigQueryOptions :as BQO]
    [gcp.bigquery.custom.BigQueryRetryConfig :as BigQueryRetryConfig]
    [gcp.bigquery.custom.Dataset :as Dataset]
    [gcp.bigquery.custom.Job :as Job]
    [gcp.bigquery.custom.Model :as Model]
    [gcp.bigquery.custom.QueryJobConfiguration :as QJC]
    [gcp.bigquery.custom.Routine :as Routine]
    [gcp.bigquery.custom.Table :as Table]
    [gcp.foreign.com.google.cloud :as cloud]
    [gcp.global :as g]
    [gcp.dwim :as dwim]
    [malli.core :as m]
    [malli.util :as mu])
  (:import
    (com.google.cloud RetryOption)
    (com.google.cloud.bigquery BigQuery BigQuery$JobField BigQuery$JobOption JobStatus$State)))

(defonce ^:dynamic *client* nil)

(defonce *clients (atom {}))

(defn client
  ([]
   (client nil))
  ([arg]
   (or *client*
       (if (instance? BigQuery arg)
         arg
         (or (get @*clients arg)
             (let [client (BQO/get-service arg)]
               (swap! *clients assoc arg client)
               client))))))

(def registry
  {:gcp.bigquery/clientable
   [:or
    (g/instance-schema com.google.cloud.bigquery.BigQuery)
    :gcp.bigquery/BigQueryOptions
    [:map [:bigquery [:or :gcp.bigquery/BigQueryOptions (g/instance-schema com.google.cloud.bigquery.BigQuery)]]]]

   :gcp.bigquery/DatasetList
   [:map {:doc "cmd definition for bq.listDatasets()"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:projectId {:optional true} :string]
    [:opts {:optional true} :gcp.bigquery/BigQuery.DatasetListOption]]

   :gcp.bigquery/DatasetGet
   [:map {:doc "cmd definition for bq.getDataset()"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:datasetId :gcp.bigquery/DatasetId]
    [:opts {:optional true} :gcp.bigquery/BigQuery.DatasetOption]]

   :gcp.bigquery/DatasetCreate
   [:map {:doc "cmd definition for bq.create(datasetInfo)"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:datasetInfo :gcp.bigquery/DatasetInfo]
    [:opts {:optional true} :gcp.bigquery/BigQuery.DatasetOption]]

   :gcp.bigquery/DatasetUpdate
   [:map {:doc "cmd definition for bq.update(datasetInfo)"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:datasetInfo :gcp.bigquery/DatasetInfo]
    [:opts {:optional true} :gcp.bigquery/BigQuery.DatasetOption]]

   :gcp.bigquery/DatasetDelete
   [:map {:doc "cmd definition for bq.delete(datasetId)"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:datasetId :gcp.bigquery/DatasetId]
    [:opts {:optional true} :gcp.bigquery/BigQuery.DatasetDeleteOption]]

   :gcp.bigquery/TableList
   [:map {:doc "cmd definition for bq.listTables(datasetId)"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:datasetId :gcp.bigquery/DatasetId]
    [:opts {:optional true} :gcp.bigquery/BigQuery.TableListOption]]

   :gcp.bigquery/TableListPartitions
   [:map {:doc "cmd definition for bq.listPartitions(tableId)"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:tableId :gcp.bigquery/TableId]]

   :gcp.bigquery/TableGet
   [:map {:doc "cmd definition for bq.getTable()"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:tableId :gcp.bigquery/TableId]
    [:opts {:optional true} :gcp.bigquery/BigQuery.TableOption]]

   :gcp.bigquery/TableCreate
   [:map {:doc "cmd definition for bq.create(tableInfo)"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:tableInfo :gcp.bigquery/TableInfo]
    [:opts {:optional true} :gcp.bigquery/BigQuery.TableOption]]

   :gcp.bigquery/TableUpdate
   [:map {:doc "cmd definition for bq.update(tableInfo)"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:tableInfo :gcp.bigquery/TableInfo]
    [:opts {:optional true} :gcp.bigquery/BigQuery.TableOption]]

   :gcp.bigquery/TableDelete
   [:map {:doc "cmd definition for bq.delete(tableId)"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:tableId :gcp.bigquery/TableId]]

   :gcp.bigquery/TableListData
   [:map {:doc "cmd definition for bq.listTableData()"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:tableId :gcp.bigquery/TableId]
    [:schema {:optional true} :gcp.bigquery/Schema]
    [:opts {:optional true} :gcp.bigquery/BigQuery.TableDataListOption]]

   :gcp.bigquery/RoutineList
   [:map {:doc "cmd definition for bq.listRoutines(datasetId)"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:datasetId :gcp.bigquery/DatasetId]
    [:opts {:optional true} :gcp.bigquery/BigQuery.RoutineListOption]]

   :gcp.bigquery/RoutineGet
   [:map {:doc "cmd definition for bq.getRoutine()"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:routineId :gcp.bigquery/RoutineId]
    [:opts {:optional true} :gcp.bigquery/BigQuery.RoutineOption]]

   :gcp.bigquery/RoutineCreate
   [:map {:doc "cmd definition for bq.create(routineInfo)"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:routineInfo :gcp.bigquery/RoutineInfo]
    [:opts {:optional true} :gcp.bigquery/BigQuery.RoutineOption]]

   :gcp.bigquery/RoutineUpdate
   [:map {:doc "cmd definition for bq.update(routineInfo)"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:routineInfo :gcp.bigquery/RoutineInfo]
    [:opts {:optional true} :gcp.bigquery/BigQuery.RoutineOption]]

   :gcp.bigquery/RoutineDelete
   [:map {:doc "cmd definition for bq.delete(routineId)"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:routineId :gcp.bigquery/RoutineId]]

   :gcp.bigquery/ModelList
   [:map {:doc "cmd definition for bq.listModels(datasetId)"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:datasetId :gcp.bigquery/DatasetId]
    [:opts {:optional true} :gcp.bigquery/BigQuery.ModelListOption]]

   :gcp.bigquery/ModelGet
   [:map {:doc "cmd definition for bq.getModel()"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:modelId :gcp.bigquery/ModelId]
    [:opts {:optional true} :gcp.bigquery/BigQuery.ModelOption]]

   :gcp.bigquery/ModelUpdate
   [:map {:doc "cmd definition for bq.update(modelInfo)"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:modelInfo :gcp.bigquery/ModelInfo]
    [:opts {:optional true} :gcp.bigquery/BigQuery.ModelOption]]

   :gcp.bigquery/ModelDelete
   [:map {:doc "cmd definition for bq.delete(modelId)"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:modelId :gcp.bigquery/ModelId]]

   :gcp.bigquery/JobList
   [:map {:doc "cmd definition for bq.listJobs()"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:opts {:optional true} :gcp.bigquery/BigQuery.JobListOption]]

   :gcp.bigquery/JobCancel
   [:map {:doc "cmd definition for bq.cancel(jobId)"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:jobId :gcp.bigquery/JobId]]

   :gcp.bigquery/JobCreate
   [:map {:doc "cmd definition for bq.create(jobInfo)"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:jobInfo :gcp.bigquery/JobInfo]
    [:opts {:optional true} :gcp.bigquery/BigQuery.JobOption]]

   :gcp.bigquery/JobGet
   [:map {:doc "cmd definition for bq.getJob(jobId)"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:jobId :gcp.bigquery/JobId]
    [:opts {:optional true} :gcp.bigquery/BigQuery.JobOption]]

   :gcp.bigquery/JobUpdate
   [:map {:doc "cmd definition for bq.update(jobInfo)"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:jobInfo :gcp.bigquery/JobInfo]
    [:opts {:optional true} :gcp.bigquery/BigQuery.JobOption]]

   :gcp.bigquery/JobDelete
   [:map {:doc "cmd definition for bq.delete(jobId)"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:jobId :gcp.bigquery/JobId]]

   :gcp.bigquery/JobWaitFor
   [:map {:doc "cmd definition for job.waitFor()"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:jobId :gcp.bigquery/JobId]
    [:retryOptions {:optional true} [:sequential :gcp.foreign.com.google.cloud/RetryOption]]
    [:bigQueryRetryConfig {:optional true} :gcp.bigquery/BigQueryRetryConfig]]

   :gcp.bigquery/JobIsDone
   [:map {:doc "cmd definition for job.isDone()"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:jobId :gcp.bigquery/JobId]]

   :gcp.bigquery/GetIamPolicy
   [:map {:doc "cmd definition for bq.getIamPolicy(tableId)"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:tableId :gcp.bigquery/TableId]
    [:opts {:optional true} :gcp.bigquery/BigQuery.IAMOption]]

   :gcp.bigquery/SetIamPolicy
   [:map {:doc "cmd definition for bq.setIamPolicy(tableId, policy)"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:tableId :gcp.bigquery/TableId]
    [:policy ::cloud/Policy]
    [:opts {:optional true} :gcp.bigquery/BigQuery.IAMOption]]

   :gcp.bigquery/TestIamPermissions
   [:map {:doc "cmd definition for bq.testIamPermissions(tableId, permissions)"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:tableId :gcp.bigquery/TableId]
    [:permissions [:sequential :string]]
    [:opts {:optional true} :gcp.bigquery/BigQuery.IAMOption]]

   :gcp.bigquery/InsertAll
   [:map {:doc "cmd definition for bq.insertAll(insertAllRequest)"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:insertAllRequest :gcp.bigquery/InsertAllRequest]]

   :gcp.bigquery/Query
   [:map {:doc "cmd definition for bq.query(configuration)"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:configuration :gcp.bigquery/QueryJobConfiguration]
    [:jobId {:optional true} :gcp.bigquery/JobId]
    [:opts {:optional true} :gcp.bigquery/BigQuery.JobOption]]

   :gcp.bigquery/QueryWithTimeout
   [:map {:doc "cmd definition for bq.queryWithTimeout(configuration, timeoutMs)"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:configuration :gcp.bigquery/QueryJobConfiguration]
    [:timeoutMs :int]
    [:jobId {:optional true} :gcp.bigquery/JobId]
    [:opts {:optional true} :gcp.bigquery/BigQuery.JobOption]]

   :gcp.bigquery/ConnectionCreate
   [:map {:doc "cmd definition for bq.createConnection()"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:connectionSettings {:optional true} :gcp.bigquery/ConnectionSettings]]

   :gcp.bigquery/Writer
   [:map {:doc "cmd definition for bq.writer()"}
    [:bigquery {:optional true} [:ref :gcp.bigquery/clientable]]
    [:writeChannelConfiguration :gcp.bigquery/WriteChannelConfiguration]
    [:jobId {:optional true} :gcp.bigquery/JobId]]})

(g/include-schema-registry! (with-meta registry {::g/name "gcp.bigquery.core"}))

(defmulti execute! :op)

#!----------------------------------------------------------------------------------------------------------------------
;; must be in [<name> [:catn [<name> <schema>]]] pattern to get recovered by key

(def ^:private list-datasets-args
  {0 [:catn]
   1 [:altn
      [:cmd        [:catn [:cmd :gcp.bigquery/DatasetList]]]
      [:project    [:catn [:projectId :string]]]
      [:opts       [:catn [:opts :gcp.bigquery/BigQuery.DatasetListOption]]]
      [:client     [:catn [:clientable :gcp.bigquery/clientable]]]]
   2 [:altn
      [:project-opts   [:catn [:projectId :string] [:opts :gcp.bigquery/BigQuery.DatasetListOption]]]
      [:client-project [:catn [:clientable :gcp.bigquery/clientable] [:projectId :string]]]
      [:client-opts    [:catn [:clientable :gcp.bigquery/clientable] [:opts :gcp.bigquery/BigQuery.DatasetListOption]]]]
   3 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:projectId :string]
      [:opts :gcp.bigquery/BigQuery.DatasetListOption]]})

(dwim/defdwim ->DatasetList
              {:facade    gcp.bigquery/list-datasets
               :cmd       :gcp.bigquery/DatasetList
               :arities   list-datasets-args
               :normalize (fn [{:keys [clientable projectId opts cmd]}]
                            (if cmd
                              (assoc cmd :op :gcp.bigquery/DatasetList)
                              {:op        :gcp.bigquery/DatasetList
                               :bigquery  clientable
                               :projectId projectId
                               :opts      opts}))})

(defmethod execute! :gcp.bigquery/DatasetList [{:keys [bigquery projectId opts]}]
  (let [client (client bigquery)
        opts (BQ/DatasetListOption-Array-from-edn opts)
        res (if projectId
              (.listDatasets client projectId opts)
              (.listDatasets client opts))]
    (map Dataset/to-edn (seq (.iterateAll res)))))

#!----------------------------------------------------------------------------------------------------------------------

(def ^:private create-datasets-args
  {1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/DatasetCreate]]]
      [:datasetId [:catn [:datasetId :gcp.bigquery/DatasetId]]]
      [:datasetInfo [:catn [:datasetInfo [:or :gcp.bigquery/DatasetInfo :gcp.bigquery/Dataset]]]]]
   2 [:altn
      [:datasetInfo-opts [:catn [:datasetInfo [:or :gcp.bigquery/DatasetInfo :gcp.bigquery/Dataset]] [:opts :gcp.bigquery/BigQuery.DatasetOption]]]
      [:client-datasetInfo [:catn [:clientable :gcp.bigquery/clientable] [:datasetInfo [:or :gcp.bigquery/DatasetInfo :gcp.bigquery/Dataset]]]]]
   3 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:datasetInfo [:or :gcp.bigquery/DatasetInfo :gcp.bigquery/Dataset]]
      [:opts :gcp.bigquery/BigQuery.DatasetOption]]})

(dwim/defdwim ->DatasetCreate
              {:facade    gcp.bigquery/create-dataset
               :cmd       :gcp.bigquery/DatasetCreate
               :arities   create-datasets-args
               :normalize (fn [{:keys [cmd datasetId datasetInfo clientable opts] :as cr}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/DatasetCreate))
                                {:op          :gcp.bigquery/DatasetCreate
                                 :bigquery    clientable
                                 :datasetInfo (or datasetInfo {:datasetId datasetId})
                                 :opts        opts}))})

(defmethod execute! :gcp.bigquery/DatasetCreate [{:keys [bigquery datasetInfo opts]}]
  (let [client (client bigquery)
        datasetInfo (DatasetInfo/from-edn datasetInfo)
        opts (BQ/DatasetOption-Array-from-edn opts)]
    (Dataset/to-edn (.create client datasetInfo opts))))

#!----------------------------------------------------------------------------------------------------------------------
;; DatasetUpdate

(def ^:private update-dataset-args
  {1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/DatasetUpdate]]]
      [:datasetInfo [:catn [:datasetInfo [:or :gcp.bigquery/DatasetInfo :gcp.bigquery/Dataset]]]]]
   2 [:altn
      [:datasetInfo-opts [:catn [:datasetInfo [:or :gcp.bigquery/DatasetInfo :gcp.bigquery/Dataset]] [:opts :gcp.bigquery/BigQuery.DatasetOption]]]
      [:client-datasetInfo [:catn [:clientable :gcp.bigquery/clientable] [:datasetInfo [:or :gcp.bigquery/DatasetInfo :gcp.bigquery/Dataset]]]]]
   3 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:datasetInfo [:or :gcp.bigquery/DatasetInfo :gcp.bigquery/Dataset]]
      [:opts :gcp.bigquery/BigQuery.DatasetOption]]})

(dwim/defdwim ->DatasetUpdate
              {:facade    gcp.bigquery/update-dataset
               :cmd       :gcp.bigquery/DatasetUpdate
               :arities   update-dataset-args
               :normalize (fn [{:keys [cmd clientable datasetInfo opts]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/DatasetUpdate))
                                {:op          :gcp.bigquery/DatasetUpdate
                                 :bigquery    clientable
                                 :datasetInfo datasetInfo
                                 :opts        opts}))})

(defmethod execute! :gcp.bigquery/DatasetUpdate [{:keys [bigquery datasetInfo opts]}]
  (let [client (client bigquery)
        datasetInfo (DatasetInfo/from-edn datasetInfo)
        opts (BQ/DatasetOption-Array-from-edn opts)]
    (Dataset/to-edn (.update client datasetInfo opts))))

#!----------------------------------------------------------------------------------------------------------------------
;; DatasetGet

(def ^:private get-dataset-args
  {1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/DatasetGet]]]
      [:datasetId [:catn [:datasetId [:or :gcp.bigquery/DatasetId :gcp.bigquery/Dataset]]]]]
   2 [:altn
      [:datasetId-opts [:catn
                        [:datasetId [:or :gcp.bigquery/DatasetId :gcp.bigquery/Dataset]]
                        [:opts :gcp.bigquery/BigQuery.DatasetOption]]]
      [:client-datasetId [:catn
                          [:clientable :gcp.bigquery/clientable]
                          [:datasetId [:or :gcp.bigquery/DatasetId :gcp.bigquery/Dataset]]]]]
   3 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:datasetId [:or :gcp.bigquery/DatasetId :gcp.bigquery/Dataset]]
      [:opts :gcp.bigquery/BigQuery.DatasetOption]]})

(dwim/defdwim ->DatasetGet
              {:facade    gcp.bigquery/get-dataset
               :cmd       :gcp.bigquery/DatasetGet
               :arities   get-dataset-args
               :normalize (fn [{:keys [cmd clientable datasetId opts]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/DatasetGet))
                                (let [resolved-dataset-id (if (:datasetId datasetId)
                                                            (:datasetId datasetId)
                                                            datasetId)]
                                  {:op        :gcp.bigquery/DatasetGet
                                   :bigquery  clientable
                                   :datasetId resolved-dataset-id
                                   :opts      opts})))})

(defmethod execute! :gcp.bigquery/DatasetGet [{:keys [bigquery datasetId opts]}]
  (let [client (client bigquery)
        datasetId (DatasetId/from-edn datasetId)
        opts (BQ/DatasetOption-Array-from-edn opts)]
    (Dataset/to-edn (.getDataset client datasetId opts))))

#!----------------------------------------------------------------------------------------------------------------------
#! DatasetDelete

(def ^:private delete-dataset-args
  {1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/DatasetDelete]]]
      [:datasetId [:catn [:datasetId [:or :gcp.bigquery/DatasetId :gcp.bigquery/Dataset]]]]]
   2 [:altn
      [:datasetId-opts [:catn
                        [:datasetId [:or :gcp.bigquery/DatasetId :gcp.bigquery/Dataset]]
                        [:opts :gcp.bigquery/BigQuery.DatasetDeleteOption]]]
      [:client-datasetId [:catn
                          [:clientable :gcp.bigquery/clientable]
                          [:datasetId [:or :gcp.bigquery/DatasetId :gcp.bigquery/Dataset]]]]]
   3 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:datasetId [:or :gcp.bigquery/DatasetId :gcp.bigquery/Dataset]]
      [:opts :gcp.bigquery/BigQuery.DatasetDeleteOption]]})

(dwim/defdwim ->DatasetDelete
              {:facade    gcp.bigquery/delete-dataset
               :cmd       :gcp.bigquery/DatasetDelete
               :arities   delete-dataset-args
               :normalize (fn [{:keys [cmd clientable datasetId opts]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/DatasetDelete))
                                (let [resolved-dataset-id (if (:datasetId datasetId)
                                                            (:datasetId datasetId)
                                                            datasetId)]
                                  {:op        :gcp.bigquery/DatasetDelete
                                   :bigquery  clientable
                                   :datasetId resolved-dataset-id
                                   :opts      opts})))})

(defmethod execute! :gcp.bigquery/DatasetDelete [{:keys [bigquery datasetId opts]}]
  (let [client (client bigquery)
        datasetId (DatasetId/from-edn datasetId)
        opts (BQ/DatasetDeleteOption-Array-from-edn opts)]
    (.delete client datasetId opts)))

#!----------------------------------------------------------------------------------------------------------------------
;; TableList

(def ^:private list-tables-args
  {1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/TableList]]]
      [:datasetId [:catn [:datasetId [:or :gcp.bigquery/DatasetId :gcp.bigquery/Dataset]]]]]
   2 [:altn
      [:project-dataset [:catn [:project string?] [:dataset string?]]]
      [:datasetId-opts [:catn [:datasetId [:or :gcp.bigquery/DatasetId :gcp.bigquery/Dataset]] [:opts :gcp.bigquery/BigQuery.TableListOption]]]
      [:client-datasetId [:catn [:clientable :gcp.bigquery/clientable] [:datasetId [:or :gcp.bigquery/DatasetId :gcp.bigquery/Dataset]]]]]
   3 [:altn
      [:project-dataset-opts [:catn [:project string?] [:dataset string?] [:opts :gcp.bigquery/BigQuery.TableListOption]]]
      [:client-project-dataset [:catn [:clientable :gcp.bigquery/clientable] [:project string?] [:dataset string?]]]
      [:client-datasetId-opts [:catn [:clientable :gcp.bigquery/clientable] [:datasetId [:or :gcp.bigquery/DatasetId :gcp.bigquery/Dataset]] [:opts :gcp.bigquery/BigQuery.TableListOption]]]]
   4 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:project string?]
      [:dataset string?]
      [:opts :gcp.bigquery/BigQuery.TableListOption]]})

(dwim/defdwim ->TableList
              {:facade    gcp.bigquery/list-tables
               :cmd       :gcp.bigquery/TableList
               :arities   list-tables-args
               :normalize (fn [{:keys [cmd clientable project dataset datasetId opts]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/TableList))
                                (let [resolved-dataset-id (or (if (:datasetId datasetId) (:datasetId datasetId) datasetId)
                                                              (cond-> {:dataset dataset}
                                                                      project (assoc :project project)))]
                                  {:op        :gcp.bigquery/TableList
                                   :bigquery  clientable
                                   :datasetId resolved-dataset-id
                                   :opts      opts})))})

(defmethod execute! :gcp.bigquery/TableList [{:keys [bigquery datasetId opts]}]
  (let [client (client bigquery)
        datasetId (DatasetId/from-edn datasetId)
        opts (BQ/TableListOption-Array-from-edn opts)
        res (.listTables client datasetId opts)]
    (map Table/Lite-to-edn (seq (.iterateAll res)))))

#!----------------------------------------------------------------------------------------------------------------------
;; TableListPartitions

(def ^:private list-partitions-args
  {1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/TableListPartitions]]]
      [:tableId [:catn [:tableId [:or :gcp.bigquery/TableId :gcp.bigquery/Table]]]]]
   2 [:altn
      [:client-tableId [:catn [:clientable :gcp.bigquery/clientable] [:tableId [:or :gcp.bigquery/TableId :gcp.bigquery/Table]]]]
      [:dataset-table [:catn [:dataset string?] [:table string?]]]]
   3 [:altn
      [:client-dataset-table [:catn [:clientable :gcp.bigquery/clientable] [:dataset string?] [:table string?]]]
      [:project-dataset-table [:catn [:project string?] [:dataset string?] [:table string?]]]]
   4 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:project string?]
      [:dataset string?]
      [:table string?]]})

(dwim/defdwim ->TableListPartitions
              {:facade    gcp.bigquery/list-partitions
               :cmd       :gcp.bigquery/TableListPartitions
               :arities   list-partitions-args
               :normalize (fn [{:keys [cmd clientable project dataset table tableId]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/TableListPartitions))
                                (let [resolved-table-id (or (if (:tableId tableId) (:tableId tableId) tableId)
                                                            (cond-> {:dataset dataset :table table}
                                                                    project (assoc :project project)))]
                                  {:op       :gcp.bigquery/TableListPartitions
                                   :bigquery clientable
                                   :tableId  resolved-table-id})))})

(defmethod execute! :gcp.bigquery/TableListPartitions [{:keys [bigquery tableId]}]
  (let [client (client bigquery)
        tableId (TableId/from-edn tableId)]
    (vec (.listPartitions client tableId))))

#!----------------------------------------------------------------------------------------------------------------------
;; TableCreate

(def ^:private create-table-args
  {1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/TableCreate]]]
      [:tableInfo [:catn [:tableInfo [:or :gcp.bigquery/TableInfo :gcp.bigquery/Table]]]]]
   2 [:altn
      [:client-tableInfo [:catn [:clientable :gcp.bigquery/clientable] [:tableInfo [:or :gcp.bigquery/TableInfo :gcp.bigquery/Table]]]]
      [:tableInfo-opts [:catn [:tableInfo [:or :gcp.bigquery/TableInfo :gcp.bigquery/Table]] [:opts :gcp.bigquery/BigQuery.TableOption]]]]
   3 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:tableInfo [:or :gcp.bigquery/TableInfo :gcp.bigquery/Table]]
      [:opts :gcp.bigquery/BigQuery.TableOption]]})

(dwim/defdwim ->TableCreate
              {:facade    gcp.bigquery/create-table
               :cmd       :gcp.bigquery/TableCreate
               :arities   create-table-args
               :normalize (fn [{:keys [cmd clientable tableInfo opts]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/TableCreate))
                                {:op        :gcp.bigquery/TableCreate
                                 :bigquery  clientable
                                 :tableInfo tableInfo
                                 :opts      opts}))})

(defmethod execute! :gcp.bigquery/TableCreate [{:keys [bigquery tableInfo opts]}]
  (let [client (client bigquery)
        tableInfo (TableInfo/from-edn tableInfo)
        opts (BQ/TableOption-Array-from-edn opts)]
    (Table/to-edn (.create client tableInfo opts))))

#!----------------------------------------------------------------------------------------------------------------------
;; TableUpdate

(def ^:private update-table-args
  {1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/TableUpdate]]]
      [:tableInfo [:catn [:tableInfo [:or :gcp.bigquery/TableInfo :gcp.bigquery/Table]]]]]
   2 [:altn
      [:client-tableInfo [:catn [:clientable :gcp.bigquery/clientable] [:tableInfo [:or :gcp.bigquery/TableInfo :gcp.bigquery/Table]]]]
      [:tableInfo-opts [:catn [:tableInfo [:or :gcp.bigquery/TableInfo :gcp.bigquery/Table]] [:opts :gcp.bigquery/BigQuery.TableOption]]]]
   3 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:tableInfo [:or :gcp.bigquery/TableInfo :gcp.bigquery/Table]]
      [:opts :gcp.bigquery/BigQuery.TableOption]]})

(dwim/defdwim ->TableUpdate
              {:facade    gcp.bigquery/update-table
               :cmd       :gcp.bigquery/TableUpdate
               :arities   update-table-args
               :normalize (fn [{:keys [cmd clientable tableInfo opts]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/TableUpdate))
                                {:op        :gcp.bigquery/TableUpdate
                                 :bigquery  clientable
                                 :tableInfo tableInfo
                                 :opts      opts}))})

(defmethod execute! :gcp.bigquery/TableUpdate [{:keys [bigquery tableInfo opts]}]
  (let [client (client bigquery)
        tableInfo (TableInfo/from-edn tableInfo)
        opts (BQ/TableOption-Array-from-edn opts)]
    (Table/to-edn (.update client tableInfo opts))))

#!----------------------------------------------------------------------------------------------------------------------
;; TableGet

(def ^:private get-table-args
  {1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/TableGet]]]
      [:tableId [:catn [:tableId [:or :gcp.bigquery/TableId :gcp.bigquery/Table]]]]]
   2 [:altn
      [:dataset-table [:catn [:dataset string?] [:table string?]]]
      [:tableId-opts [:catn [:tableId [:or :gcp.bigquery/TableId :gcp.bigquery/Table]] [:opts :gcp.bigquery/BigQuery.TableOption]]]
      [:client-tableId [:catn [:clientable :gcp.bigquery/clientable] [:tableId [:or :gcp.bigquery/TableId :gcp.bigquery/Table]]]]]
   3 [:altn
      [:project-dataset-table [:catn [:project string?] [:dataset string?] [:table string?]]]
      [:dataset-table-opts [:catn [:dataset string?] [:table string?] [:opts :gcp.bigquery/BigQuery.TableOption]]]
      [:client-dataset-table [:catn [:clientable :gcp.bigquery/clientable] [:dataset string?] [:table string?]]]
      [:client-tableId-opts [:catn [:clientable :gcp.bigquery/clientable] [:tableId [:or :gcp.bigquery/TableId :gcp.bigquery/Table]] [:opts :gcp.bigquery/BigQuery.TableOption]]]]
   4 [:altn
      [:project-dataset-table-opts [:catn [:project string?] [:dataset string?] [:table string?] [:opts :gcp.bigquery/BigQuery.TableOption]]]
      [:client-project-dataset-table [:catn [:clientable :gcp.bigquery/clientable] [:project string?] [:dataset string?] [:table string?]]]
      [:client-dataset-table-opts [:catn [:clientable :gcp.bigquery/clientable] [:dataset string?] [:table string?] [:opts :gcp.bigquery/BigQuery.TableOption]]]]
   5 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:project string?]
      [:dataset string?]
      [:table string?]
      [:opts :gcp.bigquery/BigQuery.TableOption]]})

(dwim/defdwim ->TableGet
              {:facade    gcp.bigquery/get-table
               :cmd       :gcp.bigquery/TableGet
               :arities   get-table-args
               :normalize (fn [{:keys [cmd clientable project dataset table tableId opts]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/TableGet))
                                (let [resolved-table-id (or (if (:tableId tableId) (:tableId tableId) tableId)
                                                            (cond-> {:dataset dataset :table table}
                                                                    project (assoc :project project)))]
                                  {:op       :gcp.bigquery/TableGet
                                   :bigquery clientable
                                   :tableId  resolved-table-id
                                   :opts     opts})))})

(defmethod execute! :gcp.bigquery/TableGet [{:keys [bigquery tableId opts]}]
  (let [client (client bigquery)
        tableId (TableId/from-edn tableId)
        opts (BQ/TableOption-Array-from-edn opts)]
    (Table/to-edn (.getTable client tableId opts))))

#!----------------------------------------------------------------------------------------------------------------------
;; TableDelete

(def ^:private delete-table-args
  {1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/TableDelete]]]
      [:tableId [:catn [:tableId [:or :gcp.bigquery/TableId :gcp.bigquery/Table]]]]]
   2 [:altn
      [:client-tableId [:catn [:clientable :gcp.bigquery/clientable] [:tableId [:or :gcp.bigquery/TableId :gcp.bigquery/Table]]]]
      [:dataset-table [:catn [:dataset string?] [:table string?]]]]
   3 [:altn
      [:client-dataset-table [:catn [:clientable :gcp.bigquery/clientable] [:dataset string?] [:table string?]]]
      [:project-dataset-table [:catn [:project string?] [:dataset string?] [:table string?]]]]
   4 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:project string?]
      [:dataset string?]
      [:table string?]]})

(dwim/defdwim ->TableDelete
              {:facade    gcp.bigquery/delete-table
               :cmd       :gcp.bigquery/TableDelete
               :arities   delete-table-args
               :normalize (fn [{:keys [cmd clientable project dataset table tableId]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/TableDelete))
                                (let [resolved-table-id (or (if (:tableId tableId) (:tableId tableId) tableId)
                                                            (cond-> {:dataset dataset :table table}
                                                                    project (assoc :project project)))]
                                  {:op       :gcp.bigquery/TableDelete
                                   :bigquery clientable
                                   :tableId  resolved-table-id})))})

(defmethod execute! :gcp.bigquery/TableDelete [{:keys [bigquery tableId]}]
  (let [client (client bigquery)
        tableId (TableId/from-edn tableId)]
    (.delete client tableId)))

#!----------------------------------------------------------------------------------------------------------------------
;; TableListData

(def ^:private list-table-data-args
  {1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/TableListData]]]
      [:tableId [:catn [:tableId [:or :gcp.bigquery/TableId :gcp.bigquery/Table]]]]]
   2 [:altn
      [:client-tableId [:catn [:clientable :gcp.bigquery/clientable] [:tableId [:or :gcp.bigquery/TableId :gcp.bigquery/Table]]]]
      [:dataset-table [:catn [:dataset string?] [:table string?]]]
      [:tableId-schema [:catn [:tableId [:or :gcp.bigquery/TableId :gcp.bigquery/Table]] [:schema :gcp.bigquery/Schema]]]
      [:tableId-opts [:catn [:tableId [:or :gcp.bigquery/TableId :gcp.bigquery/Table]] [:opts :gcp.bigquery/BigQuery.TableDataListOption]]]]
   3 [:altn
      [:client-dataset-table [:catn [:clientable :gcp.bigquery/clientable] [:dataset string?] [:table string?]]]
      [:client-tableId-schema [:catn [:clientable :gcp.bigquery/clientable] [:tableId [:or :gcp.bigquery/TableId :gcp.bigquery/Table]] [:schema :gcp.bigquery/Schema]]]
      [:client-tableId-opts [:catn [:clientable :gcp.bigquery/clientable] [:tableId [:or :gcp.bigquery/TableId :gcp.bigquery/Table]] [:opts :gcp.bigquery/BigQuery.TableDataListOption]]]
      [:project-dataset-table [:catn [:project string?] [:dataset string?] [:table string?]]]
      [:dataset-table-schema [:catn [:dataset string?] [:table string?] [:schema :gcp.bigquery/Schema]]]
      [:dataset-table-opts [:catn [:dataset string?] [:table string?] [:opts :gcp.bigquery/BigQuery.TableDataListOption]]]
      [:tableId-schema-opts [:catn [:tableId [:or :gcp.bigquery/TableId :gcp.bigquery/Table]] [:schema :gcp.bigquery/Schema] [:opts :gcp.bigquery/BigQuery.TableDataListOption]]]]
   4 [:altn
      [:client-project-dataset-table [:catn [:clientable :gcp.bigquery/clientable] [:project string?] [:dataset string?] [:table string?]]]
      [:client-dataset-table-schema [:catn [:clientable :gcp.bigquery/clientable] [:dataset string?] [:table string?] [:schema :gcp.bigquery/Schema]]]
      [:client-dataset-table-opts [:catn [:clientable :gcp.bigquery/clientable] [:dataset string?] [:table string?] [:opts :gcp.bigquery/BigQuery.TableDataListOption]]]
      [:client-tableId-schema-opts [:catn [:clientable :gcp.bigquery/clientable] [:tableId [:or :gcp.bigquery/TableId :gcp.bigquery/Table]] [:schema :gcp.bigquery/Schema] [:opts :gcp.bigquery/BigQuery.TableDataListOption]]]
      [:project-dataset-table-schema [:catn [:project string?] [:dataset string?] [:table string?] [:schema :gcp.bigquery/Schema]]]
      [:project-dataset-table-opts [:catn [:project string?] [:dataset string?] [:table string?] [:opts :gcp.bigquery/BigQuery.TableDataListOption]]]
      [:dataset-table-schema-opts [:catn [:dataset string?] [:table string?] [:schema :gcp.bigquery/Schema] [:opts :gcp.bigquery/BigQuery.TableDataListOption]]]]
   5 [:altn
      [:client-project-dataset-table-schema [:catn [:clientable :gcp.bigquery/clientable] [:project string?] [:dataset string?] [:table string?] [:schema :gcp.bigquery/Schema]]]
      [:client-project-dataset-table-opts [:catn [:clientable :gcp.bigquery/clientable] [:project string?] [:dataset string?] [:table string?] [:opts :gcp.bigquery/BigQuery.TableDataListOption]]]
      [:client-dataset-table-schema-opts [:catn [:clientable :gcp.bigquery/clientable] [:dataset string?] [:table string?] [:schema :gcp.bigquery/Schema] [:opts :gcp.bigquery/BigQuery.TableDataListOption]]]
      [:project-dataset-table-schema-opts [:catn [:project string?] [:dataset string?] [:table string?] [:schema :gcp.bigquery/Schema] [:opts :gcp.bigquery/BigQuery.TableDataListOption]]]]
   6 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:project string?]
      [:dataset string?]
      [:table string?]
      [:schema :gcp.bigquery/Schema]
      [:opts :gcp.bigquery/BigQuery.TableDataListOption]]})

(dwim/defdwim ->TableListData
              {:facade    gcp.bigquery/list-table-data
               :cmd       :gcp.bigquery/TableListData
               :arities   list-table-data-args
               :normalize (fn [{:keys [cmd clientable project dataset table tableId schema opts]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/TableListData))
                                (let [resolved-table-id (or (if (:tableId tableId) (:tableId tableId) tableId)
                                                            (cond-> {:dataset dataset :table table}
                                                                    project (assoc :project project)))]
                                  {:op       :gcp.bigquery/TableListData
                                   :bigquery clientable
                                   :tableId  resolved-table-id
                                   :schema   schema
                                   :opts     opts})))})

(defmethod execute! :gcp.bigquery/TableListData [{:keys [bigquery tableId schema opts]}]
  (let [client (client bigquery)
        tableId (TableId/from-edn tableId)
        opts (BQ/TableDataListOption-Array-from-edn opts)
        res (if-some [schema (some-> schema Schema/from-edn)]
              (.listTableData client tableId schema opts)
              (.listTableData client tableId opts))]
    (custom/TableResult-to-edn res)))

#!----------------------------------------------------------------------------------------------------------------------
;; InsertAll

(def ^:private insert-all-args
  {1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/InsertAll]]]
      [:insertAllRequest [:catn [:insertAllRequest :gcp.bigquery/InsertAllRequest]]]]
   2 [:altn
      [:client-request [:catn [:clientable :gcp.bigquery/clientable] [:insertAllRequest :gcp.bigquery/InsertAllRequest]]]
      [:tableId-rows [:catn [:tableId [:or :gcp.bigquery/TableId :gcp.bigquery/Table]] [:rows [:sequential {:min 1} :gcp.bigquery/InsertAllRequest$RowToInsert]]]]]
   3 [:altn
      [:client-tableId-rows [:catn [:clientable :gcp.bigquery/clientable] [:tableId [:or :gcp.bigquery/TableId :gcp.bigquery/Table]] [:rows [:sequential {:min 1} :gcp.bigquery/InsertAllRequest$RowToInsert]]]]
      [:dataset-table-rows [:catn [:dataset string?] [:table string?] [:rows [:sequential {:min 1} :gcp.bigquery/InsertAllRequest$RowToInsert]]]]]
   4 [:altn
      [:client-dataset-table-rows [:catn [:clientable :gcp.bigquery/clientable] [:dataset string?] [:table string?] [:rows [:sequential {:min 1} :gcp.bigquery/InsertAllRequest$RowToInsert]]]]
      [:project-dataset-table-rows [:catn [:project string?] [:dataset string?] [:table string?] [:rows [:sequential {:min 1} :gcp.bigquery/InsertAllRequest$RowToInsert]]]]]
   5 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:project string?]
      [:dataset string?]
      [:table string?]
      [:rows [:sequential {:min 1} :gcp.bigquery/InsertAllRequest$RowToInsert]]]})

(dwim/defdwim ->InsertAll
              {:facade    gcp.bigquery/insert-all
               :cmd       :gcp.bigquery/InsertAll
               :arities   insert-all-args
               :normalize (fn [{:keys [cmd clientable project dataset table tableId rows insertAllRequest]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/InsertAll))
                                (let [resolved-table-id (or (if (:tableId tableId) (:tableId tableId) tableId)
                                                            (cond-> {:dataset dataset :table table}
                                                                    project (assoc :project project)))
                                      request           (or insertAllRequest {:table resolved-table-id :rows rows})]
                                  {:op               :gcp.bigquery/InsertAll
                                   :bigquery         clientable
                                   :insertAllRequest request})))})

(defmethod execute! :gcp.bigquery/InsertAll [{:keys [insertAllRequest bigquery]}]
  (let [client (client bigquery)
        request (custom/InsertAllRequest-from-edn insertAllRequest)]
    (InsertAllResponse/to-edn (.insertAll client request))))

#!----------------------------------------------------------------------------------------------------------------------
;; JobList

(def ^:private list-jobs-args
  {0 [:catn]
   1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/JobList]]]
      [:client [:catn [:clientable :gcp.bigquery/clientable]]]
      [:opts [:catn [:opts :gcp.bigquery/BigQuery.JobListOption]]]]
   2 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:opts :gcp.bigquery/BigQuery.JobListOption]]})

(dwim/defdwim ->JobList
              {:facade    gcp.bigquery/list-jobs
               :cmd       :gcp.bigquery/JobList
               :arities   list-jobs-args
               :normalize (fn [{:keys [cmd clientable opts]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/JobList))
                                {:op       :gcp.bigquery/JobList
                                 :bigquery clientable
                                 :opts     opts}))})

(defmethod execute! :gcp.bigquery/JobList [{:keys [bigquery opts]}]
  (let [client (client bigquery)
        opts (BQ/JobListOption-Array-from-edn opts)
        res (.listJobs client opts)]
    (binding [g/*strict-mode* false]
      (map (bound-fn [j] (Job/to-edn j)) (seq (.iterateAll res))))))

#!----------------------------------------------------------------------------------------------------------------------
;; JobCancel

(def ^:private cancel-job-args
  {1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/JobCancel]]]
      [:jobId [:catn [:jobId [:or string? :gcp.bigquery/JobId :gcp.bigquery/Job]]]]]
   2 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:jobId [:or string? :gcp.bigquery/JobId :gcp.bigquery/Job]]]})

(dwim/defdwim ->JobCancel
              {:facade    gcp.bigquery/cancel-job
               :cmd       :gcp.bigquery/JobCancel
               :arities   cancel-job-args
               :normalize (fn [{:keys [cmd clientable jobId]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/JobCancel))
                                (let [resolved-job-id (cond
                                                        (string? jobId) {:job jobId}
                                                        (:jobId jobId) (:jobId jobId)
                                                        :else jobId)]
                                  {:op       :gcp.bigquery/JobCancel
                                   :bigquery clientable
                                   :jobId    resolved-job-id})))})

(defmethod execute! :gcp.bigquery/JobList [{:keys [bigquery opts]}]
  (let [client (client bigquery)
        opts (BQ/JobListOption-Array-from-edn opts)
        res (.listJobs client opts)]
    (binding [g/*strict-mode* false]
      (map (bound-fn [j] (Job/to-edn j)) (seq (.iterateAll res))))))

#!----------------------------------------------------------------------------------------------------------------------
;; JobCreate

(def ^:private create-job-args
  {1 [:altn
      [:cmd [:catn [:cmd (mu/dissoc :gcp.bigquery/JobCreate :op (g/mopts))]]]
      [:jobInfo [:catn [:jobInfo [:or :gcp.bigquery/JobInfo :gcp.bigquery/JobConfiguration]]]]]
   2 [:altn
      [:client-jobInfo [:catn [:clientable :gcp.bigquery/clientable] [:jobInfo [:or :gcp.bigquery/JobInfo :gcp.bigquery/JobConfiguration]]]]
      [:jobInfo-opts [:catn [:jobInfo [:or :gcp.bigquery/JobInfo :gcp.bigquery/JobConfiguration]] [:opts :gcp.bigquery/BigQuery.JobOption]]]]
   3 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:jobInfo [:or :gcp.bigquery/JobInfo :gcp.bigquery/JobConfiguration]]
      [:opts :gcp.bigquery/BigQuery.JobOption]]})

(dwim/defdwim ->JobCreate
  {:facade    gcp.bigquery/create-job
   :cmd       :gcp.bigquery/JobCreate
   :arities   create-job-args
   :normalize (fn [{:keys [cmd clientable jobInfo opts]}]
                 (or (some-> cmd (assoc :op :gcp.bigquery/JobCreate))
                     (let [jobInfo (if (:configuration jobInfo)
                                     jobInfo
                                     {:configuration jobInfo})]
                       {:op       :gcp.bigquery/JobCreate
                        :bigquery clientable
                        :jobInfo  jobInfo
                        :opts     opts})))})

(defmethod execute! :gcp.bigquery/JobCancel [{:keys [bigquery jobId]}]
  (let [client (client bigquery)
        jobId (JobId/from-edn jobId)]
    (.cancel client jobId)))

#!----------------------------------------------------------------------------------------------------------------------
;; JobGet

(def ^:private get-job-args
  {1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/JobGet]]]
      [:jobId [:catn [:jobId [:or string? :gcp.bigquery/JobId :gcp.bigquery/Job]]]]]
   2 [:altn
      [:jobId-opts [:catn [:jobId [:or string? :gcp.bigquery/JobId :gcp.bigquery/Job]] [:opts :gcp.bigquery/BigQuery.JobOption]]]
      [:client-jobId [:catn [:clientable :gcp.bigquery/clientable] [:jobId [:or string? :gcp.bigquery/JobId :gcp.bigquery/Job]]]]]
   3 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:jobId [:or string? :gcp.bigquery/JobId :gcp.bigquery/Job]]
      [:opts :gcp.bigquery/BigQuery.JobOption]]})

(dwim/defdwim ->JobGet
              {:facade    gcp.bigquery/get-job
               :cmd       :gcp.bigquery/JobGet
               :arities   get-job-args
               :normalize (fn [{:keys [cmd clientable jobId opts]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/JobGet))
                                (let [resolved-job-id (cond
                                                        (string? jobId) {:job jobId}
                                                        (:jobId jobId) (:jobId jobId)
                                                        :else jobId)]
                                  {:op       :gcp.bigquery/JobGet
                                   :bigquery clientable
                                   :jobId    resolved-job-id
                                   :opts     opts})))})

(defmethod execute! :gcp.bigquery/JobGet [{:keys [bigquery jobId opts]}]
  (let [client (client bigquery)
        jobId (JobId/from-edn jobId)
        opts (BQ/JobOption-Array-from-edn opts)]
    (binding [g/*strict-mode* false]
      (Job/to-edn (.getJob client jobId opts)))))

#!----------------------------------------------------------------------------------------------------------------------
;; JobDelete

(def ^:private delete-job-args
  {1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/JobDelete]]]
      [:jobId [:catn [:jobId [:or string? :gcp.bigquery/JobId :gcp.bigquery/Job]]]]]
   2 [:altn
      [:jobId [:catn [:jobId [:or string? :gcp.bigquery/JobId :gcp.bigquery/Job]]]]
      [:client-jobId [:catn [:clientable :gcp.bigquery/clientable] [:jobId [:or string? :gcp.bigquery/JobId :gcp.bigquery/Job]]]]]})

(dwim/defdwim ->JobDelete
              {:facade    gcp.bigquery/delete-job
               :cmd       :gcp.bigquery/JobDelete
               :arities   delete-job-args
               :normalize (fn [{:keys [cmd clientable jobId]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/JobDelete))
                                (let [resolved-job-id (cond
                                                        (string? jobId) {:job jobId}
                                                        (:jobId jobId) (:jobId jobId)
                                                        :else jobId)]
                                  {:op       :gcp.bigquery/JobDelete
                                   :bigquery clientable
                                   :jobId    resolved-job-id})))})

(defmethod execute! :gcp.bigquery/JobDelete [{:keys [bigquery jobId]}]
  (let [client (client bigquery)
        jobId (JobId/from-edn jobId)]
    (.delete client jobId)))

#!----------------------------------------------------------------------------------------------------------------------
;; JobWaitFor

(def ^:private wait-for-job-args
  {1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/JobWaitFor]]]
      [:jobId [:catn [:jobId [:or string? :gcp.bigquery/JobId :gcp.bigquery/Job]]]]]
   2 [:altn
      [:jobId-opts [:catn [:jobId [:or string? :gcp.bigquery/JobId :gcp.bigquery/Job]] [:opts [:map {:closed true} [:retryOptions {:optional true} [:sequential :gcp.foreign.com.google.cloud/RetryOption]] [:bigQueryRetryConfig {:optional true} :gcp.bigquery/BigQueryRetryConfig]]]]]
      [:client-jobId [:catn [:clientable :gcp.bigquery/clientable] [:jobId [:or string? :gcp.bigquery/JobId :gcp.bigquery/Job]]]]]
   3 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:jobId [:or string? :gcp.bigquery/JobId :gcp.bigquery/Job]]
      [:opts [:map {:closed true} [:retryOptions {:optional true} [:sequential :gcp.foreign.com.google.cloud/RetryOption]] [:bigQueryRetryConfig {:optional true} :gcp.bigquery/BigQueryRetryConfig]]]]})

(dwim/defdwim ->JobWaitFor
              {:facade    gcp.bigquery/wait-for
               :cmd       :gcp.bigquery/JobWaitFor
               :arities   wait-for-job-args
               :normalize (fn [{:keys [cmd clientable jobId opts]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/JobWaitFor))
                                (let [resolved-job-id (cond
                                                        (string? jobId) {:job jobId}
                                                        (:jobId jobId) (:jobId jobId)
                                                        :else jobId)]
                                  (cond-> {:op       :gcp.bigquery/JobWaitFor
                                           :bigquery clientable
                                           :jobId    resolved-job-id}
                                          (:retryOptions opts) (assoc :retryOptions (:retryOptions opts))
                                          (:bigQueryRetryConfig opts) (assoc :bigQueryRetryConfig (:bigQueryRetryConfig opts))))))})

(defmethod execute! :gcp.bigquery/JobWaitFor [{:keys [bigquery jobId retryOptions bigQueryRetryConfig]}]
  (let [client (client bigquery)
        jobId (JobId/from-edn jobId)
        job (.getJob client jobId (make-array BigQuery$JobOption 0))]
    (when job
      (let [retry-opts (when retryOptions
                         (into-array RetryOption
                                     (map cloud/RetryOption-from-edn retryOptions)))
            bq-retry-config (when bigQueryRetryConfig
                              (BigQueryRetryConfig/from-edn bigQueryRetryConfig))]
        (try
          (let [job (cond
                      (and retry-opts bq-retry-config)
                      (.waitFor job bq-retry-config retry-opts)

                      retry-opts
                      (.waitFor job retry-opts)

                      bq-retry-config
                      (.waitFor job bq-retry-config (make-array RetryOption 0))

                      :else
                      (.waitFor job (make-array RetryOption 0)))]
            (binding [g/*strict-mode* false] (Job/to-edn job)))
          (catch com.google.cloud.bigquery.BigQueryException e
            (throw (BigQueryException/to-edn e))))))))

#!----------------------------------------------------------------------------------------------------------------------
;; JobIsDone

(def ^:private is-done-job-args
  {1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/JobIsDone]]]
      [:jobId [:catn [:jobId [:or string? :gcp.bigquery/JobId :gcp.bigquery/Job]]]]]
   2 [:altn
      [:client-jobId [:catn [:clientable :gcp.bigquery/clientable] [:jobId [:or string? :gcp.bigquery/JobId :gcp.bigquery/Job]]]]
      [:jobId-opts [:catn [:jobId [:or string? :gcp.bigquery/JobId :gcp.bigquery/Job]] [:opts [:map {:closed true} [:bigQueryRetryConfig {:optional true} :gcp.bigquery/BigQueryRetryConfig]]]]]]
   3 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:jobId [:or string? :gcp.bigquery/JobId :gcp.bigquery/Job]]
      [:opts [:map {:closed true} [:bigQueryRetryConfig {:optional true} :gcp.bigquery/BigQueryRetryConfig]]]]})

(dwim/defdwim ->JobIsDone
              {:facade    gcp.bigquery/is-done
               :cmd       :gcp.bigquery/JobIsDone
               :arities   is-done-job-args
               :normalize (fn [{:keys [cmd clientable jobId opts]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/JobIsDone))
                                (let [resolved-job-id (cond
                                                        (string? jobId) {:job jobId}
                                                        (:jobId jobId) (:jobId jobId)
                                                        :else jobId)]
                                  (cond-> {:op       :gcp.bigquery/JobIsDone
                                           :bigquery clientable
                                           :jobId    resolved-job-id}
                                          (:bigQueryRetryConfig opts) (assoc :bigQueryRetryConfig (:bigQueryRetryConfig opts))))))})

(defmethod execute! :gcp.bigquery/JobIsDone [{:keys [bigquery jobId]}]
  (let [client (client bigquery)
        jobId (JobId/from-edn jobId)
        job (.getJob client jobId (into-array BigQuery$JobOption
                                              [(BigQuery$JobOption/fields
                                                 (into-array BigQuery$JobField
                                                             [BigQuery$JobField/STATUS]))]))]
    (if job
      (if-let [status (.getStatus job)]
        (= JobStatus$State/DONE (.getState status))
        false)
      true)))

#!----------------------------------------------------------------------------------------------------------------------
;; RoutineList

(def ^:private list-routines-args
  {1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/RoutineList]]]
      [:datasetId [:catn [:datasetId :gcp.bigquery/DatasetId]]]]
   2 [:altn
      [:project-dataset [:catn [:project string?] [:dataset string?]]]
      [:client-datasetId [:catn [:clientable :gcp.bigquery/clientable] [:datasetId :gcp.bigquery/DatasetId]]]
      [:datasetId-opts [:catn [:datasetId :gcp.bigquery/DatasetId] [:opts :gcp.bigquery/BigQuery.RoutineListOption]]]]
   3 [:altn
      [:client-project-dataset [:catn [:clientable :gcp.bigquery/clientable] [:project string?] [:dataset string?]]]
      [:client-datasetId-opts [:catn [:clientable :gcp.bigquery/clientable] [:datasetId :gcp.bigquery/DatasetId] [:opts :gcp.bigquery/BigQuery.RoutineListOption]]]
      [:project-dataset-opts [:catn [:project string?] [:dataset string?] [:opts :gcp.bigquery/BigQuery.RoutineListOption]]]]
   4 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:project string?]
      [:dataset string?]
      [:opts :gcp.bigquery/BigQuery.RoutineListOption]]})

(dwim/defdwim ->RoutineList
              {:facade    gcp.bigquery/list-routines
               :cmd       :gcp.bigquery/RoutineList
               :arities   list-routines-args
               :normalize (fn [{:keys [cmd clientable project dataset datasetId opts]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/RoutineList))
                                (let [resolved-dataset-id (or datasetId
                                                              (cond-> {:dataset dataset}
                                                                      project (assoc :project project)))]
                                  {:op        :gcp.bigquery/RoutineList
                                   :bigquery  clientable
                                   :datasetId resolved-dataset-id
                                   :opts      opts})))})

(defmethod execute! :gcp.bigquery/RoutineList [{:keys [bigquery datasetId opts]}]
  (let [client (client bigquery)
        datasetId (DatasetId/from-edn datasetId)
        opts (BQ/RoutineListOption-Array-from-edn opts)
        res (.listRoutines client datasetId opts)]
    (map Routine/to-edn (seq (.iterateAll res)))))

#!----------------------------------------------------------------------------------------------------------------------
;; RoutineCreate / RoutineUpdate (shared schema)

(def ^:private routine-create-args
  {1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/RoutineCreate]]]
      [:routineInfo [:catn [:routineInfo [:or :gcp.bigquery/RoutineInfo :gcp.bigquery/Routine]]]]]
   2 [:altn
      [:client-routineInfo [:catn [:clientable :gcp.bigquery/clientable] [:routineInfo [:or :gcp.bigquery/RoutineInfo :gcp.bigquery/Routine]]]]
      [:routineInfo-opts [:catn [:routineInfo [:or :gcp.bigquery/RoutineInfo :gcp.bigquery/Routine]] [:opts :gcp.bigquery/BigQuery.RoutineOption]]]]
   3 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:routineInfo [:or :gcp.bigquery/RoutineInfo :gcp.bigquery/Routine]]
      [:opts :gcp.bigquery/BigQuery.RoutineOption]]})

(dwim/defdwim ->RoutineCreate
              {:facade  gcp.bigquery/create-routine
               :cmd :gcp.bigquery/RoutineCreate
               :arities routine-create-args
               :normalize (fn [{:keys [cmd clientable routineInfo opts]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/RoutineCreate))
                                {:op          :gcp.bigquery/RoutineCreate
                                 :bigquery    clientable
                                 :routineInfo routineInfo
                                 :opts        opts}))})

(defmethod execute! :gcp.bigquery/RoutineCreate [{:keys [bigquery routineInfo opts]}]
  (let [client (client bigquery)
        routineInfo (RoutineInfo/from-edn routineInfo)
        opts (BQ/RoutineOption-Array-from-edn opts)]
    (Routine/to-edn (.create client routineInfo opts))))

#!----------------------------------------------------------------------------------------------------------------------
;; RoutineUpdate

(def ^:private routine-update-args
  {1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/RoutineUpdate]]]
      [:routineInfo [:catn [:routineInfo [:or :gcp.bigquery/RoutineInfo :gcp.bigquery/Routine]]]]]
   2 [:altn
      [:client-routineInfo [:catn [:clientable :gcp.bigquery/clientable] [:routineInfo [:or :gcp.bigquery/RoutineInfo :gcp.bigquery/Routine]]]]
      [:routineInfo-opts [:catn [:routineInfo [:or :gcp.bigquery/RoutineInfo :gcp.bigquery/Routine]] [:opts :gcp.bigquery/BigQuery.RoutineOption]]]]
   3 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:routineInfo [:or :gcp.bigquery/RoutineInfo :gcp.bigquery/Routine]]
      [:opts :gcp.bigquery/BigQuery.RoutineOption]]})

(dwim/defdwim ->RoutineUpdate
              {:facade  gcp.bigquery/update-routine
               :cmd :gcp.bigquery/RoutineUpdate
               :arities routine-update-args
               :normalize (fn [{:keys [cmd clientable routineInfo opts]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/RoutineUpdate))
                                {:op          :gcp.bigquery/RoutineUpdate
                                 :bigquery    clientable
                                 :routineInfo routineInfo
                                 :opts        opts}))})

(defmethod execute! :gcp.bigquery/RoutineUpdate [{:keys [bigquery routineInfo opts]}]
  (let [client (client bigquery)
        routineInfo (RoutineInfo/from-edn routineInfo)
        opts (BQ/RoutineOption-Array-from-edn opts)]
    (Routine/to-edn (.update client routineInfo opts))))

#!----------------------------------------------------------------------------------------------------------------------
;; RoutineGet

(def ^:private get-routine-args
  {1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/RoutineGet]]]
      [:routineId [:catn [:routineId [:or :gcp.bigquery/RoutineId :gcp.bigquery/Routine]]]]]
   2 [:altn
      [:client-routineId [:catn [:clientable :gcp.bigquery/clientable] [:routineId [:or :gcp.bigquery/RoutineId :gcp.bigquery/Routine]]]]
      [:dataset-routine [:catn [:dataset string?] [:routine string?]]]
      [:routineId-opts [:catn [:routineId [:or :gcp.bigquery/RoutineId :gcp.bigquery/Routine]] [:opts :gcp.bigquery/BigQuery.RoutineOption]]]]
   3 [:altn
      [:client-dataset-routine [:catn [:clientable :gcp.bigquery/clientable] [:dataset string?] [:routine string?]]]
      [:client-routineId-opts [:catn [:clientable :gcp.bigquery/clientable] [:routineId [:or :gcp.bigquery/RoutineId :gcp.bigquery/Routine]] [:opts :gcp.bigquery/BigQuery.RoutineOption]]]
      [:project-dataset-routine [:catn [:project string?] [:dataset string?] [:routine string?]]]
      [:dataset-routine-opts [:catn [:dataset string?] [:routine string?] [:opts :gcp.bigquery/BigQuery.RoutineOption]]]]
   4 [:altn
      [:client-project-dataset-routine [:catn [:clientable :gcp.bigquery/clientable] [:project string?] [:dataset string?] [:routine string?]]]
      [:client-dataset-routine-opts [:catn [:clientable :gcp.bigquery/clientable] [:dataset string?] [:routine string?] [:opts :gcp.bigquery/BigQuery.RoutineOption]]]]
   5 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:project string?]
      [:dataset string?]
      [:routine string?]
      [:opts :gcp.bigquery/BigQuery.RoutineOption]]})

(dwim/defdwim ->RoutineGet
              {:facade  gcp.bigquery/get-routine
               :cmd :gcp.bigquery/RoutineGet
               :arities get-routine-args
               :normalize (fn [{:keys [cmd clientable project dataset routine routineId opts]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/RoutineGet))
                                (let [resolved-routine-id (or (if (:routineId routineId) (:routineId routineId) routineId)
                                                              (cond-> {:dataset dataset :routine routine}
                                                                      project (assoc :project project)))]
                                  {:op        :gcp.bigquery/RoutineGet
                                   :bigquery  clientable
                                   :routineId resolved-routine-id
                                   :opts      opts})))})

(defmethod execute! :gcp.bigquery/RoutineGet [{:keys [bigquery routineId opts]}]
  (let [client (client bigquery)
        routineId (RoutineId/from-edn routineId)
        opts (BQ/RoutineOption-Array-from-edn opts)]
    (Routine/to-edn (.getRoutine client routineId opts))))

#!----------------------------------------------------------------------------------------------------------------------
;; RoutineDelete

(def ^:private delete-routine-args
  {1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/RoutineDelete]]]
      [:routineId [:catn [:routineId [:or :gcp.bigquery/RoutineId :gcp.bigquery/Routine]]]]]
   2 [:altn
      [:client-routineId [:catn [:clientable :gcp.bigquery/clientable] [:routineId [:or :gcp.bigquery/RoutineId :gcp.bigquery/Routine]]]]
      [:dataset-routine [:catn [:dataset string?] [:routine string?]]]]
   3 [:altn
      [:client-dataset-routine [:catn [:clientable :gcp.bigquery/clientable] [:dataset string?] [:routine string?]]]
      [:project-dataset-routine [:catn [:project string?] [:dataset string?] [:routine string?]]]]
   4 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:project string?]
      [:dataset string?]
      [:routine string?]]})

(dwim/defdwim ->RoutineDelete
              {:facade  gcp.bigquery/delete-routine
               :cmd :gcp.bigquery/RoutineDelete
               :arities delete-routine-args
               :normalize (fn [{:keys [cmd clientable project dataset routine routineId]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/RoutineDelete))
                                (let [resolved-routine-id (or (if (:routineId routineId) (:routineId routineId) routineId)
                                                              (cond-> {:dataset dataset :routine routine}
                                                                      project (assoc :project project)))]
                                  {:op        :gcp.bigquery/RoutineDelete
                                   :bigquery  clientable
                                   :routineId resolved-routine-id})))})

(defmethod execute! :gcp.bigquery/RoutineDelete [{:keys [bigquery routineId]}]
  (let [client (client bigquery)
        routineId (RoutineId/from-edn routineId)]
    (.delete client routineId)))

#!----------------------------------------------------------------------------------------------------------------------
;; ModelList

(def ^:private list-models-args
  {1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/ModelList]]]
      [:datasetId [:catn [:datasetId :gcp.bigquery/DatasetId]]]]
   2 [:altn
      [:project-dataset [:catn [:project string?] [:dataset string?]]]
      [:client-datasetId [:catn [:clientable :gcp.bigquery/clientable] [:datasetId :gcp.bigquery/DatasetId]]]
      [:datasetId-opts [:catn [:datasetId :gcp.bigquery/DatasetId] [:opts :gcp.bigquery/BigQuery.ModelListOption]]]]
   3 [:altn
      [:client-project-dataset [:catn [:clientable :gcp.bigquery/clientable] [:project string?] [:dataset string?]]]
      [:client-datasetId-opts [:catn [:clientable :gcp.bigquery/clientable] [:datasetId :gcp.bigquery/DatasetId] [:opts :gcp.bigquery/BigQuery.ModelListOption]]]
      [:project-dataset-opts [:catn [:project string?] [:dataset string?] [:opts :gcp.bigquery/BigQuery.ModelListOption]]]]
   4 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:project string?]
      [:dataset string?]
      [:opts :gcp.bigquery/BigQuery.ModelListOption]]})

(dwim/defdwim ->ModelList
              {:facade    gcp.bigquery/list-models
               :cmd       :gcp.bigquery/ModelList
               :arities   list-models-args
               :normalize (fn [{:keys [cmd clientable project dataset datasetId opts]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/ModelList))
                                (let [resolved-dataset-id (or datasetId
                                                              (cond-> {:dataset dataset}
                                                                      project (assoc :project project)))]
                                  {:op        :gcp.bigquery/ModelList
                                   :bigquery  clientable
                                   :datasetId resolved-dataset-id
                                   :opts      opts})))})

(defmethod execute! :gcp.bigquery/ModelList [{:keys [bigquery datasetId opts]}]
  (let [client (client bigquery)
        datasetId (DatasetId/from-edn datasetId)
        opts (BQ/ModelListOption-Array-from-edn opts)
        res (.listModels client datasetId opts)]
    (map Model/to-edn (seq (.iterateAll res)))))

;#!----------------------------------------------------------------------------------------------------------------------
;;; ModelUpdate

(def ^:private update-model-args
  {1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/ModelUpdate]]]
      [:modelInfo [:catn [:modelInfo [:or :gcp.bigquery/ModelInfo :gcp.bigquery/Model]]]]]
   2 [:altn
      [:client-modelInfo [:catn [:clientable :gcp.bigquery/clientable] [:modelInfo [:or :gcp.bigquery/ModelInfo :gcp.bigquery/Model]]]]
      [:modelInfo-opts [:catn [:modelInfo [:or :gcp.bigquery/ModelInfo :gcp.bigquery/Model]] [:opts :gcp.bigquery/BigQuery.ModelOption]]]]
   3 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:modelInfo [:or :gcp.bigquery/ModelInfo :gcp.bigquery/Model]]
      [:opts :gcp.bigquery/BigQuery.ModelOption]]})

(dwim/defdwim ->ModelUpdate
              {:facade    gcp.bigquery/update-model
               :cmd       :gcp.bigquery/ModelUpdate
               :arities   update-model-args
               :normalize (fn [{:keys [cmd clientable modelInfo opts]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/ModelUpdate))
                                {:op        :gcp.bigquery/ModelUpdate
                                 :bigquery  clientable
                                 :modelInfo modelInfo
                                 :opts      opts}))})

(defmethod execute! :gcp.bigquery/ModelUpdate [{:keys [bigquery modelInfo opts]}]
  (let [client (client bigquery)
        modelInfo (ModelInfo/from-edn modelInfo)
        opts (BQ/ModelOption-Array-from-edn opts)]
    (Model/to-edn (.update client modelInfo opts))))

;#!----------------------------------------------------------------------------------------------------------------------
;;; ModelGet

(def ^:private get-model-args
  {1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/ModelGet]]]
      [:modelId [:catn [:modelId [:or :gcp.bigquery/ModelId :gcp.bigquery/Model]]]]]
   2 [:altn
      [:client-modelId [:catn [:clientable :gcp.bigquery/clientable] [:modelId [:or :gcp.bigquery/ModelId :gcp.bigquery/Model]]]]
      [:dataset-model [:catn [:dataset string?] [:model string?]]]
      [:modelId-opts [:catn [:modelId [:or :gcp.bigquery/ModelId :gcp.bigquery/Model]] [:opts :gcp.bigquery/BigQuery.ModelOption]]]]
   3 [:altn
      [:client-dataset-model [:catn [:clientable :gcp.bigquery/clientable] [:dataset string?] [:model string?]]]
      [:client-modelId-opts [:catn [:clientable :gcp.bigquery/clientable] [:modelId [:or :gcp.bigquery/ModelId :gcp.bigquery/Model]] [:opts :gcp.bigquery/BigQuery.ModelOption]]]
      [:project-dataset-model [:catn [:project string?] [:dataset string?] [:model string?]]]
      [:dataset-model-opts [:catn [:dataset string?] [:model string?] [:opts :gcp.bigquery/BigQuery.ModelOption]]]]
   4 [:altn
      [:client-project-dataset-model [:catn [:clientable :gcp.bigquery/clientable] [:project string?] [:dataset string?] [:model string?]]]
      [:client-dataset-model-opts [:catn [:clientable :gcp.bigquery/clientable] [:dataset string?] [:model string?] [:opts :gcp.bigquery/BigQuery.ModelOption]]]]
   5 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:project string?]
      [:dataset string?]
      [:model string?]
      [:opts :gcp.bigquery/BigQuery.ModelOption]]})

(dwim/defdwim ->ModelGet
              {:facade    gcp.bigquery/get-model
               :cmd       :gcp.bigquery/ModelGet
               :arities   get-model-args
               :normalize (fn [{:keys [cmd clientable project dataset model modelId opts]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/ModelGet))
                                (let [resolved-model-id (or (if (:modelId modelId) (:modelId modelId) modelId)
                                                            (cond-> {:dataset dataset :model model}
                                                                    project (assoc :project project)))]
                                  {:op       :gcp.bigquery/ModelGet
                                   :bigquery clientable
                                   :modelId  resolved-model-id
                                   :opts     opts})))})

(defmethod execute! :gcp.bigquery/ModelGet [{:keys [bigquery modelId opts]}]
  (let [client (client bigquery)
        modelId (ModelId/from-edn modelId)
        opts (BQ/ModelOption-Array-from-edn opts)]
    (Model/to-edn (.getModel client modelId opts))))

#!----------------------------------------------------------------------------------------------------------------------
;; ModelDelete

(def ^:private delete-model-args
  {1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/ModelDelete]]]
      [:modelId [:catn [:modelId [:or :gcp.bigquery/ModelId :gcp.bigquery/Model]]]]]
   2 [:altn
      [:client-modelId [:catn [:clientable :gcp.bigquery/clientable] [:modelId [:or :gcp.bigquery/ModelId :gcp.bigquery/Model]]]]
      [:dataset-model [:catn [:dataset string?] [:model string?]]]]
   3 [:altn
      [:client-dataset-model [:catn [:clientable :gcp.bigquery/clientable] [:dataset string?] [:model string?]]]
      [:project-dataset-model [:catn [:project string?] [:dataset string?] [:model string?]]]]
   4 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:project string?]
      [:dataset string?]
      [:model string?]]})

(dwim/defdwim ->ModelDelete
              {:facade    gcp.bigquery/delete-model
               :cmd       :gcp.bigquery/ModelDelete
               :arities   delete-model-args
               :normalize (fn [{:keys [cmd clientable project dataset model modelId]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/ModelDelete))
                                (let [resolved-model-id (or (if (:modelId modelId) (:modelId modelId) modelId)
                                                            (cond-> {:dataset dataset :model model}
                                                                    project (assoc :project project)))]
                                  {:op       :gcp.bigquery/ModelDelete
                                   :bigquery clientable
                                   :modelId  resolved-model-id})))})

(defmethod execute! :gcp.bigquery/ModelDelete [{:keys [bigquery modelId]}]
  (let [client (client bigquery)
        modelId (ModelId/from-edn modelId)]
    (.delete client modelId)))

#!----------------------------------------------------------------------------------------------------------------------
;; GetIamPolicy

(def ^:private get-iam-policy-args
  {1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/GetIamPolicy]]]
      [:tableId [:catn [:tableId [:or :gcp.bigquery/TableId :gcp.bigquery/Table]]]]]
   2 [:altn
      [:client-tableId [:catn [:clientable :gcp.bigquery/clientable] [:tableId [:or :gcp.bigquery/TableId :gcp.bigquery/Table]]]]
      [:dataset-table [:catn [:dataset :string] [:table :string]]]
      [:tableId-opts [:catn [:tableId [:or :gcp.bigquery/TableId :gcp.bigquery/Table]] [:opts :gcp.bigquery/BigQuery.IAMOption]]]]
   3 [:altn
      [:client-dataset-table [:catn [:clientable :gcp.bigquery/clientable] [:dataset :string] [:table :string]]]
      [:client-tableId-opts [:catn [:clientable :gcp.bigquery/clientable] [:tableId [:or :gcp.bigquery/TableId :gcp.bigquery/Table]] [:opts :gcp.bigquery/BigQuery.IAMOption]]]
      [:project-dataset-table [:catn [:project :string] [:dataset :string] [:table :string]]]
      [:dataset-table-opts [:catn [:dataset :string] [:table :string] [:opts :gcp.bigquery/BigQuery.IAMOption]]]]
   4 [:altn
      [:client-project-dataset-table [:catn [:clientable :gcp.bigquery/clientable] [:project :string] [:dataset :string] [:table :string]]]
      [:client-dataset-table-opts [:catn [:clientable :gcp.bigquery/clientable] [:dataset :string] [:table :string] [:opts :gcp.bigquery/BigQuery.IAMOption]]]]
   5 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:project :string]
      [:dataset :string]
      [:table :string]
      [:opts :gcp.bigquery/BigQuery.IAMOption]]})

(dwim/defdwim ->GetIamPolicy
              {:facade    gcp.bigquery/get-iam-policy
               :cmd       :gcp.bigquery/GetIamPolicy
               :arities   get-iam-policy-args
               :normalize (fn [{:keys [cmd clientable project dataset table tableId opts]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/GetIamPolicy))
                                (let [resolved-table-id (or (if (:tableId tableId) (:tableId tableId) tableId)
                                                            (cond-> {:dataset dataset :table table}
                                                                    project (assoc :project project)))]
                                  {:op       :gcp.bigquery/GetIamPolicy
                                   :bigquery clientable
                                   :tableId  resolved-table-id
                                   :opts     opts})))})

(defmethod execute! :gcp.bigquery/GetIamPolicy [{:keys [bigquery tableId opts]}]
  (let [client (client bigquery)
        tableId (TableId/from-edn tableId)
        opts (BQ/IAMOption-Array-from-edn opts)]
    (cloud/Policy-to-edn (.getIamPolicy client tableId opts))))

#!----------------------------------------------------------------------------------------------------------------------
;; SetIamPolicy

(def ^:private set-iam-policy-args
  {1 [:catn [:cmd :gcp.bigquery/SetIamPolicy]]
   2 [:catn
      [:tableId [:or :gcp.bigquery/TableId :gcp.bigquery/Table]]
      [:policy ::cloud/Policy]]
   3 [:altn
      [:client-tableId-policy [:catn [:clientable :gcp.bigquery/clientable] [:tableId [:or :gcp.bigquery/TableId :gcp.bigquery/Table]] [:policy ::cloud/Policy]]]
      [:dataset-table-policy [:catn [:dataset :string] [:table :string] [:policy ::cloud/Policy]]]
      [:dataset-table-policy-opts [:catn [:dataset :string] [:table :string] [:policy ::cloud/Policy] [:opts :gcp.bigquery/BigQuery.IAMOption]]]
      [:tableId-policy-opts [:catn [:tableId [:or :gcp.bigquery/TableId :gcp.bigquery/Table]] [:policy ::cloud/Policy] [:opts :gcp.bigquery/BigQuery.IAMOption]]]]
   4 [:altn
      [:client-dataset-table-policy [:catn [:clientable :gcp.bigquery/clientable] [:dataset :string] [:table :string] [:policy ::cloud/Policy]]]
      [:project-dataset-table-policy
       [:catn
        [:project :string]
        [:dataset :string]
        [:table :string]
        [:policy ::cloud/Policy]]]
      [:client-tableId-policy-opts
       [:catn
        [:clientable :gcp.bigquery/clientable] [:tableId [:or :gcp.bigquery/TableId :gcp.bigquery/Table]] [:policy ::cloud/Policy] [:opts :gcp.bigquery/BigQuery.IAMOption]]]]
   5 [:altn
      [:client-project-dataset-table-policy [:catn [:clientable :gcp.bigquery/clientable] [:project :string] [:dataset :string] [:table :string] [:policy ::cloud/Policy]]]
      [:client-dataset-table-policy-opts [:catn [:clientable :gcp.bigquery/clientable] [:dataset :string] [:table :string] [:policy ::cloud/Policy] [:opts :gcp.bigquery/BigQuery.IAMOption]]]
      [:project-dataset-table-policy-opts [:catn [:project :string] [:dataset :string] [:table :string] [:policy ::cloud/Policy] [:opts :gcp.bigquery/BigQuery.IAMOption]]]]
   6 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:project :string]
      [:dataset :string]
      [:table :string]
      [:policy ::cloud/Policy]
      [:opts :gcp.bigquery/BigQuery.IAMOption]]})

(dwim/defdwim ->SetIamPolicy
              {:facade    gcp.bigquery/set-iam-policy
               :cmd       :gcp.bigquery/SetIamPolicy
               :arities   set-iam-policy-args
               :normalize (fn [{:keys [cmd clientable project dataset table tableId policy opts]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/SetIamPolicy))
                                (let [resolved-table-id (or (if (:tableId tableId) (:tableId tableId) tableId)
                                                            (cond-> {:dataset dataset :table table}
                                                                    project (assoc :project project)))]
                                  {:op       :gcp.bigquery/SetIamPolicy
                                   :bigquery clientable
                                   :tableId  resolved-table-id
                                   :policy   policy
                                   :opts     opts})))})

(defmethod execute! :gcp.bigquery/SetIamPolicy [{:keys [bigquery tableId policy opts]}]
  (let [client (client bigquery)
        tableId (TableId/from-edn tableId)
        policy (cloud/Policy-from-edn policy)
        opts (BQ/IAMOption-Array-from-edn opts)]
    (cloud/Policy-to-edn (.setIamPolicy client tableId policy opts))))

#!----------------------------------------------------------------------------------------------------------------------
;; TestIamPermissions

(def ^:private test-iam-permissions-args
  {1 [:catn [:cmd :gcp.bigquery/TestIamPermissions]]
   2 [:catn
      [:tableId [:or :gcp.bigquery/TableId :gcp.bigquery/Table]]
      [:permissions [:sequential :string]]]
   3 [:altn
      [:client-tableId-perms [:catn [:clientable :gcp.bigquery/clientable] [:tableId [:or :gcp.bigquery/TableId :gcp.bigquery/Table]] [:permissions [:sequential :string]]]]
      [:dataset-table-perms [:catn [:dataset :string] [:table :string] [:permissions [:sequential :string]]]]
      [:tableId-perms-opts [:catn [:tableId [:or :gcp.bigquery/TableId :gcp.bigquery/Table]] [:permissions [:sequential :string]] [:opts :gcp.bigquery/BigQuery.IAMOption]]]]
   4 [:altn
      [:client-dataset-table-perms [:catn [:clientable :gcp.bigquery/clientable] [:dataset :string] [:table :string] [:permissions [:sequential :string]]]]
      [:project-dataset-table-perms [:catn [:project :string] [:dataset :string] [:table :string] [:permissions [:sequential :string]]]]
      [:client-tableId-perms-opts [:catn [:clientable :gcp.bigquery/clientable] [:tableId [:or :gcp.bigquery/TableId :gcp.bigquery/Table]] [:permissions [:sequential :string]] [:opts :gcp.bigquery/BigQuery.IAMOption]]]
      [:dataset-table-perms-opts [:catn [:dataset :string] [:table :string] [:permissions [:sequential :string]] [:opts :gcp.bigquery/BigQuery.IAMOption]]]]
   5 [:altn
      [:client-project-dataset-table-perms [:catn [:clientable :gcp.bigquery/clientable] [:project :string] [:dataset :string] [:table :string] [:permissions [:sequential :string]]]]
      [:client-dataset-table-perms-opts [:catn [:clientable :gcp.bigquery/clientable] [:dataset :string] [:table :string] [:permissions [:sequential :string]] [:opts :gcp.bigquery/BigQuery.IAMOption]]]
      [:project-dataset-table-perms-opts [:catn [:project :string] [:dataset :string] [:table :string] [:permissions [:sequential :string]] [:opts :gcp.bigquery/BigQuery.IAMOption]]]]
   6 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:project :string]
      [:dataset :string]
      [:table :string]
      [:permissions [:sequential :string]]
      [:opts :gcp.bigquery/BigQuery.IAMOption]]})

(dwim/defdwim ->TestIamPermissions
              {:facade    gcp.bigquery/test-iam-permissions
               :cmd       :gcp.bigquery/TestIamPermissions
               :arities   test-iam-permissions-args
               :normalize (fn [{:keys [cmd clientable project dataset table tableId permissions opts]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/TestIamPermissions))
                                (let [resolved-table-id (or (if (:tableId tableId) (:tableId tableId) tableId)
                                                            (cond-> {:dataset dataset :table table}
                                                                    project (assoc :project project)))]
                                  {:op          :gcp.bigquery/TestIamPermissions
                                   :bigquery    clientable
                                   :tableId     resolved-table-id
                                   :permissions permissions
                                   :opts        opts})))})

(defmethod execute! :gcp.bigquery/TestIamPermissions [{:keys [bigquery tableId permissions opts]}]
  (let [client (client bigquery)
        tableId (TableId/from-edn tableId)
        permissions (vec permissions)
        opts (BQ/IAMOption-Array-from-edn opts)]
    (vec (.testIamPermissions client tableId permissions opts))))

#!----------------------------------------------------------------------------------------------------------------------
;; Query

(def ^:private query-args
  {1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/Query]]]
      [:configuration [:catn [:configuration :gcp.bigquery/QueryJobConfiguration]]]]
   2 [:altn
      [:configuration-jobId [:catn [:configuration :gcp.bigquery/QueryJobConfiguration] [:jobId [:or :string :gcp.bigquery/JobId :gcp.bigquery/Job]]]]
      [:configuration-opts [:catn [:configuration :gcp.bigquery/QueryJobConfiguration] [:opts :gcp.bigquery/BigQuery.JobOption]]]
      [:client-configuration [:catn [:clientable :gcp.bigquery/clientable] [:configuration :gcp.bigquery/QueryJobConfiguration]]]]
   3 [:altn
      [:configuration-jobId-opts [:catn [:configuration :gcp.bigquery/QueryJobConfiguration] [:jobId [:or :string :gcp.bigquery/JobId :gcp.bigquery/Job]] [:opts :gcp.bigquery/BigQuery.JobOption]]]
      [:client-configuration-jobId [:catn [:clientable :gcp.bigquery/clientable] [:configuration :gcp.bigquery/QueryJobConfiguration] [:jobId [:or :string :gcp.bigquery/JobId :gcp.bigquery/Job]]]]
      [:client-configuration-opts [:catn [:clientable :gcp.bigquery/clientable] [:configuration :gcp.bigquery/QueryJobConfiguration] [:opts :gcp.bigquery/BigQuery.JobOption]]]]
   4 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:configuration :gcp.bigquery/QueryJobConfiguration]
      [:jobId [:or :string :gcp.bigquery/JobId :gcp.bigquery/Job]]
      [:opts :gcp.bigquery/BigQuery.JobOption]]})

(dwim/defdwim ->Query
              {:facade    gcp.bigquery/query
               :cmd       :gcp.bigquery/Query
               :arities   query-args
               :normalize (fn [{:keys [cmd clientable configuration jobId opts]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/Query))
                                (let [resolved-job-id (cond
                                                        (string? jobId) {:job jobId}
                                                        (:jobId jobId) (:jobId jobId)
                                                        :else jobId)]
                                  {:op            :gcp.bigquery/Query
                                   :bigquery      clientable
                                   :configuration configuration
                                   :jobId         resolved-job-id
                                   :opts          opts})))})

#!----------------------------------------------------------------------------------------------------------------------
;; Q

(def ^:private q-args
  {1 [:catn [:query :string]]
   2 [:altn
      [:client-query [:catn [:clientable :gcp.bigquery/clientable] [:query :string]]]
      [:query-positionalParams [:catn [:query :string] [:positionalParameters [:sequential :gcp.bigquery/QueryParameterValue]]]]
      [:query-namedParams [:catn [:query :string] [:namedParameters [:map-of [:or simple-keyword? [:string {:min 1}]] :gcp.bigquery/QueryParameterValue]]]]]
   3 [:altn
      [:client-query-positionalParams [:catn [:clientable :gcp.bigquery/clientable] [:query :string] [:positionalParameters [:sequential :gcp.bigquery/QueryParameterValue]]]]
      [:client-query-namedParams [:catn [:clientable :gcp.bigquery/clientable] [:query :string] [:namedParameters [:map-of [:or simple-keyword? [:string {:min 1}]] :gcp.bigquery/QueryParameterValue]]]]]})

(dwim/defdwim ->Q
              {:facade    gcp.bigquery/q
               :cmd       :gcp.bigquery/Query
               :arities   q-args
               :normalize (fn [{:keys [clientable query positionalParameters namedParameters]}]
                            (let [configuration (cond-> {:type "QUERY" :query query}
                                                        positionalParameters (assoc :positionalParameters positionalParameters)
                                                        namedParameters (assoc :namedParameters namedParameters))]
                              (cond-> {:op            :gcp.bigquery/Query
                                       :configuration configuration}
                                      clientable (assoc :bigquery clientable))))})

(defmethod execute! :gcp.bigquery/Query [{:keys [bigquery configuration jobId opts]}]
  (let [client (client bigquery)
        configuration (QJC/from-edn configuration)
        jobId (some-> jobId JobId/from-edn)
        opts (BQ/JobOption-Array-from-edn opts)]
    (custom/TableResult-to-edn
      (if jobId
        (.query client configuration jobId opts)
        (.query client configuration opts)))))

#!----------------------------------------------------------------------------------------------------------------------
;; QueryWithTimeout

(def ^:private query-with-timeout-args
  {1 [:catn [:cmd :gcp.bigquery/QueryWithTimeout]]
   3 [:catn
      [:configuration :gcp.bigquery/QueryJobConfiguration]
      [:jobId [:or :string :gcp.bigquery/JobId :gcp.bigquery/Job]]
      [:timeoutMs :int]]
   4 [:altn
      [:client-configuration-jobId-timeout [:catn [:clientable :gcp.bigquery/clientable] [:configuration :gcp.bigquery/QueryJobConfiguration] [:jobId [:or :string :gcp.bigquery/JobId :gcp.bigquery/Job]] [:timeoutMs :int]]]
      [:configuration-jobId-timeout-opts [:catn [:configuration :gcp.bigquery/QueryJobConfiguration] [:jobId [:or :string :gcp.bigquery/JobId :gcp.bigquery/Job]] [:timeoutMs :int] [:opts :gcp.bigquery/BigQuery.JobOption]]]]
   5 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:configuration :gcp.bigquery/QueryJobConfiguration]
      [:jobId [:or :string :gcp.bigquery/JobId :gcp.bigquery/Job]]
      [:timeoutMs :int]
      [:opts :gcp.bigquery/BigQuery.JobOption]]})

(dwim/defdwim ->QueryWithTimeout
              {:facade    gcp.bigquery/query-with-timeout
               :cmd       :gcp.bigquery/QueryWithTimeout
               :arities   query-with-timeout-args
               :normalize (fn [{:keys [cmd clientable configuration jobId timeoutMs opts]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/QueryWithTimeout))
                                (let [resolved-job-id (cond
                                                        (string? jobId) {:job jobId}
                                                        (:jobId jobId) (:jobId jobId)
                                                        :else jobId)]
                                  {:op            :gcp.bigquery/QueryWithTimeout
                                   :bigquery      clientable
                                   :configuration configuration
                                   :timeoutMs     timeoutMs
                                   :jobId         resolved-job-id
                                   :opts          opts})))})

(defmethod execute! :gcp.bigquery/QueryWithTimeout [{:keys [bigquery configuration jobId timeoutMs opts]}]
  (let [client (client bigquery)
        configuration (QJC/from-edn configuration)
        jobId (JobId/from-edn jobId)
        timeoutMs (long timeoutMs)
        opts (BQ/JobOption-Array-from-edn opts)]
    (.queryWithTimeout client configuration jobId timeoutMs opts)))

#!----------------------------------------------------------------------------------------------------------------------
;; ConnectionCreate

(def ^:private create-connection-args
  {0 [:catn]
   1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/ConnectionCreate]]]
      [:client [:catn [:clientable :gcp.bigquery/clientable]]]
      [:connectionSettings [:catn [:connectionSettings :gcp.bigquery/ConnectionSettings]]]]
   2 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:connectionSettings :gcp.bigquery/ConnectionSettings]]})

(dwim/defdwim ->ConnectionCreate
              {:facade    gcp.bigquery/create-connection
               :cmd       :gcp.bigquery/ConnectionCreate
               :arities   create-connection-args
               :normalize (fn [{:keys [cmd clientable connectionSettings]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/ConnectionCreate))
                                (cond-> {:op       :gcp.bigquery/ConnectionCreate
                                         :bigquery clientable}
                                        connectionSettings (assoc :connectionSettings connectionSettings))))})

(defmethod execute! :gcp.bigquery/ConnectionCreate [{:keys [bigquery connectionSettings]}]
  (let [client (client bigquery)]
    (if connectionSettings
      (.createConnection client (ConnectionSettings/from-edn connectionSettings))
      (.createConnection client))))

#!----------------------------------------------------------------------------------------------------------------------
;; Writer

(def ^:private writer-args
  {1 [:altn
      [:cmd [:catn [:cmd :gcp.bigquery/Writer]]]
      [:writeChannelConfiguration [:catn [:writeChannelConfiguration :gcp.bigquery/WriteChannelConfiguration]]]]
   2 [:altn
      [:jobId-configuration [:catn [:jobId [:or :string :gcp.bigquery/JobId :gcp.bigquery/Job]] [:writeChannelConfiguration :gcp.bigquery/WriteChannelConfiguration]]]
      [:client-configuration [:catn [:clientable :gcp.bigquery/clientable] [:writeChannelConfiguration :gcp.bigquery/WriteChannelConfiguration]]]]
   3 [:catn
      [:clientable :gcp.bigquery/clientable]
      [:jobId [:or :string :gcp.bigquery/JobId :gcp.bigquery/Job]]
      [:writeChannelConfiguration :gcp.bigquery/WriteChannelConfiguration]]})

(dwim/defdwim ->Writer
              {:facade    gcp.bigquery/writer
               :cmd       :gcp.bigquery/Writer
               :arities   writer-args
               :normalize (fn [{:keys [cmd clientable jobId writeChannelConfiguration]}]
                            (or (some-> cmd (assoc :op :gcp.bigquery/Writer))
                                (let [resolved-job-id (cond
                                                        (string? jobId) {:job jobId}
                                                        (:jobId jobId) (:jobId jobId)
                                                        :else jobId)]
                                  {:op                        :gcp.bigquery/Writer
                                   :bigquery                  clientable
                                   :writeChannelConfiguration writeChannelConfiguration
                                   :jobId                     resolved-job-id})))})

(defmethod execute! :gcp.bigquery/Writer [{:keys [bigquery jobId writeChannelConfiguration]}]
  (let [client (client bigquery)
        writeChannelConfiguration (WriteChannelConfiguration/from-edn writeChannelConfiguration)]
    (if jobId
      (.writer client (JobId/from-edn jobId) writeChannelConfiguration)
      (.writer client writeChannelConfiguration))))
