(ns gcp.bigquery.v2
  (:require [gcp.global :as g]))

(def registry
  ^{::g/name ::registry}
  {
   :gcp.core/RetryOption                      :any
   :gcp.synth/labels                          [:map-of :string :string] ;TODO ..lowercase, char range etc
   :gcp.synth/resourceTags                    [:map-of :string :string]

   :gcp/bigquery.synth.location               [:string {:min 1}] ;TODO enum
   :gcp/bigquery.synth.project                [:string {:min 1}]
   :gcp/bigquery.synth.dataset                [:string {:min 1}]
   :gcp/bigquery.synth.table                  [:string {:min 1}]
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

   :gcp/bigquery.RemoteFunctionOptions        :any

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

   :gcp/bigquery.PolicyTags                   [:map {:closed true} [:names [:sequential :string]]]

   :gcp/bigquery.FieldElementType             [:map {:closed true} [:type :string]]

   :gcp/bigquery.Field                        [:map {:closed true}
                                               [:name :string]
                                               [:type :gcp/bigquery.LegacySQLTypeName]
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

   ;;--------------------------------------------------------------------------
   ;; Jobs
   ;;

   :gcp/bigquery.BigQuery.JobListOption       [:or
                                               [:map {:closed true} [:fields [:sequential :gcp/bigquery.BigQuery.JobField]]]
                                               [:map {:closed true} [:maxCreationTime :int]]
                                               [:map {:closed true} [:minCreationTime :int]]
                                               [:map {:closed true} [:pageSize :int]]
                                               [:map {:closed true} [:pageToken :string]]
                                               [:map {:closed true} [:parentJobId :string]]
                                               [:map {:closed true} [:stateFilters [:sequential :gcp/bigquery.JobStatus.State]]]]

   :gcp/bigquery.BigQuery.JobOption           [:or
                                               {:doc "union-map :gcp/bigquery.BigQueryRetryConfig|:fields|:retryOptions"}
                                               [:map [:BigQueryRetryConfig :gcp/bigquery.BigQueryRetryConfig]]
                                               [:map [:fields [:sequential :gcp/bigquery.BigQuery.JobField]]]
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

   :gcp/bigquery.HivePartitioningOptions      [:map {:closed true
                                                     :class  'com.google.cloud.bigquery.HivePartitioningOptions}
                                               [:fields {:optional false} [:sequential :string]]
                                               [:mode {:optional false} :string]
                                               [:requirePartitionFilter {:optional false} :boolean]
                                               [:sourceUriPrefix {:optional false} :string]]



   :gcp/bigquery.LoadJobConfiguration
   [:map
    {:key    :gcp/bigquery.LoadJobConfiguration,
     :closed true,
     :doc    "Google BigQuery load job configuration. A load job loads data from one of several formats into a
        table. Data is provided as URIs that point to objects in Google Cloud Storage. Load job
        configurations have JobConfiguration.Type#LOAD type.",
     :class  com.google.cloud.bigquery.LoadJobConfiguration}
    [:type [:= "LOAD"]]
    [:autodetect
     {:setterDoc "Sets automatic inference of the options and schema for CSV and JSON sources.",
      :getterDoc "Returns whether automatic inference of the options and schema for CSV and JSON sources is set.",
      :optional  true}
     :boolean]
    [:clustering
     {:setterDoc "Sets the clustering specification for the destination table.",
      :getterDoc "Returns the clustering specification for the definition table.",
      :optional  true}
     :gcp/bigquery.Clustering]
    [:columnNameCharacterMap
     {:setterDoc "[Optional] Character map supported for column names in CSV/Parquet loads. Defaults to STRICT
                and can be overridden by Project Config Service. Using this option with unsupporting load
                formats will result in an error.
               See Also: <a href=\"https://cloud.google.com/bigquery/docs/reference/rest/v2/Job#columnnamecharactermap\">  ColumnNameCharacterMap</a>",
      :getterDoc "Returns the column name character map used in CSV/Parquet loads.\nSee Also: ColumnNameCharacterMap",
      :optional  true}
     :string]
    [:connectionProperties {:setterDoc nil, :getterDoc "", :optional true} [:sequential :gcp/bigquery.ConnectionProperty]]
    [:createDisposition
     {:setterDoc "Sets whether the job is allowed to create new tables.",
      :getterDoc "Returns whether the job is allowed to create new tables.",
      :optional  true}
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
      :optional  true}
     [:sequential :string]]
    [:destinationTable
     {:setterDoc "Sets the destination table to load the data into.",
      :getterDoc "Returns the destination table to load the data into.",
      :optional  true}
     :gcp/bigquery.TableId]
    [:encryptionConfiguration {:setterDoc nil, :getterDoc "", :optional true} :gcp/bigquery.EncryptionConfiguration]
    [:fileSetSpecType
     {:setterDoc "Defines how to interpret files denoted by URIs. By default the files are assumed to be data
               files (this can be specified explicitly via FILE_SET_SPEC_TYPE_FILE_SYSTEM_MATCH). A second
               option is \"FILE_SET_SPEC_TYPE_NEW_LINE_DELIMITED_MANIFEST\" which interprets each file as a
               manifest file, where each line is a reference to a file.",
      :getterDoc "",
      :optional  true}
     :string]
    [:formatOptions
     {:setterDoc "Sets the source format, and possibly some parsing options, of the external data. Supported
                formats are CSV, NEWLINE_DELIMITED_JSON and DATASTORE_BACKUP. If not
                specified, CSV format is assumed.

               <p><a href=\"https://cloud.google.com/bigquery/docs/reference/v2/tables#externalDataConfiguration.sourceFormat\">
                Source Format</a></p>
               ",
      :getterDoc "Returns the format of the data files.",
      :optional  true}
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
      :optional  true}
     :boolean]
    [:jobTimeoutMs
     {:setterDoc "[Optional] Job timeout in milliseconds. If this time limit is exceeded, BigQuery may attempt
               to terminate the job.",
      :getterDoc "Returns the timeout associated with this job",
      :optional  true}
     :int]
    [:labels
     {:setterDoc "The labels associated with this job. You can use these to organize and group your jobs. Label
               keys and values can be no longer than 63 characters, can only contain lowercase letters,
               numeric characters, underscores and dashes. International characters are allowed. Label
               values are optional. Label keys must start with a letter and each label in the list must have
               a different key.",
      :getterDoc "Returns the labels associated with this job",
      :optional  true}
     [:map-of :string :string]]
    [:maxBadRecords
     {:setterDoc "Sets the maximum number of bad records that BigQuery can ignore when running the job. If the
               number of bad records exceeds this value, an invalid error is returned in the job result. By
               default no bad record is ignored.",
      :getterDoc "Returns the maximum number of bad records that BigQuery can ignore when running the job. If the
               number of bad records exceeds this value, an invalid error is returned in the job result. By
               default no bad record is ignored.",
      :optional  true}
     :int]
    [:nullMarker
     {:setterDoc "Sets the string that represents a null value in a CSV file. For example, if you specify \"N\",
               BigQuery interprets \"N\" as a null value when loading a CSV file. The default value is the
               empty string. If you set this property to a custom value, BigQuery throws an error if an
               empty string is present for all data types except for STRING and BYTE. For
               STRING and BYTE columns, BigQuery interprets the empty string as an empty
               value.",
      :getterDoc "Returns the string that represents a null value in a CSV file.",
      :optional  true}
     :string]
    [:rangePartitioning
     {:setterDoc "Range partitioning specification for this table. Only one of timePartitioning and
               rangePartitioning should be specified.",
      :getterDoc "Returns the range partitioning specification for the table",
      :optional  true}
     :gcp/bigquery.RangePartitioning]
    [:referenceFileSchemaUri
     {:setterDoc "When creating an external table, the user can provide a reference file with the table schema.
               This is enabled for the following formats: AVRO, PARQUET, ORC.",
      :optional  true}
     :string]
    [:schema
     {:setterDoc "Sets the schema for the destination table. The schema can be omitted if the destination table
               already exists, or if you're loading data from a Google Cloud Datastore backup (i.e.
               DATASTORE_BACKUP format option).",
      :getterDoc "Returns the schema for the destination table, if set. Returns null otherwise.",
      :optional  true}
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
      :optional  true}
     [:sequential :gcp/bigquery.JobInfo.SchemaUpdateOption]]

    ;; TODO if present cannot be empty
    ;; .. this effects newBuilder emitted
    [:sourceUris
     {:setterDoc "Sets the fully-qualified URIs that point to source data in Google Cloud Storage (e.g.
               gs://bucket/path). Each URI can contain one '*' wildcard character and it must come after the
               'bucket' name.",
      :getterDoc "Returns the fully-qualified URIs that point to source data in Google Cloud Storage (e.g.
               gs://bucket/path). Each URI can contain one '*' wildcard character and it must come after the
               'bucket' name.",
      :optional  true}
     [:seqable [:string {:min 1}]]]

    [:timePartitioning
     {:setterDoc "Sets the time partitioning specification for the destination table.",
      :getterDoc "Returns the time partitioning specification defined for the destination table.",
      :optional  true}
     :gcp/bigquery.TimePartitioning]
    [:useAvroLogicalTypes
     {:setterDoc "If FormatOptions is set to AVRO, you can interpret logical types into their corresponding
               types (such as TIMESTAMP) instead of only using their raw types (such as INTEGER). The value
               may be null.",
      :getterDoc "Returns True/False. Indicates whether the logical type is interpreted.",
      :optional  true}
     :boolean]
    [:writeDisposition
     {:setterDoc "Sets the action that should occur if the destination table already exists.",
      :getterDoc "Returns the action that should occur if the destination table already exists.",
      :optional  true}
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

   #!--------------------------------------------------------------------------
   #! :gcp/bigquery.StandardSQL

   :gcp/bigquery.StandardSQLDataType
   [:map {:closed true}
    [:typeKind {:urls     ["https://cloud.google.com/bigquery/docs/reference/standard-sql/data-types"
                           "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery.StandardSQLDataType#com_google_cloud_bigquery_StandardSQLDataType_getTypeKind__"]
                :TODO     "TODO just strings for primitives would be nice to accept here ie just \"INT64\" -> {:typeName \"INT64\"}"
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

   :gcp/bigquery.WriteChannelConfiguration
   [:map
    {:key    :gcp/bigquery.WriteChannelConfiguration,
     :closed true,
     :doc    "Google BigQuery Configuration for a load operation. A load configuration can be used to load data
        into a table with a com.google.cloud.WriteChannel (BigQuery#writer(WriteChannelConfiguration)).",
     :class  'com.google.cloud.bigquery.WriteChannelConfiguration}
    [:autodetect
     {:setterDoc "Sets automatic inference of the options and schema for CSV and JSON sources.",
      :getterDoc "Returns whether automatic inference of the options and schema for CSV and JSON sources is set.",
      :optional  true}
     :boolean]
    [:clustering
     {:setterDoc "Sets the clustering specification for the destination table.",
      :getterDoc "Returns the clustering specification for the definition table.",
      :optional  true}
     :gcp/bigquery.Clustering]
    [:connectionProperties
     {:setterDoc nil, :getterDoc "string", :optional true}
     [:sequential :gcp/bigquery.ConnectionProperty]]
    [:createDisposition
     {:setterDoc "Sets whether the job is allowed to create new tables.",
      :getterDoc "Returns whether the job is allowed to create new tables.",
      :optional  true}
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
      :optional  true}
     [:sequential :string]]
    [:destinationTable
     {:setterDoc "Sets the destination table to load the data into.",
      :getterDoc "Returns the destination table to load the data into.",
      :optional  true}
     :gcp/bigquery.TableId]
    [:encryptionConfiguration {:setterDoc nil, :getterDoc "string", :optional true} :gcp/bigquery.EncryptionConfiguration]
    [:formatOptions
     {:setterDoc "Sets the source format, and possibly some parsing options, of the external data. Supported
               formats are `CSV`, `NEWLINE_DELIMITED_JSON` and `DATASTORE_BACKUP`. If not
               specified, `CSV` format is assumed.

               [Source Format](https://cloud.google.com/bigquery/docs/reference/v2/tables#externalDataConfiguration.sourceFormat)",
      :getterDoc "Returns the format of the data files.",
      :optional  true}
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
      :optional  true}
     :boolean]
    [:labels {:setterDoc nil, :getterDoc "string", :optional true} [:map-of :string :string]]
    [:maxBadRecords
     {:setterDoc "Sets the maximum number of bad records that BigQuery can ignore when running the job. If the
               number of bad records exceeds this value, an invalid error is returned in the job result. By
               default no bad record is ignored.",
      :getterDoc "Returns the maximum number of bad records that BigQuery can ignore when running the job. If the
               number of bad records exceeds this value, an invalid error is returned in the job result. By
               default no bad record is ignored.",
      :optional  true}
     :int]
    [:nullMarker
     {:setterDoc "Sets the string that represents a null value in a CSV file. For example, if you specify \\\"N\\\",
               BigQuery interprets \\\"N\\\" as a null value when loading a CSV file. The default value is the
               empty string. If you set this property to a custom value, BigQuery throws an error if an
               empty string is present for all data types except for `STRING` and `BYTE`. For
               `STRING` and `BYTE` columns, BigQuery interprets the empty string as an empty
               value.",
      :getterDoc "Returns the string that represents a null value in a CSV file.",
      :optional  true}
     :string]
    [:schema
     {:setterDoc "Sets the schema for the destination table. The schema can be omitted if the destination table
               already exists, or if you're loading data from a Google Cloud Datastore backup (i.e. `
               DATASTORE_BACKUP` format option).",
      :getterDoc "Returns the schema for the destination table, if set. Returns null otherwise.",
      :optional  true}
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
      :optional  true}
     [:sequential :gcp/bigquery.JobInfo.SchemaUpdateOption]]
    [:timePartitioning
     {:setterDoc "Sets the time partitioning specification for the destination table.",
      :getterDoc "Returns the time partitioning specification defined for the destination table.",
      :optional  true}
     :gcp/bigquery.TimePartitioning]
    [:useAvroLogicalTypes
     {:setterDoc "If FormatOptions is set to AVRO, you can interpret logical types into their corresponding
               types (such as TIMESTAMP) instead of only using their raw types (such as INTEGER). The value
               may be `null`.",
      :getterDoc "Returns True/False. Indicates whether the logical type is interpreted.",
      :optional  true}
     :boolean]
    [:writeDisposition
     {:setterDoc "Sets the action that should occur if the destination table already exists.",
      :getterDoc "Returns the action that should occur if the destination table already exists.",
      :optional  true}
     :gcp/bigquery.JobInfo.WriteDisposition]]})

(def enum-registry
  {:gcp/bigquery.Acl.Entity.Type [:enum
                                  {:class "com.google.cloud.bigquery.Acl.Entity.Type",
                                   :gcp/key :gcp/bigquery.Acl.Entity.Type,
                                   :doc "Types of BigQuery entities."}
                                  "USER"
                                  "VIEW"
                                  "IAM_MEMBER"
                                  "ROUTINE"
                                  "DOMAIN"
                                  "DATASET"
                                  "GROUP"],
   :gcp/bigquery.Acl.Role [:or
                           {:class "com.google.cloud.bigquery.Acl.Role",
                            :gcp/key :gcp/bigquery.Acl.Role,
                            :doc "Dataset roles supported by BigQuery. See Also: Dataset Roles"}
                           [:= {:doc "Can read, query, copy or export tables in the dataset."} "READER"]
                           [:= {:doc "Same as WRITER plus can update and delete the dataset."} "OWNER"]
                           [:= {:doc "Same as READER plus can edit or append data in the dataset."} "WRITER"]],
   :gcp/bigquery.BigQuery.DatasetField [:enum
                                        {:class "com.google.cloud.bigquery.BigQuery.DatasetField",
                                         :gcp/key :gcp/bigquery.BigQuery.DatasetField,
                                         :doc "Fields of a BigQuery Dataset resource. See Also: Dataset Resource"}
                                        "ID"
                                        "LABELS"
                                        "FRIENDLY_NAME"
                                        "DESCRIPTION"
                                        "SELF_LINK"
                                        "ETAG"
                                        "DEFAULT_TABLE_EXPIRATION_MS"
                                        "DATASET_REFERENCE"
                                        "LAST_MODIFIED_TIME"
                                        "LOCATION"
                                        "CREATION_TIME"
                                        "ACCESS"],
   :gcp/bigquery.BigQuery.JobField [:enum
                                    {:class "com.google.cloud.bigquery.BigQuery.JobField",
                                     :gcp/key :gcp/bigquery.BigQuery.JobField,
                                     :doc "Fields of a BigQuery Job resource. See Also: Job Resource(https://cloud.google.com/bigquery/docs/reference/v2/jobs#resource)"}
                                    "USER_EMAIL"
                                    "ID"
                                    "STATISTICS"
                                    "SELF_LINK"
                                    "CONFIGURATION"
                                    "ETAG"
                                    "JOB_REFERENCE"
                                    "STATUS"],
   :gcp/bigquery.BigQuery.ModelField [:or
                                      {:class "com.google.cloud.bigquery.BigQuery.ModelField",
                                       :gcp/key :gcp/bigquery.BigQuery.ModelField,
                                       :doc "Fields of a BigQuery Model resource. See Also: Model Resource"}
                                      [:=
                                       {:doc "The labels associated with this model. You can use these to organize and group your models."}
                                       "LABELS"]
                                      [:= {:doc "A descriptive name for this model."} "FRIENDLY_NAME"]
                                      [:=
                                       {:doc "The time when this model expires, in milliseconds since the epoch."}
                                       "EXPIRATION_TIME"]
                                      [:= {:doc "An object that contains information about the model."} "MODEL_REFERENCE"]
                                      [:= {:doc "Information about the training runs for this model."} "TRAINING_RUNS"]
                                      [:= {:doc "Description of the model"} "DESCRIPTION"]
                                      [:= {:doc "Feature columns"} "FEATURE_COLUMNS"]
                                      [:= {:doc "A hash of this resource."} "ETAG"]
                                      [:= {:doc "type for the model"} "TYPE"]
                                      [:= {:doc "Last modified time of the model"} "LAST_MODIFIED_TIME"]
                                      [:= {:doc "The geographic location where the model resides."} "LOCATION"]
                                      [:= {:doc "Creation time of model"} "CREATION_TIME"]
                                      [:= {:doc "Label columns"} "LABEL_COLUMNS"]],
   :gcp/bigquery.BigQuery.RoutineField [:or
                                        {:class "com.google.cloud.bigquery.BigQuery.RoutineField",
                                         :gcp/key :gcp/bigquery.BigQuery.RoutineField,
                                         :doc "Fields of a BigQuery Routine resource. See Also: Routine Resource"}
                                        [:= {:doc "represents the return type of a routine"} "RETURN_TYPE"]
                                        [:= {:doc "represents the definition body of a routine"} "DEFINITION_BODY"]
                                        [:= {:doc "represents the type of a routine"} "ROUTINE_TYPE"]
                                        [:= {:doc "represents the etag of a routine"} "ETAG"]
                                        [:= {:doc "represents the language of a routine"} "LANGUAGE"]
                                        [:= {:doc "represents the reference of a routine"} "ROUTINE_REFERENCE"]
                                        [:= {:doc "represents the last modified time of a routine"} "LAST_MODIFIED_TIME"]
                                        [:= {:doc "represents the imported libraries of a routine"} "IMPORTED_LIBRARIES"]
                                        [:= {:doc "represents an argument of a routine"} "ARGUMENTS"]
                                        [:= {:doc "represents the creation time of a routine"} "CREATION_TIME"]],
   :gcp/bigquery.BigQuery.TableField [:or
                                      {:class "com.google.cloud.bigquery.BigQuery.TableField",
                                       :gcp/key :gcp/bigquery.BigQuery.TableField,
                                       :doc "Fields of a BigQuery Table resource. See Also: Table Resource"}
                                      [:= {:doc "An opaque ID uniquely identifying the table."} "ID"]
                                      [:=
                                       {:doc "The labels associated with this table. You can use key:value pairs to organize and group tables. Label keys and values can be no longer than 63 characters, can only contain lowercase letters, numeric characters, underscores and dashes. International characters are allowed. Label values are optional. Label keys must start with a letter and each label in the list must have a different key."}
                                       "LABELS"]
                                      [:= {:doc "A descriptive name for this table."} "FRIENDLY_NAME"]
                                      [:=
                                       {:doc "The number of bytes in the table that are considered \"long-term storage\"."}
                                       "NUM_LONG_TERM_BYTES"]
                                      [:=
                                       {:doc "The time when this table expires, in milliseconds since the epoch."}
                                       "EXPIRATION_TIME"]
                                      [:= {:doc "Describes the schema of this table."} "SCHEMA"]
                                      [:= {:doc "Specifies the definition of a logical view."} "VIEW"]
                                      [:=
                                       {:doc "Contains information on the table's streaming buffer, if any, if streaming inserts are in use."}
                                       "STREAMING_BUFFER"]
                                      [:= {:doc "A user-friendly description of this table."} "DESCRIPTION"]
                                      [:=
                                       {:doc "A URL that can be used to access this table using the REST API."}
                                       "SELF_LINK"]
                                      [:=
                                       {:doc "Describes the data format, location, and other properties of a table stored outside of BigQuery. By defining these properties, the data source can then be queried as if it were a standard BigQuery table."}
                                       "EXTERNAL_DATA_CONFIGURATION"]
                                      [:= {:doc "A hash of the table metadata."} "ETAG"]
                                      [:=
                                       {:doc "The number of rows in the table. This does not include data that is being buffered."}
                                       "NUM_ROWS"]
                                      [:= {:doc "Specifies time-based partitioning for this table."} "TIME_PARTITIONING"]
                                      [:= {:doc "Describes the table type."} "TYPE"]
                                      [:=
                                       {:doc "The time when this table was last modified, in milliseconds since the epoch."}
                                       "LAST_MODIFIED_TIME"]
                                      [:=
                                       {:doc "The geographic location where the table resides. This value is inherited from the dataset."}
                                       "LOCATION"]
                                      [:=
                                       {:doc "Describes the table. Cannot be used with external data configuration."}
                                       "TABLE_REFERENCE"]
                                      [:=
                                       {:doc "The time when this table was created, in milliseconds since the epoch."}
                                       "CREATION_TIME"]
                                      [:=
                                       {:doc "Specifies range-based partitioning for this table."}
                                       "RANGE_PARTITIONING"]
                                      [:=
                                       {:doc "The size of the table in bytes. This does not include data that is being buffered."}
                                       "NUM_BYTES"]],
   :gcp/bigquery.BigQuery.TableMetadataView [:enum
                                             {:class "com.google.cloud.bigquery.BigQuery.TableMetadataView",
                                              :gcp/key :gcp/bigquery.BigQuery.TableMetadataView,
                                              :doc "Metadata of a BigQuery Table. See Also: Table Resource"}
                                             "BASIC"
                                             "FULL"
                                             "TABLE_METADATA_VIEW_UNSPECIFIED"
                                             "STORAGE_STATS"],
   :gcp/bigquery.Field.Mode [:enum
                             {:class "com.google.cloud.bigquery.Field.Mode",
                              :gcp/key :gcp/bigquery.Field.Mode,
                              :doc "Mode for a BigQuery Table field. Mode#NULLABLE fields can be set to null, Mode#REQUIRED fields must be provided. Mode#REPEATED fields can contain more than one value."}
                             "REPEATED"
                             "REQUIRED"
                             "NULLABLE"],
   :gcp/bigquery.FieldValue.Attribute [:or
                                       {:class "com.google.cloud.bigquery.FieldValue.Attribute",
                                        :gcp/key :gcp/bigquery.FieldValue.Attribute,
                                        :doc "The field value's attribute, giving information on the field's content type."}
                                       [:=
                                        {:doc "A primitive field value. A `FieldValue` is primitive when the corresponding field has type LegacySQLTypeName#BYTES, LegacySQLTypeName#BOOLEAN, LegacySQLTypeName#STRING, LegacySQLTypeName#FLOAT, LegacySQLTypeName#INTEGER, LegacySQLTypeName#NUMERIC, LegacySQLTypeName#TIMESTAMP, LegacySQLTypeName#GEOGRAPHY or the value is set to `null`."}
                                        "PRIMITIVE"]
                                       [:= {:doc "A `FieldValue` for a field with Field.Mode#REPEATED mode."} "REPEATED"]
                                       [:= {:doc "A `FieldValue` for a field of type LegacySQLTypeName#RECORD."} "RECORD"]
                                       [:= {:doc "A `FieldValue` for a field of type LegacySQLTypeName#RANGE."} "RANGE"]],
   :gcp/bigquery.JobConfiguration.Type [:or
                                        {:class "com.google.cloud.bigquery.JobConfiguration.Type",
                                         :gcp/key :gcp/bigquery.JobConfiguration.Type,
                                         :doc "Type of a BigQuery Job."}
                                        [:=
                                         {:doc "An Extract Job exports a BigQuery table to Google Cloud Storage. Instances of `JobConfiguration` for this type are implemented by `ExtractJobConfiguration`."}
                                         "EXTRACT"]
                                        [:=
                                         {:doc "A Query Job runs a query against BigQuery data. Instances of `JobConfiguration` for this type are implemented by `QueryJobConfiguration`."}
                                         "QUERY"]
                                        [:=
                                         {:doc "A Copy Job copies an existing table to another new or existing table. Instances of `JobConfiguration` for this type are implemented by `CopyJobConfiguration`."}
                                         "COPY"]
                                        [:=
                                         {:doc "A Load Job loads data from one of several formats into a table. Instances of `JobConfiguration` for this type are implemented by `LoadJobConfiguration`."}
                                         "LOAD"]],
   :gcp/bigquery.JobInfo.CreateDisposition [:or
                                            {:class "com.google.cloud.bigquery.JobInfo.CreateDisposition",
                                             :gcp/key :gcp/bigquery.JobInfo.CreateDisposition,
                                             :doc "Specifies whether the job is allowed to create new tables."}
                                            [:=
                                             {:doc "Configures the job to create the table if it does not exist."}
                                             "CREATE_IF_NEEDED"]
                                            [:=
                                             {:doc "Configures the job to fail with a not-found error if the table does not exist."}
                                             "CREATE_NEVER"]],
   :gcp/bigquery.JobInfo.SchemaUpdateOption [:or
                                             {:class "com.google.cloud.bigquery.JobInfo.SchemaUpdateOption",
                                              :gcp/key :gcp/bigquery.JobInfo.SchemaUpdateOption,
                                              :doc "Specifies options relating to allowing the schema of the destination table to be updated as a side effect of the load or query job."}
                                             [:=
                                              {:doc "Allow relaxing a required field in the original schema to nullable."}
                                              "ALLOW_FIELD_RELAXATION"]
                                             [:=
                                              {:doc "Allow adding a nullable field to the schema."}
                                              "ALLOW_FIELD_ADDITION"]],
   :gcp/bigquery.JobInfo.WriteDisposition [:or
                                           {:class "com.google.cloud.bigquery.JobInfo.WriteDisposition",
                                            :gcp/key :gcp/bigquery.JobInfo.WriteDisposition,
                                            :doc "Specifies the action that occurs if the destination table already exists."}
                                           [:=
                                            {:doc "Configures the job to overwrite the table data if table already exists."}
                                            "WRITE_TRUNCATE"]
                                           [:=
                                            {:doc "Configures the job to fail with a duplicate error if the table already exists."}
                                            "WRITE_EMPTY"]
                                           [:=
                                            {:doc "Configures the job to append data to the table if it already exists."}
                                            "WRITE_APPEND"]],
   :gcp/bigquery.JobStatistics.QueryStatistics.StatementType [:enum
                                                              {:class "com.google.cloud.bigquery.JobStatistics.QueryStatistics.StatementType",
                                                               :gcp/key :gcp/bigquery.JobStatistics.QueryStatistics.StatementType,
                                                               :doc "StatementType represents possible types of SQL statements reported as part of the QueryStatistics of a BigQuery job."}
                                                              "ALTER_TABLE"
                                                              "CREATE_EXTERNAL_TABLE"
                                                              "DROP_SEARCH_INDEX"
                                                              "DROP_TABLE"
                                                              "MERGE"
                                                              "INSERT"
                                                              "CALL"
                                                              "ALTER_VIEW"
                                                              "CREATE_FUNCTION"
                                                              "CREATE_SNAPSHOT_TABLE"
                                                              "CREATE_TABLE_FUNCTION"
                                                              "CREATE_TABLE_AS_SELECT"
                                                              "CREATE_TABLE"
                                                              "SELECT"
                                                              "ALTER_SCHEMA"
                                                              "UPDATE"
                                                              "EXPORT_DATA"
                                                              "CREATE_SEARCH_INDEX"
                                                              "CREATE_PROCEDURE"
                                                              "ALTER_MATERIALIZED_VIEW"
                                                              "DELETE"
                                                              "EXPORT_MODEL"
                                                              "DROP_ROW_ACCESS_POLICY"
                                                              "DROP_PROCEDURE"
                                                              "DROP_EXTERNAL_TABLE"
                                                              "TRUNCATE_TABLE"
                                                              "DROP_MODEL"
                                                              "CREATE_MATERIALIZED_VIEW"
                                                              "DROP_FUNCTION"
                                                              "DROP_TABLE_FUNCTION"
                                                              "DROP_MATERIALIZED_VIEW"
                                                              "DROP_SNAPSHOT_TABLE"
                                                              "DROP_VIEW"
                                                              "DROP_SCHEMA"
                                                              "LOAD_DATA"
                                                              "SCRIPT"
                                                              "CREATE_SCHEMA"
                                                              "CREATE_VIEW"
                                                              "CREATE_MODEL"
                                                              "CREATE_ROW_ACCESS_POLICY"],
   :gcp/bigquery.JobStatus.State [:or
                                  {:class "com.google.cloud.bigquery.JobStatus.State",
                                   :gcp/key :gcp/bigquery.JobStatus.State,
                                   :doc "Possible states that a BigQuery Job can assume."}
                                  [:= {:doc "The BigQuery Job is being executed."} "RUNNING"]
                                  [:=
                                   {:doc "The BigQuery Job has completed either succeeding or failing. If failed #getError() will be non-null."}
                                   "DONE"]
                                  [:= {:doc "The BigQuery Job is waiting to be executed."} "PENDING"]],
   :gcp/bigquery.LegacySQLTypeName [:or
                                    {:class "com.google.cloud.bigquery.LegacySQLTypeName",
                                     :gcp/key :gcp/bigquery.LegacySQLTypeName,
                                     :doc "A type used in legacy SQL contexts. NOTE: some contexts use a mix of types; for example, for queries that use standard SQL, the return types are the legacy SQL types. See Also: https://cloud.google.com/bigquery/data-types"}
                                    [:=
                                     {:doc "Represents a time, independent of a specific date, to microsecond precision. Note, support for this type is limited in legacy SQL."}
                                     "TIME"]
                                    [:= {:doc "A Boolean value (true or false)."} "BOOLEAN"]
                                    [:= {:doc "Represents duration or amount of time."} "INTERVAL"]
                                    [:=
                                     {:doc "A decimal value with 76+ digits of precision (the 77th digit is partial) and 38 digits of scale"}
                                     "BIGNUMERIC"]
                                    [:=
                                     {:doc "Represents a logical calendar date. Note, support for this type is limited in legacy SQL."}
                                     "DATE"]
                                    [:= {:doc "Variable-length binary data."} "BYTES"]
                                    [:=
                                     {:doc "Represents a set of geographic points, represented as a Well Known Text (WKT) string."}
                                     "GEOGRAPHY"]
                                    [:=
                                     {:doc "A decimal value with 38 digits of precision and 9 digits of scale. Note, support for this type is limited in legacy SQL."}
                                     "NUMERIC"]
                                    [:= {:doc "Represents JSON data"} "JSON"]
                                    [:= {:doc "A 64-bit signed integer value."} "INTEGER"]
                                    [:= {:doc "A record type with a nested schema."} "RECORD"]
                                    [:= {:doc "A 64-bit IEEE binary floating-point value."} "FLOAT"]
                                    [:= {:doc "Variable-length character (Unicode) data."} "STRING"]
                                    [:=
                                     {:doc "Represents a year, month, day, hour, minute, second, and subsecond (microsecond precision). Note, support for this type is limited in legacy SQL."}
                                     "DATETIME"]
                                    [:=
                                     {:doc "Represents an absolute point in time, with microsecond precision."}
                                     "TIMESTAMP"]
                                    [:= {:doc "Represents a contiguous range of values."} "RANGE"]],
   :gcp/bigquery.QueryJobConfiguration.Priority [:or
                                                 {:class "com.google.cloud.bigquery.QueryJobConfiguration.Priority",
                                                  :gcp/key :gcp/bigquery.QueryJobConfiguration.Priority,
                                                  :doc "Priority levels for a query. If not specified the priority is assumed to be Priority#INTERACTIVE."}
                                                 [:=
                                                  {:doc "Query is queued and started as soon as idle resources are available, usually within a few minutes. If the query hasn't started within 3 hours, its priority is changed to Priority#INTERACTIVE."}
                                                  "BATCH"]
                                                 [:=
                                                  {:doc "Query is executed as soon as possible and count towards the concurrent rate limit and the daily rate limit."}
                                                  "INTERACTIVE"]],
   :gcp/bigquery.StandardSQLTypeName [:or
                                      {:class "com.google.cloud.bigquery.StandardSQLTypeName",
                                       :gcp/key :gcp/bigquery.StandardSQLTypeName,
                                       :doc "A type used in standard SQL contexts. For example, these types are used in queries with query parameters, which requires usage of standard SQL. See Also: https://cloud.google.com/bigquery/docs/reference/standard-sql/data-types"}
                                      [:=
                                       {:doc "Represents a time, independent of a specific date, to microsecond precision."}
                                       "TIME"]
                                      [:= {:doc "A 64-bit IEEE binary floating-point value."} "FLOAT64"]
                                      [:= {:doc "Represents duration or amount of time."} "INTERVAL"]
                                      [:=
                                       {:doc "A decimal value with 76+ digits of precision (the 77th digit is partial) and 38 digits of scale"}
                                       "BIGNUMERIC"]
                                      [:= {:doc "A Boolean value (true or false)."} "BOOL"]
                                      [:=
                                       {:doc "Represents a logical calendar date. Values range between the years 1 and 9999, inclusive."}
                                       "DATE"]
                                      [:= {:doc "Variable-length binary data."} "BYTES"]
                                      [:=
                                       {:doc "Represents a set of geographic points, represented as a Well Known Text (WKT) string."}
                                       "GEOGRAPHY"]
                                      [:=
                                       {:doc "A decimal value with 38 digits of precision and 9 digits of scale."}
                                       "NUMERIC"]
                                      [:= {:doc "Ordered list of zero or more elements of any non-array type."} "ARRAY"]
                                      [:= {:doc "Represents JSON data."} "JSON"]
                                      [:= {:doc "Variable-length character (Unicode) data."} "STRING"]
                                      [:=
                                       {:doc "Represents a year, month, day, hour, minute, second, and subsecond (microsecond precision)."}
                                       "DATETIME"]
                                      [:=
                                       {:doc "Container of ordered fields each with a type (required) and field name (optional)."}
                                       "STRUCT"]
                                      [:=
                                       {:doc "Represents an absolute point in time, with microsecond precision. Values range between the years 1 and 9999, inclusive."}
                                       "TIMESTAMP"]
                                      [:= {:doc "A 64-bit signed integer value."} "INT64"]
                                      [:= {:doc "Represents a contiguous range of values."} "RANGE"]],
   :gcp/bigquery.TableDefinition.Type [:or
                                       {:class "com.google.cloud.bigquery.TableDefinition.Type",
                                        :gcp/key :gcp/bigquery.TableDefinition.Type,
                                        :doc "The table type."}
                                       [:=
                                        {:doc "A virtual table defined by a SQL query. Instances of `TableDefinition` for this type are implemented by [ViewDefinition](/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery.ViewDefinition). See Also: [Views](https://cloud.google.com/bigquery/querying-data#views)"}
                                        "VIEW"]
                                       [:= {:doc "unknown"} "SNAPSHOT"]
                                       [:=
                                        {:doc "SQL query whose result is persisted. Instances of `MaterializedViewDefinition` for this type are implemented by [MaterializedViewDefinition](/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery.MaterializedViewDefinition). See Also: [Views](https://cloud.google.com/bigquery/querying-data#views)"}
                                        "MATERIALIZED_VIEW"]
                                       [:=
                                        {:doc "A BigQuery table backed by external data. Instances of `TableDefinition` for this type are implemented by [ExternalTableDefinition](/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery.ExternalTableDefinition). See Also: [Federated Data Sources](https://cloud.google.com/bigquery/federated-data-sources)"}
                                        "EXTERNAL"]
                                       [:=
                                        {:doc "A BigQuery table representing BigQuery ML Model. See Also: [ BigQuery ML Model](https://cloud.google.com/bigquery/docs/reference/standard-sql/bigqueryml-syntax-create#models_in_bqml_name)"}
                                        "MODEL"]
                                       [:=
                                        {:doc "A normal BigQuery table. Instances of `TableDefinition` for this type are implemented by [StandardTableDefinition](/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery.StandardTableDefinition)."}
                                        "TABLE"]],
   :gcp/bigquery.TableMetadataCacheUsage.UnusedReason [:or
                                                       {:class "com.google.cloud.bigquery.TableMetadataCacheUsage.UnusedReason",
                                                        :gcp/key :gcp/bigquery.TableMetadataCacheUsage.UnusedReason,
                                                        :doc "Reason for not using metadata caching for the table."}
                                                       [:=
                                                        {:doc "Metadata caching feature is not enabled. Update BigLake tables to enable the metadata caching."}
                                                        "METADATA_CACHING_NOT_ENABLED"]
                                                       [:=
                                                        {:doc "Unused reasons not specified."}
                                                        "UNUSED_REASON_UNSPECIFIED"]
                                                       [:=
                                                        {:doc "Metadata cache was outside the table's maxStaleness."}
                                                        "EXCEEDED_MAX_STALENESS"]
                                                       [:= {:doc "Other unknown reason."} "OTHER_REASON"]],
   :gcp/bigquery.TimePartitioning.Type [:or
                                        {:class "com.google.cloud.bigquery.TimePartitioning.Type",
                                         :gcp/key :gcp/bigquery.TimePartitioning.Type,
                                         :doc "[Optional] The supported types are DAY, HOUR, MONTH, and YEAR, which will generate one partition per day, hour, month, and year, respectively. When the interval is not specified, the default behavior is DAY."}
                                        [:= {:doc "Table is partitioned per month."} "MONTH"]
                                        [:= {:doc "Table is partitioned per year."} "YEAR"]
                                        [:= {:doc "Table is partitioned per hour."} "HOUR"]
                                        [:= {:doc "Table is partitioned per day."} "DAY"]],
   :gcp/bigquery.UserDefinedFunction.Type [:enum
                                           {:class "com.google.cloud.bigquery.UserDefinedFunction.Type",
                                            :gcp/key :gcp/bigquery.UserDefinedFunction.Type,
                                            :doc "Type of user-defined function. User defined functions can be provided inline as code blobs (#INLINE) or as a Google Cloud Storage URI (#FROM_URI)."}
                                           "FROM_URI"
                                           "INLINE"]})

(def accessor-registry
  {:gcp/bigquery.AvroOptions
   [:map
    {:gcp/key :gcp/bigquery.AvroOptions,
     :gcp/type :accessor,
     :closed true,
     :doc "Google BigQuery options for AVRO format. This class wraps some properties of AVRO files used by
        BigQuery to parse external data.",
     :class "com.google.cloud.bigquery.AvroOptions"}
    [:type {:optional true} [:= "AVRO"]]
    [:useAvroLogicalTypes
     {:optional true,
      :getterDoc "Returns whether BigQuery should interpret logical types as the corresponding BigQuery data type (for example, TIMESTAMP), instead of using the raw type (for example, INTEGER).",
      :setterDoc "[Optional] Sets whether BigQuery should interpret logical types as the corresponding BigQuery data type (for example, TIMESTAMP), instead of using the raw type (for example, INTEGER)."}
     :boolean]]

   :gcp/bigquery.BigtableOptions
   [:map
    {:gcp/key :gcp/bigquery.BigtableOptions,
     :gcp/type :accessor,
     :closed true,
     :doc "Class BigtableOptions extends FormatOptions",
     :class "com.google.cloud.bigquery.BigtableOptions"}
    [:type {:optional true} [:= "BIGTABLE"]]
    [:columnFamilies
     {:optional true,
      :getterDoc "Returns the list of column families.",
      :setterDoc "List of column families to expose in the table schema along with their types. This list restricts the column families that can be referenced in queries and specifies their value types. You can use this list to do type conversions - see the 'type' field for more details. If you leave this list empty, all column families are present in the table schema and their values are read as BYTES. During a query only the column families referenced in that query are read from Bigtable."}
     [:sequential :gcp/bigquery.BigtableColumnFamily]]
    [:ignoreUnspecifiedColumnFamilies
     {:optional true,
      :getterDoc "Returns whether অপরিচিত column families are ignored.",
      :setterDoc "If field is true, then the column families that are not specified in columnFamilies list are not exposed in the table schema. Otherwise, they are read with BYTES type values. The default value is false."}
     :boolean]
    [:readRowkeyAsString
     {:optional true,
      :getterDoc "Returns whether row key is read as a string.",
      :setterDoc "If readRowkeyAsString is true, then the rowkey column families will be read and converted to string. Otherwise they are read with BYTES type values and users need to manually cast them with CAST if necessary. The default value is false."}
     :boolean]]

   :gcp/bigquery.DatastoreBackupOptions
   [:map
    {:gcp/key :gcp/bigquery.DatastoreBackupOptions,
     :gcp/type :accessor,
     :closed true,
     :doc "Google BigQuery options for Cloud Datastore backup.",
     :class "com.google.cloud.bigquery.DatastoreBackupOptions"}
    [:type {:optional true} [:= "DATASTORE_BACKUP"]]
    [:projectionFields
     {:optional true,
      :getterDoc "Returns the value of which entity properties to load into BigQuery from a Cloud Datastore backup.",
      :setterDoc "Sets which entity properties to load into BigQuery from a Cloud Datastore backup. Property names are case sensitive and must be top-level properties. If no properties are specified, BigQuery loads all properties. If any named property isn't found in the Cloud Datastore backup, an invalid error is returned in the job result."}
     [:sequential :string]]]

   :gcp/bigquery.GoogleSheetsOptions
   [:map
    {:gcp/key :gcp/bigquery.GoogleSheetsOptions,
     :gcp/type :accessor,
     :closed true,
     :doc "Google BigQuery options for the Google Sheets format.",
     :class "com.google.cloud.bigquery.GoogleSheetsOptions"}
    [:type {:optional true} [:= "GOOGLE_SHEETS"]]
    [:range
     {:optional true,
      :getterDoc "Returns the number of range of a sheet when reading the data.",
      :setterDoc "[Optional] Range of a sheet to query from. Only used when non-empty. Typical format: sheet_name!top_left_cell_id:bottom_right_cell_id For example: sheet1!A1:B20"}
     :string]
    [:skipLeadingRows
     {:optional true,
      :getterDoc "Returns the number of rows at the top of a sheet that BigQuery will skip when reading the data.",
      :setterDoc "Sets the number of rows at the top of a sheet that BigQuery will skip when reading the data. The default value is 0. This property is useful if you have header rows that should be skipped."}
     :int]]

   :gcp/bigquery.ParquetOptions
   [:map
    {:gcp/key :gcp/bigquery.ParquetOptions,
     :gcp/type :accessor,
     :closed true,
     :doc "Options class for Parquet format.

        Parquet format is described at https://parquet.apache.org/documentation/latest/.",
     :class "com.google.cloud.bigquery.ParquetOptions"}
    [:type {:optional true} [:= "PARQUET"]]
    [:enableListInference
     {:optional true,
      :getterDoc "Whether to infer Parquet LIST logical type as BigQuery REPEATED field. When set, the inferred types for LIST elements are also determined.",
      :setterDoc "Sets whether to infer list type from leaf types (LIST) or element types (LIST -> ELEMENT)."}
     :boolean]
    [:enumAsString
     {:optional true,
      :getterDoc "Whether to infer Parquet ENUM logical type as BigQuery STRING type. When unset, ENUM is inferred as BYTES type.",
      :setterDoc "Sets whether to read Parquet ENUM type as string or raw bytes."}
     :boolean]
    [:mapTargetType
     {:optional true,
      :getterDoc "Returns how the Parquet map is represented.",
      :setterDoc "[Optional] Indicates how to represent a Parquet map if present. See Also: MapTargetType"}
     :string]]

   :gcp/bigquery.CsvOptions
   [:map
    {:gcp/key :gcp/bigquery.CsvOptions,
     :gcp/type :accessor,
     :closed true,
     :doc "Google BigQuery options for CSV format. This class wraps some properties of CSV files used by
        BigQuery to parse external data.",
     :class "com.google.cloud.bigquery.CsvOptions"}
    [:type {:optional true} [:= "CSV"]]
    [:allowJaggedRows
     {:optional true,
      :getterDoc "Returns whether BigQuery should accept rows that are missing trailing optional columns. If `true`, BigQuery treats missing trailing columns as null values. If `false`, records with missing trailing columns are treated as bad records, and if the number of bad records exceeds [ExternalTableDefinition#getMaxBadRecords()](/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery.ExternalTableDefinition#com_google_cloud_bigquery_ExternalTableDefinition_getMaxBadRecords__), an invalid error is returned in the job result.",
      :setterDoc "Set whether BigQuery should accept rows that are missing trailing optional columns. If `true`, BigQuery treats missing trailing columns as null values. If `false`, records with missing trailing columns are treated as bad records, and if there are too many bad records, an invalid error is returned in the job result. By default, rows with missing trailing columns are considered bad records."}
     :boolean]
    [:allowQuotedNewLines
     {:optional true,
      :getterDoc "Returns whether BigQuery should allow quoted data sections that contain newline characters in a CSV file.",
      :setterDoc "Sets whether BigQuery should allow quoted data sections that contain newline characters in a CSV file. By default quoted newline are not allowed."}
     :boolean]
    [:encoding
     {:optional true,
      :getterDoc "Returns the character encoding of the data. The supported values are UTF-8 or ISO-8859-1. If not set, UTF-8 is used. BigQuery decodes the data after the raw, binary data has been split using the values set in [CsvOptions#getQuote()](/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery.CsvOptions#com_google_cloud_bigquery_CsvOptions_getQuote__) and [CsvOptions#getFieldDelimiter()](/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery.CsvOptions#com_google_cloud_bigquery_CsvOptions_getFieldDelimiter__).",
      :setterDoc "Sets the character encoding of the data. The supported values are UTF-8 or ISO-8859-1. The default value is UTF-8. BigQuery decodes the data after the raw, binary data has been split using the values set in [com.google.cloud.bigquery.CsvOptions.Builder.setQuote] and [com.google.cloud.bigquery.CsvOptions.Builder.setFieldDelimiter]."}
     :string]
    [:fieldDelimiter
     {:optional true,
      :getterDoc "Returns the separator for fields in a CSV file.",
      :setterDoc "Sets the separator for fields in a CSV file. BigQuery converts the string to ISO-8859-1 encoding, and then uses the first byte of the encoded string to split the data in its raw, binary state. BigQuery also supports the escape sequence \" \" to specify a tab separator. The default value is a comma (',')."}
     :string]
    [:nullMarker
     {:optional true,
      :getterDoc "Returns the string that represents a null value in a CSV file.",
      :setterDoc "[Optional] Specifies a string that represents a null value in a CSV file. For example, if you specify \"\\N\", BigQuery interprets \"\\N\" as a null value when querying a CSV file. The default value is the empty string. If you set this property to a custom value, BigQuery throws an error if an empty string is present for all data types except for STRING and BYTE. For STRING and BYTE columns, BigQuery interprets the empty string as an empty value."}
     :string]
    [:preserveAsciiControlCharacters
     {:optional true,
      :getterDoc "Returns whether BigQuery should allow ascii control characters in a CSV file. By default ascii control characters are not allowed.",
      :setterDoc "Sets whether BigQuery should allow ASCII control characters in a CSV file. By default ASCII control characters are not allowed."}
     :boolean]
    [:quote
     {:optional true,
      :getterDoc "Returns the value that is used to quote data sections in a CSV file.",
      :setterDoc "Sets the value that is used to quote data sections in a CSV file. BigQuery converts the string to ISO-8859-1 encoding, and then uses the first byte of the encoded string to split the data in its raw, binary state. The default value is a double-quote ('\"'). If your data does not contain quoted sections, set the property value to an empty string. If your data contains quoted newline characters, you must also set [com.google.cloud.bigquery.CsvOptions.Builder.setAllowQuotedNewLines] property to `true`."}
     :string]
    [:skipLeadingRows
     {:optional true,
      :getterDoc "Returns the number of rows at the top of a CSV file that BigQuery will skip when reading the data.",
      :setterDoc "Sets the number of rows at the top of a CSV file that BigQuery will skip when reading the data. The default value is 0. This property is useful if you have header rows in the file that should be skipped."}
     :int]]


   :gcp/bigquery.BigtableColumnFamily
   [:map
    {:gcp/key :gcp/bigquery.BigtableColumnFamily,
     :gcp/type :accessor,
     :closed true,
     :doc "List of column families to expose in the table schema along with their types. This list restricts
        the column families that can be referenced in queries and specifies their value types.

        You can use this list to do type conversions - see the 'type' field for more details. If you
        leave this list empty, all column families are present in the table schema and their values are
        read as BYTES. During a query only the column families referenced in that query are read from
        Bigtable.",
     :class "com.google.cloud.bigquery.BigtableColumnFamily"}
    [:columns
     {:optional true,
      :setterDoc "Lists of columns that should be exposed as individual fields as opposed to a list of (column name, value) pairs. All columns whose qualifier matches a qualifier in this list can be accessed as .. Other columns can be accessed as a list through .Column field.",
      :getterDoc "string"}
     [:sequential :gcp/bigquery.BigtableColumn]]
    [:encoding
     {:optional true,
      :setterDoc "The encoding of the values when the type is not STRING.  Acceptable encoding values are: TEXT - indicates values are alphanumeric text strings. BINARY - indicates values are encoded using HBase Bytes.toBytes family of functions.  This can be overridden for a specific column by listing that column in 'columns' and specifying an encoding for it.",
      :getterDoc "string"}
     :string]
    [:familyID {:optional true, :setterDoc "Identifier of the column family.", :getterDoc "string"} :string]
    [:onlyReadLatest
     {:optional true,
      :setterDoc "If true, only the latest version of values are exposed for all columns in this column family. This can be overridden for a specific column by listing that column in 'columns' and specifying a different setting for that column.",
      :getterDoc "string"}
     :boolean]
    [:type
     {:optional true,
      :setterDoc "The type to convert the value in cells of this column family. The values are expected to be encoded using HBase Bytes.toBytes function when using the BINARY encoding value.  Following BigQuery types are allowed (case-sensitive): BYTES STRING INTEGER FLOAT BOOLEAN.  The default type is BYTES. This can be overridden for a specific column by listing that column in 'columns' and specifying a type for it.",
      :getterDoc "string"}
     :string]]

   :gcp/bigquery.BigtableColumn
   [:map
    {:gcp/key :gcp/bigquery.BigtableColumn,
     :gcp/type :accessor,
     :closed true,
     :doc "Class BigtableColumn.",
     :class "com.google.cloud.bigquery.BigtableColumn"}
    [:encoding
     {:optional true,
      :getterDoc "Returns the encoding of the values when a field is not explicitly set.",
      :setterDoc "The encoding of the values when the type is not STRING. Acceptable encoding values are: TEXT - indicates values are alphanumeric text strings. BINARY - indicates values are encoded using HBase Bytes.toBytes family of functions.nEncoding can also be set at the column family level. However, the setting at the column level takes precedence if 'encoding' is set at both levels."}
     :string]
    [:fieldName
     {:optional true,
      :getterDoc "Returns the field name of the column.",
      :setterDoc "If the qualifier is not a valid BigQuery field identifier, a valid identifier must be provided as the column field name and is used as field name in queries."}
     :string]
    [:onlyReadLatest
     {:optional true,
      :getterDoc "Returns whether to read the latest or all versions.",
      :setterDoc "If this is set, only the latest version of value in this column are exposed.n'onlyReadLatest' can also be set at the column family level. However, the setting at the column level takes precedence if 'onlyReadLatest' is set at both levels."}
     :boolean]
    [:qualifierEncoded
     {:optional true,
      :getterDoc "Returns the qualifier encoded of the column.",
      :setterDoc "Qualifier of the column.nColumns in the parent column family that has this exact qualifier are exposed as . field. If the qualifier is valid UTF-8 string, it can be specified in the qualifier_string field. Otherwise, a base-64 encoded value must be set to qualifier_encoded. The column field name is the same as the column qualifier. However, if the qualifier is not a valid BigQuery field identifier, a valid identifier must be provided as field_name."}
     :string]
    [:type
     {:optional true,
      :getterDoc "Returns the type of the column.",
      :setterDoc "The type to convert the value in cells of this column.nThe values are expected to be encoded using HBase Bytes.toBytes function when using the BINARY encoding value. Following BigQuery types are allowed (case-sensitive): BYTES STRING INTEGER FLOAT BOOLEAN Default type is BYTES.n'type' can also be set at the column family level. However, the setting at the column level takes precedence if 'type' is set at both levels."}
     :string]]

   :gcp/bigquery.ModelTableDefinition
   [:map
    {:gcp/key :gcp/bigquery.ModelTableDefinition,
     :gcp/type :accessor,
     :closed true,
     :doc "A Google BigQuery Model table definition. This definition is used to represent a BigQuery ML
        model.
        See Also: BigQuery  ML Model",
     :class "com.google.cloud.bigquery.ModelTableDefinition"}
    [:type {:optional true} [:= "MODEL"]]
    [:location {:optional true} :string]
    [:numBytes {:optional true} :int]]

   :gcp/bigquery.SnapshotTableDefinition
   [:map
    {:gcp/key :gcp/bigquery.SnapshotTableDefinition,
     :gcp/type :accessor,
     :closed true,
     :doc "Class SnapshotTableDefinition extends TableDefinition",
     :class "com.google.cloud.bigquery.SnapshotTableDefinition"}
    [:type {:optional true} [:= "SNAPSHOT"]]
    [:baseTableId
     {:optional true,
      :getterDoc "method getBaseTableId",
      :setterDoc "Reference describing the ID of the table that was snapshot. *"}
     :gcp/bigquery.TableId]
    [:clustering {:optional true} :gcp/bigquery.Clustering]
    [:dateTime
     {:optional true,
      :getterDoc "method getSnapshotTime",
      :setterDoc "The time at which the base table was snapshot. This value is reported in the JSON response using RFC3339 format. *"}
     :string]
    [:rangePartitioning {:optional true} :gcp/bigquery.RangePartitioning]
    [:timePartitioning {:optional true} :gcp/bigquery.TimePartitioning]]})

(def union-registry
  {:gcp/bigquery.FormatOptions
   [:or
    {:class "com.google.cloud.bigquery.FormatOptions",
     :gcp/type :concrete-union,
     :gcp/key :gcp/bigquery.FormatOptions,
     :doc "Base class for Google BigQuery format options. These class define the format of external data used by BigQuery, for either federated tables or load jobs. Load jobs support the following formats: AVRO, CSV, DATASTORE_BACKUP, GOOGLE_SHEETS, JSON, ORC, PARQUET Federated tables can be defined against following formats: AVRO, BIGTABLE, CSV, DATASTORE_BACKUP, GOOGLE_SHEETS, JSON"}
    :gcp/bigquery.AvroOptions
    :gcp/bigquery.BigtableOptions
    :gcp/bigquery.CsvOptions
    :gcp/bigquery.DatastoreBackupOptions
    :gcp/bigquery.GoogleSheetsOptions
    :gcp/bigquery.ParquetOptions
    [:map {:closed true} [:type [:= "ORC"]]]
    [:map {:closed true} [:type [:= "ICEBERG"]]]
    [:map {:closed true} [:type [:= "NEWLINE_DELIMITED_JSON"]]]]

   :gcp/bigquery.JobConfiguration
   [:or
    {:class "com.google.cloud.bigquery.JobConfiguration",
     :gcp/type :abstract-union,
     :gcp/key :gcp/bigquery.JobConfiguration,
     :doc "Base class for a BigQuery job configuration."}
    :gcp/bigquery.CopyJobConfiguration
    :gcp/bigquery.ExtractJobConfiguration
    :gcp/bigquery.LoadJobConfiguration
    :gcp/bigquery.QueryJobConfiguration]

   :gcp/bigquery.TableDefinition
   [:or
    {:class "com.google.cloud.bigquery.TableDefinition",
     :gcp/type :abstract-union,
     :gcp/key :gcp/bigquery.TableDefinition,
     :doc "Base class for a Google BigQuery table definition."}
    :gcp/bigquery.ExternalTableDefinition
    :gcp/bigquery.MaterializedViewDefinition
    :gcp/bigquery.ModelTableDefinition
    :gcp/bigquery.SnapshotTableDefinition
    :gcp/bigquery.StandardTableDefinition
    :gcp/bigquery.ViewDefinition]})

(def client-api-registry
  {:gcp/bigquery.synth.JobCreate
   [:map
    {:doc "create a job described in :jobInfo"}
    [:bigquery {:optional true} :gcp/bigquery.synth.clientable]
    [:jobInfo :gcp/bigquery.JobInfo]
    [:options {:optional true} [:maybe [:sequential :gcp/bigquery.BigQuery.JobOption]]]]

   :gcp/bigquery.synth.JobList
   [:maybe
    [:map
     [:bigquery {:optional true} :gcp/bigquery.synth.clientable]
     [:options {:optional true} [:sequential :gcp/bigquery.BigQuery.JobListOption]]]]

   :gcp/bigquery.synth.Query
   [:map {:doc "execute a QueryJobConfiguration"}
    [:bigquery {:optional true} :gcp/bigquery.synth.clientable]
    [:configuration :gcp/bigquery.QueryJobConfiguration]
    [:options {:optional true} [:sequential :gcp/bigquery.BigQuery.JobOption]]
    [:jobId {:optional true} :gcp/bigquery.JobId]]

   :gcp/bigquery.synth.JobGet
   [:map
    [:bigquery {:optional true} :gcp/bigquery.synth.clientable]
    [:jobId :gcp/bigquery.JobId]
    [:options {:optional true} [:sequential :gcp/bigquery.BigQuery.JobOption]]]

   :gcp/bigquery.synth.RoutineList
   [:map {:closed false}
    [:bigquery {:optional true} :gcp/bigquery.synth.clientable]
    [:datasetId :gcp/bigquery.DatasetId]
    [:options {:optional true} [:sequential :gcp/bigquery.BigQuery.RoutineListOption]]]

   :gcp/bigquery.synth.RoutineCreate
   [:map {:closed false}
    [:bigquery {:optional true} :gcp/bigquery.synth.clientable]
    [:routineInfo :gcp/bigquery.RoutineInfo]
    [:options {:optional true} [:maybe [:sequential :gcp/bigquery.BigQuery.RoutineOption]]]]

   :gcp/bigquery.synth.RoutineGet
   [:map {:closed false}
    [:bigquery {:optional true} :gcp/bigquery.synth.clientable]
    [:routineId :gcp/bigquery.RoutineId]
    [:options {:optional true} [:maybe [:sequential :gcp/bigquery.BigQuery.RoutineOption]]]]

   :gcp/bigquery.synth.RoutineDelete
   [:map {:closed false}
    [:bigquery {:optional true} :gcp/bigquery.synth.clientable]
    [:routineId :gcp/bigquery.RoutineId]]

   :gcp/bigquery.synth.RoutineUpdate
   [:map {:closed false}
    [:bigquery {:optional true} :gcp/bigquery.synth.clientable]
    [:routineInfo :gcp/bigquery.RoutineInfo]
    [:options {:optional true} [:maybe [:sequential :gcp/bigquery.BigQuery.RoutineOption]]]]

   :gcp/bigquery.synth.DatasetCreate [:map
                                      {:doc "create the dataset defined in :datasetInfo"}
                                      [:gcp/bigquery {:optional true} :gcp/bigquery.synth.clientable]
                                      [:datasetInfo :gcp/bigquery.DatasetInfo]
                                      [:options {:optional true} [:sequential :gcp/bigquery.BigQuery.DatasetOption]]]

   :gcp/bigquery.synth.DatasetGet    [:map
                                      [:gcp/bigquery {:optional true} :gcp/bigquery.synth.clientable]
                                      [:datasetId :gcp/bigquery.DatasetId]
                                      [:options {:optional true} [:sequential :gcp/bigquery.BigQuery.DatasetOption]]]

   :gcp/bigquery.synth.DatasetList   [:maybe
                                      [:map
                                       [:bigquery {:optional true} :gcp/bigquery.synth.clientable]
                                       [:projectId {:optional true} :gcp/bigquery.synth.project]
                                       [:options {:optional true} [:sequential :gcp/bigquery.BigQuery.DatasetListOption]]]]

   :gcp/bigquery.synth.DatasetUpdate [:map
                                      {:doc "update the dataset defined in :datasetInfo"}
                                      [:bigquery {:optional true} :gcp/bigquery.synth.clientable]
                                      [:datasetInfo :gcp/bigquery.DatasetInfo]
                                      [:options {:optional true} [:sequential :gcp/bigquery.BigQuery.DatasetOption]]]

   :gcp/bigquery.synth.DatasetDelete [:map
                                      {:doc "update the dataset defined in :datasetInfo"}
                                      [:bigquery {:optional true} :gcp/bigquery.synth.clientable]
                                      [:datasetId :gcp/bigquery.DatasetId]
                                      [:options {:optional true} [:sequential :gcp/bigquery.BigQuery.DatasetDeleteOption]]]

   :gcp/bigquery.synth.TableList     [:map
                                      [:bigquery {:optional true} :gcp/bigquery.synth.clientable]
                                      [:datasetId {:optional true} :gcp/bigquery.DatasetId]
                                      [:options {:optional true} [:sequential :gcp/bigquery.BigQuery.TableListOption]]]
   :gcp/bigquery.synth.TableGet      [:map
                                      [:bigquery {:optional true} :gcp/bigquery.synth.clientable]
                                      [:tableId :gcp/bigquery.TableId]
                                      [:options {:optional true} [:sequential :gcp/bigquery.BigQuery.TableOption]]]

   :gcp/bigquery.synth.TableCreate   [:map
                                      [:bigquery {:optional true} :gcp/bigquery.synth.clientable]
                                      [:tableInfo :gcp/bigquery.TableInfo]
                                      [:options {:optional true} [:sequential :gcp/bigquery.BigQuery.TableOption]]]

   :gcp/bigquery.synth.TableDelete   [:map {:closed true}
                                      [:bigquery {:optional true} :gcp/bigquery.synth.clientable]
                                      [:tableId :gcp/bigquery.TableId]]

   :gcp/bigquery.synth.TableUpdate   [:map
                                      [:bigquery {:optional true} :gcp/bigquery.synth.clientable]
                                      [:tableInfo :gcp/bigquery.TableInfo]
                                      [:options {:optional true} [:sequential :gcp/bigquery.BigQuery.TableOption]]]

   :gcp/bigquery.synth.WriterCreate [:map
                                     [:bigquery {:optional true} :gcp/bigquery.synth.clientable]
                                     [:writeChannelConfiguration {:optional false} :gcp/bigquery.WriteChannelConfiguration]]
   })

(let [registries [registry enum-registry union-registry accessor-registry client-api-registry]]
  (g/assert-disjoint-keys! registries)
  (g/include-schema-registry! (reduce merge registries)))