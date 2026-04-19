;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.GenerationConfig
  {:doc
     "<pre>\nGeneration config.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.GenerationConfig}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.GenerationConfig"
   :gcp.dev/certification
     {:base-seed 1776627433763
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627433763 :standard 1776627433764 :stress 1776627433765}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:37:18.238017063Z"}}
  (:require [gcp.foreign.com.google.protobuf :as protobuf]
            [gcp.global :as global]
            [gcp.vertexai.api.ImageConfig :as ImageConfig]
            [gcp.vertexai.api.Schema :as Schema]
            [gcp.vertexai.api.SpeechConfig :as SpeechConfig])
  (:import
    [com.google.cloud.vertexai.api GenerationConfig GenerationConfig$Builder
     GenerationConfig$RoutingConfig
     GenerationConfig$RoutingConfig$AutoRoutingMode
     GenerationConfig$RoutingConfig$AutoRoutingMode$Builder
     GenerationConfig$RoutingConfig$AutoRoutingMode$ModelRoutingPreference
     GenerationConfig$RoutingConfig$Builder
     GenerationConfig$RoutingConfig$ManualRoutingMode
     GenerationConfig$RoutingConfig$ManualRoutingMode$Builder
     GenerationConfig$RoutingConfig$RoutingConfigCase
     GenerationConfig$ThinkingConfig GenerationConfig$ThinkingConfig$Builder]
    [com.google.protobuf ProtocolStringList Value]))

(declare from-edn
         to-edn
         RoutingConfig$AutoRoutingMode$ModelRoutingPreference-from-edn
         RoutingConfig$AutoRoutingMode$ModelRoutingPreference-to-edn
         RoutingConfig$AutoRoutingMode-from-edn
         RoutingConfig$AutoRoutingMode-to-edn
         RoutingConfig$AutoRoutingMode$ModelRoutingPreference-from-edn
         RoutingConfig$AutoRoutingMode$ModelRoutingPreference-to-edn
         RoutingConfig$ManualRoutingMode-from-edn
         RoutingConfig$ManualRoutingMode-to-edn
         RoutingConfig$RoutingConfigCase-from-edn
         RoutingConfig$RoutingConfigCase-to-edn
         RoutingConfig-from-edn
         RoutingConfig-to-edn
         RoutingConfig$AutoRoutingMode$ModelRoutingPreference-from-edn
         RoutingConfig$AutoRoutingMode$ModelRoutingPreference-to-edn
         RoutingConfig$AutoRoutingMode-from-edn
         RoutingConfig$AutoRoutingMode-to-edn
         RoutingConfig$AutoRoutingMode$ModelRoutingPreference-from-edn
         RoutingConfig$AutoRoutingMode$ModelRoutingPreference-to-edn
         RoutingConfig$ManualRoutingMode-from-edn
         RoutingConfig$ManualRoutingMode-to-edn
         RoutingConfig$RoutingConfigCase-from-edn
         RoutingConfig$RoutingConfigCase-to-edn
         ThinkingConfig-from-edn
         ThinkingConfig-to-edn)

(def RoutingConfig$AutoRoutingMode$ModelRoutingPreference-schema
  [:enum
   {:closed true,
    :doc
      "<pre>\nThe model routing preference.\n</pre>\n\nProtobuf enum {@code\ngoogle.cloud.vertexai.v1.GenerationConfig.RoutingConfig.AutoRoutingMode.ModelRoutingPreference}",
    :gcp/category :nested/enum,
    :gcp/key
      :gcp.vertexai.api/GenerationConfig.RoutingConfig.AutoRoutingMode.ModelRoutingPreference}
   "UNKNOWN" "PRIORITIZE_QUALITY" "BALANCED" "PRIORITIZE_COST"])

(def RoutingConfig$AutoRoutingMode$ModelRoutingPreference-schema
  [:enum
   {:closed true,
    :doc
      "<pre>\nThe model routing preference.\n</pre>\n\nProtobuf enum {@code\ngoogle.cloud.vertexai.v1.GenerationConfig.RoutingConfig.AutoRoutingMode.ModelRoutingPreference}",
    :gcp/category :nested/enum,
    :gcp/key
      :gcp.vertexai.api/GenerationConfig.RoutingConfig.AutoRoutingMode.ModelRoutingPreference}
   "UNKNOWN" "PRIORITIZE_QUALITY" "BALANCED" "PRIORITIZE_COST"])

(defn
  ^GenerationConfig$RoutingConfig$AutoRoutingMode RoutingConfig$AutoRoutingMode-from-edn
  [arg]
  (let [builder (GenerationConfig$RoutingConfig$AutoRoutingMode/newBuilder)]
    (when (some? (get arg :modelRoutingPreference))
      (.setModelRoutingPreference
        builder
        (GenerationConfig$RoutingConfig$AutoRoutingMode$ModelRoutingPreference/valueOf
          (get arg :modelRoutingPreference))))
    (.build builder)))

(defn
  RoutingConfig$AutoRoutingMode-to-edn
  [^GenerationConfig$RoutingConfig$AutoRoutingMode arg]
  (when arg
    (cond-> {}
      (.hasModelRoutingPreference arg) (assoc :modelRoutingPreference
                                         (.name (.getModelRoutingPreference
                                                  arg))))))

(def RoutingConfig$AutoRoutingMode-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nWhen automated routing is specified, the routing will be determined by\nthe pretrained routing model and customer provided model routing\npreference.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.GenerationConfig.RoutingConfig.AutoRoutingMode}",
    :gcp/category :nested/protobuf-message,
    :gcp/key :gcp.vertexai.api/GenerationConfig.RoutingConfig.AutoRoutingMode}
   [:modelRoutingPreference
    {:optional true,
     :getter-doc
       "<pre>\nThe model routing preference.\n</pre>\n\n<code>\noptional .google.cloud.vertexai.v1.GenerationConfig.RoutingConfig.AutoRoutingMode.ModelRoutingPreference model_routing_preference = 1;\n</code>\n\n@return The modelRoutingPreference.",
     :setter-doc
       "<pre>\nThe model routing preference.\n</pre>\n\n<code>\noptional .google.cloud.vertexai.v1.GenerationConfig.RoutingConfig.AutoRoutingMode.ModelRoutingPreference model_routing_preference = 1;\n</code>\n\n@param value The modelRoutingPreference to set.\n@return This builder for chaining."}
    [:enum {:closed true} "UNKNOWN" "PRIORITIZE_QUALITY" "BALANCED"
     "PRIORITIZE_COST"]]])

(defn
  ^GenerationConfig$RoutingConfig$ManualRoutingMode RoutingConfig$ManualRoutingMode-from-edn
  [arg]
  (let [builder (GenerationConfig$RoutingConfig$ManualRoutingMode/newBuilder)]
    (when (some? (get arg :modelName))
      (.setModelName builder (get arg :modelName)))
    (.build builder)))

(defn
  RoutingConfig$ManualRoutingMode-to-edn
  [^GenerationConfig$RoutingConfig$ManualRoutingMode arg]
  (when arg
    (cond-> {} (.hasModelName arg) (assoc :modelName (.getModelName arg)))))

(def RoutingConfig$ManualRoutingMode-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nWhen manual routing is set, the specified model will be used directly.\n</pre>\n\nProtobuf type {@code\ngoogle.cloud.vertexai.v1.GenerationConfig.RoutingConfig.ManualRoutingMode}",
    :gcp/category :nested/protobuf-message,
    :gcp/key :gcp.vertexai.api/GenerationConfig.RoutingConfig.ManualRoutingMode}
   [:modelName
    {:optional true,
     :getter-doc
       "<pre>\nThe model name to use. Only the public LLM models are accepted. e.g.\n'gemini-1.5-pro-001'.\n</pre>\n\n<code>optional string model_name = 1;</code>\n\n@return The modelName.",
     :setter-doc
       "<pre>\nThe model name to use. Only the public LLM models are accepted. e.g.\n'gemini-1.5-pro-001'.\n</pre>\n\n<code>optional string model_name = 1;</code>\n\n@param value The modelName to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]])

(def RoutingConfig$RoutingConfigCase-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.vertexai.api/GenerationConfig.RoutingConfig.RoutingConfigCase}
   "AUTO_MODE" "MANUAL_MODE" "ROUTINGCONFIG_NOT_SET"])

(def RoutingConfig$AutoRoutingMode$ModelRoutingPreference-schema
  [:enum
   {:closed true,
    :doc
      "<pre>\nThe model routing preference.\n</pre>\n\nProtobuf enum {@code\ngoogle.cloud.vertexai.v1.GenerationConfig.RoutingConfig.AutoRoutingMode.ModelRoutingPreference}",
    :gcp/category :nested/enum,
    :gcp/key
      :gcp.vertexai.api/GenerationConfig.RoutingConfig.AutoRoutingMode.ModelRoutingPreference}
   "UNKNOWN" "PRIORITIZE_QUALITY" "BALANCED" "PRIORITIZE_COST"])

(def RoutingConfig$AutoRoutingMode$ModelRoutingPreference-schema
  [:enum
   {:closed true,
    :doc
      "<pre>\nThe model routing preference.\n</pre>\n\nProtobuf enum {@code\ngoogle.cloud.vertexai.v1.GenerationConfig.RoutingConfig.AutoRoutingMode.ModelRoutingPreference}",
    :gcp/category :nested/enum,
    :gcp/key
      :gcp.vertexai.api/GenerationConfig.RoutingConfig.AutoRoutingMode.ModelRoutingPreference}
   "UNKNOWN" "PRIORITIZE_QUALITY" "BALANCED" "PRIORITIZE_COST"])

(defn
  ^GenerationConfig$RoutingConfig$AutoRoutingMode RoutingConfig$AutoRoutingMode-from-edn
  [arg]
  (let [builder (GenerationConfig$RoutingConfig$AutoRoutingMode/newBuilder)]
    (when (some? (get arg :modelRoutingPreference))
      (.setModelRoutingPreference
        builder
        (GenerationConfig$RoutingConfig$AutoRoutingMode$ModelRoutingPreference/valueOf
          (get arg :modelRoutingPreference))))
    (.build builder)))

(defn
  RoutingConfig$AutoRoutingMode-to-edn
  [^GenerationConfig$RoutingConfig$AutoRoutingMode arg]
  (when arg
    (cond-> {}
      (.hasModelRoutingPreference arg) (assoc :modelRoutingPreference
                                         (.name (.getModelRoutingPreference
                                                  arg))))))

(def RoutingConfig$AutoRoutingMode-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nWhen automated routing is specified, the routing will be determined by\nthe pretrained routing model and customer provided model routing\npreference.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.GenerationConfig.RoutingConfig.AutoRoutingMode}",
    :gcp/category :nested/protobuf-message,
    :gcp/key :gcp.vertexai.api/GenerationConfig.RoutingConfig.AutoRoutingMode}
   [:modelRoutingPreference
    {:optional true,
     :getter-doc
       "<pre>\nThe model routing preference.\n</pre>\n\n<code>\noptional .google.cloud.vertexai.v1.GenerationConfig.RoutingConfig.AutoRoutingMode.ModelRoutingPreference model_routing_preference = 1;\n</code>\n\n@return The modelRoutingPreference.",
     :setter-doc
       "<pre>\nThe model routing preference.\n</pre>\n\n<code>\noptional .google.cloud.vertexai.v1.GenerationConfig.RoutingConfig.AutoRoutingMode.ModelRoutingPreference model_routing_preference = 1;\n</code>\n\n@param value The modelRoutingPreference to set.\n@return This builder for chaining."}
    [:enum {:closed true} "UNKNOWN" "PRIORITIZE_QUALITY" "BALANCED"
     "PRIORITIZE_COST"]]])

(defn
  ^GenerationConfig$RoutingConfig$ManualRoutingMode RoutingConfig$ManualRoutingMode-from-edn
  [arg]
  (let [builder (GenerationConfig$RoutingConfig$ManualRoutingMode/newBuilder)]
    (when (some? (get arg :modelName))
      (.setModelName builder (get arg :modelName)))
    (.build builder)))

(defn
  RoutingConfig$ManualRoutingMode-to-edn
  [^GenerationConfig$RoutingConfig$ManualRoutingMode arg]
  (when arg
    (cond-> {} (.hasModelName arg) (assoc :modelName (.getModelName arg)))))

(def RoutingConfig$ManualRoutingMode-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nWhen manual routing is set, the specified model will be used directly.\n</pre>\n\nProtobuf type {@code\ngoogle.cloud.vertexai.v1.GenerationConfig.RoutingConfig.ManualRoutingMode}",
    :gcp/category :nested/protobuf-message,
    :gcp/key :gcp.vertexai.api/GenerationConfig.RoutingConfig.ManualRoutingMode}
   [:modelName
    {:optional true,
     :getter-doc
       "<pre>\nThe model name to use. Only the public LLM models are accepted. e.g.\n'gemini-1.5-pro-001'.\n</pre>\n\n<code>optional string model_name = 1;</code>\n\n@return The modelName.",
     :setter-doc
       "<pre>\nThe model name to use. Only the public LLM models are accepted. e.g.\n'gemini-1.5-pro-001'.\n</pre>\n\n<code>optional string model_name = 1;</code>\n\n@param value The modelName to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]])

(def RoutingConfig$RoutingConfigCase-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.vertexai.api/GenerationConfig.RoutingConfig.RoutingConfigCase}
   "AUTO_MODE" "MANUAL_MODE" "ROUTINGCONFIG_NOT_SET"])

(defn ^GenerationConfig$RoutingConfig RoutingConfig-from-edn
  [arg]
  (let [builder (GenerationConfig$RoutingConfig/newBuilder)]
    (cond (contains? arg :autoMode) (.setAutoMode
                                      builder
                                      (RoutingConfig$AutoRoutingMode-from-edn
                                        (get arg :autoMode)))
          (contains? arg :manualMode)
            (.setManualMode builder
                            (RoutingConfig$ManualRoutingMode-from-edn
                              (get arg :manualMode))))
    (.build builder)))

(defn RoutingConfig-to-edn
  [^GenerationConfig$RoutingConfig arg]
  (when arg
    (let [res (cond-> {})
          res (case (.name (.getRoutingConfigCase arg))
                "AUTO_MODE" (assoc res
                              :autoMode (RoutingConfig$AutoRoutingMode-to-edn
                                          (.getAutoMode arg)))
                "MANUAL_MODE" (assoc res
                                :manualMode
                                  (RoutingConfig$ManualRoutingMode-to-edn
                                    (.getManualMode arg)))
                res)]
      res)))

(def RoutingConfig-schema
  [:and
   {:closed true,
    :doc
      "<pre>\nThe configuration for routing the request to a specific model.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.GenerationConfig.RoutingConfig}",
    :gcp/category :nested/union-protobuf-oneof,
    :gcp/key :gcp.vertexai.api/GenerationConfig.RoutingConfig}
   [:map {:closed true}
    [:autoMode
     {:optional true,
      :getter-doc
        "<pre>\nAutomated routing.\n</pre>\n\n<code>.google.cloud.vertexai.v1.GenerationConfig.RoutingConfig.AutoRoutingMode auto_mode = 1;\n</code>\n\n@return The autoMode.",
      :setter-doc
        "<pre>\nAutomated routing.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.GenerationConfig.RoutingConfig.AutoRoutingMode auto_mode = 1;\n</code>"}
     [:ref :gcp.vertexai.api/GenerationConfig.RoutingConfig.AutoRoutingMode]]
    [:manualMode
     {:optional true,
      :getter-doc
        "<pre>\nManual routing.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.GenerationConfig.RoutingConfig.ManualRoutingMode manual_mode = 2;\n</code>\n\n@return The manualMode.",
      :setter-doc
        "<pre>\nManual routing.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.GenerationConfig.RoutingConfig.ManualRoutingMode manual_mode = 2;\n</code>"}
     [:ref :gcp.vertexai.api/GenerationConfig.RoutingConfig.ManualRoutingMode]]]
   [:fn
    {:error/message
       "Only one of these keys may be present: #{:autoMode :manualMode}"}
    (quote (fn [m]
             (<= (count (filter (set (keys m)) #{:autoMode :manualMode}))
                 1)))]])

(defn ^GenerationConfig$ThinkingConfig ThinkingConfig-from-edn
  [arg]
  (let [builder (GenerationConfig$ThinkingConfig/newBuilder)]
    (when (some? (get arg :includeThoughts))
      (.setIncludeThoughts builder (get arg :includeThoughts)))
    (when (some? (get arg :thinkingBudget))
      (.setThinkingBudget builder (int (get arg :thinkingBudget))))
    (.build builder)))

(defn ThinkingConfig-to-edn
  [^GenerationConfig$ThinkingConfig arg]
  (when arg
    (cond-> {}
      (.hasIncludeThoughts arg) (assoc :includeThoughts
                                  (.getIncludeThoughts arg))
      (.hasThinkingBudget arg) (assoc :thinkingBudget
                                 (.getThinkingBudget arg)))))

(def ThinkingConfig-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nConfig for thinking features.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.GenerationConfig.ThinkingConfig}",
    :gcp/category :nested/protobuf-message,
    :gcp/key :gcp.vertexai.api/GenerationConfig.ThinkingConfig}
   [:includeThoughts
    {:optional true,
     :getter-doc
       "<pre>\nIndicates whether to include thoughts in the response.\nIf true, thoughts are returned only when available.\n</pre>\n\n<code>optional bool include_thoughts = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The includeThoughts.",
     :setter-doc
       "<pre>\nIndicates whether to include thoughts in the response.\nIf true, thoughts are returned only when available.\n</pre>\n\n<code>optional bool include_thoughts = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The includeThoughts to set.\n@return This builder for chaining."}
    :boolean]
   [:thinkingBudget
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Indicates the thinking budget in tokens.\nThis is only applied when enable_thinking is true.\n</pre>\n\n<code>optional int32 thinking_budget = 3 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The thinkingBudget.",
     :setter-doc
       "<pre>\nOptional. Indicates the thinking budget in tokens.\nThis is only applied when enable_thinking is true.\n</pre>\n\n<code>optional int32 thinking_budget = 3 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The thinkingBudget to set.\n@return This builder for chaining."}
    :i32]])

(defn ^GenerationConfig from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/GenerationConfig arg)
  (let [builder (GenerationConfig/newBuilder)]
    (when (some? (get arg :candidateCount))
      (.setCandidateCount builder (int (get arg :candidateCount))))
    (when (some? (get arg :frequencyPenalty))
      (.setFrequencyPenalty builder (float (get arg :frequencyPenalty))))
    (when (some? (get arg :imageConfig))
      (.setImageConfig builder (ImageConfig/from-edn (get arg :imageConfig))))
    (when (some? (get arg :logprobs))
      (.setLogprobs builder (int (get arg :logprobs))))
    (when (some? (get arg :maxOutputTokens))
      (.setMaxOutputTokens builder (int (get arg :maxOutputTokens))))
    (when (some? (get arg :presencePenalty))
      (.setPresencePenalty builder (float (get arg :presencePenalty))))
    (when (some? (get arg :responseJsonSchema))
      (.setResponseJsonSchema builder
                              (protobuf/Value-from-edn
                                (get arg :responseJsonSchema))))
    (when (some? (get arg :responseLogprobs))
      (.setResponseLogprobs builder (get arg :responseLogprobs)))
    (when (some? (get arg :responseMimeType))
      (.setResponseMimeType builder (get arg :responseMimeType)))
    (when (some? (get arg :responseSchema))
      (.setResponseSchema builder (Schema/from-edn (get arg :responseSchema))))
    (when (some? (get arg :routingConfig))
      (.setRoutingConfig builder
                         (RoutingConfig-from-edn (get arg :routingConfig))))
    (when (some? (get arg :seed)) (.setSeed builder (int (get arg :seed))))
    (when (some? (get arg :speechConfig))
      (.setSpeechConfig builder
                        (SpeechConfig/from-edn (get arg :speechConfig))))
    (when (seq (get arg :stopSequences))
      (.addAllStopSequences builder (seq (get arg :stopSequences))))
    (when (some? (get arg :temperature))
      (.setTemperature builder (float (get arg :temperature))))
    (when (some? (get arg :thinkingConfig))
      (.setThinkingConfig builder
                          (ThinkingConfig-from-edn (get arg :thinkingConfig))))
    (when (some? (get arg :topK)) (.setTopK builder (float (get arg :topK))))
    (when (some? (get arg :topP)) (.setTopP builder (float (get arg :topP))))
    (.build builder)))

(defn to-edn
  [^GenerationConfig arg]
  {:post [(global/strict! :gcp.vertexai.api/GenerationConfig %)]}
  (when arg
    (cond-> {}
      (.hasFrequencyPenalty arg) (assoc :frequencyPenalty
                                   (.getFrequencyPenalty arg))
      (.hasImageConfig arg) (assoc :imageConfig
                              (ImageConfig/to-edn (.getImageConfig arg)))
      (.hasLogprobs arg) (assoc :logprobs (.getLogprobs arg))
      (.hasMaxOutputTokens arg) (assoc :maxOutputTokens
                                  (.getMaxOutputTokens arg))
      (.hasPresencePenalty arg) (assoc :presencePenalty
                                  (.getPresencePenalty arg))
      (.hasResponseJsonSchema arg) (assoc :responseJsonSchema
                                     (protobuf/Value-to-edn
                                       (.getResponseJsonSchema arg)))
      (.hasResponseLogprobs arg) (assoc :responseLogprobs
                                   (.getResponseLogprobs arg))
      (some->> (.getResponseMimeType arg)
               (not= ""))
        (assoc :responseMimeType (.getResponseMimeType arg))
      (.hasResponseSchema arg) (assoc :responseSchema
                                 (Schema/to-edn (.getResponseSchema arg)))
      (.hasRoutingConfig arg) (assoc :routingConfig
                                (RoutingConfig-to-edn (.getRoutingConfig arg)))
      (.hasSeed arg) (assoc :seed (.getSeed arg))
      (.hasSpeechConfig arg) (assoc :speechConfig
                               (SpeechConfig/to-edn (.getSpeechConfig arg)))
      (seq (.getStopSequencesList arg)) (assoc :stopSequences
                                          (protobuf/ProtocolStringList-to-edn
                                            (.getStopSequencesList arg)))
      (.hasTemperature arg) (assoc :temperature (.getTemperature arg))
      (.hasThinkingConfig arg)
        (assoc :thinkingConfig (ThinkingConfig-to-edn (.getThinkingConfig arg)))
      (.hasTopK arg) (assoc :topK (.getTopK arg))
      (.hasTopP arg) (assoc :topP (.getTopP arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nGeneration config.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.GenerationConfig}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/GenerationConfig}
   [:frequencyPenalty
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Frequency penalties.\n</pre>\n\n<code>optional float frequency_penalty = 9 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The frequencyPenalty.",
     :setter-doc
       "<pre>\nOptional. Frequency penalties.\n</pre>\n\n<code>optional float frequency_penalty = 9 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The frequencyPenalty to set.\n@return This builder for chaining."}
    :f32]
   [:imageConfig
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Config for image generation features.\n</pre>\n\n<code>\noptional .google.cloud.vertexai.v1.ImageConfig image_config = 30 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The imageConfig.",
     :setter-doc
       "<pre>\nOptional. Config for image generation features.\n</pre>\n\n<code>\noptional .google.cloud.vertexai.v1.ImageConfig image_config = 30 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.vertexai.api/ImageConfig]
   [:logprobs
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Logit probabilities.\n</pre>\n\n<code>optional int32 logprobs = 7 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The logprobs.",
     :setter-doc
       "<pre>\nOptional. Logit probabilities.\n</pre>\n\n<code>optional int32 logprobs = 7 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The logprobs to set.\n@return This builder for chaining."}
    :i32]
   [:maxOutputTokens
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The maximum number of output tokens to generate per message.\n</pre>\n\n<code>optional int32 max_output_tokens = 5 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The maxOutputTokens.",
     :setter-doc
       "<pre>\nOptional. The maximum number of output tokens to generate per message.\n</pre>\n\n<code>optional int32 max_output_tokens = 5 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The maxOutputTokens to set.\n@return This builder for chaining."}
    :i32]
   [:presencePenalty
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Positive penalties.\n</pre>\n\n<code>optional float presence_penalty = 8 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The presencePenalty.",
     :setter-doc
       "<pre>\nOptional. Positive penalties.\n</pre>\n\n<code>optional float presence_penalty = 8 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The presencePenalty to set.\n@return This builder for chaining."}
    :f32]
   [:responseJsonSchema
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Output schema of the generated response. This is an alternative\nto `response_schema` that accepts [JSON Schema](https://json-schema.org/).\n\nIf set, `response_schema` must be omitted, but `response_mime_type` is\nrequired.\n\nWhile the full JSON Schema may be sent, not all features are supported.\nSpecifically, only the following properties are supported:\n\n- `$id`\n- `$defs`\n- `$ref`\n- `$anchor`\n- `type`\n- `format`\n- `title`\n- `description`\n- `enum` (for strings and numbers)\n- `items`\n- `prefixItems`\n- `minItems`\n- `maxItems`\n- `minimum`\n- `maximum`\n- `anyOf`\n- `oneOf` (interpreted the same as `anyOf`)\n- `properties`\n- `additionalProperties`\n- `required`\n\nThe non-standard `propertyOrdering` property may also be set.\n\nCyclic references are unrolled to a limited degree and, as such, may only\nbe used within non-required properties. (Nullable properties are not\nsufficient.) If `$ref` is set on a sub-schema, no other properties, except\nfor than those starting as a `$`, may be set.\n</pre>\n\n<code>\noptional .google.protobuf.Value response_json_schema = 28 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The responseJsonSchema.",
     :setter-doc
       "<pre>\nOptional. Output schema of the generated response. This is an alternative\nto `response_schema` that accepts [JSON Schema](https://json-schema.org/).\n\nIf set, `response_schema` must be omitted, but `response_mime_type` is\nrequired.\n\nWhile the full JSON Schema may be sent, not all features are supported.\nSpecifically, only the following properties are supported:\n\n- `$id`\n- `$defs`\n- `$ref`\n- `$anchor`\n- `type`\n- `format`\n- `title`\n- `description`\n- `enum` (for strings and numbers)\n- `items`\n- `prefixItems`\n- `minItems`\n- `maxItems`\n- `minimum`\n- `maximum`\n- `anyOf`\n- `oneOf` (interpreted the same as `anyOf`)\n- `properties`\n- `additionalProperties`\n- `required`\n\nThe non-standard `propertyOrdering` property may also be set.\n\nCyclic references are unrolled to a limited degree and, as such, may only\nbe used within non-required properties. (Nullable properties are not\nsufficient.) If `$ref` is set on a sub-schema, no other properties, except\nfor than those starting as a `$`, may be set.\n</pre>\n\n<code>\noptional .google.protobuf.Value response_json_schema = 28 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.foreign.com.google.protobuf/Value]
   [:responseLogprobs
    {:optional true,
     :getter-doc
       "<pre>\nOptional. If true, export the logprobs results in response.\n</pre>\n\n<code>optional bool response_logprobs = 18 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The responseLogprobs.",
     :setter-doc
       "<pre>\nOptional. If true, export the logprobs results in response.\n</pre>\n\n<code>optional bool response_logprobs = 18 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The responseLogprobs to set.\n@return This builder for chaining."}
    :boolean]
   [:responseMimeType
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Output response mimetype of the generated candidate text.\nSupported mimetype:\n- `text/plain`: (default) Text output.\n- `application/json`: JSON response in the candidates.\nThe model needs to be prompted to output the appropriate response type,\notherwise the behavior is undefined.\nThis is a preview feature.\n</pre>\n\n<code>string response_mime_type = 13 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The responseMimeType.",
     :setter-doc
       "<pre>\nOptional. Output response mimetype of the generated candidate text.\nSupported mimetype:\n- `text/plain`: (default) Text output.\n- `application/json`: JSON response in the candidates.\nThe model needs to be prompted to output the appropriate response type,\notherwise the behavior is undefined.\nThis is a preview feature.\n</pre>\n\n<code>string response_mime_type = 13 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The responseMimeType to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:responseSchema
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The `Schema` object allows the definition of input and output\ndata types. These types can be objects, but also primitives and arrays.\nRepresents a select subset of an [OpenAPI 3.0 schema\nobject](https://spec.openapis.org/oas/v3.0.3#schema).\nIf set, a compatible response_mime_type must also be set.\nCompatible mimetypes:\n`application/json`: Schema for JSON response.\n</pre>\n\n<code>\noptional .google.cloud.vertexai.v1.Schema response_schema = 16 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The responseSchema.",
     :setter-doc
       "<pre>\nOptional. The `Schema` object allows the definition of input and output\ndata types. These types can be objects, but also primitives and arrays.\nRepresents a select subset of an [OpenAPI 3.0 schema\nobject](https://spec.openapis.org/oas/v3.0.3#schema).\nIf set, a compatible response_mime_type must also be set.\nCompatible mimetypes:\n`application/json`: Schema for JSON response.\n</pre>\n\n<code>\noptional .google.cloud.vertexai.v1.Schema response_schema = 16 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.vertexai.api/Schema]
   [:routingConfig
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Routing configuration.\n</pre>\n\n<code>\noptional .google.cloud.vertexai.v1.GenerationConfig.RoutingConfig routing_config = 17 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The routingConfig.",
     :setter-doc
       "<pre>\nOptional. Routing configuration.\n</pre>\n\n<code>\noptional .google.cloud.vertexai.v1.GenerationConfig.RoutingConfig routing_config = 17 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    [:ref :gcp.vertexai.api/GenerationConfig.RoutingConfig]]
   [:seed
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Seed.\n</pre>\n\n<code>optional int32 seed = 12 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The seed.",
     :setter-doc
       "<pre>\nOptional. Seed.\n</pre>\n\n<code>optional int32 seed = 12 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The seed to set.\n@return This builder for chaining."}
    :i32]
   [:speechConfig
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The speech generation config.\n</pre>\n\n<code>\noptional .google.cloud.vertexai.v1.SpeechConfig speech_config = 23 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The speechConfig.",
     :setter-doc
       "<pre>\nOptional. The speech generation config.\n</pre>\n\n<code>\noptional .google.cloud.vertexai.v1.SpeechConfig speech_config = 23 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.vertexai.api/SpeechConfig]
   [:stopSequences
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Stop sequences.\n</pre>\n\n<code>repeated string stop_sequences = 6 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return A list containing the stopSequences.",
     :setter-doc
       "<pre>\nOptional. Stop sequences.\n</pre>\n\n<code>repeated string stop_sequences = 6 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param values The stopSequences to add.\n@return This builder for chaining."}
    :gcp.foreign.com.google.protobuf/ProtocolStringList]
   [:temperature
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Controls the randomness of predictions.\n</pre>\n\n<code>optional float temperature = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The temperature.",
     :setter-doc
       "<pre>\nOptional. Controls the randomness of predictions.\n</pre>\n\n<code>optional float temperature = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The temperature to set.\n@return This builder for chaining."}
    :f32]
   [:thinkingConfig
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Config for thinking features.\nAn error will be returned if this field is set for models that don't\nsupport thinking.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.GenerationConfig.ThinkingConfig thinking_config = 25 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The thinkingConfig.",
     :setter-doc
       "<pre>\nOptional. Config for thinking features.\nAn error will be returned if this field is set for models that don't\nsupport thinking.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.GenerationConfig.ThinkingConfig thinking_config = 25 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    [:ref :gcp.vertexai.api/GenerationConfig.ThinkingConfig]]
   [:topK
    {:optional true,
     :getter-doc
       "<pre>\nOptional. If specified, top-k sampling will be used.\n</pre>\n\n<code>optional float top_k = 3 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The topK.",
     :setter-doc
       "<pre>\nOptional. If specified, top-k sampling will be used.\n</pre>\n\n<code>optional float top_k = 3 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The topK to set.\n@return This builder for chaining."}
    :f32]
   [:topP
    {:optional true,
     :getter-doc
       "<pre>\nOptional. If specified, nucleus sampling will be used.\n</pre>\n\n<code>optional float top_p = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The topP.",
     :setter-doc
       "<pre>\nOptional. If specified, nucleus sampling will be used.\n</pre>\n\n<code>optional float top_p = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The topP to set.\n@return This builder for chaining."}
    :f32]])

(global/include-schema-registry!
  (with-meta
    {:gcp.vertexai.api/GenerationConfig schema,
     :gcp.vertexai.api/GenerationConfig.RoutingConfig RoutingConfig-schema,
     :gcp.vertexai.api/GenerationConfig.RoutingConfig.AutoRoutingMode
       RoutingConfig$AutoRoutingMode-schema,
     :gcp.vertexai.api/GenerationConfig.RoutingConfig.AutoRoutingMode.ModelRoutingPreference
       RoutingConfig$AutoRoutingMode$ModelRoutingPreference-schema,
     :gcp.vertexai.api/GenerationConfig.RoutingConfig.ManualRoutingMode
       RoutingConfig$ManualRoutingMode-schema,
     :gcp.vertexai.api/GenerationConfig.RoutingConfig.RoutingConfigCase
       RoutingConfig$RoutingConfigCase-schema,
     :gcp.vertexai.api/GenerationConfig.ThinkingConfig ThinkingConfig-schema}
    {:gcp.global/name "gcp.vertexai.api.GenerationConfig"}))