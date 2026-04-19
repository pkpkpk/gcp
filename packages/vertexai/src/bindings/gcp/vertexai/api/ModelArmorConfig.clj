;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.ModelArmorConfig
  {:doc
     "<pre>\nConfiguration for Model Armor integrations of prompt and responses.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.ModelArmorConfig}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.ModelArmorConfig"
   :gcp.dev/certification
     {:base-seed 1776627481343
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627481343 :standard 1776627481344 :stress 1776627481345}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:38:02.224019899Z"}}
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
  (when arg
    (cond-> {}
      (some->> (.getPromptTemplateName arg)
               (not= ""))
        (assoc :promptTemplateName (.getPromptTemplateName arg))
      (some->> (.getResponseTemplateName arg)
               (not= ""))
        (assoc :responseTemplateName (.getResponseTemplateName arg)))))

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
    [:string {:min 1, :gen/max 1}]]
   [:responseTemplateName
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The name of the Model Armor template to use for response\nsanitization.\n</pre>\n\n<code>\nstring response_template_name = 2 [(.google.api.field_behavior) = OPTIONAL, (.google.api.resource_reference) = { ... }\n</code>\n\n@return The responseTemplateName.",
     :setter-doc
       "<pre>\nOptional. The name of the Model Armor template to use for response\nsanitization.\n</pre>\n\n<code>\nstring response_template_name = 2 [(.google.api.field_behavior) = OPTIONAL, (.google.api.resource_reference) = { ... }\n</code>\n\n@param value The responseTemplateName to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/ModelArmorConfig schema}
    {:gcp.global/name "gcp.vertexai.api.ModelArmorConfig"}))