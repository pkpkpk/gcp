(ns gcp.bigquery.v2.RoutineArgument
  (:require [gcp.bigquery.v2.StandardSQL :as StandardSQL]
            [gcp.global :as g])
  (:import (com.google.cloud.bigquery RoutineArgument)))

(defn ^RoutineArgument from-edn
  [{:keys [dataType kind mode name] :as arg}]
  (g/strict! :bigquery/RoutineArgument arg)
  (let [builder (RoutineArgument/newBuilder)]
    (.setDataType builder (StandardSQL/DataType-from-edn dataType))
    (when kind (.setKind builder kind))
    (when mode (.setMode builder mode))
    (when name (.setName builder name))
    (.build builder)))

(defn to-edn [^RoutineArgument arg]
  {:post [(g/strict! :bigquery/RoutineArgument %)]}
  (cond-> {:dataType (StandardSQL/DataType-to-edn (.getDataType arg))}
          (some? (.getName arg))
          (assoc :name (.getName arg))

          (some? (.getMode arg))
          (assoc :mode (.getMode arg))

          (some? (.getKind arg))
          (assoc :kind (.getKind arg))))
