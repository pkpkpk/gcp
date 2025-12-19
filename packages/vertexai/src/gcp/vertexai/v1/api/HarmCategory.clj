(ns gcp.vertexai.v1.api.HarmCategory
  (:require [gcp.global :as global])
  (:import (com.google.cloud.vertexai.api HarmCategory)))

(defn ^HarmCategory from-edn [arg]
  (global/strict! :gcp.vertexai.v1.api/HarmCategory arg)
  (if (number? arg)
    (HarmCategory/forNumber (int arg))
    (HarmCategory/valueOf ^String arg)))

(defn ^String to-edn [arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/HarmCategory %)]}
  (if (int? arg)
    (.name (HarmCategory/forNumber arg))
    (if (string? arg)
      arg
      (if (instance? HarmCategory arg)
        (.name arg)
        (throw (ex-info "unsupported arg" {:arg arg}))))))

(def schema
  [:enum
   {:ns               'gcp.vertexai.v1.api.HarmCategory
    :from-edn         'gcp.vertexai.v1.api.HarmCategory/from-edn
    :to-edn           'gcp.vertexai.v1.api.HarmCategory/to-edn
    :doc              "Harm categories that will block the content."
    :generativeai/url "https://ai.google.dev/api/generate-content#harmcategory"
    :protobuf/type    "google.cloud.vertexai.v1.HarmCategory"
    :class            'com.google.cloud.vertexai.api.HarmCategory
    :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.HarmCategory"}
   "HARM_CATEGORY_DANGEROUS_CONTENT"
   "HARM_CATEGORY_HARASSMENT"
   "HARM_CATEGORY_HATE_SPEECH"
   "HARM_CATEGORY_SEXUALLY_EXPLICIT"
   "HARM_CATEGORY_UNSPECIFIED"
   "UNRECOGNIZED"
   0 1 2 3 4])

(global/register-schema! :gcp.vertexai.v1.api/HarmCategory schema)
