(ns gcp.bigquery.v2.ParquetOptions
  (:import [com.google.cloud.bigquery ParquetOptions])
  (:require [gcp.global :as global]))

(defn ^ParquetOptions from-edn
  [arg]
  (global/strict! :gcp.bigquery.v2/ParquetOptions arg)
  (let [builder (ParquetOptions/newBuilder)]
    (when (get arg :enableListInference)
      (.setEnableListInference builder (get arg :enableListInference)))
    (when (get arg :enumAsString)
      (.setEnumAsString builder (get arg :enumAsString)))
    (when (get arg :mapTargetType)
      (.setMapTargetType builder (get arg :mapTargetType)))
    (.build builder)))

(defn to-edn
  [^ParquetOptions arg]
  {:post [(global/strict! :gcp.bigquery.v2/ParquetOptions %)]}
  (cond-> {}
    (get arg :enableListInference) (assoc :enableListInference
                                     (.getEnableListInference arg))
    (get arg :enumAsString) (assoc :enumAsString (.getEnumAsString arg))
    (get arg :mapTargetType) (assoc :mapTargetType (.getMapTargetType arg))))

(def schemas
  {:gcp.bigquery.v2/ParquetOptions
   [:map {:closed true}
    [:type {:optional true} [:= "PARQUET"]]
    [:enableListInference {:optional true} :boolean]
    [:enumAsString {:optional true} :boolean]
    [:mapTargetType {:optional true} :string]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))