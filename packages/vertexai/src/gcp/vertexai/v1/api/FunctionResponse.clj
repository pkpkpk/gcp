(ns gcp.vertexai.v1.api.FunctionResponse
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import [com.google.cloud.vertexai.api FunctionResponse]))

(defn ^FunctionResponse from-edn [arg]
  (global/strict! :gcp.vertexai.v1.api/FunctionResponse arg)
  (let [builder (FunctionResponse/newBuilder)]
    (.setName builder (:name arg))
    (.setResponse builder (protobuf/struct-from-edn (:response arg)))
    (.build builder)))

(defn to-edn [^FunctionResponse arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/FunctionResponse %)]}
  {:name (.getName arg)
   :response (protobuf/struct-to-edn (.getResponse arg))})

(def schema
  [:map
   {:doc              "A predicted FunctionCall returned from the model that contains a string representing the FunctionDeclaration.name with the arguments and their values."
    :generativeai/url "https://ai.google.dev/api/caching#FunctionResponse"
    :protobuf/type    "google.cloud.vertexai.v1.FunctionResponse"
    :class            'com.google.cloud.vertexai.api.FunctionResponse
    :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.FunctionResponse"}
   [:name {:optional false} :string]
   [:response {:optional false} :gcp.protobuf/Struct]])

(global/register-schema! :gcp.vertexai.v1.api/FunctionResponse schema)
