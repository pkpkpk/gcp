;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.FunctionResponsePart
  {:doc
     "<pre>\nA datatype containing media that is part of a `FunctionResponse` message.\n\nA `FunctionResponsePart` consists of data which has an associated datatype. A\n`FunctionResponsePart` can only contain one of the accepted types in\n`FunctionResponsePart.data`.\n\nA `FunctionResponsePart` must have a fixed IANA MIME type identifying the\ntype and subtype of the media if the `inline_data` field is filled with raw\nbytes.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.FunctionResponsePart}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.FunctionResponsePart"
   :gcp.dev/certification
     {:base-seed 1774824787324
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1774824787324 :standard 1774824787325 :stress 1774824787326}
      :protocol-hash
        "b44d2581e3979d0a946f2f9d3973f275b05bb339485f3d5eb436c47b54f7dc70"
      :timestamp "2026-03-29T22:53:08.350742238Z"}}
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
    (clojure.core/cond (contains? arg :fileData)
                         (.setFileData builder
                                       (FunctionResponseFileData/from-edn
                                         (get arg :fileData)))
                       (contains? arg :inlineData)
                         (.setInlineData builder
                                         (FunctionResponseBlob/from-edn
                                           (get arg :inlineData))))
    (.build builder)))

(defn to-edn
  [^FunctionResponsePart arg]
  {:post [(global/strict! :gcp.vertexai.api/FunctionResponsePart %)]}
  (clojure.core/let [res (cond-> {})
                     res (case (.name (.getDataCase arg))
                           "FILE_DATA" (clojure.core/assoc res
                                         :fileData
                                           (FunctionResponseFileData/to-edn
                                             (.getFileData arg)))
                           "INLINE_DATA" (clojure.core/assoc res
                                           :inlineData
                                             (FunctionResponseBlob/to-edn
                                               (.getInlineData arg)))
                           res)]
    res))

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
   [:or
    [:map
     [:fileData
      {:optional true,
       :getter-doc
         "<pre>\nURI based data.\n</pre>\n\n<code>.google.cloud.vertexai.v1.FunctionResponseFileData file_data = 2;</code>\n\n@return The fileData.",
       :setter-doc
         "<pre>\nURI based data.\n</pre>\n\n<code>.google.cloud.vertexai.v1.FunctionResponseFileData file_data = 2;</code>"}
      :gcp.vertexai.api/FunctionResponseFileData]]
    [:map
     [:inlineData
      {:optional true,
       :getter-doc
         "<pre>\nInline media bytes.\n</pre>\n\n<code>.google.cloud.vertexai.v1.FunctionResponseBlob inline_data = 1;</code>\n\n@return The inlineData.",
       :setter-doc
         "<pre>\nInline media bytes.\n</pre>\n\n<code>.google.cloud.vertexai.v1.FunctionResponseBlob inline_data = 1;</code>"}
      :gcp.vertexai.api/FunctionResponseBlob]]]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/FunctionResponsePart schema,
              :gcp.vertexai.api/FunctionResponsePart.DataCase DataCase-schema}
    {:gcp.global/name "gcp.vertexai.api.FunctionResponsePart"}))