;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.FunctionCallingConfig
  {:doc
     "<pre>\nFunction calling config.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.FunctionCallingConfig}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.FunctionCallingConfig"
   :gcp.dev/certification
     {:base-seed 1775465519742
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1775465519742 :standard 1775465519743 :stress 1775465519744}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-06T08:52:00.744400990Z"}}
  (:require [gcp.foreign.com.google.protobuf :as protobuf]
            [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api FunctionCallingConfig
            FunctionCallingConfig$Builder FunctionCallingConfig$Mode]
           [com.google.protobuf ProtocolStringList]))

(declare from-edn to-edn Mode-from-edn Mode-to-edn)

(def Mode-schema
  [:enum
   {:closed true,
    :doc
      "<pre>\nFunction calling mode.\n</pre>\n\nProtobuf enum {@code google.cloud.vertexai.v1.FunctionCallingConfig.Mode}",
    :gcp/category :nested/enum,
    :gcp/key :gcp.vertexai.api/FunctionCallingConfig.Mode} "MODE_UNSPECIFIED"
   "AUTO" "ANY" "NONE"])

(defn ^FunctionCallingConfig from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/FunctionCallingConfig arg)
  (let [builder (FunctionCallingConfig/newBuilder)]
    (when (seq (get arg :allowedFunctionNames))
      (.addAllAllowedFunctionNames builder
                                   (seq (get arg :allowedFunctionNames))))
    (when (some? (get arg :mode))
      (.setMode builder (FunctionCallingConfig$Mode/valueOf (get arg :mode))))
    (when (some? (get arg :owedFunctionNames))
      (.addAllowedFunctionNames builder (get arg :owedFunctionNames)))
    (when (some? (get arg :streamFunctionCallArguments))
      (.setStreamFunctionCallArguments builder
                                       (get arg :streamFunctionCallArguments)))
    (.build builder)))

(defn to-edn
  [^FunctionCallingConfig arg]
  {:post [(global/strict! :gcp.vertexai.api/FunctionCallingConfig %)]}
  (when arg
    (cond-> {}
      (seq (.getAllowedFunctionNamesList arg))
        (assoc :allowedFunctionNames
          (protobuf/ProtocolStringList-to-edn (.getAllowedFunctionNamesList
                                                arg)))
      (.getMode arg) (assoc :mode (.name (.getMode arg)))
      (.getStreamFunctionCallArguments arg) (assoc :streamFunctionCallArguments
                                              (.getStreamFunctionCallArguments
                                                arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nFunction calling config.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.FunctionCallingConfig}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/FunctionCallingConfig}
   [:allowedFunctionNames
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Function names to call. Only set when the Mode is ANY. Function\nnames should match [FunctionDeclaration.name]. With mode set to ANY, model\nwill predict a function call from the set of function names provided.\n</pre>\n\n<code>repeated string allowed_function_names = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return A list containing the allowedFunctionNames.",
     :setter-doc
       "<pre>\nOptional. Function names to call. Only set when the Mode is ANY. Function\nnames should match [FunctionDeclaration.name]. With mode set to ANY, model\nwill predict a function call from the set of function names provided.\n</pre>\n\n<code>repeated string allowed_function_names = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@param values The allowedFunctionNames to add.\n@return This builder for chaining."}
    :gcp.foreign.com.google.protobuf/ProtocolStringList]
   [:mode
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Function calling mode.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.FunctionCallingConfig.Mode mode = 1 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The mode.",
     :setter-doc
       "<pre>\nOptional. Function calling mode.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.FunctionCallingConfig.Mode mode = 1 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@param value The mode to set.\n@return This builder for chaining."}
    [:enum {:closed true} "MODE_UNSPECIFIED" "AUTO" "ANY" "NONE"]]
   [:streamFunctionCallArguments
    {:optional true,
     :getter-doc
       "<pre>\nOptional. When set to true, arguments of a single function call will be\nstreamed out in multiple parts/contents/responses. Partial parameter\nresults will be returned in the [FunctionCall.partial_args] field.\n</pre>\n\n<code>bool stream_function_call_arguments = 4 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The streamFunctionCallArguments.",
     :setter-doc
       "<pre>\nOptional. When set to true, arguments of a single function call will be\nstreamed out in multiple parts/contents/responses. Partial parameter\nresults will be returned in the [FunctionCall.partial_args] field.\n</pre>\n\n<code>bool stream_function_call_arguments = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@param value The streamFunctionCallArguments to set.\n@return This builder for chaining."}
    :boolean]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/FunctionCallingConfig schema,
              :gcp.vertexai.api/FunctionCallingConfig.Mode Mode-schema}
    {:gcp.global/name "gcp.vertexai.api.FunctionCallingConfig"}))