;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.ImageConfig
  {:doc
     "<pre>\nConfig for image generation features.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.ImageConfig}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.ImageConfig"
   :gcp.dev/certification
     {:base-seed 1774824593363
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1774824593363 :standard 1774824593364 :stress 1774824593365}
      :protocol-hash
        "b44d2581e3979d0a946f2f9d3973f275b05bb339485f3d5eb436c47b54f7dc70"
      :timestamp "2026-03-29T22:49:54.284417687Z"}}
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
  (cond-> {} (.hasAspectRatio arg) (assoc :aspectRatio (.getAspectRatio arg))))

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