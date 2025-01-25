(ns gcp.bigquery.v2.RoutineInfo
  (:require [gcp.global :as g]
            [gcp.bigquery.v2.RemoteFunctionOptions :as RemoteFunctionOptions]
            [gcp.bigquery.v2.RoutineArgument       :as RoutineArgument]
            [gcp.bigquery.v2.RoutineId             :as RoutineId]
            [gcp.bigquery.v2.StandardSQL           :as StandardSQL])
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
  (g/strict! :bigquery/RoutineInfo routine-map)
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
    (when returnTableType (.setReturnTableType builder (StandardSQL/TableType-from-edn returnTableType)))
    (.build builder)))

(defn to-edn [^RoutineInfo routine]
  {:post [(g/strict! :bigquery/RoutineInfo %)]}
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
          (assoc :returnType (StandardSQL/DataType-to-edn (.getReturnType routine)))

          (some? (.getReturnTableType routine))
          (assoc :returnTableType (StandardSQL/TableType-to-edn (.getReturnTableType routine)))

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
