(ns gcp.vertexai.v1.api.FunctionCall
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import [com.google.cloud.vertexai.api FunctionCall]))

(defn ^FunctionCall from-edn [arg]
  (global/strict! :gcp.vertexai.v1.api/FunctionCall arg)
  (let [builder (FunctionCall/newBuilder)]
    (.setName builder (:name arg))
    (.setArgs builder (protobuf/struct-from-edn (:args arg)))
    (.build builder)))

(defn to-edn [^FunctionCall fc]
  {:post [(global/strict! :gcp.vertexai.v1.api/FunctionCall %)]}
  {:name (.getName fc)
   :args (protobuf/struct-to-edn (.getArgs fc))})

(defn to-edn [^FunctionCall fc]
  {:post [(global/strict! :gcp.vertexai.v1.api/FunctionCall %)]}
  {:name (.getName fc)
   :args (protobuf/struct-to-edn (.getArgs fc))})

(def schema
  [:map
   {:doc              "A predicted FunctionCall returned from the model that contains a string representing the FunctionDeclaration.name with the arguments and their values."
    :generativeai/url "https://ai.google.dev/api/caching#FunctionCall"
    :class            'com.google.cloud.vertexai.api.FunctionCall
    :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.FunctionCall"}
   [:name {:optional false} :string]
   [:args :gcp.protobuf/Struct]])

(global/register-schema! :gcp.vertexai.v1.api/FunctionCall schema)