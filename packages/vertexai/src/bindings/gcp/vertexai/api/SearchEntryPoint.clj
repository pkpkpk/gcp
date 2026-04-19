;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.SearchEntryPoint
  {:doc
     "<pre>\nGoogle search entry point.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.SearchEntryPoint}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.SearchEntryPoint"
   :gcp.dev/certification
     {:base-seed 1776627404165
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627404165 :standard 1776627404166 :stress 1776627404167}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:36:45.115754484Z"}}
  (:require [gcp.foreign.com.google.protobuf :as protobuf]
            [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api SearchEntryPoint
            SearchEntryPoint$Builder]
           [com.google.protobuf ByteString]))

(declare from-edn to-edn)

(defn ^SearchEntryPoint from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/SearchEntryPoint arg)
  (let [builder (SearchEntryPoint/newBuilder)]
    (when (some? (get arg :renderedContent))
      (.setRenderedContent builder (get arg :renderedContent)))
    (when (some? (get arg :sdkBlob))
      (.setSdkBlob builder (protobuf/ByteString-from-edn (get arg :sdkBlob))))
    (.build builder)))

(defn to-edn
  [^SearchEntryPoint arg]
  {:post [(global/strict! :gcp.vertexai.api/SearchEntryPoint %)]}
  (when arg
    (cond-> {}
      (some->> (.getRenderedContent arg)
               (not= ""))
        (assoc :renderedContent (.getRenderedContent arg))
      (.getSdkBlob arg) (assoc :sdkBlob
                          (protobuf/ByteString-to-edn (.getSdkBlob arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nGoogle search entry point.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.SearchEntryPoint}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/SearchEntryPoint}
   [:renderedContent
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Web content snippet that can be embedded in a web page or an app\nwebview.\n</pre>\n\n<code>string rendered_content = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The renderedContent.",
     :setter-doc
       "<pre>\nOptional. Web content snippet that can be embedded in a web page or an app\nwebview.\n</pre>\n\n<code>string rendered_content = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The renderedContent to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:sdkBlob
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Base64 encoded JSON representing array of &lt;search term, search\nurl&gt; tuple.\n</pre>\n\n<code>bytes sdk_blob = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The sdkBlob.",
     :setter-doc
       "<pre>\nOptional. Base64 encoded JSON representing array of &lt;search term, search\nurl&gt; tuple.\n</pre>\n\n<code>bytes sdk_blob = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The sdkBlob to set.\n@return This builder for chaining."}
    :gcp.foreign.com.google.protobuf/ByteString]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/SearchEntryPoint schema}
    {:gcp.global/name "gcp.vertexai.api.SearchEntryPoint"}))