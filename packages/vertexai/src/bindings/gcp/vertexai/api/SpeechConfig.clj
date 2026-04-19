;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.SpeechConfig
  {:doc
     "<pre>\nConfiguration for speech generation.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.SpeechConfig}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.SpeechConfig"
   :gcp.dev/certification
     {:base-seed 1776627431019
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627431019 :standard 1776627431020 :stress 1776627431021}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:37:12.665477750Z"}}
  (:require [gcp.global :as global]
            [gcp.vertexai.api.MultiSpeakerVoiceConfig :as
             MultiSpeakerVoiceConfig]
            [gcp.vertexai.api.VoiceConfig :as VoiceConfig])
  (:import [com.google.cloud.vertexai.api SpeechConfig SpeechConfig$Builder]))

(declare from-edn to-edn)

(defn ^SpeechConfig from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/SpeechConfig arg)
  (let [builder (SpeechConfig/newBuilder)]
    (when (some? (get arg :languageCode))
      (.setLanguageCode builder (get arg :languageCode)))
    (when (some? (get arg :multiSpeakerVoiceConfig))
      (.setMultiSpeakerVoiceConfig builder
                                   (MultiSpeakerVoiceConfig/from-edn
                                     (get arg :multiSpeakerVoiceConfig))))
    (when (some? (get arg :voiceConfig))
      (.setVoiceConfig builder (VoiceConfig/from-edn (get arg :voiceConfig))))
    (.build builder)))

(defn to-edn
  [^SpeechConfig arg]
  {:post [(global/strict! :gcp.vertexai.api/SpeechConfig %)]}
  (when arg
    (cond-> {}
      (some->> (.getLanguageCode arg)
               (not= ""))
        (assoc :languageCode (.getLanguageCode arg))
      (.hasMultiSpeakerVoiceConfig arg) (assoc :multiSpeakerVoiceConfig
                                          (MultiSpeakerVoiceConfig/to-edn
                                            (.getMultiSpeakerVoiceConfig arg)))
      (.hasVoiceConfig arg) (assoc :voiceConfig
                              (VoiceConfig/to-edn (.getVoiceConfig arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nConfiguration for speech generation.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.SpeechConfig}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/SpeechConfig}
   [:languageCode
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The language code (ISO 639-1) for the speech synthesis.\n</pre>\n\n<code>string language_code = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The languageCode.",
     :setter-doc
       "<pre>\nOptional. The language code (ISO 639-1) for the speech synthesis.\n</pre>\n\n<code>string language_code = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The languageCode to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:multiSpeakerVoiceConfig
    {:optional true,
     :getter-doc
       "<pre>\nThe configuration for a multi-speaker text-to-speech request.\nThis field is mutually exclusive with `voice_config`.\n</pre>\n\n<code>.google.cloud.vertexai.v1.MultiSpeakerVoiceConfig multi_speaker_voice_config = 3;</code>\n\n@return The multiSpeakerVoiceConfig.",
     :setter-doc
       "<pre>\nThe configuration for a multi-speaker text-to-speech request.\nThis field is mutually exclusive with `voice_config`.\n</pre>\n\n<code>.google.cloud.vertexai.v1.MultiSpeakerVoiceConfig multi_speaker_voice_config = 3;\n</code>"}
    :gcp.vertexai.api/MultiSpeakerVoiceConfig]
   [:voiceConfig
    {:optional true,
     :getter-doc
       "<pre>\nThe configuration for the voice to use.\n</pre>\n\n<code>.google.cloud.vertexai.v1.VoiceConfig voice_config = 1;</code>\n\n@return The voiceConfig.",
     :setter-doc
       "<pre>\nThe configuration for the voice to use.\n</pre>\n\n<code>.google.cloud.vertexai.v1.VoiceConfig voice_config = 1;</code>"}
    :gcp.vertexai.api/VoiceConfig]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/SpeechConfig schema}
    {:gcp.global/name "gcp.vertexai.api.SpeechConfig"}))