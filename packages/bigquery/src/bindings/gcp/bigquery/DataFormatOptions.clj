;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.DataFormatOptions
  {:doc
     "Google BigQuery DataFormatOptions. Configures the output format for data types returned from\nBigQuery."
   :file-git-sha "b54cd633d7898dd3cf5e11f2ba3892f7b9a86553"
   :fqcn "com.google.cloud.bigquery.DataFormatOptions"
   :gcp.dev/certification
     {:base-seed 1790034168607
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034168607 :standard 1790034168608 :stress 1790034168609}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:42:49.664153156Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery DataFormatOptions
            DataFormatOptions$Builder
            DataFormatOptions$TimestampFormatOptions]))

(declare from-edn
         to-edn
         TimestampFormatOptions-from-edn
         TimestampFormatOptions-to-edn)

(def TimestampFormatOptions-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.bigquery/DataFormatOptions.TimestampFormatOptions}
   "TIMESTAMP_OUTPUT_FORMAT_UNSPECIFIED" "FLOAT64" "INT64" "ISO8601_STRING"])

(defn ^DataFormatOptions from-edn
  [arg]
  (global/strict! :gcp.bigquery/DataFormatOptions arg)
  (let [builder (DataFormatOptions/newBuilder)]
    (when (some? (get arg :timestampFormatOptions))
      (.timestampFormatOptions builder
                               (DataFormatOptions$TimestampFormatOptions/valueOf
                                 (get arg :timestampFormatOptions))))
    (when (some? (get arg :useInt64Timestamp))
      (.useInt64Timestamp builder (get arg :useInt64Timestamp)))
    (.build builder)))

(defn to-edn
  [^DataFormatOptions arg]
  {:post [(global/strict! :gcp.bigquery/DataFormatOptions %)]}
  (when arg
    (cond-> {:timestampFormatOptions (.name (.timestampFormatOptions arg)),
             :useInt64Timestamp (.useInt64Timestamp arg)})))

(def schema
  [:map
   {:closed true,
    :doc
      "Google BigQuery DataFormatOptions. Configures the output format for data types returned from\nBigQuery.",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/DataFormatOptions}
   [:timestampFormatOptions {:getter-doc nil, :setter-doc nil}
    [:enum {:closed true} "TIMESTAMP_OUTPUT_FORMAT_UNSPECIFIED" "FLOAT64"
     "INT64" "ISO8601_STRING"]]
   [:useInt64Timestamp {:getter-doc nil, :setter-doc nil} :boolean]])

(global/include-registry!
  "gcp.bigquery.DataFormatOptions"
  {:gcp.bigquery/DataFormatOptions schema,
   :gcp.bigquery/DataFormatOptions.TimestampFormatOptions
     TimestampFormatOptions-schema})