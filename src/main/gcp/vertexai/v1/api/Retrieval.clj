(ns gcp.vertexai.v1.api.Retrieval
  (:require [gcp.vertexai.v1.api.VertexAISearch :as VertexAISearch]
            [gcp.global :as global])
  (:import (com.google.cloud.vertexai.api Retrieval)))

(def ^{:class Retrieval} schema
  [:map {:closed true}
   [:vertexAiSearch {:optional? false} VertexAISearch/schema]])

(defn ^Retrieval from-edn
  [{:keys [vertexAISearch] :as arg}]
  (global/strict! schema arg)
  (let [builder (Retrieval/newBuilder)]
    (.setVertexAiSearch builder (VertexAISearch/from-edn vertexAISearch))
    (.build builder)))

(defn to-edn [^Retrieval arg]
  {:post [(global/strict! schema arg)]}
  {:vertexAiSearch (VertexAISearch/to-edn (.getVertexAiSearch arg))})
