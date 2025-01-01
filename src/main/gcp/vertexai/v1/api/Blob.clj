(ns gcp.vertexai.v1.api.Blob
  (:require [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api Blob]
           (com.google.protobuf ByteString)
           (java.nio ByteBuffer)))

(def ^{:class Blob} schema
  [:map {:closed true}
   [:mimeType {:optional false} :string]  ; TODO consider validating
   [:data {:optional false}
    [:or
     bytes?
     (global/instance-schema ByteBuffer)
     [:sequential (global/instance-schema ByteString)]]]])

(defn ^Blob from-edn [arg]
  (let [builder (Blob/newBuilder)
        data    (:data arg)]
    (.setMimeType builder (:mimeType arg))
    (if (string? data)
      (.setData builder (ByteString/copyFromUtf8 data))
      ;; byte[], bytebuffer, iterable<ByteString>
      (.setData builder (ByteString/copyFrom data)))
    (.build builder)))

(defn to-edn [^Blob blob]
  {:post [(global/strict! schema %)]}
  {:data (.getData blob)
   :mimeType (.getMimeType blob)})