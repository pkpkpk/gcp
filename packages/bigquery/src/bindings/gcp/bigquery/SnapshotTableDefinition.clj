;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.SnapshotTableDefinition
  {:doc nil
   :file-git-sha "56d69efeaa90c9fa71e0c2c95412a1ad0c95dc90"
   :fqcn "com.google.cloud.bigquery.SnapshotTableDefinition"
   :gcp.dev/certification
     {:base-seed 1790034086215
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034086215 :standard 1790034086216 :stress 1790034086217}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:41:27.376848675Z"}}
  (:require [gcp.bigquery.Clustering :as Clustering]
            [gcp.bigquery.RangePartitioning :as RangePartitioning]
            [gcp.bigquery.Schema :as Schema]
            [gcp.bigquery.TableId :as TableId]
            [gcp.bigquery.TimePartitioning :as TimePartitioning]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery SnapshotTableDefinition
            SnapshotTableDefinition$Builder]))

(declare from-edn to-edn)

(defn ^SnapshotTableDefinition from-edn
  [arg]
  (global/strict! :gcp.bigquery/SnapshotTableDefinition arg)
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
  {:post [(global/strict! :gcp.bigquery/SnapshotTableDefinition %)]}
  (when arg
    (cond-> {:type "SNAPSHOT"}
      (.getBaseTableId arg) (assoc :baseTableId
                              (TableId/to-edn (.getBaseTableId arg)))
      (.getClustering arg) (assoc :clustering
                             (Clustering/to-edn (.getClustering arg)))
      (.getRangePartitioning arg) (assoc :rangePartitioning
                                    (RangePartitioning/to-edn
                                      (.getRangePartitioning arg)))
      (.getSchema arg) (assoc :schema (Schema/to-edn (.getSchema arg)))
      (some->> (.getSnapshotTime arg)
               (not= ""))
        (assoc :snapshotTime (.getSnapshotTime arg))
      (.getTimePartitioning arg) (assoc :timePartitioning
                                   (TimePartitioning/to-edn
                                     (.getTimePartitioning arg))))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :variant-accessor,
    :gcp/key :gcp.bigquery/SnapshotTableDefinition} [:type [:= "SNAPSHOT"]]
   [:baseTableId
    {:optional true,
     :setter-doc
       "Reference describing the ID of the table that was snapshot. *"}
    :gcp.bigquery/TableId]
   [:clustering {:optional true} :gcp.bigquery/Clustering]
   [:rangePartitioning {:optional true} :gcp.bigquery/RangePartitioning]
   [:schema
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the table's schema."} :gcp.bigquery/Schema]
   [:snapshotTime
    {:optional true,
     :setter-doc
       "The time at which the base table was snapshot. This value is reported in the JSON response\nusing RFC3339 format. *"}
    [:string {:min 1, :gen/max 1}]]
   [:timePartitioning {:optional true} :gcp.bigquery/TimePartitioning]])

(global/include-registry! "gcp.bigquery.SnapshotTableDefinition"
                          {:gcp.bigquery/SnapshotTableDefinition schema})