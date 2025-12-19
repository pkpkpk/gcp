(ns gcp.vertexai.v1.api.RagRetrievalConfig
  (:require [gcp.global :as global])
  (:import (com.google.cloud.vertexai.api RagRetrievalConfig
                                          RagRetrievalConfig$Filter
                                          RagRetrievalConfig$Ranking
                                          RagRetrievalConfig$Ranking$RankService)))

(defn ^RagRetrievalConfig$Filter Filter-from-edn [arg]
  (let [builder (RagRetrievalConfig$Filter/newBuilder)]
    (some->> (:metadataFilter arg) (.setMetadataFilter builder))
    (if (contains? arg :vectorDistanceThreshold)
      (.setVectorDistanceThreshold builder (:vectorDistanceThreshold arg))
      (when (contains? arg :vectorSimilarityThreshold)
        (.setVectorSimilarityThreshold builder (:vectorSimilarityThreshold arg))))
    (.build builder)))

(defn Filter-to-edn [^RagRetrievalConfig$Filter arg]
  (cond-> {}
          (.hasVectorDistanceThreshold arg)
          (assoc :vectorDistanceThreshold (.getVectorDistanceThreshold arg))
          (.hasVectorSimilarityThreshold arg)
          (assoc :vectorSimilarityThreshold (.getVectorSimilarityThreshold arg))
          (not (empty? (.getMetadataFilter arg)))
          (assoc :metadataFilter (.getMetadataFilter arg))))

(defn ^RagRetrievalConfig$Ranking$RankService RankService-from-edn [arg]
  (let [builder (RagRetrievalConfig$Ranking$RankService/newBuilder)]
    (some->> (:modelName arg) (.setModelName builder))
    (.build builder)))

(defn RankService-to-edn [^RagRetrievalConfig$Ranking$RankService arg]
  (cond-> {}
          (.hasModelName arg)
          (assoc :modelName (.getModelName arg))))

(defn ^RagRetrievalConfig$Ranking Ranking-from-edn [arg]
  (let [builder (RagRetrievalConfig$Ranking/newBuilder)]
    (some->> (:rankService arg) RankService-from-edn (.setRankService builder))
    (.build builder)))

(defn Ranking-to-edn [^RagRetrievalConfig$Ranking arg]
  (cond-> {}
          (.hasRankService arg)
          (assoc :rankService (RankService-to-edn (.getRankService arg)))))

(defn ^RagRetrievalConfig from-edn
  [{:keys [topK filter ranking] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/RagRetrievalConfig arg)
  (let [builder (RagRetrievalConfig/newBuilder)]
    (some->> topK (.setTopK builder))
    (some->> filter Filter-from-edn (.setFilter builder))
    (some->> ranking Ranking-from-edn (.setRanking builder))
    (.build builder)))

(defn to-edn [^RagRetrievalConfig arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/RagRetrievalConfig %)]}
  (cond-> {}
          (> (.getTopK arg) 0)
          (assoc :topK (.getTopK arg))
          (.hasFilter arg)
          (assoc :filter (Filter-to-edn (.getFilter arg)))
          (.hasRanking arg)
          (assoc :ranking (Ranking-to-edn (.getRanking arg)))))

(def schema
  [:map
   {:doc              "Configuration for RAG retrieval."
    :class            'com.google.cloud.vertexai.api.RagRetrievalConfig
    :protobuf/type    "google.cloud.vertexai.v1.RagRetrievalConfig"}
   [:topK {:optional true} :int]
   [:filter {:optional true} [:map
                               [:metadataFilter {:optional true} :string]
                               [:vectorDistanceThreshold {:optional true} :float]
                               [:vectorSimilarityThreshold {:optional true} :float]]]
   [:ranking {:optional true} [:map
                               [:rankService {:optional true} [:map
                                                               [:modelName {:optional true} :string]]]]]])

(global/register-schema! :gcp.vertexai.v1.api/RagRetrievalConfig schema)
