;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.custom.QueryJobConfiguration
  {:doc "Google BigQuery Query Job configuration. A Query Job runs a query against BigQuery data. Query\njob configurations have {@link JobConfiguration.Type#QUERY} type.",
   :file-git-sha "63b8bdb5f21ab28ff5c2ca3dbd34922463b5ea6f",
   :fqcn "com.google.cloud.bigquery.QueryJobConfiguration"}
  (:require
    [gcp.bigquery.custom :as custom]
    [gcp.bigquery.Clustering :as Clustering]
    [gcp.bigquery.ConnectionProperty :as ConnectionProperty]
    [gcp.bigquery.DatasetId :as DatasetId]
    [gcp.bigquery.EncryptionConfiguration :as EncryptionConfiguration]
    [gcp.bigquery.ExternalTableDefinition :as ExternalTableDefinition]
    [gcp.bigquery.RangePartitioning :as RangePartitioning]
    [gcp.bigquery.TableId :as TableId]
    [gcp.bigquery.TimePartitioning :as TimePartitioning]
    [gcp.bigquery.UserDefinedFunction :as UserDefinedFunction]
    [gcp.global :as g])
  (:import [com.google.cloud.bigquery JobInfo$CreateDisposition
                                      JobInfo$SchemaUpdateOption JobInfo$WriteDisposition
                                      QueryJobConfiguration QueryJobConfiguration$JobCreationMode
                                      QueryJobConfiguration$Priority]))

(def QueryJobConfiguration$Priority-schema
  [:enum
   {:closed       true,
    :doc          "Priority levels for a query. If not specified the priority is assumed to be {@link\nPriority#INTERACTIVE}.",
    :gcp/category :nested/enum,
    :gcp/key      :gcp.bigquery/QueryJobConfiguration.Priority}
   "INTERACTIVE" "BATCH"])

(defn ^QueryJobConfiguration$Priority Priority-from-edn [arg] (QueryJobConfiguration$Priority/valueOf arg))
(defn ^String Priority-to-edn [^QueryJobConfiguration$Priority arg] (.name arg))

(def QueryJobConfiguration$JobCreationMode-schema
  [:enum
   {:closed       true,
    :doc          "Job Creation Mode provides different options on job creation.",
    :gcp/category :nested/enum,
    :gcp/key      :gcp.bigquery/QueryJobConfiguration.JobCreationMode}
   "JOB_CREATION_MODE_UNSPECIFIED" "JOB_CREATION_REQUIRED"
   "JOB_CREATION_OPTIONAL"])

(defn ^QueryJobConfiguration$JobCreationMode JobCreationMode-from-edn [arg] (QueryJobConfiguration$JobCreationMode/valueOf arg))
(defn ^String JobCreationMode-to-edn [^QueryJobConfiguration$JobCreationMode arg] (.name arg))

(defn ^QueryJobConfiguration from-edn
  [arg]
  (g/strict! :gcp.bigquery/QueryJobConfiguration arg)
  (let [builder (QueryJobConfiguration/newBuilder (get arg :query))]
    (when (some? (get arg :addPositionalParameter))
      (.addPositionalParameter builder
                               (custom/QueryParameterValue-from-edn
                                 (get arg :addPositionalParameter))))
    (when (some? (get arg :allowLargeResults))
      (.setAllowLargeResults builder (get arg :allowLargeResults)))
    (when (some? (get arg :clustering))
      (.setClustering builder (Clustering/from-edn (get arg :clustering))))
    (when (some? (get arg :connectionProperties))
      (.setConnectionProperties builder
                                (map ConnectionProperty/from-edn
                                     (get arg :connectionProperties))))
    (when (some? (get arg :createDisposition))
      (.setCreateDisposition builder
                             (JobInfo$CreateDisposition/valueOf
                               (get arg :createDisposition))))
    (when (some? (get arg :createSession))
      (.setCreateSession builder (get arg :createSession)))
    (when (some? (get arg :defaultDataset))
      (.setDefaultDataset builder
                          (DatasetId/from-edn (get arg :defaultDataset))))
    (when (some? (get arg :destinationEncryptionConfiguration))
      (.setDestinationEncryptionConfiguration
        builder
        (EncryptionConfiguration/from-edn
          (get arg :destinationEncryptionConfiguration))))
    (when (some? (get arg :destinationTable))
      (.setDestinationTable builder
                            (TableId/from-edn (get arg :destinationTable))))
    (when (some? (get arg :dryRun)) (.setDryRun builder (get arg :dryRun)))
    (when (some? (get arg :flattenResults))
      (.setFlattenResults builder (get arg :flattenResults)))
    (when (some? (get arg :jobCreationMode))
      (.setJobCreationMode builder
                           (QueryJobConfiguration$JobCreationMode/valueOf
                             (get arg :jobCreationMode))))
    (when (some? (get arg :jobTimeoutMs))
      (.setJobTimeoutMs builder (get arg :jobTimeoutMs)))
    (when (some? (get arg :labels))
      (.setLabels builder
                  (into {} (map (fn [[k v]] [(name k) v])) (get arg :labels))))
    (when (some? (get arg :maxResults))
      (.setMaxResults builder (get arg :maxResults)))
    (when (some? (get arg :maximumBillingTier))
      (.setMaximumBillingTier builder (int (get arg :maximumBillingTier))))
    (when (some? (get arg :maximumBytesBilled))
      (.setMaximumBytesBilled builder (get arg :maximumBytesBilled)))
    (when (some? (get arg :namedParameters))
      (.setNamedParameters
        builder
        (into {}
              (map (fn [[k v]] [(name k)
                                (custom/QueryParameterValue-from-edn v)]))
              (get arg :namedParameters))))
    (when (some? (get arg :parameterMode))
      (.setParameterMode builder (get arg :parameterMode)))
    (when (some? (get arg :positionalParameters))
      (.setPositionalParameters builder
                                (map custom/QueryParameterValue-from-edn
                                  (get arg :positionalParameters))))
    (when (some? (get arg :priority))
      (.setPriority builder (QueryJobConfiguration$Priority/valueOf (get arg :priority))))
    (when (some? (get arg :rangePartitioning))
      (.setRangePartitioning builder
                             (RangePartitioning/from-edn
                               (get arg :rangePartitioning))))
    (when (some? (get arg :reservation))
      (.setReservation builder (get arg :reservation)))
    (when (some? (get arg :schemaUpdateOptions))
      (.setSchemaUpdateOptions builder (map #(JobInfo$SchemaUpdateOption/valueOf %) (get arg :schemaUpdateOptions))))
    (when (some? (get arg :tableDefinitions))
      (.setTableDefinitions
        builder
        (into {}
              (map (fn [[k v]] [(name k) (ExternalTableDefinition/from-edn v)]))
              (get arg :tableDefinitions))))
    (when (some? (get arg :timePartitioning))
      (.setTimePartitioning builder
                            (TimePartitioning/from-edn
                              (get arg :timePartitioning))))
    (when (some? (get arg :useLegacySql))
      (.setUseLegacySql builder (get arg :useLegacySql)))
    (when (some? (get arg :useQueryCache))
      (.setUseQueryCache builder (get arg :useQueryCache)))
    (when (some? (get arg :userDefinedFunctions))
      (.setUserDefinedFunctions builder
                                (map UserDefinedFunction/from-edn
                                  (get arg :userDefinedFunctions))))
    (when (some? (get arg :writeDisposition))
      (.setWriteDisposition builder
                            (JobInfo$WriteDisposition/valueOf
                              (get arg :writeDisposition))))
    (.build builder)))

(defn to-edn
  [^QueryJobConfiguration arg]
  {:post [(g/strict! :gcp.bigquery/QueryJobConfiguration %)]}
  (cond-> {:query (.getQuery arg), :type "QUERY"}
          (.allowLargeResults arg) (assoc :allowLargeResults (.allowLargeResults arg))
          (.getClustering arg) (assoc :clustering
                                      (Clustering/to-edn (.getClustering arg)))
          (.getConnectionProperties arg) (assoc :connectionProperties
                                                (map ConnectionProperty/to-edn
                                                     (.getConnectionProperties arg)))
          (.getCreateDisposition arg) (assoc :createDisposition
                                             (.name (.getCreateDisposition arg)))
          (.createSession arg) (assoc :createSession (.createSession arg))
          (.getDefaultDataset arg) (assoc :defaultDataset
                                          (DatasetId/to-edn (.getDefaultDataset arg)))
          (.getDestinationEncryptionConfiguration arg)
          (assoc :destinationEncryptionConfiguration
                 (EncryptionConfiguration/to-edn (.getDestinationEncryptionConfiguration
                                                   arg)))
          (.getDestinationTable arg) (assoc :destinationTable
                                            (TableId/to-edn (.getDestinationTable arg)))
          (.dryRun arg) (assoc :dryRun (.dryRun arg))
          (.flattenResults arg) (assoc :flattenResults (.flattenResults arg))
          (.getJobCreationMode arg) (assoc :jobCreationMode
                                           (.name (.getJobCreationMode arg)))
          (.getJobTimeoutMs arg) (assoc :jobTimeoutMs (.getJobTimeoutMs arg))
          (.getLabels arg)
          (assoc :labels
                 (into {} (map (fn [[k v]] [(keyword k) v])) (.getLabels arg)))
          (.getMaxResults arg) (assoc :maxResults (.getMaxResults arg))
          (.getMaximumBillingTier arg) (assoc :maximumBillingTier
                                              (.getMaximumBillingTier arg))
          (.getMaximumBytesBilled arg) (assoc :maximumBytesBilled
                                              (.getMaximumBytesBilled arg))
          (seq (.getNamedParameters arg))
          (assoc :namedParameters
                 (into {}
                       (map (fn [[k v]] [(keyword k) (custom/QueryParameterValue-to-edn v)]))
                       (.getNamedParameters arg)))
          (seq (.getPositionalParameters arg)) (assoc :positionalParameters
                                                      (map custom/QueryParameterValue-to-edn
                                                           (.getPositionalParameters arg)))
          (.getPriority arg) (assoc :priority (.name (.getPriority arg)))
          (.getRangePartitioning arg) (assoc :rangePartitioning
                                             (RangePartitioning/to-edn
                                               (.getRangePartitioning arg)))
          (.getReservation arg) (assoc :reservation (.getReservation arg))
          (.getSchemaUpdateOptions arg) (assoc :schemaUpdateOptions
                                               (map (fn [e] (.name e))
                                                    (.getSchemaUpdateOptions arg)))
          (.getTableDefinitions arg)
          (assoc :tableDefinitions
                 (into {}
                       (map (fn [[k v]] [(keyword k)
                                         (ExternalTableDefinition/to-edn v)]))
                       (.getTableDefinitions arg)))
          (.getTimePartitioning arg) (assoc :timePartitioning
                                            (TimePartitioning/to-edn (.getTimePartitioning
                                                                       arg)))
          (.useLegacySql arg) (assoc :useLegacySql (.useLegacySql arg))
          (.useQueryCache arg) (assoc :useQueryCache (.useQueryCache arg))
          (.getUserDefinedFunctions arg) (assoc :userDefinedFunctions
                                                (map UserDefinedFunction/to-edn
                                                     (.getUserDefinedFunctions arg)))
          (.getWriteDisposition arg) (assoc :writeDisposition
                                            (.name (.getWriteDisposition arg)))))

(def map-schema
  [:map {:closed true}
   [:type [:= "QUERY"]]
   [:allowLargeResults
    {:optional true,
     :getter-doc
     "Returns whether the job is enabled to create arbitrarily large results. If {@code true} the\nquery is allowed to create large results at a slight cost in performance. the query is allowed\nto create large results at a slight cost in performance.\n\n@see <a href=\"https://cloud.google.com/bigquery/querying-data#largequeryresults\">Returning\n    Large Query Results</a>",
     :setter-doc
     "Sets whether the job is enabled to create arbitrarily large results. If {@code true} the\nquery is allowed to create large results at a slight cost in performance. If {@code true}\n{@link Builder#setDestinationTable(TableId)} must be provided.\n\n@see <a href=\"https://cloud.google.com/bigquery/querying-data#largequeryresults\">Returning\n    Large Query Results</a>"}
    :boolean]
   [:clustering
    {:optional true,
     :getter-doc
     "Returns the clustering specification for the destination table.",
     :setter-doc "Sets the clustering specification for the destination table."}
    :gcp.bigquery/Clustering]
   [:connectionProperties
    {:optional true,
     :getter-doc
     "Returns the connection properties for connection string with this job",
     :setter-doc
     "A connection-level property to customize query behavior. Under JDBC, these correspond\ndirectly to connection properties passed to the DriverManager. Under ODBC, these correspond\nto properties in the connection string. Currently, the only supported connection property is\n\"time_zone\", whose value represents the default timezone used to run the query. Additional\nproperties are allowed, but ignored. Specifying multiple connection properties with the same\nkey is an error.\n\n@param connectionProperties connectionProperties or {@code null} for none"}
    [:sequential {:min 1} :gcp.bigquery/ConnectionProperty]]
   [:createDisposition
    {:optional true,
     :getter-doc
     "Returns whether the job is allowed to create new tables.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/v2/jobs#configuration.query.createDisposition\">\n    Create Disposition</a>",
     :setter-doc
     "Sets whether the job is allowed to create tables.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/v2/jobs#configuration.query.createDisposition\">\n    Create Disposition</a>"}
    [:enum {:closed true} "CREATE_IF_NEEDED" "CREATE_NEVER"]]
   [:createSession
    {:optional true,
     :getter-doc
     "Returns whether to create a new session.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/sessions-create\">Create Sessions</a>",
     :setter-doc
     "Sets whether to create a new session. If {@code true} a random session id will be generated\nby BigQuery. If false, runs query with an existing session_id passed in ConnectionProperty,\notherwise runs query in non-session mode.\""}
    :boolean]
   [:defaultDataset
    {:optional true,
     :getter-doc
     "Returns the default dataset. This dataset is used for all unqualified table names used in the\nquery.",
     :setter-doc
     "Sets the default dataset. This dataset is used for all unqualified table names used in the\nquery."}
    :gcp.bigquery/DatasetId]
   [:destinationEncryptionConfiguration {:optional true} :gcp.bigquery/EncryptionConfiguration]
   [:destinationTable
    {:optional true,
     :getter-doc
     "Returns the table where to put query results. If not provided a new table is created. This\nvalue is required if {@link #allowLargeResults()} is {@code true}.",
     :setter-doc
     "Sets the table where to put query results. If not provided a new table is created. This value\nis required if {@link Builder#setAllowLargeResults(Boolean)} is set to {@code true}."}
    :gcp.bigquery/TableId]
   [:dryRun
    {:optional true,
     :getter-doc
     "Returns whether the job has to be dry run or not. If set, the job is not executed. A valid\nquery will return a mostly empty response with some processing statistics, while an invalid\nquery will return the same error it would if it wasn't a dry run.",
     :setter-doc
     "Sets whether the job has to be dry run or not. If set, the job is not executed. A valid query\nwill return a mostly empty response with some processing statistics, while an invalid query\nwill return the same error it would if it wasn't a dry run."}
    :boolean]
   [:flattenResults
    {:optional true,
     :getter-doc
     "Returns whether nested and repeated fields should be flattened. If set to {@code false} {@link\nBuilder#setAllowLargeResults(Boolean)} must be {@code true}.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/data#flatten\">Flatten</a>",
     :setter-doc
     "Sets whether nested and repeated fields should be flattened. If set to {@code false} {@link\nBuilder#setAllowLargeResults(Boolean)} must be {@code true}. By default results are\nflattened.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/data#flatten\">Flatten</a>"}
    :boolean]
   [:jobCreationMode
    {:optional true,
     :getter-doc "Returns the job creation mode.",
     :setter-doc
     "Provides different options on job creation. If not specified the job creation mode is assumed\nto be {@link JobCreationMode#JOB_CREATION_REQUIRED}."}
    [:enum {:closed true} "JOB_CREATION_MODE_UNSPECIFIED"
     "JOB_CREATION_REQUIRED" "JOB_CREATION_OPTIONAL"]]
   [:jobTimeoutMs
    {:optional true,
     :getter-doc "Returns the timeout associated with this job",
     :setter-doc
     "[Optional] Job timeout in milliseconds. If this time limit is exceeded, BigQuery may attempt\nto terminate the job.\n\n@param jobTimeoutMs jobTimeoutMs or {@code null} for none"}
    :int]
   [:labels
    {:optional true,
     :getter-doc "Returns the labels associated with this job",
     :setter-doc
     "The labels associated with this job. You can use these to organize and group your jobs. Label\nkeys and values can be no longer than 63 characters, can only contain lowercase letters,\nnumeric characters, underscores and dashes. International characters are allowed. Label\nvalues are optional. Label keys must start with a letter and each label in the list must have\na different key.\n\n@param labels labels or {@code null} for none"}
    [:map-of [:or keyword? [:string {:min 1}]] [:string {:min 1}]]]
   [:maxResults
    {:optional true,
     :getter-doc
     "This is only supported in the fast query path [Optional] The maximum number of rows of data to\nreturn per page of results. Setting this flag to a small value such as 1000 and then paging\nthrough results might improve reliability when the query result set is large. In addition to\nthis limit, responses are also limited to 10 MB. By default, there is no maximum row count, and\nonly the byte limit applies.\n\n@return value or {@code null} for none",
     :setter-doc
     "This is only supported in the fast query path [Optional] The maximum number of rows of data\nto return per page of results. Setting this flag to a small value such as 1000 and then\npaging through results might improve reliability when the query result set is large. In\naddition to this limit, responses are also limited to 10 MB. By default, there is no maximum\nrow count, and only the byte limit applies.\n\n@param maxResults maxResults or {@code null} for none"}
    :int]
   [:maximumBillingTier
    {:optional true,
     :getter-doc "Returns the optional billing tier limit for this job.",
     :setter-doc
     "Limits the billing tier for this job. Queries that have resource usage beyond this tier will\nfail (without incurring a charge). If unspecified, this will be set to your project default.\n\n@param maximumBillingTier maximum billing tier for this job"}
    [:int {:min -2147483648, :max 2147483647}]]
   [:maximumBytesBilled
    {:optional true,
     :getter-doc "Returns the optional bytes billed limit for this job.",
     :setter-doc
     "Limits the bytes billed for this job. Queries that will have bytes billed beyond this limit\nwill fail (without incurring a charge). If unspecified, this will be set to your project\ndefault.\n\n@param maximumBytesBilled maximum bytes billed for this job"}
    :int]
   [:priority
    {:optional true,
     :getter-doc "Returns the query priority.",
     :setter-doc
     "Sets a priority for the query. If not specified the priority is assumed to be {@link\nPriority#INTERACTIVE}."}
    [:enum {:closed true} "INTERACTIVE" "BATCH"]]
   [:query {:getter-doc "Returns the Google BigQuery SQL query."}
    [:string {:min 1}]]
   [:rangePartitioning
    {:optional true,
     :getter-doc "Returns the range partitioning specification for the table",
     :setter-doc
     "Range partitioning specification for this table. Only one of timePartitioning and\nrangePartitioning should be specified.\n\n@param rangePartitioning rangePartitioning or {@code null} for none"}
    :gcp.bigquery/RangePartitioning]
   [:reservation
    {:optional true,
     :getter-doc "Returns the reservation associated with this job",
     :setter-doc
     "[Optional] The reservation that job would use. User can specify a reservation to execute the\njob. If reservation is not set, reservation is determined based on the rules defined by the\nreservation assignments. The expected format is\n`projects/{project}/locations/{location}/reservations/{reservation}`.\n\n@param reservation reservation or {@code null} for none"}
    [:string {:min 1}]]
   [:schemaUpdateOptions
    {:optional true,
     :getter-doc
     "[Experimental] Returns options allowing the schema of the destination table to be updated as a\nside effect of the query job. Schema update options are supported in two cases: when\nwriteDisposition is WRITE_APPEND; when writeDisposition is WRITE_TRUNCATE and the destination\ntable is a partition of a table, specified by partition decorators. For normal tables,\nWRITE_TRUNCATE will always overwrite the schema.",
     :setter-doc
     "[Experimental] Sets options allowing the schema of the destination table to be updated as a\nside effect of the query job. Schema update options are supported in two cases: when\nwriteDisposition is WRITE_APPEND; when writeDisposition is WRITE_TRUNCATE and the destination\ntable is a partition of a table, specified by partition decorators. For normal tables,\nWRITE_TRUNCATE will always overwrite the schema."}
    [:sequential {:min 1}
     [:enum "ALLOW_FIELD_ADDITION" "ALLOW_FIELD_RELAXATION"]]]
   [:tableDefinitions
    {:optional true,
     :getter-doc
     "Returns the external tables definitions. If querying external data sources outside of BigQuery,\nthis value describes the data format, location and other properties of the data sources. By\ndefining these properties, the data sources can be queried as if they were standard BigQuery\ntables.",
     :setter-doc
     "Sets the external tables definitions. If querying external data sources outside of BigQuery,\nthis value describes the data format, location and other properties of the data sources. By\ndefining these properties, the data sources can be queried as if they were standard BigQuery\ntables."}
    [:map-of [:or keyword? [:string {:min 1}]]
     :gcp.bigquery/ExternalTableDefinition]]
   [:timePartitioning
    {:optional true,
     :getter-doc
     "Returns the time partitioning specification for the destination table.",
     :setter-doc
     "Sets the time partitioning specification for the destination table."}
    :gcp.bigquery/TimePartitioning]
   [:useLegacySql
    {:optional true,
     :getter-doc
     "Returns whether to use BigQuery's legacy SQL dialect for this query. By default this property\nis set to {@code false}. If set to {@code false}, the query will use BigQuery's <a\nhref=\"https://cloud.google.com/bigquery/sql-reference/\">Standard SQL</a>. When set to {@code\nfalse}, the values of {@link #allowLargeResults()} and {@link #flattenResults()} are ignored;\nquery will be run as if {@link #allowLargeResults()} is {@code true} and {@link\n#flattenResults()} is {@code false}. If set to {@code null} or {@code true}, legacy SQL dialect\nis used. This property is experimental and might be subject to change.",
     :setter-doc
     "Sets whether to use BigQuery's legacy SQL dialect for this query. By default this property is\nset to {@code false}. If set to {@code false}, the query will use BigQuery's <a\nhref=\"https://cloud.google.com/bigquery/sql-reference/\">Standard SQL</a>. When set to {@code\nfalse}, the values of {@link #setAllowLargeResults(Boolean)} and {@link\n#setFlattenResults(Boolean)} are ignored; query will be run as if {@link\n#setAllowLargeResults(Boolean)} is {@code true} and {@link #setFlattenResults(Boolean)} is\n{@code false}.\n\n<p>If set to {@code null} or {@code true}, legacy SQL dialect is used. This property is\nexperimental and might be subject to change."}
    :boolean]
   [:useQueryCache
    {:optional true,
     :getter-doc
     "Returns whether to look for the result in the query cache. The query cache is a best-effort\ncache that will be flushed whenever tables in the query are modified. Moreover, the query cache\nis only available when {@link Builder#setDestinationTable(TableId)} is not set.\n\n@see <a href=\"https://cloud.google.com/bigquery/querying-data#querycaching\">Query Caching</a>",
     :setter-doc
     "Sets whether to look for the result in the query cache. The query cache is a best-effort\ncache that will be flushed whenever tables in the query are modified. Moreover, the query\ncache is only available when {@link Builder#setDestinationTable(TableId)} is not set.\n\n@see <a href=\"https://cloud.google.com/bigquery/querying-data#querycaching\">Query Caching</a>"}
    :boolean]
   [:userDefinedFunctions
    {:optional true,
     :getter-doc
     "Returns user defined function resources that can be used by this query. Function resources can\neither be defined inline ({@link UserDefinedFunction.Type#INLINE}) or loaded from a Google\nCloud Storage URI ({@link UserDefinedFunction.Type#FROM_URI}.",
     :setter-doc
     "Sets user defined function resources that can be used by this query. Function resources can\neither be defined inline ({@link UserDefinedFunction#inline(String)}) or loaded from a Google\nCloud Storage URI ({@link UserDefinedFunction#fromUri(String)}."}
    [:sequential {:min 1} :gcp.bigquery/UserDefinedFunction]]
   [:writeDisposition
    {:optional true,
     :getter-doc
     "Returns the action that should occur if the destination table already exists.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/v2/jobs#configuration.query.writeDisposition\">\n    Write Disposition</a>",
     :setter-doc
     "Sets the action that should occur if the destination table already exists.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/v2/jobs#configuration.query.writeDisposition\">\n    Write Disposition</a>"}
    [:enum {:closed true} "WRITE_TRUNCATE" "WRITE_TRUNCATE_DATA" "WRITE_APPEND"
     "WRITE_EMPTY"]]
   ;;-------------------------
   [:namedParameters
    {:optional true
     :getter-doc "Returns the named query parameters to use for the query.",
     :setter-doc "Sets the query parameters to a set of named query parameters to use in the query.\n\n<p>The set of query parameters must either be all positional or all named parameters. Named\nparameters are denoted using an @ prefix, e.g. @myParam for a parameter named \"myParam\".\n\n<p>Additionally, useLegacySql must be set to false; query parameters cannot be used with\nlegacy SQL.\n\n<p>The values parameter can be set to null to clear out the named parameters so that\npositional parameters can be used instead."}
    [:map-of [:or 'simple-keyword? [:string {:min 1}]]
     :gcp.bigquery/QueryParameterValue]]
   [:positionalParameters
    {:optional true
     :getter-doc "Returns the positional query parameters to use for the query.",
     :setter-doc "Sets the query parameters to a list of positional query parameters to use in the query.\n\n<p>The set of query parameters must either be all positional or all named parameters.\nPositional parameters are denoted in the query with a question mark (?).\n\n<p>Additionally, useLegacySql must be set to false; query parameters cannot be used with\nlegacy SQL.\n\n<p>The values parameter can be set to null to clear out the positional parameters so that\nnamed parameters can be used instead."}
    [:sequential :gcp.bigquery/QueryParameterValue]]])


(def schema
  [:and
   {:doc "Google BigQuery Query Job configuration. A Query Job runs a query against BigQuery data. Query job configurations have {@link JobConfiguration.Type#QUERY} type.",
    :gcp/category :variant-accessor,
    :gcp/key :gcp.bigquery/QueryJobConfiguration}
   map-schema
   [:fn
    {:error/message ":namedParameters + :positionalParameters are exclusive to each-other"}
    '(fn [m]
       (not (and (contains? m :namedParameters)
                 (contains? m :positionalParameters))))]])

(g/include-schema-registry!
  (with-meta {:gcp.bigquery/QueryJobConfiguration schema,
              :gcp.bigquery/QueryJobConfiguration.JobCreationMode QueryJobConfiguration$JobCreationMode-schema,
              :gcp.bigquery/QueryJobConfiguration.Priority QueryJobConfiguration$Priority-schema}
    {:gcp.global/name "gcp.bigquery.QueryJobConfiguration"}))