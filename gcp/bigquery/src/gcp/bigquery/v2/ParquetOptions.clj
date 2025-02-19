(ns gcp.bigquery.v2.ParquetOptions
  (:import [com.google.cloud.bigquery ParquetOptions])
  (:require [gcp.global :as g]))

(defn ^ParquetOptions from-edn
  [arg]
  (gcp.global/strict! :gcp/bigquery.ParquetOptions arg)
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
  {:post [(gcp.global/strict! :gcp/bigquery.ParquetOptions %)]}
  (cond-> {}
    (get arg :enableListInference) (assoc :enableListInference
                                     (.getEnableListInference arg))
    (get arg :enumAsString) (assoc :enumAsString (.getEnumAsString arg))
    (get arg :mapTargetType) (assoc :mapTargetType (.getMapTargetType arg))))