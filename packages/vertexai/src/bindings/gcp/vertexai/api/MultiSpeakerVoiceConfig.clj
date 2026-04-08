;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.MultiSpeakerVoiceConfig
  {:doc
     "<pre>\nConfiguration for a multi-speaker text-to-speech request.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.MultiSpeakerVoiceConfig}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.MultiSpeakerVoiceConfig"
   :gcp.dev/certification
     {:base-seed 1775465502145
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1775465502145 :standard 1775465502146 :stress 1775465502147}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-06T08:51:43.994514311Z"}}
  (:require [gcp.global :as global]
            [gcp.vertexai.api.SpeakerVoiceConfig :as SpeakerVoiceConfig])
  (:import [com.google.cloud.vertexai.api MultiSpeakerVoiceConfig
            MultiSpeakerVoiceConfig$Builder]))

(declare from-edn to-edn)

(defn ^MultiSpeakerVoiceConfig from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/MultiSpeakerVoiceConfig arg)
  (let [builder (MultiSpeakerVoiceConfig/newBuilder)]
    (when (seq (get arg :speakerVoiceConfigs))
      (.addAllSpeakerVoiceConfigs builder
                                  (map SpeakerVoiceConfig/from-edn
                                    (get arg :speakerVoiceConfigs))))
    (.build builder)))

(defn to-edn
  [^MultiSpeakerVoiceConfig arg]
  {:post [(global/strict! :gcp.vertexai.api/MultiSpeakerVoiceConfig %)]}
  (when arg
    (cond-> {:speakerVoiceConfigs (map SpeakerVoiceConfig/to-edn
                                    (.getSpeakerVoiceConfigsList arg))})))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nConfiguration for a multi-speaker text-to-speech request.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.MultiSpeakerVoiceConfig}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/MultiSpeakerVoiceConfig}
   [:speakerVoiceConfigs
    {:getter-doc
       "<pre>\nRequired. A list of configurations for the voices of the speakers. Exactly\ntwo speaker voice configurations must be provided.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.SpeakerVoiceConfig speaker_voice_configs = 2 [(.google.api.field_behavior) = REQUIRED];\n</code>",
     :setter-doc
       "<pre>\nRequired. A list of configurations for the voices of the speakers. Exactly\ntwo speaker voice configurations must be provided.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.SpeakerVoiceConfig speaker_voice_configs = 2 [(.google.api.field_behavior) = REQUIRED];\n</code>"}
    [:sequential {:min 1} :gcp.vertexai.api/SpeakerVoiceConfig]]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/MultiSpeakerVoiceConfig schema}
    {:gcp.global/name "gcp.vertexai.api.MultiSpeakerVoiceConfig"}))