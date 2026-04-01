;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.Blob
  {:doc
     "<pre>\nContent blob.\n\nIt's preferred to send as [text][google.cloud.aiplatform.v1.Part.text]\ndirectly rather than raw bytes.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.Blob}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.Blob"
   :gcp.dev/certification
     {:base-seed 1774824753007
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1774824753007 :standard 1774824753008 :stress 1774824753009}
      :protocol-hash
        "b44d2581e3979d0a946f2f9d3973f275b05bb339485f3d5eb436c47b54f7dc70"
      :timestamp "2026-03-29T22:52:33.959913099Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api Blob Blob$Builder]))

(declare from-edn to-edn)

(defn ^Blob from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/Blob arg)
  (let [builder (Blob/newBuilder)]
    (when (some? (get arg :mimeType))
      (.setMimeType builder (get arg :mimeType)))
    (.build builder)))

(defn to-edn
  [^Blob arg]
  {:post [(global/strict! :gcp.vertexai.api/Blob %)]}
  (cond-> {:mimeType (.getMimeType arg)}))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nContent blob.\n\nIt's preferred to send as [text][google.cloud.aiplatform.v1.Part.text]\ndirectly rather than raw bytes.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.Blob}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/Blob}
   [:mimeType
    {:getter-doc
       "<pre>\nRequired. The IANA standard MIME type of the source data.\n</pre>\n\n<code>string mime_type = 1 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@return The mimeType.",
     :setter-doc
       "<pre>\nRequired. The IANA standard MIME type of the source data.\n</pre>\n\n<code>string mime_type = 1 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@param value The mimeType to set.\n@return This builder for chaining."}
    [:string {:min 1}]]])

(global/include-schema-registry! (with-meta {:gcp.vertexai.api/Blob schema}
                                   {:gcp.global/name "gcp.vertexai.api.Blob"}))