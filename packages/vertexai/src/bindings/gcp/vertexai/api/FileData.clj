;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.FileData
  {:doc
     "<pre>\nURI based data.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.FileData}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.FileData"
   :gcp.dev/certification
     {:base-seed 1776627446250
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627446250 :standard 1776627446251 :stress 1776627446252}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:37:27.889355103Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api FileData FileData$Builder]))

(declare from-edn to-edn)

(defn ^FileData from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/FileData arg)
  (let [builder (FileData/newBuilder)]
    (when (some? (get arg :fileUri)) (.setFileUri builder (get arg :fileUri)))
    (when (some? (get arg :mimeType))
      (.setMimeType builder (get arg :mimeType)))
    (.build builder)))

(defn to-edn
  [^FileData arg]
  {:post [(global/strict! :gcp.vertexai.api/FileData %)]}
  (when arg
    (cond-> {:fileUri (.getFileUri arg), :mimeType (.getMimeType arg)})))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nURI based data.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.FileData}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/FileData}
   [:fileUri
    {:getter-doc
       "<pre>\nRequired. URI.\n</pre>\n\n<code>string file_uri = 2 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@return The fileUri.",
     :setter-doc
       "<pre>\nRequired. URI.\n</pre>\n\n<code>string file_uri = 2 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@param value The fileUri to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:mimeType
    {:getter-doc
       "<pre>\nRequired. The IANA standard MIME type of the source data.\n</pre>\n\n<code>string mime_type = 1 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@return The mimeType.",
     :setter-doc
       "<pre>\nRequired. The IANA standard MIME type of the source data.\n</pre>\n\n<code>string mime_type = 1 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@param value The mimeType to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]])

(global/include-schema-registry! (with-meta {:gcp.vertexai.api/FileData schema}
                                   {:gcp.global/name
                                      "gcp.vertexai.api.FileData"}))