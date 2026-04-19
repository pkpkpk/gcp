;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.FunctionResponseFileData
  {:doc
     "<pre>\nURI based data for function response.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.FunctionResponseFileData}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.FunctionResponseFileData"
   :gcp.dev/certification
     {:base-seed 1776627468078
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627468078 :standard 1776627468079 :stress 1776627468080}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:37:48.939697533Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api FunctionResponseFileData
            FunctionResponseFileData$Builder]))

(declare from-edn to-edn)

(defn ^FunctionResponseFileData from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/FunctionResponseFileData arg)
  (let [builder (FunctionResponseFileData/newBuilder)]
    (when (some? (get arg :displayName))
      (.setDisplayName builder (get arg :displayName)))
    (when (some? (get arg :fileUri)) (.setFileUri builder (get arg :fileUri)))
    (when (some? (get arg :mimeType))
      (.setMimeType builder (get arg :mimeType)))
    (.build builder)))

(defn to-edn
  [^FunctionResponseFileData arg]
  {:post [(global/strict! :gcp.vertexai.api/FunctionResponseFileData %)]}
  (when arg
    (cond-> {:fileUri (.getFileUri arg), :mimeType (.getMimeType arg)}
      (some->> (.getDisplayName arg)
               (not= ""))
        (assoc :displayName (.getDisplayName arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nURI based data for function response.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.FunctionResponseFileData}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/FunctionResponseFileData}
   [:displayName
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Display name of the file data.\n\nUsed to provide a label or filename to distinguish file datas.\n\nThis field is only returned in PromptMessage for prompt management.\nIt is currently used in the Gemini GenerateContent calls only when server\nside tools (code_execution, google_search, and url_context) are enabled.\n</pre>\n\n<code>string display_name = 3 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The displayName.",
     :setter-doc
       "<pre>\nOptional. Display name of the file data.\n\nUsed to provide a label or filename to distinguish file datas.\n\nThis field is only returned in PromptMessage for prompt management.\nIt is currently used in the Gemini GenerateContent calls only when server\nside tools (code_execution, google_search, and url_context) are enabled.\n</pre>\n\n<code>string display_name = 3 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The displayName to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
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

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/FunctionResponseFileData schema}
    {:gcp.global/name "gcp.vertexai.api.FunctionResponseFileData"}))