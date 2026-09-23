;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.AvroOptions
  {:doc
     "Google BigQuery options for AVRO format. This class wraps some properties of AVRO files used by\nBigQuery to parse external data."
   :file-git-sha "8041c3ff309ffbb7ee817d30e6bc9d905929f9d8"
   :fqcn "com.google.cloud.bigquery.AvroOptions"
   :gcp.dev/certification
     {:base-seed 1790034038466
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034038466 :standard 1790034038467 :stress 1790034038468}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:40:39.674049140Z"}}
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

(global/include-registry! "gcp.bigquery.AvroOptions"
                          {:gcp.bigquery/AvroOptions schema})