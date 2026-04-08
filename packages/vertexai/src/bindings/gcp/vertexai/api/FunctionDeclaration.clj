;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.FunctionDeclaration
  {:doc
     "<pre>\nStructured representation of a function declaration as defined by the\n[OpenAPI 3.0 specification](https://spec.openapis.org/oas/v3.0.3). Included\nin this declaration are the function name, description, parameters and\nresponse type. This FunctionDeclaration is a representation of a block of\ncode that can be used as a `Tool` by the model and executed by the client.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.FunctionDeclaration}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.FunctionDeclaration"
   :gcp.dev/certification
     {:base-seed 1775465557877
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1775465557877 :standard 1775465557878 :stress 1775465557879}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-06T08:52:41.550928076Z"}}
  (:require [gcp.foreign.com.google.protobuf :as protobuf]
            [gcp.global :as global]
            [gcp.vertexai.api.Schema :as Schema])
  (:import [com.google.cloud.vertexai.api FunctionDeclaration
            FunctionDeclaration$Builder]
           [com.google.protobuf Value]))

(declare from-edn to-edn)

(defn ^FunctionDeclaration from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/FunctionDeclaration arg)
  (let [builder (FunctionDeclaration/newBuilder)]
    (when (some? (get arg :description))
      (.setDescription builder (get arg :description)))
    (when (some? (get arg :name)) (.setName builder (get arg :name)))
    (when (some? (get arg :parameters))
      (.setParameters builder (Schema/from-edn (get arg :parameters))))
    (when (some? (get arg :parametersJsonSchema))
      (.setParametersJsonSchema builder
                                (protobuf/Value-from-edn
                                  (get arg :parametersJsonSchema))))
    (when (some? (get arg :response))
      (.setResponse builder (Schema/from-edn (get arg :response))))
    (when (some? (get arg :responseJsonSchema))
      (.setResponseJsonSchema builder
                              (protobuf/Value-from-edn
                                (get arg :responseJsonSchema))))
    (.build builder)))

(defn to-edn
  [^FunctionDeclaration arg]
  {:post [(global/strict! :gcp.vertexai.api/FunctionDeclaration %)]}
  (when arg
    (cond-> {:name (.getName arg)}
      (some->> (.getDescription arg)
               (not= ""))
        (assoc :description (.getDescription arg))
      (.hasParameters arg) (assoc :parameters
                             (Schema/to-edn (.getParameters arg)))
      (.hasParametersJsonSchema arg) (assoc :parametersJsonSchema
                                       (protobuf/Value-to-edn
                                         (.getParametersJsonSchema arg)))
      (.hasResponse arg) (assoc :response (Schema/to-edn (.getResponse arg)))
      (.hasResponseJsonSchema arg) (assoc :responseJsonSchema
                                     (protobuf/Value-to-edn
                                       (.getResponseJsonSchema arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nStructured representation of a function declaration as defined by the\n[OpenAPI 3.0 specification](https://spec.openapis.org/oas/v3.0.3). Included\nin this declaration are the function name, description, parameters and\nresponse type. This FunctionDeclaration is a representation of a block of\ncode that can be used as a `Tool` by the model and executed by the client.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.FunctionDeclaration}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/FunctionDeclaration}
   [:description
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Description and purpose of the function.\nModel uses it to decide how and whether to call the function.\n</pre>\n\n<code>string description = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The description.",
     :setter-doc
       "<pre>\nOptional. Description and purpose of the function.\nModel uses it to decide how and whether to call the function.\n</pre>\n\n<code>string description = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The description to set.\n@return This builder for chaining."}
    [:string {:min 1}]]
   [:name
    {:getter-doc
       "<pre>\nRequired. The name of the function to call.\nMust start with a letter or an underscore.\nMust be a-z, A-Z, 0-9, or contain underscores, dots and dashes, with a\nmaximum length of 64.\n</pre>\n\n<code>string name = 1 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@return The name.",
     :setter-doc
       "<pre>\nRequired. The name of the function to call.\nMust start with a letter or an underscore.\nMust be a-z, A-Z, 0-9, or contain underscores, dots and dashes, with a\nmaximum length of 64.\n</pre>\n\n<code>string name = 1 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@param value The name to set.\n@return This builder for chaining."}
    [:string {:min 1}]]
   [:parameters
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Describes the parameters to this function in JSON Schema Object\nformat. Reflects the Open API 3.03 Parameter Object. string Key: the name\nof the parameter. Parameter names are case sensitive. Schema Value: the\nSchema defining the type used for the parameter. For function with no\nparameters, this can be left unset. Parameter names must start with a\nletter or an underscore and must only contain chars a-z, A-Z, 0-9, or\nunderscores with a maximum length of 64. Example with 1 required and 1\noptional parameter: type: OBJECT properties:\nparam1:\ntype: STRING\nparam2:\ntype: INTEGER\nrequired:\n- param1\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.Schema parameters = 3 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The parameters.",
     :setter-doc
       "<pre>\nOptional. Describes the parameters to this function in JSON Schema Object\nformat. Reflects the Open API 3.03 Parameter Object. string Key: the name\nof the parameter. Parameter names are case sensitive. Schema Value: the\nSchema defining the type used for the parameter. For function with no\nparameters, this can be left unset. Parameter names must start with a\nletter or an underscore and must only contain chars a-z, A-Z, 0-9, or\nunderscores with a maximum length of 64. Example with 1 required and 1\noptional parameter: type: OBJECT properties:\nparam1:\ntype: STRING\nparam2:\ntype: INTEGER\nrequired:\n- param1\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.Schema parameters = 3 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.vertexai.api/Schema]
   [:parametersJsonSchema
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Describes the parameters to the function in JSON Schema format.\nThe schema must describe an object where the properties are the parameters\nto the function. For example:\n\n```\n{\n\"type\": \"object\",\n\"properties\": {\n\"name\": { \"type\": \"string\" },\n\"age\": { \"type\": \"integer\" }\n},\n\"additionalProperties\": false,\n\"required\": [\"name\", \"age\"],\n\"propertyOrdering\": [\"name\", \"age\"]\n}\n```\n\nThis field is mutually exclusive with `parameters`.\n</pre>\n\n<code>\n.google.protobuf.Value parameters_json_schema = 5 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The parametersJsonSchema.",
     :setter-doc
       "<pre>\nOptional. Describes the parameters to the function in JSON Schema format.\nThe schema must describe an object where the properties are the parameters\nto the function. For example:\n\n```\n{\n\"type\": \"object\",\n\"properties\": {\n\"name\": { \"type\": \"string\" },\n\"age\": { \"type\": \"integer\" }\n},\n\"additionalProperties\": false,\n\"required\": [\"name\", \"age\"],\n\"propertyOrdering\": [\"name\", \"age\"]\n}\n```\n\nThis field is mutually exclusive with `parameters`.\n</pre>\n\n<code>\n.google.protobuf.Value parameters_json_schema = 5 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.foreign.com.google.protobuf/Value]
   [:response
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Describes the output from this function in JSON Schema format.\nReflects the Open API 3.03 Response Object. The Schema defines the type\nused for the response value of the function.\n</pre>\n\n<code>.google.cloud.vertexai.v1.Schema response = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The response.",
     :setter-doc
       "<pre>\nOptional. Describes the output from this function in JSON Schema format.\nReflects the Open API 3.03 Response Object. The Schema defines the type\nused for the response value of the function.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.Schema response = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.vertexai.api/Schema]
   [:responseJsonSchema
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Describes the output from this function in JSON Schema format.\nThe value specified by the schema is the response value of the function.\n\nThis field is mutually exclusive with `response`.\n</pre>\n\n<code>\n.google.protobuf.Value response_json_schema = 6 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The responseJsonSchema.",
     :setter-doc
       "<pre>\nOptional. Describes the output from this function in JSON Schema format.\nThe value specified by the schema is the response value of the function.\n\nThis field is mutually exclusive with `response`.\n</pre>\n\n<code>\n.google.protobuf.Value response_json_schema = 6 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.foreign.com.google.protobuf/Value]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/FunctionDeclaration schema}
    {:gcp.global/name "gcp.vertexai.api.FunctionDeclaration"}))