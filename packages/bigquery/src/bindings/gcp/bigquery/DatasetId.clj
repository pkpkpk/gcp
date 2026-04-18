;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.DatasetId
  {:doc "Google BigQuery Dataset identity."
   :file-git-sha "c3548a2f521b19761c844c0b24fc8caab541aba7"
   :fqcn "com.google.cloud.bigquery.DatasetId"
   :gcp.dev/certification
     {:base-seed 1776499322821
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1776499322821 :standard 1776499322822 :stress 1776499322823}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:02:04.173151381Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery DatasetId]))

(declare from-edn to-edn)

(defn ^DatasetId from-edn
  [arg]
  (global/strict! :gcp.bigquery/DatasetId arg)
  (if (string? arg)
    (DatasetId/of arg)
    (if (get arg :project)
      (DatasetId/of (get arg :project) (get arg :dataset))
      (DatasetId/of (get arg :dataset)))))

(defn to-edn
  [^DatasetId arg]
  {:post [(global/strict! :gcp.bigquery/DatasetId %)]}
  (when arg
    (cond-> {:dataset (.getDataset arg)}
      (.getProject arg) (assoc :project (.getProject arg)))))

(def schema
  [:or
   {:closed true,
    :doc "Google BigQuery Dataset identity.",
    :gcp/category :static-factory,
    :gcp/key :gcp.bigquery/DatasetId} [:string {:min 1}]
   [:map {:closed true}
    [:dataset {:doc "Returns dataset's user-defined id."} [:string {:min 1}]]
    [:project {:doc "Returns project's user-defined id.", :optional true}
     [:string {:min 1}]]]])

(global/include-schema-registry! (with-meta {:gcp.bigquery/DatasetId schema}
                                   {:gcp.global/name "gcp.bigquery.DatasetId"}))