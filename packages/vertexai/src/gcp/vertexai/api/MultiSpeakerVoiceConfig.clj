;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.MultiSpeakerVoiceConfig
  {:doc
     "<pre>\nConfiguration for a multi-speaker text-to-speech request.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.MultiSpeakerVoiceConfig}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.MultiSpeakerVoiceConfig"
   :gcp.dev/certification
     {:base-seed 1774824611089
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1774824611089 :standard 1774824611090 :stress 1774824611091}
      :protocol-hash
        "b44d2581e3979d0a946f2f9d3973f275b05bb339485f3d5eb436c47b54f7dc70"
      :timestamp "2026-03-29T22:50:12.070328406Z"}}
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
  (cond-> {:speakerVoiceConfigs (map SpeakerVoiceConfig/to-edn
                                  (.getSpeakerVoiceConfigsList arg))}))

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