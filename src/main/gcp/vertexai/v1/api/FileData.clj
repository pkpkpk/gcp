(ns gcp.vertexai.v1.api.FileData
  (:require [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api FileData]))

(def schema
  [:map {:closed true}
   [:fileUri :string]
   [:mimeType :string]])

(defn ^FileData from-edn [arg]
  (let [builder (FileData/newBuilder)]
    (.setFileUri builder (:fileUri arg))
    (.setMimeType builder (:mimeType arg))
    (.build builder)))

(defn to-edn [^FileData arg]
  {:post [(global/strict! schema %)]}
  {:mimeType (.getMimeType arg)
   :fileUri (.getFileUri arg)})