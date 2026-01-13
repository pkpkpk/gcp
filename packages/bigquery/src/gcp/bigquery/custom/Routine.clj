(ns gcp.bigquery.custom.Routine
  (:require [gcp.bigquery.v2.BigQuery]
            [gcp.bigquery.v2.RoutineInfo :as RoutineInfo]
            [gcp.global :as global])
  (:import (com.google.cloud.bigquery Routine)))

(defn to-edn [^Routine arg]
  (when arg
    (assoc (RoutineInfo/to-edn arg) :bigquery (.getBigQuery arg))))

(def schemas
  {:gcp.bigquery/Routine
   [:and
    :gcp.bigquery/RoutineInfo
    [:map [:bigquery :gcp.bigquery/BigQuery]]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))
