(ns gcp.bigquery.v2.WriteChannelConfiguration
  (:import [com.google.cloud.bigquery WriteChannelConfiguration
            JobInfo$SchemaUpdateOption JobInfo$CreateDisposition
            JobInfo$WriteDisposition])
  (:require [gcp.bigquery.v2.Clustering :as Clustering]
            [gcp.bigquery.v2.ConnectionProperty :as ConnectionProperty]
            [gcp.bigquery.v2.EncryptionConfiguration :as
             EncryptionConfiguration]
            [gcp.bigquery.v2.FormatOptions :as FormatOptions]
            [gcp.bigquery.v2.Schema :as Schema]
            [gcp.bigquery.v2.TableId :as TableId]
            [gcp.bigquery.v2.TimePartitioning :as TimePartitioning]
            [gcp.global :as g]))

(defn from-edn
  [arg]
  (let [builder (WriteChannelConfiguration/newBuilder
                  (TableId/from-edn (get arg :destinationTable)))]
    (when (get arg :autodetect) (.setAutodetect builder (get arg :autodetect)))
    (when (get arg :clustering)
      (.setClustering builder (Clustering/from-edn (get arg :clustering))))
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
    (when (get arg :formatOptions)
      (.setFormatOptions builder
                         (FormatOptions/from-edn (get arg :formatOptions))))
    (when (get arg :ignoreUnknownValues)
      (.setIgnoreUnknownValues builder (get arg :ignoreUnknownValues)))
    (when (get arg :labels) (.setLabels builder (get arg :labels)))
    (when (get arg :maxBadRecords)
      (.setMaxBadRecords builder (get arg :maxBadRecords)))
    (when (get arg :nullMarker) (.setNullMarker builder (get arg :nullMarker)))
    (when (get arg :schema)
      (.setSchema builder (Schema/from-edn (get arg :schema))))
    (when (get arg :schemaUpdateOptions)
      (.setSchemaUpdateOptions builder
                               (map JobInfo$SchemaUpdateOption/valueOf
                                 (get arg :schemaUpdateOptions))))
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