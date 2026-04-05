;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.FormatOptions
  {:doc
     "Base class for Google BigQuery format options. These class define the format of external data\nused by BigQuery, for either federated tables or load jobs.\n\n<p>Load jobs support the following formats: AVRO, CSV, DATASTORE_BACKUP, GOOGLE_SHEETS, JSON,\nORC, PARQUET\n\n<p>Federated tables can be defined against following formats: AVRO, BIGTABLE, CSV,\nDATASTORE_BACKUP, GOOGLE_SHEETS, JSON"
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.FormatOptions"
   :gcp.dev/certification
     {:base-seed 1775130852342
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1775130852342 :standard 1775130852343 :stress 1775130852344}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:54:13.560725480Z"}}
  (:require [gcp.bigquery.AvroOptions :as AvroOptions]
            [gcp.bigquery.BigtableOptions :as BigtableOptions]
            [gcp.bigquery.CsvOptions :as CsvOptions]
            [gcp.bigquery.DatastoreBackupOptions :as DatastoreBackupOptions]
            [gcp.bigquery.GoogleSheetsOptions :as GoogleSheetsOptions]
            [gcp.bigquery.ParquetOptions :as ParquetOptions]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery FormatOptions]))

(declare from-edn to-edn)

(defn ^FormatOptions from-edn
  [arg]
  (global/strict! :gcp.bigquery/FormatOptions arg)
  (case (get arg :type)
    "ORC" (FormatOptions/orc)
    "ICEBERG" (FormatOptions/iceberg)
    "NEWLINE_DELIMITED_JSON" (FormatOptions/json)
    "DATASTORE_BACKUP" (DatastoreBackupOptions/from-edn arg)
    "BIGTABLE" (BigtableOptions/from-edn arg)
    "CSV" (CsvOptions/from-edn arg)
    "PARQUET" (ParquetOptions/from-edn arg)
    "AVRO" (AvroOptions/from-edn arg)
    "GOOGLE_SHEETS" (GoogleSheetsOptions/from-edn arg)))

(defn to-edn
  [^FormatOptions arg]
  {:post [(global/strict! :gcp.bigquery/FormatOptions %)]}
  (when arg
    (case (.getType arg)
      "ORC" {:type "ORC"}
      "ICEBERG" {:type "ICEBERG"}
      "NEWLINE_DELIMITED_JSON" {:type "NEWLINE_DELIMITED_JSON"}
      "DATASTORE_BACKUP" (DatastoreBackupOptions/to-edn arg)
      "BIGTABLE" (BigtableOptions/to-edn arg)
      "CSV" (CsvOptions/to-edn arg)
      "PARQUET" (ParquetOptions/to-edn arg)
      "AVRO" (AvroOptions/to-edn arg)
      "GOOGLE_SHEETS" (GoogleSheetsOptions/to-edn arg))))

(def schema
  [:or
   {:closed true,
    :doc
      "Base class for Google BigQuery format options. These class define the format of external data\nused by BigQuery, for either federated tables or load jobs.\n\n<p>Load jobs support the following formats: AVRO, CSV, DATASTORE_BACKUP, GOOGLE_SHEETS, JSON,\nORC, PARQUET\n\n<p>Federated tables can be defined against following formats: AVRO, BIGTABLE, CSV,\nDATASTORE_BACKUP, GOOGLE_SHEETS, JSON",
    :gcp/category :union-concrete,
    :gcp/key :gcp.bigquery/FormatOptions} :gcp.bigquery/DatastoreBackupOptions
   :gcp.bigquery/CsvOptions :gcp.bigquery/GoogleSheetsOptions
   [:map {:closed true, :doc "Default options for the ORC format."}
    [:type [:= "ORC"]]] :gcp.bigquery/BigtableOptions
   :gcp.bigquery/ParquetOptions :gcp.bigquery/AvroOptions
   [:map
    {:closed true, :doc "Default options for the Apache Iceberg table format."}
    [:type [:= "ICEBERG"]]]
   [:map
    {:closed true, :doc "Default options for NEWLINE_DELIMITED_JSON format."}
    [:type [:= "NEWLINE_DELIMITED_JSON"]]]])

(global/include-schema-registry! (with-meta {:gcp.bigquery/FormatOptions schema}
                                   {:gcp.global/name
                                      "gcp.bigquery.FormatOptions"}))