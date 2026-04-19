;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.ToolConfig
  {:doc
     "<pre>\nTool config. This config is shared for all tools provided in the request.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.ToolConfig}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.ToolConfig"
   :gcp.dev/certification
     {:base-seed 1776627453335
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627453335 :standard 1776627453336 :stress 1776627453337}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:37:34.217975188Z"}}
  (:require [gcp.global :as global]
            [gcp.vertexai.api.FunctionCallingConfig :as FunctionCallingConfig]
            [gcp.vertexai.api.RetrievalConfig :as RetrievalConfig])
  (:import [com.google.cloud.vertexai.api ToolConfig ToolConfig$Builder]))

(declare from-edn to-edn)

(defn ^ToolConfig from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/ToolConfig arg)
  (let [builder (ToolConfig/newBuilder)]
    (when (some? (get arg :functionCallingConfig))
      (.setFunctionCallingConfig builder
                                 (FunctionCallingConfig/from-edn
                                   (get arg :functionCallingConfig))))
    (when (some? (get arg :retrievalConfig))
      (.setRetrievalConfig builder
                           (RetrievalConfig/from-edn (get arg
                                                          :retrievalConfig))))
    (.build builder)))

(defn to-edn
  [^ToolConfig arg]
  {:post [(global/strict! :gcp.vertexai.api/ToolConfig %)]}
  (when arg
    (cond-> {}
      (.hasFunctionCallingConfig arg) (assoc :functionCallingConfig
                                        (FunctionCallingConfig/to-edn
                                          (.getFunctionCallingConfig arg)))
      (.hasRetrievalConfig arg) (assoc :retrievalConfig
                                  (RetrievalConfig/to-edn (.getRetrievalConfig
                                                            arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nTool config. This config is shared for all tools provided in the request.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.ToolConfig}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/ToolConfig}
   [:functionCallingConfig
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Function calling config.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.FunctionCallingConfig function_calling_config = 1 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The functionCallingConfig.",
     :setter-doc
       "<pre>\nOptional. Function calling config.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.FunctionCallingConfig function_calling_config = 1 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.vertexai.api/FunctionCallingConfig]
   [:retrievalConfig
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Retrieval config.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.RetrievalConfig retrieval_config = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The retrievalConfig.",
     :setter-doc
       "<pre>\nOptional. Retrieval config.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.RetrievalConfig retrieval_config = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.vertexai.api/RetrievalConfig]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/ToolConfig schema}
    {:gcp.global/name "gcp.vertexai.api.ToolConfig"}))