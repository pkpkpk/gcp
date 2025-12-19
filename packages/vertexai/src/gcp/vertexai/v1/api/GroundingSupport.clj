(ns gcp.vertexai.v1.api.GroundingSupport
  (:require [gcp.vertexai.v1.api.Segment :as Segment]
            [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api GroundingSupport]))

(defn ^GroundingSupport from-edn [arg]
  (global/strict! :gcp.vertexai.v1.api/GroundingSupport arg)
  (if (instance? GroundingSupport arg)
    (let [{:keys [segment confidenceScores groundingChunkIndices]} arg
          builder (GroundingSupport/newBuilder)]
      (.addAllGroundingChunkIndices builder groundingChunkIndices)
      (.addAllConfidenceScores builder confidenceScores)
      (.setSegment builder (Segment/from-edn segment))
      (.build builder))))

(defn to-edn [^GroundingSupport arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/GroundingSupport %)]}
  (cond-> {}
          (pos? (.getConfidenceScoresCount arg))
          (assoc :confidenceScores (mapv double (.getConfidenceScoresList arg)))
          (pos? (.getGroundingChunkIndicesCount arg))
          (assoc :groundingChunkIndices (mapv int (.getGroundingChunkIndicesList arg)))
          (.hasSegment arg)
          (assoc :segment (Segment/to-edn (.getSegment arg)))))

(def schema
  [:map
   {:ns               'gcp.vertexai.v1.api.GroundingSupport
    :from-edn         'gcp.vertexai.v1.api.GroundingSupport/from-edn
    :to-edn           'gcp.vertexai.v1.api.GroundingSupport/to-edn
    :generativeai/url "https://ai.google.dev/api/generate-content#GroundingSupport"
    :doc              "Grounding support."
    :protobuf/type    "google.cloud.vertexai.v1.GroundingSupport"
    :class            'com.google.cloud.vertexai.api.GroundingSupport
    :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.GroundingSupport"}
   [:groundingChunkIndices {:optional true} [:sequential :int]]
   [:confidenceScores {:optional true} [:sequential [:or :int :double]]]
   [:segment {:optional true} :gcp.vertexai.v1.api/Segment]])

(global/register-schema! :gcp.vertexai.v1.api/GroundingSupport schema)
