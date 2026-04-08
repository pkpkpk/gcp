;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.ReplicatedVoiceConfig
  {:doc
     "<pre>\nThe configuration for the replicated voice to use.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.ReplicatedVoiceConfig}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.ReplicatedVoiceConfig"
   :gcp.dev/certification
     {:base-seed 1775465466078
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1775465466078 :standard 1775465466079 :stress 1775465466080}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-06T08:51:07.616896542Z"}}
  (:require [gcp.foreign.com.google.protobuf :as protobuf]
            [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api ReplicatedVoiceConfig
            ReplicatedVoiceConfig$Builder]
           [com.google.protobuf ByteString]))

(declare from-edn to-edn)

(defn ^ReplicatedVoiceConfig from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/ReplicatedVoiceConfig arg)
  (let [builder (ReplicatedVoiceConfig/newBuilder)]
    (when (some? (get arg :mimeType))
      (.setMimeType builder (get arg :mimeType)))
    (when (some? (get arg :voiceSampleAudio))
      (.setVoiceSampleAudio builder
                            (protobuf/ByteString-from-edn
                              (get arg :voiceSampleAudio))))
    (.build builder)))

(defn to-edn
  [^ReplicatedVoiceConfig arg]
  {:post [(global/strict! :gcp.vertexai.api/ReplicatedVoiceConfig %)]}
  (when arg
    (cond-> {}
      (some->> (.getMimeType arg)
               (not= ""))
        (assoc :mimeType (.getMimeType arg))
      (.getVoiceSampleAudio arg) (assoc :voiceSampleAudio
                                   (protobuf/ByteString-to-edn
                                     (.getVoiceSampleAudio arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nThe configuration for the replicated voice to use.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.ReplicatedVoiceConfig}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/ReplicatedVoiceConfig}
   [:mimeType
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The mimetype of the voice sample. The only currently supported\nvalue is `audio/wav`. This represents 16-bit signed little-endian wav data,\nwith a 24kHz sampling rate. `mime_type` will default to `audio/wav` if not\nset.\n</pre>\n\n<code>string mime_type = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The mimeType.",
     :setter-doc
       "<pre>\nOptional. The mimetype of the voice sample. The only currently supported\nvalue is `audio/wav`. This represents 16-bit signed little-endian wav data,\nwith a 24kHz sampling rate. `mime_type` will default to `audio/wav` if not\nset.\n</pre>\n\n<code>string mime_type = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The mimeType to set.\n@return This builder for chaining."}
    [:string {:min 1}]]
   [:voiceSampleAudio
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The sample of the custom voice.\n</pre>\n\n<code>bytes voice_sample_audio = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The voiceSampleAudio.",
     :setter-doc
       "<pre>\nOptional. The sample of the custom voice.\n</pre>\n\n<code>bytes voice_sample_audio = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The voiceSampleAudio to set.\n@return This builder for chaining."}
    :gcp.foreign.com.google.protobuf/ByteString]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/ReplicatedVoiceConfig schema}
    {:gcp.global/name "gcp.vertexai.api.ReplicatedVoiceConfig"}))