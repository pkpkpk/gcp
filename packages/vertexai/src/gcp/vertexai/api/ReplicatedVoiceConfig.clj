;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.ReplicatedVoiceConfig
  {:doc
     "<pre>\nThe configuration for the replicated voice to use.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.ReplicatedVoiceConfig}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.ReplicatedVoiceConfig"
   :gcp.dev/certification
     {:base-seed 1774824576896
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1774824576896 :standard 1774824576897 :stress 1774824576898}
      :protocol-hash
        "b44d2581e3979d0a946f2f9d3973f275b05bb339485f3d5eb436c47b54f7dc70"
      :timestamp "2026-03-29T22:49:37.811785071Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api ReplicatedVoiceConfig
            ReplicatedVoiceConfig$Builder]))

(declare from-edn to-edn)

(defn ^ReplicatedVoiceConfig from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/ReplicatedVoiceConfig arg)
  (let [builder (ReplicatedVoiceConfig/newBuilder)]
    (when (some? (get arg :mimeType))
      (.setMimeType builder (get arg :mimeType)))
    (.build builder)))

(defn to-edn
  [^ReplicatedVoiceConfig arg]
  {:post [(global/strict! :gcp.vertexai.api/ReplicatedVoiceConfig %)]}
  (cond-> {}
    (some->> (.getMimeType arg)
             (not= ""))
      (assoc :mimeType (.getMimeType arg))))

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
    [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/ReplicatedVoiceConfig schema}
    {:gcp.global/name "gcp.vertexai.api.ReplicatedVoiceConfig"}))