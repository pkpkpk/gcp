;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.AvroOptions
  {:doc
     "Google BigQuery options for AVRO format. This class wraps some properties of AVRO files used by\nBigQuery to parse external data."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.AvroOptions"
   :gcp.dev/certification
     {:base-seed 1776499326150
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1776499326150 :standard 1776499326151 :stress 1776499326152}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:02:07.492020300Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery AvroOptions AvroOptions$Builder]))

(declare from-edn to-edn)

(defn ^AvroOptions from-edn
  [arg]
  (global/strict! :gcp.bigquery/AvroOptions arg)
  (let [builder (AvroOptions/newBuilder)]
    (when (some? (get arg :useAvroLogicalTypes))
      (.setUseAvroLogicalTypes builder (get arg :useAvroLogicalTypes)))
    (.build builder)))

(defn to-edn
  [^AvroOptions arg]
  {:post [(global/strict! :gcp.bigquery/AvroOptions %)]}
  (when arg
    (cond-> {:type "AVRO"}
      (.useAvroLogicalTypes arg) (assoc :useAvroLogicalTypes
                                   (.useAvroLogicalTypes arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Google BigQuery options for AVRO format. This class wraps some properties of AVRO files used by\nBigQuery to parse external data.",
    :gcp/category :variant-accessor,
    :gcp/key :gcp.bigquery/AvroOptions} [:type [:= "AVRO"]]
   [:useAvroLogicalTypes
    {:optional true,
     :getter-doc
       "Returns whether BigQuery should interpret logical types as the corresponding BigQuery data type\n(for example, TIMESTAMP), instead of using the raw type (for example, INTEGER).",
     :setter-doc
       "[Optional] Sets whether BigQuery should interpret logical types as the corresponding BigQuery\ndata type (for example, TIMESTAMP), instead of using the raw type (for example, INTEGER)."}
    :boolean]])

(global/include-schema-registry! (with-meta {:gcp.bigquery/AvroOptions schema}
                                   {:gcp.global/name
                                      "gcp.bigquery.AvroOptions"}))