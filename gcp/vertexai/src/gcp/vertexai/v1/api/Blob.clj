(ns gcp.vertexai.v1.api.Blob
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import [com.google.cloud.vertexai.api Blob]))

(defn ^Blob from-edn [arg]
  (global/strict! [:ref :vertexai.api/Blob] arg)
  (let [builder (Blob/newBuilder)]
    (.setMimeType builder (:mimeType arg))
    (.setData builder (protobuf/bytestring-from-edn (:data arg)))
    (.build builder)))

(defn to-edn [^Blob blob]
  {:post [(global/strict! [:ref :vertexai.api/Blob] %)]}
  {:data (.getData blob)
   :mimeType (.getMimeType blob)})