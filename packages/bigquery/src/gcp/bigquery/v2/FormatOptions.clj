(ns gcp.bigquery.v2.FormatOptions
  (:import [com.google.cloud.bigquery FormatOptions])
  (:require [gcp.bigquery.v2.AvroOptions :as AvroOptions]
            [gcp.bigquery.v2.BigtableOptions :as BigtableOptions]
            [gcp.bigquery.v2.CsvOptions :as CsvOptions]
            [gcp.bigquery.v2.DatastoreBackupOptions :as DatastoreBackupOptions]
            [gcp.bigquery.v2.GoogleSheetsOptions :as GoogleSheetsOptions]
            [gcp.bigquery.v2.ParquetOptions :as ParquetOptions]
            [gcp.global :as global]))

(defn to-edn
  [^FormatOptions arg]
  {:post [(global/strict! :gcp.bigquery.v2/FormatOptions %)]}
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
  (global/strict! :gcp.bigquery.v2/FormatOptions arg)
  (or
    (and (or (= "AVRO" (get arg :type))
             (global/valid? :gcp.bigquery.v2/AvroOptions arg))
         (AvroOptions/from-edn arg))
    (and (or (= "BIGTABLE" (get arg :type))
             (global/valid? :gcp.bigquery.v2/BigtableOptions arg))
         (BigtableOptions/from-edn arg))
    (and (or (= "CSV" (get arg :type))
             (global/valid? :gcp.bigquery.v2/CsvOptions arg))
         (CsvOptions/from-edn arg))
    (and (or (= "DATASTORE_BACKUP" (get arg :type))
             (global/valid? :gcp.bigquery.v2/DatastoreBackupOptions arg))
         (DatastoreBackupOptions/from-edn arg))
    (and (or (= "GOOGLE_SHEETS" (get arg :type))
             (global/valid? :gcp.bigquery.v2/GoogleSheetsOptions arg))
         (GoogleSheetsOptions/from-edn arg))
    (and (= "ICEBERG" (get arg :type)) (FormatOptions/iceberg))
    (and (= "NEWLINE_DELIMITED_JSON" (get arg :type)) (FormatOptions/json))
    (and (= "ORC" (get arg :type)) (FormatOptions/orc))
    (and (or (= "PARQUET" (get arg :type))
             (global/valid? :gcp.bigquery.v2/ParquetOptions arg))
         (ParquetOptions/from-edn arg))
    (throw
      (clojure.core/ex-info
        "failed to match variant for union com.google.cloud.bigquery.FormatOptions"
        {:arg arg,
         :expected #{"DATASTORE_BACKUP" "CSV" "GOOGLE_SHEETS" "ORC" "BIGTABLE"
                     "PARQUET" "AVRO" "ICEBERG" "NEWLINE_DELIMITED_JSON"}}))))

(def schemas
  {:gcp.bigquery.v2/FormatOptions
   [:or
    {:gcp/type :concrete-union
     :class 'com.google.cloud.bigquery.FormatOptions}
    :gcp.bigquery.v2/AvroOptions
    :gcp.bigquery.v2/BigtableOptions
    :gcp.bigquery.v2/CsvOptions
    :gcp.bigquery.v2/DatastoreBackupOptions
    :gcp.bigquery.v2/GoogleSheetsOptions
    :gcp.bigquery.v2/ParquetOptions
    [:map {:closed true} [:type [:= "ORC"]]]
    [:map {:closed true} [:type [:= "ICEBERG"]]]
    [:map {:closed true} [:type [:= "NEWLINE_DELIMITED_JSON"]]]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))