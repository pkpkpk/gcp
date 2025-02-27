(ns gcp.vertexai.v1.api.GenerationConfig
  (:require [gcp.global :as global]
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

(defn ^GenerationConfig$RoutingConfig$AutoRoutingMode
  AutoRoutingMode-from-edn
  [{:keys [modelRoutingPreference] :as arg}]
  (let [builder (GenerationConfig$RoutingConfig$AutoRoutingMode/newBuilder)]
    (some->> modelRoutingPreference mrp-from-edn (.setModelRoutingPreference builder))
    (.build builder)))

(defn AutoRoutingMode-to-edn
  [^GenerationConfig$RoutingConfig$AutoRoutingMode arg]
  (cond-> {}
          (.hasModelRoutingPreference arg)
          {:modelRoutingPreference (mrp-to-edn (.getModelRoutingPreference arg))}))

(defn ^GenerationConfig$RoutingConfig$ManualRoutingMode
  ManualRoutingMode-from-edn [arg]
  (let [builder (GenerationConfig$RoutingConfig$ManualRoutingMode/newBuilder)]
    (.setModelName builder (:modelName arg))
    (.build builder)))

(defn ManualRoutingMode-to-edn
  [^GenerationConfig$RoutingConfig$ManualRoutingMode arg]
  (cond-> {} (.hasModelName arg) {:modelName (.getModelName arg)}))

(defn RoutingConfig-from-edn [arg]
  (let [builder (GenerationConfig$RoutingConfig/newBuilder)]
    (if (contains? arg :autoMode)
      (.setAutoMode builder (AutoRoutingMode-from-edn (:autoMode arg)))
      (.setManualMode builder (ManualRoutingMode-from-edn (:manualMode arg))))
    (.build builder)))

(defn RoutingConfig-to-edn
  [^GenerationConfig$RoutingConfig arg]
  (if (.hasAutoMode arg)
    {:autoMode (AutoRoutingMode-to-edn (.getAutoMode arg))}
    {:manualMode (ManualRoutingMode-to-edn (.getManualMode arg))}))

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
  (global/strict! :gcp/vertexai.api.GenerationConfig arg)
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
  {:post [(global/strict! :gcp/vertexai.api.GenerationConfig %)]}
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
