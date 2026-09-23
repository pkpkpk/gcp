;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.RoutineId
  {:doc "RoutineId represents the identifier for a given Routine."
   :file-git-sha "5410f6bbd78031621045db8ac9f56dc8ab6bb837"
   :fqcn "com.google.cloud.bigquery.RoutineId"
   :gcp.dev/certification
     {:base-seed 1790034103417
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034103417 :standard 1790034103418 :stress 1790034103419}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:41:44.451128104Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery RoutineId]))

(declare from-edn to-edn)

(defn ^RoutineId from-edn
  [arg]
  (global/strict! :gcp.bigquery/RoutineId arg)
  (if (get arg :project)
    (RoutineId/of (get arg :project) (get arg :dataset) (get arg :routine))
    (RoutineId/of (get arg :dataset) (get arg :routine))))

(defn to-edn
  [^RoutineId arg]
  {:post [(global/strict! :gcp.bigquery/RoutineId %)]}
  (when arg
    (cond-> {:dataset (.getDataset arg), :routine (.getRoutine arg)}
      (.getProject arg) (assoc :project (.getProject arg)))))

(def schema
  [:map
   {:closed true,
    :doc "RoutineId represents the identifier for a given Routine.",
    :gcp/category :static-factory,
    :gcp/key :gcp.bigquery/RoutineId}
   [:dataset {:doc "Return corresponding dataset ID for this routine. *"}
    [:string {:min 1, :gen/max 1}]]
   [:project
    {:doc "Return corresponding project ID for this routine. *", :optional true}
    [:string {:min 1, :gen/max 1}]]
   [:routine {:doc "Return corresponding routine ID for this routine. *"}
    [:string {:min 1, :gen/max 1}]]])

(global/include-registry! "gcp.bigquery.RoutineId"
                          {:gcp.bigquery/RoutineId schema})