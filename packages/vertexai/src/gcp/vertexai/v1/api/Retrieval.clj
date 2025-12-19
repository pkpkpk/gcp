(ns gcp.vertexai.v1.api.Retrieval
  (:require [gcp.vertexai.v1.api.VertexAISearch :as VertexAISearch]
            [gcp.vertexai.v1.api.VertexRagStore :as VertexRagStore]
            [gcp.global :as global])
  (:import (com.google.cloud.vertexai.api Retrieval)))

(defn ^Retrieval from-edn [{:keys [vertexAISearch vertexRagStore] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/Retrieval arg)
  (let [builder (Retrieval/newBuilder)]
    (some->> vertexAISearch VertexAISearch/from-edn (.setVertexAiSearch builder))
    (some->> vertexRagStore VertexRagStore/from-edn (.setVertexRagStore builder))
    (.build builder)))

(defn to-edn [^Retrieval arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/Retrieval %)]}
  (cond-> {}
          (.hasVertexAiSearch arg)
          (assoc :vertexAISearch (VertexAISearch/to-edn (.getVertexAiSearch arg)))
          (.hasVertexRagStore arg)
          (assoc :vertexRagStore (VertexRagStore/to-edn (.getVertexRagStore arg)))))

(def schema
  [:map
   {:closed   true
    :class    'com.google.cloud.vertexai.api.Retrieval
    :from-edn 'gcp.vertexai.v1.api.Retrieval/from-edn
    :to-edn   'gcp.vertexai.v1.api.Retrieval/to-edn
    :protobuf/type    "google.cloud.vertexai.v1.Retrieval"}
   [:vertexAiSearch {:optional false} :gcp.vertexai.v1.api/VertexAISearch]])

(global/register-schema! :gcp.vertexai.v1.api/Retrieval schema)
