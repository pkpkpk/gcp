(ns gcp.bigquery.v2.Routine
  (:require [gcp.bigquery.v2.BigQuery]
            [gcp.bigquery.v2.RoutineInfo :as RoutineInfo]
            [gcp.global :as global])
  (:import (com.google.cloud.bigquery Routine)))

(defn to-edn [^Routine arg]
  (when arg
    (assoc (RoutineInfo/to-edn arg) :bigquery (.getBigQuery arg))))

(def schemas
  {:gcp.bigquery.v2/Routine
   [:and
    :gcp.bigquery.v2/RoutineInfo
    [:map [:bigquery :gcp.bigquery.v2/BigQuery]]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))
