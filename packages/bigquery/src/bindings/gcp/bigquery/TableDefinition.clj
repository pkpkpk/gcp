;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.TableDefinition
  {:doc "Base class for a Google BigQuery table definition."
   :file-git-sha "56d69efeaa90c9fa71e0c2c95412a1ad0c95dc90"
   :fqcn "com.google.cloud.bigquery.TableDefinition"
   :gcp.dev/certification
     {:base-seed 1790034101380
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034101380 :standard 1790034101381 :stress 1790034101382}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:41:43.073957982Z"}}
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

(global/include-registry! "gcp.bigquery.TableDefinition"
                          {:gcp.bigquery/TableDefinition schema})