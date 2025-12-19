(ns gcp.bigquery.v2.DatastoreBackupOptions
  (:import [com.google.cloud.bigquery DatastoreBackupOptions])
  (:require [gcp.global :as global]))

(defn ^DatastoreBackupOptions from-edn
  [arg]
  (global/strict! :gcp.bigquery.v2/DatastoreBackupOptions arg)
  (let [builder (DatastoreBackupOptions/newBuilder)]
    (when (get arg :projectionFields)
      (.setProjectionFields builder (get arg :projectionFields)))
    (.build builder)))

(defn to-edn
  [^DatastoreBackupOptions arg]
  {:post [(global/strict! :gcp.bigquery.v2/DatastoreBackupOptions %)]}
  (cond-> {}
    (get arg :projectionFields) (assoc :projectionFields
                                  (.getProjectionFields arg))))

(def schemas
  {:gcp.bigquery.v2/DatastoreBackupOptions
   [:map {:closed true}
    [:type {:optional true} [:= "DATASTORE_BACKUP"]]
    [:projectionFields {:optional true} [:sequential :string]]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))