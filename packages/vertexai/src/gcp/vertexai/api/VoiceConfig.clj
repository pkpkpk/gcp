;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.VoiceConfig
  {:doc
     "<pre>\nConfiguration for a voice.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.VoiceConfig}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.VoiceConfig"
   :gcp.dev/certification
     {:base-seed 1774824578828
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1774824578828 :standard 1774824578829 :stress 1774824578830}
      :protocol-hash
        "b44d2581e3979d0a946f2f9d3973f275b05bb339485f3d5eb436c47b54f7dc70"
      :timestamp "2026-03-29T22:49:39.762804055Z"}}
  (:require [gcp.global :as global]
            [gcp.vertexai.api.PrebuiltVoiceConfig :as PrebuiltVoiceConfig]
            [gcp.vertexai.api.ReplicatedVoiceConfig :as ReplicatedVoiceConfig])
  (:import [com.google.cloud.vertexai.api VoiceConfig VoiceConfig$Builder
            VoiceConfig$VoiceConfigCase]))

(declare from-edn to-edn VoiceConfigCase-from-edn VoiceConfigCase-to-edn)

(def VoiceConfigCase-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.vertexai.api/VoiceConfig.VoiceConfigCase}
   "PREBUILT_VOICE_CONFIG" "REPLICATED_VOICE_CONFIG" "VOICECONFIG_NOT_SET"])

(defn ^VoiceConfig from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/VoiceConfig arg)
  (let [builder (VoiceConfig/newBuilder)]
    (clojure.core/cond (contains? arg :prebuiltVoiceConfig)
                         (.setPrebuiltVoiceConfig builder
                                                  (PrebuiltVoiceConfig/from-edn
                                                    (get arg
                                                         :prebuiltVoiceConfig)))
                       (contains? arg :replicatedVoiceConfig)
                         (.setReplicatedVoiceConfig
                           builder
                           (ReplicatedVoiceConfig/from-edn
                             (get arg :replicatedVoiceConfig))))
    (.build builder)))

(defn to-edn
  [^VoiceConfig arg]
  {:post [(global/strict! :gcp.vertexai.api/VoiceConfig %)]}
  (clojure.core/let [res (cond-> {})
                     res (case (.name (.getVoiceConfigCase arg))
                           "PREBUILT_VOICE_CONFIG"
                             (clojure.core/assoc res
                               :prebuiltVoiceConfig (PrebuiltVoiceConfig/to-edn
                                                      (.getPrebuiltVoiceConfig
                                                        arg)))
                           "REPLICATED_VOICE_CONFIG"
                             (clojure.core/assoc res
                               :replicatedVoiceConfig
                                 (ReplicatedVoiceConfig/to-edn
                                   (.getReplicatedVoiceConfig arg)))
                           res)]
    res))

(def schema
  [:and
   {:closed true,
    :doc
      "<pre>\nConfiguration for a voice.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.VoiceConfig}",
    :gcp/category :union-protobuf-oneof,
    :gcp/key :gcp.vertexai.api/VoiceConfig}
   [:map {:closed true}
    [:prebuiltVoiceConfig
     {:optional true,
      :getter-doc
        "<pre>\nThe configuration for a prebuilt voice.\n</pre>\n\n<code>.google.cloud.vertexai.v1.PrebuiltVoiceConfig prebuilt_voice_config = 1;</code>\n\n@return The prebuiltVoiceConfig.",
      :setter-doc
        "<pre>\nThe configuration for a prebuilt voice.\n</pre>\n\n<code>.google.cloud.vertexai.v1.PrebuiltVoiceConfig prebuilt_voice_config = 1;</code>"}
     :gcp.vertexai.api/PrebuiltVoiceConfig]
    [:replicatedVoiceConfig
     {:optional true,
      :getter-doc
        "<pre>\nOptional. The configuration for a replicated voice. This enables users to\nreplicate a voice from an audio sample.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.ReplicatedVoiceConfig replicated_voice_config = 3 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The replicatedVoiceConfig.",
      :setter-doc
        "<pre>\nOptional. The configuration for a replicated voice. This enables users to\nreplicate a voice from an audio sample.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.ReplicatedVoiceConfig replicated_voice_config = 3 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
     :gcp.vertexai.api/ReplicatedVoiceConfig]]
   [:or
    [:map
     [:prebuiltVoiceConfig
      {:optional true,
       :getter-doc
         "<pre>\nThe configuration for a prebuilt voice.\n</pre>\n\n<code>.google.cloud.vertexai.v1.PrebuiltVoiceConfig prebuilt_voice_config = 1;</code>\n\n@return The prebuiltVoiceConfig.",
       :setter-doc
         "<pre>\nThe configuration for a prebuilt voice.\n</pre>\n\n<code>.google.cloud.vertexai.v1.PrebuiltVoiceConfig prebuilt_voice_config = 1;</code>"}
      :gcp.vertexai.api/PrebuiltVoiceConfig]]
    [:map
     [:replicatedVoiceConfig
      {:optional true,
       :getter-doc
         "<pre>\nOptional. The configuration for a replicated voice. This enables users to\nreplicate a voice from an audio sample.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.ReplicatedVoiceConfig replicated_voice_config = 3 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The replicatedVoiceConfig.",
       :setter-doc
         "<pre>\nOptional. The configuration for a replicated voice. This enables users to\nreplicate a voice from an audio sample.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.ReplicatedVoiceConfig replicated_voice_config = 3 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
      :gcp.vertexai.api/ReplicatedVoiceConfig]]]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/VoiceConfig schema,
              :gcp.vertexai.api/VoiceConfig.VoiceConfigCase
                VoiceConfigCase-schema}
    {:gcp.global/name "gcp.vertexai.api.VoiceConfig"}))