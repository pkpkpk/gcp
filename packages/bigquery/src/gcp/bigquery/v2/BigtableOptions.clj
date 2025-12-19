(ns gcp.bigquery.v2.BigtableOptions
  (:import [com.google.cloud.bigquery BigtableOptions])
  (:require [gcp.bigquery.v2.BigtableColumnFamily :as BigtableColumnFamily]
            [gcp.global :as global]))

(defn ^BigtableOptions from-edn
  [arg]
  (global/strict! :gcp.bigquery.v2/BigtableOptions arg)
  (let [builder (BigtableOptions/newBuilder)]
    (when (get arg :columnFamilies)
      (.setColumnFamilies builder
                          (map BigtableColumnFamily/from-edn
                            (get arg :columnFamilies))))
    (when (get arg :ignoreUnspecifiedColumnFamilies)
      (.setIgnoreUnspecifiedColumnFamilies
        builder
        (get arg :ignoreUnspecifiedColumnFamilies)))
    (when (get arg :readRowkeyAsString)
      (.setReadRowkeyAsString builder (get arg :readRowkeyAsString)))
    (.build builder)))

(defn to-edn
  [^BigtableOptions arg]
  {:post [(global/strict! :gcp.bigquery.v2/BigtableOptions %)]}
  (cond-> {}
    (get arg :columnFamilies) (assoc :columnFamilies
                                (mapv BigtableColumnFamily/to-edn
                                  (.getColumnFamilies arg)))
    (get arg :ignoreUnspecifiedColumnFamilies)
      (assoc :ignoreUnspecifiedColumnFamilies
        (.getIgnoreUnspecifiedColumnFamilies arg))
    (get arg :readRowkeyAsString) (assoc :readRowkeyAsString
                                    (.getReadRowkeyAsString arg))))

(def schemas
  {:gcp.bigquery.v2/BigtableOptions
   [:map {:closed true}
    [:type {:optional true} [:= "BIGTABLE"]]
    [:columnFamilies {:optional true} [:sequential :gcp.bigquery.v2/BigtableColumnFamily]]
    [:ignoreUnspecifiedColumnFamilies {:optional true} :boolean]
    [:readRowkeyAsString {:optional true} :boolean]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))