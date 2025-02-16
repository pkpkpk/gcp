(ns gcp.bigquery.v2.BigtableOptions
  (:import [com.google.cloud.bigquery BigtableOptions])
  (:require [gcp.bigquery.v2.BigtableColumnFamily :as BigtableColumnFamily]
            [gcp.global :as g]))

(defn ^BigtableOptions from-edn
  [arg]
  (gcp.global/strict! :gcp/bigquery.BigtableOptions arg)
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
  {:post [(gcp.global/strict! :gcp/bigquery.BigtableOptions %)]}
  (cond-> {}
    (get arg :columnFamilies) (assoc :columnFamilies
                                (mapv BigtableColumnFamily/to-edn
                                  (.getColumnFamilies arg)))
    (get arg :ignoreUnspecifiedColumnFamilies)
      (assoc :ignoreUnspecifiedColumnFamilies
        (.getIgnoreUnspecifiedColumnFamilies arg))
    (get arg :readRowkeyAsString) (assoc :readRowkeyAsString
                                    (.getReadRowkeyAsString arg))))