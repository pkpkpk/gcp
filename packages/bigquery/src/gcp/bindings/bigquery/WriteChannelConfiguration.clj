;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.WriteChannelConfiguration
  {:doc
     "Google BigQuery Configuration for a load operation. A load configuration can be used to load data\ninto a table with a {@link com.google.cloud.WriteChannel} ({@link\nBigQuery#writer(WriteChannelConfiguration)})."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.WriteChannelConfiguration"
   :gcp.dev/certification
     {:base-seed 1772051241082
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1772051241082 :standard 1772051241083 :stress 1772051241084}
      :protocol-hash
        "b8a5eb17212acb18f49dcb7f15a243eac9b32f54c0f054a99be660a3a25e0315"
      :timestamp "2026-02-25T20:27:31.031066912Z"}}
  (:require
    [gcp.bindings.bigquery.Clustering :as Clustering]
    [gcp.bindings.bigquery.ConnectionProperty :as ConnectionProperty]
    [gcp.bindings.bigquery.CsvOptions :as CsvOptions]
    [gcp.bindings.bigquery.DatastoreBackupOptions :as DatastoreBackupOptions]
    [gcp.bindings.bigquery.EncryptionConfiguration :as EncryptionConfiguration]
    [gcp.bindings.bigquery.FormatOptions :as FormatOptions]
    [gcp.bindings.bigquery.Schema :as Schema]
    [gcp.bindings.bigquery.TableId :as TableId]
    [gcp.bindings.bigquery.TimePartitioning :as TimePartitioning]
    [gcp.global :as global])
  (:import [com.google.cloud.bigquery JobInfo$CreateDisposition
            JobInfo$SchemaUpdateOption JobInfo$WriteDisposition
            WriteChannelConfiguration WriteChannelConfiguration$Builder]))

(defn ^WriteChannelConfiguration from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/WriteChannelConfiguration arg)
  (let [builder (WriteChannelConfiguration/newBuilder
                  (TableId/from-edn (get arg :destinationTable)))]
    (when (some? (get arg :autodetect))
      (.setAutodetect builder (get arg :autodetect)))
    (when (some? (get arg :clustering))
      (.setClustering builder (Clustering/from-edn (get arg :clustering))))
    (when (some? (get arg :connectionProperties))
      (.setConnectionProperties builder
                                (map ConnectionProperty/from-edn
                                  (get arg :connectionProperties))))
    (when (some? (get arg :createDisposition))
      (.setCreateDisposition builder
                             (JobInfo$CreateDisposition/valueOf
                               (get arg :createDisposition))))
    (when (some? (get arg :createSession))
      (.setCreateSession builder (get arg :createSession)))
    (when (some? (get arg :decimalTargetTypes))
      (.setDecimalTargetTypes builder (seq (get arg :decimalTargetTypes))))
    (when (some? (get arg :destinationEncryptionConfiguration))
      (.setDestinationEncryptionConfiguration
        builder
        (EncryptionConfiguration/from-edn
          (get arg :destinationEncryptionConfiguration))))
    (when (some? (get arg :formatOptions))
      (.setFormatOptions builder
                         (FormatOptions/from-edn (get arg :formatOptions))))
    (when (some? (get arg :ignoreUnknownValues))
      (.setIgnoreUnknownValues builder (get arg :ignoreUnknownValues)))
    (when (some? (get arg :labels))
      (.setLabels builder
                  (into {} (map (fn [[k v]] [(name k) v])) (get arg :labels))))
    (when (some? (get arg :maxBadRecords))
      (.setMaxBadRecords builder (int (get arg :maxBadRecords))))
    (when (some? (get arg :nullMarker))
      (.setNullMarker builder (get arg :nullMarker)))
    (when (some? (get arg :schema))
      (.setSchema builder (Schema/from-edn (get arg :schema))))
    (when (some? (get arg :schemaUpdateOptions))
      (.setSchemaUpdateOptions builder
                               (map JobInfo$SchemaUpdateOption/valueOf
                                 (get arg :schemaUpdateOptions))))
    (when (some? (get arg :timePartitioning))
      (.setTimePartitioning builder
                            (TimePartitioning/from-edn
                              (get arg :timePartitioning))))
    (when (some? (get arg :useAvroLogicalTypes))
      (.setUseAvroLogicalTypes builder (get arg :useAvroLogicalTypes)))
    (when (some? (get arg :writeDisposition))
      (.setWriteDisposition builder
                            (JobInfo$WriteDisposition/valueOf
                              (get arg :writeDisposition))))
    (.build builder)))

(defn to-edn
  [^WriteChannelConfiguration arg]
  {:post [(global/strict! :gcp.bindings.bigquery/WriteChannelConfiguration %)]}
  (cond-> {:destinationTable (TableId/to-edn (.getDestinationTable arg))}
    (.getAutodetect arg) (assoc :autodetect (.getAutodetect arg))
    (.getClustering arg) (assoc :clustering
                           (Clustering/to-edn (.getClustering arg)))
    (.getConnectionProperties arg) (assoc :connectionProperties
                                     (map ConnectionProperty/to-edn
                                       (.getConnectionProperties arg)))
    (.getCreateDisposition arg) (assoc :createDisposition
                                  (.name (.getCreateDisposition arg)))
    (.getCreateSession arg) (assoc :createSession (.getCreateSession arg))
    (.getCsvOptions arg) (assoc :csvOptions
                           (CsvOptions/to-edn (.getCsvOptions arg)))
    (.getDatastoreBackupOptions arg) (assoc :datastoreBackupOptions
                                       (DatastoreBackupOptions/to-edn
                                         (.getDatastoreBackupOptions arg)))
    (.getDecimalTargetTypes arg) (assoc :decimalTargetTypes
                                   (seq (.getDecimalTargetTypes arg)))
    (.getDestinationEncryptionConfiguration arg)
      (assoc :destinationEncryptionConfiguration
        (EncryptionConfiguration/to-edn (.getDestinationEncryptionConfiguration
                                          arg)))
    (.getFormat arg) (assoc :format (.getFormat arg))
    (.ignoreUnknownValues arg) (assoc :ignoreUnknownValues
                                 (.ignoreUnknownValues arg))
    (.getLabels arg)
      (assoc :labels
        (into {} (map (fn [[k v]] [(keyword k) v])) (.getLabels arg)))
    (.getMaxBadRecords arg) (assoc :maxBadRecords (.getMaxBadRecords arg))
    (.getNullMarker arg) (assoc :nullMarker (.getNullMarker arg))
    (.getSchema arg) (assoc :schema (Schema/to-edn (.getSchema arg)))
    (.getSchemaUpdateOptions arg) (assoc :schemaUpdateOptions
                                    (map (fn [e] (.name e))
                                      (.getSchemaUpdateOptions arg)))
    (.getTimePartitioning arg) (assoc :timePartitioning
                                 (TimePartitioning/to-edn (.getTimePartitioning
                                                            arg)))
    (.getUseAvroLogicalTypes arg) (assoc :useAvroLogicalTypes
                                    (.getUseAvroLogicalTypes arg))
    (.getWriteDisposition arg) (assoc :writeDisposition
                                 (.name (.getWriteDisposition arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Google BigQuery Configuration for a load operation. A load configuration can be used to load data\ninto a table with a {@link com.google.cloud.WriteChannel} ({@link\nBigQuery#writer(WriteChannelConfiguration)}).",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/WriteChannelConfiguration}
   [:autodetect {:optional true} :boolean]
   [:clustering {:optional true} :gcp.bindings.bigquery/Clustering]
   [:connectionProperties {:optional true}
    [:sequential {:min 1} :gcp.bindings.bigquery/ConnectionProperty]]
   [:createDisposition {:optional true}
    [:enum {:closed true} "CREATE_IF_NEEDED" "CREATE_NEVER"]]
   [:createSession {:optional true} :boolean]
   [:csvOptions {:optional true, :read-only? true}
    :gcp.bindings.bigquery/CsvOptions]
   [:datastoreBackupOptions {:optional true, :read-only? true}
    :gcp.bindings.bigquery/DatastoreBackupOptions]
   [:decimalTargetTypes {:optional true}
    [:sequential {:min 1} [:string {:min 1}]]]
   [:destinationEncryptionConfiguration {:optional true}
    :gcp.bindings.bigquery/EncryptionConfiguration]
   [:destinationTable {:getter-doc nil} :gcp.bindings.bigquery/TableId]
   [:format {:optional true, :read-only? true} [:string {:min 1}]]
   [:ignoreUnknownValues {:optional true} :boolean]
   [:labels {:optional true}
    [:map-of [:or simple-keyword? [:string {:min 1}]] [:string {:min 1}]]]
   [:maxBadRecords {:optional true} [:int {:min -2147483648, :max 2147483647}]]
   [:nullMarker {:optional true} [:string {:min 1}]]
   [:schema {:optional true} :gcp.bindings.bigquery/Schema]
   [:schemaUpdateOptions {:optional true}
    [:sequential {:min 1}
     [:enum {:closed true} "ALLOW_FIELD_ADDITION" "ALLOW_FIELD_RELAXATION"]]]
   [:timePartitioning {:optional true} :gcp.bindings.bigquery/TimePartitioning]
   [:useAvroLogicalTypes {:optional true} :boolean]
   [:writeDisposition {:optional true}
    [:enum {:closed true} "WRITE_TRUNCATE" "WRITE_TRUNCATE_DATA" "WRITE_APPEND"
     "WRITE_EMPTY"]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/WriteChannelConfiguration schema}
    {:gcp.global/name "gcp.bindings.bigquery.WriteChannelConfiguration"}))