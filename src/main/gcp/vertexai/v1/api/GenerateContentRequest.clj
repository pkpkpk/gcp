(ns gcp.vertexai.v1.api.GenerateContentRequest
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.Content :as Content]
            [gcp.vertexai.v1.api.GenerationConfig :as GenerationConfig]
            [gcp.vertexai.v1.api.SafetySetting :as SafetySetting]
            [gcp.vertexai.v1.api.Tool :as Tool]
            [gcp.vertexai.v1.api.ToolConfig :as ToolConfig])
  (:import [com.google.cloud.vertexai.api GenerateContentRequest]))

(def schema
  [:map
   [:model :string]
   [:contents {:optional false} [:sequential Content/schema]]
   [:tools {:optional true} [:sequential Tool/schema]]
   [:generationConfig {:optional true} GenerationConfig/schema]
   [:toolConfig {:optional true} ToolConfig/schema]
   [:systemInstruction {:optional true} Content/schema]
   [:safetySettings {:optional true} [:sequential SafetySetting/schema]]])

(defn ^GenerateContentRequest from-edn
  [{:keys [model contents tools systemInstruction
           generationConfig toolConfig safetySettings] :as arg}]
  (global/strict! schema arg)
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