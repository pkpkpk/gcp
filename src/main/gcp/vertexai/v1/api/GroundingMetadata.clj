(ns gcp.vertexai.v1.api.GroundingMetadata
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf]
            [gcp.vertexai.v1.api.GroundingChunk :as gc]
            [gcp.vertexai.v1.api.GroundingSupport :as gs]
            [gcp.vertexai.v1.api.SearchEntryPoint :as sep])
  (:import [com.google.cloud.vertexai.api GroundingMetadata]))

(def ^{:class GroundingMetadata} schema
  [:map
   [:groundingChunks
    {:optional true
     :doc "List of supporting references retrieved from specified grounding source"}
    [:sequential gc/schema]]
   [:groundingSupports
    {:optional true}
    [:sequential gs/schema]]
   [:webSearchQueries
    {:optional true}
    [:sequential :string]]
   [:searchEntryPoint
    {:optional true
     :doc      "Optional. Google search entry for the following-up web searches."}
    sep/schema]
   #_[:retrievalMetadata
    {:gemini-only? true
     :optional true
     :doc "Metadata related to retrieval in the grounding flow."}
    :any]])

(defn ^GroundingMetadata from-edn
  [{:keys [groundingChunks
           groundingSupports
           searchEntryPoint
           webSearchQueries] :as arg}]
  (global/strict! schema arg)
  (let [builder (GroundingMetadata/newBuilder)]
    (some->> groundingChunks (map gc/from-edn) (.addAllGroundingChunks builder))
    (some->> groundingSupports (map gs/from-edn) (.addAllGroundingSupports builder))
    (some->> searchEntryPoint sep/from-edn (.setSearchEntryPoint builder))
    (some->> webSearchQueries (.addAllWebSearchQueries))
    (.build builder)))

(defn to-edn [^GroundingMetadata arg]
  {:post [(global/strict! schema %)]}
  (cond-> {}

          (.hasSearchEntryPoint arg)
          (assoc :searchEntryPoint (sep/to-edn (.getSearchEntryPoint arg)))

          (pos? (.getWebSearchQueriesCount arg))
          (assoc :webSearchQueries (protobuf/protocolstringlist-to-edn (.getWebSearchQueriesList arg)))

          (pos? (.getGroundingSupportsCount arg))
          (assoc :groundingSupports (mapv gs/to-edn (.getGroundingSupportsList arg)))

          (pos? (.getGroundingChunksCount arg))
          (assoc :groundingChunks (mapv gc/to-edn (.getGroundingChunksList arg)))))
