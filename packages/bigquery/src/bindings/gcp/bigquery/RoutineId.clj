;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.RoutineId
  {:doc "RoutineId represents the identifier for a given Routine."
   :file-git-sha "c3548a2f521b19761c844c0b24fc8caab541aba7"
   :fqcn "com.google.cloud.bigquery.RoutineId"
   :gcp.dev/certification
     {:base-seed 1776499402737
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1776499402737 :standard 1776499402738 :stress 1776499402739}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:03:24.691569830Z"}}
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
    [:string {:min 1}]]
   [:project
    {:doc "Return corresponding project ID for this routine. *", :optional true}
    [:string {:min 1}]]
   [:routine {:doc "Return corresponding routine ID for this routine. *"}
    [:string {:min 1}]]])

(global/include-schema-registry! (with-meta {:gcp.bigquery/RoutineId schema}
                                   {:gcp.global/name "gcp.bigquery.RoutineId"}))