;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.FormatOptions
  {:doc
     "Base class for Google BigQuery format options. These class define the format of external data\nused by BigQuery, for either federated tables or load jobs.\n\n<p>Load jobs support the following formats: AVRO, CSV, DATASTORE_BACKUP, GOOGLE_SHEETS, JSON,\nORC, PARQUET\n\n<p>Federated tables can be defined against following formats: AVRO, BIGTABLE, CSV,\nDATASTORE_BACKUP, GOOGLE_SHEETS, JSON"
   :file-git-sha "e8841639b4b2fac7b3b2ce601ec4b50cb354a9e1"
   :fqcn "com.google.cloud.bigquery.FormatOptions"
   :gcp.dev/certification
     {:base-seed 1790034052014
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034052014 :standard 1790034052015 :stress 1790034052016}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:40:53.338381439Z"}}
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

(global/include-registry! "gcp.bigquery.FormatOptions"
                          {:gcp.bigquery/FormatOptions schema})