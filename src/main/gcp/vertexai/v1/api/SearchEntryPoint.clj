(ns gcp.vertexai.v1.api.SearchEntryPoint
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import [com.google.cloud.vertexai.api SearchEntryPoint]))

(def ^{:class SearchEntryPoint} schema
  [:map
   [:renderedContent
    {:doc "Optional. Web content snippet that can be embedded in a web page or an app webview."
     :optional true}
    :string]
   [:sdkBlob
    {:doc "Optional. Base64 encoded JSON representing array of <search term, search url> tuple. A base64-encoded string"
     :optional true}
    protobuf/bytestring-schema]])

(defn ^SearchEntryPoint from-edn [arg]
  (global/strict! schema arg)
  (if (instance? SearchEntryPoint arg)
    arg
    (let [builder (SearchEntryPoint/newBuilder)]
      (some->> arg :sdkBlob (.setSdkBlob arg))
      (some->> arg :sdkBlob (.set arg))
      (.build builder))))

(defn to-edn [^SearchEntryPoint arg]
  {:post [(global/strict! schema %)]}
  {:renderedContent (.getRenderedContent arg)
   :sdkBlob         (protobuf/bytestring-to-edn (.getSdkBlob arg))})