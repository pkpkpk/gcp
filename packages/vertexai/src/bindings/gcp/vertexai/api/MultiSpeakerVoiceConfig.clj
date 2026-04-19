;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.MultiSpeakerVoiceConfig
  {:doc
     "<pre>\nConfiguration for a multi-speaker text-to-speech request.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.MultiSpeakerVoiceConfig}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.MultiSpeakerVoiceConfig"
   :gcp.dev/certification
     {:base-seed 1776627428488
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627428488 :standard 1776627428489 :stress 1776627428490}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:37:09.916489781Z"}}
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
                                  (mapv SpeakerVoiceConfig/from-edn
                                    (get arg :speakerVoiceConfigs))))
    (.build builder)))

(defn to-edn
  [^MultiSpeakerVoiceConfig arg]
  {:post [(global/strict! :gcp.vertexai.api/MultiSpeakerVoiceConfig %)]}
  (when arg
    (cond-> {:speakerVoiceConfigs (mapv SpeakerVoiceConfig/to-edn
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
    [:sequential {:min 1, :gen/max 2} :gcp.vertexai.api/SpeakerVoiceConfig]]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/MultiSpeakerVoiceConfig schema}
    {:gcp.global/name "gcp.vertexai.api.MultiSpeakerVoiceConfig"}))