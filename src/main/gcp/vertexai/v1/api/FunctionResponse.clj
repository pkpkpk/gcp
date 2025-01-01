(ns gcp.vertexai.v1.api.FunctionResponse
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import [com.google.cloud.vertexai.api FunctionResponse]))

(def ^{:class FunctionResponse} schema
  [:map
   [:name {:optional false} :string]
   [:response {:optional false} protobuf/struct-schema]])

(defn ^FunctionResponse from-edn [arg]
  (global/strict! schema arg)
  (let [builder (FunctionResponse/newBuilder)]
    (.setName builder (:name arg))
    (.setResponse builder (protobuf/struct-from-edn (:response arg)))
    (.build builder)))

(defn to-edn [^FunctionResponse arg]
  {:post [(global/strict! schema %)]}
  {:name (.getName arg)
   :response (protobuf/struct-to-edn (.getResponse arg))})
