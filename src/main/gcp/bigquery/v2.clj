(ns gcp.bigquery.v2
  (:require [clojure.edn :as edn]
            [gcp.bigquery.v2.BigQueryOptions :as bqo]
            [gcp.global :as global])
  (:import (com.google.cloud TransportOptions)
           (com.google.gson JsonObject)
           (java.time LocalDate LocalDateTime)))

(def registry
  {:gcp.core/RetryOption                  :any
   :gcp.synth/labels                      [:map-of :string :string] ;TODO ..lowercase, char range etc
   :gcp.synth/resourceTags                [:map-of :string :string]

   :bigquery.synth/location               :string
   :bigquery.synth/project                :string
   :bigquery.synth/dataset                :string
   :bigquery.synth/table                  :string
   :bigquery.synth/uri                    :string           ;; export supports single wildcard
   :bigquery.synth/compression            [:enum "GZIP" "DEFLATE" "SNAPPY"]
   :bigquery.synth/format                 [:enum
                                           {:doc "The default value for tables is CSV. Tables with nested or repeated fields cannot be exported as CSV. The default value for models is ML_TF_SAVED_MODEL."}
                                           "AVRO" "CSV" "PARQUET" "NEWLINE_DELIMITED_JSON"
                                           "ML_TF_SAVED_MODEL" "ML_XGBOOST_BOOSTER"]

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
   :bigquery.BigQuery/RoutineListOption   :any
   :bigquery.BigQuery/RoutineOption       :any
   :bigquery.BigQuery/TableDataListOption {}

   ;;--------------------------------------------------------------------------
   ;; Routines

   :bigquery.synth/RoutineList
   [:map {:closed false}
    [:bigquery {:optional true} :bigquery.synth/clientable]
    [:datasetId :bigquery/DatasetId]
    [:options {:optional true} [:maybe [:sequential :bigquery.BigQuery/RoutineListOption]]]]

   :bigquery.synth/RoutineCreate
   [:map {:closed false}
    [:bigquery {:optional true} :bigquery.synth/clientable]
    [:routineInfo :bigquery/RoutineInfo]
    [:options {:optional true} [:maybe [:sequential :bigquery.BigQuery/RoutineOption]]]]

   :bigquery.synth/RoutineGet
   [:map {:closed false}
    [:bigquery {:optional true} :bigquery.synth/clientable]
    [:routineId :bigquery/RoutineId]
    [:options {:optional true} [:maybe [:sequential :bigquery.BigQuery/RoutineOption]]]]

   :bigquery.synth/RoutineDelete
   [:map {:closed false}
    [:bigquery {:optional true} :bigquery.synth/clientable]
    [:routineId :bigquery/RoutineId]]

   :bigquery.synth/RoutineUpdate
   [:map {:closed false}
    [:bigquery {:optional true} :bigquery.synth/clientable]
    [:routineInfo :bigquery/RoutineInfo]
    [:options {:optional true} [:maybe [:sequential :bigquery.BigQuery/RoutineOption]]]]

   ;;--------------------------------------------------------------------------

   :bigquery/RoutineId
   [:map
    [:project {:optional true} :string]
    [:dataset :string]
    [:routine :string]]

   :bigquery/RoutineArgument
   [:map
    [:kind
     {:doc      "A FIXED_TYPE argument is a fully specified type. It can be a struct or an array, but not a table. An ANY_TYPE argument is any type. It can be a struct or an array, but not a table."
      :optional true}
     [:enum "FIXED_TYPE" "ANY_TYPE"]]
    [:mode
     {:doc      "An IN mode argument is input-only. An OUT mode argument is output-only. An INOUT mode argument is both an input and output."
      :optional true}
     [:enum "IN" "OUT" "INOUT"]]
    [:name :string]
    [:dataType :bigquery/StandardSQLDataType]]

   :bigquery/RemoteFunctionOptions
   [:map]

   :bigquery/RoutineInfo
   [:map {:closed true
          :urls   ["https://cloud.google.com/bigquery/docs/reference/rest/v2/routines"]
          :class  'com.google.cloud.bigquery.RoutineInfo}
    [:routineId :bigquery/RoutineId]
    [:arguments {:doc "Specifies the list of input/output arguments for the routine." :optional true} [:sequential :bigquery/RoutineArgument]]
    [:body {:doc      "The body of the routine. For functions, this is the expression in the AS clause. If language=SQL, it is the substring inside (but excluding) the parentheses. For example, for the function created with the following statement: CREATE FUNCTION JoinLines(x string, y string) as (concat(x, \" \", y))\n\nThe definitionBody is concat(x, \" \", y) ( is not replaced with linebreak).\n\nIf language=JAVASCRIPT, it is the evaluated string in the AS clause. For example, for the function created with the following statement:\n\nCREATE FUNCTION f() RETURNS STRING LANGUAGE js AS 'return \" \"; ' The definitionBody is return \" \"; Note that both are replaced with linebreaks"
            ;;TODO is required on write, not on reads?
            :optional true} :string]
    [:dataGovernanceType {:doc "Data governance type of the routine (e.g. DATA_MASKING)" :optional true} [:enum "DATA_GOVERNANCE_TYPE_UNSPECIFIED" "DATA_MASKING"]]
    [:determinismLevel {:doc "Determinism level for JavaScript UDFs" :optional true} [:enum "DETERMINISM_LEVEL_UNSPECIFIED" "DETERMINISTIC" "NOT_DETERMINISTIC"]]
    [:importedLibrariesList {:doc "language = \"JAVASCRIPT\", list of gs:// URLs for JavaScript libraries" :optional true} [:sequential :string]]
    [:language {:optional true} [:enum "JAVASCRIPT" "SQL"]]
    [:remoteFunctionOptions {:doc "Options for a remote function" :optional true} :bigquery/RemoteFunctionOptions]
    [:returnTableType {:doc      "Table type returned by the routine (StandardSQLTableType)"
                       :optional true}
     :bigquery/StandardSQLTableType]
    [:routineType {:doc      "Type of the routine (e.g. SCALAR_FUNCTION)"
                   :optional true}
     [:enum "ROUTINE_TYPE_UNSPECIFIED" "SCALAR_FUNCTION" "PROCEDURE" "TABLE_VALUED_FUNCTION"]]
    [:returnType {:doc      "Data type returned by the routine (StandardSQLDataType)"
                  :optional true}
     :bigquery/StandardSQLDataType]
    #!--read-only---
    [:description {:optional true :read-only? true} :string]
    [:etag {:doc "Hash of the routine resource" :optional true :read-only? true} :string]
    [:creationTime {:doc "Time (ms since epoch) the routine was created" :optional true :read-only? true} :int]
    [:lastModifiedTime {:doc "Time (ms since epoch) the routine was last modified" :optional true :read-only? true} :int]]

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
                                           [:acl {:optional true} [:seqable :bigquery/Acl]]
                                           [:datasetId :bigquery/DatasetId]
                                           [:defaultCollation {:optional true} :string]
                                           [:defaultEncryptionConfiguration {:optional true} :bigquery/EncryptionConfiguration]
                                           [:defaultPartitionExpirationMs {:optional true} :int]
                                           [:defaultTableLifetime {:optional true} :int]
                                           [:description {:optional true} :string]
                                           [:etag {:optional true} :string]
                                           [:externalDatasetReference {:optional true} :bigquery/ExternalDatasetReference]
                                           [:friendlyName {:optional true} :string]
                                           [:generatedId {:optional true} :string]
                                           [:labels {:optional true} :gcp.synth/labels]
                                           [:location {:optional true} :bigquery.synth/location]
                                           [:maxTimeTravelHours {:optional true} :int]
                                           [:storageBillingModel {:optional true} [:enum "PHYSICAL" "LOGICAL"]]]

   :bigquery/Dataset                      [:and
                                           :bigquery/DatasetInfo
                                           [:map [:bigquery :bigquery.synth/clientable]]]

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
                                           [:project {:optional true} :bigquery.synth/project]
                                           [:dataset :bigquery.synth/dataset]
                                           [:table :bigquery.synth/table]]

   :bigquery.synth/TableList              [:map
                                           [:bigquery {:optional true} :bigquery.synth/clientable]
                                           [:datasetId {:optional true} :bigquery/DatasetId]
                                           [:options {:optional true} [:sequential :bigquery.BigQuery/TableListOption]]]

   :bigquery/PolicyTags                   [:map {:closed true} [:names [:sequential :string]]]

   :bigquery/FieldElementType             [:map {:closed true} [:type :string]]

   :bigquery/Field                        [:map {:closed true}
                                           [:name :string]
                                           [:type :bigquery/StandardSQLTypeName]
                                           [:collation {:optional true} :string]
                                           [:defaultValueExpression {:optional true} :string]
                                           [:description {:optional true} :string]
                                           [:maxLength {:optional true} :int]
                                           [:mode {:optional true} [:enum "NULLABLE" "REQUIRED" "REPEATED"]]
                                           [:policyTags {:optional true} :bigquery/PolicyTags]
                                           [:precision {:optional true} :int]
                                           [:rangeElementType {:optional true} :bigquery/FieldElementType]
                                           [:scale {:optional true} :int]]

   :bigquery/Schema                       [:map {:closed true}
                                           [:fields [:sequential :bigquery/Field]]]

   :bigquery/ExternalTableDefinition      [:map {:closed true}
                                           [:type [:= "EXTERNAL"]]]

   :bigquery/MaterializedViewDefinition   [:map {:closed true}
                                           [:type [:= "MATERIALIZED_VIEW"]]
                                           [:clustering {:optional true} :bigquery/Clustering]
                                           [:enableRefresh {:optional true} :boolean]
                                           [:query :string]
                                           [:rangePartitioning {:optional true} :bigquery/RangePartitioning]
                                           [:refreshInterval {:optional true} :int]
                                           [:schema {:optional true} :bigquery/Schema]
                                           [:timePartitioning {:optional true} :bigquery/TimePartitioning]]

   :bigquery/ViewDefinition               [:map {:closed true}
                                           [:type [:= "VIEW"]]
                                           [:query :string]
                                           [:userDefinedFunctions [:seqable :bigquery/UserDefinedFunction]]]

   :bigquery/StandardTableDefinition      [:map {:closed true}
                                           [:type [:= "TABLE"]]
                                           [:bigLakeConfiguration {:optional true} :bigquery/BigLakeConfiguration]
                                           [:clustering {:optional true} :bigquery/Clustering]
                                           [:location {:optional true} :bigquery.synth/location]
                                           [:numActiveLogicalBytes {:optional true} :int]
                                           [:numActivePhysicalBytes {:optional true} :int]
                                           [:numBytes {:optional true} :int]
                                           [:numLongTermBytes {:optional true} :int]
                                           [:numLongTermLogicalBytes {:optional true} :int]
                                           [:numLongTermPhysicalBytes {:optional true} :int]
                                           [:numRows {:optional true} :int]
                                           [:numTimeTravelPhysicalBytes {:optional true} :int]
                                           [:numTotalLogicalBytes {:optional true} :int]
                                           [:numTotalPhysicalBytes {:optional true} :int]
                                           [:rangePartitioning {:optional true} :bigquery/RangePartitioning]
                                           [:schema {:optional true} :bigquery/Schema]
                                           [:streamingBuffer {:optional true} :any]
                                           [:tableConstraints {:optional true} :bigquery/TableConstraints]
                                           [:timePartitioning {:optional true} :bigquery/TimePartitioning]]

   :bigquery/TableDefinition              [:and
                                           [:map
                                            [:type [:enum "EXTERNAL" "MATERIALIZED_VIEW" "MODEL" "SNAPSHOT" "TABLE" "VIEW"]]
                                            [:schema {:optional true} :bigquery/Schema]]
                                           [:or
                                            :bigquery/ExternalTableDefinition
                                            :bigquery/MaterializedViewDefinition
                                            :bigquery/StandardTableDefinition
                                            :bigquery/ViewDefinition]]

   :bigquery/PrimaryKey                   [:map {:closed true}
                                           [:columns [:sequential {:min 1} :string]]]

   :bigquery/ColumnReference              [:map {:closed true}
                                           [:referencingColumn {:doc "The source column of this reference"} :string]
                                           [:referencedColumn {:doc "The target column of this reference"} :string]]

   :bigquery/ForeignKey                   [:map {:closed true}
                                           [:name {:doc "The name of the foreign key"} :string]
                                           [:referencedTable {:doc "The table referenced by this foreign key"} :bigquery/TableId]
                                           [:referencedColumns {:doc "The set of column references for this foreign key"} [:sequential :bigquery/ColumnReference]]]

   :bigquery/TableConstraints             [:map {:closed true}
                                           [:primaryKey {:optional true} :bigquery/PrimaryKey]
                                           [:foreignKeys {:optional true} [:sequential {:min 1} :bigquery/ForeignKey]]]

   :bigquery/CloneDefinition              [:map {:doc "read only"}
                                           [:baseTableId :bigquery/TableId]
                                           [:dateTime :string]]

   :bigquery/TableInfo                    [:map {:url "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery.TableInfo"}
                                           [:tableId :bigquery/TableId]
                                           [:etag {:optional true} :string]
                                           [:generatedId {:optional true} :string]
                                           [:definition {:optional true} :bigquery/TableDefinition]
                                           [:cloneDefinition {:optional true} :bigquery/CloneDefinition]
                                           [:defaultCollation {:optional true} :string]
                                           [:description {:optional true} :string]
                                           [:encryptionConfiguration {:optional true} :bigquery/EncryptionConfiguration]
                                           [:expirationTime {:optional true} :int]
                                           [:friendlyName {:optional true} :string]
                                           [:labels {:optional true} :gcp.synth/labels]
                                           [:requirePartitionFilter {:optional true} :boolean]
                                           [:resourceTags {:optional true} :gcp.synth/resourceTags]
                                           [:tableConstraints {:optional true} :bigquery/TableConstraints]
                                           [:creationTime {:optional true} :int]
                                           [:lastModifiedTime {:optional true} :int]
                                           [:numActiveLogicalBytes {:optional true} :int]
                                           [:numActivePhysicalBytes {:optional true} :int]
                                           [:numBytes {:optional true} :int]
                                           [:numLongTermBytes {:optional true} :int]
                                           [:numLongTermLogicalBytes {:optional true} :int]
                                           [:numLongTermPhysicalBytes {:optional true} :int]
                                           [:numRows {:optional true} [:fn #(instance? BigInteger %)]]
                                           [:numTimeTravelPhysicalBytes {:optional true} :int]
                                           [:numTotalLogicalBytes {:optional true} :int]
                                           [:numTotalPhysicalBytes {:optional true} :int]]

   :bigquery/Table                        [:and :bigquery/TableInfo [:map [:bigquery :bigquery.synth/clientable]]]

   :bigquery.synth/TableGet               [:map
                                           [:bigquery {:optional true} :bigquery.synth/clientable]
                                           [:tableId :bigquery/TableId]
                                           [:options {:optional true} [:sequential :bigquery.BigQuery/TableOption]]]

   :bigquery.synth/TableCreate            [:map
                                           [:bigquery {:optional true} :bigquery.synth/clientable]
                                           [:tableInfo :bigquery/TableInfo]
                                           [:options {:optional true} [:sequential :bigquery.BigQuery/TableOption]]]

   :bigquery.synth/TableDelete            [:map {:closed true}
                                           [:bigquery {:optional true} :bigquery.synth/clientable]
                                           [:tableId :bigquery/TableId]]

   :bigquery.synth/TableUpdate            [:map
                                           [:bigquery {:optional true} :bigquery.synth/clientable]
                                           [:tableInfo :bigquery/TableInfo]
                                           [:options {:optional true} [:sequential :bigquery.BigQuery/TableOption]]]

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

   :bigquery/JobId                        [:map {:closed true}
                                           [:job {:optional true} :string]
                                           [:location {:optional true} :bigquery.synth/location]
                                           [:project {:optional true} :bigquery.synth/project]]

   :bigquery/JobStatistics                [:map
                                           [:creationTime {:optional true} :int] ; Typically a timestamp in ms
                                           [:endTime {:optional true} :int] ; Also a timestamp in ms
                                           [:numChildJobs {:optional true} :int]
                                           [:parentJobId {:optional true} :string]
                                           [:reservationUsage
                                            {:optional true
                                             :doc      "ReservationUsage contains information about a job's usage of a single reservation."}
                                            [:map
                                             [:name :string]
                                             [:slotMs :int]]]
                                           [:scriptStatistics {:optional true}
                                            [:map {:closed true}
                                             [:evaluationKind {:doc "child job was statement or expression"} :string]
                                             [:stackFrames {:doc "Stack trace showing the line/column/procedure name of each frame on the stack at the point where the current evaluation happened. The leaf frame is first, the primary script is last. Never empty."}
                                              [:sequential
                                               [:map {:closed true}
                                                [:endColumn :int]
                                                [:endLine :int]
                                                [:procedureId :string]
                                                [:startColumn :int]
                                                [:startLine :int]
                                                [:text :string]]]]]]
                                           [:sessionInfo {:optional true
                                                          :doc      "SessionInfo contains information about the session if this job is part of one."}
                                            [:map {:closed true} [:sessionId :string]]]
                                           [:startTime {:optional true} :int] ; Timestamp in ms
                                           [:totalSlotMs {:optional true} :int]
                                           [:transactionInfo
                                            {:optional true
                                             :doc      "TransactionInfo contains information about a multi-statement transaction that may have associated with a job."}
                                            [:map {:closed true} [:transactionId :string]]]]

   :bigquery/BigQueryError                [:map
                                           [:debugInfo {:optional true} :string]
                                           [:location {:optional true} :bigquery.synth/location]
                                           [:message {:optional true} :string]
                                           [:reason {:doc "https://cloud.google.com/bigquery/docs/error-messages"} :string]]

   :bigquery/JobStatus                    [:map {:closed true}
                                           [:error {:optional true} :bigquery/BigQueryError]
                                           [:executionErrors {:optional true} [:sequential :bigquery/BigQueryError]]
                                           [:state [:enum "DONE" "PENDING" "RUNNING"]]]

   :bigquery/JobInfo                      [:map
                                           [:jobId {:optional true} :bigquery/JobId]
                                           [:configuration :bigquery/JobConfiguration]
                                           [:statistics {:optional true} :bigquery/JobStatistics]
                                           [:etag {:optional true} :string]
                                           [:generatedId {:optional true} :string]
                                           [:status {:optional true} :bigquery/JobStatus]
                                           [:userEmail {:optional true} :string]]

   :bigquery/Job                          [:and
                                           :bigquery/JobInfo
                                           [:map [:bigquery :bigquery.synth/clientable]]]

   :bigquery.JobInfo/CreateDisposition    [:enum "CREATE_IF_NEEDED" "CREATE_NEVER"]

   :bigquery.JobInfo/WriteDisposition     [:enum "WRITE_APPEND" "WRITE_EMPTY" "WRITE_TRUNCATE"]

   :bigquery.JobInfo/SchemaUpdateOption   [:enum "ALLOW_FIELD_ADDITION" "ALLOW_FIELD_RELAXATION"]

   :bigquery/JobField                     [:enum "CONFIGURATION" "ETAG" "ID" "JOB_REFERENCE" "SELF_LINK" "STATISTICS" "STATUS" "USER_EMAIL"]

   :bigquery/JobConfiguration             [:and
                                           {:doc      "abstract class for Copy/Extract/Load/Query configs"
                                            :class    'com.google.cloud.bigquery.JobConfiguration
                                            :from-edn 'gcp.bigquery.v2.JobConfiguration/from-edn
                                            :to-edn   'gcp.bigquery.v2.JobConfiguration/to-edn}
                                           [:map [:type [:enum "COPY" "EXTRACT" "LOAD" "QUERY"]]]
                                           [:or
                                            :bigquery/CopyJobConfiguration
                                            :bigquery/ExtractJobConfiguration
                                            :bigquery/LoadJobConfiguration
                                            :bigquery/QueryJobConfiguration]]

   :bigquery/CopyJobConfiguration         [:map
                                           [:type [:= "COPY"]]
                                           [:destinationTable :bigquery/TableId]
                                           [:sourceTables {:min 1} [:sequential :bigquery/TableId]]
                                           [:createDisposition {:optional true} :bigquery.JobInfo/CreateDisposition]
                                           [:encryptionConfiguration {:optional true} :bigquery/EncryptionConfiguration]
                                           [:destinationExpiration {:optional true} :string]
                                           [:jobTimeoutMs {:optional true} :int]
                                           [:labels {:optional true} :gcp.synth/labels]
                                           [:operationType {:optional true} [:enum "COPY" "CLONE" "SNAPSHOT" "RESTORE"]]
                                           [:writeDisposition {:optional true} :bigquery.JobInfo/WriteDisposition]]

   :bigquery/ModelId                      :any

   :bigquery/ExtractJobConfiguration      [:map
                                           {:closed true
                                            :class  'com.google.cloud.bigquery.ExtractJobConfiguration}
                                           [:type [:= "EXTRACT"]]
                                           [:sourceTable {:optional true} :bigquery/TableId]
                                           [:sourceModel {:optional true} :bigquery/ModelId]
                                           [:destinationUris
                                            {:doc "Sets the list of fully-qualified Google Cloud Storage URIs (e.g. gs://bucket/path) where the extracted table should be written."
                                             :min 1}
                                            [:sequential string?]]
                                           [:compression {:optional true} :bigquery.synth/compression]
                                           [:fieldDelimiter {:optional true} string?]
                                           [:format {:optional true} :bigquery.synth/format]
                                           [:jobTimeoutMs {:optional true} :int]
                                           [:labels {:optional true} :gcp.synth/labels]
                                           [:printHeader {:optional true} boolean?]
                                           [:useAvroLogicalTypes {:optional true} boolean?]]

   :bigquery/LoadJobConfiguration         [:map {:closed true
                                                 :class  'com.google.cloud.bigquery.LoadJobConfiguration}
                                           [:type [:= "LOAD"]]
                                           ]

   :bigquery/QueryJobConfiguration        [:map
                                           {:closed true
                                            :class  'com.google.cloud.bigquery.QueryJobConfiguration}
                                           [:type [:= "QUERY"]]
                                           [:allowLargeResults {:optional true} boolean?]
                                           [:clustering {:optional true} :bigquery/Clustering]
                                           [:connectionProperties {:optional true} [:or [:map-of :string :string] [:sequential :bigquery/ConnectionProperty]]] ;; Sequential of connection properties
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
                                             :doc      "Specifies options relating to allowing the schema of the destination table to be updated as a side effect of the load or query job. Schema update options are supported in two cases: when writeDisposition is WRITE_APPEND; when writeDisposition is WRITE_TRUNCATE and the destination table is a partition of a table, specified by partition decorators. For normal tables, WRITE_TRUNCATE will always overwrite the schema."}
                                            [:sequential :bigquery.JobInfo/SchemaUpdateOption]]
                                           [:tableDefinitions {:optional true} [:map-of string? :bigquery/ExternalTableDefinition]]
                                           [:timePartitioning {:optional true} :bigquery/TimePartitioning]
                                           [:useLegacySql {:optional true} boolean?]
                                           [:useQueryCache {:optional true} boolean?]
                                           [:userDefinedFunctions {:optional true} [:sequential :bigquery/UserDefinedFunction]]
                                           [:writeDisposition {:optional true} :bigquery.JobInfo/WriteDisposition]]

   :bigquery.synth/JobCreate              [:map
                                           {:doc "create a job described in :jobInfo"}
                                           [:bigquery {:optional true} :bigquery.synth/clientable]
                                           [:jobInfo :bigquery/JobInfo]
                                           [:options {:optional true} [:maybe [:sequential :bigquery.BigQuery/JobOption]]]]

   :bigquery.synth/JobList                [:maybe
                                           [:map
                                            [:bigquery {:optional true} :bigquery.synth/clientable]
                                            [:options {:optional true} [:sequential :bigquery.BigQuery/JobListOption]]]]

   :bigquery.synth/Query                  [:map {:doc "execute a QueryJobConfiguration"}
                                           [:bigquery {:optional true} :bigquery.synth/clientable]
                                           [:configuration :bigquery/QueryJobConfiguration]
                                           [:options {:optional true} [:sequential :bigquery.BigQuery/JobOption]]
                                           [:jobId {:optional true} :bigquery/JobId]]

   :bigquery.synth/JobGet                 [:map
                                           [:bigquery {:optional true} :bigquery.synth/clientable]
                                           [:jobId :bigquery/JobId]
                                           [:options {:optional true} [:sequential :bigquery.BigQuery/JobOption]]]

   #!--------------------------------------------------------------------------

   :bigquery/Acl                          [:map
                                           [:role [:enum "OWNER" "READER" "WRITER"]]
                                           [:entity [:map [:type [:enum "DATASET" "DOMAIN" "GROUP" "IAM_MEMBER" "ROUTINE" "USER" "VIEW"]]]]]

   :bigquery/BigLakeConfiguration         :any

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
   :bigquery/UserDefinedFunction          [:map
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

   #!--------------------------------------------------------------------------
   #! :bigquery/StandardSQL

   :bigquery/StandardSQLDataType
   [:map {:closed true}
    ;; TODO just strings for primitives would be nice to accept here ie just "INT64" -> {:typeName "INT64"}
    [:typeKind {:urls     ["https://cloud.google.com/bigquery/docs/reference/standard-sql/data-types"
                           "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery.StandardSQLDataType#com_google_cloud_bigquery_StandardSQLDataType_getTypeKind__"]
                :optional true} :bigquery/StandardSQLTypeName]
    [:typeName {:optional true} :bigquery/StandardSQLTypeName]
    [:arrayElementType {:optional true} [:ref :bigquery/StandardSQLDataType]]
    [:structType {:optional true} :bigquery/StandardSQLStructType]]

   :bigquery/StandardSQLField
   [:map {:closed true}
    [:name {:optional true} :string]
    [:dataType [:ref :bigquery/StandardSQLDataType]]]

   :bigquery/StandardSQLStructType
   [:map {:closed true}
    [:fieldList [:sequential :bigquery/StandardSQLField]]]

   :bigquery/StandardSQLTableType
   [:map {:closed true}
    [:columns [:sequential :bigquery/StandardSQLField]]]

   :bigquery/StandardSQLTypeName          [:or
                                           {:url "https://cloud.google.com/bigquery/docs/reference/standard-sql/data-types"}
                                           [:= {:doc "Ordered list of zero or more elements of any non-array type."} "ARRAY"]
                                           [:= {:doc "A decimal value with 76+ digits of precision (the 77th digit is partial) and 38 digits of scale."} "BIGNUMERIC"]
                                           [:= {:doc "A Boolean value (true or false)."} "BOOLEAN"]
                                           [:= {:doc "Variable-length binary data."} "BYTES"]
                                           [:= {:doc "Represents a logical calendar date. Values range between the years 1 and 9999, inclusive."} "DATE"]
                                           [:= {:doc "Represents a year, month, day, hour, minute, second, and subsecond (microsecond precision)."} "DATETIME"]
                                           [:= {:doc "A 64-bit IEEE binary floating-point value."} "FLOAT"]
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

(global/include-schema-registry! registry)