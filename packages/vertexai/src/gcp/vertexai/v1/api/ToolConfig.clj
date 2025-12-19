(ns gcp.vertexai.v1.api.ToolConfig
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.FunctionCallingConfig :as FunctionCallingConfig])
  (:import (com.google.cloud.vertexai.api ToolConfig)))

(defn ^ToolConfig from-edn
  [{:keys [functionCallingConfig] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/ToolConfig arg)
  (let [builder (ToolConfig/newBuilder)]
    (some->> functionCallingConfig FunctionCallingConfig/from-edn (.setFunctionCallingConfig builder))
    (.build builder)))

(defn to-edn [^ToolConfig arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/ToolConfig %)]}
  (cond-> {}
          (.hasFunctionCallingConfig arg)
          (assoc :functionCallingConfig (FunctionCallingConfig/to-edn (.getFunctionCallingConfig arg)))))

(def schema
  [:map
   {:class    'com.google.cloud.vertexai.api.ToolConfig
    :closed   true
    :doc      "Tool config. This config is shared for all tools provided in the request."
    :from-edn 'gcp.vertexai.v1.api.ToolConfig/from-edn
    :to-edn   'gcp.vertexai.v1.api.ToolConfig/to-edn
    :protobuf/type    "google.cloud.vertexai.v1.ToolConfig"
    :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.ToolConfig"}
   [:functionCallingConfig {:optional true} :gcp.vertexai.v1.api/FunctionCallingConfig]])

(global/register-schema! :gcp.vertexai.v1.api/ToolConfig schema)
