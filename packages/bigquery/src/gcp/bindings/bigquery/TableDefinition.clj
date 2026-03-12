;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.TableDefinition
  {:doc "Base class for a Google BigQuery table definition."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.TableDefinition"
   :gcp.dev/certification
     {:base-seed 1771694543429
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771694543429 :standard 1771694543430 :stress 1771694543431}
      :protocol-hash
        "b772aa63021cdca286f80d356c1662202367b300ee8558c915570cd863593527"
      :timestamp "2026-02-21T17:22:28.895460010Z"}}
  (:require
    [gcp.bindings.bigquery.ExternalTableDefinition :as ExternalTableDefinition]
    [gcp.bindings.bigquery.MaterializedViewDefinition :as
     MaterializedViewDefinition]
    [gcp.bindings.bigquery.ModelTableDefinition :as ModelTableDefinition]
    [gcp.bindings.bigquery.SnapshotTableDefinition :as SnapshotTableDefinition]
    [gcp.bindings.bigquery.StandardTableDefinition :as StandardTableDefinition]
    [gcp.bindings.bigquery.ViewDefinition :as ViewDefinition]
    [gcp.global :as global])
  (:import [com.google.cloud.bigquery TableDefinition]))

(defn ^TableDefinition from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/TableDefinition arg)
  (case (get arg :type)
    "SNAPSHOT" (SnapshotTableDefinition/from-edn arg)
    "EXTERNAL" (ExternalTableDefinition/from-edn arg)
    "MATERIALIZED_VIEW" (MaterializedViewDefinition/from-edn arg)
    "TABLE" (StandardTableDefinition/from-edn arg)
    "VIEW" (ViewDefinition/from-edn arg)
    "MODEL" (ModelTableDefinition/from-edn arg)))

(defn to-edn
  [^TableDefinition arg]
  {:post [(global/strict! :gcp.bindings.bigquery/TableDefinition %)]}
  (case (.name (.getType arg))
    "SNAPSHOT" (SnapshotTableDefinition/to-edn arg)
    "EXTERNAL" (ExternalTableDefinition/to-edn arg)
    "MATERIALIZED_VIEW" (MaterializedViewDefinition/to-edn arg)
    "TABLE" (StandardTableDefinition/to-edn arg)
    "VIEW" (ViewDefinition/to-edn arg)
    "MODEL" (ModelTableDefinition/to-edn arg)))

(def schema
  [:or
   {:closed true,
    :doc "Base class for a Google BigQuery table definition.",
    :gcp/category :union-abstract,
    :gcp/key :gcp.bindings.bigquery/TableDefinition}
   :gcp.bindings.bigquery/SnapshotTableDefinition
   :gcp.bindings.bigquery/ExternalTableDefinition
   :gcp.bindings.bigquery/MaterializedViewDefinition
   :gcp.bindings.bigquery/StandardTableDefinition
   :gcp.bindings.bigquery/ViewDefinition
   :gcp.bindings.bigquery/ModelTableDefinition])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/TableDefinition schema}
    {:gcp.global/name "gcp.bindings.bigquery.TableDefinition"}))