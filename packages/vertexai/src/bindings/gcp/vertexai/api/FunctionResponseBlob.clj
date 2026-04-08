;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.FunctionResponseBlob
  {:doc
     "<pre>\nRaw media bytes for function response.\n\nText should not be sent as raw bytes, use the 'text' field.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.FunctionResponseBlob}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.FunctionResponseBlob"
   :gcp.dev/certification
     {:base-seed 1775465696166
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1775465696166 :standard 1775465696167 :stress 1775465696168}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-06T08:54:57.219453359Z"}}
  (:require [gcp.foreign.com.google.protobuf :as protobuf]
            [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api FunctionResponseBlob
            FunctionResponseBlob$Builder]
           [com.google.protobuf ByteString]))

(declare from-edn to-edn)

(defn ^FunctionResponseBlob from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/FunctionResponseBlob arg)
  (let [builder (FunctionResponseBlob/newBuilder)]
    (when (some? (get arg :data))
      (.setData builder (protobuf/ByteString-from-edn (get arg :data))))
    (when (some? (get arg :displayName))
      (.setDisplayName builder (get arg :displayName)))
    (when (some? (get arg :mimeType))
      (.setMimeType builder (get arg :mimeType)))
    (.build builder)))

(defn to-edn
  [^FunctionResponseBlob arg]
  {:post [(global/strict! :gcp.vertexai.api/FunctionResponseBlob %)]}
  (when arg
    (cond-> {:data (protobuf/ByteString-to-edn (.getData arg)),
             :mimeType (.getMimeType arg)}
      (some->> (.getDisplayName arg)
               (not= ""))
        (assoc :displayName (.getDisplayName arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nRaw media bytes for function response.\n\nText should not be sent as raw bytes, use the 'text' field.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.FunctionResponseBlob}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/FunctionResponseBlob}
   [:data
    {:getter-doc
       "<pre>\nRequired. Raw bytes.\n</pre>\n\n<code>bytes data = 2 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@return The data.",
     :setter-doc
       "<pre>\nRequired. Raw bytes.\n</pre>\n\n<code>bytes data = 2 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@param value The data to set.\n@return This builder for chaining."}
    :gcp.foreign.com.google.protobuf/ByteString]
   [:displayName
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Display name of the blob.\n\nUsed to provide a label or filename to distinguish blobs.\n\nThis field is only returned in PromptMessage for prompt management.\nIt is currently used in the Gemini GenerateContent calls only when server\nside tools (code_execution, google_search, and url_context) are enabled.\n</pre>\n\n<code>string display_name = 4 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The displayName.",
     :setter-doc
       "<pre>\nOptional. Display name of the blob.\n\nUsed to provide a label or filename to distinguish blobs.\n\nThis field is only returned in PromptMessage for prompt management.\nIt is currently used in the Gemini GenerateContent calls only when server\nside tools (code_execution, google_search, and url_context) are enabled.\n</pre>\n\n<code>string display_name = 4 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The displayName to set.\n@return This builder for chaining."}
    [:string {:min 1}]]
   [:mimeType
    {:getter-doc
       "<pre>\nRequired. The IANA standard MIME type of the source data.\n</pre>\n\n<code>string mime_type = 1 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@return The mimeType.",
     :setter-doc
       "<pre>\nRequired. The IANA standard MIME type of the source data.\n</pre>\n\n<code>string mime_type = 1 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@param value The mimeType to set.\n@return This builder for chaining."}
    [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/FunctionResponseBlob schema}
    {:gcp.global/name "gcp.vertexai.api.FunctionResponseBlob"}))