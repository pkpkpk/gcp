;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.FormatOptions
  {:doc
     "Base class for Google BigQuery format options. These class define the format of external data\nused by BigQuery, for either federated tables or load jobs.\n\n<p>Load jobs support the following formats: AVRO, CSV, DATASTORE_BACKUP, GOOGLE_SHEETS, JSON,\nORC, PARQUET\n\n<p>Federated tables can be defined against following formats: AVRO, BIGTABLE, CSV,\nDATASTORE_BACKUP, GOOGLE_SHEETS, JSON"
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.FormatOptions"
   :gcp.dev/certification
     {:base-seed 1779204636455
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1779204636455 :standard 1779204636456 :stress 1779204636457}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-05-19T15:30:37.250837663Z"}}
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