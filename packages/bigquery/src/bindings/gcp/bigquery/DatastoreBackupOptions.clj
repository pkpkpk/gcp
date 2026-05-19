;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.DatastoreBackupOptions
  {:doc "Google BigQuery options for Cloud Datastore backup."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.DatastoreBackupOptions"
   :gcp.dev/certification
     {:base-seed 1779204633252
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1779204633252 :standard 1779204633253 :stress 1779204633254}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-05-19T15:30:34.050756901Z"}}
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

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/DatastoreBackupOptions schema}
    {:gcp.global/name "gcp.bigquery.DatastoreBackupOptions"}))