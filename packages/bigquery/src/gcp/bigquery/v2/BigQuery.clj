(ns gcp.bigquery.v2.BigQuery
  (:require [gcp.global :as global]
            [gcp.bigquery.v2.BigQueryOptions]
            [gcp.bigquery.v2.BigQueryRetryConfig :as BigQueryRetryConfig]
            [gcp.core.RetryOption :as RO])
  (:import (com.google.cloud RetryOption)
           (com.google.cloud.bigquery BigQuery$DatasetDeleteOption BigQuery$DatasetField BigQuery$DatasetListOption BigQuery$DatasetOption BigQuery$IAMOption BigQuery$JobField BigQuery$JobListOption BigQuery$JobOption BigQuery$ModelListOption BigQuery$ModelOption BigQuery$QueryOption BigQuery$QueryResultsOption BigQuery$RoutineListOption BigQuery$RoutineOption BigQuery$TableDataListOption BigQuery$TableField BigQuery$TableMetadataView BigQuery$TableListOption BigQuery$TableOption JobStatus$State)))

(defn ^BigQuery$DatasetDeleteOption DatasetDeleteOption:from-edn [arg]
  (global/strict! :gcp.bigquery.v2/BigQuery.DatasetDeleteOption arg)
  (if (contains? arg :deleteContents)
    (BigQuery$DatasetDeleteOption/deleteContents)
    (throw (ex-info "bad arg to DatasetDeleteOption" {:arg arg}))))

(defn ^BigQuery$DatasetListOption DatasetListOption:from-edn
  [{:keys [labelFilter pageSize pageToken] :as arg}]
  (global/strict! :gcp.bigquery.v2/BigQuery.DatasetListOption arg)
  (if (contains? arg :all)
    (BigQuery$DatasetListOption/all)
    (if labelFilter
      (BigQuery$DatasetListOption/labelFilter labelFilter)
      (if pageSize
        (BigQuery$DatasetListOption/pageSize pageSize)
        (BigQuery$DatasetListOption/pageToken pageToken)))))

(defn ^BigQuery$DatasetOption DatasetOption:from-edn
  [{:keys [fields] :as arg}]
  (global/strict! [:sequential :gcp.bigquery.v2/BigQuery.DatasetField] arg) ;; Was DatasetOption in v2.clj but here it validates fields directly? No, arg is map with fields. v2.clj had [:map {:closed true} [:fields [:sequential ...]]]
  (let [fields (map #(BigQuery$DatasetField/valueOf %) fields)]
    (BigQuery$DatasetOption/fields (into-array BigQuery$DatasetField fields))))

(defn ^BigQuery$IAMOption IAMOption:from-edn
  [{:keys [version]}]
  (BigQuery$IAMOption/requestedPolicyVersion (long version)))

(defn ^BigQuery$JobListOption JobListOption:from-edn
  [{:keys [fields
           maxCreationTime
           minCreationTime
           pageSize
           pageToken
           parentJobId
           stateFilters] :as arg}]
  (global/strict! :gcp.bigquery.v2/BigQuery.JobListOption arg)
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

(defn ^BigQuery$JobOption JobOption:from-edn [arg]
  (global/strict! :gcp.bigquery.v2/BigQuery.JobOption arg)
  (if (contains? arg :fields)
    (BigQuery$JobOption/fields (into-array BigQuery$JobField (map #(BigQuery$JobField/valueOf %) (:fields arg))))
    (if (contains? arg :gcp.bigquery.v2/BigQueryRetryConfig)
      (BigQuery$JobOption/bigQueryRetryConfig (BigQueryRetryConfig/from-edn (:gcp.bigquery.v2/BigQueryRetryConfig arg)))
      (BigQuery$JobOption/retryOptions (into-array RetryOption (map RO/from-edn (:options arg)))))))

(defn ^BigQuery$ModelListOption ModelListOption:from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn ^BigQuery$ModelOption ModelOption:from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn ^BigQuery$QueryOption QueryOption:from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn ^BigQuery$QueryResultsOption QueryResultsOption:from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn ^BigQuery$RoutineListOption RoutineListOption:from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn ^BigQuery$RoutineOption RoutineOption:from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn ^BigQuery$TableDataListOption TableDataListOption:from-edn
  [{:keys [pageSize pageToken] :as arg}]
  (global/strict! :gcp.bigquery.v2/BigQuery.TableListOption arg) ;; Reuse TableListOption schema for now? Or TableDataListOption? v2.clj had TableDataListOption defined as :any
  (if pageSize
    (BigQuery$TableDataListOption/pageSize (long pageSize))
    (BigQuery$TableDataListOption/pageToken pageToken)))

(defn ^BigQuery$TableListOption TableListOption:from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn ^BigQuery$TableOption TableOption:from-edn
  [{:keys [autoDetect fields tableMetadataView] :as arg}]
  (global/strict! :gcp.bigquery.v2/BigQuery.TableOption arg)
  (if autoDetect
    (BigQuery$TableOption/autodetectSchema autoDetect)
    (if tableMetadataView
      (BigQuery$TableOption/tableMetadataView (BigQuery$TableMetadataView/valueOf tableMetadataView))
      (BigQuery$TableOption/fields (into-array BigQuery$TableField (map #(BigQuery$TableField/valueOf %) fields))))))

(def schemas
  {:gcp.bigquery.v2/BigQuery
   (assoc-in (global/instance-schema com.google.cloud.bigquery.BigQuery)
             [1 :from-edn] 'gcp.bigquery.v2.BigQueryOptions/get-service)

   :gcp.bigquery.v2/Clientable
   [:maybe
    [:or
     :gcp.bigquery.v2/BigQueryOptions
     :gcp.bigquery.v2/BigQuery
     [:map [:bigquery [:or :gcp.bigquery.v2/BigQueryOptions :gcp.bigquery.v2/BigQuery]]]]]

   :gcp.bigquery.v2/BigQuery.DatasetDeleteOption [:map {:closed true} [:deleteContents {:doc "truthy. non-empty datasets throw otherwise"} :any]]

   :gcp.bigquery.v2/BigQuery.DatasetListOption   [:or
                                               {:doc "union-map :all|:labelFilter|:pageSize|:pageToken"}
                                               [:map {:closed true} [:all {:doc "returns hidden" :optional true} :any]]
                                               [:map {:closed true} [:labelFilter :string]]
                                               [:map {:closed true} [:pageSize :int]]
                                               [:map {:closed true} [:pageToken :string]]]

   :gcp.bigquery.v2/BigQuery.DatasetOption       [:map {:closed true} [:fields [:sequential :gcp.bigquery.v2/BigQuery.DatasetField]]]
   :gcp.bigquery.v2/BigQuery.DatasetField [:enum "ID" "LABELS" "FRIENDLY_NAME" "DESCRIPTION" "SELF_LINK" "ETAG" "DEFAULT_TABLE_EXPIRATION_MS" "DATASET_REFERENCE" "LAST_MODIFIED_TIME" "LOCATION" "CREATION_TIME" "ACCESS"]

   :gcp.bigquery.v2/BigQuery.IAMOption           [:map {:closed true} [:version :string]]

   :gcp.bigquery.v2/BigQuery.JobListOption       [:or
                                               [:map {:closed true} [:fields [:sequential :gcp.bigquery.v2/BigQuery.JobField]]]
                                               [:map {:closed true} [:maxCreationTime :int]]
                                               [:map {:closed true} [:minCreationTime :int]]
                                               [:map {:closed true} [:pageSize :int]]
                                               [:map {:closed true} [:pageToken :string]]
                                               [:map {:closed true} [:parentJobId :string]]
                                               [:map {:closed true} [:stateFilters [:sequential :gcp.bigquery.v2/JobStatus.State]]]]
   :gcp.bigquery.v2/BigQuery.JobField [:enum "USER_EMAIL" "ID" "STATISTICS" "SELF_LINK" "CONFIGURATION" "ETAG" "JOB_REFERENCE" "STATUS"]
   :gcp.bigquery.v2/JobStatus.State [:enum "RUNNING" "DONE" "PENDING"]

   :gcp.bigquery.v2/BigQuery.JobOption           [:or
                                               {:doc "union-map :gcp.bigquery.v2/BigQueryRetryConfig|:fields|:retryOptions"}
                                               [:map [:BigQueryRetryConfig :gcp.bigquery.v2/BigQueryRetryConfig]]
                                               [:map [:fields [:sequential :gcp.bigquery.v2/BigQuery.JobField]]]
                                               [:map [:options [:sequential :gcp.core/RetryOption]]]]

   :gcp.bigquery.v2/BigQuery.ModelListOption     :any
   :gcp.bigquery.v2/BigQuery.ModelOption         :any
   :gcp.bigquery.v2/BigQuery.QueryOption         :any
   :gcp.bigquery.v2/BigQuery.QueryResultsOption  :any
   :gcp.bigquery.v2/BigQuery.RoutineListOption   :any
   :gcp.bigquery.v2/BigQuery.RoutineOption       :any
   :gcp.bigquery.v2/BigQuery.TableDataListOption :any

   :gcp.bigquery.v2/BigQuery.TableListOption     [:or
                                               [:map {:closed true} [:pageSize :int]]
                                               [:map {:closed true} [:pageToken :string]]]

   :gcp.bigquery.v2/BigQuery.TableOption         [:or
                                               [:map {:closed true} [:autoDetect :boolean]]
                                               [:map {:closed true} [:fields :gcp.bigquery.v2/BigQuery.TableField]]
                                               [:map {:closed true} [:tableMetadataView :gcp.bigquery.v2/BigQuery.TableMetadataView]]]
   :gcp.bigquery.v2/BigQuery.TableField [:enum "ID" "LABELS" "FRIENDLY_NAME" "NUM_LONG_TERM_BYTES" "EXPIRATION_TIME" "SCHEMA" "VIEW" "STREAMING_BUFFER" "DESCRIPTION" "SELF_LINK" "EXTERNAL_DATA_CONFIGURATION" "ETAG" "NUM_ROWS" "TIME_PARTITIONING" "TYPE" "LAST_MODIFIED_TIME" "LOCATION" "TABLE_REFERENCE" "CREATION_TIME" "RANGE_PARTITIONING" "NUM_BYTES"]
   :gcp.bigquery.v2/BigQuery.TableMetadataView [:enum "BASIC" "FULL" "TABLE_METADATA_VIEW_UNSPECIFIED" "STORAGE_STATS"]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))
