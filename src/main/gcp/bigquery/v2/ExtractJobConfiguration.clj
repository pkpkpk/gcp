(ns gcp.bigquery.v2.ExtractJobConfiguration
  (:require [gcp.bigquery.v2.TableId :as TableId]
            [gcp.bigquery.v2.ModelId :as ModelId]
            [gcp.global :as global])
  (:import (com.google.cloud.bigquery ExtractJobConfiguration)
           (java.util List)))

(defn from-edn
  [{:keys [sourceTable
           sourceModel
           destinationUris
           compression
           fieldDelimiter
           format
           jobTimeoutMs
           labels
           printHeader
           useAvroLogicalTypes] :as arg}]
  (global/strict! :gcp/bigquery.ExtractJobConfiguration arg)
  (let [dst     ^List (seq destinationUris)
        builder (if sourceTable
                  (ExtractJobConfiguration/newBuilder (TableId/from-edn sourceTable) dst)
                  (ExtractJobConfiguration/newBuilder (ModelId/from-edn sourceModel) dst))]
    (when compression
      (.setCompression builder compression))
    (when fieldDelimiter
      (.setFieldDelimiter builder fieldDelimiter))
    (when format
      (.setFormat builder format))
    (when jobTimeoutMs
      (.setJobTimeoutMs builder (long jobTimeoutMs)))
    (when labels
      (.setLabels builder labels))
    (when (some? printHeader)
      (.setPrintHeader builder (boolean printHeader)))
    (when (some? useAvroLogicalTypes)
      (.setUseAvroLogicalTypes builder (boolean useAvroLogicalTypes)))
    (.build builder)))

(defn to-edn [^ExtractJobConfiguration arg]
  {:post [(global/strict! :gcp/bigquery.ExtractJobConfiguration %)]}
  (cond-> {:type "EXTRACT"}
          (some? (.getSourceTable arg))
          (assoc :sourceTable (TableId/to-edn (.getSourceTable arg)))

          (some? (.getSourceModel arg))
          (assoc :sourceModel (ModelId/to-edn (.getSourceModel arg)))

          (seq (.getDestinationUris arg))
          (assoc :destinationUris (vec (.getDestinationUris arg)))

          (some? (.getCompression arg))
          (assoc :compression (.getCompression arg))

          (some? (.getFieldDelimiter arg))
          (assoc :fieldDelimiter (.getFieldDelimiter arg))

          (some? (.getFormat arg))
          (assoc :format (.getFormat arg))

          (some? (.getJobTimeoutMs arg))
          (assoc :jobTimeoutMs (.getJobTimeoutMs arg))

          (seq (.getLabels arg))
          (assoc :labels (into {} (.getLabels arg)))

          (some? (.printHeader arg))
          (assoc :printHeader (.printHeader arg))

          (some? (.getUseAvroLogicalTypes arg))
          (assoc :useAvroLogicalTypes (.getUseAvroLogicalTypes arg))))
