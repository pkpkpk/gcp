;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.FunctionResponsePart
  {:doc
     "<pre>\nA datatype containing media that is part of a `FunctionResponse` message.\n\nA `FunctionResponsePart` consists of data which has an associated datatype. A\n`FunctionResponsePart` can only contain one of the accepted types in\n`FunctionResponsePart.data`.\n\nA `FunctionResponsePart` must have a fixed IANA MIME type identifying the\ntype and subtype of the media if the `inline_data` field is filled with raw\nbytes.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.FunctionResponsePart}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.FunctionResponsePart"
   :gcp.dev/certification
     {:base-seed 1776627470073
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627470073 :standard 1776627470074 :stress 1776627470075}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:37:51.167251301Z"}}
  (:require [gcp.global :as global]
            [gcp.vertexai.api.FunctionResponseBlob :as FunctionResponseBlob]
            [gcp.vertexai.api.FunctionResponseFileData :as
             FunctionResponseFileData])
  (:import [com.google.cloud.vertexai.api FunctionResponsePart
            FunctionResponsePart$Builder FunctionResponsePart$DataCase]))

(declare from-edn to-edn DataCase-from-edn DataCase-to-edn)

(def DataCase-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.vertexai.api/FunctionResponsePart.DataCase} "INLINE_DATA"
   "FILE_DATA" "DATA_NOT_SET"])

(defn ^FunctionResponsePart from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/FunctionResponsePart arg)
  (let [builder (FunctionResponsePart/newBuilder)]
    (cond (contains? arg :fileData) (.setFileData
                                      builder
                                      (FunctionResponseFileData/from-edn
                                        (get arg :fileData)))
          (contains? arg :inlineData) (.setInlineData
                                        builder
                                        (FunctionResponseBlob/from-edn
                                          (get arg :inlineData))))
    (.build builder)))

(defn to-edn
  [^FunctionResponsePart arg]
  {:post [(global/strict! :gcp.vertexai.api/FunctionResponsePart %)]}
  (when arg
    (let [res (cond-> {})
          res (case (.name (.getDataCase arg))
                "FILE_DATA" (assoc res
                              :fileData (FunctionResponseFileData/to-edn
                                          (.getFileData arg)))
                "INLINE_DATA" (assoc res
                                :inlineData (FunctionResponseBlob/to-edn
                                              (.getInlineData arg)))
                res)]
      res)))

(def schema
  [:and
   {:closed true,
    :doc
      "<pre>\nA datatype containing media that is part of a `FunctionResponse` message.\n\nA `FunctionResponsePart` consists of data which has an associated datatype. A\n`FunctionResponsePart` can only contain one of the accepted types in\n`FunctionResponsePart.data`.\n\nA `FunctionResponsePart` must have a fixed IANA MIME type identifying the\ntype and subtype of the media if the `inline_data` field is filled with raw\nbytes.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.FunctionResponsePart}",
    :gcp/category :union-protobuf-oneof,
    :gcp/key :gcp.vertexai.api/FunctionResponsePart}
   [:map {:closed true}
    [:fileData
     {:optional true,
      :getter-doc
        "<pre>\nURI based data.\n</pre>\n\n<code>.google.cloud.vertexai.v1.FunctionResponseFileData file_data = 2;</code>\n\n@return The fileData.",
      :setter-doc
        "<pre>\nURI based data.\n</pre>\n\n<code>.google.cloud.vertexai.v1.FunctionResponseFileData file_data = 2;</code>"}
     :gcp.vertexai.api/FunctionResponseFileData]
    [:inlineData
     {:optional true,
      :getter-doc
        "<pre>\nInline media bytes.\n</pre>\n\n<code>.google.cloud.vertexai.v1.FunctionResponseBlob inline_data = 1;</code>\n\n@return The inlineData.",
      :setter-doc
        "<pre>\nInline media bytes.\n</pre>\n\n<code>.google.cloud.vertexai.v1.FunctionResponseBlob inline_data = 1;</code>"}
     :gcp.vertexai.api/FunctionResponseBlob]]
   [:fn
    {:error/message
       "Only one of these keys may be present: #{:inlineData :fileData}"}
    (quote (fn [m]
             (<= (count (filter (set (keys m)) #{:inlineData :fileData}))
                 1)))]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/FunctionResponsePart schema,
              :gcp.vertexai.api/FunctionResponsePart.DataCase DataCase-schema}
    {:gcp.global/name "gcp.vertexai.api.FunctionResponsePart"}))