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
           userDefinedFunctions writeDisposition] :as arg}]
  (global/strict! :gcp.bigquery.v2/QueryJobConfiguration arg)
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
              (.addNamedParameter builder (name k) (QueryParameterValue/from-edn v)))))
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

(defn to-edn [^QueryJobConfiguration arg]
  {:post [(global/strict! :gcp.bigquery.v2/QueryJobConfiguration %)]}
  (cond-> {:type  (.name (.getType arg))
           :query (.getQuery arg)}

          (some? (.getDefaultDataset arg))
          (assoc :defaultDataset (DatasetId/to-edn (.getDefaultDataset arg)))

          (some? (.allowLargeResults arg))
          (assoc :allowLargeResults (.allowLargeResults arg))

          (some? (.getClustering arg))
          (assoc :clustering (Clustering/to-edn (.getClustering arg)))

          (seq (.getConnectionProperties arg))
          (assoc :connectionProperties (mapv ConnectionProperty/to-edn (.getConnectionProperties arg)))

          (some? (.getCreateDisposition arg))
          (assoc :createDisposition (str (.getCreateDisposition arg)))

          (some? (.createSession arg))
          (assoc :createSession (.createSession arg))

          (some? (.dryRun arg))
          (assoc :dryRun (.dryRun arg))

          (some? (.flattenResults arg))
          (assoc :flattenResults (.flattenResults arg))

          (some? (.getDestinationTable arg))
          (assoc :destinationTable (TableId/to-edn (.getDestinationTable arg)))

          (some? (.getDestinationEncryptionConfiguration arg))
          (assoc :encryptionConfiguration (EncryptionConfiguration/to-edn (.getDestinationEncryptionConfiguration arg)))

          (some? (.getJobTimeoutMs arg))
          (assoc :jobTimeoutMs (.getJobTimeoutMs arg))

          (seq (.getLabels arg))
          (assoc :labels (into {} (.getLabels arg)))

          (some? (.getMaxResults arg))
          (assoc :maxResults (.getMaxResults arg))

          (some? (.getMaximumBillingTier arg))
          (assoc :maximumBillingTier (.getMaximumBillingTier arg))

          (some? (.getMaximumBytesBilled arg))
          (assoc :maximumBytesBilled (.getMaximumBytesBilled arg))

          (some? (.getPriority arg))
          (assoc :priority (str (.getPriority arg)))

          (some? (.getNamedParameters arg))
          (assoc :queryParameters
                 (into {}
                       (map (fn [[k v]]
                              [(keyword k) (QueryParameterValue/to-edn v)]))
                       (.getNamedParameters arg)))

          (some? (.getPositionalParameters arg))
          (assoc :queryParameters (mapv QueryParameterValue/to-edn (.getPositionalParameters arg)))

          (some? (.getRangePartitioning arg))
          (assoc :rangePartitioning (RangePartitioning/to-edn (.getRangePartitioning arg)))

          (seq (.getSchemaUpdateOptions arg))
          (assoc :schemaUpdateOptions (mapv #(str %) (.getSchemaUpdateOptions arg)))

          (seq (.getTableDefinitions arg))
          (assoc :tableDefinitions
                 (into {}
                       (map (fn [[k v]]
                              [k (ExternalTableDefinition/to-edn v)]))
                       (.getTableDefinitions arg)))

          (some? (.getTimePartitioning arg))
          (assoc :timePartitioning (TimePartitioning/to-edn (.getTimePartitioning arg)))

          (some? (.useLegacySql arg))
          (assoc :useLegacySql (.useLegacySql arg))

          (some? (.useQueryCache arg))
          (assoc :useQueryCache (.useQueryCache arg))

          (seq (.getUserDefinedFunctions arg))
          (assoc :userDefinedFunctions (mapv UserDefinedFunction/to-edn (.getUserDefinedFunctions arg)))

          (some? (.getWriteDisposition arg))
          (assoc :writeDisposition (str (.getWriteDisposition arg)))))

(def schemas
  {:gcp.bigquery.v2/QueryJobConfiguration
   [:map
    {:closed true
     :class  'com.google.cloud.bigquery.QueryJobConfiguration}
    [:type [:= "QUERY"]]
    [:allowLargeResults {:optional true} :boolean]
    [:clustering {:optional true} :gcp.bigquery.v2/Clustering]
    [:connectionProperties {:optional true} [:or [:map-of :string :string] [:sequential :gcp.bigquery.v2/ConnectionProperty]]]
    [:createDisposition {:optional true} :gcp.bigquery.v2/JobInfo.CreateDisposition]
    [:createSession {:optional true} :boolean]
    [:defaultDataset {:optional true} :gcp.bigquery.v2/DatasetId]
    [:destinationTable {:optional true} :gcp.bigquery.v2/TableId]
    [:dryRun {:optional true} :boolean]
    [:encryptionConfiguration {:optional true} :gcp.bigquery.v2/EncryptionConfiguration]
    [:flattenResults {:optional true} :boolean]
    [:jobTimeoutMs {:optional true} 'number?]
    [:labels {:optional true} [:map-of :string :string]]
    [:maxResults {:optional true} 'number?]
    [:maximumBillingTier {:optional true} 'number?]
    [:maximumBytesBilled {:optional true} 'number?]
    [:priority {:optional true} :gcp.bigquery.v2/QueryJobConfiguration.Priority]
    [:query :string]
    [:queryParameters {:optional true} :gcp.bigquery.v2/QueryParameterValue]
    [:rangePartitioning {:optional true} :gcp.bigquery.v2/RangePartitioning]
    [:schemaUpdateOptions {:optional true} [:sequential :gcp.bigquery.v2/JobInfo.SchemaUpdateOption]]
    [:tableDefinitions {:optional true} [:map-of :string :gcp.bigquery.v2/ExternalTableDefinition]]
    [:timePartitioning {:optional true} :gcp.bigquery.v2/TimePartitioning]
    [:useLegacySql {:optional true} :boolean]
    [:useQueryCache {:optional true} :boolean]
    [:userDefinedFunctions {:optional true} [:sequential :gcp.bigquery.v2/UserDefinedFunction]]
    [:writeDisposition {:optional true} :gcp.bigquery.v2/JobInfo.WriteDisposition]]

   :gcp.bigquery.v2/QueryJobConfiguration.Priority [:enum "BATCH" "INTERACTIVE"]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))

