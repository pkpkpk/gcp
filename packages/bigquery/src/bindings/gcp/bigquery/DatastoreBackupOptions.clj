;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.DatastoreBackupOptions
  {:doc "Google BigQuery options for Cloud Datastore backup."
   :file-git-sha "36af39e222ccf992d15ddbf82148b98e377b40f3"
   :fqcn "com.google.cloud.bigquery.DatastoreBackupOptions"
   :gcp.dev/certification
     {:base-seed 1790034046461
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034046461 :standard 1790034046462 :stress 1790034046463}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:40:47.860465427Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery DatastoreBackupOptions
            DatastoreBackupOptions$Builder]))

(declare from-edn to-edn)

(defn ^DatastoreBackupOptions from-edn
  [arg]
  (global/strict! :gcp.bigquery/DatastoreBackupOptions arg)
  (let [builder (DatastoreBackupOptions/newBuilder)]
    (when (some? (get arg :projectionFields))
      (.setProjectionFields builder (seq (get arg :projectionFields))))
    (.build builder)))

(defn to-edn
  [^DatastoreBackupOptions arg]
  {:post [(global/strict! :gcp.bigquery/DatastoreBackupOptions %)]}
  (when arg
    (cond-> {:type "DATASTORE_BACKUP"}
      (seq (.getProjectionFields arg)) (assoc :projectionFields
                                         (seq (.getProjectionFields arg))))))

(def schema
  [:map
   {:closed true,
    :doc "Google BigQuery options for Cloud Datastore backup.",
    :gcp/category :variant-accessor,
    :gcp/key :gcp.bigquery/DatastoreBackupOptions}
   [:type [:= "DATASTORE_BACKUP"]]
   [:projectionFields
    {:optional true,
     :getter-doc
       "Returns the value of which entity properties to load into BigQuery from a Cloud Datastore\nbackup.",
     :setter-doc
       "Sets which entity properties to load into BigQuery from a Cloud Datastore backup. Property\nnames are case sensitive and must be top-level properties. If no properties are specified,\nBigQuery loads all properties. If any named property isn't found in the Cloud Datastore\nbackup, an invalid error is returned in the job result."}
    [:sequential {:min 1, :gen/max 2} [:string {:min 1, :gen/max 1}]]]])

(global/include-registry! "gcp.bigquery.DatastoreBackupOptions"
                          {:gcp.bigquery/DatastoreBackupOptions schema})