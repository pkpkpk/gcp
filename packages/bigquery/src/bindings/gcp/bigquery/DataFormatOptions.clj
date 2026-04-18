;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.DataFormatOptions
  {:doc
     "Google BigQuery DataFormatOptions. Configures the output format for data types returned from\nBigQuery."
   :file-git-sha "6dcc90053353422ae766e531413b3ecc65b8b155"
   :fqcn "com.google.cloud.bigquery.DataFormatOptions"
   :gcp.dev/certification
     {:base-seed 1776499464054
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1776499464054 :standard 1776499464055 :stress 1776499464056}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:04:25.848338812Z"}}
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

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/DataFormatOptions schema,
              :gcp.bigquery/DataFormatOptions.TimestampFormatOptions
                TimestampFormatOptions-schema}
    {:gcp.global/name "gcp.bigquery.DataFormatOptions"}))