(ns gcp.vertexai.v1.api.FileData
  (:require [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api FileData]))

(defn ^FileData from-edn [arg]
  (global/strict! :gcp.vertexai.v1.api/FileData arg)
  (let [builder (FileData/newBuilder)]
    (.setFileUri builder (:fileUri arg))
    (.setMimeType builder (:mimeType arg))
    (.build builder)))

(defn to-edn [^FileData arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/FileData %)]}
  {:mimeType (.getMimeType arg)
   :fileUri (.getFileUri arg)})

(def schema
  [:map
   {:doc              "URI based data."
    :generativeai/url "https://ai.google.dev/api/caching#FileData"
    :class            'com.google.cloud.vertexai.api.FileData
    :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.FileData"}
   [:fileUri :string]
   [:mimeType :string]])

(global/register-schema! :gcp.vertexai.v1.api/FileData schema)