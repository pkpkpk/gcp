;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.FormatOptions
  {:doc
     "Base class for Google BigQuery format options. These class define the format of external data\nused by BigQuery, for either federated tables or load jobs.\n\n<p>Load jobs support the following formats: AVRO, CSV, DATASTORE_BACKUP, GOOGLE_SHEETS, JSON,\nORC, PARQUET\n\n<p>Federated tables can be defined against following formats: AVRO, BIGTABLE, CSV,\nDATASTORE_BACKUP, GOOGLE_SHEETS, JSON"
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.FormatOptions"
   :gcp.dev/certification
     {:base-seed 1771347324545
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771347324545 :standard 1771347324546 :stress 1771347324547}
      :protocol-hash
        "32175171656ac95c72455bff25fe019346fb52c916efee805be149692e31cec4"
      :timestamp "2026-02-17T16:55:24.794682861Z"}}
  (:require [gcp.bindings.bigquery.AvroOptions :as AvroOptions]
            [gcp.bindings.bigquery.BigtableOptions :as BigtableOptions]
            [gcp.bindings.bigquery.CsvOptions :as CsvOptions]
            [gcp.bindings.bigquery.DatastoreBackupOptions :as
             DatastoreBackupOptions]
            [gcp.bindings.bigquery.GoogleSheetsOptions :as GoogleSheetsOptions]
            [gcp.bindings.bigquery.ParquetOptions :as ParquetOptions]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery FormatOptions]))

(defn ^FormatOptions from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/FormatOptions arg)
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
  {:post [(global/strict! :gcp.bindings.bigquery/FormatOptions %)]}
  (case (.getType arg)
    "ORC" {:type "ORC"}
    "ICEBERG" {:type "ICEBERG"}
    "NEWLINE_DELIMITED_JSON" {:type "NEWLINE_DELIMITED_JSON"}
    "DATASTORE_BACKUP" (DatastoreBackupOptions/to-edn arg)
    "BIGTABLE" (BigtableOptions/to-edn arg)
    "CSV" (CsvOptions/to-edn arg)
    "PARQUET" (ParquetOptions/to-edn arg)
    "AVRO" (AvroOptions/to-edn arg)
    "GOOGLE_SHEETS" (GoogleSheetsOptions/to-edn arg)))

(def schema
  [:or
   {:closed true,
    :doc
      "Base class for Google BigQuery format options. These class define the format of external data\nused by BigQuery, for either federated tables or load jobs.\n\n<p>Load jobs support the following formats: AVRO, CSV, DATASTORE_BACKUP, GOOGLE_SHEETS, JSON,\nORC, PARQUET\n\n<p>Federated tables can be defined against following formats: AVRO, BIGTABLE, CSV,\nDATASTORE_BACKUP, GOOGLE_SHEETS, JSON",
    :gcp/category :union-concrete,
    :gcp/key :gcp.bindings.bigquery/FormatOptions}
   :gcp.bindings.bigquery/DatastoreBackupOptions
   :gcp.bindings.bigquery/CsvOptions :gcp.bindings.bigquery/GoogleSheetsOptions
   [:map {:closed true, :doc "Default options for the ORC format."}
    [:type [:= "ORC"]]] :gcp.bindings.bigquery/BigtableOptions
   :gcp.bindings.bigquery/ParquetOptions :gcp.bindings.bigquery/AvroOptions
   [:map
    {:closed true, :doc "Default options for the Apache Iceberg table format."}
    [:type [:= "ICEBERG"]]]
   [:map
    {:closed true, :doc "Default options for NEWLINE_DELIMITED_JSON format."}
    [:type [:= "NEWLINE_DELIMITED_JSON"]]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/FormatOptions schema}
    {:gcp.global/name "gcp.bindings.bigquery.FormatOptions"}))