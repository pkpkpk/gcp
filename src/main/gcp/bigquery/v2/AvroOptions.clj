(ns gcp.bigquery.v2.AvroOptions
  (:import [com.google.cloud.bigquery AvroOptions])
  (:require [gcp.global :as g]))

(defn ^AvroOptions from-edn
  [arg]
  (gcp.global/strict! :gcp/bigquery.AvroOptions arg)
  (let [builder (AvroOptions/newBuilder)]
    (when (get arg :useAvroLogicalTypes)
      (.setUseAvroLogicalTypes builder (get arg :useAvroLogicalTypes)))
    (.build builder)))

(defn to-edn
  [^AvroOptions arg]
  {:post [(gcp.global/strict! :gcp/bigquery.AvroOptions %)]}
  (cond-> {}
    (get arg :useAvroLogicalTypes) (assoc :useAvroLogicalTypes
                                     (.useAvroLogicalTypes arg))))