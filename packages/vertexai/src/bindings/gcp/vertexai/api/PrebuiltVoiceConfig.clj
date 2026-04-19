;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.PrebuiltVoiceConfig
  {:doc
     "<pre>\nConfiguration for a prebuilt voice.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.PrebuiltVoiceConfig}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.PrebuiltVoiceConfig"
   :gcp.dev/certification
     {:base-seed 1776627392084
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627392084 :standard 1776627392085 :stress 1776627392086}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:36:32.976397125Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api PrebuiltVoiceConfig
            PrebuiltVoiceConfig$Builder]))

(declare from-edn to-edn)

(defn ^PrebuiltVoiceConfig from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/PrebuiltVoiceConfig arg)
  (let [builder (PrebuiltVoiceConfig/newBuilder)]
    (when (some? (get arg :voiceName))
      (.setVoiceName builder (get arg :voiceName)))
    (.build builder)))

(defn to-edn
  [^PrebuiltVoiceConfig arg]
  {:post [(global/strict! :gcp.vertexai.api/PrebuiltVoiceConfig %)]}
  (when arg
    (cond-> {} (.hasVoiceName arg) (assoc :voiceName (.getVoiceName arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nConfiguration for a prebuilt voice.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.PrebuiltVoiceConfig}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/PrebuiltVoiceConfig}
   [:voiceName
    {:optional true,
     :getter-doc
       "<pre>\nThe name of the prebuilt voice to use.\n</pre>\n\n<code>optional string voice_name = 1;</code>\n\n@return The voiceName.",
     :setter-doc
       "<pre>\nThe name of the prebuilt voice to use.\n</pre>\n\n<code>optional string voice_name = 1;</code>\n\n@param value The voiceName to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/PrebuiltVoiceConfig schema}
    {:gcp.global/name "gcp.vertexai.api.PrebuiltVoiceConfig"}))