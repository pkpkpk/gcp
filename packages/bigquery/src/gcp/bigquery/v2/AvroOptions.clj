(ns gcp.bigquery.v2.AvroOptions
  (:import [com.google.cloud.bigquery AvroOptions])
  (:require [gcp.global :as global]))

(defn ^AvroOptions from-edn
  [arg]
  (global/strict! :gcp.bigquery.v2/AvroOptions arg)
  (let [builder (AvroOptions/newBuilder)]
    (when (get arg :useAvroLogicalTypes)
      (.setUseAvroLogicalTypes builder (get arg :useAvroLogicalTypes)))
    (.build builder)))

(defn to-edn
  [^AvroOptions arg]
  {:post [(global/strict! :gcp.bigquery.v2/AvroOptions %)]}
  (cond-> {}
    (get arg :useAvroLogicalTypes) (assoc :useAvroLogicalTypes
                                     (.useAvroLogicalTypes arg))))

(def schemas
  {:gcp.bigquery.v2/AvroOptions
   [:map {:closed true}
    [:type {:optional true} [:= "AVRO"]]
    [:useAvroLogicalTypes {:optional true} :boolean]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))