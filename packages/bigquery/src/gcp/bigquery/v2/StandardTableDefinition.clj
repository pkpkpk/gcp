(ns gcp.bigquery.v2.StandardTableDefinition
  (:require [gcp.bigquery.v2.BigLakeConfiguration :as BigLakeConfiguration]
            [gcp.bigquery.v2.Clustering :as Clustering]
            [gcp.bigquery.v2.RangePartitioning :as RangePartitioning]
            [gcp.bigquery.v2.Schema :as Schema]
            [gcp.bigquery.v2.TableConstraints :as TableConstraints]
            [gcp.bigquery.v2.TimePartitioning :as TimePartitioning]
            [gcp.global :as global])
  (:import (com.google.cloud.bigquery StandardTableDefinition StandardTableDefinition$StreamingBuffer)))

(defn ^StandardTableDefinition from-edn
  "Constructs a StandardTableDefinition from the given EDN map.
   Many fields are typically read-only (e.g., numBytes),
   so only settable fields are included here.
   For any unimplemented field, an exception is thrown.
   Adjust to match your codebase's needs."
  [{:keys [bigLakeConfiguration
           clustering
           location
           numActiveLogicalBytes
           numActivePhysicalBytes
           numBytes
           numLongTermBytes
           numLongTermLogicalBytes
           numLongTermPhysicalBytes
           numRows
           numTimeTravelPhysicalBytes
           numTotalLogicalBytes
           numTotalPhysicalBytes
           rangePartitioning
           schema
           streamingBuffer
           tableConstraints
           timePartitioning
           type] :as arg}]
  (global/strict! :gcp.bigquery.v2/StandardTableDefinition arg)
  (let [builder (StandardTableDefinition/newBuilder)]
    (when bigLakeConfiguration
      (.setBigLakeConfiguration builder (BigLakeConfiguration/from-edn bigLakeConfiguration)))
    (when clustering
      (.setClustering builder (Clustering/from-edn clustering)))
    (when location
      (.setLocation builder location))
    (when numActiveLogicalBytes
      (.setNumActiveLogicalBytes builder (long numActiveLogicalBytes)))
    (when numActivePhysicalBytes
      (.setNumActivePhysicalBytes builder (long numActivePhysicalBytes)))
    (when numBytes
      (.setNumBytes builder (long numBytes)))
    (when numLongTermBytes
      (.setNumLongTermBytes builder (long numLongTermBytes)))
    (when numLongTermLogicalBytes
      (.setNumLongTermLogicalBytes builder (long numLongTermLogicalBytes)))
    (when numLongTermPhysicalBytes
      (.setNumLongTermPhysicalBytes builder (long numLongTermPhysicalBytes)))
    (when numRows
      (.setNumRows builder (long numRows)))
    (when numTimeTravelPhysicalBytes
      (.setNumTimeTravelPhysicalBytes builder (long numTimeTravelPhysicalBytes)))
    (when numTotalLogicalBytes
      (.setNumTotalLogicalBytes builder (long numTotalLogicalBytes)))
    (when numTotalPhysicalBytes
      (.setNumTotalPhysicalBytes builder (long numTotalPhysicalBytes)))
    (when rangePartitioning
      (.setRangePartitioning builder (RangePartitioning/from-edn rangePartitioning)))
    (when schema
      (.setSchema builder (Schema/from-edn schema)))
    (when streamingBuffer
      (throw (Exception. "unimplemented")))
    (when tableConstraints
      (.setTableConstraints builder (TableConstraints/from-edn tableConstraints)))
    (when timePartitioning
      (.setTimePartitioning builder (TimePartitioning/from-edn timePartitioning)))
    (.build builder)))

(defn StreamingBuffer:to-edn
  [^StandardTableDefinition$StreamingBuffer arg]
  {:estimatedBytes (.getEstimatedBytes arg)
   :estimatedRows (.getEstimatedRows arg)})

(defn to-edn [^StandardTableDefinition arg]
  {:post [(global/strict! :gcp.bigquery.v2/StandardTableDefinition %)]}
  (cond-> {}
          (some? (.getBigLakeConfiguration arg))
          (assoc :bigLakeConfiguration (BigLakeConfiguration/to-edn (.getBigLakeConfiguration arg)))

          (some? (.getClustering arg))
          (assoc :clustering (Clustering/to-edn (.getClustering arg)))

          (some? (.getLocation arg))
          (assoc :location (.getLocation arg))

          (some? (.getNumActiveLogicalBytes arg))
          (assoc :numActiveLogicalBytes (.getNumActiveLogicalBytes arg))

          (some? (.getNumActivePhysicalBytes arg))
          (assoc :numActivePhysicalBytes (.getNumActivePhysicalBytes arg))

          (some? (.getNumBytes arg))
          (assoc :numBytes (.getNumBytes arg))

          (some? (.getNumLongTermBytes arg))
          (assoc :numLongTermBytes (.getNumLongTermBytes arg))

          (some? (.getNumLongTermLogicalBytes arg))
          (assoc :numLongTermLogicalBytes (.getNumLongTermLogicalBytes arg))

          (some? (.getNumLongTermPhysicalBytes arg))
          (assoc :numLongTermPhysicalBytes (.getNumLongTermPhysicalBytes arg))

          (some? (.getNumRows arg))
          (assoc :numRows (.getNumRows arg))

          (some? (.getNumTimeTravelPhysicalBytes arg))
          (assoc :numTimeTravelPhysicalBytes (.getNumTimeTravelPhysicalBytes arg))

          (some? (.getNumTotalLogicalBytes arg))
          (assoc :numTotalLogicalBytes (.getNumTotalLogicalBytes arg))

          (some? (.getNumTotalPhysicalBytes arg))
          (assoc :numTotalPhysicalBytes (.getNumTotalPhysicalBytes arg))

          (some? (.getRangePartitioning arg))
          (assoc :rangePartitioning (RangePartitioning/to-edn (.getRangePartitioning arg)))

          (some? (.getSchema arg))
          (assoc :schema (Schema/to-edn (.getSchema arg)))

          (some? (.getStreamingBuffer arg))
          (assoc :streamingBuffer (StreamingBuffer:to-edn (.getStreamingBuffer arg)))

          (some? (.getTableConstraints arg))
          (assoc :tableConstraints (TableConstraints/to-edn (.getTableConstraints arg)))

          (some? (.getTimePartitioning arg))
          (assoc :timePartitioning (TimePartitioning/to-edn arg))

          (some? (.getType arg))
          (assoc :type (.name (.getType arg)))))

(def schemas
  {:gcp.bigquery.v2/StandardTableDefinition
   [:map {:closed true}
    [:type [:= "TABLE"]]
    [:bigLakeConfiguration {:optional true} :gcp.bigquery.v2/BigLakeConfiguration]
    [:clustering {:optional true} :gcp.bigquery.v2/Clustering]
    [:location {:optional true} :string]
    [:numActiveLogicalBytes {:optional true} :int]
    [:numActivePhysicalBytes {:optional true} :int]
    [:numBytes {:optional true} :int]
    [:numLongTermBytes {:optional true} :int]
    [:numLongTermLogicalBytes {:optional true} :int]
    [:numLongTermPhysicalBytes {:optional true} :int]
    [:numRows {:optional true} :int]
    [:numTimeTravelPhysicalBytes {:optional true} :int]
    [:numTotalLogicalBytes {:optional true} :int]
    [:numTotalPhysicalBytes {:optional true} :int]
    [:rangePartitioning {:optional true} :gcp.bigquery.v2/RangePartitioning]
    [:schema {:optional true} :gcp.bigquery.v2/Schema]
    [:streamingBuffer {:optional true} :any]
    [:tableConstraints {:optional true} :gcp.bigquery.v2/TableConstraints]
    [:timePartitioning {:optional true} :gcp.bigquery.v2/TimePartitioning]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))
