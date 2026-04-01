;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.VideoMetadata
  {:doc
     "<pre>\nMetadata describes the input video content.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.VideoMetadata}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.VideoMetadata"
   :gcp.dev/certification
     {:base-seed 1774824751069
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1774824751069 :standard 1774824751070 :stress 1774824751071}
      :protocol-hash
        "b44d2581e3979d0a946f2f9d3973f275b05bb339485f3d5eb436c47b54f7dc70"
      :timestamp "2026-03-29T22:52:32.029713860Z"}}
  (:require [gcp.foreign.com.google.protobuf :as protobuf]
            [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api VideoMetadata VideoMetadata$Builder]
           [com.google.protobuf Duration]))

(declare from-edn to-edn)

(defn ^VideoMetadata from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/VideoMetadata arg)
  (let [builder (VideoMetadata/newBuilder)]
    (when (some? (get arg :endOffset))
      (.setEndOffset builder (protobuf/Duration-from-edn (get arg :endOffset))))
    (when (some? (get arg :startOffset))
      (.setStartOffset builder
                       (protobuf/Duration-from-edn (get arg :startOffset))))
    (.build builder)))

(defn to-edn
  [^VideoMetadata arg]
  {:post [(global/strict! :gcp.vertexai.api/VideoMetadata %)]}
  (cond-> {}
    (.hasEndOffset arg) (assoc :endOffset
                          (protobuf/Duration-to-edn (.getEndOffset arg)))
    (.hasStartOffset arg) (assoc :startOffset
                            (protobuf/Duration-to-edn (.getStartOffset arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nMetadata describes the input video content.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.VideoMetadata}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/VideoMetadata}
   [:endOffset
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The end offset of the video.\n</pre>\n\n<code>.google.protobuf.Duration end_offset = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The endOffset.",
     :setter-doc
       "<pre>\nOptional. The end offset of the video.\n</pre>\n\n<code>.google.protobuf.Duration end_offset = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.foreign.com.google.protobuf/Duration]
   [:startOffset
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The start offset of the video.\n</pre>\n\n<code>.google.protobuf.Duration start_offset = 1 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The startOffset.",
     :setter-doc
       "<pre>\nOptional. The start offset of the video.\n</pre>\n\n<code>.google.protobuf.Duration start_offset = 1 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.foreign.com.google.protobuf/Duration]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/VideoMetadata schema}
    {:gcp.global/name "gcp.vertexai.api.VideoMetadata"}))