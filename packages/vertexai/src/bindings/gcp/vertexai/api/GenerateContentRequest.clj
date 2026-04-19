;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.GenerateContentRequest
  {:doc
     "<pre>\nRequest message for [PredictionService.GenerateContent].\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.GenerateContentRequest}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.GenerateContentRequest"
   :gcp.dev/certification
     {:base-seed 1776627499384
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627499384 :standard 1776627499385 :stress 1776627499386}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:38:30.302380115Z"}}
  (:require [gcp.global :as global]
            [gcp.vertexai.api.Content :as Content]
            [gcp.vertexai.api.GenerationConfig :as GenerationConfig]
            [gcp.vertexai.api.ModelArmorConfig :as ModelArmorConfig]
            [gcp.vertexai.api.SafetySetting :as SafetySetting]
            [gcp.vertexai.api.Tool :as Tool]
            [gcp.vertexai.api.ToolConfig :as ToolConfig])
  (:import [com.google.cloud.vertexai.api GenerateContentRequest
            GenerateContentRequest$Builder]))

(declare from-edn to-edn)

(defn ^GenerateContentRequest from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/GenerateContentRequest arg)
  (let [builder (GenerateContentRequest/newBuilder)]
    (when (some? (get arg :cachedContent))
      (.setCachedContent builder (get arg :cachedContent)))
    (when (seq (get arg :contents))
      (.addAllContents builder (mapv Content/from-edn (get arg :contents))))
    (when (some? (get arg :generationConfig))
      (.setGenerationConfig builder
                            (GenerationConfig/from-edn
                              (get arg :generationConfig))))
    (when (seq (get arg :labels))
      (.putAllLabels
        builder
        (into {} (map (fn [[k v]] [(name k) v])) (get arg :putAllLabels))))
    (when (some? (get arg :model)) (.setModel builder (get arg :model)))
    (when (some? (get arg :modelArmorConfig))
      (.setModelArmorConfig builder
                            (ModelArmorConfig/from-edn
                              (get arg :modelArmorConfig))))
    (when (seq (get arg :safetySettings))
      (.addAllSafetySettings builder
                             (mapv SafetySetting/from-edn
                               (get arg :safetySettings))))
    (when (some? (get arg :systemInstruction))
      (.setSystemInstruction builder
                             (Content/from-edn (get arg :systemInstruction))))
    (when (some? (get arg :toolConfig))
      (.setToolConfig builder (ToolConfig/from-edn (get arg :toolConfig))))
    (when (seq (get arg :tools))
      (.addAllTools builder (mapv Tool/from-edn (get arg :tools))))
    (.build builder)))

(defn to-edn
  [^GenerateContentRequest arg]
  {:post [(global/strict! :gcp.vertexai.api/GenerateContentRequest %)]}
  (when arg
    (cond-> {:contents (mapv Content/to-edn (.getContentsList arg)),
             :model (.getModel arg)}
      (some->> (.getCachedContent arg)
               (not= ""))
        (assoc :cachedContent (.getCachedContent arg))
      (.hasGenerationConfig arg) (assoc :generationConfig
                                   (GenerationConfig/to-edn
                                     (.getGenerationConfig arg)))
      (seq (.getLabelsMap arg))
        (assoc :labels
          (into {} (map (fn [[k v]] [(keyword k) v])) (.getLabelsMap arg)))
      (.hasModelArmorConfig arg) (assoc :modelArmorConfig
                                   (ModelArmorConfig/to-edn
                                     (.getModelArmorConfig arg)))
      (seq (.getSafetySettingsList arg)) (assoc :safetySettings
                                           (mapv SafetySetting/to-edn
                                             (.getSafetySettingsList arg)))
      (.hasSystemInstruction arg)
        (assoc :systemInstruction (Content/to-edn (.getSystemInstruction arg)))
      (.hasToolConfig arg) (assoc :toolConfig
                             (ToolConfig/to-edn (.getToolConfig arg)))
      (seq (.getToolsList arg)) (assoc :tools
                                  (mapv Tool/to-edn (.getToolsList arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nRequest message for [PredictionService.GenerateContent].\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.GenerateContentRequest}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/GenerateContentRequest}
   [:cachedContent
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The name of the cached content used as context to serve the\nprediction. Note: only used in explicit caching, where users can have\ncontrol over caching (e.g. what content to cache) and enjoy guaranteed cost\nsavings. Format:\n`projects/{project}/locations/{location}/cachedContents/{cachedContent}`\n</pre>\n\n<code>\nstring cached_content = 9 [(.google.api.field_behavior) = OPTIONAL, (.google.api.resource_reference) = { ... }\n</code>\n\n@return The cachedContent.",
     :setter-doc
       "<pre>\nOptional. The name of the cached content used as context to serve the\nprediction. Note: only used in explicit caching, where users can have\ncontrol over caching (e.g. what content to cache) and enjoy guaranteed cost\nsavings. Format:\n`projects/{project}/locations/{location}/cachedContents/{cachedContent}`\n</pre>\n\n<code>\nstring cached_content = 9 [(.google.api.field_behavior) = OPTIONAL, (.google.api.resource_reference) = { ... }\n</code>\n\n@param value The cachedContent to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:contents
    {:getter-doc
       "<pre>\nRequired. The content of the current conversation with the model.\n\nFor single-turn queries, this is a single instance. For multi-turn queries,\nthis is a repeated field that contains conversation history + latest\nrequest.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.Content contents = 2 [(.google.api.field_behavior) = REQUIRED];\n</code>",
     :setter-doc
       "<pre>\nRequired. The content of the current conversation with the model.\n\nFor single-turn queries, this is a single instance. For multi-turn queries,\nthis is a repeated field that contains conversation history + latest\nrequest.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.Content contents = 2 [(.google.api.field_behavior) = REQUIRED];\n</code>"}
    [:sequential {:min 1, :gen/max 2} :gcp.vertexai.api/Content]]
   [:generationConfig
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Generation config.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.GenerationConfig generation_config = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The generationConfig.",
     :setter-doc
       "<pre>\nOptional. Generation config.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.GenerationConfig generation_config = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.vertexai.api/GenerationConfig]
   [:labels
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The labels with user-defined metadata for the request. It is used\nfor billing and reporting only.\n\nLabel keys and values can be no longer than 63 characters\n(Unicode codepoints) and can only contain lowercase letters, numeric\ncharacters, underscores, and dashes. International characters are allowed.\nLabel values are optional. Label keys must start with a letter.\n</pre>\n\n<code>map&lt;string, string&gt; labels = 10 [(.google.api.field_behavior) = OPTIONAL];</code>",
     :setter-doc
       "<pre>\nOptional. The labels with user-defined metadata for the request. It is used\nfor billing and reporting only.\n\nLabel keys and values can be no longer than 63 characters\n(Unicode codepoints) and can only contain lowercase letters, numeric\ncharacters, underscores, and dashes. International characters are allowed.\nLabel values are optional. Label keys must start with a letter.\n</pre>\n\n<code>map&lt;string, string&gt; labels = 10 [(.google.api.field_behavior) = OPTIONAL];</code>"}
    [:map-of [:or simple-keyword? [:string {:min 1}]]
     [:string {:min 1, :gen/max 1}]]]
   [:model
    {:getter-doc
       "<pre>\nRequired. The fully qualified name of the publisher model or tuned model\nendpoint to use.\n\nPublisher model format:\n`projects/{project}/locations/{location}/publishers/&#42;&#47;models/&#42;`\n\nTuned model endpoint format:\n`projects/{project}/locations/{location}/endpoints/{endpoint}`\n</pre>\n\n<code>string model = 5 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@return The model.",
     :setter-doc
       "<pre>\nRequired. The fully qualified name of the publisher model or tuned model\nendpoint to use.\n\nPublisher model format:\n`projects/{project}/locations/{location}/publishers/&#42;&#47;models/&#42;`\n\nTuned model endpoint format:\n`projects/{project}/locations/{location}/endpoints/{endpoint}`\n</pre>\n\n<code>string model = 5 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@param value The model to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:modelArmorConfig
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Settings for prompt and response sanitization using the Model\nArmor service. If supplied, safety_settings must not be supplied.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.ModelArmorConfig model_armor_config = 11 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The modelArmorConfig.",
     :setter-doc
       "<pre>\nOptional. Settings for prompt and response sanitization using the Model\nArmor service. If supplied, safety_settings must not be supplied.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.ModelArmorConfig model_armor_config = 11 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.vertexai.api/ModelArmorConfig]
   [:safetySettings
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Per request settings for blocking unsafe content.\nEnforced on GenerateContentResponse.candidates.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.SafetySetting safety_settings = 3 [(.google.api.field_behavior) = OPTIONAL];\n</code>",
     :setter-doc
       "<pre>\nOptional. Per request settings for blocking unsafe content.\nEnforced on GenerateContentResponse.candidates.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.SafetySetting safety_settings = 3 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    [:sequential {:min 1, :gen/max 2} :gcp.vertexai.api/SafetySetting]]
   [:systemInstruction
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The user provided system instructions for the model.\nNote: only text should be used in parts and content in each part will be in\na separate paragraph.\n</pre>\n\n<code>\noptional .google.cloud.vertexai.v1.Content system_instruction = 8 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The systemInstruction.",
     :setter-doc
       "<pre>\nOptional. The user provided system instructions for the model.\nNote: only text should be used in parts and content in each part will be in\na separate paragraph.\n</pre>\n\n<code>\noptional .google.cloud.vertexai.v1.Content system_instruction = 8 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.vertexai.api/Content]
   [:toolConfig
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Tool config. This config is shared for all tools provided in the\nrequest.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.ToolConfig tool_config = 7 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The toolConfig.",
     :setter-doc
       "<pre>\nOptional. Tool config. This config is shared for all tools provided in the\nrequest.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.ToolConfig tool_config = 7 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.vertexai.api/ToolConfig]
   [:tools
    {:optional true,
     :getter-doc
       "<pre>\nOptional. A list of `Tools` the model may use to generate the next\nresponse.\n\nA `Tool` is a piece of code that enables the system to interact with\nexternal systems to perform an action, or set of actions, outside of\nknowledge and scope of the model.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.Tool tools = 6 [(.google.api.field_behavior) = OPTIONAL];\n</code>",
     :setter-doc
       "<pre>\nOptional. A list of `Tools` the model may use to generate the next\nresponse.\n\nA `Tool` is a piece of code that enables the system to interact with\nexternal systems to perform an action, or set of actions, outside of\nknowledge and scope of the model.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.Tool tools = 6 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    [:sequential {:min 1, :gen/max 2} :gcp.vertexai.api/Tool]]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/GenerateContentRequest schema}
    {:gcp.global/name "gcp.vertexai.api.GenerateContentRequest"}))