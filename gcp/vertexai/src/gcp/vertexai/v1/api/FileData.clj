(ns gcp.vertexai.v1.api.FileData
  (:require [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api FileData]))

(defn ^FileData from-edn [arg]
  (global/strict! [:ref :vertexai.api/FileData] arg)
  (let [builder (FileData/newBuilder)]
    (.setFileUri builder (:fileUri arg))
    (.setMimeType builder (:mimeType arg))
    (.build builder)))

(defn to-edn [^FileData arg]
  {:post [(global/strict! [:ref :vertexai.api/FileData] %)]}
  {:mimeType (.getMimeType arg)
   :fileUri (.getFileUri arg)})