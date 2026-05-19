;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.DataFormatOptions
  {:doc
     "Google BigQuery DataFormatOptions. Configures the output format for data types returned from\nBigQuery."
   :file-git-sha "6dcc90053353422ae766e531413b3ecc65b8b155"
   :fqcn "com.google.cloud.bigquery.DataFormatOptions"
   :gcp.dev/certification
     {:base-seed 1779204722891
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1779204722891 :standard 1779204722892 :stress 1779204722893}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-05-19T15:32:03.757875087Z"}}
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