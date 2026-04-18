;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.TableDefinition
  {:doc "Base class for a Google BigQuery table definition."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.TableDefinition"
   :gcp.dev/certification
     {:base-seed 1776499400241
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1776499400241 :standard 1776499400242 :stress 1776499400243}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:03:22.358844199Z"}}
  (:require [gcp.bigquery.ExternalTableDefinition :as ExternalTableDefinition]
            [gcp.bigquery.MaterializedViewDefinition :as
             MaterializedViewDefinition]
            [gcp.bigquery.ModelTableDefinition :as ModelTableDefinition]
            [gcp.bigquery.SnapshotTableDefinition :as SnapshotTableDefinition]
            [gcp.bigquery.StandardTableDefinition :as StandardTableDefinition]
            [gcp.bigquery.ViewDefinition :as ViewDefinition]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery TableDefinition]))

(declare from-edn to-edn)

(defn ^TableDefinition from-edn
  [arg]
  (global/strict! :gcp.bigquery/TableDefinition arg)
  (case (get arg :type)
    "SNAPSHOT" (SnapshotTableDefinition/from-edn arg)
    "EXTERNAL" (ExternalTableDefinition/from-edn arg)
    "MATERIALIZED_VIEW" (MaterializedViewDefinition/from-edn arg)
    "TABLE" (StandardTableDefinition/from-edn arg)
    "VIEW" (ViewDefinition/from-edn arg)
    "MODEL" (ModelTableDefinition/from-edn arg)))

(defn to-edn
  [^TableDefinition arg]
  {:post [(global/strict! :gcp.bigquery/TableDefinition %)]}
  (when arg
    (case (.name (.getType arg))
      "SNAPSHOT" (SnapshotTableDefinition/to-edn arg)
      "EXTERNAL" (ExternalTableDefinition/to-edn arg)
      "MATERIALIZED_VIEW" (MaterializedViewDefinition/to-edn arg)
      "TABLE" (StandardTableDefinition/to-edn arg)
      "VIEW" (ViewDefinition/to-edn arg)
      "MODEL" (ModelTableDefinition/to-edn arg))))

(def schema
  [:or
   {:closed true,
    :doc "Base class for a Google BigQuery table definition.",
    :gcp/category :union-abstract,
    :gcp/key :gcp.bigquery/TableDefinition}
   :gcp.bigquery/SnapshotTableDefinition :gcp.bigquery/ExternalTableDefinition
   :gcp.bigquery/MaterializedViewDefinition
   :gcp.bigquery/StandardTableDefinition :gcp.bigquery/ViewDefinition
   :gcp.bigquery/ModelTableDefinition])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/TableDefinition schema}
    {:gcp.global/name "gcp.bigquery.TableDefinition"}))