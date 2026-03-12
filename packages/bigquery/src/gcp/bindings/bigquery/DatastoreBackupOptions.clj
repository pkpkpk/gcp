;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.DatastoreBackupOptions
  {:doc "Google BigQuery options for Cloud Datastore backup."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.DatastoreBackupOptions"
   :gcp.dev/certification
     {:base-seed 1771346799820
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771346799820 :standard 1771346799821 :stress 1771346799822}
      :protocol-hash
        "32175171656ac95c72455bff25fe019346fb52c916efee805be149692e31cec4"
      :timestamp "2026-02-17T16:46:39.856834168Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery DatastoreBackupOptions
            DatastoreBackupOptions$Builder]))

(defn ^DatastoreBackupOptions from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/DatastoreBackupOptions arg)
  (let [builder (DatastoreBackupOptions/newBuilder)]
    (when (some? (get arg :projectionFields))
      (.setProjectionFields builder (seq (get arg :projectionFields))))
    (.build builder)))

(defn to-edn
  [^DatastoreBackupOptions arg]
  {:post [(global/strict! :gcp.bindings.bigquery/DatastoreBackupOptions %)]}
  (cond-> {:type "DATASTORE_BACKUP"}
    (.getProjectionFields arg) (assoc :projectionFields
                                 (seq (.getProjectionFields arg)))))

(def schema
  [:map
   {:closed true,
    :doc "Google BigQuery options for Cloud Datastore backup.",
    :gcp/category :variant-accessor,
    :gcp/key :gcp.bindings.bigquery/DatastoreBackupOptions}
   [:type [:= "DATASTORE_BACKUP"]]
   [:projectionFields
    {:optional true,
     :getter-doc
       "Returns the value of which entity properties to load into BigQuery from a Cloud Datastore\nbackup.",
     :setter-doc
       "Sets which entity properties to load into BigQuery from a Cloud Datastore backup. Property\nnames are case sensitive and must be top-level properties. If no properties are specified,\nBigQuery loads all properties. If any named property isn't found in the Cloud Datastore\nbackup, an invalid error is returned in the job result."}
    [:seqable {:min 1} [:string {:min 1}]]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/DatastoreBackupOptions schema}
    {:gcp.global/name "gcp.bindings.bigquery.DatastoreBackupOptions"}))