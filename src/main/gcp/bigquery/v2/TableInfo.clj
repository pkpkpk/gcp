(ns gcp.bigquery.v2.TableInfo
  (:require [gcp.bigquery.v2.TableId :as TableId]
            [gcp.bigquery.v2.TableConstraints :as TableConstraints]
            [gcp.bigquery.v2.TableDefinition :as TableDefinition]
            [gcp.bigquery.v2.EncryptionConfiguration :as EncryptionConfiguration]
            [gcp.global :as global])
  (:import (com.google.cloud.bigquery TableInfo)))

(defn ^TableInfo from-edn
  [{:keys [tableId
           defaultCollation
           description
           encryptionConfiguration
           expirationTime
           friendlyName
           labels
           requirePartitionFilter
           resourceTags
           tableConstraints
           definition
           cloneDefinition] :as arg}]
  (global/strict! :bigquery/TableInfo arg)
  (let [table-def (cond
                    definition      (throw (Exception. "unimplemented: build table definition from EDN"))
                    cloneDefinition (throw (Exception. "unimplemented: build clone definition from EDN"))
                    :else           nil)
        builder   (TableInfo/newBuilder (TableId/from-edn tableId) table-def)]
    (when defaultCollation
      (.setDefaultCollation builder defaultCollation))
    (when description
      (.setDescription builder description))
    (when encryptionConfiguration
      (.setEncryptionConfiguration builder (EncryptionConfiguration/from-edn encryptionConfiguration)))
    (when expirationTime
      (.setExpirationTime builder (long expirationTime)))
    (when friendlyName
      (.setFriendlyName builder friendlyName))
    (when labels
      (.setLabels builder labels))
    (when (some? requirePartitionFilter)
      (.setRequirePartitionFilter builder (boolean requirePartitionFilter)))
    (when resourceTags
      (throw (Exception. "unimplemented: setResourceTags may be read-only or unavailable")))
    (some->> tableConstraints TableConstraints/from-edn (.setTableConstraints builder))
    (.build builder)))

(defn to-edn [^TableInfo arg]
  {:post [(global/strict! :bigquery/TableInfo %)]}
  (cond-> {:tableId (TableId/to-edn (.getTableId arg))}

          (some? (.getCreationTime arg))
          (assoc :creationTime (.getCreationTime arg))

          (some? (.getDefaultCollation arg))
          (assoc :defaultCollation (.getDefaultCollation arg))

          (some? (.getDescription arg))
          (assoc :description (.getDescription arg))

          (some? (.getEncryptionConfiguration arg))
          (assoc :encryptionConfiguration (EncryptionConfiguration/to-edn (.getEncryptionConfiguration arg)))

          (some? (.getEtag arg))
          (assoc :etag (.getEtag arg))

          (some? (.getExpirationTime arg))
          (assoc :expirationTime (.getExpirationTime arg))

          (some? (.getFriendlyName arg))
          (assoc :friendlyName (.getFriendlyName arg))

          (some? (.getGeneratedId arg))
          (assoc :generatedId (.getGeneratedId arg))

          (seq (.getLabels arg))
          (assoc :labels (into {} (.getLabels arg)))

          (some? (.getLastModifiedTime arg))
          (assoc :lastModifiedTime (.getLastModifiedTime arg))

          ;; For numeric fields that default to 0, you may choose (pos?) or just (some?).
          ;; The snippet below only includes them if they are > 0.
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

          (some? (.getRequirePartitionFilter arg))
          (assoc :requirePartitionFilter (.getRequirePartitionFilter arg))

          (seq (.getResourceTags arg))
          (assoc :resourceTags (into {} (.getResourceTags arg)))

          (some? (.getSelfLink arg))
          (assoc :selfLink (.getSelfLink arg))

          (some? (.getTableConstraints arg))
          (assoc :tableConstraints (TableConstraints/to-edn (.getTableConstraints arg)))

          ;; If you have specific "to-edn" logic for definitions or clone definitions,
          ;; you can call them here, or raise an exception until implemented:
          (some? (.getDefinition arg))
          (assoc :definition (TableDefinition/to-edn (.getDefinition arg)))

          (some? (.getCloneDefinition arg))
          (assoc :cloneDefinition
                 {:baseTableId (TableId/to-edn (.getBaseTableId (.getCloneDefinition arg)))
                  :dateTime   (.getCloneTime (.getCloneDefinition arg))})))
