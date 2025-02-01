(ns gcp.bigquery.v2.DatasetInfo
  (:require [gcp.bigquery.v2.Acl :as Acl]
            [gcp.bigquery.v2.DatasetId :as DatasetId]
            [gcp.bigquery.v2.EncryptionConfiguration :as EncryptionConfiguration]
            [gcp.bigquery.v2.ExternalDatasetReference :as ExternalDatasetReference]
            [gcp.global :as global])
  (:import (com.google.cloud.bigquery DatasetInfo)))

(defn ^DatasetInfo from-edn
  [{:keys [acl
           datasetId
           defaultCollation
           defaultEncryptionConfiguration
           defaultPartitionExpirationMs
           defaultTableLifeTime
           description
           externalDatasetReference
           friendlyName
           labels
           location
           maxTimeTravelHours
           storageBillingModel] :as arg}]
  (global/strict! :gcp/bigquery.DatasetInfo arg)
  (let [builder (DatasetInfo/newBuilder (DatasetId/from-edn datasetId))]
    (when acl
      (.setAcl builder (map Acl/from-edn acl)))
    (when defaultCollation
      (.setDefaultCollation builder defaultCollation))
    (when defaultEncryptionConfiguration
      (throw (Exception. "unimplemented")))
    (when defaultPartitionExpirationMs
      (.setDefaultPartitionExpirationMs builder (long defaultPartitionExpirationMs)))
    (when defaultTableLifeTime
      (.setDefaultTableLifetime builder (long defaultTableLifeTime)))
    (when description
      (.setDescription builder description))
    (when externalDatasetReference
      (throw (Exception. "unimplemented")))
    (when friendlyName
      (.setFriendlyName builder friendlyName))
    (when labels
      (.setLabels builder labels))
    (when location
      (.setLocation builder location))
    (when maxTimeTravelHours
      (.setMaxTimeTravelHours builder (long maxTimeTravelHours)))
    (when storageBillingModel
      (.setStorageBillingModel builder storageBillingModel))
    (.build builder)))

(defn to-edn [^DatasetInfo arg]
  {:post [(global/strict! :gcp/bigquery.DatasetInfo %)]}
  (cond-> {:datasetId (DatasetId/to-edn (.getDatasetId arg))
           :location (.getLocation arg)}

          (.getCreationTime arg)
          (assoc :creationTime (.getCreationTime arg))

          (.getLastModified arg)
          (assoc :lastModified (.getLastModified arg))

          (pos? (count (.getAcl arg)))
          (assoc :acl (into #{} (map Acl/to-edn) (.getAcl arg)))

          (some? (.getDefaultCollation arg))
          (assoc :defaultCollation (.getDefaultCollation arg))

          (some? (.getDefaultEncryptionConfiguration arg))
          (assoc :defaultEncryptionConfiguration (EncryptionConfiguration/to-edn (.getDefaultEncryptionConfiguration arg)))

          (some? (.getDefaultPartitionExpirationMs arg))
          (assoc :defaultPartitionExpirationMs (.getDefaultPartitionExpirationMs arg))

          (some? (.getDefaultTableLifetime arg))
          (assoc :defaultTableLifetime (.getDefaultTableLifetime arg))

          (some? (.getDescription arg))
          (assoc :description (.getDescription arg))

          (some? (.getEtag arg))
          (assoc :etag (.getEtag arg))

          (some? (.getExternalDatasetReference arg))
          (assoc :externalDatasetReference (ExternalDatasetReference/to-edn (.getExternalDatasetReference arg)))

          (some? (.getFriendlyName arg))
          (assoc :friendlyName (.getFriendlyName arg))

          (some? (.getGeneratedId arg))
          (assoc :generatedId (.getGeneratedId arg))

          (seq (.getLabels arg))
          (assoc :labels (into {} (.getLabels arg)))

          (some? (.getMaxTimeTravelHours arg))
          (assoc :maxTimeTravelHours (.getMaxTimeTravelHours arg))

          (some? (.getStorageBillingModel arg))
          (assoc :storageBillingModel (.getStorageBillingModel arg))))