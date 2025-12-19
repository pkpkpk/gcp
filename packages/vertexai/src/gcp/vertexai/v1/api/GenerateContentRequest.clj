(ns gcp.vertexai.v1.api.GenerateContentRequest
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.Content :as Content]
            [gcp.vertexai.v1.api.SafetySetting :as SafetySetting]
            [gcp.vertexai.v1.api.Tool :as Tool]
            [gcp.vertexai.v1.api.ToolConfig :as ToolConfig]
            [gcp.vertexai.v1.api.GenerationConfig :as GenerationConfig])
  (:import (com.google.cloud.vertexai.api GenerateContentRequest)))

(global/register-schema!
  :gcp.vertexai.synth/ModelConfig
  [:map
   [:generationConfig {:optional true} :gcp.vertexai.v1.api/GenerationConfig]
   [:model :string]
   [:safetySettings {:optional true} [:sequential :gcp.vertexai.v1.api/SafetySetting]]
   [:systemInstruction {:optional true} :gcp.vertexai.v1.api/Content]
   [:toolConfig {:optional true} :gcp.vertexai.v1.api/ToolConfig]
   [:tools {:optional true} [:sequential :gcp.vertexai.v1.api/Tool]]])

(defn ^GenerateContentRequest from-edn
  [{:keys [contents model generationConfig safetySettings systemInstruction toolConfig tools] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/GenerateContentRequest arg)
  (let [builder (GenerateContentRequest/newBuilder)]
    (.setModel builder model)
    (some->> (not-empty contents) (map Content/from-edn) (.addAllContents builder))
    (some->> systemInstruction Content/from-edn (.setSystemInstruction builder))
    (some->> generationConfig GenerationConfig/from-edn (.setGenerationConfig builder))
    (some->> (not-empty tools) (map Tool/from-edn) (.addAllTools builder))
    (some->> (not-empty safetySettings) (map SafetySetting/from-edn) (.addAllSafetySettings builder))
    (some->> toolConfig ToolConfig/from-edn (.setToolConfig builder))
    (.build builder)))

(defn to-edn [^GenerateContentRequest arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/GenerateContentRequest %)]}
  (cond-> {:model (.getModel arg)}
          (.hasGenerationConfig arg)
          (assoc :generationConfig (GenerationConfig/to-edn (.getGenerationConfig arg)))
          (.hasToolConfig arg)
          (assoc :toolConfig (ToolConfig/to-edn (.getToolConfig arg)))
          (pos? (.getToolsCount arg))
          (assoc :tools (mapv Tool/to-edn (.getToolsList arg)))
          (.hasSystemInstruction arg)
          (assoc :systemInstruction (Content/to-edn (.getSystemInstruction arg)))
          (pos? (.getContentsCount arg))
          (assoc :contents (mapv Content/to-edn (.getContentsList arg)))
          (pos? (.getSafetySettingsCount arg))
          (assoc :safetySettings (mapv SafetySetting/to-edn (.getSafetySettingsList arg)))))

(def schema
  [:and
   {:ns               'gcp.vertexai.v1.api.GenerateContentRequest
    :from-edn         'gcp.vertexai.v1.api.GenerateContentRequest/from-edn
    :to-edn           'gcp.vertexai.v1.api.GenerateContentRequest/to-edn
    :doc              "Request message for [PredictionService.GenerateContent]"
    :generativeai/url "https://ai.google.dev/api/generate-content#request-body"
    :protobuf/type    "google.cloud.vertexai.v1.GenerateContentRequest"
    :class            'com.google.cloud.vertexai.api.GenerateContentRequest
    :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.GenerateContentRequest"}
                                                 :gcp.vertexai.synth/ModelConfig   [:map
    [:contents {:optional false} [:sequential :gcp.vertexai.v1.api/Content]]]])

(global/register-schema! :gcp.vertexai.v1.api/GenerateContentRequest schema)
