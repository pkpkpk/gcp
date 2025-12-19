(ns gcp.bigquery.v2.FieldElementType
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery FieldElementType)))

(defn from-edn [arg]
  (let [builder (FieldElementType/newBuilder)]
    (.setType builder (:type arg))
    (.build builder)))

(defn to-edn [^FieldElementType arg]
  {:type (.getType arg)})

(def schemas
  {:gcp.bigquery.v2/FieldElementType
   [:map {:closed true} [:type :string]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))