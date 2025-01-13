(ns gcp.bigquery.v2.CopyJobConfiguration
  (:require [gcp.global :as global]
            [gcp.bigquery.v2.TableId :as TableId])
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
  (global/strict! :bigquery/CopyJobConfiguration arg)
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

(defn to-edn [^CopyJobConfiguration arg] (throw (Exception. "unimplemented")))
