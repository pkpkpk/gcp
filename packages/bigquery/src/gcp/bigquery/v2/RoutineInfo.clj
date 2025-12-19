(ns gcp.bigquery.v2.RoutineInfo
  (:require [gcp.bigquery.v2.RemoteFunctionOptions :as RemoteFunctionOptions]
            [gcp.bigquery.v2.RoutineArgument       :as RoutineArgument]
            [gcp.bigquery.v2.RoutineId             :as RoutineId]
            [gcp.bigquery.v2.StandardSQL           :as StandardSQL]
            [gcp.global :as global])
  (:import (com.google.cloud.bigquery RoutineInfo)))

(defn ^RoutineInfo from-edn
  [{:keys [routineId
           arguments
           body
           determinismLevel
           importedLibraries
           language
           returnType
           returnTableType
           routineType
           remoteFunctionOptions
           dataGovernanceType] :as routine-map}]
  (global/strict! :gcp.bigquery.v2/RoutineInfo routine-map)
  (let [builder (RoutineInfo/newBuilder (RoutineId/from-edn routineId))]
    (when arguments (.setArguments builder (map RoutineArgument/from-edn arguments)))
    (.setBody builder body)
    (.setLanguage builder language)
    (some->> routineType (.setRoutineType builder))
    (when dataGovernanceType (.setDataGovernanceType builder dataGovernanceType))
    (when determinismLevel (.setDeterminismLevel builder determinismLevel))
    (when importedLibraries (.setImportedLibraries builder (seq importedLibraries)))
    (when remoteFunctionOptions (.setRemoteFunctionOptions builder (RemoteFunctionOptions/from-edn remoteFunctionOptions)))
    (when returnType (.setReturnType builder returnType))
    (when returnTableType (.setReturnTableType builder (StandardSQL/TableType:from-edn returnTableType)))
    (.build builder)))

(defn to-edn [^RoutineInfo routine]
  {:post [(global/strict! :gcp.bigquery.v2/RoutineInfo %)]}
  (cond-> {:routineId (RoutineId/to-edn (.getRoutineId routine))}
          (seq (.getArguments routine))
          (assoc :arguments (mapv RoutineArgument/to-edn (.getArguments routine)))

          (some? (.getBody routine))
          (assoc :body (.getBody routine))

          (some? (.getDeterminismLevel routine))
          (assoc :determinismLevel (.getDeterminismLevel routine))

          (seq (.getImportedLibraries routine))
          (assoc :importedLibraries (vec (.getImportedLibraries routine)))

          (some? (.getLanguage routine))
          (assoc :language (.getLanguage routine))

          (some? (.getReturnType routine))
          (assoc :returnType (StandardSQL/DataType:to-edn (.getReturnType routine)))

          (some? (.getReturnTableType routine))
          (assoc :returnTableType (StandardSQL/TableType:to-edn (.getReturnTableType routine)))

          (some? (.getRoutineType routine))
          (assoc :routineType (.getRoutineType routine))

          (some? (.getRemoteFunctionOptions routine))
          (assoc :remoteFunctionOptions (RemoteFunctionOptions/to-edn (.getRemoteFunctionOptions routine)))

          (some? (.getDataGovernanceType routine))
          (assoc :dataGovernanceType (.getDataGovernanceType routine))

          ;; READ-ONLY fields below
          (some? (.getEtag routine))
          (assoc :etag (.getEtag routine))

          (some? (.getCreationTime routine))
          (assoc :creationTime (.getCreationTime routine))

          (some? (.getLastModifiedTime routine))
          (assoc :lastModifiedTime (.getLastModifiedTime routine))))

(def schemas
  {:gcp.bigquery.v2/RoutineInfo
   [:map {:closed true}
    [:routineId :gcp.bigquery.v2/RoutineId]
    [:arguments {:doc "Specifies the list of input/output arguments for the routine." :optional true} [:sequential :gcp.bigquery.v2/RoutineArgument]]
    [:body {:doc      "The body of the routine."
            :optional true} :string]
    [:dataGovernanceType {:doc "Data governance type of the routine (e.g. DATA_MASKING)" :optional true} [:enum "DATA_GOVERNANCE_TYPE_UNSPECIFIED" "DATA_MASKING"]]
    [:determinismLevel {:doc "Determinism level for JavaScript UDFs" :optional true} [:enum "DETERMINISM_LEVEL_UNSPECIFIED" "DETERMINISTIC" "NOT_DETERMINISTIC"]]
    [:importedLibrariesList {:doc "language = \"JAVASCRIPT\", list of gs:// URLs for JavaScript libraries" :optional true} [:sequential :string]]
    [:language {:optional true} [:enum "JAVASCRIPT" "SQL"]]
    [:remoteFunctionOptions {:doc "Options for a remote function" :optional true} :gcp.bigquery.v2/RemoteFunctionOptions]
    [:returnTableType {:doc      "Table type returned by the routine (StandardSQLTableType)"
                       :optional true}
     :gcp.bigquery.v2/StandardSQLTableType]
    [:routineType {:doc      "Type of the routine (e.g. SCALAR_FUNCTION)"
                   :optional true}
     [:enum "ROUTINE_TYPE_UNSPECIFIED" "SCALAR_FUNCTION" "PROCEDURE" "TABLE_VALUED_FUNCTION"]]
    [:returnType {:doc      "Data type returned by the routine (StandardSQLDataType)"
                  :optional true}
     :gcp.bigquery.v2/StandardSQLDataType]
    #!--read-only---
    [:description {:optional true :read-only? true} :string]
    [:etag {:doc "Hash of the routine resource" :optional true :read-only? true} :string]
    [:creationTime {:doc "Time (ms since epoch) the routine was created" :optional true :read-only? true} :int]
    [:lastModifiedTime {:doc "Time (ms since epoch) the routine was last modified" :optional true :read-only? true} :int]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))
