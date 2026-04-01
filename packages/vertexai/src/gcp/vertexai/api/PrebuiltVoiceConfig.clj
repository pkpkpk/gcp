;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.PrebuiltVoiceConfig
  {:doc
     "<pre>\nConfiguration for a prebuilt voice.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.PrebuiltVoiceConfig}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.PrebuiltVoiceConfig"
   :gcp.dev/certification
     {:base-seed 1774824574835
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1774824574835 :standard 1774824574836 :stress 1774824574837}
      :protocol-hash
        "b44d2581e3979d0a946f2f9d3973f275b05bb339485f3d5eb436c47b54f7dc70"
      :timestamp "2026-03-29T22:49:35.907019560Z"}}
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
  (cond-> {} (.hasVoiceName arg) (assoc :voiceName (.getVoiceName arg))))

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
    [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/PrebuiltVoiceConfig schema}
    {:gcp.global/name "gcp.vertexai.api.PrebuiltVoiceConfig"}))