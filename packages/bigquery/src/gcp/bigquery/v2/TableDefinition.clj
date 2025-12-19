(ns gcp.bigquery.v2.TableDefinition
  (:import [com.google.cloud.bigquery TableDefinition])
  (:require
    [gcp.bigquery.v2.ExternalTableDefinition :as ExternalTableDefinition]
    [gcp.bigquery.v2.MaterializedViewDefinition :as MaterializedViewDefinition]
    ;[gcp.bigquery.v2.ModelTableDefinition :as ModelTableDefinition]
    ;[gcp.bigquery.v2.SnapshotTableDefinition :as SnapshotTableDefinition]
    [gcp.bigquery.v2.StandardTableDefinition :as StandardTableDefinition]
    [gcp.bigquery.v2.ViewDefinition :as ViewDefinition]
    [gcp.global :as global]))

(defn to-edn
  [^TableDefinition arg]
  {:post [(global/strict! :gcp.bigquery.v2/TableDefinition %)]}
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
  (global/strict! :gcp.bigquery.v2/TableDefinition arg)
  (or
    (and (or (= "EXTERNAL" (get arg :type))
             (global/valid? :gcp.bigquery.v2/ExternalTableDefinition arg))
         (ExternalTableDefinition/from-edn arg))
    (and (or (= "MATERIALIZED_VIEW" (get arg :type))
             (global/valid? :gcp.bigquery.v2/MaterializedViewDefinition arg))
         (MaterializedViewDefinition/from-edn arg))
    ;(and (or (= "MODEL" (get arg :type))
    ;         (global/valid? :gcp.bigquery.v2/ModelTableDefinition arg))
    ;     (ModelTableDefinition/from-edn arg))
    ;(and (or (= "SNAPSHOT" (get arg :type))
    ;         (global/valid? :gcp.bigquery.v2/SnapshotTableDefinition arg))
    ;     (SnapshotTableDefinition/from-edn arg))
    (and (or (= "TABLE" (get arg :type))
             (global/valid? :gcp.bigquery.v2/StandardTableDefinition arg))
         (StandardTableDefinition/from-edn arg))
    (and (or (= "VIEW" (get arg :type))
             (global/valid? :gcp.bigquery.v2/ViewDefinition arg))
         (ViewDefinition/from-edn arg))
    (throw
      (clojure.core/ex-info
        "failed to match variant for union com.google.cloud.bigquery.TableDefinition"
        {:arg arg,
         :expected #{"SNAPSHOT" "VIEW" "MATERIALIZED_VIEW" "TABLE" "EXTERNAL"
                     "MODEL"}}))))

(def schemas
  {:gcp.bigquery.v2/TableDefinition
   [:or
    {:gcp/type :abstract-union
     :class 'com.google.cloud.bigquery.TableDefinition}
    :gcp.bigquery.v2/ExternalTableDefinition
    :gcp.bigquery.v2/MaterializedViewDefinition
    :gcp.bigquery.v2/StandardTableDefinition
    :gcp.bigquery.v2/ViewDefinition]

   :gcp.bigquery.v2/TableDefinition.Type [:or
                                       [:= "VIEW"]
                                       [:= "SNAPSHOT"]
                                       [:= "MATERIALIZED_VIEW"]
                                       [:= "EXTERNAL"]
                                       [:= "MODEL"]
                                       [:= "TABLE"]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))