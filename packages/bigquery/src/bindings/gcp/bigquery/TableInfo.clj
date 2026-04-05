;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.TableInfo
  {:doc
     "Google BigQuery table information. Use {@link StandardTableDefinition} to create simple BigQuery\ntable. Use {@link ViewDefinition} to create a BigQuery view. Use {@link ExternalTableDefinition}\nto create a BigQuery a table backed by external data.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/tables\">Managing Tables</a>"
   :file-git-sha "6e3e07a22b8397e1e9d5b567589e44abc55961f2"
   :fqcn "com.google.cloud.bigquery.TableInfo"
   :gcp.dev/certification
     {:base-seed 1775131021051
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1775131021051 :standard 1775131021052 :stress 1775131021053}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:57:03.658971856Z"}}
  (:require [gcp.bigquery.CloneDefinition :as CloneDefinition]
            [gcp.bigquery.EncryptionConfiguration :as EncryptionConfiguration]
            [gcp.bigquery.TableConstraints :as TableConstraints]
            [gcp.bigquery.TableDefinition :as TableDefinition]
            [gcp.bigquery.TableId :as TableId]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery TableInfo TableInfo$Builder]))

(declare from-edn to-edn)

(defn ^TableInfo from-edn
  [arg]
  (global/strict! :gcp.bigquery/TableInfo arg)
  (let [builder (TableInfo/newBuilder (TableId/from-edn (get arg :tableId))
                                      (TableDefinition/from-edn
                                        (get arg :definition)))]
    (when (some? (get arg :cloneDefinition))
      (.setCloneDefinition builder
                           (CloneDefinition/from-edn (get arg
                                                          :cloneDefinition))))
    (when (some? (get arg :defaultCollation))
      (.setDefaultCollation builder (get arg :defaultCollation)))
    (when (some? (get arg :description))
      (.setDescription builder (get arg :description)))
    (when (some? (get arg :encryptionConfiguration))
      (.setEncryptionConfiguration builder
                                   (EncryptionConfiguration/from-edn
                                     (get arg :encryptionConfiguration))))
    (when (some? (get arg :expirationTime))
      (.setExpirationTime builder (long (get arg :expirationTime))))
    (when (some? (get arg :friendlyName))
      (.setFriendlyName builder (get arg :friendlyName)))
    (when (seq (get arg :labels))
      (.setLabels builder
                  (into {} (map (fn [[k v]] [(name k) v])) (get arg :labels))))
    (when (some? (get arg :requirePartitionFilter))
      (.setRequirePartitionFilter builder (get arg :requirePartitionFilter)))
    (when (seq (get arg :resourceTags))
      (.setResourceTags
        builder
        (into {} (map (fn [[k v]] [(name k) v])) (get arg :resourceTags))))
    (when (some? (get arg :tableConstraints))
      (.setTableConstraints builder
                            (TableConstraints/from-edn
                              (get arg :tableConstraints))))
    (.build builder)))

(defn to-edn
  [^TableInfo arg]
  {:post [(global/strict! :gcp.bigquery/TableInfo %)]}
  (when arg
    (cond-> {:definition (TableDefinition/to-edn (.getDefinition arg)),
             :tableId (TableId/to-edn (.getTableId arg))}
      (.getCloneDefinition arg) (assoc :cloneDefinition
                                  (CloneDefinition/to-edn (.getCloneDefinition
                                                            arg)))
      (.getCreationTime arg) (assoc :creationTime (.getCreationTime arg))
      (some->> (.getDefaultCollation arg)
               (not= ""))
        (assoc :defaultCollation (.getDefaultCollation arg))
      (some->> (.getDescription arg)
               (not= ""))
        (assoc :description (.getDescription arg))
      (.getEncryptionConfiguration arg) (assoc :encryptionConfiguration
                                          (EncryptionConfiguration/to-edn
                                            (.getEncryptionConfiguration arg)))
      (some->> (.getEtag arg)
               (not= ""))
        (assoc :etag (.getEtag arg))
      (.getExpirationTime arg) (assoc :expirationTime (.getExpirationTime arg))
      (some->> (.getFriendlyName arg)
               (not= ""))
        (assoc :friendlyName (.getFriendlyName arg))
      (some->> (.getGeneratedId arg)
               (not= ""))
        (assoc :generatedId (.getGeneratedId arg))
      (seq (.getLabels arg))
        (assoc :labels
          (into {} (map (fn [[k v]] [(keyword k) v])) (.getLabels arg)))
      (.getLastModifiedTime arg) (assoc :lastModifiedTime
                                   (.getLastModifiedTime arg))
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
      (.getRequirePartitionFilter arg) (assoc :requirePartitionFilter
                                         (.getRequirePartitionFilter arg))
      (seq (.getResourceTags arg))
        (assoc :resourceTags
          (into {} (map (fn [[k v]] [(keyword k) v])) (.getResourceTags arg)))
      (some->> (.getSelfLink arg)
               (not= ""))
        (assoc :selfLink (.getSelfLink arg))
      (.getTableConstraints arg) (assoc :tableConstraints
                                   (TableConstraints/to-edn
                                     (.getTableConstraints arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "Google BigQuery table information. Use {@link StandardTableDefinition} to create simple BigQuery\ntable. Use {@link ViewDefinition} to create a BigQuery view. Use {@link ExternalTableDefinition}\nto create a BigQuery a table backed by external data.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/tables\">Managing Tables</a>",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/TableInfo}
   [:cloneDefinition {:optional true} :gcp.bigquery/CloneDefinition]
   [:creationTime
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns the time when this table was created, in milliseconds since the epoch."}
    :i64] [:defaultCollation {:optional true} [:string {:min 1}]]
   [:definition {:getter-doc "Returns the table definition."}
    :gcp.bigquery/TableDefinition]
   [:description
    {:optional true,
     :getter-doc "Returns a user-friendly description for the table.",
     :setter-doc "Sets a user-friendly description for the table."}
    [:string {:min 1}]]
   [:encryptionConfiguration {:optional true}
    :gcp.bigquery/EncryptionConfiguration]
   [:etag
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the hash of the table resource."} [:string {:min 1}]]
   [:expirationTime
    {:optional true,
     :getter-doc
       "Returns the time when this table expires, in milliseconds since the epoch. If not present, the\ntable will persist indefinitely. Expired tables will be deleted and their storage reclaimed.",
     :setter-doc
       "Sets the time when this table expires, in milliseconds since the epoch. If not present, the\ntable will persist indefinitely. Expired tables will be deleted and their storage reclaimed."}
    :i64]
   [:friendlyName
    {:optional true,
     :getter-doc "Returns a user-friendly name for the table.",
     :setter-doc "Sets a user-friendly name for the table."} [:string {:min 1}]]
   [:generatedId
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the service-generated id for the table."}
    [:string {:min 1}]]
   [:labels
    {:optional true,
     :getter-doc
       "Return a map for labels applied to the table.\n\n<p>Unstable, because labels are <a\nhref=\"https://cloud.google.com/bigquery/docs/reference/rest/v2/tables\">experimental</a>.",
     :setter-doc
       "Sets the labels applied to this table.\n\n<p>Unstable, because labels are <a\nhref=\"https://cloud.google.com/bigquery/docs/reference/rest/v2/tables\">experimental</a>.\n\n<p>When used with {@link BigQuery#update(TableInfo, TableOption...)}, setting {@code labels}\nto {@code null} removes all labels; otherwise all keys that are mapped to {@code null} values\nare removed and other keys are updated to their respective values."}
    [:map-of [:or simple-keyword? [:string {:min 1}]] [:string {:min 1}]]]
   [:lastModifiedTime
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns the time when this table was last modified, in milliseconds since the epoch."}
    :i64]
   [:numActiveLogicalBytes
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns the number of active logical bytes.\n\n@see <a href=\"https://cloud.google.com/bigquery/pricing#storage\">Storage Pricing</a>"}
    :i64]
   [:numActivePhysicalBytes
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns the number of active physical bytes.\n\n@see <a href=\"https://cloud.google.com/bigquery/pricing#storage\">Storage Pricing</a>"}
    :i64]
   [:numBytes
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the size of this table in bytes"} :i64]
   [:numLongTermBytes
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns the number of bytes considered \"long-term storage\" for reduced billing purposes.\n\n@see <a href=\"https://cloud.google.com/bigquery/pricing#long-term-storage\">Long Term Storage\n    Pricing</a>"}
    :i64]
   [:numLongTermLogicalBytes
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns the number of long term logical bytes.\n\n@see <a href=\"https://cloud.google.com/bigquery/pricing#storage\">Storage Pricing</a>"}
    :i64]
   [:numLongTermPhysicalBytes
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns the number of long term physical bytes.\n\n@see <a href=\"https://cloud.google.com/bigquery/pricing#storage\">Storage Pricing</a>"}
    :i64]
   [:numRows
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the number of rows of data in this table"} :bigint]
   [:numTimeTravelPhysicalBytes
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns the number of time travel physical bytes.\n\n@see <a href=\"https://cloud.google.com/bigquery/pricing#storage\">Storage Pricing</a>"}
    :i64]
   [:numTotalLogicalBytes
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns the number of total logical bytes.\n\n@see <a href=\"https://cloud.google.com/bigquery/pricing#storage\">Storage Pricing</a>"}
    :i64]
   [:numTotalPhysicalBytes
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns the number of total physical bytes.\n\n@see <a href=\"https://cloud.google.com/bigquery/pricing#storage\">Storage Pricing</a>"}
    :i64]
   [:requirePartitionFilter
    {:optional true,
     :getter-doc
       "Returns true if a partition filter (that can be used for partition elimination) is required for\nqueries over this table."}
    :boolean]
   [:resourceTags
    {:optional true,
     :getter-doc "Return a map for resource tags applied to the table.",
     :setter-doc "Sets the resource tags applied to this table."}
    [:map-of [:or simple-keyword? [:string {:min 1}]] [:string {:min 1}]]]
   [:selfLink
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns an URL that can be used to access the resource again. The returned URL can be used for\nget or update requests."}
    [:string {:min 1}]]
   [:tableConstraints {:optional true} :gcp.bigquery/TableConstraints]
   [:tableId {:getter-doc "Returns the table identity."}
    :gcp.bigquery/TableId]])

(global/include-schema-registry! (with-meta {:gcp.bigquery/TableInfo schema}
                                   {:gcp.global/name "gcp.bigquery.TableInfo"}))