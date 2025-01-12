(ns gcp.bigquery.v2
  (:require [gcp.bigquery.v2.BigQueryOptions :as bqo]
            [gcp.global :as global])
  (:import (com.google.cloud TransportOptions)
           (com.google.gson JsonObject)
           (java.time LocalDate LocalDateTime)))

(def registry
  {:gcp.core/RetryOption                  :any
   :gcp.synth/labels                      [:map-of :string :string] ;TODO ..lowercase, char range etc

   :bigquery.synth/location               :string
   :bigquery.synth/project                :string
   :bigquery.synth/dataset                :string
   :bigquery.synth/table                  :string

   ;;-------------------------------
   ;; Client

   :bigquery/BigQueryOptions              [:maybe
                                           [:map
                                            [:location {:optional true} :string]
                                            [:transportOptions {:optional true} (global/instance-schema TransportOptions)]
                                            [:useInt64Timestamps {:optional true} :boolean]]]

   :bigquery.synth/client                 [:fn
                                           {:error/message "expected bigquery client instance"
                                            :from-edn      bqo/get-service}
                                           #(instance? com.google.cloud.bigquery.BigQuery %)]

   :bigquery.synth/clientable             [:or
                                           :bigquery/BigQueryOptions
                                           :bigquery.synth/client
                                           [:map [:bigquery [:or :bigquery/BigQueryOptions :bigquery.synth/client]]]]

   ;;-------------------------------

   :bigquery.BigQuery/IAMOption           [:map {:closed true} [:version :string]]
   :bigquery.BigQuery/ModelListOption     {}
   :bigquery.BigQuery/ModelOption         {}
   :bigquery.BigQuery/QueryOption         {}
   :bigquery.BigQuery/QueryResultsOption  {}
   :bigquery.BigQuery/RoutineListOption   {}
   :bigquery.BigQuery/RoutineOption       {}
   :bigquery.BigQuery/TableDataListOption {}

   ;;--------------------------------------------------------------------------
   ;; Datasets

   :bigquery.BigQuery/DatasetDeleteOption [:map {:closed true} [:deleteContents {:doc "truthy. non-empty datasets throw otherwise"} :any]]

   :bigquery.BigQuery/DatasetListOption   [:or
                                           {:doc "union-map :all|:labelFilter|:pageSize|:pageToken"}
                                           [:map {:closed true} [:all {:doc "returns hidden" :optional true} :any]]
                                           [:map {:closed true} [:labelFilter :string]]
                                           [:map {:closed true} [:pageSize :int]]
                                           [:map {:closed true} [:pageToken :string]]]

   :bigquery.BigQuery/DatasetOption       [:map {:closed true} [:fields [:sequential :bigquery.BigQuery/DatasetField]]]

   :bigquery.synth/DatasetCreate          [:map
                                           {:doc "create the dataset defined in :datasetInfo"}
                                           [:bigquery {:optional true} :bigquery.synth/clientable]
                                           [:datasetInfo :bigquery/DatasetInfo]
                                           [:options {:optional true} [:sequential :bigquery.BigQuery/DatasetOption]]]
   :bigquery.synth/DatasetGet             [:map
                                           [:bigquery {:optional true} :bigquery.synth/clientable]
                                           [:datasetId :bigquery/DatasetId]
                                           [:options {:optional true} [:sequential :bigquery.BigQuery/DatasetOption]]]
   :bigquery.synth/DatasetList            [:maybe
                                           [:map
                                            [:bigquery {:optional true} :bigquery.synth/clientable]
                                            [:projectId {:optional true} :bigquery.synth/project]
                                            [:options {:optional true} [:sequential :bigquery.BigQuery/DatasetListOption]]]]
   :bigquery.synth/DatasetUpdate          [:map
                                           {:doc "update the dataset defined in :datasetInfo"}
                                           [:bigquery {:optional true} :bigquery.synth/clientable]
                                           [:datasetInfo :bigquery/DatasetInfo]
                                           [:options {:optional true} [:sequential :bigquery.BigQuery/DatasetOption]]]

   :bigquery.synth/DatasetDelete          [:map
                                           {:doc "update the dataset defined in :datasetInfo"}
                                           [:bigquery {:optional true} :bigquery.synth/clientable]
                                           [:datasetId :bigquery/DatasetId]
                                           [:options {:optional true} [:sequential :bigquery.BigQuery/DatasetDeleteOption]]]


   :bigquery/DatasetId                    [:map {:closed true}
                                           [:dataset {:optional true} :bigquery.synth/dataset]
                                           [:project {:optional true} :bigquery.synth/project]]

   :bigquery/DatasetInfo                  [:map
                                           [:acl {:optional true} [:sequential :bigquery/Acl]]
                                           [:datasetId :bigquery/DatasetId]
                                           [:defaultCollation {:optional true} :string]
                                           [:defaultEncryptionConfiguration {:optional true} :bigquery/EncryptionConfiguration]
                                           [:defaultPartitionExpirationMs {:optional true} :int]
                                           [:defaultTableLifetime {:optional true} :int]
                                           [:description {:optional true} :string]
                                           [:externalDatasetReference {:optional true} :bigquery/ExternalDatasetReference]
                                           [:friendlyName {:optional true} :string]
                                           [:labels {:optional true} :gcp.synth/labels]
                                           [:location {:optional true} :bigquery.synth/location]
                                           [:maxTimeTravelHours {:optional true} :int]
                                           [:storageBillingModel {:optional true} [:enum "PHYSICAL" "LOGICAL"]]]

   :bigquery/Dataset                      [:and
                                           :bigquery/DatasetInfo
                                           [:map
                                            [:bigquery :bigquery.synth/clientable]
                                            [:generatedId {:optional true} :string]
                                            [:etag {:optional true} :string]]]

   ;;--------------------------------------------------------------------------
   ;; Tables

   :bigquery.BigQuery/TableListOption     [:or
                                           [:map {:closed true} [:pageSize :int]]
                                           [:map {:closed true} [:pageToken :string]]]

   :bigquery.BigQuery/TableOption         [:or
                                           [:map {:closed true} [:autoDetect :boolean]]
                                           [:map {:closed true} [:fields :bigquery.BigQuery/TableField]]
                                           [:map {:closed true} [:tableMetadataView :bigquery.BigQuery/TableMetadataView]]]

   :bigquery/TableId                      [:map
                                           [:dataset :bigquery.synth/dataset]
                                           [:project {:optional true} :bigquery.synth/project]
                                           [:table :bigquery.synth/table]]

   ;;--------------------------------------------------------------------------
   ;; Jobs

   :bigquery.BigQuery/JobListOption       [:or
                                           [:map {:closed true} [:fields [:sequential :bigquery/JobField]]]
                                           [:map {:closed true} [:maxCreationTime :int]]
                                           [:map {:closed true} [:minCreationTime :int]]
                                           [:map {:closed true} [:pageSize :int]]
                                           [:map {:closed true} [:pageToken :string]]
                                           [:map {:closed true} [:parentJobId :string]]
                                           [:map {:closed true} [:stateFilters [:sequential :bigquery.JobStatus/State]]]]

   :bigquery.BigQuery/JobOption           [:or
                                           {:doc "union-map :bigQueryRetryConfig|:fields|:retryOptions"}
                                           [:map [:bigQueryRetryConfig :bigquery/BigQueryRetryConfig]]
                                           [:map [:fields [:sequential :bigquery/JobField]]]
                                           [:map [:options [:sequential :gcp.core/RetryOption]]]]

   :bigquery/JobId                        [:map
                                           [:job {:optional true} :string]
                                           [:project {:optional true} :bigquery.synth/project]]
   :bigquery/JobInfo                      [:map
                                           [:jobId {:optional true} :bigquery/JobId]
                                           [:configuration :bigquery/JobConfiguration]]

   :bigquery.JobInfo/CreateDisposition    [:enum "CREATE_IF_NEEDED" "CREATE_NEVER"]

   :bigquery.JobInfo/WriteDisposition     [:enum "WRITE_APPEND" "WRITE_EMPTY" "WRITE_TRUNCATE"]

   :bigquery.JobInfo/SchemaUpdateOption   [:enum "ALLOW_FIELD_ADDITION" "ALLOW_FIELD_RELAXATION"]

   :bigquery/JobField                     [:enum "CONFIGURATION" "ETAG" "ID" "JOB_REFERENCE" "SELF_LINK" "STATISTICS" "STATUS" "USER_EMAIL"]

   :bigquery/JobConfiguration             [:or
                                           {:doc   "abstract class for Copy/Extract/Load/Query configs"
                                            :class 'com.google.cloud.bigquery.JobConfiguration}
                                           :bigquery/CopyJobConfiguration
                                           :bigquery/ExtractJobConfiguration
                                           :bigquery/LoadJobConfiguration
                                           :bigquery/QueryJobConfiguration]

   :bigquery/CopyJobConfiguration         [:map
                                           [:destinationTable :bigquery/TableId]
                                           [:sourceTables {:min 1} [:sequential :bigquery/TableId]]
                                           [:createDisposition {:optional true} :bigquery.JobInfo/CreateDisposition]
                                           [:encryptionConfiguration {:optional true} :bigquery/EncryptionConfiguration]
                                           [:destinationExpiration {:optional true} :string]
                                           [:jobTimeoutMs {:optional true} :int]
                                           [:labels {:optional true} :gcp.synth/labels]
                                           [:operationType {:optional true} [:enum "COPY" "CLONE" "SNAPSHOT" "RESTORE"]]
                                           [:writeDisposition {:optional true} :bigquery.JobInfo/WriteDisposition]]

   :bigquery/ExtractJobConfiguration      :any

   :bigquery/LoadJobConfiguration         :any

   :bigquery/QueryJobConfiguration        [:map
                                           {:closed true
                                            :class  'com.google.cloud.bigquery.QueryJobConfiguration}
                                           [:allowLargeResults {:optional true} boolean?]
                                           [:clustering {:optional true} :bigquery/Clustering]
                                           [:connectionProperties
                                            {:optional true}
                                            [:or [:map-of :string :string]
                                             [:sequential :bigquery/ConnectionProperty]]] ;; Sequential of connection properties
                                           [:createDisposition {:optional true} :bigquery.JobInfo/CreateDisposition]
                                           [:createSession {:optional true} boolean?]
                                           [:defaultDataset {:optional true} :bigquery/DatasetId]
                                           [:destinationTable {:optional true} :bigquery/TableId]
                                           [:dryRun {:optional true} boolean?]
                                           [:encryptionConfiguration {:optional true} :bigquery/EncryptionConfiguration]
                                           [:flattenResults {:optional true} boolean?]
                                           [:jobTimeoutMs {:optional true} number?]
                                           [:labels {:optional true} :gcp.synth/labels]
                                           [:maxResults {:optional true} number?]
                                           [:maximumBillingTier {:optional true} number?]
                                           [:maximumBytesBilled {:optional true} number?]
                                           [:priority {:optional true} [:enum "BATCH" "INTERACTIVE"]]
                                           [:query string?]
                                           [:queryParameters {:optional true} [:seqable :bigquery/QueryParameterValue]]
                                           [:rangePartitioning {:optional true} :bigquery/RangePartitioning]
                                           [:schemaUpdateOptions
                                            {:optional true
                                             :doc      "Specifies options relating to allowing the schema of the destination table to be updated as a side effect of the load or query job."}
                                            [:sequential :bigquery.JobInfo/SchemaUpdateOption]]
                                           [:tableDefinitions {:optional true} [:map-of string? :bigquery/ExternalTableDefinition]]
                                           [:timePartitioning {:optional true} :bigquery/TimePartitioning]
                                           [:useLegacySql {:optional true} boolean?]
                                           [:useQueryCache {:optional true} boolean?]
                                           [:userDefinedFunctions {:optional true} [:sequential :bigquery/UserDefinedFunctions]]
                                           [:writeDisposition {:optional true} :bigquery.JobInfo/WriteDisposition]]

   :bigquery.synth/JobCreate              [:map
                                           {:doc "create a job described in :jobInfo"}
                                           [:bigquery :bigquery.synth/clientable]
                                           [:jobInfo :bigquery/JobInfo]
                                           [:options {:optional true} [:sequential :bigquery/JobOption]]]

   :bigquery.synth/Query                  [:map
                                           {:doc "execute a QueryJobConfiguration"}
                                           [:bigquery :bigquery.synth/clientable]
                                           [:configuration :bigquery/QueryJobConfiguration]
                                           [:options {:optional true} [:sequential :bigquery/JobOption]]
                                           [:jobId {:optional true} :bigquery/JobId]]

   #!--------------------------------------------------------------------------

   :bigquery/Acl                          [:map]

   :bigquery/BigQueryRetryConfig          [:and
                                           {:doc "part of JobOption"}
                                           [:map
                                            [:errorMessages {:optional true} [:sequential :string]]
                                            [:regExPatterns {:optional true} [:sequential :string]]]
                                           [:fn
                                            {:error/message "must be one of :errorMessages or :regExPatterns"}
                                            (fn [m] (or (contains? m :errorMessages) (contains? m :regExPatterns)))]]


   :bigquery/Clustering                   [:map {:closed true} [:fields [:sequential :string]]]
   :bigquery/ConnectionProperty           [:or
                                           {:error/message "must be single-entry map or [string string] tuple"}
                                           [:and
                                            [:map-of :string :string]
                                            [:fn #(= 1 (count %))]]
                                           [:tuple :string :string]]
   :bigquery/EncryptionConfiguration      [:map {:closed true} [:kmsKeyName :string]]
   :bigquery/ExternalDatasetReference     :any
   :bigquery/ExternalTableDefinition      :any
   :bigquery/RangePartitioning            [:map {:closed true}
                                           [:field :string]
                                           [:range [:map {:closed true}
                                                    [:start :int]
                                                    [:end :int]
                                                    [:interval :int]]]]
   :bigquery/TimePartitioning             [:map {:closed true}
                                           [:expirationMs :int]
                                           [:field :string]
                                           [:requiredPartitionFilter :boolean]]
   :bigquery.UserDefinedFunction          [:map
                                           {:doc "https://cloud.google.com/bigquery/docs/user-defined-functions"}
                                           [:type [:enum "INLINE" "FROM_URI"]]
                                           [:functionDefinition :string]]

   :bigquery/QueryParameterValue          [:or
                                           {:class 'com.google.cloud.bigquery.QueryParameterValue}
                                           :string
                                           :boolean
                                           :int
                                           :float
                                           decimal?
                                           bytes?
                                           inst?
                                           (global/instance-schema JsonObject)
                                           (global/instance-schema LocalDate)
                                           (global/instance-schema LocalDateTime)
                                           [:map {:closed true}
                                            [:interval :string]]
                                           [:map {:closed true}
                                            [:geography :string]]
                                           [:map-of :string [:ref :bigquery/QueryParameterValue]]
                                           [:sequential [:ref :bigquery/QueryParameterValue]]]
   ;;-------------------------------
   ;; enums

   :bigquery.BigQuery/DatasetField        [:enum "ACCESS" "CREATION_TIME" "DATASET_REFERENCE"
                                           "DEFAULT_TABLE_EXPIRATION_MS" "DESCRIPTION" "ETAG"
                                           "FRIENDLY_NAME" "ID" "LABELS" "LAST_MODIFIED_TIME"
                                           "LOCATION" "SELF_LINK"]

   :bigquery.BigQuery/TableField          [:enum
                                           "CREATION_TIME"
                                           "DESCRIPTION"
                                           "ETAG"
                                           "EXPIRATION_TIME"
                                           "EXTERNAL_DATA_CONFIGURATION"
                                           "FRIENDLY_NAME"
                                           "ID"
                                           "LABELS"
                                           "LAST_MODIFIED_TIME"
                                           "LOCATION"
                                           "NUM_BYTES"
                                           "NUM_LONG_TERM_BYTES"
                                           "NUM_ROWS"
                                           "RANGE_PARTITIONING"
                                           "SCHEMA"
                                           "SELF_LINK"
                                           "STREAMING_BUFFER"
                                           "TABLE_REFERENCE"
                                           "TIME_PARTITIONING"
                                           "TYPE"
                                           "VIEW"]

   :bigquery.BigQuery/TableMetadataView   [:enum "BASIC" "FULL" "STORAGE_STATS" "TABLE_METADATA_VIEW_UNSPECIFIED"]

   :bigquery.JobStatus/State              [:enum "DONE" "PENDING" "RUNNING"]

   :bigquery.StandardSQLTypeName          [:or
                                           {:url "https://cloud.google.com/bigquery/docs/reference/standard-sql/data-types"}
                                           [:= {:doc "Ordered list of zero or more elements of any non-array type."} "ARRAY"]
                                           [:= {:doc "A decimal value with 76+ digits of precision (the 77th digit is partial) and 38 digits of scale."} "BIGNUMERIC"]
                                           [:= {:doc "A Boolean value (true or false)."} "BOOL"]
                                           [:= {:doc "Variable-length binary data."} "BYTES"]
                                           [:= {:doc "Represents a logical calendar date. Values range between the years 1 and 9999, inclusive."} "DATE"]
                                           [:= {:doc "Represents a year, month, day, hour, minute, second, and subsecond (microsecond precision)."} "DATETIME"]
                                           [:= {:doc "A 64-bit IEEE binary floating-point value."} "FLOAT64"]
                                           [:= {:doc "Represents a set of geographic points, represented as a Well Known Text (WKT) string."} "GEOGRAPHY"]
                                           [:= {:doc "A 64-bit signed integer value."} "INT64"]
                                           [:= {:doc "Represents duration or amount of time."} "INTERVAL"]
                                           [:= {:doc "Represents JSON data."} "JSON"]
                                           [:= {:doc "A decimal value with 38 digits of precision and 9 digits of scale."} "NUMERIC"]
                                           [:= {:doc "Represents a contiguous range of values."} "RANGE"]
                                           [:= {:doc "Variable-length character (Unicode) data."} "STRING"]
                                           [:= {:doc "Container of ordered fields each with a type (required) and field name (optional)."} "STRUCT"]
                                           [:= {:doc "Represents a time, independent of a specific date, to microsecond precision."} "TIME"]
                                           [:= {:doc "Represents an absolute point in time, with microsecond precision. Values range between the years 1 and 9999, inclusive."} "TIMESTAMP"]]})

(global/include! registry)