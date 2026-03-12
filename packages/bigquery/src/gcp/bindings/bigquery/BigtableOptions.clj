;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.BigtableOptions
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.BigtableOptions"
   :gcp.dev/certification
     {:base-seed 1771081750536
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771081750536 :standard 1771081750537 :stress 1771081750538}
      :protocol-hash
        "289ef1a932bafc0fa192695722ee393d771f421c462b8729c9ca1661a5b101d4"
      :timestamp "2026-02-14T15:09:11.759199281Z"}}
  (:require [gcp.bindings.bigquery.BigtableColumnFamily :as
             BigtableColumnFamily]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery BigtableOptions BigtableOptions$Builder]))

(defn ^BigtableOptions from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/BigtableOptions arg)
  (let [builder (BigtableOptions/newBuilder)]
    (when (some? (get arg :columnFamilies))
      (.setColumnFamilies builder
                          (map BigtableColumnFamily/from-edn
                            (get arg :columnFamilies))))
    (when (some? (get arg :ignoreUnspecifiedColumnFamilies))
      (.setIgnoreUnspecifiedColumnFamilies
        builder
        (get arg :ignoreUnspecifiedColumnFamilies)))
    (when (some? (get arg :readRowkeyAsString))
      (.setReadRowkeyAsString builder (get arg :readRowkeyAsString)))
    (.build builder)))

(defn to-edn
  [^BigtableOptions arg]
  {:post [(global/strict! :gcp.bindings.bigquery/BigtableOptions %)]}
  (cond-> {:type (.getType arg)}
    (.getColumnFamilies arg) (assoc :columnFamilies
                               (map BigtableColumnFamily/to-edn
                                 (.getColumnFamilies arg)))
    (.getIgnoreUnspecifiedColumnFamilies arg)
      (assoc :ignoreUnspecifiedColumnFamilies
        (.getIgnoreUnspecifiedColumnFamilies arg))
    (.getReadRowkeyAsString arg) (assoc :readRowkeyAsString
                                   (.getReadRowkeyAsString arg))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :variant-accessor,
    :gcp/key :gcp.bindings.bigquery/BigtableOptions}
   [:columnFamilies
    {:optional true,
     :setter-doc
       "List of column families to expose in the table schema along with their types.\n\n<p>This list restricts the column families that can be referenced in queries and specifies\ntheir value types. You can use this list to do type conversions - see the 'type' field for\nmore details. If you leave this list empty, all column families are present in the table\nschema and their values are read as BYTES. During a query only the column families referenced\nin that query are read from Bigtable."}
    [:seqable {:min 1} :gcp.bindings.bigquery/BigtableColumnFamily]]
   [:ignoreUnspecifiedColumnFamilies
    {:optional true,
     :setter-doc
       "If field is true, then the column families that are not specified in columnFamilies list are\nnot exposed in the table schema. Otherwise, they are read with BYTES type values. The default\nvalue is false."}
    :boolean]
   [:readRowkeyAsString
    {:optional true,
     :setter-doc
       "If readRowkeyAsString is true, then the rowkey column families will be read and converted to\nstring. Otherwise they are read with BYTES type values and users need to manually cast them\nwith CAST if necessary. The default value is false."}
    :boolean]
   [:type
    {:read-only? true,
     :getter-doc "Returns the external data format, as a string."}
    [:= "BIGTABLE"]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/BigtableOptions schema}
    {:gcp.global/name "gcp.bindings.bigquery.BigtableOptions"}))