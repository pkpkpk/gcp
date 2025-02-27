(ns gcp.vertexai.v1.api.FunctionResponse
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import [com.google.cloud.vertexai.api FunctionResponse]))

(defn ^FunctionResponse from-edn [arg]
  (global/strict! :gcp/vertexai.api.FunctionResponse arg)
  (let [builder (FunctionResponse/newBuilder)]
    (.setName builder (:name arg))
    (.setResponse builder (protobuf/struct-from-edn (:response arg)))
    (.build builder)))

(defn to-edn [^FunctionResponse arg]
  {:post [(global/strict! :gcp/vertexai.api.FunctionResponse %)]}
  {:name (.getName arg)
   :response (protobuf/struct-to-edn (.getResponse arg))})
