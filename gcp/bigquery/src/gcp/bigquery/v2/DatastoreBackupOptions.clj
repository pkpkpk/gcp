(ns gcp.bigquery.v2.DatastoreBackupOptions
  (:import [com.google.cloud.bigquery DatastoreBackupOptions])
  (:require [gcp.global :as g]))

(defn ^DatastoreBackupOptions from-edn
  [arg]
  (gcp.global/strict! :gcp/bigquery.DatastoreBackupOptions arg)
  (let [builder (DatastoreBackupOptions/newBuilder)]
    (when (get arg :projectionFields)
      (.setProjectionFields builder (get arg :projectionFields)))
    (.build builder)))

(defn to-edn
  [^DatastoreBackupOptions arg]
  {:post [(gcp.global/strict! :gcp/bigquery.DatastoreBackupOptions %)]}
  (cond-> {}
    (get arg :projectionFields) (assoc :projectionFields
                                  (.getProjectionFields arg))))