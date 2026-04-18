;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.StandardTableDefinition
  {:doc
     "A Google BigQuery default table definition. This definition is used for standard, two-dimensional\ntables with individual records organized in rows, and a data type assigned to each column (also\ncalled a field). Individual fields within a record may contain nested and repeated children\nfields. Every table is described by a schema that describes field names, types, and other\ninformation.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/tables\">Managing Tables</a>"
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.StandardTableDefinition"
   :gcp.dev/certification
     {:base-seed 1776499396205
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1776499396205 :standard 1776499396206 :stress 1776499396207}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:03:17.851110944Z"}}
  (:require [gcp.bigquery.BigLakeConfiguration :as BigLakeConfiguration]
            [gcp.bigquery.Clustering :as Clustering]
            [gcp.bigquery.RangePartitioning :as RangePartitioning]
            [gcp.bigquery.Schema :as Schema]
            [gcp.bigquery.TableConstraints :as TableConstraints]
            [gcp.bigquery.TimePartitioning :as TimePartitioning]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery StandardTableDefinition
            StandardTableDefinition$Builder
            StandardTableDefinition$StreamingBuffer]))

(declare from-edn to-edn StreamingBuffer-from-edn StreamingBuffer-to-edn)

(defn ^StandardTableDefinition$StreamingBuffer StreamingBuffer-from-edn
  [arg]
  (throw
    (Exception.
      "Class com.google.cloud.bigquery.StandardTableDefinition.StreamingBuffer is read-only")))

(defn StreamingBuffer-to-edn
  [^StandardTableDefinition$StreamingBuffer arg]
  (when arg
    (cond-> {}
      (.getEstimatedBytes arg) (assoc :estimatedBytes (.getEstimatedBytes arg))
      (.getEstimatedRows arg) (assoc :estimatedRows (.getEstimatedRows arg))
      (.getOldestEntryTime arg) (assoc :oldestEntryTime
                                  (.getOldestEntryTime arg)))))

(def StreamingBuffer-schema
  [:map
   {:closed true,
    :doc
      "Google BigQuery Table's Streaming Buffer information. This class contains information on a\ntable's streaming buffer as the estimated size in number of rows/bytes.",
    :gcp/category :nested/read-only,
    :gcp/key :gcp.bigquery/StandardTableDefinition.StreamingBuffer}
   [:estimatedBytes
    {:read-only? true,
     :optional true,
     :doc
       "Returns a lower-bound estimate of the number of bytes currently in the streaming buffer."}
    :i64]
   [:estimatedRows
    {:read-only? true,
     :optional true,
     :doc
       "Returns a lower-bound estimate of the number of rows currently in the streaming buffer."}
    :i64]
   [:oldestEntryTime
    {:read-only? true,
     :optional true,
     :doc
       "Returns the timestamp of the oldest entry in the streaming buffer, in milliseconds since\nepoch. Returns {@code null} if the streaming buffer is empty."}
    :i64]])

(defn ^StandardTableDefinition from-edn
  [arg]
  (global/strict! :gcp.bigquery/StandardTableDefinition arg)
  (let [builder (StandardTableDefinition/newBuilder)]
    (when (some? (get arg :bigLakeConfiguration))
      (.setBigLakeConfiguration builder
                                (BigLakeConfiguration/from-edn
                                  (get arg :bigLakeConfiguration))))
    (when (some? (get arg :clustering))
      (.setClustering builder (Clustering/from-edn (get arg :clustering))))
    (when (some? (get arg :location))
      (.setLocation builder (get arg :location)))
    (when (some? (get arg :numActiveLogicalBytes))
      (.setNumActiveLogicalBytes builder
                                 (long (get arg :numActiveLogicalBytes))))
    (when (some? (get arg :numActivePhysicalBytes))
      (.setNumActivePhysicalBytes builder
                                  (long (get arg :numActivePhysicalBytes))))
    (when (some? (get arg :numBytes))
      (.setNumBytes builder (long (get arg :numBytes))))
    (when (some? (get arg :numLongTermBytes))
      (.setNumLongTermBytes builder (long (get arg :numLongTermBytes))))
    (when (some? (get arg :numLongTermLogicalBytes))
      (.setNumLongTermLogicalBytes builder
                                   (long (get arg :numLongTermLogicalBytes))))
    (when (some? (get arg :numLongTermPhysicalBytes))
      (.setNumLongTermPhysicalBytes builder
                                    (long (get arg :numLongTermPhysicalBytes))))
    (when (some? (get arg :numRows))
      (.setNumRows builder (long (get arg :numRows))))
    (when (some? (get arg :numTimeTravelPhysicalBytes))
      (.setNumTimeTravelPhysicalBytes builder
                                      (long (get arg
                                                 :numTimeTravelPhysicalBytes))))
    (when (some? (get arg :numTotalLogicalBytes))
      (.setNumTotalLogicalBytes builder (long (get arg :numTotalLogicalBytes))))
    (when (some? (get arg :numTotalPhysicalBytes))
      (.setNumTotalPhysicalBytes builder
                                 (long (get arg :numTotalPhysicalBytes))))
    (when (some? (get arg :rangePartitioning))
      (.setRangePartitioning builder
                             (RangePartitioning/from-edn
                               (get arg :rangePartitioning))))
    (when (some? (get arg :schema))
      (.setSchema builder (Schema/from-edn (get arg :schema))))
    (when (some? (get arg :streamingBuffer))
      (.setStreamingBuffer builder
                           (StreamingBuffer-from-edn (get arg
                                                          :streamingBuffer))))
    (when (some? (get arg :tableConstraints))
      (.setTableConstraints builder
                            (TableConstraints/from-edn
                              (get arg :tableConstraints))))
    (when (some? (get arg :timePartitioning))
      (.setTimePartitioning builder
                            (TimePartitioning/from-edn
                              (get arg :timePartitioning))))
    (.build builder)))

(defn to-edn
  [^StandardTableDefinition arg]
  {:post [(global/strict! :gcp.bigquery/StandardTableDefinition %)]}
  (when arg
    (cond-> {:type "TABLE"}
      (.getBigLakeConfiguration arg) (assoc :bigLakeConfiguration
                                       (BigLakeConfiguration/to-edn
                                         (.getBigLakeConfiguration arg)))
      (.getClustering arg) (assoc :clustering
                             (Clustering/to-edn (.getClustering arg)))
      (some->> (.getLocation arg)
               (not= ""))
        (assoc :location (.getLocation arg))
      (.getNumActiveLogicalBytes arg) (assoc :numActiveLogicalBytes
                                        (.getNumActiveLogicalBytes arg))
      (.getNumActivePhysicalBytes arg) (assoc :numActivePhysicalBytes
                                         (.getNumActivePhysicalBytes arg))
      (.getNumBytes arg) (assoc :numBytes (.getNumBytes arg))
      (.getNumLongTermBytes arg) (assoc :numLongTermBytes
                                   (.getNumLongTermBytes arg))
      (.getNumLongTermLogicalBytes arg) (assoc :numLongTermLogicalBytes
                                          (.getNumLongTermLogicalBytes arg))
      (.getNumLongTermPhysicalBytes arg) (assoc :numLongTermPhysicalBytes
                                           (.getNumLongTermPhysicalBytes arg))
      (.getNumRows arg) (assoc :numRows (.getNumRows arg))
      (.getNumTimeTravelPhysicalBytes arg)
        (assoc :numTimeTravelPhysicalBytes (.getNumTimeTravelPhysicalBytes arg))
      (.getNumTotalLogicalBytes arg) (assoc :numTotalLogicalBytes
                                       (.getNumTotalLogicalBytes arg))
      (.getNumTotalPhysicalBytes arg) (assoc :numTotalPhysicalBytes
                                        (.getNumTotalPhysicalBytes arg))
      (.getRangePartitioning arg) (assoc :rangePartitioning
                                    (RangePartitioning/to-edn
                                      (.getRangePartitioning arg)))
      (.getSchema arg) (assoc :schema (Schema/to-edn (.getSchema arg)))
      (.getStreamingBuffer arg) (assoc :streamingBuffer
                                  (StreamingBuffer-to-edn (.getStreamingBuffer
                                                            arg)))
      (.getTableConstraints arg) (assoc :tableConstraints
                                   (TableConstraints/to-edn
                                     (.getTableConstraints arg)))
      (.getTimePartitioning arg) (assoc :timePartitioning
                                   (TimePartitioning/to-edn
                                     (.getTimePartitioning arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "A Google BigQuery default table definition. This definition is used for standard, two-dimensional\ntables with individual records organized in rows, and a data type assigned to each column (also\ncalled a field). Individual fields within a record may contain nested and repeated children\nfields. Every table is described by a schema that describes field names, types, and other\ninformation.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/tables\">Managing Tables</a>",
    :gcp/category :variant-accessor,
    :gcp/key :gcp.bigquery/StandardTableDefinition} [:type [:= "TABLE"]]
   [:bigLakeConfiguration
    {:optional true,
     :getter-doc
       "[Optional] Specifies the configuration of a BigLake managed table. The value may be {@code\nnull}.",
     :setter-doc
       "Set the configuration of a BigLake managed table. If not set, the table is not a BigLake\nmanaged table."}
    :gcp.bigquery/BigLakeConfiguration]
   [:clustering
    {:optional true,
     :getter-doc
       "Returns the clustering configuration for this table. If {@code null}, the table is not\nclustered.",
     :setter-doc
       "Set the clustering configuration for the table. If not set, the table is not clustered.\nBigQuery supports clustering for both partitioned and non-partitioned tables."}
    :gcp.bigquery/Clustering]
   [:location
    {:optional true,
     :getter-doc
       "Returns the geographic location where the table should reside. This value is inherited from the\ndataset.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/managing_jobs_datasets_projects#dataset-location\">\n    Dataset Location</a>"}
    [:string {:min 1}]]
   [:numActiveLogicalBytes
    {:optional true,
     :getter-doc
       "Returns the number of active logical bytes.\n\n@see <a href=\"https://cloud.google.com/bigquery/pricing#storage\">Storage Pricing</a>"}
    :i64]
   [:numActivePhysicalBytes
    {:optional true,
     :getter-doc
       "Returns the number of active physical bytes.\n\n@see <a href=\"https://cloud.google.com/bigquery/pricing#storage\">Storage Pricing</a>"}
    :i64]
   [:numBytes
    {:optional true,
     :getter-doc
       "Returns the size of this table in bytes, excluding any data in the streaming buffer."}
    :i64]
   [:numLongTermBytes
    {:optional true,
     :getter-doc
       "Returns the number of bytes considered \"long-term storage\" for reduced billing purposes.\n\n@see <a href=\"https://cloud.google.com/bigquery/pricing#long-term-storage\">Long Term Storage\n    Pricing</a>"}
    :i64]
   [:numLongTermLogicalBytes
    {:optional true,
     :getter-doc
       "Returns the number of long term logical bytes.\n\n@see <a href=\"https://cloud.google.com/bigquery/pricing#storage\">Storage Pricing</a>"}
    :i64]
   [:numLongTermPhysicalBytes
    {:optional true,
     :getter-doc
       "Returns the number of long term physical bytes.\n\n@see <a href=\"https://cloud.google.com/bigquery/pricing#storage\">Storage Pricing</a>"}
    :i64]
   [:numRows
    {:optional true,
     :getter-doc
       "Returns the number of rows in this table, excluding any data in the streaming buffer."}
    :i64]
   [:numTimeTravelPhysicalBytes
    {:optional true,
     :getter-doc
       "Returns the number of time travel physical bytes.\n\n@see <a href=\"https://cloud.google.com/bigquery/pricing#storage\">Storage Pricing</a>"}
    :i64]
   [:numTotalLogicalBytes
    {:optional true,
     :getter-doc
       "Returns the number of total logical bytes.\n\n@see <a href=\"https://cloud.google.com/bigquery/pricing#storage\">Storage Pricing</a>"}
    :i64]
   [:numTotalPhysicalBytes
    {:optional true,
     :getter-doc
       "Returns the number of total physical bytes.\n\n@see <a href=\"https://cloud.google.com/bigquery/pricing#storage\">Storage Pricing</a>"}
    :i64]
   [:rangePartitioning
    {:optional true,
     :getter-doc
       "Returns the range partitioning configuration for this table. If {@code null}, the table is not\nrange-partitioned.",
     :setter-doc
       "Sets the range partitioning configuration for the table. Only one of timePartitioning and\nrangePartitioning should be specified."}
    :gcp.bigquery/RangePartitioning]
   [:schema
    {:optional true,
     :getter-doc "Returns the table's schema.",
     :setter-doc "Sets the table schema."} :gcp.bigquery/Schema]
   [:streamingBuffer
    {:optional true,
     :getter-doc
       "Returns information on the table's streaming buffer if any exists. Returns {@code null} if no\nstreaming buffer exists."}
    [:ref :gcp.bigquery/StandardTableDefinition.StreamingBuffer]]
   [:tableConstraints
    {:optional true,
     :getter-doc
       "Returns the table constraints for this table. Returns {@code null} if no table constraints are\nset for this table."}
    :gcp.bigquery/TableConstraints]
   [:timePartitioning
    {:optional true,
     :getter-doc
       "Returns the time partitioning configuration for this table. If {@code null}, the table is not\ntime-partitioned.",
     :setter-doc
       "Sets the time partitioning configuration for the table. If not set, the table is not\ntime-partitioned."}
    :gcp.bigquery/TimePartitioning]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/StandardTableDefinition schema,
              :gcp.bigquery/StandardTableDefinition.StreamingBuffer
                StreamingBuffer-schema}
    {:gcp.global/name "gcp.bigquery.StandardTableDefinition"}))