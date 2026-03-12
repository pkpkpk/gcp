;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.SnapshotTableDefinition
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.SnapshotTableDefinition"
   :gcp.dev/certification
     {:base-seed 1771341178400
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771341178400 :standard 1771341178401 :stress 1771341178402}
      :protocol-hash
        "32175171656ac95c72455bff25fe019346fb52c916efee805be149692e31cec4"
      :timestamp "2026-02-17T15:13:11.428901550Z"}}
  (:require [gcp.bindings.bigquery.Clustering :as Clustering]
            [gcp.bindings.bigquery.RangePartitioning :as RangePartitioning]
            [gcp.bindings.bigquery.Schema :as Schema]
            [gcp.bindings.bigquery.TableId :as TableId]
            [gcp.bindings.bigquery.TimePartitioning :as TimePartitioning]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery SnapshotTableDefinition
            SnapshotTableDefinition$Builder]))

(defn ^SnapshotTableDefinition from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/SnapshotTableDefinition arg)
  (let [builder (SnapshotTableDefinition/newBuilder)]
    (when (some? (get arg :baseTableId))
      (.setBaseTableId builder (TableId/from-edn (get arg :baseTableId))))
    (when (some? (get arg :clustering))
      (.setClustering builder (Clustering/from-edn (get arg :clustering))))
    (when (some? (get arg :rangePartitioning))
      (.setRangePartitioning builder
                             (RangePartitioning/from-edn
                               (get arg :rangePartitioning))))
    (when (some? (get arg :snapshotTime))
      (.setSnapshotTime builder (get arg :snapshotTime)))
    (when (some? (get arg :timePartitioning))
      (.setTimePartitioning builder
                            (TimePartitioning/from-edn
                              (get arg :timePartitioning))))
    (.build builder)))

(defn to-edn
  [^SnapshotTableDefinition arg]
  {:post [(global/strict! :gcp.bindings.bigquery/SnapshotTableDefinition %)]}
  (cond-> {:type "SNAPSHOT"}
    (.getBaseTableId arg) (assoc :baseTableId
                            (TableId/to-edn (.getBaseTableId arg)))
    (.getClustering arg) (assoc :clustering
                           (Clustering/to-edn (.getClustering arg)))
    (.getRangePartitioning arg) (assoc :rangePartitioning
                                  (RangePartitioning/to-edn
                                    (.getRangePartitioning arg)))
    (.getSchema arg) (assoc :schema (Schema/to-edn (.getSchema arg)))
    (.getSnapshotTime arg) (assoc :snapshotTime (.getSnapshotTime arg))
    (.getTimePartitioning arg) (assoc :timePartitioning
                                 (TimePartitioning/to-edn (.getTimePartitioning
                                                            arg)))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :variant-accessor,
    :gcp/key :gcp.bindings.bigquery/SnapshotTableDefinition}
   [:type [:= "SNAPSHOT"]]
   [:baseTableId
    {:optional true,
     :setter-doc
       "Reference describing the ID of the table that was snapshot. *"}
    :gcp.bindings.bigquery/TableId]
   [:timePartitioning {:optional true} :gcp.bindings.bigquery/TimePartitioning]
   [:snapshotTime
    {:optional true,
     :setter-doc
       "The time at which the base table was snapshot. This value is reported in the JSON response\nusing RFC3339 format. *"}
    [:string {:min 1}]]
   [:clustering {:optional true} :gcp.bindings.bigquery/Clustering]
   [:rangePartitioning {:optional true}
    :gcp.bindings.bigquery/RangePartitioning]
   [:schema
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the table's schema."} :gcp.bindings.bigquery/Schema]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/SnapshotTableDefinition schema}
    {:gcp.global/name "gcp.bindings.bigquery.SnapshotTableDefinition"}))