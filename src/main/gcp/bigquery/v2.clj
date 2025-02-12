(ns gcp.bigquery.v2
  (:require [gcp.global :as g]))

(def registry
  ^{::g/name ::registry}
  {
   :gcp.core/RetryOption                      :any
   :gcp.synth/labels                          [:map-of :string :string] ;TODO ..lowercase, char range etc
   :gcp.synth/resourceTags                    [:map-of :string :string]

   :gcp/bigquery.synth.location               :string
   :gcp/bigquery.synth.project                :string
   :gcp/bigquery.synth.dataset                :string
   :gcp/bigquery.synth.table                  :string
   :gcp/bigquery.synth.uri                    :string           ;; export supports single wildcard
   :gcp/bigquery.synth.compression            [:enum "GZIP" "DEFLATE" "SNAPPY"]
   :gcp/bigquery.synth.format                 [:enum
                                               {:doc "The default value for tables is CSV. Tables with nested or repeated fields cannot be exported as CSV. The default value for models is ML_TF_SAVED_MODEL."}
                                               "AVRO" "CSV" "PARQUET" "NEWLINE_DELIMITED_JSON"
                                               "ML_TF_SAVED_MODEL" "ML_XGBOOST_BOOSTER"]

   ;; TODO this should be in cloud core?
   :gcp/TransportOptions                      (g/instance-schema com.google.cloud.TransportOptions)

   ;;;-------------------------------
   ;;; Client

   :gcp/bigquery.BigQueryOptions              [:maybe
                                               [:map
                                                [:location {:optional true} :string]
                                                [:transportOptions {:optional true} :gcp/TransportOptions]
                                                [:useInt64Timestamps {:optional true} :boolean]]]

   :gcp/bigquery.synth.client                 (assoc-in (g/instance-schema com.google.cloud.bigquery.BigQuery)
                                                        [1 :from-edn] 'gcp.bigquery.v2.BigQueryOptions/get-service)

   :gcp/bigquery.synth.clientable             [:maybe
                                               [:or
                                                :gcp/bigquery.BigQueryOptions
                                                :gcp/bigquery.synth.client
                                                [:map [:bigquery [:or :gcp/bigquery.BigQueryOptions :gcp/bigquery.synth.client]]]]]

   ;;--------------------------------------------------------------------------
   ;; Routines

   :gcp/bigquery.synth.RoutineList
   [:map {:closed false}
    [:bigquery {:optional true} :gcp/bigquery.synth.clientable]
    [:datasetId :gcp/bigquery.DatasetId]
    [:options {:optional true}  [:sequential :gcp/bigquery.BigQuery.RoutineListOption]]]

   :gcp/bigquery.synth.RoutineCreate
   [:map {:closed false}
    [:gcp/bigquery. {:optional true} :gcp/bigquery.synth.clientable]
    [:routineInfo :gcp/bigquery.RoutineInfo]
    [:options {:optional true} [:maybe [:sequential :gcp/bigquery.BigQuery.RoutineOption]]]]

   :gcp/bigquery.synth.RoutineGet
   [:map {:closed false}
    [:gcp/bigquery. {:optional true} :gcp/bigquery.synth.clientable]
    [:routineId :gcp/bigquery.RoutineId]
    [:options {:optional true} [:maybe [:sequential :gcp/bigquery.BigQuery.RoutineOption]]]]

   :gcp/bigquery.synth.RoutineDelete
   [:map {:closed false}
    [:gcp/bigquery. {:optional true} :gcp/bigquery.synth.clientable]
    [:routineId :gcp/bigquery.RoutineId]]

   :gcp/bigquery.synth.RoutineUpdate
   [:map {:closed false}
    [:gcp/bigquery. {:optional true} :gcp/bigquery.synth.clientable]
    [:routineInfo :gcp/bigquery.RoutineInfo]
    [:options {:optional true} [:maybe [:sequential :gcp/bigquery.BigQuery.RoutineOption]]]]

   ;;;-------------------------------

   :gcp/bigquery.BigQuery.IAMOption           [:map {:closed true} [:version :string]]
   :gcp/bigquery.BigQuery.ModelListOption     :any
   :gcp/bigquery.BigQuery.ModelOption         :any
   :gcp/bigquery.BigQuery.QueryOption         :any
   :gcp/bigquery.BigQuery.QueryResultsOption  :any
   :gcp/bigquery.BigQuery.RoutineListOption   :any
   :gcp/bigquery.BigQuery.RoutineOption       :any
   :gcp/bigquery.BigQuery.TableDataListOption :any

   ;;;--------------------------------------------------------------------------

   :gcp/bigquery.RoutineId
   [:map
    [:project {:optional true} :string]
    [:dataset :string]
    [:routine :string]]

   :gcp/bigquery.RoutineArgument
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
    [:dataType :gcp/bigquery.StandardSQLDataType]]

   :gcp/bigquery.RemoteFunctionOptions :any

   :gcp/bigquery.RoutineInfo
   [:map {:closed true
          :urls   ["https://cloud.google.com/bigquery/docs/reference/rest/v2/routines"]
          :class  'com.google.cloud.bigquery.RoutineInfo}
    [:routineId :gcp/bigquery.RoutineId]
    [:arguments {:doc "Specifies the list of input/output arguments for the routine." :optional true} [:sequential :gcp/bigquery.RoutineArgument]]
    [:body {:doc      "The body of the routine. For functions, this is the expression in the AS clause. If language=SQL, it is the substring inside (but excluding) the parentheses. For example, for the function created with the following statement: CREATE FUNCTION JoinLines(x string, y string) as (concat(x, \" \", y))\n\nThe definitionBody is concat(x, \" \", y) ( is not replaced with linebreak).\n\nIf language=JAVASCRIPT, it is the evaluated string in the AS clause. For example, for the function created with the following statement:\n\nCREATE FUNCTION f() RETURNS STRING LANGUAGE js AS 'return \" \"; ' The definitionBody is return \" \"; Note that both are replaced with linebreaks"
            ;;TODO is required on write, not on reads?
            :optional true} :string]
    [:dataGovernanceType {:doc "Data governance type of the routine (e.g. DATA_MASKING)" :optional true} [:enum "DATA_GOVERNANCE_TYPE_UNSPECIFIED" "DATA_MASKING"]]
    [:determinismLevel {:doc "Determinism level for JavaScript UDFs" :optional true} [:enum "DETERMINISM_LEVEL_UNSPECIFIED" "DETERMINISTIC" "NOT_DETERMINISTIC"]]
    [:importedLibrariesList {:doc "language = \"JAVASCRIPT\", list of gs:// URLs for JavaScript libraries" :optional true} [:sequential :string]]
    [:language {:optional true} [:enum "JAVASCRIPT" "SQL"]]
    [:remoteFunctionOptions {:doc "Options for a remote function" :optional true} :gcp/bigquery.RemoteFunctionOptions]
    [:returnTableType {:doc      "Table type returned by the routine (StandardSQLTableType)"
                       :optional true}
     :gcp/bigquery.StandardSQLTableType]
    [:routineType {:doc      "Type of the routine (e.g. SCALAR_FUNCTION)"
                   :optional true}
     [:enum "ROUTINE_TYPE_UNSPECIFIED" "SCALAR_FUNCTION" "PROCEDURE" "TABLE_VALUED_FUNCTION"]]
    [:returnType {:doc      "Data type returned by the routine (StandardSQLDataType)"
                  :optional true}
     :gcp/bigquery.StandardSQLDataType]
    #!--read-only---
    [:description {:optional true :read-only? true} :string]
    [:etag {:doc "Hash of the routine resource" :optional true :read-only? true} :string]
    [:creationTime {:doc "Time (ms since epoch) the routine was created" :optional true :read-only? true} :int]
    [:lastModifiedTime {:doc "Time (ms since epoch) the routine was last modified" :optional true :read-only? true} :int]]

   ;;;--------------------------------------------------------------------------
   ;;; Datasets

   :gcp/bigquery.BigQuery.DatasetDeleteOption [:map {:closed true} [:deleteContents {:doc "truthy. non-empty datasets throw otherwise"} :any]]

   :gcp/bigquery.BigQuery.DatasetListOption   [:or
                                               {:doc "union-map :all|:labelFilter|:pageSize|:pageToken"}
                                               [:map {:closed true} [:all {:doc "returns hidden" :optional true} :any]]
                                               [:map {:closed true} [:labelFilter :string]]
                                               [:map {:closed true} [:pageSize :int]]
                                               [:map {:closed true} [:pageToken :string]]]

   :gcp/bigquery.BigQuery.DatasetOption       [:map {:closed true} [:fields [:sequential :gcp/bigquery.BigQuery.DatasetField]]]

   :gcp/bigquery.synth.DatasetCreate          [:map
                                               {:doc "create the dataset defined in :datasetInfo"}
                                               [:gcp/bigquery. {:optional true} :gcp/bigquery.synth.clientable]
                                               [:datasetInfo :gcp/bigquery.DatasetInfo]
                                               [:options {:optional true} [:sequential :gcp/bigquery.BigQuery.DatasetOption]]]
   :gcp/bigquery.synth.DatasetGet             [:map
                                               [:gcp/bigquery. {:optional true} :gcp/bigquery.synth.clientable]
                                               [:datasetId :gcp/bigquery.DatasetId]
                                               [:options {:optional true} [:sequential :gcp/bigquery.BigQuery.DatasetOption]]]
   :gcp/bigquery.synth.DatasetList            [:maybe
                                               [:map
                                                [:gcp/bigquery. {:optional true} :gcp/bigquery.synth.clientable]
                                                [:projectId {:optional true} :gcp/bigquery.synth.project]
                                                [:options {:optional true} [:sequential :gcp/bigquery.BigQuery.DatasetListOption]]]]
   :gcp/bigquery.synth.DatasetUpdate          [:map
                                               {:doc "update the dataset defined in :datasetInfo"}
                                               [:gcp/bigquery. {:optional true} :gcp/bigquery.synth.clientable]
                                               [:datasetInfo :gcp/bigquery.DatasetInfo]
                                               [:options {:optional true} [:sequential :gcp/bigquery.BigQuery.DatasetOption]]]

   :gcp/bigquery.synth.DatasetDelete          [:map
                                               {:doc "update the dataset defined in :datasetInfo"}
                                               [:gcp/bigquery. {:optional true} :gcp/bigquery.synth.clientable]
                                               [:datasetId :gcp/bigquery.DatasetId]
                                               [:options {:optional true} [:sequential :gcp/bigquery.BigQuery.DatasetDeleteOption]]]


   :gcp/bigquery.DatasetId                    [:map {:closed true}
                                               [:dataset {:optional true} :gcp/bigquery.synth.dataset]
                                               [:project {:optional true} :gcp/bigquery.synth.project]]

   :gcp/bigquery.DatasetInfo                  [:map
                                               [:acl {:optional true} [:seqable :gcp/bigquery.Acl]]
                                               [:datasetId :gcp/bigquery.DatasetId]
                                               [:defaultCollation {:optional true} :string]
                                               [:defaultEncryptionConfiguration {:optional true} :gcp/bigquery.EncryptionConfiguration]
                                               [:defaultPartitionExpirationMs {:optional true} :int]
                                               [:defaultTableLifetime {:optional true} :int]
                                               [:description {:optional true} :string]
                                               [:etag {:optional true} :string]
                                               [:externalDatasetReference {:optional true} :gcp/bigquery.ExternalDatasetReference]
                                               [:friendlyName {:optional true} :string]
                                               [:generatedId {:optional true} :string]
                                               [:labels {:optional true} :gcp.synth/labels]
                                               [:location {:optional true} :gcp/bigquery.synth.location]
                                               [:maxTimeTravelHours {:optional true} :int]
                                               [:storageBillingModel {:optional true} [:enum "PHYSICAL" "LOGICAL"]]]

   :gcp/bigquery.Dataset                      [:and
                                               :gcp/bigquery.DatasetInfo
                                               [:map
                                                [:bigquery :gcp/bigquery.synth.client]]]

   ;;--------------------------------------------------------------------------
   ;; Tables

   :gcp/bigquery.BigQuery.TableListOption     [:or
                                               [:map {:closed true} [:pageSize :int]]
                                               [:map {:closed true} [:pageToken :string]]]

   :gcp/bigquery.BigQuery.TableOption         [:or
                                               [:map {:closed true} [:autoDetect :boolean]]
                                               [:map {:closed true} [:fields :gcp/bigquery.BigQuery.TableField]]
                                               [:map {:closed true} [:tableMetadataView :gcp/bigquery.BigQuery.TableMetadataView]]]

   :gcp/bigquery.TableId                      [:map
                                               [:project {:optional true} :gcp/bigquery.synth.project]
                                               [:dataset :gcp/bigquery.synth.dataset]
                                               [:table :gcp/bigquery.synth.table]]

   :gcp/bigquery.synth.TableList              [:map
                                               [:bigquery {:optional true} :gcp/bigquery.synth.clientable]
                                               [:datasetId {:optional true} :gcp/bigquery.DatasetId]
                                               [:options {:optional true} [:sequential :gcp/bigquery.BigQuery.TableListOption]]]

   :gcp/bigquery.PolicyTags                   [:map {:closed true} [:names [:sequential :string]]]

   :gcp/bigquery.FieldElementType             [:map {:closed true} [:type :string]]

   :gcp/bigquery.Field                        [:map {:closed true}
                                               [:name :string]
                                               [:type :gcp/bigquery.StandardSQLTypeName]
                                               [:collation {:optional true} :string]
                                               [:defaultValueExpression {:optional true} :string]
                                               [:description {:optional true} :string]
                                               [:maxLength {:optional true} :int]
                                               [:mode {:optional true} [:enum "NULLABLE" "REQUIRED" "REPEATED"]]
                                               [:policyTags {:optional true} :gcp/bigquery.PolicyTags]
                                               [:precision {:optional true} :int]
                                               [:rangeElementType {:optional true} :gcp/bigquery.FieldElementType]
                                               [:scale {:optional true} :int]]

   :gcp/bigquery.Schema                       [:map {:closed true}
                                               [:fields [:sequential :gcp/bigquery.Field]]]

   :gcp/bigquery.ExternalTableDefinition      [:map {:closed true}
                                               [:type [:= "EXTERNAL"]]]

   :gcp/bigquery.MaterializedViewDefinition   [:map {:closed true}
                                               [:type [:= "MATERIALIZED_VIEW"]]
                                               [:clustering {:optional true} :gcp/bigquery.Clustering]
                                               [:enableRefresh {:optional true} :boolean]
                                               [:query :string]
                                               [:rangePartitioning {:optional true} :gcp/bigquery.RangePartitioning]
                                               [:refreshInterval {:optional true} :int]
                                               [:schema {:optional true} :gcp/bigquery.Schema]
                                               [:timePartitioning {:optional true} :gcp/bigquery.TimePartitioning]]

   :gcp/bigquery.ViewDefinition               [:map {:closed true}
                                               [:type [:= "VIEW"]]
                                               [:query :string]
                                               [:userDefinedFunctions [:seqable :gcp/bigquery.UserDefinedFunction]]]

   :gcp/bigquery.StandardTableDefinition      [:map {:closed true}
                                               [:type [:= "TABLE"]]
                                               [:bigLakeConfiguration {:optional true} :gcp/bigquery.BigLakeConfiguration]
                                               [:clustering {:optional true} :gcp/bigquery.Clustering]
                                               [:location {:optional true} :gcp/bigquery.synth.location]
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
                                               [:rangePartitioning {:optional true} :gcp/bigquery.RangePartitioning]
                                               [:schema {:optional true} :gcp/bigquery.Schema]
                                               [:streamingBuffer {:optional true} :any]
                                               [:tableConstraints {:optional true} :gcp/bigquery.TableConstraints]
                                               [:timePartitioning {:optional true} :gcp/bigquery.TimePartitioning]]

   :gcp/bigquery.TableDefinition              [:and
                                               [:map
                                                [:type [:enum "EXTERNAL" "MATERIALIZED_VIEW" "MODEL" "SNAPSHOT" "TABLE" "VIEW"]]
                                                [:schema {:optional true} :gcp/bigquery.Schema]]
                                               [:or
                                                :gcp/bigquery.ExternalTableDefinition
                                                :gcp/bigquery.MaterializedViewDefinition
                                                :gcp/bigquery.StandardTableDefinition
                                                :gcp/bigquery.ViewDefinition]]

   :gcp/bigquery.PrimaryKey                   [:map {:closed true}
                                               [:columns [:sequential {:min 1} :string]]]

   :gcp/bigquery.ColumnReference              [:map {:closed true}
                                               [:referencingColumn {:doc "The source column of this reference"} :string]
                                               [:referencedColumn {:doc "The target column of this reference"} :string]]

   :gcp/bigquery.ForeignKey                   [:map {:closed true}
                                               [:name {:doc "The name of the foreign key"} :string]
                                               [:referencedTable {:doc "The table referenced by this foreign key"} :gcp/bigquery.TableId]
                                               [:referencedColumns {:doc "The set of column references for this foreign key"} [:sequential :gcp/bigquery.ColumnReference]]]

   :gcp/bigquery.TableConstraints             [:map {:closed true}
                                               [:primaryKey {:optional true} :gcp/bigquery.PrimaryKey]
                                               [:foreignKeys {:optional true} [:sequential {:min 1} :gcp/bigquery.ForeignKey]]]

   :gcp/bigquery.CloneDefinition              [:map {:doc "read only"}
                                               [:baseTableId :gcp/bigquery.TableId]
                                               [:dateTime :string]]

   :gcp/bigquery.TableInfo                    [:map {:url "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery.TableInfo"}
                                               [:tableId :gcp/bigquery.TableId]
                                               [:etag {:optional true} :string]
                                               [:generatedId {:optional true} :string]
                                               [:definition {:optional true} :gcp/bigquery.TableDefinition]
                                               [:cloneDefinition {:optional true} :gcp/bigquery.CloneDefinition]
                                               [:defaultCollation {:optional true} :string]
                                               [:description {:optional true} :string]
                                               [:encryptionConfiguration {:optional true} :gcp/bigquery.EncryptionConfiguration]
                                               [:expirationTime {:optional true} :int]
                                               [:friendlyName {:optional true} :string]
                                               [:labels {:optional true} :gcp.synth/labels]
                                               [:requirePartitionFilter {:optional true} :boolean]
                                               [:resourceTags {:optional true} :gcp.synth/resourceTags]
                                               [:tableConstraints {:optional true} :gcp/bigquery.TableConstraints]
                                               [:creationTime {:optional true} :int]
                                               [:lastModifiedTime {:optional true} :int]
                                               [:numActiveLogicalBytes {:optional true} :int]
                                               [:numActivePhysicalBytes {:optional true} :int]
                                               [:numBytes {:optional true} :int]
                                               [:numLongTermBytes {:optional true} :int]
                                               [:numLongTermLogicalBytes {:optional true} :int]
                                               [:numLongTermPhysicalBytes {:optional true} :int]
                                               [:numRows {:optional true} #_(g/instance-schema java.math.BigInteger) :any]
                                               [:numTimeTravelPhysicalBytes {:optional true} :int]
                                               [:numTotalLogicalBytes {:optional true} :int]
                                               [:numTotalPhysicalBytes {:optional true} :int]]

   :gcp/bigquery.Table                        [:and :gcp/bigquery.TableInfo [:map [:bigquery :gcp/bigquery.synth.clientable]]]

   :gcp/bigquery.synth.TableGet               [:map
                                               [:gcp/bigquery. {:optional true} :gcp/bigquery.synth.clientable]
                                               [:tableId :gcp/bigquery.TableId]
                                               [:options {:optional true} [:sequential :gcp/bigquery.BigQuery.TableOption]]]

   :gcp/bigquery.synth.TableCreate            [:map
                                               [:gcp/bigquery. {:optional true} :gcp/bigquery.synth.clientable]
                                               [:tableInfo :gcp/bigquery.TableInfo]
                                               [:options {:optional true} [:sequential :gcp/bigquery.BigQuery.TableOption]]]

   :gcp/bigquery.synth.TableDelete            [:map {:closed true}
                                               [:gcp/bigquery. {:optional true} :gcp/bigquery.synth.clientable]
                                               [:tableId :gcp/bigquery.TableId]]

   :gcp/bigquery.synth.TableUpdate            [:map
                                               [:gcp/bigquery. {:optional true} :gcp/bigquery.synth.clientable]
                                               [:tableInfo :gcp/bigquery.TableInfo]
                                               [:options {:optional true} [:sequential :gcp/bigquery.BigQuery.TableOption]]]

   ;;--------------------------------------------------------------------------
   ;; Jobs

   :gcp/bigquery.BigQuery.JobListOption       [:or
                                               [:map {:closed true} [:fields [:sequential :gcp/bigquery.JobField]]]
                                               [:map {:closed true} [:maxCreationTime :int]]
                                               [:map {:closed true} [:minCreationTime :int]]
                                               [:map {:closed true} [:pageSize :int]]
                                               [:map {:closed true} [:pageToken :string]]
                                               [:map {:closed true} [:parentJobId :string]]
                                               [:map {:closed true} [:stateFilters [:sequential :gcp/bigquery.JobStatus.State]]]]

   :gcp/bigquery.BigQuery.JobOption           [:or
                                               {:doc "union-map :gcp/bigquery.etryConfig|:fields|:retryOptions"}
                                               [:map [:bigQueryRetryConfig :gcp/bigquery.BigQueryRetryConfig]]
                                               [:map [:fields [:sequential :gcp/bigquery.JobField]]]
                                               [:map [:options [:sequential :gcp.core/RetryOption]]]]

   :gcp/bigquery.JobId                        [:map {:closed true}
                                               [:job {:optional true} :string]
                                               [:location {:optional true} :gcp/bigquery.synth.location]
                                               [:project {:optional true} :gcp/bigquery.synth.project]]

   :gcp/bigquery.JobStatistics                [:map
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

   :gcp/bigquery.BigQueryError                [:map
                                               [:debugInfo {:optional true} :string]
                                               [:location {:optional true} :gcp/bigquery.synth.location]
                                               [:message {:optional true} :string]
                                               [:reason {:doc "https://cloud.google.com/bigquery/docs/error-messages"} :string]]

   :gcp/bigquery.JobStatus                    [:map {:closed true}
                                               [:error {:optional true} :gcp/bigquery.BigQueryError]
                                               [:executionErrors {:optional true} [:sequential :gcp/bigquery.BigQueryError]]
                                               [:state [:enum "DONE" "PENDING" "RUNNING"]]]

   :gcp/bigquery.JobInfo                      [:map
                                               [:jobId {:optional true} :gcp/bigquery.JobId]
                                               [:configuration :gcp/bigquery.JobConfiguration]
                                               [:statistics {:optional true} :gcp/bigquery.JobStatistics]
                                               [:etag {:optional true} :string]
                                               [:generatedId {:optional true} :string]
                                               [:status {:optional true} :gcp/bigquery.JobStatus]
                                               [:userEmail {:optional true} :string]]

   :gcp/bigquery.Job                          [:and
                                               :gcp/bigquery.JobInfo
                                               [:map [:bigquery :gcp/bigquery.synth.clientable]]]

   :gcp/bigquery.JobInfo.CreateDisposition    [:enum "CREATE_IF_NEEDED" "CREATE_NEVER"]

   :gcp/bigquery.JobInfo.WriteDisposition     [:enum "WRITE_APPEND" "WRITE_EMPTY" "WRITE_TRUNCATE"]

   :gcp/bigquery.JobInfo.SchemaUpdateOption   [:enum "ALLOW_FIELD_ADDITION" "ALLOW_FIELD_RELAXATION"]

   :gcp/bigquery.JobField                     [:enum "CONFIGURATION" "ETAG" "ID" "JOB_REFERENCE" "SELF_LINK" "STATISTICS" "STATUS" "USER_EMAIL"]

   :gcp/bigquery.JobConfiguration             [:and
                                               {:doc      "abstract class for Copy/Extract/Load/Query configs"
                                                :class    'com.google.cloud.bigquery.JobConfiguration
                                                :from-edn 'gcp.bigquery.v2.JobConfiguration/from-edn
                                                :to-edn   'gcp.bigquery.v2.JobConfiguration/to-edn}
                                               [:map [:type [:enum "COPY" "EXTRACT" "LOAD" "QUERY"]]]
                                               [:or
                                                :gcp/bigquery.CopyJobConfiguration
                                                :gcp/bigquery.ExtractJobConfiguration
                                                :gcp/bigquery.LoadJobConfiguration
                                                :gcp/bigquery.QueryJobConfiguration]]

   :gcp/bigquery.CopyJobConfiguration         [:map
                                               [:type [:= "COPY"]]
                                               [:destinationTable :gcp/bigquery.TableId]
                                               [:sourceTables {:min 1} [:sequential :gcp/bigquery.TableId]]
                                               [:createDisposition {:optional true} :gcp/bigquery.JobInfo.CreateDisposition]
                                               [:encryptionConfiguration {:optional true} :gcp/bigquery.EncryptionConfiguration]
                                               [:destinationExpiration {:optional true} :string]
                                               [:jobTimeoutMs {:optional true} :int]
                                               [:labels {:optional true} :gcp.synth/labels]
                                               [:operationType {:optional true} [:enum "COPY" "CLONE" "SNAPSHOT" "RESTORE"]]
                                               [:writeDisposition {:optional true} :gcp/bigquery.JobInfo.WriteDisposition]]

   :gcp/bigquery.ModelId                      :any

   :gcp/bigquery.ExtractJobConfiguration      [:map
                                               {:closed true
                                                :class  'com.google.cloud.bigquery.ExtractJobConfiguration}
                                               [:type [:= "EXTRACT"]]
                                               [:sourceTable {:optional true} :gcp/bigquery.TableId]
                                               [:sourceModel {:optional true} :gcp/bigquery.ModelId]
                                               [:destinationUris
                                                {:doc "Sets the list of fully-qualified Google Cloud Storage URIs (e.g. gs://bucket/path) where the extracted table should be written."
                                                 :min 1}
                                                [:sequential :string]]
                                               [:compression {:optional true} :gcp/bigquery.synth.compression]
                                               [:fieldDelimiter {:optional true} :string]
                                               [:format {:optional true} :gcp/bigquery.synth.format]
                                               [:jobTimeoutMs {:optional true} :int]
                                               [:labels {:optional true} :gcp.synth/labels]
                                               [:printHeader {:optional true} :boolean]
                                               [:useAvroLogicalTypes {:optional true} :boolean]]

   :gcp/bigquery.FormatOptions.Type [:enum "AVRO", "CSV", "DATASTORE_BACKUP", "GOOGLE_SHEETS", "JSON", "ORC", "PARQUET"]

   :gcp/bigquery.CsvOptions
   [:map {:closed true,
          :class 'com.google.cloud.bigquery.CsvOptions}
    [:type {:readOnly true} [:= "CSV"]]
    [:allowJaggedRows
     {:readDoc "Returns whether BigQuery should accept rows that are missing trailing optional columns. If
             <code translate=\"no\" dir=\"ltr\">true</code>, BigQuery treats missing trailing columns as null values. If <code translate=\"no\" dir=\"ltr\">false</code>,
             records with missing trailing columns are treated as bad records, and if the number of bad
             records exceeds <a class=\"xref\" href=\"/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery.ExternalTableDefinition#com_google_cloud_bigquery_ExternalTableDefinition_getMaxBadRecords__\">ExternalTableDefinition#getMaxBadRecords()</a>, an invalid error is
             returned in the job result.",
      :writeDoc "Set whether BigQuery should accept rows that are missing trailing optional columns. If `
              true`, BigQuery treats missing trailing columns as null values. If `false`, records
              with missing trailing columns are treated as bad records, and if there are too many bad
              records, an invalid error is returned in the job result. By default, rows with missing
              trailing columns are considered bad records.",
      :optional true}
     :boolean]
    [:allowQuotedNewLines
     {:readDoc "Returns whether BigQuery should allow quoted data sections that contain newline characters in a\nCSV file.",
      :writeDoc "Sets whether BigQuery should allow quoted data sections that contain newline characters in a
              CSV file. By default quoted newline are not allowed."}
     :boolean]
    [:encoding
     {:readDoc "Returns the character encoding of the data. The supported values are UTF-8 or ISO-8859-1. If
             not set, UTF-8 is used. BigQuery decodes the data after the raw, binary data has been split
             using the values set in <a class=\"xref\" href=\"/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery.CsvOptions#com_google_cloud_bigquery_CsvOptions_getQuote__\">#getQuote()</a> and <a class=\"xref\" href=\"/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery.CsvOptions#com_google_cloud_bigquery_CsvOptions_getFieldDelimiter__\">#getFieldDelimiter()</a>.",
      :writeDoc "Sets the character encoding of the data. The supported values are UTF-8 or ISO-8859-1. The
              default value is UTF-8. BigQuery decodes the data after the raw, binary data has been split
              using the values set in #setQuote(String) and #setFieldDelimiter(String)."}
     :string]
    [:fieldDelimiter
     {:readDoc "Returns the separator for fields in a CSV file.",
      :writeDoc "Sets the separator for fields in a CSV file. BigQuery converts the string to ISO-8859-1
              encoding, and then uses the first byte of the encoded string to split the data in its raw,
              binary state. BigQuery also supports the escape sequence \"\\t\" to specify a tab separator. The
              default value is a comma (',')."}
     :string]
    [:nullMarker
     {:readDoc "Returns the string that represents a null value in a CSV file.",
      :writeDoc "\\[Optional] Specifies a string that represents a null value in a CSV file. For example, if you
              specify \"\\N\", BigQuery interprets \"\\N\" as a null value when querying a CSV file. The
              default value is the empty string. If you set this property to a custom value, BigQuery
              throws an error if an empty string is present for all data types except for STRING and BYTE.
              For STRING and BYTE columns, BigQuery interprets the empty string as an empty value.",
      :optional true}
     :string]
    [:preserveAsciiControlCharacters
     {:readDoc "Returns whether BigQuery should allow ascii control characters in a CSV file. By default ascii
             control characters are not allowed.",
      :writeDoc "Sets whether BigQuery should allow ASCII control characters in a CSV file. By default ASCII
              control characters are not allowed."}
     :boolean]
    [:quote
     {:readDoc "Returns the value that is used to quote data sections in a CSV file.",
      :writeDoc "Sets the value that is used to quote data sections in a CSV file. BigQuery converts the
              string to ISO-8859-1 encoding, and then uses the first byte of the encoded string to split
              the data in its raw, binary state. The default value is a double-quote ('\"'). If your data
              does not contain quoted sections, set the property value to an empty string. If your data
              contains quoted newline characters, you must also set #setAllowQuotedNewLines(boolean) property to `true`."}
     :string]
    [:skipLeadingRows
     {:readDoc "Returns the number of rows at the top of a CSV file that BigQuery will skip when reading the\ndata.",
      :writeDoc "Sets the number of rows at the top of a CSV file that BigQuery will skip when reading the
              data. The default value is 0. This property is useful if you have header rows in the file
              that should be skipped."}
     :int]]

   :gcp/bigquery.DatastoreBackupOptions       [:map {:closed true
                                                     :class 'com.google.cloud.bigquery.DatastoreBackupOptions}
                                               [:projectionFields {:optional false} [:sequential :string]]]

   :gcp/bigquery.HivePartitioningOptions      [:map {:closed true
                                                     :class 'com.google.cloud.bigquery.HivePartitioningOptions}
                                               [:fields {:optional false} [:sequential :string]]
                                               [:mode {:optional false} :string]
                                               [:requirePartitionFilter {:optional false} :boolean]
                                               [:sourceUriPrefix {:optional false} :string]]

   :gcp/bigquery.ParquetOptions               [:map {:closed true
                                                     :class 'com.google.cloud.bigquery.ParquetOptions}
                                               [:enableListInference {:optional false} :boolean]
                                               [:enumAsString {:optional false} :boolean]
                                               [:mapTargetType {:optional false} :string]]

   :gcp/bigquery.LoadJobConfiguration
   [:map
    {:key :gcp/bigquery.LoadJobConfiguration,
     :closed true,
     :doc "Google BigQuery load job configuration. A load job loads data from one of several formats into a
        table. Data is provided as URIs that point to objects in Google Cloud Storage. Load job
        configurations have JobConfiguration.Type#LOAD type.",
     :class com.google.cloud.bigquery.LoadJobConfiguration}
    [:type [:= "LOAD"]]
    [:autodetect
     {:setterDoc "Sets automatic inference of the options and schema for CSV and JSON sources.",
      :getterDoc "Returns whether automatic inference of the options and schema for CSV and JSON sources is set.",
      :optional true}
     :boolean]
    [:clustering
     {:setterDoc "Sets the clustering specification for the destination table.",
      :getterDoc "Returns the clustering specification for the definition table.",
      :optional true}
     :gcp/bigquery.Clustering]
    [:columnNameCharacterMap
     {:setterDoc "[Optional] Character map supported for column names in CSV/Parquet loads. Defaults to STRICT
                and can be overridden by Project Config Service. Using this option with unsupporting load
                formats will result in an error.
               See Also: <a href=\"https://cloud.google.com/bigquery/docs/reference/rest/v2/Job#columnnamecharactermap\">  ColumnNameCharacterMap</a>",
      :getterDoc "Returns the column name character map used in CSV/Parquet loads.\nSee Also: ColumnNameCharacterMap",
      :optional true}
     :string]
    [:connectionProperties {:setterDoc nil, :getterDoc "", :optional true} [:sequential :gcp/bigquery.ConnectionProperty]]
    [:createDisposition
     {:setterDoc "Sets whether the job is allowed to create new tables.",
      :getterDoc "Returns whether the job is allowed to create new tables.",
      :optional true}
     :gcp/bigquery.JobInfo.CreateDisposition]
    [:createSession {:setterDoc nil, :getterDoc "", :optional true} :boolean]
    [:decimalTargetTypes
     {:setterDoc "Defines the list of possible SQL data types to which the source decimal values are converted.
               This list and the precision and the scale parameters of the decimal field determine the
               target type. In the order of NUMERIC, BIGNUMERIC, and STRING, a type is picked if it is in
               the specified list and if it supports the precision and the scale. STRING supports all
               precision and scale values.",
      :getterDoc "Returns the list of possible SQL data types to which the source decimal values are converted.
               This list and the precision and the scale parameters of the decimal field determine the target
               type. In the order of NUMERIC, BIGNUMERIC, and STRING, a type is picked if it is in the
               specified list and if it supports the precision and the scale. STRING supports all precision
               and scale values.",
      :optional true}
     [:sequential :string]]
    [:destinationTable
     {:setterDoc "Sets the destination table to load the data into.",
      :getterDoc "Returns the destination table to load the data into.",
      :optional true}
     :gcp/bigquery.TableId]
    [:encryptionConfiguration {:setterDoc nil, :getterDoc "", :optional true} :gcp/bigquery.EncryptionConfiguration]
    [:fileSetSpecType
     {:setterDoc "Defines how to interpret files denoted by URIs. By default the files are assumed to be data
               files (this can be specified explicitly via FILE_SET_SPEC_TYPE_FILE_SYSTEM_MATCH). A second
               option is \"FILE_SET_SPEC_TYPE_NEW_LINE_DELIMITED_MANIFEST\" which interprets each file as a
               manifest file, where each line is a reference to a file.",
      :getterDoc "",
      :optional true}
     :string]
    [:formatOptions
     {:setterDoc "Sets the source format, and possibly some parsing options, of the external data. Supported
                formats are CSV, NEWLINE_DELIMITED_JSON and DATASTORE_BACKUP. If not
                specified, CSV format is assumed.

               <p><a href=\"https://cloud.google.com/bigquery/docs/reference/v2/tables#externalDataConfiguration.sourceFormat\">
                Source Format</a></p>
               ",
      :getterDoc "Returns the format of the data files.",
      :optional true}
     :gcp/bigquery.FormatOptions]
    [:hivePartitioningOptions {:setterDoc nil, :getterDoc "", :optional true} :gcp/bigquery.HivePartitioningOptions]
    [:ignoreUnknownValues
     {:setterDoc "Sets whether BigQuery should allow extra values that are not represented in the table schema.
               If true, the extra values are ignored. If false, records with extra columns
               are treated as bad records, and if there are too many bad records, an invalid error is
               returned in the job result. By default unknown values are not allowed.",
      :getterDoc "Returns whether BigQuery should allow extra values that are not represented in the table
               schema. If true, the extra values are ignored. If true, records with extra
               columns are treated as bad records, and if there are too many bad records, an invalid error is
               returned in the job result. By default unknown values are not allowed.",
      :optional true}
     :boolean]
    [:jobTimeoutMs
     {:setterDoc "[Optional] Job timeout in milliseconds. If this time limit is exceeded, BigQuery may attempt
               to terminate the job.",
      :getterDoc "Returns the timeout associated with this job",
      :optional true}
     :int]
    [:labels
     {:setterDoc "The labels associated with this job. You can use these to organize and group your jobs. Label
               keys and values can be no longer than 63 characters, can only contain lowercase letters,
               numeric characters, underscores and dashes. International characters are allowed. Label
               values are optional. Label keys must start with a letter and each label in the list must have
               a different key.",
      :getterDoc "Returns the labels associated with this job",
      :optional true}
     [:map-of :string :string]]
    [:maxBadRecords
     {:setterDoc "Sets the maximum number of bad records that BigQuery can ignore when running the job. If the
               number of bad records exceeds this value, an invalid error is returned in the job result. By
               default no bad record is ignored.",
      :getterDoc "Returns the maximum number of bad records that BigQuery can ignore when running the job. If the
               number of bad records exceeds this value, an invalid error is returned in the job result. By
               default no bad record is ignored.",
      :optional true}
     :int]
    [:nullMarker
     {:setterDoc "Sets the string that represents a null value in a CSV file. For example, if you specify \"N\",
               BigQuery interprets \"N\" as a null value when loading a CSV file. The default value is the
               empty string. If you set this property to a custom value, BigQuery throws an error if an
               empty string is present for all data types except for STRING and BYTE. For
               STRING and BYTE columns, BigQuery interprets the empty string as an empty
               value.",
      :getterDoc "Returns the string that represents a null value in a CSV file.",
      :optional true}
     :string]
    [:rangePartitioning
     {:setterDoc "Range partitioning specification for this table. Only one of timePartitioning and
               rangePartitioning should be specified.",
      :getterDoc "Returns the range partitioning specification for the table",
      :optional true}
     :gcp/bigquery.RangePartitioning]
    [:referenceFileSchemaUri
     {:setterDoc "When creating an external table, the user can provide a reference file with the table schema.
               This is enabled for the following formats: AVRO, PARQUET, ORC.",
      :optional true}
     :string]
    [:schema
     {:setterDoc "Sets the schema for the destination table. The schema can be omitted if the destination table
               already exists, or if you're loading data from a Google Cloud Datastore backup (i.e.
               DATASTORE_BACKUP format option).",
      :getterDoc "Returns the schema for the destination table, if set. Returns null otherwise.",
      :optional true}
     :gcp/bigquery.Schema]
    [:schemaUpdateOptions
     {:setterDoc "Sets options allowing the schema of the destination table to be updated as a side effect of
               the load job. Schema update options are supported in two cases: when writeDisposition is
               WRITE_APPEND; when writeDisposition is WRITE_TRUNCATE and the destination table is a
               partition of a table, specified by partition decorators. For normal tables, WRITE_TRUNCATE
               will always overwrite the schema.",
      :getterDoc "Returns options allowing the schema of the destination table to be updated as a side effect of
               the load job. Schema update options are supported in two cases: when writeDisposition is
               WRITE_APPEND; when writeDisposition is WRITE_TRUNCATE and the destination table is a partition
               of a table, specified by partition decorators. For normal tables, WRITE_TRUNCATE will always
               overwrite the schema.",
      :optional true}
     [:sequential :gcp/bigquery.JobInfo.SchemaUpdateOption]]
    [:sourceUris
     {:setterDoc "Sets the fully-qualified URIs that point to source data in Google Cloud Storage (e.g.
               gs://bucket/path). Each URI can contain one '*' wildcard character and it must come after the
               'bucket' name.",
      :getterDoc "Returns the fully-qualified URIs that point to source data in Google Cloud Storage (e.g.
               gs://bucket/path). Each URI can contain one '*' wildcard character and it must come after the
               'bucket' name.",
      :optional true}
     [:sequential :string]]
    [:timePartitioning
     {:setterDoc "Sets the time partitioning specification for the destination table.",
      :getterDoc "Returns the time partitioning specification defined for the destination table.",
      :optional true}
     :gcp/bigquery.TimePartitioning]
    [:useAvroLogicalTypes
     {:setterDoc "If FormatOptions is set to AVRO, you can interpret logical types into their corresponding
               types (such as TIMESTAMP) instead of only using their raw types (such as INTEGER). The value
               may be null.",
      :getterDoc "Returns True/False. Indicates whether the logical type is interpreted.",
      :optional true}
     :boolean]
    [:writeDisposition
     {:setterDoc "Sets the action that should occur if the destination table already exists.",
      :getterDoc "Returns the action that should occur if the destination table already exists.",
      :optional true}
     :gcp/bigquery.JobInfo.WriteDisposition]]

   :gcp/bigquery.QueryJobConfiguration        [:map
                                               {:closed true
                                                :class  'com.google.cloud.bigquery.QueryJobConfiguration}
                                               [:type [:= "QUERY"]]
                                               [:allowLargeResults {:optional true} :boolean]
                                               [:clustering {:optional true} :gcp/bigquery.Clustering]
                                               [:connectionProperties {:optional true} [:or [:map-of :string :string] [:sequential :gcp/bigquery.ConnectionProperty]]] ;; Sequential of connection properties
                                               [:createDisposition {:optional true} :gcp/bigquery.JobInfo.CreateDisposition]
                                               [:createSession {:optional true} :boolean]
                                               [:defaultDataset {:optional true} :gcp/bigquery.DatasetId]
                                               [:destinationTable {:optional true} :gcp/bigquery.TableId]
                                               [:dryRun {:optional true} :boolean]
                                               [:encryptionConfiguration {:optional true} :gcp/bigquery.EncryptionConfiguration]
                                               [:flattenResults {:optional true} :boolean]
                                               [:jobTimeoutMs {:optional true} 'number?]
                                               [:labels {:optional true} :gcp.synth/labels]
                                               [:maxResults {:optional true} 'number?]
                                               [:maximumBillingTier {:optional true} 'number?]
                                               [:maximumBytesBilled {:optional true} 'number?]
                                               [:priority {:optional true} [:enum "BATCH" "INTERACTIVE"]]
                                               [:query :string]
                                               [:queryParameters {:optional true} [:seqable :gcp/bigquery.QueryParameterValue]]
                                               [:rangePartitioning {:optional true} :gcp/bigquery.RangePartitioning]
                                               [:schemaUpdateOptions
                                                {:optional true
                                                 :doc      "Specifies options relating to allowing the schema of the destination table to be updated as a side effect of the load or query job. Schema update options are supported in two cases: when writeDisposition is WRITE_APPEND; when writeDisposition is WRITE_TRUNCATE and the destination table is a partition of a table, specified by partition decorators. For normal tables, WRITE_TRUNCATE will always overwrite the schema."}
                                                [:sequential :gcp/bigquery.JobInfo.SchemaUpdateOption]]
                                               [:tableDefinitions {:optional true} [:map-of :string :gcp/bigquery.ExternalTableDefinition]]
                                               [:timePartitioning {:optional true} :gcp/bigquery.TimePartitioning]
                                               [:useLegacySql {:optional true} :boolean]
                                               [:useQueryCache {:optional true} :boolean]
                                               [:userDefinedFunctions {:optional true} [:sequential :gcp/bigquery.UserDefinedFunction]]
                                               [:writeDisposition {:optional true} :gcp/bigquery.JobInfo.WriteDisposition]]

   :gcp/bigquery.synth.JobCreate              [:map
                                               {:doc "create a job described in :jobInfo"}
                                               [:gcp/bigquery. {:optional true} :gcp/bigquery.synth.clientable]
                                               [:jobInfo :gcp/bigquery.JobInfo]
                                               [:options {:optional true} [:maybe [:sequential :gcp/bigquery.BigQuery.JobOption]]]]

   :gcp/bigquery.synth.JobList                [:maybe
                                               [:map
                                                [:gcp/bigquery. {:optional true} :gcp/bigquery.synth.clientable]
                                                [:options {:optional true} [:sequential :gcp/bigquery.BigQuery.JobListOption]]]]

   :gcp/bigquery.synth.Query                  [:map {:doc "execute a QueryJobConfiguration"}
                                               [:gcp/bigquery. {:optional true} :gcp/bigquery.synth.clientable]
                                               [:configuration :gcp/bigquery.QueryJobConfiguration]
                                               [:options {:optional true} [:sequential :gcp/bigquery.BigQuery.JobOption]]
                                               [:jobId {:optional true} :gcp/bigquery.JobId]]

   :gcp/bigquery.synth.JobGet                 [:map
                                               [:gcp/bigquery. {:optional true} :gcp/bigquery.synth.clientable]
                                               [:jobId :gcp/bigquery.JobId]
                                               [:options {:optional true} [:sequential :gcp/bigquery.BigQuery.JobOption]]]

   #!--------------------------------------------------------------------------

   :gcp/bigquery.Acl                          [:map
                                               [:role [:enum "OWNER" "READER" "WRITER"]]
                                               [:entity [:map [:type [:enum "DATASET" "DOMAIN" "GROUP" "IAM_MEMBER" "ROUTINE" "USER" "VIEW"]]]]]

   :gcp/bigquery.BigLakeConfiguration         :any

   :gcp/bigquery.BigQueryRetryConfig          [:and
                                               {:doc "part of JobOption"}
                                               [:map
                                                [:errorMessages {:optional true} [:sequential :string]]
                                                [:regExPatterns {:optional true} [:sequential :string]]]
                                               [:fn
                                                {:error/message "must be one of :errorMessages or :regExPatterns"}
                                                '(fn [m] (or (contains? m :errorMessages) (contains? m :regExPatterns)))]]


   :gcp/bigquery.Clustering                   [:map {:closed true} [:fields [:sequential :string]]]
   :gcp/bigquery.ConnectionProperty           [:or
                                               {:error/message "must be single-entry map or [string string] tuple"}
                                               [:and
                                                [:map-of :string :string]
                                                [:fn '(fn [v] (= 1 (count v)))]]
                                               [:tuple :string :string]]
   :gcp/bigquery.EncryptionConfiguration      [:map {:closed true} [:kmsKeyName :string]]
   :gcp/bigquery.ExternalDatasetReference     :any
   :gcp/bigquery.RangePartitioning            [:map {:closed true}
                                               [:field :string]
                                               [:range [:map {:closed true}
                                                        [:start :int]
                                                        [:end :int]
                                                        [:interval :int]]]]
   :gcp/bigquery.TimePartitioning             [:map {:closed true}
                                               [:expirationMs :int]
                                               [:field :string]
                                               [:requiredPartitionFilter :boolean]]
   :gcp/bigquery.UserDefinedFunction          [:map
                                               {:doc "https://cloud.google.com/bigquery/docs/user-defined-functions"}
                                               [:type [:enum "INLINE" "FROM_URI"]]
                                               [:functionDefinition :string]]

   :gcp/bigquery.QueryParameterValue          [:or
                                               {:class 'com.google.cloud.bigquery.QueryParameterValue}
                                               :string
                                               :boolean
                                               :int
                                               :float
                                               'decimal?
                                               'bytes?
                                               'inst?
                                               (g/instance-schema com.google.gson.JsonObject)
                                               (g/instance-schema java.time.LocalDate)
                                               (g/instance-schema java.time.LocalDateTime)
                                               [:map {:closed true}
                                                [:interval :string]]
                                               [:map {:closed true}
                                                [:geography :string]]
                                               [:map-of :string [:ref :gcp/bigquery.QueryParameterValue]]
                                               [:sequential [:ref :gcp/bigquery.QueryParameterValue]]]
   ;;-------------------------------
   ;; enums

   :gcp/bigquery.BigQuery.DatasetField        [:enum "ACCESS" "CREATION_TIME" "DATASET_REFERENCE"
                                               "DEFAULT_TABLE_EXPIRATION_MS" "DESCRIPTION" "ETAG"
                                               "FRIENDLY_NAME" "ID" "LABELS" "LAST_MODIFIED_TIME"
                                               "LOCATION" "SELF_LINK"]

   :gcp/bigquery.BigQuery.TableField          [:enum
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

   :gcp/bigquery.BigQuery.TableMetadataView   [:enum "BASIC" "FULL" "STORAGE_STATS" "TABLE_METADATA_VIEW_UNSPECIFIED"]

   :gcp/bigquery.JobStatus.State              [:enum "DONE" "PENDING" "RUNNING"]

   #!--------------------------------------------------------------------------
   #! :gcp/bigquery.StandardSQL

   :gcp/bigquery.StandardSQLDataType
   [:map {:closed true}
    [:typeKind {:urls     ["https://cloud.google.com/bigquery/docs/reference/standard-sql/data-types"
                           "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery.StandardSQLDataType#com_google_cloud_bigquery_StandardSQLDataType_getTypeKind__"]
                :TODO "TODO just strings for primitives would be nice to accept here ie just \"INT64\" -> {:typeName \"INT64\"}"
                :optional true} :gcp/bigquery.StandardSQLTypeName]
    [:typeName {:optional true} :gcp/bigquery.StandardSQLTypeName]
    [:arrayElementType {:optional true} [:ref :gcp/bigquery.StandardSQLDataType]]
    [:structType {:optional true} [:ref :gcp/bigquery.StandardSQLStructType]]]

   :gcp/bigquery.StandardSQLField
   [:map {:closed true}
    [:name {:optional true} :string]
    [:dataType :gcp/bigquery.StandardSQLDataType]]

   :gcp/bigquery.StandardSQLStructType
   [:map {:closed true}
    [:fieldList [:sequential :gcp/bigquery.StandardSQLField]]]

   :gcp/bigquery.StandardSQLTableType
   [:map {:closed true}
    [:columns [:sequential :gcp/bigquery.StandardSQLField]]]

   :gcp/bigquery.StandardSQLTypeName          [:or
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
                                               [:= {:doc "Represents an absolute point in time, with microsecond precision. Values range between the years 1 and 9999, inclusive."} "TIMESTAMP"]]

   :gcp/bigquery.FormatOptions
   [:map
    {:key :gcp/bigquery.FormatOptions,
     :closed true,
     :doc "Base class for Google BigQuery format options. These class define the format of external data
       used by BigQuery, for either federated tables or load jobs.

       Load jobs support the following formats: AVRO, CSV, DATASTORE_BACKUP, GOOGLE_SHEETS, JSON,
       ORC, PARQUET

       Federated tables can be defined against following formats: AVRO, BIGTABLE, CSV,
       DATASTORE_BACKUP, GOOGLE_SHEETS, JSON",
     :class 'com.google.cloud.bigquery.FormatOptions}
    [:format {:getterDoc "Returns the external data format, as a string."}
     [:enum "ICEBERG" "CSV" "PARQUET" "ORC" "GOOGLE_SHEETS" "NEWLINE_DELIMITED_JSON" "BIGTABLE" "AVRO"]]]

   :gcp/bigquery.WriteChannelConfiguration
   [:map
    {:key :gcp/bigquery.WriteChannelConfiguration,
     :closed true,
     :doc "Google BigQuery Configuration for a load operation. A load configuration can be used to load data
        into a table with a com.google.cloud.WriteChannel (BigQuery#writer(WriteChannelConfiguration)).",
     :class com.google.cloud.bigquery.WriteChannelConfiguration}
    [:autodetect
     {:setterDoc "Sets automatic inference of the options and schema for CSV and JSON sources.",
      :getterDoc "Returns whether automatic inference of the options and schema for CSV and JSON sources is set.",
      :optional true}
     :boolean]
    [:clustering
     {:setterDoc "Sets the clustering specification for the destination table.",
      :getterDoc "Returns the clustering specification for the definition table.",
      :optional true}
     :gcp/bigquery.Clustering]
    [:connectionProperties
     {:setterDoc nil, :getterDoc "string", :optional true}
     [:sequential :gcp/bigquery.ConnectionProperty]]
    [:createDisposition
     {:setterDoc "Sets whether the job is allowed to create new tables.",
      :getterDoc "Returns whether the job is allowed to create new tables.",
      :optional true}
     :gcp/bigquery.JobInfo.CreateDisposition]
    [:createSession {:setterDoc nil, :getterDoc "string", :optional true} :boolean]
    [:decimalTargetTypes
     {:setterDoc "Defines the list of possible SQL data types to which the source decimal values are converted.
               This list and the precision and the scale parameters of the decimal field determine the
               target type. In the order of NUMERIC, BIGNUMERIC, and STRING, a type is picked if it is in
               the specified list and if it supports the precision and the scale. STRING supports all
               precision and scale values.",
      :getterDoc "Returns the list of possible SQL data types to which the source decimal values are converted.
               This list and the precision and the scale parameters of the decimal field determine the target
               type. In the order of NUMERIC, BIGNUMERIC, and STRING, a type is picked if it is in the
               specified list and if it supports the precision and the scale. STRING supports all precision
               and scale values.",
      :optional true}
     [:sequential :string]]
    [:destinationTable
     {:setterDoc "Sets the destination table to load the data into.",
      :getterDoc "Returns the destination table to load the data into.",
      :optional true}
     :gcp/bigquery.TableId]
    [:encryptionConfiguration {:setterDoc nil, :getterDoc "string", :optional true} :gcp/bigquery.EncryptionConfiguration]
    [:formatOptions
     {:setterDoc "Sets the source format, and possibly some parsing options, of the external data. Supported
               formats are `CSV`, `NEWLINE_DELIMITED_JSON` and `DATASTORE_BACKUP`. If not
               specified, `CSV` format is assumed.

               [Source Format](https://cloud.google.com/bigquery/docs/reference/v2/tables#externalDataConfiguration.sourceFormat)",
      :getterDoc "Returns the format of the data files.",
      :optional true}
     :gcp/bigquery.FormatOptions]
    [:ignoreUnknownValues
     {:setterDoc "Sets whether BigQuery should allow extra values that are not represented in the table schema.
               If `true`, the extra values are ignored. If `false`, records with extra columns
               are treated as bad records, and if there are too many bad records, an invalid error is
               returned in the job result. By default unknown values are not allowed.",
      :getterDoc "Returns whether BigQuery should allow extra values that are not represented in the table
               schema. If true, the extra values are ignored. If true, records with extra
               columns are treated as bad records, and if there are too many bad records, an invalid error is
               returned in the job result. By default unknown values are not allowed.",
      :optional true}
     :boolean]
    [:labels {:setterDoc nil, :getterDoc "string", :optional true} [:map-of :string :string]]
    [:maxBadRecords
     {:setterDoc "Sets the maximum number of bad records that BigQuery can ignore when running the job. If the
               number of bad records exceeds this value, an invalid error is returned in the job result. By
               default no bad record is ignored.",
      :getterDoc "Returns the maximum number of bad records that BigQuery can ignore when running the job. If the
               number of bad records exceeds this value, an invalid error is returned in the job result. By
               default no bad record is ignored.",
      :optional true}
     :int]
    [:nullMarker
     {:setterDoc "Sets the string that represents a null value in a CSV file. For example, if you specify \\\"N\\\",
               BigQuery interprets \\\"N\\\" as a null value when loading a CSV file. The default value is the
               empty string. If you set this property to a custom value, BigQuery throws an error if an
               empty string is present for all data types except for `STRING` and `BYTE`. For
               `STRING` and `BYTE` columns, BigQuery interprets the empty string as an empty
               value.",
      :getterDoc "Returns the string that represents a null value in a CSV file.",
      :optional true}
     :string]
    [:schema
     {:setterDoc "Sets the schema for the destination table. The schema can be omitted if the destination table
               already exists, or if you're loading data from a Google Cloud Datastore backup (i.e. `
               DATASTORE_BACKUP` format option).",
      :getterDoc "Returns the schema for the destination table, if set. Returns null otherwise.",
      :optional true}
     :gcp/bigquery.Schema]
    [:schemaUpdateOptions
     {:setterDoc "Sets options allowing the schema of the destination table to be updated as a side effect of
               the load job. Schema update options are supported in two cases: when writeDisposition is
               WRITE_APPEND; when writeDisposition is WRITE_TRUNCATE and the destination table is a
               partition of a table, specified by partition decorators. For normal tables, WRITE_TRUNCATE
               will always overwrite the schema.",
      :getterDoc "Returns options allowing the schema of the destination table to be updated as a side effect of
               the load job. Schema update options are supported in two cases: when writeDisposition is
               WRITE_APPEND; when writeDisposition is WRITE_TRUNCATE and the destination table is a partition
               of a table, specified by partition decorators. For normal tables, WRITE_TRUNCATE will always
               overwrite the schema.",
      :optional true}
     [:sequential :gcp/bigquery.JobInfo.SchemaUpdateOption]]
    [:timePartitioning
     {:setterDoc "Sets the time partitioning specification for the destination table.",
      :getterDoc "Returns the time partitioning specification defined for the destination table.",
      :optional true}
     :gcp/bigquery.TimePartitioning]
    [:useAvroLogicalTypes
     {:setterDoc "If FormatOptions is set to AVRO, you can interpret logical types into their corresponding
               types (such as TIMESTAMP) instead of only using their raw types (such as INTEGER). The value
               may be `null`.",
      :getterDoc "Returns True/False. Indicates whether the logical type is interpreted.",
      :optional true}
     :boolean]
    [:writeDisposition
     {:setterDoc "Sets the action that should occur if the destination table already exists.",
      :getterDoc "Returns the action that should occur if the destination table already exists.",
      :optional true}
     :gcp/bigquery.JobInfo.WriteDisposition]]

   })

(g/include-schema-registry! registry)