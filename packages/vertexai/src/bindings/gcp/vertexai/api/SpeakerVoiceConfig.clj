;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.SpeakerVoiceConfig
  {:doc
     "<pre>\nConfiguration for a single speaker in a multi-speaker setup.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.SpeakerVoiceConfig}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.SpeakerVoiceConfig"
   :gcp.dev/certification
     {:base-seed 1776627398103
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627398103 :standard 1776627398104 :stress 1776627398105}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:36:39.255528299Z"}}
  (:require [gcp.global :as global]
            [gcp.vertexai.api.VoiceConfig :as VoiceConfig])
  (:import [com.google.cloud.vertexai.api SpeakerVoiceConfig
            SpeakerVoiceConfig$Builder]))

(declare from-edn to-edn)

(defn ^SpeakerVoiceConfig from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/SpeakerVoiceConfig arg)
  (let [builder (SpeakerVoiceConfig/newBuilder)]
    (when (some? (get arg :speaker)) (.setSpeaker builder (get arg :speaker)))
    (when (some? (get arg :voiceConfig))
      (.setVoiceConfig builder (VoiceConfig/from-edn (get arg :voiceConfig))))
    (.build builder)))

(defn to-edn
  [^SpeakerVoiceConfig arg]
  {:post [(global/strict! :gcp.vertexai.api/SpeakerVoiceConfig %)]}
  (when arg
    (cond-> {:speaker (.getSpeaker arg),
             :voiceConfig (VoiceConfig/to-edn (.getVoiceConfig arg))})))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nConfiguration for a single speaker in a multi-speaker setup.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.SpeakerVoiceConfig}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/SpeakerVoiceConfig}
   [:speaker
    {:getter-doc
       "<pre>\nRequired. The name of the speaker. This should be the same as the speaker\nname used in the prompt.\n</pre>\n\n<code>string speaker = 1 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@return The speaker.",
     :setter-doc
       "<pre>\nRequired. The name of the speaker. This should be the same as the speaker\nname used in the prompt.\n</pre>\n\n<code>string speaker = 1 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@param value The speaker to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:voiceConfig
    {:getter-doc
       "<pre>\nRequired. The configuration for the voice of this speaker.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.VoiceConfig voice_config = 2 [(.google.api.field_behavior) = REQUIRED];\n</code>\n\n@return The voiceConfig.",
     :setter-doc
       "<pre>\nRequired. The configuration for the voice of this speaker.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.VoiceConfig voice_config = 2 [(.google.api.field_behavior) = REQUIRED];\n</code>"}
    :gcp.vertexai.api/VoiceConfig]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/SpeakerVoiceConfig schema}
    {:gcp.global/name "gcp.vertexai.api.SpeakerVoiceConfig"}))