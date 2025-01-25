(ns gcp.bigquery.v2.Routine
  (:require [gcp.bigquery.v2.RoutineInfo :as RoutineInfo])
  (:import (com.google.cloud.bigquery Routine)))

(defn to-edn [^Routine arg]
  (when arg
    (assoc (RoutineInfo/to-edn arg) :bigquery (.getBigQuery arg))))
