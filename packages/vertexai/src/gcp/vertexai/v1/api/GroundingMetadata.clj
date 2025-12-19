(ns gcp.vertexai.v1.api.GroundingMetadata
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf]
            [gcp.vertexai.v1.api.GroundingChunk :as gc]
            [gcp.vertexai.v1.api.GroundingSupport :as gs]
            [gcp.vertexai.v1.api.RetrievalMetadata :as RetrievalMetadata]
            [gcp.vertexai.v1.api.SearchEntryPoint :as sep])
  (:import [com.google.cloud.vertexai.api GroundingMetadata]))

(defn ^GroundingMetadata from-edn
  [{:keys [groundingChunks
           groundingSupports
           searchEntryPoint
           webSearchQueries
           retrievalMetadata] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/GroundingMetadata arg)
  (let [builder (GroundingMetadata/newBuilder)]
    (some->> groundingChunks (map gc/from-edn) (.addAllGroundingChunks builder))
    (some->> groundingSupports (map gs/from-edn) (.addAllGroundingSupports builder))
    (some->> searchEntryPoint sep/from-edn (.setSearchEntryPoint builder))
    (some->> webSearchQueries (.addAllWebSearchQueries builder))
    (some->> retrievalMetadata RetrievalMetadata/from-edn (.setRetrievalMetadata builder))
    (.build builder)))

(defn to-edn [^GroundingMetadata arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/GroundingMetadata %)]}
  (cond-> {}
          (.hasSearchEntryPoint arg)
          (assoc :searchEntryPoint (sep/to-edn (.getSearchEntryPoint arg)))

          (.hasRetrievalMetadata arg)
          (assoc :retrievalMetadata (RetrievalMetadata/to-edn (.getRetrievalMetadata arg)))

          (pos? (.getWebSearchQueriesCount arg))
          (assoc :webSearchQueries (protobuf/protocolstringlist-to-edn (.getWebSearchQueriesList arg)))

          (pos? (.getGroundingSupportsCount arg))
          (assoc :groundingSupports (mapv gs/to-edn (.getGroundingSupportsList arg)))

          (pos? (.getGroundingChunksCount arg))
          (assoc :groundingChunks (mapv gc/to-edn (.getGroundingChunksList arg)))))

(def schema
  [:map
   {:ns               'gcp.vertexai.v1.api.GroundingMetadata
    :from-edn         'gcp.vertexai.v1.api.GroundingMetadata/from-edn
    :to-edn           'gcp.vertexai.v1.api.GroundingMetadata/to-edn
    :doc              "Metadata returned to client when grounding is enabled."
    :generativeai/url "https://ai.google.dev/api/generate-content#GroundingMetadata"
    :protobuf/type    "google.cloud.vertexai.v1.GroundingMetadata"
    :class            'com.google.cloud.vertexai.api.GroundingMetadata
    :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.GroundingMetadata"}
   [:groundingChunks
    {:optional true
     :doc      "List of supporting references retrieved from specified grounding source"}
    [:sequential :gcp.vertexai.v1.api/GroundingChunk]]
   [:groundingSupports {:optional true} [:sequential :gcp.vertexai.v1.api/GroundingSupport]]
   [:webSearchQueries {:optional true} [:sequential :string]]
   [:searchEntryPoint
    {:optional true
     :doc      "Optional. Google search entry for the following-up web searches."}
    :gcp.vertexai.v1.api/SearchEntryPoint]
   [:retrievalMetadata
    {:gemini-only? true
     :optional     true
     :doc          "Metadata related to retrieval in the grounding flow."}
    :gcp.vertexai.v1.api/RetrievalMetadata]])

(global/register-schema! :gcp.vertexai.v1.api/GroundingMetadata schema)
