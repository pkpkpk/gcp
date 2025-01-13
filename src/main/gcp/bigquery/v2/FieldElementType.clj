(ns gcp.bigquery.v2.FieldElementType
  (:import (com.google.cloud.bigquery FieldElementType)))

(defn from-edn [arg]
  (let [builder (FieldElementType/newBuilder)]
    (.setType builder (:type arg))
    (.build builder)))

(defn to-edn [^FieldElementType arg]
  {:type (.getType arg)})