;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.MaterializedViewDefinition
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.MaterializedViewDefinition"
   :gcp.dev/certification
     {:base-seed 1775130893668
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1775130893668 :standard 1775130893669 :stress 1775130893670}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:54:54.921883280Z"}}
  (:require [gcp.bigquery.Clustering :as Clustering]
            [gcp.bigquery.RangePartitioning :as RangePartitioning]
            [gcp.bigquery.Schema :as Schema]
            [gcp.bigquery.TimePartitioning :as TimePartitioning]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery MaterializedViewDefinition
            MaterializedViewDefinition$Builder]))

(declare from-edn to-edn)

(defn ^MaterializedViewDefinition from-edn
  [arg]
  (global/strict! :gcp.bigquery/MaterializedViewDefinition arg)
  (let [builder (MaterializedViewDefinition/newBuilder (get arg :query))]
    (when (some? (get arg :clustering))
      (.setClustering builder (Clustering/from-edn (get arg :clustering))))
    (when (some? (get arg :enableRefresh))
      (.setEnableRefresh builder (get arg :enableRefresh)))
    (when (some? (get arg :rangePartitioning))
      (.setRangePartitioning builder
                             (RangePartitioning/from-edn
                               (get arg :rangePartitioning))))
    (when (some? (get arg :refreshIntervalMs))
      (.setRefreshIntervalMs builder (long (get arg :refreshIntervalMs))))
    (when (some? (get arg :schema))
      (.setSchema builder (Schema/from-edn (get arg :schema))))
    (when (some? (get arg :timePartitioning))
      (.setTimePartitioning builder
                            (TimePartitioning/from-edn
                              (get arg :timePartitioning))))
    (.build builder)))

(defn to-edn
  [^MaterializedViewDefinition arg]
  {:post [(global/strict! :gcp.bigquery/MaterializedViewDefinition %)]}
  (when arg
    (cond-> {:query (.getQuery arg), :type "MATERIALIZED_VIEW"}
      (.getClustering arg) (assoc :clustering
                             (Clustering/to-edn (.getClustering arg)))
      (.getEnableRefresh arg) (assoc :enableRefresh (.getEnableRefresh arg))
      (.getLastRefreshTime arg) (assoc :lastRefreshTime
                                  (.getLastRefreshTime arg))
      (.getRangePartitioning arg) (assoc :rangePartitioning
                                    (RangePartitioning/to-edn
                                      (.getRangePartitioning arg)))
      (.getRefreshIntervalMs arg) (assoc :refreshIntervalMs
                                    (.getRefreshIntervalMs arg))
      (.getSchema arg) (assoc :schema (Schema/to-edn (.getSchema arg)))
      (.getTimePartitioning arg) (assoc :timePartitioning
                                   (TimePartitioning/to-edn
                                     (.getTimePartitioning arg))))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :variant-accessor,
    :gcp/key :gcp.bigquery/MaterializedViewDefinition}
   [:type [:= "MATERIALIZED_VIEW"]]
   [:clustering
    {:optional true,
     :getter-doc
       "Returns the clustering configuration for this table. If {@code null}, the table is not\nclustered.",
     :setter-doc
       "Set the clustering configuration for the materialized view. If not set, the materialized view\nis not clustered. BigQuery supports clustering for both partitioned and non-partitioned\nmaterialized views."}
    :gcp.bigquery/Clustering]
   [:enableRefresh
    {:optional true,
     :getter-doc
       "Returns enable automatic refresh of the materialized view when the base table is updated. The\ndefault value is \"true\".",
     :setter-doc
       "Set enable automatic refresh of the materialized view when the base table is updated. The\ndefault value is \"true\"."}
    :boolean]
   [:lastRefreshTime
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns time when this materialized view was last modified, in milliseconds since the epoch."}
    :i64]
   [:query {:getter-doc "Returns a query whose result is persisted."}
    [:string {:min 1}]]
   [:rangePartitioning
    {:optional true,
     :getter-doc
       "Returns the range partitioning configuration for this table. If {@code null}, the table is not\nrange-partitioned.",
     :setter-doc
       "Sets the range partitioning configuration for the materialized view. Only one of\ntimePartitioning and rangePartitioning should be specified."}
    :gcp.bigquery/RangePartitioning]
   [:refreshIntervalMs
    {:optional true,
     :getter-doc
       "Returns a maximum frequency at which this materialized view will be refreshed. The default\nvalue is \"1800000\" (30 minutes).",
     :setter-doc
       "Set a maximum frequency at which this materialized view will be refreshed. The default value\nis \"1800000\" (30 minutes)."}
    :i64]
   [:schema
    {:optional true,
     :getter-doc "Returns the table's schema.",
     :setter-doc "Sets the table schema."} :gcp.bigquery/Schema]
   [:timePartitioning
    {:optional true,
     :getter-doc
       "Returns the time partitioning configuration for this table. If {@code null}, the table is not\ntime-partitioned.",
     :setter-doc
       "Sets the time partitioning configuration for the materialized view. If not set, the\nmaterialized view is not time-partitioned."}
    :gcp.bigquery/TimePartitioning]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/MaterializedViewDefinition schema}
    {:gcp.global/name "gcp.bigquery.MaterializedViewDefinition"}))