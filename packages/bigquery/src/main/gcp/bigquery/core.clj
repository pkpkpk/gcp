(ns gcp.bigquery.core
  (:require [gcp.bigquery.BigQuery :as BQ]
            [gcp.bigquery.custom :as custom]
            [gcp.bigquery.custom.BigQueryOptions :as BQO]
            [gcp.bigquery.custom.Dataset :as Dataset]
            [gcp.bigquery.custom.Job :as Job]
            [gcp.bigquery.custom.Model :as Model]
            [gcp.bigquery.custom.QueryJobConfiguration :as QJC]
            [gcp.bigquery.custom.Routine :as Routine]
            [gcp.bigquery.custom.Table :as Table]
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
            [gcp.foreign.com.google.cloud :as cloud]
            [gcp.global :as g]
            [malli.core :as m])
  (:import [com.google.cloud.bigquery BigQuery]))

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
  {::clientable
   [:or
    (g/instance-schema com.google.cloud.bigquery.BigQuery)
    :gcp.bigquery/BigQueryOptions
    [:map [:bigquery [:or :gcp.bigquery/BigQueryOptions (g/instance-schema com.google.cloud.bigquery.BigQuery)]]]]

   ::DatasetList
   [:map {:closed true :doc "call record for bq.listDatasets()"}
    [:op [:= ::DatasetList]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:projectId {:optional true} string?]
    [:opts {:optional true} :gcp.bigquery/BigQuery.DatasetListOption]]

   ::DatasetGet
   [:map {:closed true :doc "call record for bq.getDataset()"}
    [:op [:= ::DatasetGet]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:datasetId :gcp.bigquery/DatasetId]
    [:opts {:optional true} :gcp.bigquery/BigQuery.DatasetOption]]

   ::DatasetCreate
   [:map {:closed true :doc "call record for bq.create(datasetInfo)"}
    [:op [:= ::DatasetCreate]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:datasetInfo :gcp.bigquery/DatasetInfo]
    [:opts {:optional true} :gcp.bigquery/BigQuery.DatasetOption]]

   ::DatasetUpdate
   [:map {:closed true :doc "call record for bq.update(datasetInfo)"}
    [:op [:= ::DatasetUpdate]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:datasetInfo :gcp.bigquery/DatasetInfo]
    [:opts {:optional true} :gcp.bigquery/BigQuery.DatasetOption]]

   ::DatasetDelete
   [:map {:closed true :doc "call record for bq.delete(datasetId)"}
    [:op [:= ::DatasetDelete]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:datasetId :gcp.bigquery/DatasetId]
    [:opts {:optional true} :gcp.bigquery/BigQuery.DatasetDeleteOption]]

   ::TableList
   [:map {:closed true :doc "call record for bq.listTables(datasetId)"}
    [:op [:= ::TableList]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:datasetId :gcp.bigquery/DatasetId]
    [:opts {:optional true} :gcp.bigquery/BigQuery.TableListOption]]

   ::TableListPartitions
   [:map {:closed true :doc "call record for bq.listPartitions(tableId)"}
    [:op [:= ::TableListPartitions]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:tableId :gcp.bigquery/TableId]]

   ::TableGet
   [:map {:closed true :doc "call record for bq.getTable()"}
    [:op [:= ::TableGet]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:tableId :gcp.bigquery/TableId]
    [:opts {:optional true} :gcp.bigquery/BigQuery.TableOption]]

   ::TableCreate
   [:map {:closed true :doc "call record for bq.create(tableInfo)"}
    [:op [:= ::TableCreate]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:tableInfo :gcp.bigquery/TableInfo]
    [:opts {:optional true} :gcp.bigquery/BigQuery.TableOption]]

   ::TableUpdate
   [:map {:closed true :doc "call record for bq.update(tableInfo)"}
    [:op [:= ::TableUpdate]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:tableInfo :gcp.bigquery/TableInfo]
    [:opts {:optional true} :gcp.bigquery/BigQuery.TableOption]]

   ::TableDelete
   [:map {:closed true :doc "call record for bq.delete(tableId)"}
    [:op [:= ::TableDelete]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:tableId :gcp.bigquery/TableId]]

   ::TableListData
   [:map {:closed true :doc "call record for bq.listTableData()"}
    [:op [:= ::TableListData]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:tableId :gcp.bigquery/TableId]
    [:schema {:optional true} :gcp.bigquery/Schema]
    [:opts {:optional true} :gcp.bigquery/BigQuery.TableDataListOption]]

   ::RoutineList
   [:map {:closed true :doc "call record for bq.listRoutines(datasetId)"}
    [:op [:= ::RoutineList]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:datasetId :gcp.bigquery/DatasetId]
    [:opts {:optional true} :gcp.bigquery/BigQuery.RoutineListOption]]

   ::RoutineGet
   [:map {:closed true :doc "call record for bq.getRoutine()"}
    [:op [:= ::RoutineGet]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:routineId :gcp.bigquery/RoutineId]
    [:opts {:optional true} :gcp.bigquery/BigQuery.RoutineOption]]

   ::RoutineCreate
   [:map {:closed true :doc "call record for bq.create(routineInfo)"}
    [:op [:= ::RoutineCreate]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:routineInfo :gcp.bigquery/RoutineInfo]
    [:opts {:optional true} :gcp.bigquery/BigQuery.RoutineOption]]

   ::RoutineUpdate
   [:map {:closed true :doc "call record for bq.update(routineInfo)"}
    [:op [:= ::RoutineUpdate]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:routineInfo :gcp.bigquery/RoutineInfo]
    [:opts {:optional true} :gcp.bigquery/BigQuery.RoutineOption]]

   ::RoutineDelete
   [:map {:closed true :doc "call record for bq.delete(routineId)"}
    [:op [:= ::RoutineDelete]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:routineId :gcp.bigquery/RoutineId]]

   ::ModelList
   [:map {:closed true :doc "call record for bq.listModels(datasetId)"}
    [:op [:= ::ModelList]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:datasetId :gcp.bigquery/DatasetId]
    [:opts {:optional true} :gcp.bigquery/BigQuery.ModelListOption]]

   ::ModelGet
   [:map {:closed true :doc "call record for bq.getModel()"}
    [:op [:= ::ModelGet]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:modelId :gcp.bigquery/ModelId]
    [:opts {:optional true} :gcp.bigquery/BigQuery.ModelOption]]

   ::ModelUpdate
   [:map {:closed true :doc "call record for bq.update(modelInfo)"}
    [:op [:= ::ModelUpdate]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:modelInfo :gcp.bigquery/ModelInfo]
    [:opts {:optional true} :gcp.bigquery/BigQuery.ModelOption]]

   ::ModelDelete
   [:map {:closed true :doc "call record for bq.delete(modelId)"}
    [:op [:= ::ModelDelete]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:modelId :gcp.bigquery/ModelId]]

   ::JobList
   [:map {:closed true :doc "call record for bq.listJobs()"}
    [:op [:= ::JobList]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:opts {:optional true} :gcp.bigquery/BigQuery.JobListOption]]

   ::JobCancel
   [:map {:closed true :doc "call record for bq.cancel(jobId)"}
    [:op [:= ::JobCancel]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:jobId :gcp.bigquery/JobId]]

   ::JobCreate
   [:map {:closed true :doc "call record for bq.create(jobInfo)"}
    [:op [:= ::JobCreate]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:jobInfo :gcp.bigquery/JobInfo]
    [:opts {:optional true} :gcp.bigquery/BigQuery.JobOption]]

   ::JobGet
   [:map {:closed true :doc "call record for bq.getJob(jobId)"}
    [:op [:= ::JobGet]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:jobId :gcp.bigquery/JobId]
    [:opts {:optional true} :gcp.bigquery/BigQuery.JobOption]]

   ::JobUpdate
   [:map {:closed true :doc "call record for bq.update(jobInfo)"}
    [:op [:= ::JobUpdate]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:jobInfo :gcp.bigquery/JobInfo]
    [:opts {:optional true} :gcp.bigquery/BigQuery.JobOption]]

   ::JobDelete
   [:map {:closed true :doc "call record for bq.delete(jobId)"}
    [:op [:= ::JobDelete]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:jobId :gcp.bigquery/JobId]]

   ::GetIamPolicy
   [:map {:closed true :doc "call record for bq.getIamPolicy(tableId)"}
    [:op [:= ::GetIamPolicy]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:tableId :gcp.bigquery/TableId]
    [:opts {:optional true} :gcp.bigquery/BigQuery.IAMOption]]

   ::SetIamPolicy
   [:map {:closed true :doc "call record for bq.setIamPolicy(tableId, policy)"}
    [:op [:= ::SetIamPolicy]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:tableId :gcp.bigquery/TableId]
    [:policy ::cloud/Policy]
    [:opts {:optional true} :gcp.bigquery/BigQuery.IAMOption]]

   ::TestIamPermissions
   [:map {:closed true :doc "call record for bq.testIamPermissions(tableId, permissions)"}
    [:op [:= ::TestIamPermissions]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:tableId :gcp.bigquery/TableId]
    [:permissions [:sequential string?]]
    [:opts {:optional true} :gcp.bigquery/BigQuery.IAMOption]]

   ::InsertAll
   [:map {:closed true :doc "call record for bq.insertAll(insertAllRequest)"}
    [:op [:= ::InsertAll]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:insertAllRequest :gcp.bigquery/InsertAllRequest]]

   ::Query
   [:map {:closed true :doc "call record for bq.query(configuration)"}
    [:op [:= ::Query]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:configuration :gcp.bigquery/QueryJobConfiguration]
    [:jobId {:optional true} :gcp.bigquery/JobId]
    [:opts {:optional true} :gcp.bigquery/BigQuery.JobOption]]

   ::QueryWithTimeout
   [:map {:closed true :doc "call record for bq.queryWithTimeout(configuration, timeoutMs)"}
    [:op [:= ::QueryWithTimeout]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:configuration :gcp.bigquery/QueryJobConfiguration]
    [:timeoutMs :int]
    [:jobId {:optional true} :gcp.bigquery/JobId]
    [:opts {:optional true} :gcp.bigquery/BigQuery.JobOption]]

   ::ConnectionCreate
   [:map {:closed true :doc "call record for bq.createConnection()"}
    [:op [:= ::ConnectionCreate]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:connectionSettings {:optional true} :gcp.bigquery/ConnectionSettings]]

   ::Writer
   [:map {:closed true :doc "call record for bq.writer()"}
    [:op [:= ::Writer]]
    [:bigquery {:optional true} [:ref ::clientable]]
    [:writeChannelConfiguration :gcp.bigquery/WriteChannelConfiguration]
    [:jobId {:optional true} :gcp.bigquery/JobId]]})

(g/include-schema-registry! (with-meta registry {::g/name "gcp.bigquery.core"}))

#!----------------------------------------------------------------------------------------------------------------------

(defn- extract-parse-values [parsed]
  (cond
    (instance? malli.core.Tag parsed)
    (extract-parse-values (:value parsed))

    (instance? malli.core.Tags parsed)
    (:values parsed)

    (map? parsed)
    parsed

    :else parsed))

#!----------------------------------------------------------------------------------------------------------------------

(def ^:private list-datasets-args-schema
  [:altn
   [:arity-0 [:catn]]
   [:arity-1 [:altn
              [:project [:catn [:projectId string?]]]
              [:opts    [:catn [:opts :gcp.bigquery/BigQuery.DatasetListOption]]]
              [:client  [:catn [:clientable ::clientable]]]]]
   [:arity-2 [:altn
              [:project-opts   [:catn [:projectId string?] [:opts :gcp.bigquery/BigQuery.DatasetListOption]]]
              [:client-project [:catn [:clientable ::clientable] [:projectId string?]]]
              [:client-opts    [:catn [:clientable ::clientable] [:opts :gcp.bigquery/BigQuery.DatasetListOption]]]]]
   [:arity-3 [:catn [:clientable ::clientable] [:projectId string?] [:opts :gcp.bigquery/BigQuery.DatasetListOption]]]])

(defn ->DatasetList [args]
  (let [schema (g/schema list-datasets-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to list-datasets" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable projectId opts]} (extract-parse-values parsed)]
        {:op        ::DatasetList
         :bigquery  clientable
         :projectId projectId
         :opts      opts}))))

(def ^:private create-dataset-args-schema
  [:altn
   [:arity-1 [:catn [:datasetInfo :gcp.bigquery/DatasetInfo]]]
   [:arity-2 [:altn
              [:info-opts   [:catn [:datasetInfo :gcp.bigquery/DatasetInfo] [:opts :gcp.bigquery/BigQuery.DatasetOption]]]
              [:client-info [:catn [:clientable ::clientable] [:datasetInfo :gcp.bigquery/DatasetInfo]]]]]
   [:arity-3 [:catn [:clientable ::clientable] [:datasetInfo :gcp.bigquery/DatasetInfo] [:opts :gcp.bigquery/BigQuery.DatasetOption]]]])

(defn ->DatasetCreate [args]
  (let [schema (g/schema create-dataset-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to create-dataset" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable datasetInfo opts]} (extract-parse-values parsed)]
        {:op          ::DatasetCreate
         :bigquery    clientable
         :datasetInfo datasetInfo
         :opts        opts}))))

(def ^:private update-dataset-args-schema
  [:altn
   [:arity-1 [:catn [:datasetInfo :gcp.bigquery/DatasetInfo]]]
   [:arity-2 [:altn
              [:info-opts   [:catn [:datasetInfo :gcp.bigquery/DatasetInfo] [:opts :gcp.bigquery/BigQuery.DatasetOption]]]
              [:client-info [:catn [:clientable ::clientable] [:datasetInfo :gcp.bigquery/DatasetInfo]]]]]
   [:arity-3 [:catn [:clientable ::clientable] [:datasetInfo :gcp.bigquery/DatasetInfo] [:opts :gcp.bigquery/BigQuery.DatasetOption]]]])

(defn ->DatasetUpdate [args]
  (let [schema (g/schema update-dataset-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to update-dataset" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable datasetInfo opts]} (extract-parse-values parsed)]
        {:op          ::DatasetUpdate
         :bigquery    clientable
         :datasetInfo datasetInfo
         :opts        opts}))))

(def ^:private get-dataset-args-schema
  [:altn
   [:arity-1 [:catn [:datasetId :gcp.bigquery/DatasetId]]]
   [:arity-2 [:altn
              [:id-opts   [:catn [:datasetId :gcp.bigquery/DatasetId] [:opts :gcp.bigquery/BigQuery.DatasetOption]]]
              [:client-id [:catn [:clientable ::clientable] [:datasetId :gcp.bigquery/DatasetId]]]]]
   [:arity-3 [:catn [:clientable ::clientable] [:datasetId :gcp.bigquery/DatasetId] [:opts :gcp.bigquery/BigQuery.DatasetOption]]]])

(defn ->DatasetGet [args]
  (let [schema (g/schema get-dataset-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to get-dataset" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable datasetId opts]} (extract-parse-values parsed)]
        {:op        ::DatasetGet
         :bigquery  clientable
         :datasetId datasetId
         :opts      opts}))))

(def ^:private delete-dataset-args-schema
  [:altn
   [:arity-1 [:catn [:datasetId :gcp.bigquery/DatasetId]]]
   [:arity-2 [:altn
              [:id-opts   [:catn [:datasetId :gcp.bigquery/DatasetId] [:opts :gcp.bigquery/BigQuery.DatasetDeleteOption]]]
              [:client-id [:catn [:clientable ::clientable] [:datasetId :gcp.bigquery/DatasetId]]]]]
   [:arity-3 [:catn [:clientable ::clientable] [:datasetId :gcp.bigquery/DatasetId] [:opts :gcp.bigquery/BigQuery.DatasetDeleteOption]]]])

(defn ->DatasetDelete [args]
  (let [schema (g/schema delete-dataset-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to delete-dataset" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable datasetId opts]} (extract-parse-values parsed)]
        {:op        ::DatasetDelete
         :bigquery  clientable
         :datasetId datasetId
         :opts      opts}))))

(def ^:private list-tables-args-schema
  [:altn
   [:arity-1 [:catn [:datasetId :gcp.bigquery/DatasetId]]]
   [:arity-2 [:altn
              [:project-dataset [:catn [:project string?] [:dataset string?]]]
              [:dataset-opts    [:catn [:datasetId :gcp.bigquery/DatasetId] [:opts :gcp.bigquery/BigQuery.TableListOption]]]
              [:client-dataset  [:catn [:clientable ::clientable] [:datasetId :gcp.bigquery/DatasetId]]]]]
   [:arity-3 [:altn
              [:project-dataset-opts   [:catn [:project string?] [:dataset string?] [:opts :gcp.bigquery/BigQuery.TableListOption]]]
              [:client-project-dataset [:catn [:clientable ::clientable] [:project string?] [:dataset string?]]]
              [:client-dataset-opts    [:catn [:clientable ::clientable] [:datasetId :gcp.bigquery/DatasetId] [:opts :gcp.bigquery/BigQuery.TableListOption]]]]]
   [:arity-4 [:catn [:clientable ::clientable] [:project string?] [:dataset string?] [:opts :gcp.bigquery/BigQuery.TableListOption]]]])

(defn ->TableList [args]
  (let [schema (g/schema list-tables-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to list-tables" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable project dataset datasetId opts]} (extract-parse-values parsed)
            resolved-dataset-id (or datasetId 
                                    (cond-> {:dataset dataset}
                                      project (assoc :project project)))]
        {:op        ::TableList
         :bigquery  clientable
         :datasetId resolved-dataset-id
         :opts      opts}))))

(def ^:private list-partitions-args-schema
  [:altn
   [:arity-1 [:catn [:tableId :gcp.bigquery/TableId]]]
   [:arity-2 [:altn
              [:client-table  [:catn [:clientable ::clientable] [:tableId :gcp.bigquery/TableId]]]
              [:dataset-table [:catn [:dataset string?] [:table string?]]]]]
   [:arity-3 [:altn
              [:client-dataset-table  [:catn [:clientable ::clientable] [:dataset string?] [:table string?]]]
              [:project-dataset-table [:catn [:project string?] [:dataset string?] [:table string?]]]]]
   [:arity-4 [:catn [:clientable ::clientable] [:project string?] [:dataset string?] [:table string?]]]])

(defn ->TableListPartitions [args]
  (let [schema (g/schema list-partitions-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to list-partitions" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable project dataset table tableId]} (extract-parse-values parsed)
            resolved-table-id (or tableId 
                                  (cond-> {:dataset dataset :table table}
                                    project (assoc :project project)))]
        {:op        ::TableListPartitions
         :bigquery  clientable
         :tableId   resolved-table-id}))))

(def ^:private create-table-args-schema
  [:altn
   [:arity-1 [:catn [:tableInfo :gcp.bigquery/TableInfo]]]
   [:arity-2 [:altn
              [:client-info [:catn [:clientable ::clientable] [:tableInfo :gcp.bigquery/TableInfo]]]
              [:info-opts   [:catn [:tableInfo :gcp.bigquery/TableInfo] [:opts :gcp.bigquery/BigQuery.TableOption]]]]]
   [:arity-3 [:catn [:clientable ::clientable] [:tableInfo :gcp.bigquery/TableInfo] [:opts :gcp.bigquery/BigQuery.TableOption]]]])

(defn ->TableCreate [args]
  (let [schema (g/schema create-table-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to create-table" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable tableInfo opts]} (extract-parse-values parsed)]
        {:op          ::TableCreate
         :bigquery    clientable
         :tableInfo   tableInfo
         :opts        opts}))))

(def ^:private update-table-args-schema
  [:altn
   [:arity-1 [:catn [:tableInfo :gcp.bigquery/TableInfo]]]
   [:arity-2 [:altn
              [:client-info [:catn [:clientable ::clientable] [:tableInfo :gcp.bigquery/TableInfo]]]
              [:info-opts   [:catn [:tableInfo :gcp.bigquery/TableInfo] [:opts :gcp.bigquery/BigQuery.TableOption]]]]]
   [:arity-3 [:catn [:clientable ::clientable] [:tableInfo :gcp.bigquery/TableInfo] [:opts :gcp.bigquery/BigQuery.TableOption]]]])

(defn ->TableUpdate [args]
  (let [schema (g/schema update-table-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to update-table" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable tableInfo opts]} (extract-parse-values parsed)]
        {:op          ::TableUpdate
         :bigquery    clientable
         :tableInfo   tableInfo
         :opts        opts}))))

(def ^:private get-table-args-schema
  [:altn
   [:arity-1 [:catn [:tableId :gcp.bigquery/TableId]]]
   [:arity-2 [:altn
              [:dataset-table [:catn [:dataset string?] [:table string?]]]
              [:table-opts    [:catn [:tableId :gcp.bigquery/TableId] [:opts :gcp.bigquery/BigQuery.TableOption]]]
              [:client-table  [:catn [:clientable ::clientable] [:tableId :gcp.bigquery/TableId]]]]]
   [:arity-3 [:altn
              [:project-dataset-table [:catn [:project string?] [:dataset string?] [:table string?]]]
              [:dataset-table-opts    [:catn [:dataset string?] [:table string?] [:opts :gcp.bigquery/BigQuery.TableOption]]]
              [:client-dataset-table  [:catn [:clientable ::clientable] [:dataset string?] [:table string?]]]
              [:client-table-opts     [:catn [:clientable ::clientable] [:tableId :gcp.bigquery/TableId] [:opts :gcp.bigquery/BigQuery.TableOption]]]]]
   [:arity-4 [:altn
              [:project-dataset-table-opts   [:catn [:project string?] [:dataset string?] [:table string?] [:opts :gcp.bigquery/BigQuery.TableOption]]]
              [:client-project-dataset-table [:catn [:clientable ::clientable] [:project string?] [:dataset string?] [:table string?]]]
              [:client-dataset-table-opts    [:catn [:clientable ::clientable] [:dataset string?] [:table string?] [:opts :gcp.bigquery/BigQuery.TableOption]]]]]
   [:arity-5 [:catn [:clientable ::clientable] [:project string?] [:dataset string?] [:table string?] [:opts :gcp.bigquery/BigQuery.TableOption]]]])

(defn ->TableGet [args]
  (let [schema (g/schema get-table-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to get-table" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable project dataset table tableId opts]} (extract-parse-values parsed)
            resolved-table-id (or tableId 
                                  (cond-> {:dataset dataset :table table}
                                    project (assoc :project project)))]
        {:op       ::TableGet
         :bigquery clientable
         :tableId  resolved-table-id
         :opts     opts}))))

(def ^:private delete-table-args-schema
  [:altn
   [:arity-1 [:catn [:tableId :gcp.bigquery/TableId]]]
   [:arity-2 [:altn
              [:client-table  [:catn [:clientable ::clientable] [:tableId :gcp.bigquery/TableId]]]
              [:dataset-table [:catn [:dataset string?] [:table string?]]]]]
   [:arity-3 [:altn
              [:client-dataset-table  [:catn [:clientable ::clientable] [:dataset string?] [:table string?]]]
              [:project-dataset-table [:catn [:project string?] [:dataset string?] [:table string?]]]]]
   [:arity-4 [:catn [:clientable ::clientable] [:project string?] [:dataset string?] [:table string?]]]])

(defn ->TableDelete [args]
  (let [schema (g/schema delete-table-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to delete-table" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable project dataset table tableId]} (extract-parse-values parsed)
            resolved-table-id (or tableId 
                                  (cond-> {:dataset dataset :table table}
                                    project (assoc :project project)))]
        {:op       ::TableDelete
         :bigquery clientable
         :tableId  resolved-table-id}))))

(def ^:private insert-all-args-schema
  [:altn
   [:arity-1 [:catn [:insertAllRequest :gcp.bigquery/InsertAllRequest]]]
   [:arity-2 [:altn
              [:client-request [:catn [:clientable ::clientable] [:insertAllRequest :gcp.bigquery/InsertAllRequest]]]
              [:table-rows     [:catn [:tableId :gcp.bigquery/TableId] [:rows [:sequential {:min 1} :gcp.bigquery/InsertAllRequest$RowToInsert]]]]]]
   [:arity-3 [:altn
              [:client-table-rows  [:catn [:clientable ::clientable] [:tableId :gcp.bigquery/TableId] [:rows [:sequential {:min 1} :gcp.bigquery/InsertAllRequest$RowToInsert]]]]
              [:dataset-table-rows [:catn [:dataset string?] [:table string?] [:rows [:sequential {:min 1} :gcp.bigquery/InsertAllRequest$RowToInsert]]]]]]
   [:arity-4 [:altn
              [:client-dataset-table-rows  [:catn [:clientable ::clientable] [:dataset string?] [:table string?] [:rows [:sequential {:min 1} :gcp.bigquery/InsertAllRequest$RowToInsert]]]]
              [:project-dataset-table-rows [:catn [:project string?] [:dataset string?] [:table string?] [:rows [:sequential {:min 1} :gcp.bigquery/InsertAllRequest$RowToInsert]]]]]]
   [:arity-5 [:catn [:clientable ::clientable] [:project string?] [:dataset string?] [:table string?] [:rows [:sequential {:min 1} :gcp.bigquery/InsertAllRequest$RowToInsert]]]]])

(defn ->InsertAll [args]
  (let [schema (g/schema insert-all-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to insert-all" {:args args :explain (m/explain schema args (g/mopts))}))
      (let [{:keys [clientable project dataset table tableId rows insertAllRequest]} (extract-parse-values parsed)
            resolved-table-id (or tableId
                                  (cond-> {:dataset dataset :table table}
                                          project (assoc :project project)))
            request (or insertAllRequest {:table resolved-table-id :rows rows})]
        {:op               ::InsertAll
         :bigquery         clientable
         :insertAllRequest request}))))

(def ^:private list-table-data-args-schema
  [:altn
   [:arity-1 [:catn [:tableId :gcp.bigquery/TableId]]]
   [:arity-2 [:altn
              [:client-table  [:catn [:clientable ::clientable] [:tableId :gcp.bigquery/TableId]]]
              [:dataset-table [:catn [:dataset string?] [:table string?]]]
              [:table-schema  [:catn [:tableId :gcp.bigquery/TableId] [:schema :gcp.bigquery/Schema]]]
              [:table-opts    [:catn [:tableId :gcp.bigquery/TableId] [:opts :gcp.bigquery/BigQuery.TableDataListOption]]]]]
   [:arity-3 [:altn
              [:client-dataset-table  [:catn [:clientable ::clientable] [:dataset string?] [:table string?]]]
              [:client-table-schema   [:catn [:clientable ::clientable] [:tableId :gcp.bigquery/TableId] [:schema :gcp.bigquery/Schema]]]
              [:client-table-opts     [:catn [:clientable ::clientable] [:tableId :gcp.bigquery/TableId] [:opts :gcp.bigquery/BigQuery.TableDataListOption]]]
              [:project-dataset-table [:catn [:project string?] [:dataset string?] [:table string?]]]
              [:dataset-table-schema  [:catn [:dataset string?] [:table string?] [:schema :gcp.bigquery/Schema]]]
              [:dataset-table-opts    [:catn [:dataset string?] [:table string?] [:opts :gcp.bigquery/BigQuery.TableDataListOption]]]
              [:table-schema-opts     [:catn [:tableId :gcp.bigquery/TableId] [:schema :gcp.bigquery/Schema] [:opts :gcp.bigquery/BigQuery.TableDataListOption]]]]]
   [:arity-4 [:altn
              [:client-project-dataset-table [:catn [:clientable ::clientable] [:project string?] [:dataset string?] [:table string?]]]
              [:client-dataset-table-schema  [:catn [:clientable ::clientable] [:dataset string?] [:table string?] [:schema :gcp.bigquery/Schema]]]
              [:client-dataset-table-opts    [:catn [:clientable ::clientable] [:dataset string?] [:table string?] [:opts :gcp.bigquery/BigQuery.TableDataListOption]]]
              [:client-table-schema-opts     [:catn [:clientable ::clientable] [:tableId :gcp.bigquery/TableId] [:schema :gcp.bigquery/Schema] [:opts :gcp.bigquery/BigQuery.TableDataListOption]]]
              [:project-dataset-table-schema [:catn [:project string?] [:dataset string?] [:table string?] [:schema :gcp.bigquery/Schema]]]
              [:project-dataset-table-opts   [:catn [:project string?] [:dataset string?] [:table string?] [:opts :gcp.bigquery/BigQuery.TableDataListOption]]]
              [:dataset-table-schema-opts    [:catn [:dataset string?] [:table string?] [:schema :gcp.bigquery/Schema] [:opts :gcp.bigquery/BigQuery.TableDataListOption]]]]]
   [:arity-5 [:altn
              [:client-project-dataset-table-schema [:catn [:clientable ::clientable] [:project string?] [:dataset string?] [:table string?] [:schema :gcp.bigquery/Schema]]]
              [:client-project-dataset-table-opts   [:catn [:clientable ::clientable] [:project string?] [:dataset string?] [:table string?] [:opts :gcp.bigquery/BigQuery.TableDataListOption]]]
              [:client-dataset-table-schema-opts    [:catn [:clientable ::clientable] [:dataset string?] [:table string?] [:schema :gcp.bigquery/Schema] [:opts :gcp.bigquery/BigQuery.TableDataListOption]]]
              [:project-dataset-table-schema-opts   [:catn [:project string?] [:dataset string?] [:table string?] [:schema :gcp.bigquery/Schema] [:opts :gcp.bigquery/BigQuery.TableDataListOption]]]]]
   [:arity-6 [:catn [:clientable ::clientable] [:project string?] [:dataset string?] [:table string?] [:schema :gcp.bigquery/Schema] [:opts :gcp.bigquery/BigQuery.TableDataListOption]]]])

(defn ->TableListData [args]
  (let [schema (g/schema list-table-data-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to list-table-data" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable project dataset table tableId schema opts]} (extract-parse-values parsed)
            resolved-table-id (or tableId 
                                  (cond-> {:dataset dataset :table table}
                                    project (assoc :project project)))]
        {:op        ::TableListData
         :bigquery  clientable
         :tableId   resolved-table-id
         :schema    schema
         :opts      opts}))))

(def ^:private list-jobs-args-schema
  [:altn
   [:arity-0 [:catn]]
   [:arity-1 [:altn
              [:client [:catn [:clientable ::clientable]]]
              [:opts   [:catn [:opts :gcp.bigquery/BigQuery.JobListOption]]]]]
   [:arity-2 [:catn [:clientable ::clientable] [:opts :gcp.bigquery/BigQuery.JobListOption]]]])

(defn ->JobList [args]
  (let [schema (g/schema list-jobs-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to list-jobs" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable opts]} (extract-parse-values parsed)]
        {:op       ::JobList
         :bigquery clientable
         :opts     opts}))))

(def ^:private cancel-job-args-schema
  [:altn
   [:arity-1 [:catn [:jobId [:or string? :gcp.bigquery/JobId]]]]
   [:arity-2 [:catn [:clientable ::clientable] [:jobId [:or string? :gcp.bigquery/JobId]]]]])

(defn ->JobCancel [args]
  (let [schema (g/schema cancel-job-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to cancel-job" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable jobId]} (extract-parse-values parsed)
            resolved-job-id (if (string? jobId) {:job jobId} jobId)]
        {:op       ::JobCancel
         :bigquery clientable
         :jobId    resolved-job-id}))))

(def ^:private create-job-args-schema
  [:altn
   [:arity-1 [:catn [:jobInfo :gcp.bigquery/JobInfo]]]
   [:arity-2 [:altn
              [:client-info [:catn [:clientable ::clientable] [:jobInfo :gcp.bigquery/JobInfo]]]
              [:info-opts   [:catn [:jobInfo :gcp.bigquery/JobInfo] [:opts :gcp.bigquery/BigQuery.JobOption]]]]]
   [:arity-3 [:catn [:clientable ::clientable] [:jobInfo :gcp.bigquery/JobInfo] [:opts :gcp.bigquery/BigQuery.JobOption]]]])

(defn ->JobCreate [args]
  (let [schema (g/schema create-job-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to create-job" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable jobInfo opts]} (extract-parse-values parsed)]
        {:op       ::JobCreate
         :bigquery clientable
         :jobInfo  jobInfo
         :opts     opts}))))

(def ^:private update-job-args-schema
  [:altn
   [:arity-1 [:catn [:jobInfo :gcp.bigquery/JobInfo]]]
   [:arity-2 [:altn
              [:client-info [:catn [:clientable ::clientable] [:jobInfo :gcp.bigquery/JobInfo]]]
              [:info-opts   [:catn [:jobInfo :gcp.bigquery/JobInfo] [:opts :gcp.bigquery/BigQuery.JobOption]]]]]
   [:arity-3 [:catn [:clientable ::clientable] [:jobInfo :gcp.bigquery/JobInfo] [:opts :gcp.bigquery/BigQuery.JobOption]]]])

(defn ->JobUpdate [args]
  (let [schema (g/schema update-job-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to update-job" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable jobInfo opts]} (extract-parse-values parsed)]
        {:op       ::JobUpdate
         :bigquery clientable
         :jobInfo  jobInfo
         :opts     opts}))))

(def ^:private get-job-args-schema
  [:altn
   [:arity-1 [:catn [:jobId [:or string? :gcp.bigquery/JobId]]]]
   [:arity-2 [:altn
              [:client-job [:catn [:clientable ::clientable] [:jobId [:or string? :gcp.bigquery/JobId]]]]
              [:job-opts   [:catn [:jobId [:or string? :gcp.bigquery/JobId]] [:opts :gcp.bigquery/BigQuery.JobOption]]]]]
   [:arity-3 [:catn [:clientable ::clientable] [:jobId [:or string? :gcp.bigquery/JobId]] [:opts :gcp.bigquery/BigQuery.JobOption]]]])

(defn ->JobGet [args]
  (let [schema (g/schema get-job-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to get-job" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable jobId opts]} (extract-parse-values parsed)
            resolved-job-id (if (string? jobId) {:job jobId} jobId)]
        {:op       ::JobGet
         :bigquery clientable
         :jobId    resolved-job-id
         :opts     opts}))))

(def ^:private delete-job-args-schema
  [:altn
   [:arity-1 [:catn [:jobId [:or string? :gcp.bigquery/JobId]]]]
   [:arity-2 [:catn [:clientable ::clientable] [:jobId [:or string? :gcp.bigquery/JobId]]]]])

(defn ->JobDelete [args]
  (let [schema (g/schema delete-job-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to delete-job" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable jobId]} (extract-parse-values parsed)
            resolved-job-id (if (string? jobId) {:job jobId} jobId)]
        {:op       ::JobDelete
         :bigquery clientable
         :jobId    resolved-job-id}))))

(def ^:private list-routines-args-schema
  [:altn
   [:arity-1 [:catn [:datasetId :gcp.bigquery/DatasetId]]]
   [:arity-2 [:altn
              [:project-dataset [:catn [:project string?] [:dataset string?]]]
              [:client-dataset  [:catn [:clientable ::clientable] [:datasetId :gcp.bigquery/DatasetId]]]
              [:dataset-opts    [:catn [:datasetId :gcp.bigquery/DatasetId] [:opts :gcp.bigquery/BigQuery.RoutineListOption]]]]]
   [:arity-3 [:altn
              [:client-project-dataset [:catn [:clientable ::clientable] [:project string?] [:dataset string?]]]
              [:client-dataset-opts    [:catn [:clientable ::clientable] [:datasetId :gcp.bigquery/DatasetId] [:opts :gcp.bigquery/BigQuery.RoutineListOption]]]
              [:project-dataset-opts   [:catn [:project string?] [:dataset string?] [:opts :gcp.bigquery/BigQuery.RoutineListOption]]]]]
   [:arity-4 [:catn [:clientable ::clientable] [:project string?] [:dataset string?] [:opts :gcp.bigquery/BigQuery.RoutineListOption]]]])

(defn ->RoutineList [args]
  (let [schema (g/schema list-routines-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to list-routines" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable project dataset datasetId opts]} (extract-parse-values parsed)
            resolved-dataset-id (or datasetId 
                                    (cond-> {:dataset dataset}
                                      project (assoc :project project)))]
        {:op        ::RoutineList
         :bigquery  clientable
         :datasetId resolved-dataset-id
         :opts      opts}))))

(def ^:private routine-info-args-schema
  [:altn
   [:arity-1 [:catn [:routineInfo :gcp.bigquery/RoutineInfo]]]
   [:arity-2 [:altn
              [:client-info [:catn [:clientable ::clientable] [:routineInfo :gcp.bigquery/RoutineInfo]]]
              [:info-opts   [:catn [:routineInfo :gcp.bigquery/RoutineInfo] [:opts :gcp.bigquery/BigQuery.RoutineOption]]]]]
   [:arity-3 [:catn [:clientable ::clientable] [:routineInfo :gcp.bigquery/RoutineInfo] [:opts :gcp.bigquery/BigQuery.RoutineOption]]]])

(defn ->RoutineCreate [args]
  (let [schema (g/schema routine-info-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to create-routine" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable routineInfo opts]} (extract-parse-values parsed)]
        {:op          ::RoutineCreate
         :bigquery    clientable
         :routineInfo routineInfo
         :opts        opts}))))

(defn ->RoutineUpdate [args]
  (let [schema (g/schema routine-info-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to update-routine" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable routineInfo opts]} (extract-parse-values parsed)]
        {:op          ::RoutineUpdate
         :bigquery    clientable
         :routineInfo routineInfo
         :opts        opts}))))

(def ^:private get-routine-args-schema
  [:altn
   [:arity-1 [:catn [:routineId :gcp.bigquery/RoutineId]]]
   [:arity-2 [:altn
              [:client-routine  [:catn [:clientable ::clientable] [:routineId :gcp.bigquery/RoutineId]]]
              [:dataset-routine [:catn [:dataset string?] [:routine string?]]]
              [:routine-opts    [:catn [:routineId :gcp.bigquery/RoutineId] [:opts :gcp.bigquery/BigQuery.RoutineOption]]]]]
   [:arity-3 [:altn
              [:client-ds-routine  [:catn [:clientable ::clientable] [:dataset string?] [:routine string?]]]
              [:client-routine-opts [:catn [:clientable ::clientable] [:routineId :gcp.bigquery/RoutineId] [:opts :gcp.bigquery/BigQuery.RoutineOption]]]
              [:project-ds-routine [:catn [:project string?] [:dataset string?] [:routine string?]]]
              [:ds-routine-opts    [:catn [:dataset string?] [:routine string?] [:opts :gcp.bigquery/BigQuery.RoutineOption]]]]]
   [:arity-4 [:altn
              [:client-proj-ds-routine [:catn [:clientable ::clientable] [:project string?] [:dataset string?] [:routine string?]]]
              [:client-ds-routine-opts  [:catn [:clientable ::clientable] [:dataset string?] [:routine string?] [:opts :gcp.bigquery/BigQuery.RoutineOption]]]]]
   [:arity-5 [:catn [:clientable ::clientable] [:project string?] [:dataset string?] [:routine string?] [:opts :gcp.bigquery/BigQuery.RoutineOption]]]])

(defn ->RoutineGet [args]
  (let [schema (g/schema get-routine-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to get-routine" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable project dataset routine routineId opts]} (extract-parse-values parsed)
            resolved-routine-id (or routineId 
                                    (cond-> {:dataset dataset :routine routine}
                                      project (assoc :project project)))]
        {:op        ::RoutineGet
         :bigquery  clientable
         :routineId resolved-routine-id
         :opts      opts}))))

(def ^:private delete-routine-args-schema
  [:altn
   [:arity-1 [:catn [:routineId :gcp.bigquery/RoutineId]]]
   [:arity-2 [:altn
              [:client-routine  [:catn [:clientable ::clientable] [:routineId :gcp.bigquery/RoutineId]]]
              [:dataset-routine [:catn [:dataset string?] [:routine string?]]]]]
   [:arity-3 [:altn
              [:client-ds-routine  [:catn [:clientable ::clientable] [:dataset string?] [:routine string?]]]
              [:project-ds-routine [:catn [:project string?] [:dataset string?] [:routine string?]]]]]
   [:arity-4 [:catn [:clientable ::clientable] [:project string?] [:dataset string?] [:routine string?]]]])

(defn ->RoutineDelete [args]
  (let [schema (g/schema delete-routine-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to delete-routine" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable project dataset routine routineId]} (extract-parse-values parsed)
            resolved-routine-id (or routineId 
                                    (cond-> {:dataset dataset :routine routine}
                                      project (assoc :project project)))]
        {:op        ::RoutineDelete
         :bigquery  clientable
         :routineId resolved-routine-id}))))

(def ^:private list-models-args-schema
  [:altn
   [:arity-1 [:catn [:datasetId :gcp.bigquery/DatasetId]]]
   [:arity-2 [:altn
              [:project-dataset [:catn [:project string?] [:dataset string?]]]
              [:client-dataset  [:catn [:clientable ::clientable] [:datasetId :gcp.bigquery/DatasetId]]]
              [:dataset-opts    [:catn [:datasetId :gcp.bigquery/DatasetId] [:opts :gcp.bigquery/BigQuery.ModelListOption]]]]]
   [:arity-3 [:altn
              [:client-project-dataset [:catn [:clientable ::clientable] [:project string?] [:dataset string?]]]
              [:client-dataset-opts    [:catn [:clientable ::clientable] [:datasetId :gcp.bigquery/DatasetId] [:opts :gcp.bigquery/BigQuery.ModelListOption]]]
              [:project-dataset-opts   [:catn [:project string?] [:dataset string?] [:opts :gcp.bigquery/BigQuery.ModelListOption]]]]]
   [:arity-4 [:catn [:clientable ::clientable] [:project string?] [:dataset string?] [:opts :gcp.bigquery/BigQuery.ModelListOption]]]])

(defn ->ModelList [args]
  (let [schema (g/schema list-models-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to list-models" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable project dataset datasetId opts]} (extract-parse-values parsed)
            resolved-dataset-id (or datasetId 
                                    (cond-> {:dataset dataset}
                                      project (assoc :project project)))]
        {:op        ::ModelList
         :bigquery  clientable
         :datasetId resolved-dataset-id
         :opts      opts}))))

(def ^:private update-model-args-schema
  [:altn
   [:arity-1 [:catn [:modelInfo :gcp.bigquery/ModelInfo]]]
   [:arity-2 [:altn
              [:client-info [:catn [:clientable ::clientable] [:modelInfo :gcp.bigquery/ModelInfo]]]
              [:info-opts   [:catn [:modelInfo :gcp.bigquery/ModelInfo] [:opts :gcp.bigquery/BigQuery.ModelOption]]]]]
   [:arity-3 [:catn [:clientable ::clientable] [:modelInfo :gcp.bigquery/ModelInfo] [:opts :gcp.bigquery/BigQuery.ModelOption]]]])

(defn ->ModelUpdate [args]
  (let [schema (g/schema update-model-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to update-model" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable modelInfo opts]} (extract-parse-values parsed)]
        {:op          ::ModelUpdate
         :bigquery    clientable
         :modelInfo   modelInfo
         :opts        opts}))))

(def ^:private get-model-args-schema
  [:altn
   [:arity-1 [:catn [:modelId :gcp.bigquery/ModelId]]]
   [:arity-2 [:altn
              [:client-model  [:catn [:clientable ::clientable] [:modelId :gcp.bigquery/ModelId]]]
              [:dataset-model [:catn [:dataset string?] [:model string?]]]
              [:model-opts    [:catn [:modelId :gcp.bigquery/ModelId] [:opts :gcp.bigquery/BigQuery.ModelOption]]]]]
   [:arity-3 [:altn
              [:client-ds-model   [:catn [:clientable ::clientable] [:dataset string?] [:model string?]]]
              [:client-model-opts [:catn [:clientable ::clientable] [:modelId :gcp.bigquery/ModelId] [:opts :gcp.bigquery/BigQuery.ModelOption]]]
              [:project-ds-model  [:catn [:project string?] [:dataset string?] [:model string?]]]
              [:ds-model-opts     [:catn [:dataset string?] [:model string?] [:opts :gcp.bigquery/BigQuery.ModelOption]]]]]
   [:arity-4 [:altn
              [:client-proj-ds-model [:catn [:clientable ::clientable] [:project string?] [:dataset string?] [:model string?]]]
              [:client-ds-model-opts [:catn [:clientable ::clientable] [:dataset string?] [:model string?] [:opts :gcp.bigquery/BigQuery.ModelOption]]]]]
   [:arity-5 [:catn [:clientable ::clientable] [:project string?] [:dataset string?] [:model string?] [:opts :gcp.bigquery/BigQuery.ModelOption]]]])

(defn ->ModelGet [args]
  (let [schema (g/schema get-model-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to get-model" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable project dataset model modelId opts]} (extract-parse-values parsed)
            resolved-model-id (or modelId 
                                  (cond-> {:dataset dataset :model model}
                                    project (assoc :project project)))]
        {:op        ::ModelGet
         :bigquery  clientable
         :modelId   resolved-model-id
         :opts      opts}))))

(def ^:private delete-model-args-schema
  [:altn
   [:arity-1 [:catn [:modelId :gcp.bigquery/ModelId]]]
   [:arity-2 [:altn
              [:client-model  [:catn [:clientable ::clientable] [:modelId :gcp.bigquery/ModelId]]]
              [:dataset-model [:catn [:dataset string?] [:model string?]]]]]
   [:arity-3 [:altn
              [:client-ds-model  [:catn [:clientable ::clientable] [:dataset string?] [:model string?]]]
              [:project-ds-model [:catn [:project string?] [:dataset string?] [:model string?]]]]]
   [:arity-4 [:catn [:clientable ::clientable] [:project string?] [:dataset string?] [:model string?]]]])

(defn ->ModelDelete [args]
  (let [schema (g/schema delete-model-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to delete-model" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable project dataset model modelId]} (extract-parse-values parsed)
            resolved-model-id (or modelId 
                                  (cond-> {:dataset dataset :model model}
                                    project (assoc :project project)))]
        {:op        ::ModelDelete
         :bigquery  clientable
         :modelId   resolved-model-id}))))

(def ^:private get-iam-policy-args-schema
  [:altn
   [:arity-1 [:catn [:tableId :gcp.bigquery/TableId]]]
   [:arity-2 [:altn
              [:client-table  [:catn [:clientable ::clientable] [:tableId :gcp.bigquery/TableId]]]
              [:dataset-table [:catn [:dataset string?] [:table string?]]]
              [:table-opts    [:catn [:tableId :gcp.bigquery/TableId] [:opts :gcp.bigquery/BigQuery.IAMOption]]]]]
   [:arity-3 [:altn
              [:client-dataset-table  [:catn [:clientable ::clientable] [:dataset string?] [:table string?]]]
              [:client-table-opts     [:catn [:clientable ::clientable] [:tableId :gcp.bigquery/TableId] [:opts :gcp.bigquery/BigQuery.IAMOption]]]
              [:project-dataset-table [:catn [:project string?] [:dataset string?] [:table string?]]]
              [:dataset-table-opts    [:catn [:dataset string?] [:table string?] [:opts :gcp.bigquery/BigQuery.IAMOption]]]]]
   [:arity-4 [:altn
              [:client-project-dataset-table [:catn [:clientable ::clientable] [:project string?] [:dataset string?] [:table string?]]]
              [:client-dataset-table-opts    [:catn [:clientable ::clientable] [:dataset string?] [:table string?] [:opts :gcp.bigquery/BigQuery.IAMOption]]]]]
   [:arity-5 [:catn [:clientable ::clientable] [:project string?] [:dataset string?] [:table string?] [:opts :gcp.bigquery/BigQuery.IAMOption]]]])

(defn ->GetIamPolicy [args]
  (let [schema (g/schema get-iam-policy-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to get-iam-policy" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable project dataset table tableId opts]} (extract-parse-values parsed)
            resolved-table-id (or tableId 
                                  (cond-> {:dataset dataset :table table}
                                    project (assoc :project project)))]
        {:op       ::GetIamPolicy
         :bigquery clientable
         :tableId  resolved-table-id
         :opts     opts}))))

(def ^:private set-iam-policy-args-schema
  [:altn
   [:arity-2 [:catn [:tableId :gcp.bigquery/TableId] [:policy ::cloud/Policy]]]
   [:arity-3 [:altn
              [:client-table-policy [:catn [:clientable ::clientable] [:tableId :gcp.bigquery/TableId] [:policy ::cloud/Policy]]]
              [:dataset-table-policy [:catn [:dataset string?] [:table string?] [:policy ::cloud/Policy]]]
              [:table-policy-opts   [:catn [:tableId :gcp.bigquery/TableId] [:policy ::cloud/Policy] [:opts :gcp.bigquery/BigQuery.IAMOption]]]]]
   [:arity-4 [:altn
              [:client-dataset-table-policy [:catn [:clientable ::clientable] [:dataset string?] [:table string?] [:policy ::cloud/Policy]]]
              [:project-dataset-table-policy [:catn [:project string?] [:dataset string?] [:table string?] [:policy ::cloud/Policy]]]
              [:client-table-policy-opts    [:catn [:clientable ::clientable] [:tableId :gcp.bigquery/TableId] [:policy ::cloud/Policy] [:opts :gcp.bigquery/BigQuery.IAMOption]]]
              [:dataset-table-policy-opts   [:catn [:dataset string?] [:table string?] [:policy ::cloud/Policy] [:opts :gcp.bigquery/BigQuery.IAMOption]]]]]
   [:arity-5 [:altn
              [:client-project-dataset-table-policy [:catn [:clientable ::clientable] [:project string?] [:dataset string?] [:table string?] [:policy ::cloud/Policy]]]
              [:client-dataset-table-policy-opts    [:catn [:clientable ::clientable] [:dataset string?] [:table string?] [:policy ::cloud/Policy] [:opts :gcp.bigquery/BigQuery.IAMOption]]]
              [:project-dataset-table-policy-opts   [:catn [:project string?] [:dataset string?] [:table string?] [:policy ::cloud/Policy] [:opts :gcp.bigquery/BigQuery.IAMOption]]]]]
   [:arity-6 [:catn [:clientable ::clientable] [:project string?] [:dataset string?] [:table string?] [:policy ::cloud/Policy] [:opts :gcp.bigquery/BigQuery.IAMOption]]]])

(defn ->SetIamPolicy [args]
  (let [schema (g/schema set-iam-policy-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to set-iam-policy" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable project dataset table tableId policy opts]} (extract-parse-values parsed)
            resolved-table-id (or tableId 
                                  (cond-> {:dataset dataset :table table}
                                    project (assoc :project project)))]
        {:op       ::SetIamPolicy
         :bigquery clientable
         :tableId  resolved-table-id
         :policy   policy
         :opts     opts}))))

(def ^:private test-iam-permissions-args-schema
  [:altn
   [:arity-2 [:catn [:tableId :gcp.bigquery/TableId] [:permissions [:sequential string?]]]]
   [:arity-3 [:altn
              [:client-table-perms  [:catn [:clientable ::clientable] [:tableId :gcp.bigquery/TableId] [:permissions [:sequential string?]]]]
              [:dataset-table-perms [:catn [:dataset string?] [:table string?] [:permissions [:sequential string?]]]]
              [:table-perms-opts    [:catn [:tableId :gcp.bigquery/TableId] [:permissions [:sequential string?]] [:opts :gcp.bigquery/BigQuery.IAMOption]]]]]
   [:arity-4 [:altn
              [:client-dataset-table-perms  [:catn [:clientable ::clientable] [:dataset string?] [:table string?] [:permissions [:sequential string?]]]]
              [:project-dataset-table-perms [:catn [:project string?] [:dataset string?] [:table string?] [:permissions [:sequential string?]]]]
              [:client-table-perms-opts     [:catn [:clientable ::clientable] [:tableId :gcp.bigquery/TableId] [:permissions [:sequential string?]] [:opts :gcp.bigquery/BigQuery.IAMOption]]]
              [:dataset-table-perms-opts    [:catn [:dataset string?] [:table string?] [:permissions [:sequential string?]] [:opts :gcp.bigquery/BigQuery.IAMOption]]]]]
   [:arity-5 [:altn
              [:client-project-dataset-table-perms  [:catn [:clientable ::clientable] [:project string?] [:dataset string?] [:table string?] [:permissions [:sequential string?]]]]
              [:client-dataset-table-perms-opts     [:catn [:clientable ::clientable] [:dataset string?] [:table string?] [:permissions [:sequential string?]] [:opts :gcp.bigquery/BigQuery.IAMOption]]]
              [:project-dataset-table-perms-opts    [:catn [:project string?] [:dataset string?] [:table string?] [:permissions [:sequential string?]] [:opts :gcp.bigquery/BigQuery.IAMOption]]]]]
   [:arity-6 [:catn [:clientable ::clientable] [:project string?] [:dataset string?] [:table string?] [:permissions [:sequential string?]] [:opts :gcp.bigquery/BigQuery.IAMOption]]]])

(defn ->TestIamPermissions [args]
  (let [schema (g/schema test-iam-permissions-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to test-iam-permissions" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable project dataset table tableId permissions opts]} (extract-parse-values parsed)
            resolved-table-id (or tableId 
                                  (cond-> {:dataset dataset :table table}
                                    project (assoc :project project)))]
        {:op          ::TestIamPermissions
         :bigquery    clientable
         :tableId     resolved-table-id
         :permissions permissions
         :opts        opts}))))

(def ^:private query-args-schema
  [:altn
   [:arity-1 [:catn [:configuration :gcp.bigquery/QueryJobConfiguration]]]
   [:arity-2 [:altn
              [:client-config [:catn [:clientable ::clientable] [:configuration :gcp.bigquery/QueryJobConfiguration]]]
              [:config-job    [:catn [:configuration :gcp.bigquery/QueryJobConfiguration] [:jobId [:or string? :gcp.bigquery/JobId]]]]
              [:config-opts   [:catn [:configuration :gcp.bigquery/QueryJobConfiguration] [:opts :gcp.bigquery/BigQuery.JobOption]]]]]
   [:arity-3 [:altn
              [:client-config-job  [:catn [:clientable ::clientable] [:configuration :gcp.bigquery/QueryJobConfiguration] [:jobId [:or string? :gcp.bigquery/JobId]]]]
              [:client-config-opts [:catn [:clientable ::clientable] [:configuration :gcp.bigquery/QueryJobConfiguration] [:opts :gcp.bigquery/BigQuery.JobOption]]]
              [:config-job-opts    [:catn [:configuration :gcp.bigquery/QueryJobConfiguration] [:jobId [:or string? :gcp.bigquery/JobId]] [:opts :gcp.bigquery/BigQuery.JobOption]]]]]
   [:arity-4 [:catn [:clientable ::clientable] [:configuration :gcp.bigquery/QueryJobConfiguration] [:jobId [:or string? :gcp.bigquery/JobId]] [:opts :gcp.bigquery/BigQuery.JobOption]]]])

(defn ->Query [args]
  (let [schema (g/schema query-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to query" {:args args :explain (m/explain schema args (g/mopts))}))
      (let [{:keys [clientable configuration jobId opts]} (extract-parse-values parsed)
            resolved-job-id (if (string? jobId) {:job jobId} jobId)]
        {:op            ::Query
         :bigquery      clientable
         :configuration configuration
         :jobId         resolved-job-id
         :opts          opts}))))

(def ^:private q-args-schema
  [:altn
   [:arity-1 [:catn [:query string?]]]
   [:arity-2 [:altn
              [:client-query       [:catn [:clientable ::clientable] [:query string?]]]
              [:query-seq-params   [:catn [:query string?] [:positionalParameters [:sequential :gcp.bigquery/QueryParameterValue]]]]
              [:query-map-params   [:catn [:query string?] [:namedParameters [:map-of [:or simple-keyword? [:string {:min 1}]] :gcp.bigquery/QueryParameterValue]]]]]]
   [:arity-3 [:altn
              [:client-query-seq-params [:catn [:clientable ::clientable] [:query string?] [:positionalParameters [:sequential :gcp.bigquery/QueryParameterValue]]]]
              [:client-query-map-params [:catn [:clientable ::clientable] [:query string?] [:namedParameters [:map-of [:or simple-keyword? [:string {:min 1}]] :gcp.bigquery/QueryParameterValue]]]]]]])

(defn ->Q [args]
  (let [schema (g/schema q-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to q" {:args args :explain (m/explain schema args (g/mopts))}))
      (let [{:keys [clientable query positionalParameters namedParameters]} (extract-parse-values parsed)
            configuration (cond-> {:query query}
                            positionalParameters (assoc :positionalParameters positionalParameters)
                            namedParameters      (assoc :namedParameters namedParameters))]
        (cond-> {:op            ::Query
                 :configuration configuration}
          clientable (assoc :bigquery clientable))))))

(def ^:private query-with-timeout-args-schema
  [:altn
   [:arity-3 [:catn [:configuration :gcp.bigquery/QueryJobConfiguration] [:jobId [:or string? :gcp.bigquery/JobId]] [:timeoutMs :int]]]
   [:arity-4 [:altn
              [:client-config-job-timeout [:catn [:clientable ::clientable] [:configuration :gcp.bigquery/QueryJobConfiguration] [:jobId [:or string? :gcp.bigquery/JobId]] [:timeoutMs :int]]]
              [:config-job-timeout-opts   [:catn [:configuration :gcp.bigquery/QueryJobConfiguration] [:jobId [:or string? :gcp.bigquery/JobId]] [:timeoutMs :int] [:opts :gcp.bigquery/BigQuery.JobOption]]]]]
   [:arity-5 [:catn [:clientable ::clientable] [:configuration :gcp.bigquery/QueryJobConfiguration] [:jobId [:or string? :gcp.bigquery/JobId]] [:timeoutMs :int] [:opts :gcp.bigquery/BigQuery.JobOption]]]])

(defn ->QueryWithTimeout [args]
  (let [schema (g/schema query-with-timeout-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to query-with-timeout" {:args args :explain (m/explain schema args (g/mopts))}))
      (let [{:keys [clientable configuration jobId timeoutMs opts]} (extract-parse-values parsed)
            resolved-job-id (if (string? jobId) {:job jobId} jobId)]
        {:op            ::QueryWithTimeout
         :bigquery      clientable
         :configuration configuration
         :timeoutMs     timeoutMs
         :jobId         resolved-job-id
         :opts          opts}))))

(def ^:private create-connection-args-schema
  [:altn
   [:arity-0 [:catn]]
   [:arity-1 [:altn
              [:client   [:catn [:clientable ::clientable]]]
              [:settings [:catn [:connectionSettings :gcp.bigquery/ConnectionSettings]]]]]
   [:arity-2 [:catn [:clientable ::clientable] [:connectionSettings :gcp.bigquery/ConnectionSettings]]]])

(defn ->ConnectionCreate [args]
  (let [schema (g/schema create-connection-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to create-connection" {:args args :explain (m/explain schema args (g/mopts))}))
      (let [{:keys [clientable connectionSettings]} (extract-parse-values parsed)]
        {:op                 ::ConnectionCreate
         :bigquery           clientable
         :connectionSettings connectionSettings}))))

(def ^:private writer-args-schema
  [:altn
   [:arity-1 [:catn [:writeChannelConfiguration :gcp.bigquery/WriteChannelConfiguration]]]
   [:arity-2 [:altn
              [:client-config [:catn [:clientable ::clientable] [:writeChannelConfiguration :gcp.bigquery/WriteChannelConfiguration]]]
              [:job-config    [:catn [:jobId [:or string? :gcp.bigquery/JobId]] [:writeChannelConfiguration :gcp.bigquery/WriteChannelConfiguration]]]]]
   [:arity-3 [:catn [:clientable ::clientable] [:jobId [:or string? :gcp.bigquery/JobId]] [:writeChannelConfiguration :gcp.bigquery/WriteChannelConfiguration]]]])

(defn ->Writer [args]
  (let [schema (g/schema writer-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to writer" {:args args :explain (m/explain schema args (g/mopts))}))
      (let [{:keys [clientable jobId writeChannelConfiguration]} (extract-parse-values parsed)
            resolved-job-id (if (string? jobId) {:job jobId} jobId)]
        {:op                        ::Writer
         :bigquery                  clientable
         :writeChannelConfiguration writeChannelConfiguration
         :jobId                     resolved-job-id}))))

#!----------------------------------------------------------------------------------------------------------------------

(defmulti execute! :op)

(defmethod execute! ::DatasetList [{:keys [bigquery projectId opts]}]
  (let [client (client bigquery)
        opts (BQ/DatasetListOption-Array-from-edn opts)
        res (if projectId
              (.listDatasets client projectId opts)
              (.listDatasets client opts))]
    (map Dataset/to-edn (seq (.iterateAll res)))))

(defmethod execute! ::DatasetCreate [{:keys [bigquery datasetInfo opts]}]
  (let [client (client bigquery)
        datasetInfo (DatasetInfo/from-edn datasetInfo)
        opts (BQ/DatasetOption-Array-from-edn opts)]
    (Dataset/to-edn (.create client datasetInfo opts))))

(defmethod execute! ::DatasetUpdate [{:keys [bigquery datasetInfo opts]}]
  (let [client (client bigquery)
        datasetInfo (DatasetInfo/from-edn datasetInfo)
        opts (BQ/DatasetOption-Array-from-edn opts)]
    (Dataset/to-edn (.update client datasetInfo opts))))

(defmethod execute! ::DatasetGet [{:keys [bigquery datasetId opts]}]
  (let [client (client bigquery)
        datasetId (DatasetId/from-edn datasetId)
        opts (BQ/DatasetOption-Array-from-edn opts)]
    (Dataset/to-edn (.getDataset client datasetId opts))))

(defmethod execute! ::DatasetDelete [{:keys [bigquery datasetId opts]}]
  (let [client (client bigquery)
        datasetId (DatasetId/from-edn datasetId)
        opts (BQ/DatasetDeleteOption-Array-from-edn opts)]
    (.delete client datasetId opts)))

#!----------------------------------------------------------------------------------------------------------------------

(defmethod execute! ::TableList [{:keys [bigquery datasetId opts]}]
  (let [client (client bigquery)
        datasetId (DatasetId/from-edn datasetId)
        opts (BQ/TableListOption-Array-from-edn opts)
        res (.listTables client datasetId opts)]
    (map Table/Lite-to-edn (seq (.iterateAll res)))))

(defmethod execute! ::TableListPartitions [{:keys [bigquery tableId]}]
  (let [client (client bigquery)
        tableId (TableId/from-edn tableId)]
    (vec (.listPartitions client tableId))))

(defmethod execute! ::TableCreate [{:keys [bigquery tableInfo opts]}]
  (let [client (client bigquery)
        tableInfo (TableInfo/from-edn tableInfo)
        opts (BQ/TableOption-Array-from-edn opts)]
    (Table/to-edn (.create client tableInfo opts))))

(defmethod execute! ::TableUpdate [{:keys [bigquery tableInfo opts]}]
  (let [client (client bigquery)
        tableInfo (TableInfo/from-edn tableInfo)
        opts (BQ/TableOption-Array-from-edn opts)]
    (Table/to-edn (.update client tableInfo opts))))

(defmethod execute! ::TableGet [{:keys [bigquery tableId opts]}]
  (let [client (client bigquery)
        tableId (TableId/from-edn tableId)
        opts (BQ/TableOption-Array-from-edn opts)]
    (Table/to-edn (.getTable client tableId opts))))

(defmethod execute! ::TableDelete [{:keys [bigquery tableId]}]
  (let [client (client bigquery)
        tableId (TableId/from-edn tableId)]
    (.delete client tableId)))

(defmethod execute! ::InsertAll [{:keys [request bigquery]}]
  (let [client (client bigquery)
        request (custom/InsertAllRequest-from-edn request)]
    (InsertAllResponse/to-edn (.insertAll client request))))

(defmethod execute! ::TableListData [{:keys [bigquery tableId schema opts]}]
  (let [client (client bigquery)
        tableId (TableId/from-edn tableId)
        opts (BQ/TableDataListOption-Array-from-edn opts)
        res (if-some [schema (some-> schema Schema/from-edn)]
              (.listTableData client tableId schema opts)
              (.listTableData client tableId opts))]
    (custom/TableResult-to-edn res)))

#!----------------------------------------------------------------------------------------------------------------------

(defmethod execute! ::JobList [{:keys [bigquery opts]}]
  (let [client (client bigquery)
        opts (BQ/JobListOption-Array-from-edn opts)
        res (.listJobs client opts)]
    (map Job/to-edn (seq (.iterateAll res)))))

(defmethod execute! ::JobCancel [{:keys [bigquery jobId]}]
  (let [client (client bigquery)
        jobId (JobId/from-edn jobId)]
    (.cancel client jobId)))

(defmethod execute! ::JobCreate [{:keys [bigquery jobInfo opts]}]
  (let [client (client bigquery)
        jobInfo (JobInfo/from-edn jobInfo)
        opts (BQ/JobOption-Array-from-edn opts)]
    (Job/to-edn (.create client jobInfo opts))))

(defmethod execute! ::JobGet [{:keys [bigquery jobId opts]}]
  (let [client (client bigquery)
        jobId (JobId/from-edn jobId)
        opts (BQ/JobOption-Array-from-edn opts)]
    (Job/to-edn (.getJob client jobId opts))))

(defmethod execute! ::JobDelete [{:keys [bigquery jobId]}]
  (let [client (client bigquery)
        jobId (JobId/from-edn jobId)]
    (.delete client jobId)))

(defmethod execute! ::Query [{:keys [bigquery configuration jobId opts]}]
  (let [client (client bigquery)
        configuration (QJC/from-edn configuration)
        jobId (some-> jobId JobId/from-edn)
        opts (BQ/JobOption-Array-from-edn opts)]
    (custom/TableResult-to-edn
      (if jobId
        (.query client configuration jobId opts)
        (.query client configuration opts)))))

(defmethod execute! ::QueryWithTimeout [{:keys [bigquery configuration jobId timeoutMs opts]}]
  (let [client (client bigquery)
        configuration (QJC/from-edn configuration)
        jobId (JobId/from-edn jobId)
        timeoutMs (long timeoutMs)
        opts (BQ/JobOption-Array-from-edn opts)]
    (.queryWithTimeout client configuration jobId timeoutMs opts)))

#!----------------------------------------------------------------------------------------------------------------------

(defmethod execute! ::RoutineList [{:keys [bigquery datasetId opts]}]
  (let [client (client bigquery)
        datasetId (DatasetId/from-edn datasetId)
        opts (BQ/RoutineListOption-Array-from-edn opts)
        res (.listRoutines client datasetId opts)]
    (map Routine/to-edn (seq (.iterateAll res)))))

(defmethod execute! ::RoutineCreate [{:keys [bigquery routineInfo opts]}]
  (let [client (client bigquery)
        routineInfo (RoutineInfo/from-edn routineInfo)
        opts (BQ/RoutineOption-Array-from-edn opts)]
    (Routine/to-edn (.create client routineInfo opts))))

(defmethod execute! ::RoutineUpdate [{:keys [bigquery routineInfo opts]}]
  (let [client (client bigquery)
        routineInfo (RoutineInfo/from-edn routineInfo)
        opts (BQ/RoutineOption-Array-from-edn opts)]
    (Routine/to-edn (.update client routineInfo opts))))

(defmethod execute! ::RoutineGet [{:keys [bigquery routineId opts]}]
  (let [client (client bigquery)
        routineId (RoutineId/from-edn routineId)
        opts (BQ/RoutineOption-Array-from-edn opts)]
    (Routine/to-edn (.getRoutine client routineId opts))))

(defmethod execute! ::RoutineDelete [{:keys [bigquery routineId]}]
  (let [client (client bigquery)
        routineId (RoutineId/from-edn routineId)]
    (.delete client routineId)))

#!----------------------------------------------------------------------------------------------------------------------

(defmethod execute! ::ModelList [{:keys [bigquery datasetId opts]}]
  (let [client (client bigquery)
        datasetId (DatasetId/from-edn datasetId)
        opts (BQ/ModelListOption-Array-from-edn opts)
        res (.listModels client datasetId opts)]
    (map Model/to-edn (seq (.iterateAll res)))))

(defmethod execute! ::ModelUpdate [{:keys [bigquery modelInfo opts]}]
  (let [client (client bigquery)
        modelInfo (ModelInfo/from-edn modelInfo)
        opts (BQ/ModelOption-Array-from-edn opts)]
    (Model/to-edn (.update client modelInfo opts))))

(defmethod execute! ::ModelGet [{:keys [bigquery modelId opts]}]
  (let [client (client bigquery)
        modelId (ModelId/from-edn modelId)
        opts (BQ/ModelOption-Array-from-edn opts)]
    (Model/to-edn (.getModel client modelId opts))))

(defmethod execute! ::ModelDelete [{:keys [bigquery modelId]}]
  (let [client (client bigquery)
        modelId (ModelId/from-edn modelId)]
    (.delete client modelId)))

#!----------------------------------------------------------------------------------------------------------------------

(defmethod execute! ::GetIamPolicy [{:keys [bigquery tableId opts]}]
  (let [client (client bigquery)
        tableId (TableId/from-edn tableId)
        opts (BQ/IAMOption-Array-from-edn opts)]
    (cloud/Policy-to-edn (.getIamPolicy client tableId opts))))

(defmethod execute! ::SetIamPolicy [{:keys [bigquery tableId policy opts]}]
  (let [client (client bigquery)
        tableId (TableId/from-edn tableId)
        policy (cloud/Policy-from-edn policy)
        opts (BQ/IAMOption-Array-from-edn opts)]
    (cloud/Policy-to-edn (.setIamPolicy client tableId policy opts))))

(defmethod execute! ::TestIamPermissions [{:keys [bigquery tableId permissions opts]}]
  (let [client (client bigquery)
        tableId (TableId/from-edn tableId)
        permissions (vec permissions)
        opts (BQ/IAMOption-Array-from-edn opts)]
    (vec (.testIamPermissions client tableId permissions opts))))

#!----------------------------------------------------------------------------------------------------------------------

(defmethod execute! ::ConnectionCreate [{:keys [bigquery connectionSettings]}]
  (let [client (client bigquery)]
    (if connectionSettings
      (.createConnection client (ConnectionSettings/from-edn connectionSettings))
      (.createConnection client))))

(defmethod execute! ::Writer [{:keys [bigquery jobId writeChannelConfiguration]}]
  (let [client (client bigquery)
        writeChannelConfiguration (WriteChannelConfiguration/from-edn writeChannelConfiguration)]
    (if jobId
      (.writer client (JobId/from-edn jobId) writeChannelConfiguration)
      (.writer client writeChannelConfiguration))))
