(ns gcp.bigquery.v2.LoadJobConfiguration
  (:import [com.google.cloud.bigquery LoadJobConfiguration])
  (:require
    [gcp.bigquery.v2.Clustering :as Clustering]
    [gcp.bigquery.v2.ConnectionProperty :as ConnectionProperty]
    [gcp.bigquery.v2.CsvOptions :as CsvOptions]
    [gcp.bigquery.v2.DatastoreBackupOptions :as DatastoreBackupOptions]
    [gcp.bigquery.v2.EncryptionConfiguration :as EncryptionConfiguration]
    [gcp.bigquery.v2.FormatOptions :as FormatOptions]
    [gcp.bigquery.v2.HivePartitioningOptions :as HivePartitioningOptions]
    ;[gcp.bigquery.v2.JobInfo :as JobInfo]
    [gcp.bigquery.v2.ParquetOptions :as ParquetOptions]
    [gcp.bigquery.v2.RangePartitioning :as RangePartitioning]
    [gcp.bigquery.v2.Schema :as Schema]
    [gcp.bigquery.v2.TableId :as TableId]
    [gcp.bigquery.v2.TimePartitioning :as TimePartitioning]
    [gcp.global :as g]))

(defn ^LoadJobConfiguration from-edn [arg] (throw (Exception. "unimplemented")))

(defn to-edn [^LoadJobConfiguration arg] (throw (Exception. "unimplemented")))