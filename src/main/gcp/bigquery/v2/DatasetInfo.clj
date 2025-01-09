(ns gcp.bigquery.v2.DatasetInfo
  (:require [gcp.bigquery.v2.Acl :as Acl]
            [gcp.bigquery.v2.DatasetId :as DatasetId]
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
  (global/strict! :bigquery/DatasetInfo arg)
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
