(ns gcp.vertexai.v1.api.SearchEntryPoint
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import [com.google.cloud.vertexai.api SearchEntryPoint]))

(defn ^SearchEntryPoint from-edn [arg]
  (global/strict! :vertex.api/SearchEntryPoint arg)
  (if (instance? SearchEntryPoint arg)
    arg
    (let [builder (SearchEntryPoint/newBuilder)]
      (some->> arg :sdkBlob (.setSdkBlob arg))
      (some->> arg :sdkBlob (.set arg))
      (.build builder))))

(defn to-edn [^SearchEntryPoint arg]
  {:post [(global/strict! :vertex.api/SearchEntryPoint %)]}
  {:renderedContent (.getRenderedContent arg)
   :sdkBlob         (protobuf/bytestring-to-edn (.getSdkBlob arg))})