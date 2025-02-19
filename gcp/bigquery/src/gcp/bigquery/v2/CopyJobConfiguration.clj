(ns gcp.bigquery.v2.CopyJobConfiguration
  (:require [gcp.bigquery.v2.EncryptionConfiguration :as EncryptionConfiguration]
            [gcp.bigquery.v2.TableId :as TableId]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery CopyJobConfiguration JobInfo$CreateDisposition JobInfo$WriteDisposition]
           (java.util List)))

(defn from-edn
  [{:keys [destinationTable
           sourceTables
           createDisposition
           encryptionConfiguration
           destinationExpiration
           jobTimeoutMs
           labels
           operationType
           writeDisposition] :as arg}]
  (global/strict! :gcp/bigquery.CopyJobConfiguration arg)
  (let [dst (TableId/from-edn destinationTable)
        src (map TableId/from-edn sourceTables)
        builder (CopyJobConfiguration/newBuilder dst ^List src)]
    (when createDisposition
      (.setCreateDisposition builder (JobInfo$CreateDisposition/valueOf createDisposition)))
    (when writeDisposition
      (.setWriteDisposition builder (JobInfo$WriteDisposition/valueOf writeDisposition)))
    (when encryptionConfiguration
      (.setDestinationEncryptionConfiguration builder encryptionConfiguration))
    (when destinationExpiration
      (.setDestinationExpirationTime builder destinationExpiration))
    (when jobTimeoutMs
      (.setJobTimeoutMs builder jobTimeoutMs))
    (when labels
      (.setLabels builder labels))
    (when operationType
      (.setOperationType builder operationType))
    (.build builder)))

(defn to-edn [^CopyJobConfiguration arg]
  {:post [(global/strict! :gcp/bigquery.CopyJobConfiguration %)]}
  (cond-> {:type "COPY"
           :destinationTable (TableId/to-edn (.getDestinationTable arg))
           :sourceTables    (mapv TableId/to-edn (.getSourceTables arg))}
          (some? (.getCreateDisposition arg))
          (assoc :createDisposition (.name (.getCreateDisposition arg)))

          (some? (.getWriteDisposition arg))
          (assoc :writeDisposition (.name (.getWriteDisposition arg)))

          (some? (.getDestinationEncryptionConfiguration arg))
          (assoc :encryptionConfiguration (EncryptionConfiguration/to-edn (.getDestinationEncryptionConfiguration arg)))

          (some? (.getDestinationExpirationTime arg))
          (assoc :destinationExpiration (.getDestinationExpirationTime arg))

          (some? (.getJobTimeoutMs arg))
          (assoc :jobTimeoutMs (.getJobTimeoutMs arg))

          (seq (.getLabels arg))
          (assoc :labels (into {} (.getLabels arg)))

          (some? (.getOperationType arg))
          (assoc :operationType (.getOperationType arg))))

