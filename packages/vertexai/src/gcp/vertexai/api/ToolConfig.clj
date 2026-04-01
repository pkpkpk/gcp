;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.ToolConfig
  {:doc
     "<pre>\nTool config. This config is shared for all tools provided in the request.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.ToolConfig}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.ToolConfig"
   :gcp.dev/certification
     {:base-seed 1774824629760
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1774824629760 :standard 1774824629761 :stress 1774824629762}
      :protocol-hash
        "b44d2581e3979d0a946f2f9d3973f275b05bb339485f3d5eb436c47b54f7dc70"
      :timestamp "2026-03-29T22:50:30.813484904Z"}}
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
  (cond-> {}
    (.hasFunctionCallingConfig arg) (assoc :functionCallingConfig
                                      (FunctionCallingConfig/to-edn
                                        (.getFunctionCallingConfig arg)))
    (.hasRetrievalConfig arg) (assoc :retrievalConfig
                                (RetrievalConfig/to-edn (.getRetrievalConfig
                                                          arg)))))

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