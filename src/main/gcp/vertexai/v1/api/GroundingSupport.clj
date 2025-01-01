(ns gcp.vertexai.v1.api.GroundingSupport
  (:require [gcp.vertexai.v1.api.Segment :as Segment]
            [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api GroundingSupport]))

(def ^{:class GroundingSupport} schema
  [:map
   [:groundingChunkIndices [:sequential :int]]
   [:confidenceScores [:sequential [:or :int :double]]]
   [:segment Segment/schema]])

(defn ^GroundingSupport from-edn [arg]
  (global/strict! schema arg)
  (if (instance? GroundingSupport arg)
    (let [{:keys [segment confidenceScores groundingChunkIndices]} arg
          builder (GroundingSupport/newBuilder)]
      (.addAllGroundingChunkIndices builder groundingChunkIndices)
      (.addAllConfidenceScores builder confidenceScores)
      (.setSegment builder (Segment/from-edn segment))
      (.build builder))))

(defn to-edn [^GroundingSupport arg]
  {:post [(global/strict! schema %)]}
  (cond-> {}
          (pos? (.getConfidenceScoresCount arg))
          (assoc :confidenceScores (mapv double (.getConfidenceScoresList arg)))
          (pos? (.getGroundingChunkIndicesCount arg))
          (assoc :groundingChunkIndices (mapv int (.getGroundingChunkIndicesList arg)))
          (.hasSegment arg)
          (assoc :segment (Segment/to-edn (.getSegment arg)))))
