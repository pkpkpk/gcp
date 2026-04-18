;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.BigtableColumnFamily
  {:doc
     "List of column families to expose in the table schema along with their types. This list restricts\nthe column families that can be referenced in queries and specifies their value types.\n\n<p>You can use this list to do type conversions - see the 'type' field for more details. If you\nleave this list empty, all column families are present in the table schema and their values are\nread as BYTES. During a query only the column families referenced in that query are read from\nBigtable."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.BigtableColumnFamily"
   :gcp.dev/certification
     {:base-seed 1776499329398
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1776499329398 :standard 1776499329399 :stress 1776499329400}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:02:10.885542862Z"}}
  (:require [gcp.bigquery.BigtableColumn :as BigtableColumn]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery BigtableColumnFamily
            BigtableColumnFamily$Builder]))

(declare from-edn to-edn)

(defn ^BigtableColumnFamily from-edn
  [arg]
  (global/strict! :gcp.bigquery/BigtableColumnFamily arg)
  (let [builder (BigtableColumnFamily/newBuilder)]
    (when (seq (get arg :columns))
      (.setColumns builder (mapv BigtableColumn/from-edn (get arg :columns))))
    (when (some? (get arg :encoding))
      (.setEncoding builder (get arg :encoding)))
    (when (some? (get arg :familyID))
      (.setFamilyID builder (get arg :familyID)))
    (when (some? (get arg :onlyReadLatest))
      (.setOnlyReadLatest builder (get arg :onlyReadLatest)))
    (when (some? (get arg :type)) (.setType builder (get arg :type)))
    (.build builder)))

(defn to-edn
  [^BigtableColumnFamily arg]
  {:post [(global/strict! :gcp.bigquery/BigtableColumnFamily %)]}
  (when arg
    (cond-> {:columns (mapv BigtableColumn/to-edn (.getColumns arg)),
             :encoding (.getEncoding arg),
             :familyID (.getFamilyID arg),
             :onlyReadLatest (.getOnlyReadLatest arg),
             :type (.getType arg)})))

(def schema
  [:map
   {:closed true,
    :doc
      "List of column families to expose in the table schema along with their types. This list restricts\nthe column families that can be referenced in queries and specifies their value types.\n\n<p>You can use this list to do type conversions - see the 'type' field for more details. If you\nleave this list empty, all column families are present in the table schema and their values are\nread as BYTES. During a query only the column families referenced in that query are read from\nBigtable.",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/BigtableColumnFamily}
   [:columns
    {:getter-doc nil,
     :setter-doc
       "Lists of columns that should be exposed as individual fields as opposed to a list of (column\nname, value) pairs. All columns whose qualifier matches a qualifier in this list can be\naccessed as .. Other columns can be accessed as a list through .Column field."}
    [:sequential {:min 1} :gcp.bigquery/BigtableColumn]]
   [:encoding
    {:getter-doc nil,
     :setter-doc
       "The encoding of the values when the type is not STRING.\n\n<p>Acceptable encoding values are: TEXT - indicates values are alphanumeric text strings.\nBINARY - indicates values are encoded using HBase Bytes.toBytes family of functions.\n\n<p>This can be overridden for a specific column by listing that column in 'columns' and\nspecifying an encoding for it."}
    [:string {:min 1}]]
   [:familyID {:getter-doc nil, :setter-doc "Identifier of the column family."}
    [:string {:min 1}]]
   [:onlyReadLatest
    {:getter-doc nil,
     :setter-doc
       "If true, only the latest version of values are exposed for all columns in this column family.\nThis can be overridden for a specific column by listing that column in 'columns' and\nspecifying a different setting for that column."}
    :boolean]
   [:type
    {:getter-doc nil,
     :setter-doc
       "The type to convert the value in cells of this column family. The values are expected to be\nencoded using HBase Bytes.toBytes function when using the BINARY encoding value.\n\n<p>Following BigQuery types are allowed (case-sensitive): BYTES STRING INTEGER FLOAT BOOLEAN.\n\n<p>The default type is BYTES. This can be overridden for a specific column by listing that\ncolumn in 'columns' and specifying a type for it."}
    [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/BigtableColumnFamily schema}
    {:gcp.global/name "gcp.bigquery.BigtableColumnFamily"}))