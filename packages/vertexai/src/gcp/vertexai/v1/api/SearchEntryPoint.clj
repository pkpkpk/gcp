(ns gcp.vertexai.v1.api.SearchEntryPoint
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import [com.google.cloud.vertexai.api SearchEntryPoint]))

(defn ^SearchEntryPoint from-edn [arg]
  (global/strict! :gcp.vertexai.v1.api/SearchEntryPoint arg)
  (if (instance? SearchEntryPoint arg)
    arg
    (let [builder (SearchEntryPoint/newBuilder)]
      (some->> (:sdkBlob arg) protobuf/bytestring-from-edn (.setSdkBlob builder))
      (some->> (:renderedContent arg) (.setRenderedContent builder))
      (.build builder))))

(defn to-edn [^SearchEntryPoint arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/SearchEntryPoint %)]}
  (cond-> {}
          (not (empty? (.getRenderedContent arg)))
          (assoc :renderedContent (.getRenderedContent arg))
          (not (.isEmpty (.getSdkBlob arg)))
          (assoc :sdkBlob (protobuf/bytestring-to-edn (.getSdkBlob arg)))))

(def schema
  [:map
   {:class    'com.google.cloud.vertexai.api.SearchEntryPoint
    :from-edn 'gcp.vertexai.v1.api.SearchEntryPoint/from-edn
    :to-edn   'gcp.vertexai.v1.api.SearchEntryPoint/to-edn
    :protobuf/type    "google.cloud.vertexai.v1.SearchEntryPoint"}
   [:renderedContent
    {:doc      "Optional. Web content snippet that can be embedded in a web page or an app webview."
     :optional true}
    :string]
   [:sdkBlob
    {:doc      "Optional. Base64 encoded JSON representing array of <search term, search url> tuple. A base64-encoded string"
     :optional true}
    :gcp.protobuf/ByteString]])

(global/register-schema! :gcp.vertexai.v1.api/SearchEntryPoint schema)
