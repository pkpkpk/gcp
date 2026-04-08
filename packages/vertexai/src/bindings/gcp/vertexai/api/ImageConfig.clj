;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.ImageConfig
  {:doc
     "<pre>\nConfig for image generation features.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.ImageConfig}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.ImageConfig"
   :gcp.dev/certification
     {:base-seed 1775465485870
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1775465485870 :standard 1775465485871 :stress 1775465485872}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-06T08:51:26.765982243Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api ImageConfig ImageConfig$Builder]))

(declare from-edn to-edn)

(defn ^ImageConfig from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/ImageConfig arg)
  (let [builder (ImageConfig/newBuilder)]
    (when (some? (get arg :aspectRatio))
      (.setAspectRatio builder (get arg :aspectRatio)))
    (.build builder)))

(defn to-edn
  [^ImageConfig arg]
  {:post [(global/strict! :gcp.vertexai.api/ImageConfig %)]}
  (when arg
    (cond-> {}
      (.hasAspectRatio arg) (assoc :aspectRatio (.getAspectRatio arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nConfig for image generation features.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.ImageConfig}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/ImageConfig}
   [:aspectRatio
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The desired aspect ratio for the generated images. The following\naspect ratios are supported:\n\n\"1:1\"\n\"2:3\", \"3:2\"\n\"3:4\", \"4:3\"\n\"4:5\", \"5:4\"\n\"9:16\", \"16:9\"\n\"21:9\"\n</pre>\n\n<code>optional string aspect_ratio = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The aspectRatio.",
     :setter-doc
       "<pre>\nOptional. The desired aspect ratio for the generated images. The following\naspect ratios are supported:\n\n\"1:1\"\n\"2:3\", \"3:2\"\n\"3:4\", \"4:3\"\n\"4:5\", \"5:4\"\n\"9:16\", \"16:9\"\n\"21:9\"\n</pre>\n\n<code>optional string aspect_ratio = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The aspectRatio to set.\n@return This builder for chaining."}
    [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/ImageConfig schema}
    {:gcp.global/name "gcp.vertexai.api.ImageConfig"}))