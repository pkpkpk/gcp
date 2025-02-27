(ns gcp.vertexai.v1.api.GroundingSupport
  (:require [gcp.vertexai.v1.api.Segment :as Segment]
            [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api GroundingSupport]))

(defn ^GroundingSupport from-edn [arg]
  (global/strict! :gcp/vertexai.api.GroundingSupport arg)
  (if (instance? GroundingSupport arg)
    (let [{:keys [segment confidenceScores groundingChunkIndices]} arg
          builder (GroundingSupport/newBuilder)]
      (.addAllGroundingChunkIndices builder groundingChunkIndices)
      (.addAllConfidenceScores builder confidenceScores)
      (.setSegment builder (Segment/from-edn segment))
      (.build builder))))

(defn to-edn [^GroundingSupport arg]
  {:post [(global/strict! :gcp/vertexai.api.GroundingSupport %)]}
  (cond-> {}
          (pos? (.getConfidenceScoresCount arg))
          (assoc :confidenceScores (mapv double (.getConfidenceScoresList arg)))
          (pos? (.getGroundingChunkIndicesCount arg))
          (assoc :groundingChunkIndices (mapv int (.getGroundingChunkIndicesList arg)))
          (.hasSegment arg)
          (assoc :segment (Segment/to-edn (.getSegment arg)))))
