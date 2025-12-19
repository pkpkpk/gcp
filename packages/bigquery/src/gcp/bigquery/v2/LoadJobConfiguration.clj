(ns gcp.bigquery.v2.LoadJobConfiguration
  (:import [com.google.cloud.bigquery LoadJobConfiguration
                                      JobInfo$SchemaUpdateOption JobInfo$CreateDisposition
                                      JobInfo$WriteDisposition]
           (java.util List))
  (:require
    [gcp.bigquery.v2.Clustering :as Clustering]
    [gcp.bigquery.v2.ConnectionProperty :as ConnectionProperty]
    [gcp.bigquery.v2.EncryptionConfiguration :as EncryptionConfiguration]
    [gcp.bigquery.v2.FormatOptions :as FormatOptions]
    [gcp.bigquery.v2.HivePartitioningOptions :as HivePartitioningOptions]
    [gcp.bigquery.v2.RangePartitioning :as RangePartitioning]
    [gcp.bigquery.v2.Schema :as Schema]
    [gcp.bigquery.v2.TableId :as TableId]
    [gcp.bigquery.v2.TimePartitioning :as TimePartitioning]
    [gcp.global :as global]))

(defn ^LoadJobConfiguration from-edn [arg]
  (global/strict! :gcp.bigquery.v2/LoadJobConfiguration arg)
  (let [builder (LoadJobConfiguration/newBuilder (TableId/from-edn
                                                   (get arg :destinationTable))
                                                 ^List (get arg :sourceUris))]
    (when (get arg :autodetect) (.setAutodetect builder (get arg :autodetect)))
    (when (get arg :clustering)
      (.setClustering builder (Clustering/from-edn (get arg :clustering))))
    (when (get arg :columnNameCharacterMap)
      (.setColumnNameCharacterMap builder (get arg :columnNameCharacterMap)))
    (when (get arg :connectionProperties)
      (.setConnectionProperties builder
                                (map ConnectionProperty/from-edn
                                     (get arg :connectionProperties))))
    (when (get arg :createDisposition)
      (.setCreateDisposition builder
                             (JobInfo$CreateDisposition/valueOf
                               (get arg :createDisposition))))
    (when (get arg :createSession)
      (.setCreateSession builder (get arg :createSession)))
    (when (get arg :decimalTargetTypes)
      (.setDecimalTargetTypes builder (get arg :decimalTargetTypes)))
    (when (get arg :encryptionConfiguration)
      (.setDestinationEncryptionConfiguration
        builder
        (EncryptionConfiguration/from-edn (get arg :encryptionConfiguration))))
    (when (get arg :fileSetSpecType)
      (.setFileSetSpecType builder (get arg :fileSetSpecType)))
    (when (get arg :formatOptions)
      (.setFormatOptions builder
                         (FormatOptions/from-edn (get arg :formatOptions))))
    (when (get arg :hivePartitioningOptions)
      (.setHivePartitioningOptions builder
                                   (HivePartitioningOptions/from-edn
                                     (get arg :hivePartitioningOptions))))
    (when (get arg :ignoreUnknownValues)
      (.setIgnoreUnknownValues builder (get arg :ignoreUnknownValues)))
    (when (get arg :jobTimeoutMs)
      (.setJobTimeoutMs builder (get arg :jobTimeoutMs)))
    (when (get arg :labels) (.setLabels builder (get arg :labels)))
    (when (get arg :maxBadRecords)
      (.setMaxBadRecords builder (get arg :maxBadRecords)))
    (when (get arg :nullMarker) (.setNullMarker builder (get arg :nullMarker)))
    (when (get arg :rangePartitioning)
      (.setRangePartitioning builder
                             (RangePartitioning/from-edn
                               (get arg :rangePartitioning))))
    (when (get arg :referenceFileSchemaUri)
      (.setReferenceFileSchemaUri builder (get arg :referenceFileSchemaUri)))
    (when (get arg :schema)
      (.setSchema builder (Schema/from-edn (get arg :schema))))
    (when (get arg :schemaUpdateOptions)
      (.setSchemaUpdateOptions builder (map JobInfo$SchemaUpdateOption/valueOf (get arg :schemaUpdateOptions))))
    (when (get arg :timePartitioning)
      (.setTimePartitioning builder
                            (TimePartitioning/from-edn
                              (get arg :timePartitioning))))
    (when (get arg :useAvroLogicalTypes)
      (.setUseAvroLogicalTypes builder (get arg :useAvroLogicalTypes)))
    (when (get arg :writeDisposition)
      (.setWriteDisposition builder
                            (JobInfo$WriteDisposition/valueOf
                              (get arg :writeDisposition))))
    (.build builder)))

(defn to-edn [^LoadJobConfiguration arg]
  ;(throw (Exception. "unimplemented"))
  (println "WARNING LoadJobConfiguration/to-edn not implemented")
  arg
  )

(def schemas
  {:gcp.bigquery.v2/LoadJobConfiguration
   [:map
    {:key    :gcp.bigquery.v2/LoadJobConfiguration,
     :closed true,
     :doc    "Google BigQuery load job configuration."
     :class  com.google.cloud.bigquery.LoadJobConfiguration}
    [:type [:= "LOAD"]]
    [:autodetect {:optional true} :boolean]
    [:clustering {:optional true} :gcp.bigquery.v2/Clustering]
    [:columnNameCharacterMap {:optional true} :string]
    [:connectionProperties {:optional true} [:sequential :gcp.bigquery.v2/ConnectionProperty]]
    [:createDisposition {:optional true} :gcp.bigquery.v2/JobInfo.CreateDisposition]
    [:createSession {:optional true} :boolean]
    [:decimalTargetTypes {:optional true} [:sequential :string]]
    [:destinationTable {:optional true} :gcp.bigquery.v2/TableId]
    [:encryptionConfiguration {:optional true} :gcp.bigquery.v2/EncryptionConfiguration]
    [:fileSetSpecType {:optional true} :string]
    [:formatOptions {:optional true} :gcp.bigquery.v2/FormatOptions]
    [:hivePartitioningOptions {:optional true} :gcp.bigquery.v2/HivePartitioningOptions]
    [:ignoreUnknownValues {:optional true} :boolean]
    [:jobTimeoutMs {:optional true} :int]
    [:labels {:optional true} [:map-of :string :string]]
    [:maxBadRecords {:optional true} :int]
    [:nullMarker {:optional true} :string]
    [:rangePartitioning {:optional true} :gcp.bigquery.v2/RangePartitioning]
    [:referenceFileSchemaUri {:optional true} :string]
    [:schema {:optional true} :gcp.bigquery.v2/Schema]
    [:schemaUpdateOptions {:optional true} [:sequential :gcp.bigquery.v2/JobInfo.SchemaUpdateOption]]
    [:sourceUris {:optional true} [:seqable [:string {:min 1}]]]
    [:timePartitioning {:optional true} :gcp.bigquery.v2/TimePartitioning]
    [:useAvroLogicalTypes {:optional true} :boolean]
    [:writeDisposition {:optional true} :gcp.bigquery.v2/JobInfo.WriteDisposition]]

   :gcp.bigquery.v2/JobInfo.SchemaUpdateOption [:enum "ALLOW_FIELD_RELAXATION" "ALLOW_FIELD_ADDITION"]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))