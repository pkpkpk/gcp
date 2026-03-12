(ns gcp.bigquery.custom.Routine
  (:require [gcp.bindings.bigquery.RoutineInfo :as RoutineInfo])
  (:import (com.google.cloud.bigquery Routine)))

(defn to-edn [^Routine arg]
  (when arg
    (assoc (RoutineInfo/to-edn arg) :bigquery (.getBigQuery arg))))
