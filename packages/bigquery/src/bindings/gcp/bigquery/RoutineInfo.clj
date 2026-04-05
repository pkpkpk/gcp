;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.RoutineInfo
  {:doc
     "Google BigQuery routine information. A Routine is an API abstraction that encapsulates several\nrelated concepts inside the BigQuery service, including scalar user defined functions (UDFS) and\nstored procedures.\n\n<p>For more information about the REST representation of routines, see:\nhttps://cloud.google.com/bigquery/docs/reference/rest/v2/routines\n\n<p>For more information about working with scalar functions, see:\nhttps://cloud.google.com/bigquery/docs/reference/standard-sql/user-defined-functions"
   :file-git-sha "6e3e07a22b8397e1e9d5b567589e44abc55961f2"
   :fqcn "com.google.cloud.bigquery.RoutineInfo"
   :gcp.dev/certification
     {:base-seed 1775131013752
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1775131013752 :standard 1775131013753 :stress 1775131013754}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:56:55.284838322Z"}}
  (:require [gcp.bigquery.RemoteFunctionOptions :as RemoteFunctionOptions]
            [gcp.bigquery.RoutineArgument :as RoutineArgument]
            [gcp.bigquery.RoutineId :as RoutineId]
            [gcp.bigquery.custom.StandardSQL :as StandardSQL]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery RoutineInfo RoutineInfo$Builder]))

(declare from-edn to-edn)

(defn ^RoutineInfo from-edn
  [arg]
  (global/strict! :gcp.bigquery/RoutineInfo arg)
  (let [builder (RoutineInfo/newBuilder (RoutineId/from-edn (get arg
                                                                 :routineId)))]
    (when (seq (get arg :arguments))
      (.setArguments builder
                     (map RoutineArgument/from-edn (get arg :arguments))))
    (when (some? (get arg :body)) (.setBody builder (get arg :body)))
    (when (some? (get arg :dataGovernanceType))
      (.setDataGovernanceType builder (get arg :dataGovernanceType)))
    (when (some? (get arg :determinismLevel))
      (.setDeterminismLevel builder (get arg :determinismLevel)))
    (when (seq (get arg :importedLibraries))
      (.setImportedLibraries builder (seq (get arg :importedLibraries))))
    (when (some? (get arg :language))
      (.setLanguage builder (get arg :language)))
    (when (some? (get arg :remoteFunctionOptions))
      (.setRemoteFunctionOptions builder
                                 (RemoteFunctionOptions/from-edn
                                   (get arg :remoteFunctionOptions))))
    (when (some? (get arg :returnTableType))
      (.setReturnTableType builder
                           (StandardSQL/StandardSQLTableType-from-edn
                             (get arg :returnTableType))))
    (when (some? (get arg :returnType))
      (.setReturnType builder
                      (StandardSQL/StandardSQLDataType-from-edn
                        (get arg :returnType))))
    (when (some? (get arg :routineType))
      (.setRoutineType builder (get arg :routineType)))
    (.build builder)))

(defn to-edn
  [^RoutineInfo arg]
  {:post [(global/strict! :gcp.bigquery/RoutineInfo %)]}
  (when arg
    (cond-> {:routineId (RoutineId/to-edn (.getRoutineId arg))}
      (seq (.getArguments arg))
        (assoc :argumentList (map RoutineArgument/to-edn (.getArguments arg)))
      (some->> (.getBody arg)
               (not= ""))
        (assoc :body (.getBody arg))
      (.getCreationTime arg) (assoc :creationTime (.getCreationTime arg))
      (some->> (.getDataGovernanceType arg)
               (not= ""))
        (assoc :dataGovernanceType (.getDataGovernanceType arg))
      (some->> (.getDescription arg)
               (not= ""))
        (assoc :description (.getDescription arg))
      (some->> (.getDeterminismLevel arg)
               (not= ""))
        (assoc :determinismLevel (.getDeterminismLevel arg))
      (some->> (.getEtag arg)
               (not= ""))
        (assoc :etag (.getEtag arg))
      (seq (.getImportedLibraries arg)) (assoc :importedLibrariesList
                                          (seq (.getImportedLibraries arg)))
      (some->> (.getLanguage arg)
               (not= ""))
        (assoc :language (.getLanguage arg))
      (.getLastModifiedTime arg) (assoc :lastModifiedTime
                                   (.getLastModifiedTime arg))
      (.getRemoteFunctionOptions arg) (assoc :remoteFunctionOptions
                                        (RemoteFunctionOptions/to-edn
                                          (.getRemoteFunctionOptions arg)))
      (.getReturnTableType arg) (assoc :returnTableType
                                  (StandardSQL/StandardSQLTableType-to-edn
                                    (.getReturnTableType arg)))
      (.getReturnType arg) (assoc :returnType
                             (StandardSQL/StandardSQLDataType-to-edn
                               (.getReturnType arg)))
      (some->> (.getRoutineType arg)
               (not= ""))
        (assoc :routineType (.getRoutineType arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Google BigQuery routine information. A Routine is an API abstraction that encapsulates several\nrelated concepts inside the BigQuery service, including scalar user defined functions (UDFS) and\nstored procedures.\n\n<p>For more information about the REST representation of routines, see:\nhttps://cloud.google.com/bigquery/docs/reference/rest/v2/routines\n\n<p>For more information about working with scalar functions, see:\nhttps://cloud.google.com/bigquery/docs/reference/standard-sql/user-defined-functions",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/RoutineInfo}
   [:argumentList
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the list of arguments for the routine."}
    [:sequential {:min 1} :gcp.bigquery/RoutineArgument]]
   [:body
    {:optional true,
     :getter-doc "Returns the definition body of the routine.",
     :setter-doc
       "Required. The body of the routine.\n\n<p>For functions, this is the expression in the AS clause.\n\n<p>If language=SQL, it is the substring inside (but excluding) the parentheses. For example,\nfor the function created with the following statement:\n\n<p>CREATE FUNCTION JoinLines(x string, y string) as (concat(x, \"\\n\", y))\n\n<p>The definitionBody is concat(x, \"\\n\", y) (\\n is not replaced with linebreak).\n\n<p>If language=JAVASCRIPT, it is the evaluated string in the AS clause. For example, for the\nfunction created with the following statement:\n\n<p>CREATE FUNCTION f() RETURNS STRING LANGUAGE js AS 'return \"\\n\";\\n'\n\n<p>The definitionBody is\n\n<p>return \"\\n\";\\n\n\n<p>Note that both \\n are replaced with linebreaks."}
    [:string {:min 1}]]
   [:creationTime
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns the creation time of the routine, represented as milliseconds since the epoch."}
    :i64]
   [:dataGovernanceType
    {:optional true,
     :getter-doc
       "Returns the data governance type of the routine, e.g. DATA_MASKING.",
     :setter-doc
       "Sets the data governance type for the Builder (e.g. DATA_MASKING).\n\n<p>See https://cloud.google.com/bigquery/docs/reference/rest/v2/routines"}
    [:string {:min 1}]]
   [:description
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the description of the routine."} [:string {:min 1}]]
   [:determinismLevel
    {:optional true,
     :getter-doc
       "Returns the determinism level of the JavaScript UDF if defined.",
     :setter-doc
       "Sets the JavaScript UDF determinism levels (e.g. DETERMINISM_LEVEL_UNSPECIFIED,\nDETERMINISTIC, NOT_DETERMINISTIC) only applicable to Javascript UDFs."}
    [:string {:min 1}]]
   [:etag
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the hash of the routine resource."}
    [:string {:min 1}]]
   [:importedLibrariesList
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns the list of imported libraries for the routine. Only relevant for routines implemented\nusing the JAVASCRIPT language."}
    [:sequential {:min 1} [:string {:min 1}]]]
   [:language
    {:optional true,
     :getter-doc
       "Returns the language of the routine. Currently supported languages include SQL and JAVASCRIPT.",
     :setter-doc "Sets the language for the routine (e.g. SQL or JAVASCRIPT)"}
    [:string {:min 1}]]
   [:lastModifiedTime
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns the last modification time of the routine, represented as milliseconds since the epoch."}
    :i64]
   [:remoteFunctionOptions
    {:optional true,
     :getter-doc "Returns the Remote function specific options.",
     :setter-doc
       "Optional. Remote function specific options.\n\n@param remoteFunctionOptions\n@return"}
    :gcp.bigquery/RemoteFunctionOptions]
   [:returnTableType
    {:optional true,
     :getter-doc
       "If specified, returns the table type returned from the routine.",
     :setter-doc
       "Optional. Set only if Routine is a \"TABLE_VALUED_FUNCTION\"."}
    :gcp.bigquery/StandardSQLTableType]
   [:returnType
    {:optional true,
     :getter-doc
       "If specified, returns the data type returned from the routine.",
     :setter-doc
       "Sets the return type of the routine.\n\n<p>Optional if language = \"SQL\"; required otherwise.\n\n<p>If absent, the return type is inferred from definitionBody at query time in each query\nthat references this routine. If present, then the evaluated result will be cast to the\nspecified returned type at query time."}
    :gcp.bigquery/StandardSQLDataType]
   [:routineId
    {:getter-doc "Returns the RoutineId identified for the routine resource. *"}
    :gcp.bigquery/RoutineId]
   [:routineType
    {:optional true,
     :getter-doc "Returns the type of the routine, e.g. SCALAR_FUNCTION.",
     :setter-doc
       "Sets the routine type for the Builder (e.g. SCALAR_FUNCTION).\n\n<p>See https://cloud.google.com/bigquery/docs/reference/rest/v2/routines"}
    [:string {:min 1}]]])

(global/include-schema-registry! (with-meta {:gcp.bigquery/RoutineInfo schema}
                                   {:gcp.global/name
                                      "gcp.bigquery.RoutineInfo"}))