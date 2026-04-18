;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.WriteChannelConfiguration
  {:doc
     "Google BigQuery Configuration for a load operation. A load configuration can be used to load data\ninto a table with a {@link com.google.cloud.WriteChannel} ({@link\nBigQuery#writer(WriteChannelConfiguration)})."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.WriteChannelConfiguration"
   :gcp.dev/certification
     {:base-seed 1776499501003
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1776499501003 :standard 1776499501004 :stress 1776499501005}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:05:03.079213929Z"}}
  (:require [gcp.bigquery.Clustering :as Clustering]
            [gcp.bigquery.ConnectionProperty :as ConnectionProperty]
            [gcp.bigquery.CsvOptions :as CsvOptions]
            [gcp.bigquery.EncryptionConfiguration :as EncryptionConfiguration]
            [gcp.bigquery.FormatOptions :as FormatOptions]
            [gcp.bigquery.Schema :as Schema]
            [gcp.bigquery.TableId :as TableId]
            [gcp.bigquery.TimePartitioning :as TimePartitioning]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery JobInfo$CreateDisposition
            JobInfo$SchemaUpdateOption JobInfo$WriteDisposition
            WriteChannelConfiguration WriteChannelConfiguration$Builder]))

(declare from-edn to-edn)

(defn ^WriteChannelConfiguration from-edn
  [arg]
  (global/strict! :gcp.bigquery/WriteChannelConfiguration arg)
  (let [builder (WriteChannelConfiguration/newBuilder
                  (TableId/from-edn (get arg :destinationTable)))]
    (when (some? (get arg :autodetect))
      (.setAutodetect builder (get arg :autodetect)))
    (when (some? (get arg :clustering))
      (.setClustering builder (Clustering/from-edn (get arg :clustering))))
    (when (seq (get arg :connectionProperties))
      (.setConnectionProperties builder
                                (mapv ConnectionProperty/from-edn
                                  (get arg :connectionProperties))))
    (when (some? (get arg :createDisposition))
      (.setCreateDisposition builder
                             (JobInfo$CreateDisposition/valueOf
                               (get arg :createDisposition))))
    (when (some? (get arg :createSession))
      (.setCreateSession builder (get arg :createSession)))
    (when (seq (get arg :decimalTargetTypes))
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
    (when (seq (get arg :labels))
      (.setLabels builder
                  (into {} (map (fn [[k v]] [(name k) v])) (get arg :labels))))
    (when (some? (get arg :maxBadRecords))
      (.setMaxBadRecords builder (int (get arg :maxBadRecords))))
    (when (some? (get arg :nullMarker))
      (.setNullMarker builder (get arg :nullMarker)))
    (when (some? (get arg :schema))
      (.setSchema builder (Schema/from-edn (get arg :schema))))
    (when (seq (get arg :schemaUpdateOptions))
      (.setSchemaUpdateOptions builder
                               (mapv JobInfo$SchemaUpdateOption/valueOf
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
  {:post [(global/strict! :gcp.bigquery/WriteChannelConfiguration %)]}
  (when arg
    (cond-> {:destinationTable (TableId/to-edn (.getDestinationTable arg))}
      (.getAutodetect arg) (assoc :autodetect (.getAutodetect arg))
      (.getClustering arg) (assoc :clustering
                             (Clustering/to-edn (.getClustering arg)))
      (seq (.getConnectionProperties arg)) (assoc :connectionProperties
                                             (mapv ConnectionProperty/to-edn
                                               (.getConnectionProperties arg)))
      (.getCreateDisposition arg) (assoc :createDisposition
                                    (.name (.getCreateDisposition arg)))
      (.getCreateSession arg) (assoc :createSession (.getCreateSession arg))
      (seq (.getDecimalTargetTypes arg)) (assoc :decimalTargetTypes
                                           (seq (.getDecimalTargetTypes arg)))
      (.getDestinationEncryptionConfiguration arg)
        (assoc :destinationEncryptionConfiguration
          (EncryptionConfiguration/to-edn
            (.getDestinationEncryptionConfiguration arg)))
      (.getCsvOptions arg) (assoc :formatOptions
                             (CsvOptions/to-edn (.getCsvOptions arg)))
      (.ignoreUnknownValues arg) (assoc :ignoreUnknownValues
                                   (.ignoreUnknownValues arg))
      (seq (.getLabels arg))
        (assoc :labels
          (into {} (map (fn [[k v]] [(keyword k) v])) (.getLabels arg)))
      (.getMaxBadRecords arg) (assoc :maxBadRecords (.getMaxBadRecords arg))
      (some->> (.getNullMarker arg)
               (not= ""))
        (assoc :nullMarker (.getNullMarker arg))
      (.getSchema arg) (assoc :schema (Schema/to-edn (.getSchema arg)))
      (seq (.getSchemaUpdateOptions arg)) (assoc :schemaUpdateOptions
                                            (mapv (fn [e] (.name e))
                                              (.getSchemaUpdateOptions arg)))
      (.getTimePartitioning arg) (assoc :timePartitioning
                                   (TimePartitioning/to-edn
                                     (.getTimePartitioning arg)))
      (.getUseAvroLogicalTypes arg) (assoc :useAvroLogicalTypes
                                      (.getUseAvroLogicalTypes arg))
      (.getWriteDisposition arg) (assoc :writeDisposition
                                   (.name (.getWriteDisposition arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "Google BigQuery Configuration for a load operation. A load configuration can be used to load data\ninto a table with a {@link com.google.cloud.WriteChannel} ({@link\nBigQuery#writer(WriteChannelConfiguration)}).",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/WriteChannelConfiguration}
   [:autodetect {:optional true, :setter-doc nil} :boolean]
   [:clustering {:optional true, :setter-doc nil} :gcp.bigquery/Clustering]
   [:connectionProperties {:optional true, :setter-doc nil}
    [:sequential {:min 1} :gcp.bigquery/ConnectionProperty]]
   [:createDisposition {:optional true, :setter-doc nil}
    [:enum {:closed true} "CREATE_IF_NEEDED" "CREATE_NEVER"]]
   [:createSession {:optional true, :setter-doc nil} :boolean]
   [:decimalTargetTypes {:optional true, :setter-doc nil}
    [:sequential {:min 1} [:string {:min 1}]]]
   [:destinationEncryptionConfiguration {:optional true, :setter-doc nil}
    :gcp.bigquery/EncryptionConfiguration]
   [:destinationTable {:getter-doc nil} :gcp.bigquery/TableId]
   [:formatOptions {:optional true, :setter-doc nil} :gcp.bigquery/CsvOptions]
   [:ignoreUnknownValues {:optional true, :setter-doc nil} :boolean]
   [:labels {:optional true, :setter-doc nil}
    [:map-of [:or simple-keyword? [:string {:min 1}]] [:string {:min 1}]]]
   [:maxBadRecords {:optional true, :setter-doc nil} :i32]
   [:nullMarker {:optional true, :setter-doc nil} [:string {:min 1}]]
   [:schema {:optional true, :setter-doc nil} :gcp.bigquery/Schema]
   [:schemaUpdateOptions {:optional true, :setter-doc nil}
    [:sequential {:min 1}
     [:enum {:closed true} "ALLOW_FIELD_ADDITION" "ALLOW_FIELD_RELAXATION"]]]
   [:timePartitioning {:optional true, :setter-doc nil}
    :gcp.bigquery/TimePartitioning]
   [:useAvroLogicalTypes {:optional true, :setter-doc nil} :boolean]
   [:writeDisposition {:optional true, :setter-doc nil}
    [:enum {:closed true} "WRITE_TRUNCATE" "WRITE_TRUNCATE_DATA" "WRITE_APPEND"
     "WRITE_EMPTY"]]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/WriteChannelConfiguration schema}
    {:gcp.global/name "gcp.bigquery.WriteChannelConfiguration"}))