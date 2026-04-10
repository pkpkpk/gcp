(ns gcp.bigquery.custom.JobStatistics
  (:require gcp.bigquery.BiEngineStats
            gcp.bigquery.DmlStats
            gcp.bigquery.MetadataCacheStats
            gcp.bigquery.QueryStage
            gcp.bigquery.RoutineId
            [gcp.bigquery.Schema :as Schema]
            gcp.bigquery.SearchStats
            gcp.bigquery.TableId
            gcp.bigquery.TimelineSample
            gcp.api.services.bigquery.model.QueryParameter
            [gcp.global :as g])
  (:import (com.google.cloud.bigquery JobStatistics
                                      JobStatistics$CopyStatistics
                                      JobStatistics$ExtractStatistics
                                      JobStatistics$LoadStatistics
                                      JobStatistics$QueryStatistics
                                      JobStatistics$QueryStatistics$ExportDataStats
                                      JobStatistics$ReservationUsage
                                      JobStatistics$ScriptStatistics
                                      JobStatistics$ScriptStatistics$ScriptStackFrame
                                      JobStatistics$SessionInfo)))

(defn ReservationUsage-to-edn
  [^JobStatistics$ReservationUsage arg]
  {:name (.getName arg)
   :slotMs (.getSlotMs arg)})

(defn ScriptStackFrame-to-edn
  [^JobStatistics$ScriptStatistics$ScriptStackFrame arg]
  {:endColumn (.getEndColumn arg)
   :endLine (.getEndLine arg)
   :procedureId (.getProcedureId arg)
   :startColumn (.getStartColumn arg)
   :startLine (.getStartLine arg)
   :text (.getText arg)})

(defn ScriptStatistics-to-edn
  [^JobStatistics$ScriptStatistics arg]
  {:evaluationKind (.getEvaluationKind arg)
   :stackFrames    (mapv ScriptStackFrame-to-edn (.getStackFrames arg))})

(defn SessionInfo-to-edn [^JobStatistics$SessionInfo arg]
  {:sessionId (.getSessionId arg)})

(def JobStatistics$SessionInfo-schema
  [:map {:closed true
         :doc "SessionInfo contains information about the session if this job is part of one."}
   [:sessionId :string]])

(defn base-to-edn [^JobStatistics arg]
  {:post [(g/strict! :gcp.bigquery/JobStatistics %)]}
  (cond-> {:creationTime (.getCreationTime arg)
           :startTime    (.getStartTime arg)}
          (some? (.getEndTime arg)) (assoc :endTime (.getEndTime arg))
          (some? (.getNumChildJobs arg)) (assoc :numChildJobs (.getNumChildJobs arg))
          (some? (.getParentJobId arg)) (assoc :parentJobId (.getParentJobId arg))
          (some? (.getTotalSlotMs arg)) (assoc :totalSlotMs (.getTotalSlotMs arg))
          (some? (.getReservationUsage arg)) (assoc :reservationUsage (map ReservationUsage-to-edn (.getReservationUsage arg)))
          (some? (.getSessionInfo arg)) (assoc :sessionInfo (SessionInfo-to-edn (.getSessionInfo arg)))
          (some? (.getScriptStatistics arg)) (assoc :scriptStatistics (ScriptStatistics-to-edn (.getScriptStatistics arg)))
          (some? (.getTransactionInfo arg)) (assoc :transactionInfo {:transactionId (.getTransactionId (.getTransactionInfo arg))})))

(def base-schema
  [:map
   [:creationTime {:doc "Typically a timestamp in ms"} :int]
   [:endTime {:doc "Typically a timestamp in ms" :optional true} :int]
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
   [:sessionInfo {:optional true} :gcp.bigquery/JobStatistics.SessionInfo]
   [:startTime :int] ; Timestamp in ms
   [:totalSlotMs {:optional true} :int]
   [:transactionInfo
    {:optional true
     :doc "TransactionInfo contains information about a multi-statement transaction that may have associated with a job."}
    [:map {:closed true} [:transactionId :string]]]])

#!----------------------------------------------------------------------------------------------------------------------

(def JobStatistics$CopyStatistics-schema
  [:map
   [:copiedLogicalBytes {:doc "Returns number of logical bytes copied to the destination table"} :int]
   [:copiedRows {:doc "Returns number of rows copied to the destination table"} :int]])

(defn CopyStatistics-to-edn
  [^JobStatistics$CopyStatistics arg]
  (cond-> {}
    (some? (.getCopiedLogicalBytes arg)) (assoc :copiedLogicalBytes (.getCopiedLogicalBytes arg))
    (some? (.getCopiedRows arg)) (assoc :copiedRows (.getCopiedRows arg))))

#!----------------------------------------------------------------------------------------------------------------------

(def JobStatistics$ExtractStatistics-schema
  [:map
   [:destinationUriFileCounts
    {:doc "Returns the number of files per destination URI or URI pattern specified in the extract job. These values will be in the same order as the URIs specified by ExtractJobConfiguration#getDestinationUris()"}
    [:sequential :int]]
   [:inputBytes {:doc "Returns number of user bytes extracted into the result"} :int]])

(defn ExtractStatistics-to-edn
  [^JobStatistics$ExtractStatistics arg]
  (cond-> {}
    (some? (.getDestinationUriFileCounts arg)) (assoc :destinationUriFileCounts (vec (.getDestinationUriFileCounts arg)))
    (some? (.getInputBytes arg)) (assoc :inputBytes (.getInputBytes arg))))

#!----------------------------------------------------------------------------------------------------------------------

(def JobStatistics$LoadStatistics-schema
  [:map
   [:badRecords {:optional true :doc "Returns the number of bad records reported in a job"} :int]
   [:inputBytes {:optional true :doc "Returns the number of bytes of source data in a load job"} :int]
   [:inputFiles {:optional true :doc "Returns the number of source files in a load job"} :int]
   [:outputBytes {:optional true :doc "Returns the size of the data loaded by a load job so far, in bytes"} :int]
   [:outputRows {:optional true :doc "Returns the number of rows loaded by a load job so far"} :int]])

(defn LoadStatistics-to-edn
  [^JobStatistics$LoadStatistics arg]
  (cond-> {}
    (some? (.getBadRecords arg)) (assoc :badRecords (.getBadRecords arg))
    (some? (.getInputBytes arg)) (assoc :inputBytes (.getInputBytes arg))
    (some? (.getInputFiles arg)) (assoc :inputFiles (.getInputFiles arg))
    (some? (.getOutputBytes arg)) (assoc :outputBytes (.getOutputBytes arg))
    (some? (.getOutputRows arg)) (assoc :outputRows (.getOutputRows arg))))

#!----------------------------------------------------------------------------------------------------------------------

(def JobStatistics$QueryStatistics$StatementType-schema
  [:enum
   "ALTER_MATERIALIZED_VIEW"
   "ALTER_SCHEMA"
   "ALTER_TABLE"
   "ALTER_VIEW"
   "CALL"
   "CREATE_EXTERNAL_TABLE"
   "CREATE_FUNCTION"
   "CREATE_MATERIALIZED_VIEW"
   "CREATE_MODEL"
   "CREATE_PROCEDURE"
   "CREATE_ROW_ACCESS_POLICY"
   "CREATE_SCHEMA"
   "CREATE_SEARCH_INDEX"
   "CREATE_SNAPSHOT_TABLE"
   "CREATE_TABLE"
   "CREATE_TABLE_AS_SELECT"
   "CREATE_TABLE_FUNCTION"
   "CREATE_VIEW"
   "DELETE"
   "DROP_EXTERNAL_TABLE"
   "DROP_FUNCTION"
   "DROP_MATERIALIZED_VIEW"
   "DROP_MODEL"
   "DROP_PROCEDURE"
   "DROP_ROW_ACCESS_POLICY"
   "DROP_SCHEMA"
   "DROP_SEARCH_INDEX"
   "DROP_SNAPSHOT_TABLE"
   "DROP_TABLE"
   "DROP_TABLE_FUNCTION"
   "DROP_VIEW"
   "EXPORT_DATA"
   "EXPORT_MODEL"
   "INSERT"
   "LOAD_DATA"
   "MERGE"
   "SCRIPT"
   "SELECT"
   "TRUNCATE_TABLE"
   "UPDATE"])

(def JobStatistics$QueryStatistics-schema
  [:map
   [:biEngineStats
    {:optional true :doc "Returns query statistics specific to the use of BI Engine"} :gcp.bigquery/BiEngineStats]
   [:billingTier {:optional true} :int]
   [:cacheHit {:optional true} :boolean]
   [:ddlOperationPerformed {:optional true} :string]
   [:ddlTargetRoutine {:optional true} :gcp.bigquery/RoutineId]
   [:ddlTargetTable {:optional true} :gcp.bigquery/TableId]
   [:dmlStats {:optional true} :gcp.bigquery/DmlStats]
   [:estimatedBytesProcessed {:optional true} :int]
   [:exportDataStats
    {:optional true
     :doc "Detailed statistics for EXPORT DATA statement"}
    [:map {:closed true}
     [:fileCount {:optional true} :int]
     [:rowCount {:optional true} :int]]]
   [:metadataCacheStats {:optional true :doc "Statistics for metadata caching in BigLake tables"} :gcp.bigquery/MetadataCacheStats]
   [:numDmlAffectedRows {:optional true :doc "The number of rows affected by a DML statement. Present only for DML statements INSERT, UPDATE or DELETE"} :int]
   [:queryParameters
    {:optional true
     :doc "Standard SQL only: Returns a list of undeclared query parameters detected during a dry run validation"}
    [:sequential :gcp.api.services.bigquery.model/QueryParameter]]
   [:queryPlan
    {:optional true
     :doc "Returns the query plan as a list of stages or null if a query plan is not available. Each stage involves a number of steps that read from data sources, perform a series of transformations on the input, and emit an output to a future stage (or the final result). The query plan is available for a completed query job and is retained for 7 days."}
    [:sequential :gcp.bigquery/QueryStage]]
   [:referencedTables
    {:optional true
     :doc "Referenced tables for the job. Queries that reference more than 50 tables will not have a complete list"}
    [:sequential :gcp.bigquery/TableId]]
   [:schema {:optional true
             :doc "Returns the schema for the query result. Present only for successful dry run of non-legacy SQL queries"}
    :gcp.bigquery/Schema]
   [:searchStats
    {:optional true
     :doc "Statistics for a search query. Populated as part of JobStatistics2. Provides information about how indexes are used in search queries. If an index is not used, you can retrieve debugging information about the reason why."}
    :gcp.bigquery/SearchStats]
   [:statementType
    {:optional true
     :doc "[BETA] The type of query statement, if valid. Possible values include: SELECT INSERT UPDATE DELETE CREATE_TABLE CREATE_TABLE_AS_SELECT DROP_TABLE CREATE_VIEW DROP_VIEW"}
    JobStatistics$QueryStatistics$StatementType-schema]
   [:timeline
    {:optional true
     :doc "Return the timeline for the query, as a list of timeline samples. Each sample provides information about the overall progress of the query. Information includes time of the sample, progress reporting on active, completed, and pending units of work, as well as the cumulative estimation of slot-milliseconds consumed by the query."}
    [:sequential :gcp.bigquery/TimelineSample]]
   [:totalBytesBilled {:optional true} :int]
   [:totalBytesProcessed {:optional true} :int]
   [:totalPartitionsProcessed {:optional true} :int]
   [:useReadApi {:optional true :doc "Returns whether the query result is read from the high throughput ReadAPI"} :boolean]])

(defn ExportDataStats-to-edn
  [^JobStatistics$QueryStatistics$ExportDataStats arg]
  (cond-> {}
    (some? (.getFileCount arg)) (assoc :fileCount (.getFileCount arg))
    (some? (.getRowCount arg)) (assoc :rowCount (.getRowCount arg))))

(defn QueryStatistics-to-edn
  [^JobStatistics$QueryStatistics arg]
  (cond-> {}
    (some? (.getBiEngineStats arg)) (assoc :biEngineStats (gcp.bigquery.BiEngineStats/to-edn (.getBiEngineStats arg)))
    (some? (.getBillingTier arg)) (assoc :billingTier (.getBillingTier arg))
    (some? (.getCacheHit arg)) (assoc :cacheHit (.getCacheHit arg))
    (some? (.getDdlOperationPerformed arg)) (assoc :ddlOperationPerformed (.getDdlOperationPerformed arg))
    (some? (.getDdlTargetRoutine arg)) (assoc :ddlTargetRoutine (gcp.bigquery.RoutineId/to-edn (.getDdlTargetRoutine arg)))
    (some? (.getDdlTargetTable arg)) (assoc :ddlTargetTable (gcp.bigquery.TableId/to-edn (.getDdlTargetTable arg)))
    (some? (.getDmlStats arg)) (assoc :dmlStats (gcp.bigquery.DmlStats/to-edn (.getDmlStats arg)))
    (some? (.getEstimatedBytesProcessed arg)) (assoc :estimatedBytesProcessed (.getEstimatedBytesProcessed arg))
    (some? (.getExportDataStats arg)) (assoc :exportDataStats (ExportDataStats-to-edn (.getExportDataStats arg)))
    (some? (.getMetadataCacheStats arg)) (assoc :metadataCacheStats (gcp.bigquery.MetadataCacheStats/to-edn (.getMetadataCacheStats arg)))
    (some? (.getNumDmlAffectedRows arg)) (assoc :numDmlAffectedRows (.getNumDmlAffectedRows arg))
    (some? (.getQueryParameters arg)) (assoc :queryParameters (mapv gcp.api.services.bigquery.model.QueryParameter/to-edn (.getQueryParameters arg)))
    (some? (.getQueryPlan arg)) (assoc :queryPlan (mapv gcp.bigquery.QueryStage/to-edn (.getQueryPlan arg)))
    (some? (.getReferencedTables arg)) (assoc :referencedTables (mapv gcp.bigquery.TableId/to-edn (.getReferencedTables arg)))
    (some? (.getSchema arg)) (assoc :schema (Schema/to-edn (.getSchema arg)))
    (some? (.getSearchStats arg)) (assoc :searchStats (gcp.bigquery.SearchStats/to-edn (.getSearchStats arg)))
    (some? (.getStatementType arg)) (assoc :statementType (.name (.getStatementType arg)))
    (some? (.getTimeline arg)) (assoc :timeline (mapv gcp.bigquery.TimelineSample/to-edn (.getTimeline arg)))
    (some? (.getTotalBytesBilled arg)) (assoc :totalBytesBilled (.getTotalBytesBilled arg))
    (some? (.getTotalBytesProcessed arg)) (assoc :totalBytesProcessed (.getTotalBytesProcessed arg))
    (some? (.getTotalPartitionsProcessed arg)) (assoc :totalPartitionsProcessed (.getTotalPartitionsProcessed arg))
    (some? (.getUseReadApi arg)) (assoc :useReadApi (.getUseReadApi arg))))

#!----------------------------------------------------------------------------------------------------------------------

(def schema
  [:and
   {:gcp/category :read-only}
   base-schema
   [:or
    JobStatistics$CopyStatistics-schema
    JobStatistics$ExtractStatistics-schema
    JobStatistics$LoadStatistics-schema
    JobStatistics$QueryStatistics-schema]])

(defn to-edn [^JobStatistics arg]
  {:post [(g/strict! schema %)]}
  (let [base (base-to-edn arg)]
    (cond
      (instance? JobStatistics$CopyStatistics arg)
      (merge base (CopyStatistics-to-edn arg))
      (instance? JobStatistics$ExtractStatistics arg)
      (merge base (ExtractStatistics-to-edn arg))
      (instance? JobStatistics$LoadStatistics arg)
      (merge base (LoadStatistics-to-edn arg))
      (instance? JobStatistics$QueryStatistics arg)
      (merge base (QueryStatistics-to-edn arg))
      :else
      (throw (ex-info "unknown statistics type" {:arg arg
                                                 :base base})))))

(defn from-edn [arg] (throw (Exception. "JobStatistics is read-only")))

(g/include-schema-registry!
  (with-meta {:gcp.bigquery/JobStatistics.SessionInfo JobStatistics$SessionInfo-schema
              :gcp.bigquery/JobStatistics.QueryStatistics JobStatistics$QueryStatistics-schema
              :gcp.bigquery/JobStatistics schema}
             {::g/name "gcp.bigquery.custom.JobStatistics"}))
