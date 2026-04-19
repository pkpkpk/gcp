;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.Blob
  {:doc
     "<pre>\nContent blob.\n\nIt's preferred to send as [text][google.cloud.aiplatform.v1.Part.text]\ndirectly rather than raw bytes.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.Blob}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.Blob"
   :gcp.dev/certification
     {:base-seed 1776627443361
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627443361 :standard 1776627443362 :stress 1776627443363}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:37:24.383778326Z"}}
  (:require [gcp.foreign.com.google.protobuf :as protobuf]
            [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api Blob Blob$Builder]
           [com.google.protobuf ByteString]))

(declare from-edn to-edn)

(defn ^Blob from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/Blob arg)
  (let [builder (Blob/newBuilder)]
    (when (some? (get arg :data))
      (.setData builder (protobuf/ByteString-from-edn (get arg :data))))
    (when (some? (get arg :mimeType))
      (.setMimeType builder (get arg :mimeType)))
    (.build builder)))

(defn to-edn
  [^Blob arg]
  {:post [(global/strict! :gcp.vertexai.api/Blob %)]}
  (when arg
    (cond-> {:data (protobuf/ByteString-to-edn (.getData arg)),
             :mimeType (.getMimeType arg)})))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nContent blob.\n\nIt's preferred to send as [text][google.cloud.aiplatform.v1.Part.text]\ndirectly rather than raw bytes.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.Blob}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/Blob}
   [:data
    {:getter-doc
       "<pre>\nRequired. Raw bytes.\n</pre>\n\n<code>bytes data = 2 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@return The data.",
     :setter-doc
       "<pre>\nRequired. Raw bytes.\n</pre>\n\n<code>bytes data = 2 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@param value The data to set.\n@return This builder for chaining."}
    :gcp.foreign.com.google.protobuf/ByteString]
   [:mimeType
    {:getter-doc
       "<pre>\nRequired. The IANA standard MIME type of the source data.\n</pre>\n\n<code>string mime_type = 1 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@return The mimeType.",
     :setter-doc
       "<pre>\nRequired. The IANA standard MIME type of the source data.\n</pre>\n\n<code>string mime_type = 1 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@param value The mimeType to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]])

(global/include-schema-registry! (with-meta {:gcp.vertexai.api/Blob schema}
                                   {:gcp.global/name "gcp.vertexai.api.Blob"}))