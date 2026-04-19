;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.FunctionResponse
  {:doc
     "<pre>\nThe result output from a [FunctionCall] that contains a string representing\nthe [FunctionDeclaration.name] and a structured JSON object containing any\noutput from the function is used as context to the model. This should contain\nthe result of a [FunctionCall] made based on model prediction.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.FunctionResponse}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.FunctionResponse"
   :gcp.dev/certification
     {:base-seed 1776627472212
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627472212 :standard 1776627472213 :stress 1776627472214}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:37:53.401456699Z"}}
  (:require [gcp.foreign.com.google.protobuf :as protobuf]
            [gcp.global :as global]
            [gcp.vertexai.api.FunctionResponsePart :as FunctionResponsePart])
  (:import [com.google.cloud.vertexai.api FunctionResponse
            FunctionResponse$Builder]
           [com.google.protobuf Struct]))

(declare from-edn to-edn)

(defn ^FunctionResponse from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/FunctionResponse arg)
  (let [builder (FunctionResponse/newBuilder)]
    (when (some? (get arg :name)) (.setName builder (get arg :name)))
    (when (seq (get arg :parts))
      (.addAllParts builder
                    (mapv FunctionResponsePart/from-edn (get arg :parts))))
    (when (some? (get arg :response))
      (.setResponse builder (protobuf/Struct-from-edn (get arg :response))))
    (.build builder)))

(defn to-edn
  [^FunctionResponse arg]
  {:post [(global/strict! :gcp.vertexai.api/FunctionResponse %)]}
  (when arg
    (cond-> {:name (.getName arg),
             :response (protobuf/Struct-to-edn (.getResponse arg))}
      (seq (.getPartsList arg))
        (assoc :parts (mapv FunctionResponsePart/to-edn (.getPartsList arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nThe result output from a [FunctionCall] that contains a string representing\nthe [FunctionDeclaration.name] and a structured JSON object containing any\noutput from the function is used as context to the model. This should contain\nthe result of a [FunctionCall] made based on model prediction.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.FunctionResponse}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/FunctionResponse}
   [:name
    {:getter-doc
       "<pre>\nRequired. The name of the function to call.\nMatches [FunctionDeclaration.name] and [FunctionCall.name].\n</pre>\n\n<code>string name = 1 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@return The name.",
     :setter-doc
       "<pre>\nRequired. The name of the function to call.\nMatches [FunctionDeclaration.name] and [FunctionCall.name].\n</pre>\n\n<code>string name = 1 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@param value The name to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:parts
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Ordered `Parts` that constitute a function response. Parts may\nhave different IANA MIME types.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.FunctionResponsePart parts = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>",
     :setter-doc
       "<pre>\nOptional. Ordered `Parts` that constitute a function response. Parts may\nhave different IANA MIME types.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.FunctionResponsePart parts = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    [:sequential {:min 1, :gen/max 2} :gcp.vertexai.api/FunctionResponsePart]]
   [:response
    {:getter-doc
       "<pre>\nRequired. The function response in JSON object format.\nUse \"output\" key to specify function output and \"error\" key to specify\nerror details (if any). If \"output\" and \"error\" keys are not specified,\nthen whole \"response\" is treated as function output.\n</pre>\n\n<code>.google.protobuf.Struct response = 2 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@return The response.",
     :setter-doc
       "<pre>\nRequired. The function response in JSON object format.\nUse \"output\" key to specify function output and \"error\" key to specify\nerror details (if any). If \"output\" and \"error\" keys are not specified,\nthen whole \"response\" is treated as function output.\n</pre>\n\n<code>.google.protobuf.Struct response = 2 [(.google.api.field_behavior) = REQUIRED];</code>"}
    :gcp.foreign.com.google.protobuf/Struct]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/FunctionResponse schema}
    {:gcp.global/name "gcp.vertexai.api.FunctionResponse"}))