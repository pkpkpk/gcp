(ns gcp.bigquery.v2.BigtableColumnFamily
  (:import [com.google.cloud.bigquery BigtableColumnFamily])
  (:require [gcp.bigquery.v2.BigtableColumn :as BigtableColumn]
            [gcp.global :as g]))

(defn ^BigtableColumnFamily from-edn
  [arg]
  (gcp.global/strict! :gcp/bigquery.BigtableColumnFamily arg)
  (let [builder (BigtableColumnFamily/newBuilder)]
    (when (get arg :columns)
      (.setColumns builder (map BigtableColumn/from-edn (get arg :columns))))
    (when (get arg :encoding) (.setEncoding builder (get arg :encoding)))
    (when (get arg :familyID) (.setFamilyID builder (get arg :familyID)))
    (when (get arg :onlyReadLatest)
      (.setOnlyReadLatest builder (get arg :onlyReadLatest)))
    (when (get arg :type) (.setType builder (get arg :type)))
    (.build builder)))

(defn to-edn
  [^BigtableColumnFamily arg]
  {:post [(gcp.global/strict! :gcp/bigquery.BigtableColumnFamily %)]}
  (cond-> {}
    (get arg :columns) (assoc :columns
                         (mapv BigtableColumn/to-edn (.getColumns arg)))
    (get arg :encoding) (assoc :encoding (.getEncoding arg))
    (get arg :familyID) (assoc :familyID (.getFamilyID arg))
    (get arg :onlyReadLatest) (assoc :onlyReadLatest (.getOnlyReadLatest arg))
    (get arg :type) (assoc :type (.getType arg))))