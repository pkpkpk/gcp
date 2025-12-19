(ns gcp.bigquery.v2.RoutineArgument
  (:require [gcp.bigquery.v2.StandardSQL :as StandardSQL]
            [gcp.global :as global])
  (:import (com.google.cloud.bigquery RoutineArgument)))

(defn ^RoutineArgument from-edn
  [{:keys [dataType kind mode name] :as arg}]
  (global/strict! :gcp.bigquery.v2/RoutineArgument arg)
  (let [builder (RoutineArgument/newBuilder)]
    (.setDataType builder (StandardSQL/DataType:from-edn dataType))
    (when kind (.setKind builder kind))
    (when mode (.setMode builder mode))
    (when name (.setName builder name))
    (.build builder)))

(defn to-edn [^RoutineArgument arg]
  {:post [(global/strict! :gcp.bigquery.v2/RoutineArgument %)]}
  (cond-> {:dataType (StandardSQL/DataType:to-edn (.getDataType arg))}
          (some? (.getName arg))
          (assoc :name (.getName arg))

          (some? (.getMode arg))
          (assoc :mode (.getMode arg))

          (some? (.getKind arg))
          (assoc :kind (.getKind arg))))

(def schemas
  {:gcp.bigquery.v2/RoutineArgument
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
    [:dataType :gcp.bigquery.v2/StandardSQLDataType]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))
