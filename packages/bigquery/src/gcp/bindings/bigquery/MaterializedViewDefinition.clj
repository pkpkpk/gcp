;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.MaterializedViewDefinition
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.MaterializedViewDefinition"
   :gcp.dev/certification
     {:base-seed 1771346518147
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771346518147 :standard 1771346518148 :stress 1771346518149}
      :protocol-hash
        "32175171656ac95c72455bff25fe019346fb52c916efee805be149692e31cec4"
      :timestamp "2026-02-17T16:42:10.773364781Z"}}
  (:require [gcp.bindings.bigquery.Clustering :as Clustering]
            [gcp.bindings.bigquery.RangePartitioning :as RangePartitioning]
            [gcp.bindings.bigquery.Schema :as Schema]
            [gcp.bindings.bigquery.TimePartitioning :as TimePartitioning]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery MaterializedViewDefinition
            MaterializedViewDefinition$Builder]))

(defn ^MaterializedViewDefinition from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/MaterializedViewDefinition arg)
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
      (.setRefreshIntervalMs builder (get arg :refreshIntervalMs)))
    (when (some? (get arg :schema))
      (.setSchema builder (Schema/from-edn (get arg :schema))))
    (when (some? (get arg :timePartitioning))
      (.setTimePartitioning builder
                            (TimePartitioning/from-edn
                              (get arg :timePartitioning))))
    (.build builder)))

(defn to-edn
  [^MaterializedViewDefinition arg]
  {:post [(global/strict! :gcp.bindings.bigquery/MaterializedViewDefinition %)]}
  (cond-> {:query (.getQuery arg), :type "MATERIALIZED_VIEW"}
    (.getClustering arg) (assoc :clustering
                           (Clustering/to-edn (.getClustering arg)))
    (.getEnableRefresh arg) (assoc :enableRefresh (.getEnableRefresh arg))
    (.getLastRefreshTime arg) (assoc :lastRefreshTime (.getLastRefreshTime arg))
    (.getRangePartitioning arg) (assoc :rangePartitioning
                                  (RangePartitioning/to-edn
                                    (.getRangePartitioning arg)))
    (.getRefreshIntervalMs arg) (assoc :refreshIntervalMs
                                  (.getRefreshIntervalMs arg))
    (.getSchema arg) (assoc :schema (Schema/to-edn (.getSchema arg)))
    (.getTimePartitioning arg) (assoc :timePartitioning
                                 (TimePartitioning/to-edn (.getTimePartitioning
                                                            arg)))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :variant-accessor,
    :gcp/key :gcp.bindings.bigquery/MaterializedViewDefinition}
   [:type [:= "MATERIALIZED_VIEW"]]
   [:query {:getter-doc "Returns a query whose result is persisted."}
    [:string {:min 1}]]
   [:schema
    {:optional true,
     :getter-doc "Returns the table's schema.",
     :setter-doc "Sets the table schema."} :gcp.bindings.bigquery/Schema]
   [:timePartitioning
    {:optional true,
     :getter-doc
       "Returns the time partitioning configuration for this table. If {@code null}, the table is not\ntime-partitioned.",
     :setter-doc
       "Sets the time partitioning configuration for the materialized view. If not set, the\nmaterialized view is not time-partitioned."}
    :gcp.bindings.bigquery/TimePartitioning]
   [:enableRefresh
    {:optional true,
     :getter-doc
       "Returns enable automatic refresh of the materialized view when the base table is updated. The\ndefault value is \"true\".",
     :setter-doc
       "Set enable automatic refresh of the materialized view when the base table is updated. The\ndefault value is \"true\"."}
    :boolean]
   [:clustering
    {:optional true,
     :getter-doc
       "Returns the clustering configuration for this table. If {@code null}, the table is not\nclustered.",
     :setter-doc
       "Set the clustering configuration for the materialized view. If not set, the materialized view\nis not clustered. BigQuery supports clustering for both partitioned and non-partitioned\nmaterialized views."}
    :gcp.bindings.bigquery/Clustering]
   [:rangePartitioning
    {:optional true,
     :getter-doc
       "Returns the range partitioning configuration for this table. If {@code null}, the table is not\nrange-partitioned.",
     :setter-doc
       "Sets the range partitioning configuration for the materialized view. Only one of\ntimePartitioning and rangePartitioning should be specified."}
    :gcp.bindings.bigquery/RangePartitioning]
   [:refreshIntervalMs
    {:optional true,
     :getter-doc
       "Returns a maximum frequency at which this materialized view will be refreshed. The default\nvalue is \"1800000\" (30 minutes).",
     :setter-doc
       "Set a maximum frequency at which this materialized view will be refreshed. The default value\nis \"1800000\" (30 minutes)."}
    :int]
   [:lastRefreshTime
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns time when this materialized view was last modified, in milliseconds since the epoch."}
    :int]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/MaterializedViewDefinition schema}
    {:gcp.global/name "gcp.bindings.bigquery.MaterializedViewDefinition"}))