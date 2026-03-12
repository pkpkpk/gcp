;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.ConnectionSettings
  {:doc "ConnectionSettings for setting up a BigQuery query connection."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.ConnectionSettings"
   :gcp.dev/certification
     {:base-seed 1772050367975
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1772050367975 :standard 1772050367976 :stress 1772050367977}
      :protocol-hash
        "b8a5eb17212acb18f49dcb7f15a243eac9b32f54c0f054a99be660a3a25e0315"
      :timestamp "2026-02-25T20:14:35.429040260Z"}}
  (:require
    [gcp.bindings.bigquery.Clustering :as Clustering]
    [gcp.bindings.bigquery.ConnectionProperty :as ConnectionProperty]
    [gcp.bindings.bigquery.DatasetId :as DatasetId]
    [gcp.bindings.bigquery.EncryptionConfiguration :as EncryptionConfiguration]
    [gcp.bindings.bigquery.ExternalTableDefinition :as ExternalTableDefinition]
    [gcp.bindings.bigquery.RangePartitioning :as RangePartitioning]
    [gcp.bindings.bigquery.TableId :as TableId]
    [gcp.bindings.bigquery.TimePartitioning :as TimePartitioning]
    [gcp.bindings.bigquery.UserDefinedFunction :as UserDefinedFunction]
    [gcp.global :as global])
  (:import [com.google.cloud.bigquery ConnectionSettings
            ConnectionSettings$Builder JobInfo$CreateDisposition
            JobInfo$SchemaUpdateOption JobInfo$WriteDisposition
            QueryJobConfiguration$Priority]))

(defn ^ConnectionSettings from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/ConnectionSettings arg)
  (let [builder (ConnectionSettings/newBuilder)]
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
    (when (some? (get arg :flattenResults))
      (.setFlattenResults builder (get arg :flattenResults)))
    (when (some? (get arg :jobTimeoutMs))
      (.setJobTimeoutMs builder (get arg :jobTimeoutMs)))
    (when (some? (get arg :maxResultPerPage))
      (.setMaxResultPerPage builder (int (get arg :maxResultPerPage))))
    (when (some? (get arg :maxResults))
      (.setMaxResults builder (get arg :maxResults)))
    (when (some? (get arg :maximumBillingTier))
      (.setMaximumBillingTier builder (int (get arg :maximumBillingTier))))
    (when (some? (get arg :maximumBytesBilled))
      (.setMaximumBytesBilled builder (get arg :maximumBytesBilled)))
    (when (some? (get arg :minResultSize))
      (.setMinResultSize builder (int (get arg :minResultSize))))
    (when (some? (get arg :numBufferedRows))
      (.setNumBufferedRows builder (int (get arg :numBufferedRows))))
    (when (some? (get arg :priority))
      (.setPriority builder
                    (QueryJobConfiguration$Priority/valueOf (get arg
                                                                 :priority))))
    (when (some? (get arg :rangePartitioning))
      (.setRangePartitioning builder
                             (RangePartitioning/from-edn
                               (get arg :rangePartitioning))))
    (when (some? (get arg :requestTimeout))
      (.setRequestTimeout builder (get arg :requestTimeout)))
    (when (some? (get arg :schemaUpdateOptions))
      (.setSchemaUpdateOptions builder
                               (map JobInfo$SchemaUpdateOption/valueOf
                                 (get arg :schemaUpdateOptions))))
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
    (when (some? (get arg :totalToPageRowCountRatio))
      (.setTotalToPageRowCountRatio builder
                                    (int (get arg :totalToPageRowCountRatio))))
    (when (some? (get arg :useQueryCache))
      (.setUseQueryCache builder (get arg :useQueryCache)))
    (when (some? (get arg :useReadAPI))
      (.setUseReadAPI builder (get arg :useReadAPI)))
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
  [^ConnectionSettings arg]
  {:post [(global/strict! :gcp.bindings.bigquery/ConnectionSettings %)]}
  (cond-> {}
    (.getAllowLargeResults arg) (assoc :allowLargeResults
                                  (.getAllowLargeResults arg))
    (.getClustering arg) (assoc :clustering
                           (Clustering/to-edn (.getClustering arg)))
    (.getConnectionProperties arg) (assoc :connectionProperties
                                     (map ConnectionProperty/to-edn
                                       (.getConnectionProperties arg)))
    (.getCreateDisposition arg) (assoc :createDisposition
                                  (.name (.getCreateDisposition arg)))
    (.getCreateSession arg) (assoc :createSession (.getCreateSession arg))
    (.getDefaultDataset arg) (assoc :defaultDataset
                               (DatasetId/to-edn (.getDefaultDataset arg)))
    (.getDestinationEncryptionConfiguration arg)
      (assoc :destinationEncryptionConfiguration
        (EncryptionConfiguration/to-edn (.getDestinationEncryptionConfiguration
                                          arg)))
    (.getDestinationTable arg) (assoc :destinationTable
                                 (TableId/to-edn (.getDestinationTable arg)))
    (.getFlattenResults arg) (assoc :flattenResults (.getFlattenResults arg))
    (.getJobTimeoutMs arg) (assoc :jobTimeoutMs (.getJobTimeoutMs arg))
    (.getMaxResultPerPage arg) (assoc :maxResultPerPage
                                 (.getMaxResultPerPage arg))
    (.getMaxResults arg) (assoc :maxResults (.getMaxResults arg))
    (.getMaximumBillingTier arg) (assoc :maximumBillingTier
                                   (.getMaximumBillingTier arg))
    (.getMaximumBytesBilled arg) (assoc :maximumBytesBilled
                                   (.getMaximumBytesBilled arg))
    (.getMinResultSize arg) (assoc :minResultSize (.getMinResultSize arg))
    (.getNumBufferedRows arg) (assoc :numBufferedRows (.getNumBufferedRows arg))
    (.getPriority arg) (assoc :priority (.name (.getPriority arg)))
    (.getRangePartitioning arg) (assoc :rangePartitioning
                                  (RangePartitioning/to-edn
                                    (.getRangePartitioning arg)))
    (.getRequestTimeout arg) (assoc :requestTimeout (.getRequestTimeout arg))
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
    (.getTotalToPageRowCountRatio arg) (assoc :totalToPageRowCountRatio
                                         (.getTotalToPageRowCountRatio arg))
    (.getUseQueryCache arg) (assoc :useQueryCache (.getUseQueryCache arg))
    (.getUseReadAPI arg) (assoc :useReadAPI (.getUseReadAPI arg))
    (.getUserDefinedFunctions arg) (assoc :userDefinedFunctions
                                     (map UserDefinedFunction/to-edn
                                       (.getUserDefinedFunctions arg)))
    (.getWriteDisposition arg) (assoc :writeDisposition
                                 (.name (.getWriteDisposition arg)))))

(def schema
  [:map
   {:closed true,
    :doc "ConnectionSettings for setting up a BigQuery query connection.",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/ConnectionSettings}
   [:allowLargeResults
    {:optional true,
     :getter-doc
       "Returns whether the job is enabled to create arbitrarily large results. If {@code true} the\nquery is allowed to create large results at a slight cost in performance. the query is allowed\nto create large results at a slight cost in performance.\n\n@see <a href=\"https://cloud.google.com/bigquery/querying-data#largequeryresults\">Returning\n    Large Query Results</a>",
     :setter-doc
       "Sets whether the job is enabled to create arbitrarily large results. If {@code true} the\nquery is allowed to create large results at a slight cost in performance. If {@code true}\n{@link ConnectionSettings.Builder#setDestinationTable(TableId)} must be provided.\n\n@see <a href=\"https://cloud.google.com/bigquery/querying-data#largequeryresults\">Returning\n    Large Query Results</a>"}
    :boolean]
   [:clustering
    {:optional true,
     :getter-doc
       "Returns the clustering specification for the destination table.",
     :setter-doc "Sets the clustering specification for the destination table."}
    :gcp.bindings.bigquery/Clustering]
   [:connectionProperties
    {:optional true,
     :getter-doc
       "Returns the connection properties for connection string with this query",
     :setter-doc
       "Sets a connection-level property to customize query behavior.\n\n@param connectionProperties connectionProperties or {@code null} for none"}
    [:sequential {:min 1} :gcp.bindings.bigquery/ConnectionProperty]]
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
     :getter-doc "Returns the default dataset",
     :setter-doc
       "Sets the default dataset. This dataset is used for all unqualified table names used in the\nquery."}
    :gcp.bindings.bigquery/DatasetId]
   [:destinationEncryptionConfiguration
    {:optional true,
     :getter-doc
       "Returns the custom encryption configuration (e.g., Cloud KMS keys)",
     :setter-doc
       "Sets the custom encryption configuration (e.g., Cloud KMS keys).\n\n@param destinationEncryptionConfiguration destinationEncryptionConfiguration or {@code null}\n    for none"}
    :gcp.bindings.bigquery/EncryptionConfiguration]
   [:destinationTable
    {:optional true,
     :getter-doc
       "Returns the table where to put query results. If not provided a new table is created. This\nvalue is required if {@link #getAllowLargeResults()} is {@code true}.",
     :setter-doc
       "Sets the table where to put query results. If not provided a new table is created. This value\nis required if {@link ConnectionSettings.Builder#setAllowLargeResults(Boolean)} is set to\n{@code true}."}
    :gcp.bindings.bigquery/TableId]
   [:flattenResults
    {:optional true,
     :getter-doc
       "Returns whether nested and repeated fields should be flattened. If set to {@code false} {@link\nConnectionSettings.Builder#setAllowLargeResults(Boolean)} must be {@code true}.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/data#flatten\">Flatten</a>",
     :setter-doc
       "Sets whether nested and repeated fields should be flattened. If set to {@code false} {@link\nConnectionSettings.Builder#setAllowLargeResults(Boolean)} must be {@code true}. By default\nresults are flattened.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/data#flatten\">Flatten</a>"}
    :boolean]
   [:jobTimeoutMs
    {:optional true,
     :getter-doc "Returns the timeout associated with this job",
     :setter-doc
       "[Optional] Job timeout in milliseconds. If this time limit is exceeded, BigQuery may attempt\nto terminate the job.\n\n@param jobTimeoutMs jobTimeoutMs or {@code null} for none"}
    :int]
   [:maxResultPerPage
    {:optional true,
     :setter-doc
       "Sets the maximum records per page to be used for pagination. This is used as an input for the\ntabledata.list and jobs.getQueryResults RPC calls\n\n@param maxResultPerPage"}
    [:int {:min -2147483648, :max 2147483647}]]
   [:maxResults
    {:optional true,
     :getter-doc "Returns the maximum number of rows of data",
     :setter-doc
       "Sets the maximum number of rows of data to return per page of results. Setting this flag to a\nsmall value such as 1000 and then paging through results might improve reliability when the\nquery result set is large. In addition to this limit, responses are also limited to 10 MB. By\ndefault, there is no maximum row count, and only the byte limit applies.\n\n@param maxResults maxResults or {@code null} for none"}
    :int]
   [:maximumBillingTier
    {:optional true,
     :getter-doc "Returns the optional billing tier limit for this job.",
     :setter-doc
       "Limits the billing tier for this job. Queries that have resource usage beyond this tier will\nfail (without incurring a charge). If unspecified, this will be set to your project default.\n\n@param maximumBillingTier maximum billing tier for this job"}
    [:int {:min -2147483648, :max 2147483647}]]
   [:maximumBytesBilled
    {:optional true,
     :getter-doc "Returns the limits the bytes billed for this job",
     :setter-doc
       "Limits the bytes billed for this job. Queries that will have bytes billed beyond this limit\nwill fail (without incurring a charge). If unspecified, this will be set to your project\ndefault.\n\n@param maximumBytesBilled maximum bytes billed for this job"}
    :int]
   [:minResultSize
    {:optional true,
     :setter-doc
       "Sets the minimum result size for which the Read API will be enabled\n\n@param minResultSize minResultSize"}
    [:int {:min -2147483648, :max 2147483647}]]
   [:numBufferedRows
    {:optional true,
     :getter-doc "Returns the number of rows of data to pre-fetch",
     :setter-doc
       "Sets the number of rows in the buffer (a blocking queue) that query results are consumed\nfrom.\n\n@param numBufferedRows numBufferedRows or {@code null} for none"}
    [:int {:min -2147483648, :max 2147483647}]]
   [:priority
    {:optional true,
     :getter-doc "Returns the query priority.",
     :setter-doc
       "Sets a priority for the query. If not specified the priority is assumed to be {@link\nPriority#INTERACTIVE}."}
    [:enum {:closed true} "INTERACTIVE" "BATCH"]]
   [:rangePartitioning
    {:optional true,
     :getter-doc "Returns the range partitioning specification for the table",
     :setter-doc
       "Range partitioning specification for this table. Only one of timePartitioning and\nrangePartitioning should be specified.\n\n@param rangePartitioning rangePartitioning or {@code null} for none"}
    :gcp.bindings.bigquery/RangePartitioning]
   [:requestTimeout
    {:optional true,
     :getter-doc
       "Returns the synchronous response timeoutMs associated with this query",
     :setter-doc
       "Sets how long to wait for the query to complete, in milliseconds, before the request times\nout and returns. Note that this is only a timeout for the request, not the query. If the\nquery takes longer to run than the timeout value, the call returns without any results and\nwith the 'jobComplete' flag set to false. You can call GetQueryResults() to wait for the\nquery to complete and read the results. The default value is 10000 milliseconds (10 seconds).\n\n@param timeoutMs or {@code null} for none"}
    :int]
   [:schemaUpdateOptions
    {:optional true,
     :getter-doc
       "[Experimental] Returns options allowing the schema of the destination table to be updated as a\nside effect of the query job. Schema update options are supported in two cases: when\nwriteDisposition is WRITE_APPEND; when writeDisposition is WRITE_TRUNCATE and the destination\ntable is a partition of a table, specified by partition decorators. For normal tables,\nWRITE_TRUNCATE will always overwrite the schema.",
     :setter-doc
       "[Experimental] Sets options allowing the schema of the destination table to be updated as a\nside effect of the query job. Schema update options are supported in two cases: when\nwriteDisposition is WRITE_APPEND; when writeDisposition is WRITE_TRUNCATE and the destination\ntable is a partition of a table, specified by partition decorators. For normal tables,\nWRITE_TRUNCATE will always overwrite the schema."}
    [:sequential {:min 1}
     [:enum {:closed true} "ALLOW_FIELD_ADDITION" "ALLOW_FIELD_RELAXATION"]]]
   [:tableDefinitions
    {:optional true,
     :getter-doc
       "Returns the external tables definitions. If querying external data sources outside of BigQuery,\nthis value describes the data format, location and other properties of the data sources. By\ndefining these properties, the data sources can be queried as if they were standard BigQuery\ntables.",
     :setter-doc
       "Sets the external tables definitions. If querying external data sources outside of BigQuery,\nthis value describes the data format, location and other properties of the data sources. By\ndefining these properties, the data sources can be queried as if they were standard BigQuery\ntables."}
    [:map-of [:or simple-keyword? [:string {:min 1}]]
     :gcp.bindings.bigquery/ExternalTableDefinition]]
   [:timePartitioning
    {:optional true,
     :getter-doc
       "Returns the time partitioning specification for the destination table.",
     :setter-doc
       "Sets the time partitioning specification for the destination table."}
    :gcp.bindings.bigquery/TimePartitioning]
   [:totalToPageRowCountRatio
    {:optional true,
     :setter-doc
       "Sets a ratio of the total number of records and the records returned in the current page.\nThis value is checked before calling the Read API\n\n@param totalToPageRowCountRatio totalToPageRowCountRatio"}
    [:int {:min -2147483648, :max 2147483647}]]
   [:useQueryCache
    {:optional true,
     :getter-doc "Returns whether to look for the result in the query cache",
     :setter-doc
       "Sets whether to look for the result in the query cache. The query cache is a best-effort\ncache that will be flushed whenever tables in the query are modified. Moreover, the query\ncache is only available when {@link ConnectionSettings.Builder#setDestinationTable(TableId)}\nis not set.\n\n@see <a href=\"https://cloud.google.com/bigquery/querying-data#querycaching\">Query Caching</a>"}
    :boolean]
   [:useReadAPI
    {:optional true,
     :getter-doc
       "Returns useReadAPI flag, enabled by default. Read API will be used if the underlying conditions\nare satisfied and this flag is enabled",
     :setter-doc
       "Sets useReadAPI flag, enabled by default. Read API will be used if the underlying conditions\nare satisfied and this flag is enabled\n\n@param useReadAPI or {@code true} for none"}
    :boolean]
   [:userDefinedFunctions
    {:optional true,
     :getter-doc
       "Returns user defined function resources that can be used by this query. Function resources can\neither be defined inline ({@link UserDefinedFunction.Type#INLINE}) or loaded from a Google\nCloud Storage URI ({@link UserDefinedFunction.Type#FROM_URI}.",
     :setter-doc
       "Sets user defined function resources that can be used by this query. Function resources can\neither be defined inline ({@link UserDefinedFunction#inline(String)}) or loaded from a Google\nCloud Storage URI ({@link UserDefinedFunction#fromUri(String)}."}
    [:sequential {:min 1} :gcp.bindings.bigquery/UserDefinedFunction]]
   [:writeDisposition
    {:optional true,
     :getter-doc
       "Returns the action that should occur if the destination table already exists.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/v2/jobs#configuration.query.writeDisposition\">\n    Write Disposition</a>",
     :setter-doc
       "Sets the action that should occur if the destination table already exists.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/v2/jobs#configuration.query.writeDisposition\">\n    Write Disposition</a>"}
    [:enum {:closed true} "WRITE_TRUNCATE" "WRITE_TRUNCATE_DATA" "WRITE_APPEND"
     "WRITE_EMPTY"]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/ConnectionSettings schema}
    {:gcp.global/name "gcp.bindings.bigquery.ConnectionSettings"}))