(ns gcp.bigquery.v2.RoutineId
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery RoutineId)))

(defn ^RoutineId from-edn
  "Constructs a RoutineId from a map conforming to the :gcp.bigquery.v2/RoutineId schema.
   The :dataset and :routine keys are required, while :project is optional."
  [{:keys [project dataset routine] :as m}]
  (global/strict! :gcp.bigquery.v2/RoutineId m)
  (if project
    (RoutineId/of project dataset routine)
    (RoutineId/of dataset routine)))

(defn to-edn
  "Converts a RoutineId to an EDN map with keys :project, :dataset, :routine.
   The :project key is only set if it is non-nil."
  [^RoutineId routine-id]
  {:post [(global/strict! :gcp.bigquery.v2/RoutineId %)]}
  (cond-> {:dataset (.getDataset routine-id)
           :routine (.getRoutine routine-id)}
          (some? (.getProject routine-id))
          (assoc :project (.getProject routine-id))))

(def schemas
  {:gcp.bigquery.v2/RoutineId
   [:map
    [:project {:optional true} :string]
    [:dataset :string]
    [:routine :string]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))
