(ns gcp.vertexai.v1.api.GenerationConfig
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf]
            [gcp.vertexai.v1.api.Schema :as schema])
  (:import [com.google.cloud.vertexai.api GenerationConfig
                                          GenerationConfig$RoutingConfig
                                          GenerationConfig$RoutingConfig$AutoRoutingMode
                                          GenerationConfig$RoutingConfig$AutoRoutingMode$ModelRoutingPreference
                                          GenerationConfig$RoutingConfig$ManualRoutingMode]))

(defn ^GenerationConfig$RoutingConfig$AutoRoutingMode$ModelRoutingPreference
  mrp-from-edn
  [arg]
  (if (number? arg)
    (GenerationConfig$RoutingConfig$AutoRoutingMode$ModelRoutingPreference/forNumber (int arg))
    (GenerationConfig$RoutingConfig$AutoRoutingMode$ModelRoutingPreference/valueOf ^String arg)))

(defn mrp-to-edn [arg]
  (if (int? arg)
    (.name (GenerationConfig$RoutingConfig$AutoRoutingMode$ModelRoutingPreference/forNumber arg))
    (if (string? arg)
      arg
      (if (instance? GenerationConfig$RoutingConfig$AutoRoutingMode$ModelRoutingPreference arg)
        (.name arg)
        (throw (ex-info "unsupported arg" {:arg arg}))))))

(def ^{:class GenerationConfig$RoutingConfig$AutoRoutingMode}
  AutoRoutingMode-schema
  [:map
   [:modelRoutingPreference
    {:optional true}
    [:enum "BALANCED" "PRIORITIZE_COST" "PRIORITIZE_QUALITY" "UNKNOWN" "UNRECOGNIZED" 0 1 2 3]]])

(defn ^GenerationConfig$RoutingConfig$AutoRoutingMode
  AutoRoutingMode-from-edn
  [{:keys [modelRoutingPreference] :as arg}]
  (global/strict! AutoRoutingMode-schema arg)
  (let [builder (GenerationConfig$RoutingConfig$AutoRoutingMode/newBuilder)]
    (some->> modelRoutingPreference mrp-from-edn (.setModelRoutingPreference builder))
    (.build builder)))

(defn AutoRoutingMode-to-edn
  [^GenerationConfig$RoutingConfig$AutoRoutingMode arg]
  {:post [(global/strict! AutoRoutingMode-schema %)]}
  (cond-> {}
          (.hasModelRoutingPreference arg)
          {:modelRoutingPreference (mrp-to-edn (.getModelRoutingPreference arg))}))

#!-----------------------------------------------------------------------------

(def ^{:class GenerationConfig$RoutingConfig$ManualRoutingMode}
  ManualRoutingMode-schema
  [:map [:modelName {:optional true} :string]])

(defn ^GenerationConfig$RoutingConfig$ManualRoutingMode
  ManualRoutingMode-from-edn
  [arg]
  (let [builder (GenerationConfig$RoutingConfig$ManualRoutingMode/newBuilder)]
    (.setModelName builder (:modelName arg))
    (.build builder)))

(defn ManualRoutingMode-to-edn
  [^GenerationConfig$RoutingConfig$ManualRoutingMode arg]
  {:post [(global/strict! ManualRoutingMode-schema %)]}
  (cond-> {} (.hasModelName arg) {:modelName (.getModelName arg)}))

#!-----------------------------------------------------------------------------

(def ^{:class GenerationConfig$RoutingConfig}
  RoutingConfig-schema
  [:or
   [:map
    [:autoMode AutoRoutingMode-schema]]
   [:map
    [:manualMode
     {:doc "When manual routing is set, the specified model will be used directly."}
     ManualRoutingMode-schema]]])

(defn RoutingConfig-from-edn [arg]
  (if (instance? GenerationConfig$RoutingConfig arg)
    arg
    (let [builder (GenerationConfig$RoutingConfig/newBuilder)]
      (if (contains? arg :autoMode)
        (.setAutoMode builder (AutoRoutingMode-from-edn (:autoMode arg)))
        (.setManualMode builder (ManualRoutingMode-from-edn (:manualMode arg))))
      (.build builder))))

(defn RoutingConfig-to-edn
  [^GenerationConfig$RoutingConfig arg]
  {:post [(global/strict! RoutingConfig-schema %)]}
  (if (.hasAutoMode arg)
    {:autoMode (AutoRoutingMode-to-edn (.getAutoMode arg))}
    {:manualMode (ManualRoutingMode-to-edn (.getManualMode arg))}))

#!-----------------------------------------------------------------------------

(def ^{:class GenerationConfig} schema
  [:map
   [:stopSequences
    {:optional true
     :doc "The set of character sequences (up to 5) that will stop output generation. If specified, the API will stop at the first appearance of a stop_sequence. The stop sequence will not be included as part of the response."}
    [:sequential :string]]
   [:responseMimeType
    {:optional true
     :doc "MIME type of the generated candidate text. Supported MIME types are: text/plain: (default) Text output. application/json: JSON response in the response candidates. text/x.enum: ENUM as a string response in the response candidates. Refer to the docs for a list of all supported text MIME types."}
    [:or :string protobuf/bytestring-schema]]
   [:responseSchema
    {:optional true
     :doc "Output schema of the generated candidate text. Schemas must be a subset of the OpenAPI schema and can be objects, primitives or arrays.\n\nIf set, a compatible responseMimeType must also be set. Compatible MIME types: application/json: Schema for JSON response. Refer to the JSON text generation guide for more details."}
    schema/schema]
   [:candidateCount
    {:optional true
     :doc "Number of generated responses to return.\n\nCurrently, this value can only be set to 1. If unset, this will default to 1."}
    :int]
   [:maxOutputTokens
    {:optional true
     :doc "The maximum number of tokens to include in a response candidate.\n\nNote: The default value varies by model, see the Model.output_token_limit attribute of the Model returned from the getModel function."}
    :int]
   [:temperature
    {:optional true
     :doc "Controls the randomness of the output.\n\nNote: The default value varies by model, see the Model.temperature attribute of the Model returned from the getModel function.\n\nValues can range from [0.0, 2.0]."}
    :double]
   [:topP
    {:optional true
     :doc "The maximum cumulative probability of tokens to consider when sampling.\n\nThe model uses combined Top-k and Top-p (nucleus) sampling.\n\nTokens are sorted based on their assigned probabilities so that only the most likely tokens are considered. Top-k sampling directly limits the maximum number of tokens to consider, while Nucleus sampling limits the number of tokens based on the cumulative probability.\n\nNote: The default value varies by Model and is specified by the Model.top_p attribute returned from the getModel function. An empty topK attribute indicates that the model doesn't apply top-k sampling and doesn't allow setting topK on requests."}
    :double]
   [:topK
    {:optional true
     :doc "The maximum number of tokens to consider when sampling.\n\nGemini models use Top-p (nucleus) sampling or a combination of Top-k and nucleus sampling. Top-k sampling considers the set of topK most probable tokens. Models running with nucleus sampling don't allow topK setting.\n\nNote: The default value varies by Model and is specified by the Model.top_p attribute returned from the getModel function. An empty topK attribute indicates that the model doesn't apply top-k sampling and doesn't allow setting topK on requests."}
    :int]
   [:presencePenalty
    {:optional true
     :doc "Presence penalty applied to the next token's logprobs if the token has already been seen in the response. This penalty is binary on/off and not dependant on the number of times the token is used (after the first). Use frequencyPenalty for a penalty that increases with each use.\n\nA positive penalty will discourage the use of tokens that have already been used in the response, increasing the vocabulary.\n\nA negative penalty will encourage the use of tokens that have already been used in the response, decreasing the vocabulary."}
    :double]
   [:frequencyPenalty
    {:optional true
     :doc "Frequency penalty applied to the next token's logprobs, multiplied by the number of times each token has been seen in the response so far.\n\nA positive penalty will discourage the use of tokens that have already been used, proportional to the number of times the token has been used: The more a token is used, the more difficult it is for the model to use that token again increasing the vocabulary of responses.\n\nCaution: A negative penalty will encourage the model to reuse tokens proportional to the number of times the token has been used. Small negative values will reduce the vocabulary of a response. Larger negative values will cause the model to start repeating a common token until it hits the maxOutputTokens limit: \"...the the the the the...\"."}
    :double]
   #_[:responseLogprobs
    {:optional true
     :doc "If true, export the logprobs results in response."}
    :boolean]
   #_[:logprobs
    {:optional true
     :doc "Only valid if responseLogprobs=True. This sets the number of top logprobs to return at each decoding step in the Candidate.logprobs_result."}
    :int]
   [:routingConfig {:optional true} RoutingConfig-schema]
   [:seed {:optional true} :int]])

(defn ^GenerationConfig from-edn
  [{:keys [stopSequences
           responseSchema
           responseMimeType
           candidateCount
           maxOutputTokens
           temperature
           topP
           topK
           presencePenalty
           frequencyPenalty
           seed
           routingConfig] :as arg}]
  (global/strict! schema arg)
  (let [builder (GenerationConfig/newBuilder)]
    ;;--------------------------------------------------------------------------------------
    (when stopSequences
      (.addAllStopSequences builder stopSequences))
    ;;--------------------------------------------------------------------------------------
    (when responseSchema
      (.setResponseSchema builder (schema/from-edn responseSchema)))
    ;;--------------------------------------------------------------------------------------
    (when responseMimeType
      (.setResponseMimeType builder responseMimeType))
    ;;--------------------------------------------------------------------------------------
    (when candidateCount
      (.setCandidateCount builder candidateCount))
    ;;--------------------------------------------------------------------------------------
    (when maxOutputTokens
      (.setMaxOutputTokens builder maxOutputTokens))
    ;;--------------------------------------------------------------------------------------
    (when temperature
      (.setTemperature builder temperature))
    ;;--------------------------------------------------------------------------------------
    (when topP
      (.setTopP builder topP))
    ;;--------------------------------------------------------------------------------------
    (when topK
      (.setTopK builder topK))
    ;;--------------------------------------------------------------------------------------
    (when presencePenalty
      (.setPresencePenalty builder presencePenalty))
    ;;--------------------------------------------------------------------------------------
    (some->> frequencyPenalty (.setFrequencyPenalty builder))
    ;;--------------------------------------------------------------------------------------
    (when seed
      (.setSeed builder seed))
    ;;--------------------------------------------------------------------------------------
    (when routingConfig
      (RoutingConfig-from-edn routingConfig))
    (.build builder)))

(defn to-edn [^GenerationConfig gc]
  {:post [(global/strict! schema %)]}
  (cond-> {:responseMimeType (.getResponseMimeType gc)}
          (.hasTemperature gc)
          (assoc :temperature (.getTemperature gc))
          (.hasTopK gc)
          (assoc :topK (.getTopK gc))
          (.hasTopP gc)
          (assoc :topP (.getTopP gc))
          (.hasSeed gc)
          (assoc :seed (.getSeed gc))
          (pos? (.getCandidateCount gc))
          (assoc :candidateCount (.getCandidateCount gc))
          (.hasFrequencyPenalty gc)
          (assoc :frequencyPenalty (.getFrequencyPenalty gc))
          (.hasMaxOutputTokens gc)
          (assoc :maxOutputTokens (.getMaxOutputTokens gc))
          (pos? (.getStopSequencesCount gc))
          (assoc :stopSequences (.getStopSequencesList gc))
          (.hasPresencePenalty gc)
          (assoc :presencePenalty (.getPresencePenalty gc))
          (.hasResponseSchema gc)
          (assoc :responseSchema (schema/to-edn (.getResponseSchema gc)))
          (.hasRoutingConfig gc)
          (assoc :routingConfig (RoutingConfig-to-edn (.getRoutingConfig gc)))))
