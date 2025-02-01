(ns gcp.bigquery.v2.TableDefinition
  (:require  [gcp.bigquery.v2.ExternalTableDefinition :as ExternalTableDefinition]
             [gcp.bigquery.v2.MaterializedViewDefinition :as MaterializedViewDefinition]
             [gcp.bigquery.v2.StandardTableDefinition :as StandardTableDefinition]
             [gcp.bigquery.v2.ViewDefinition :as ViewDefinition]
             [gcp.bigquery.v2.Schema :as Schema]
             [gcp.global :as global])
  (:import (com.google.cloud.bigquery TableDefinition)))

(defn ^TableDefinition from-edn [{t :type :as arg}]
  (global/strict! :gcp/bigquery.TableDefinition arg)
  (case t
    "EXTERNAL" (ExternalTableDefinition/from-edn arg)
    "MATERIALIZED_VIEW" (MaterializedViewDefinition/from-edn arg)
    "MODEL" (throw (Exception. "unimplemented"))
    "SNAPSHOT" (throw (Exception. "unimplemented"))
    "TABLE" (StandardTableDefinition/from-edn arg)
    "VIEW" (ViewDefinition/from-edn arg)
    (throw (ex-info (str "unimplemented from-edn for TableDefinition type '" t "'") {:arg arg}))))

(defn to-edn [^TableDefinition arg]
  {:post [(global/strict! :gcp/bigquery.TableDefinition %)]}
  (let [t (.name (.getType arg))
        base (cond-> {:type t}
                     (some? (.getSchema arg))
                     (assoc :schema (Schema/to-edn (.getSchema arg))))]
    (case t
      "EXTERNAL" (merge base (ExternalTableDefinition/to-edn arg))
      "MATERIALIZED_VIEW" (merge base (MaterializedViewDefinition/to-edn arg))
      "MODEL" (throw (Exception. "unimplemented"))
      "SNAPSHOT" (throw (Exception. "unimplemented"))
      "TABLE" (merge base (StandardTableDefinition/to-edn arg))
      "VIEW" (merge base (ViewDefinition/to-edn arg))
      (throw (ex-info (str "unimplemented to-edn for TableDefinition type '" t "'")
                      {:type t :base base :arg arg})))))