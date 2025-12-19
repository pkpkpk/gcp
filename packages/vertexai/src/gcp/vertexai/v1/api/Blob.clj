(ns gcp.vertexai.v1.api.Blob
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import [com.google.cloud.vertexai.api Blob]))

(defn ^Blob from-edn [arg]
  (global/strict! :gcp.vertexai.v1.api/Blob arg)
  (let [builder (Blob/newBuilder)]
    (.setMimeType builder (:mimeType arg))
    (.setData builder (protobuf/bytestring-from-edn (:data arg)))
    (.build builder)))

(defn to-edn [^Blob blob]
  {:post [(global/strict! :gcp.vertexai.v1.api/Blob %)]}
  {:data (.getData blob)
   :mimeType (.getMimeType blob)})

(def schema
  [:map
   {:doc              "Raw media bytes. Text should not be sent as raw bytes, use Part/text"
    :generativeai/url "https://ai.google.dev/api/caching#Blob"
    :class            'com.google.cloud.vertexai.api.Blob
    :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.Blob"}
   [:mimeType {:optional false
               :doc "TODO consider validating"} [:string {:min 1}]]
   [:data {:optional false} :gcp.protobuf/ByteString]])

(global/register-schema! :gcp.vertexai.v1.api/Blob schema)