;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.AvroOptions
  {:doc
     "Google BigQuery options for AVRO format. This class wraps some properties of AVRO files used by\nBigQuery to parse external data."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.AvroOptions"
   :gcp.dev/certification
     {:base-seed 1770997675932
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1770997675932 :standard 1770997675933 :stress 1770997675934}
      :protocol-hash
        "600a262ece6bd21dc98250ea6f25d9fa1a7ab0d8840c5d6ce9608615488fe05f"
      :timestamp "2026-02-13T15:47:55.999015686Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery AvroOptions AvroOptions$Builder]))

(defn ^AvroOptions from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/AvroOptions arg)
  (let [builder (AvroOptions/newBuilder)]
    (when (get arg :useAvroLogicalTypes)
      (.setUseAvroLogicalTypes builder (get arg :useAvroLogicalTypes)))
    (.build builder)))

(defn to-edn
  [^AvroOptions arg]
  {:post [(global/strict! :gcp.bindings.bigquery/AvroOptions %)]}
  (cond-> {:type (.getType arg)}
    (.useAvroLogicalTypes arg) (assoc :useAvroLogicalTypes
                                 (.useAvroLogicalTypes arg))))

(def schema
  [:map
   {:closed true,
    :doc
      "Google BigQuery options for AVRO format. This class wraps some properties of AVRO files used by\nBigQuery to parse external data.",
    :gcp/category :variant-accessor,
    :gcp/key :gcp.bindings.bigquery/AvroOptions}
   [:type
    {:read-only? true,
     :getter-doc "Returns the external data format, as a string."} [:= "AVRO"]]
   [:useAvroLogicalTypes
    {:optional true,
     :getter-doc
       "Returns whether BigQuery should interpret logical types as the corresponding BigQuery data type\n(for example, TIMESTAMP), instead of using the raw type (for example, INTEGER).",
     :setter-doc
       "[Optional] Sets whether BigQuery should interpret logical types as the corresponding BigQuery\ndata type (for example, TIMESTAMP), instead of using the raw type (for example, INTEGER)."}
    :boolean]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/AvroOptions schema}
    {:gcp.global/name "gcp.bindings.bigquery.AvroOptions"}))