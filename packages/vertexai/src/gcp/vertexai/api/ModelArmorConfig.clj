;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.ModelArmorConfig
  {:doc
     "<pre>\nConfiguration for Model Armor integrations of prompt and responses.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.ModelArmorConfig}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.ModelArmorConfig"
   :gcp.dev/certification
     {:base-seed 1774824654997
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1774824654997 :standard 1774824654998 :stress 1774824654999}
      :protocol-hash
        "b44d2581e3979d0a946f2f9d3973f275b05bb339485f3d5eb436c47b54f7dc70"
      :timestamp "2026-03-29T22:50:55.938443093Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api ModelArmorConfig
            ModelArmorConfig$Builder]))

(declare from-edn to-edn)

(defn ^ModelArmorConfig from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/ModelArmorConfig arg)
  (let [builder (ModelArmorConfig/newBuilder)]
    (when (some? (get arg :promptTemplateName))
      (.setPromptTemplateName builder (get arg :promptTemplateName)))
    (when (some? (get arg :responseTemplateName))
      (.setResponseTemplateName builder (get arg :responseTemplateName)))
    (.build builder)))

(defn to-edn
  [^ModelArmorConfig arg]
  {:post [(global/strict! :gcp.vertexai.api/ModelArmorConfig %)]}
  (cond-> {}
    (some->> (.getPromptTemplateName arg)
             (not= ""))
      (assoc :promptTemplateName (.getPromptTemplateName arg))
    (some->> (.getResponseTemplateName arg)
             (not= ""))
      (assoc :responseTemplateName (.getResponseTemplateName arg))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nConfiguration for Model Armor integrations of prompt and responses.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.ModelArmorConfig}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/ModelArmorConfig}
   [:promptTemplateName
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The name of the Model Armor template to use for prompt\nsanitization.\n</pre>\n\n<code>\nstring prompt_template_name = 1 [(.google.api.field_behavior) = OPTIONAL, (.google.api.resource_reference) = { ... }\n</code>\n\n@return The promptTemplateName.",
     :setter-doc
       "<pre>\nOptional. The name of the Model Armor template to use for prompt\nsanitization.\n</pre>\n\n<code>\nstring prompt_template_name = 1 [(.google.api.field_behavior) = OPTIONAL, (.google.api.resource_reference) = { ... }\n</code>\n\n@param value The promptTemplateName to set.\n@return This builder for chaining."}
    [:string {:min 1}]]
   [:responseTemplateName
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The name of the Model Armor template to use for response\nsanitization.\n</pre>\n\n<code>\nstring response_template_name = 2 [(.google.api.field_behavior) = OPTIONAL, (.google.api.resource_reference) = { ... }\n</code>\n\n@return The responseTemplateName.",
     :setter-doc
       "<pre>\nOptional. The name of the Model Armor template to use for response\nsanitization.\n</pre>\n\n<code>\nstring response_template_name = 2 [(.google.api.field_behavior) = OPTIONAL, (.google.api.resource_reference) = { ... }\n</code>\n\n@param value The responseTemplateName to set.\n@return This builder for chaining."}
    [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/ModelArmorConfig schema}
    {:gcp.global/name "gcp.vertexai.api.ModelArmorConfig"}))