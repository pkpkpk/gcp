(ns gcp.vertexai.v1.api.Type
  (:require [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api Type]))

(defn ^Type from-edn [arg]
  (global/strict! :gcp.vertexai.v1.api/Type arg)
  (if (number? arg)
    (if (neg? arg)
      Type/UNRECOGNIZED
      (Type/forNumber (int arg)))
    (Type/valueOf ^String arg)))

(defn ^String to-edn [^Type t]
  {:post [(global/strict! :gcp.vertexai.v1.api/Type %)]}
  (.toString t))

(def schema
  [:or
   {:class            'com.google.cloud.vertexai.api.Type
    :ns               'gcp.vertexai.v1.api.Type
    :from-edn         'gcp.vertexai.v1.api.Type/from-edn
    :to-edn           'gcp.vertexai.v1.api.Type/to-edn
    :doc              "OpenAPI data types as defined by https://swagger.io/docs/specification/data-models/data-types/"
    :generativeai/url "https://ai.google.dev/api/caching#Type"
    :protobuf/type    "google.cloud.vertexai.v1.Type"
    :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.Type"}
   [:and :string
    [:enum "TYPE_UNSPECIFIED" "STRING" "NUMBER" "INTEGER" "BOOLEAN" "ARRAY" "OBJECT" "UNRECOGNIZED"]]
   [:and :int [:enum 0 1 2 3 4 5 6]]])

(global/register-schema! :gcp.vertexai.v1.api/Type schema)
