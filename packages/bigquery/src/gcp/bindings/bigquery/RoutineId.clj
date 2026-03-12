;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.RoutineId
  {:doc "RoutineId represents the identifier for a given Routine."
   :file-git-sha "c3548a2f521b19761c844c0b24fc8caab541aba7"
   :fqcn "com.google.cloud.bigquery.RoutineId"
   :gcp.dev/certification
     {:base-seed 1770905451408
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1770905451408 :standard 1770905451409 :stress 1770905451410}
      :protocol-hash
        "12be2957e350878c667952b725a3aa0cead5b4ccecfda54c534822e9bb380b94"
      :timestamp "2026-02-12T14:10:51.434382514Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery RoutineId]))

(defn ^RoutineId from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/RoutineId arg)
  (if (get arg :project)
    (RoutineId/of (get arg :project) (get arg :dataset) (get arg :routine))
    (RoutineId/of (get arg :dataset) (get arg :routine))))

(defn to-edn
  [^RoutineId arg]
  {:post [(global/strict! :gcp.bindings.bigquery/RoutineId %)]}
  (cond-> {:dataset (.getDataset arg), :routine (.getRoutine arg)}
    (.getProject arg) (assoc :project (.getProject arg))))

(def schema
  [:map
   {:closed true,
    :doc "RoutineId represents the identifier for a given Routine.",
    :gcp/category :static-factory,
    :gcp/key :gcp.bindings.bigquery/RoutineId}
   [:dataset {:doc "Return corresponding dataset ID for this routine. *"}
    :string]
   [:project
    {:doc "Return corresponding project ID for this routine. *", :optional true}
    :string]
   [:routine {:doc "Return corresponding routine ID for this routine. *"}
    :string]])

(global/include-schema-registry! (with-meta {:gcp.bindings.bigquery/RoutineId
                                               schema}
                                   {:gcp.global/name (str *ns*)}))