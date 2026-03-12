;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.DataFormatOptions
  {:doc
     "Google BigQuery DataFormatOptions. Configures the output format for data types returned from\nBigQuery."
   :file-git-sha "6dcc90053353422ae766e531413b3ecc65b8b155"
   :fqcn "com.google.cloud.bigquery.DataFormatOptions"
   :gcp.dev/certification
     {:base-seed 1772045867584
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1772045867584 :standard 1772045867585 :stress 1772045867586}
      :protocol-hash
        "b8a5eb17212acb18f49dcb7f15a243eac9b32f54c0f054a99be660a3a25e0315"
      :timestamp "2026-02-25T18:57:47.592059892Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery DataFormatOptions
            DataFormatOptions$Builder
            DataFormatOptions$TimestampFormatOptions]))

(declare DataFormatOptions$TimestampFormatOptions-from-edn
         DataFormatOptions$TimestampFormatOptions-to-edn)

(def DataFormatOptions$TimestampFormatOptions-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.bindings.bigquery/DataFormatOptions.TimestampFormatOptions}
   "TIMESTAMP_OUTPUT_FORMAT_UNSPECIFIED" "FLOAT64" "INT64" "ISO8601_STRING"])

(defn ^DataFormatOptions from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/DataFormatOptions arg)
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
  {:post [(global/strict! :gcp.bindings.bigquery/DataFormatOptions %)]}
  (cond-> {:timestampFormatOptions (.name (.timestampFormatOptions arg)),
           :useInt64Timestamp (.useInt64Timestamp arg)}))

(def schema
  [:map
   {:closed true,
    :doc
      "Google BigQuery DataFormatOptions. Configures the output format for data types returned from\nBigQuery.",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/DataFormatOptions}
   [:timestampFormatOptions {:getter-doc nil, :setter-doc nil}
    [:enum {:closed true} "TIMESTAMP_OUTPUT_FORMAT_UNSPECIFIED" "FLOAT64"
     "INT64" "ISO8601_STRING"]]
   [:useInt64Timestamp {:getter-doc nil, :setter-doc nil} :boolean]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/DataFormatOptions schema,
              :gcp.bindings.bigquery/DataFormatOptions.TimestampFormatOptions
                DataFormatOptions$TimestampFormatOptions-schema}
    {:gcp.global/name "gcp.bindings.bigquery.DataFormatOptions"}))