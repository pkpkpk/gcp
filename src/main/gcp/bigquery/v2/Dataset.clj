(ns gcp.bigquery.v2.Dataset
  (:refer-clojure :exclude [get])
  (:require [clojure.string :as string]
            [gcp.bigquery.v2.Acl :as Acl]
            [gcp.bigquery.v2.BigQuery.DatasetOption :as DatasetOption]
            [gcp.bigquery.v2.DatasetId :as DatasetId]
            [gcp.bigquery.v2.DatasetInfo :as DatasetInfo]
            [gcp.bigquery.v2.EncryptionConfiguration :as EncryptionConfiguration]
            [gcp.bigquery.v2.ExternalDatasetReference :as ExternalDatasetReference]
            [gcp.global :as global])
  (:import (com.google.cloud.bigquery Dataset)))

;https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery.Dataset
;https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery.DatasetInfo.Builder

;; Dataset == DatasetInfo (immut record) + client + w/ IO & mutation methods
; Dataset.create(<tableDef>)    ->   sugared bigquery.create(TableInfo)
; Dataset.delete(<tableId>)     ->   sugared bigquery.delete(TableInfo)
; Dataset.list()                ->   sugared bigquery.listTables(datasetId)
; Dataset.reload()              ->   sugared bigquery.get(datasetId)
; Dataset.update()              ->   sugared bigquery

(defn to-edn [^Dataset arg]
  {:post [(global/strict! :bigquery/Dataset %)]}
  (cond-> {:bigquery (.getBigQuery arg)
           :datasetId (DatasetId/to-edn (.getDatasetId arg))
           :location (.getLocation arg)}

          (.getCreationTime arg)
          (assoc :creationTime (.getCreationTime arg))

          (.getLastModified arg)
          (assoc :lastModified (.getLastModified arg))

          (pos? (count (.getAcl arg)))
          (assoc :acl (map Acl/to-edn (.getAcl arg)))

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