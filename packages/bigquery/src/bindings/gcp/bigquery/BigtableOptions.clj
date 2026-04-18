;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.BigtableOptions
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.BigtableOptions"
   :gcp.dev/certification
     {:base-seed 1776499331300
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1776499331300 :standard 1776499331301 :stress 1776499331302}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:02:12.880052801Z"}}
  (:require [gcp.bigquery.BigtableColumnFamily :as BigtableColumnFamily]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery BigtableOptions BigtableOptions$Builder]))

(declare from-edn to-edn)

(defn ^BigtableOptions from-edn
  [arg]
  (global/strict! :gcp.bigquery/BigtableOptions arg)
  (let [builder (BigtableOptions/newBuilder)]
    (when (some? (get arg :columnFamilies))
      (.setColumnFamilies builder
                          (mapv BigtableColumnFamily/from-edn
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
  {:post [(global/strict! :gcp.bigquery/BigtableOptions %)]}
  (when arg
    (cond-> {:type "BIGTABLE"}
      (seq (.getColumnFamilies arg)) (assoc :columnFamilies
                                       (mapv BigtableColumnFamily/to-edn
                                         (.getColumnFamilies arg)))
      (.getIgnoreUnspecifiedColumnFamilies arg)
        (assoc :ignoreUnspecifiedColumnFamilies
          (.getIgnoreUnspecifiedColumnFamilies arg))
      (.getReadRowkeyAsString arg) (assoc :readRowkeyAsString
                                     (.getReadRowkeyAsString arg)))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :variant-accessor,
    :gcp/key :gcp.bigquery/BigtableOptions} [:type [:= "BIGTABLE"]]
   [:columnFamilies
    {:optional true,
     :setter-doc
       "List of column families to expose in the table schema along with their types.\n\n<p>This list restricts the column families that can be referenced in queries and specifies\ntheir value types. You can use this list to do type conversions - see the 'type' field for\nmore details. If you leave this list empty, all column families are present in the table\nschema and their values are read as BYTES. During a query only the column families referenced\nin that query are read from Bigtable."}
    [:sequential {:min 1} :gcp.bigquery/BigtableColumnFamily]]
   [:ignoreUnspecifiedColumnFamilies
    {:optional true,
     :setter-doc
       "If field is true, then the column families that are not specified in columnFamilies list are\nnot exposed in the table schema. Otherwise, they are read with BYTES type values. The default\nvalue is false."}
    :boolean]
   [:readRowkeyAsString
    {:optional true,
     :setter-doc
       "If readRowkeyAsString is true, then the rowkey column families will be read and converted to\nstring. Otherwise they are read with BYTES type values and users need to manually cast them\nwith CAST if necessary. The default value is false."}
    :boolean]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/BigtableOptions schema}
    {:gcp.global/name "gcp.bigquery.BigtableOptions"}))