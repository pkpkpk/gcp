(ns gcp.bigquery.v2.MaterializedViewDefinition
  (:require [gcp.bigquery.v2.Clustering :as Clustering]
            [gcp.bigquery.v2.RangePartitioning :as RangePartitioning]
            [gcp.bigquery.v2.Schema :as Schema]
            [gcp.bigquery.v2.TimePartitioning :as TimePartitioning])
  (:import (com.google.cloud.bigquery MaterializedViewDefinition)))

(defn ^MaterializedViewDefinition from-edn
  [{:keys [query
           clustering
           enableRefresh
           rangePartitioning
           refreshInterval
           schema
           timePartitioning] :as arg}]
  (let [builder (MaterializedViewDefinition/newBuilder query)]
    (when clustering (.setClustering builder (Clustering/from-edn clustering)))
    (some->> enableRefresh (.setEnableRefresh builder))
    (when rangePartitioning (.setRangePartitioning builder (RangePartitioning/from-edn rangePartitioning)))
    (some->> refreshInterval (.setRefreshIntervalMs builder))
    (when schema (.setSchema builder (Schema/from-edn schema)))
    (when timePartitioning (.setTimePartitioning builder (TimePartitioning/from-edn timePartitioning)))
    (.build builder)))

(defn to-edn [^MaterializedViewDefinition arg]
  (cond-> {:type  (.name (.getType arg))
           :query (.getQuery arg)}
          (some? (.getClustering arg))
          (assoc :clustering (Clustering/to-edn (.getClustering arg)))

          (some? (.getEnableRefresh arg))
          (assoc :enableRefresh (.getEnableRefresh arg))

          (some? (.getRangePartitioning arg))
          (assoc :rangePartitioning (RangePartitioning/to-edn (.getRangePartitioning arg)))

          (pos? (.getRefreshIntervalMs arg))
          (assoc :refreshInterval (.getRefreshIntervalMs arg))

          (some? (.getSchema arg))
          (assoc :schema (Schema/to-edn (.getSchema arg)))

          (some? (.getTimePartitioning arg))
          (assoc :timePartitioning (TimePartitioning/to-edn (.getTimePartitioning arg)))))
