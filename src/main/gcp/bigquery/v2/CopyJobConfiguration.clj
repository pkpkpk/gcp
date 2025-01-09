(ns gcp.bigquery.v2.CopyJobConfiguration
  (:require [gcp.global :as global]
            [gcp.bigquery.v2.TableId :as TableId])
  (:import [com.google.cloud.bigquery CopyJobConfiguration]))

(defn from-edn
  [{:keys [destinationTable
           sourceTable
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
        src (if (seq sourceTables)
              (map TableId/from-edn sourceTables)
              (TableId/from-edn sourceTable))
        builder (CopyJobConfiguration/newBuilder dst src)]
    (when createDisposition
      (throw (Exception. "unimplemented")))
    (when encryptionConfiguration
      (throw (Exception. "unimplemented")))
    (when destinationExpiration
      (throw (Exception. "unimplemented")))
    (when jobTimeoutMs
      (throw (Exception. "unimplemented")))
    (when labels
      (throw (Exception. "unimplemented")))
    (when operationType
      (throw (Exception. "unimplemented")))
    (when writeDisposition
      (throw (Exception. "unimplemented")))
    (.build builder)))
