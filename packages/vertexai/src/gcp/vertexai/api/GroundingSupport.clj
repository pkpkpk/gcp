;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.GroundingSupport
  {:doc
     "<pre>\nGrounding support.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.GroundingSupport}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.GroundingSupport"
   :gcp.dev/certification
     {:base-seed 1774824765469
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1774824765469 :standard 1774824765470 :stress 1774824765471}
      :protocol-hash
        "b44d2581e3979d0a946f2f9d3973f275b05bb339485f3d5eb436c47b54f7dc70"
      :timestamp "2026-03-29T22:52:46.630176170Z"}}
  (:require [gcp.global :as global]
            [gcp.vertexai.api.Segment :as Segment])
  (:import [com.google.cloud.vertexai.api GroundingSupport
            GroundingSupport$Builder]))

(declare from-edn to-edn)

(defn ^GroundingSupport from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/GroundingSupport arg)
  (let [builder (GroundingSupport/newBuilder)]
    (when (seq (get arg :confidenceScores))
      (.addAllConfidenceScores builder (seq (get arg :confidenceScores))))
    (when (seq (get arg :groundingChunkIndices))
      (.addAllGroundingChunkIndices builder
                                    (seq (get arg :groundingChunkIndices))))
    (when (some? (get arg :segment))
      (.setSegment builder (Segment/from-edn (get arg :segment))))
    (.build builder)))

(defn to-edn
  [^GroundingSupport arg]
  {:post [(global/strict! :gcp.vertexai.api/GroundingSupport %)]}
  (cond-> {}
    (seq (.getConfidenceScoresList arg)) (assoc :confidenceScores
                                           (seq (.getConfidenceScoresList arg)))
    (seq (.getGroundingChunkIndicesList arg))
      (assoc :groundingChunkIndices (seq (.getGroundingChunkIndicesList arg)))
    (.hasSegment arg) (assoc :segment (Segment/to-edn (.getSegment arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nGrounding support.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.GroundingSupport}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/GroundingSupport}
   [:confidenceScores
    {:optional true,
     :getter-doc
       "<pre>\nConfidence score of the support references. Ranges from 0 to 1. 1 is the\nmost confident. This list must have the same size as the\ngrounding_chunk_indices.\n</pre>\n\n<code>repeated float confidence_scores = 3;</code>\n\n@return A list containing the confidenceScores.",
     :setter-doc
       "<pre>\nConfidence score of the support references. Ranges from 0 to 1. 1 is the\nmost confident. This list must have the same size as the\ngrounding_chunk_indices.\n</pre>\n\n<code>repeated float confidence_scores = 3;</code>\n\n@param values The confidenceScores to add.\n@return This builder for chaining."}
    [:sequential {:min 1} :f32]]
   [:groundingChunkIndices
    {:optional true,
     :getter-doc
       "<pre>\nA list of indices (into 'grounding_chunk') specifying the\ncitations associated with the claim. For instance [1,3,4] means\nthat grounding_chunk[1], grounding_chunk[3],\ngrounding_chunk[4] are the retrieved content attributed to the claim.\n</pre>\n\n<code>repeated int32 grounding_chunk_indices = 2;</code>\n\n@return A list containing the groundingChunkIndices.",
     :setter-doc
       "<pre>\nA list of indices (into 'grounding_chunk') specifying the\ncitations associated with the claim. For instance [1,3,4] means\nthat grounding_chunk[1], grounding_chunk[3],\ngrounding_chunk[4] are the retrieved content attributed to the claim.\n</pre>\n\n<code>repeated int32 grounding_chunk_indices = 2;</code>\n\n@param values The groundingChunkIndices to add.\n@return This builder for chaining."}
    [:sequential {:min 1} :i32]]
   [:segment
    {:optional true,
     :getter-doc
       "<pre>\nSegment of the content this support belongs to.\n</pre>\n\n<code>optional .google.cloud.vertexai.v1.Segment segment = 1;</code>\n\n@return The segment.",
     :setter-doc
       "<pre>\nSegment of the content this support belongs to.\n</pre>\n\n<code>optional .google.cloud.vertexai.v1.Segment segment = 1;</code>"}
    :gcp.vertexai.api/Segment]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/GroundingSupport schema}
    {:gcp.global/name "gcp.vertexai.api.GroundingSupport"}))