(ns gcp.bigquery.v2.FormatOptions
  (:import [com.google.cloud.bigquery FormatOptions])
  (:require [gcp.bigquery.v2.AvroOptions :as AvroOptions]
            [gcp.bigquery.v2.BigtableOptions :as BigtableOptions]
            [gcp.bigquery.v2.CsvOptions :as CsvOptions]
            [gcp.bigquery.v2.DatastoreBackupOptions :as DatastoreBackupOptions]
            [gcp.bigquery.v2.GoogleSheetsOptions :as GoogleSheetsOptions]
            [gcp.bigquery.v2.ParquetOptions :as ParquetOptions]
            gcp.global))

(defn to-edn
  [^FormatOptions arg]
  {:post [(gcp.global/strict! :gcp/bigquery.FormatOptions %)]}
  (case (.getType arg)
    "DATASTORE_BACKUP" (assoc (DatastoreBackupOptions/to-edn arg)
                         :type "DATASTORE_BACKUP")
    "CSV" (assoc (CsvOptions/to-edn arg) :type "CSV")
    "GOOGLE_SHEETS" (assoc (GoogleSheetsOptions/to-edn arg)
                      :type "GOOGLE_SHEETS")
    "ORC" {:type "ORC"}
    "BIGTABLE" (assoc (BigtableOptions/to-edn arg) :type "BIGTABLE")
    "PARQUET" (assoc (ParquetOptions/to-edn arg) :type "PARQUET")
    "AVRO" (assoc (AvroOptions/to-edn arg) :type "AVRO")
    "ICEBERG" {:type "ICEBERG"}
    "NEWLINE_DELIMITED_JSON" {:type "NEWLINE_DELIMITED_JSON"}))

(defn ^FormatOptions from-edn
  [arg]
  (gcp.global/strict! :gcp/bigquery.FormatOptions arg)
  (or
    (and (or (= "AVRO" (get arg :type))
             (gcp.global/valid? :gcp/bigquery.AvroOptions arg))
         (AvroOptions/from-edn arg))
    (and (or (= "BIGTABLE" (get arg :type))
             (gcp.global/valid? :gcp/bigquery.BigtableOptions arg))
         (BigtableOptions/from-edn arg))
    (and (or (= "CSV" (get arg :type))
             (gcp.global/valid? :gcp/bigquery.CsvOptions arg))
         (CsvOptions/from-edn arg))
    (and (or (= "DATASTORE_BACKUP" (get arg :type))
             (gcp.global/valid? :gcp/bigquery.DatastoreBackupOptions arg))
         (DatastoreBackupOptions/from-edn arg))
    (and (or (= "GOOGLE_SHEETS" (get arg :type))
             (gcp.global/valid? :gcp/bigquery.GoogleSheetsOptions arg))
         (GoogleSheetsOptions/from-edn arg))
    (and (= "ICEBERG" (get arg :type)) (FormatOptions/iceberg))
    (and (= "NEWLINE_DELIMITED_JSON" (get arg :type)) (FormatOptions/json))
    (and (= "ORC" (get arg :type)) (FormatOptions/orc))
    (and (or (= "PARQUET" (get arg :type))
             (gcp.global/valid? :gcp/bigquery.ParquetOptions arg))
         (ParquetOptions/from-edn arg))
    (throw
      (clojure.core/ex-info
        "failed to match variant for union com.google.cloud.bigquery.FormatOptions"
        {:arg arg,
         :expected #{"DATASTORE_BACKUP" "CSV" "GOOGLE_SHEETS" "ORC" "BIGTABLE"
                     "PARQUET" "AVRO" "ICEBERG" "NEWLINE_DELIMITED_JSON"}}))))