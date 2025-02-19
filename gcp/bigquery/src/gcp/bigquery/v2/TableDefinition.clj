(ns gcp.bigquery.v2.TableDefinition
  (:import [com.google.cloud.bigquery TableDefinition])
  (:require
    [gcp.bigquery.v2.ExternalTableDefinition :as ExternalTableDefinition]
    [gcp.bigquery.v2.MaterializedViewDefinition :as MaterializedViewDefinition]
    ;[gcp.bigquery.v2.ModelTableDefinition :as ModelTableDefinition]
    ;[gcp.bigquery.v2.SnapshotTableDefinition :as SnapshotTableDefinition]
    [gcp.bigquery.v2.StandardTableDefinition :as StandardTableDefinition]
    [gcp.bigquery.v2.ViewDefinition :as ViewDefinition]
    gcp.global))

(defn to-edn
  [^TableDefinition arg]
  {:post [(gcp.global/strict! :gcp/bigquery.TableDefinition %)]}
  (case (.name (.getType arg))
    ;"SNAPSHOT" (assoc (SnapshotTableDefinition/to-edn arg) :type "SNAPSHOT")
    "VIEW" (assoc (ViewDefinition/to-edn arg) :type "VIEW")
    "MATERIALIZED_VIEW" (assoc (MaterializedViewDefinition/to-edn arg)
                          :type "MATERIALIZED_VIEW")
    "TABLE" (assoc (StandardTableDefinition/to-edn arg) :type "TABLE")
    "EXTERNAL" (assoc (ExternalTableDefinition/to-edn arg) :type "EXTERNAL")
    ;"MODEL" (assoc (ModelTableDefinition/to-edn arg) :type "MODEL")
    ))

(defn ^TableDefinition from-edn
  [arg]
  (gcp.global/strict! :gcp/bigquery.TableDefinition arg)
  (or
    (and (or (= "EXTERNAL" (get arg :type))
             (gcp.global/valid? :gcp/bigquery.ExternalTableDefinition arg))
         (ExternalTableDefinition/from-edn arg))
    (and (or (= "MATERIALIZED_VIEW" (get arg :type))
             (gcp.global/valid? :gcp/bigquery.MaterializedViewDefinition arg))
         (MaterializedViewDefinition/from-edn arg))
    ;(and (or (= "MODEL" (get arg :type))
    ;         (gcp.global/valid? :gcp/bigquery.ModelTableDefinition arg))
    ;     (ModelTableDefinition/from-edn arg))
    ;(and (or (= "SNAPSHOT" (get arg :type))
    ;         (gcp.global/valid? :gcp/bigquery.SnapshotTableDefinition arg))
    ;     (SnapshotTableDefinition/from-edn arg))
    (and (or (= "TABLE" (get arg :type))
             (gcp.global/valid? :gcp/bigquery.StandardTableDefinition arg))
         (StandardTableDefinition/from-edn arg))
    (and (or (= "VIEW" (get arg :type))
             (gcp.global/valid? :gcp/bigquery.ViewDefinition arg))
         (ViewDefinition/from-edn arg))
    (throw
      (clojure.core/ex-info
        "failed to match variant for union com.google.cloud.bigquery.TableDefinition"
        {:arg arg,
         :expected #{"SNAPSHOT" "VIEW" "MATERIALIZED_VIEW" "TABLE" "EXTERNAL"
                     "MODEL"}}))))