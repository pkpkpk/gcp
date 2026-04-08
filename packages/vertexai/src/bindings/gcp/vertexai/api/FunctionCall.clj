;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.FunctionCall
  {:doc
     "<pre>\nA predicted [FunctionCall] returned from the model that contains a string\nrepresenting the [FunctionDeclaration.name] and a structured JSON object\ncontaining the parameters and their values.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.FunctionCall}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.FunctionCall"
   :gcp.dev/certification
     {:base-seed 1775465693906
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1775465693906 :standard 1775465693907 :stress 1775465693908}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-06T08:54:55.002509730Z"}}
  (:require [gcp.foreign.com.google.protobuf :as protobuf]
            [gcp.global :as global]
            [gcp.vertexai.api.PartialArg :as PartialArg])
  (:import [com.google.cloud.vertexai.api FunctionCall FunctionCall$Builder]
           [com.google.protobuf Struct]))

(declare from-edn to-edn)

(defn ^FunctionCall from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/FunctionCall arg)
  (let [builder (FunctionCall/newBuilder)]
    (when (some? (get arg :args))
      (.setArgs builder (protobuf/Struct-from-edn (get arg :args))))
    (when (some? (get arg :name)) (.setName builder (get arg :name)))
    (when (seq (get arg :partialArgs))
      (.addAllPartialArgs builder
                          (map PartialArg/from-edn (get arg :partialArgs))))
    (when (some? (get arg :willContinue))
      (.setWillContinue builder (get arg :willContinue)))
    (.build builder)))

(defn to-edn
  [^FunctionCall arg]
  {:post [(global/strict! :gcp.vertexai.api/FunctionCall %)]}
  (when arg
    (cond-> {}
      (.hasArgs arg) (assoc :args (protobuf/Struct-to-edn (.getArgs arg)))
      (some->> (.getName arg)
               (not= ""))
        (assoc :name (.getName arg))
      (seq (.getPartialArgsList arg))
        (assoc :partialArgs (map PartialArg/to-edn (.getPartialArgsList arg)))
      (.getWillContinue arg) (assoc :willContinue (.getWillContinue arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nA predicted [FunctionCall] returned from the model that contains a string\nrepresenting the [FunctionDeclaration.name] and a structured JSON object\ncontaining the parameters and their values.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.FunctionCall}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/FunctionCall}
   [:args
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The function parameters and values in JSON object format.\nSee [FunctionDeclaration.parameters] for parameter details.\n</pre>\n\n<code>.google.protobuf.Struct args = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The args.",
     :setter-doc
       "<pre>\nOptional. The function parameters and values in JSON object format.\nSee [FunctionDeclaration.parameters] for parameter details.\n</pre>\n\n<code>.google.protobuf.Struct args = 2 [(.google.api.field_behavior) = OPTIONAL];</code>"}
    :gcp.foreign.com.google.protobuf/Struct]
   [:name
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The name of the function to call.\nMatches [FunctionDeclaration.name].\n</pre>\n\n<code>string name = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The name.",
     :setter-doc
       "<pre>\nOptional. The name of the function to call.\nMatches [FunctionDeclaration.name].\n</pre>\n\n<code>string name = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The name to set.\n@return This builder for chaining."}
    [:string {:min 1}]]
   [:partialArgs
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The partial argument value of the function call.\nIf provided, represents the arguments/fields that are streamed\nincrementally.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.PartialArg partial_args = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>",
     :setter-doc
       "<pre>\nOptional. The partial argument value of the function call.\nIf provided, represents the arguments/fields that are streamed\nincrementally.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.PartialArg partial_args = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    [:sequential {:min 1} :gcp.vertexai.api/PartialArg]]
   [:willContinue
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Whether this is the last part of the FunctionCall.\nIf true, another partial message for the current FunctionCall is expected\nto follow.\n</pre>\n\n<code>bool will_continue = 5 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The willContinue.",
     :setter-doc
       "<pre>\nOptional. Whether this is the last part of the FunctionCall.\nIf true, another partial message for the current FunctionCall is expected\nto follow.\n</pre>\n\n<code>bool will_continue = 5 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The willContinue to set.\n@return This builder for chaining."}
    :boolean]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/FunctionCall schema}
    {:gcp.global/name "gcp.vertexai.api.FunctionCall"}))