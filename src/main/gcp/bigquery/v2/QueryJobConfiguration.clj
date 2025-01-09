(ns gcp.bigquery.v2.QueryJobConfiguration
  (:require [gcp.bigquery.v2.Clustering :as Clustering]
            [gcp.bigquery.v2.ConnectionProperty :as ConnectionProperty]
            [gcp.bigquery.v2.DatasetId :as DatasetId]
            [gcp.bigquery.v2.EncryptionConfiguration :as EncryptionConfiguration]
            [gcp.bigquery.v2.ExternalTableDefinition :as ExternalTableDefinition]
            [gcp.bigquery.v2.QueryParameterValue :as QueryParameterValue]
            [gcp.bigquery.v2.RangePartitioning :as RangePartitioning]
            [gcp.bigquery.v2.TableId :as TableId]
            [gcp.bigquery.v2.TimePartitioning :as TimePartitioning]
            [gcp.bigquery.v2.UserDefinedFunction :as UserDefinedFunction]
            [gcp.global :as global])
  (:import (com.google.cloud.bigquery JobInfo$CreateDisposition JobInfo$SchemaUpdateOption JobInfo$WriteDisposition QueryJobConfiguration QueryJobConfiguration$Priority)))

(defn ^QueryJobConfiguration from-edn
  [{:keys [allowLargeResults clustering connectionProperties createDisposition createSession
           defaultDataset destinationTable dryRun encryptionConfiguration flattenResults jobTimeoutMs
           labels maxResults maximumBillingTier maximumBytesBilled priority query queryParameters
           rangePartitioning schemaUpdateOptions tableDefinitions timePartitioning useLegacySql useQueryCache
           userDefinedFunctions writeDisposition]
    :as arg}]
  (global/strict! :bigquery/QueryJobConfiguration arg)
  (let [builder (QueryJobConfiguration/newBuilder query)]
    (when defaultDataset
      (if (string? defaultDataset)
        (.setDefaultDataset builder ^String defaultDataset)
        (.setDefaultDataset builder (DatasetId/from-edn defaultDataset))))
    (when createDisposition
      (.setCreateDisposition builder (JobInfo$CreateDisposition/valueOf createDisposition)))
    (when writeDisposition
      (.setWriteDisposition builder (JobInfo$WriteDisposition/valueOf writeDisposition)))
    (when priority
      (.setPriority builder (QueryJobConfiguration$Priority/valueOf priority)))
    (when (seq queryParameters)
      (if (map? queryParameters)
        (do
          (.setParameterMode builder "NAMED")
          (doseq [[k v] queryParameters]
            (when (some? v)
              (.addNamedParameter builder (name k) (QueryParameterValue/from-edn val)))))
        (do
          (.setParameterMode builder "POSITIONAL")
          (.setPositionalParameters builder (map QueryParameterValue/from-edn queryParameters)))))
    (doseq [[table-name tableDefinition] tableDefinitions]
      (.addTableDefinition builder table-name (ExternalTableDefinition/from-edn tableDefinition)))
    (some->> dryRun (.setDryRun builder))
    (some->> allowLargeResults (.setAllowLargeResults builder))
    (some->> createSession (.setCreateSession builder))
    (some->> flattenResults (.setFlattenResults builder))
    (some->> jobTimeoutMs (.setJobTimeoutMs builder))
    (some->> labels (.setLabels builder))
    (some->> maxResults (.setMaxResults builder))
    (some->> maximumBillingTier (.setMaximumBillingTier builder))
    (some->> maximumBytesBilled (.setMaximumBytesBilled builder))
    (some->> useLegacySql (.setUseLegacySql builder))
    (some->> useQueryCache (.setUseQueryCache builder))
    (some->> destinationTable TableId/from-edn (.setDestinationTable builder))
    (some->> clustering Clustering/from-edn (.setClustering builder))
    (some->> encryptionConfiguration EncryptionConfiguration/from-edn (.setDestinationEncryptionConfiguration builder))
    (some->> rangePartitioning RangePartitioning/from-edn (.setRangePartitioning builder))
    (some->> timePartitioning TimePartitioning/from-edn (.setTimePartitioning builder))
    (some->> connectionProperties (map ConnectionProperty/from-edn) (.setConnectionProperties builder))
    (some->> schemaUpdateOptions (map #(JobInfo$SchemaUpdateOption/valueOf %)) (.setSchemaUpdateOptions builder))
    (some->> userDefinedFunctions (map UserDefinedFunction/from-edn) (.setUserDefinedFunctions builder))
    (.build builder)))
