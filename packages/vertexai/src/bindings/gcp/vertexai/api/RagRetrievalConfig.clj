;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.RagRetrievalConfig
  {:doc
     "<pre>\nSpecifies the context retrieval config.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.RagRetrievalConfig}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.RagRetrievalConfig"
   :gcp.dev/certification
     {:base-seed 1776627415778
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627415778 :standard 1776627415779 :stress 1776627415780}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:36:56.769632959Z"}}
  (:require [gcp.global :as global])
  (:import
    [com.google.cloud.vertexai.api RagRetrievalConfig RagRetrievalConfig$Builder
     RagRetrievalConfig$Filter RagRetrievalConfig$Filter$Builder
     RagRetrievalConfig$Filter$VectorDbThresholdCase RagRetrievalConfig$Ranking
     RagRetrievalConfig$Ranking$Builder RagRetrievalConfig$Ranking$LlmRanker
     RagRetrievalConfig$Ranking$LlmRanker$Builder
     RagRetrievalConfig$Ranking$RankService
     RagRetrievalConfig$Ranking$RankService$Builder
     RagRetrievalConfig$Ranking$RankingConfigCase]))

(declare from-edn
         to-edn
         Filter$VectorDbThresholdCase-from-edn
         Filter$VectorDbThresholdCase-to-edn
         Filter-from-edn
         Filter-to-edn
         Filter$VectorDbThresholdCase-from-edn
         Filter$VectorDbThresholdCase-to-edn
         Ranking$RankService-from-edn
         Ranking$RankService-to-edn
         Ranking$LlmRanker-from-edn
         Ranking$LlmRanker-to-edn
         Ranking$RankingConfigCase-from-edn
         Ranking$RankingConfigCase-to-edn
         Ranking-from-edn
         Ranking-to-edn
         Ranking$RankService-from-edn
         Ranking$RankService-to-edn
         Ranking$LlmRanker-from-edn
         Ranking$LlmRanker-to-edn
         Ranking$RankingConfigCase-from-edn
         Ranking$RankingConfigCase-to-edn)

(def Filter$VectorDbThresholdCase-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.vertexai.api/RagRetrievalConfig.Filter.VectorDbThresholdCase}
   "VECTOR_DISTANCE_THRESHOLD" "VECTOR_SIMILARITY_THRESHOLD"
   "VECTORDBTHRESHOLD_NOT_SET"])

(def Filter$VectorDbThresholdCase-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.vertexai.api/RagRetrievalConfig.Filter.VectorDbThresholdCase}
   "VECTOR_DISTANCE_THRESHOLD" "VECTOR_SIMILARITY_THRESHOLD"
   "VECTORDBTHRESHOLD_NOT_SET"])

(defn ^RagRetrievalConfig$Filter Filter-from-edn
  [arg]
  (let [builder (RagRetrievalConfig$Filter/newBuilder)]
    (when (some? (get arg :metadataFilter))
      (.setMetadataFilter builder (get arg :metadataFilter)))
    (cond (contains? arg :vectorDistanceThreshold)
            (.setVectorDistanceThreshold builder
                                         (double
                                           (get arg :vectorDistanceThreshold)))
          (contains? arg :vectorSimilarityThreshold)
            (.setVectorSimilarityThreshold
              builder
              (double (get arg :vectorSimilarityThreshold))))
    (.build builder)))

(defn Filter-to-edn
  [^RagRetrievalConfig$Filter arg]
  (when arg
    (let [res (cond-> {}
                (some->> (.getMetadataFilter arg)
                         (not= ""))
                  (assoc :metadataFilter (.getMetadataFilter arg)))
          res (case (.name (.getVectorDbThresholdCase arg))
                "VECTOR_DISTANCE_THRESHOLD"
                  (assoc res
                    :vectorDistanceThreshold (.getVectorDistanceThreshold arg))
                "VECTOR_SIMILARITY_THRESHOLD" (assoc res
                                                :vectorSimilarityThreshold
                                                  (.getVectorSimilarityThreshold
                                                    arg))
                res)]
      res)))

(def Filter-schema
  [:and
   {:closed true,
    :doc
      "<pre>\nConfig for filters.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.RagRetrievalConfig.Filter}",
    :gcp/category :nested/union-protobuf-oneof,
    :gcp/key :gcp.vertexai.api/RagRetrievalConfig.Filter}
   [:map {:closed true}
    [:metadataFilter
     {:optional true,
      :getter-doc
        "<pre>\nOptional. String for metadata filtering.\n</pre>\n\n<code>string metadata_filter = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The metadataFilter.",
      :setter-doc
        "<pre>\nOptional. String for metadata filtering.\n</pre>\n\n<code>string metadata_filter = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The metadataFilter to set.\n@return This builder for chaining."}
     [:string {:min 1, :gen/max 1}]]
    [:vectorDistanceThreshold
     {:optional true,
      :getter-doc
        "<pre>\nOptional. Only returns contexts with vector distance smaller than the\nthreshold.\n</pre>\n\n<code>double vector_distance_threshold = 3 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The vectorDistanceThreshold.",
      :setter-doc
        "<pre>\nOptional. Only returns contexts with vector distance smaller than the\nthreshold.\n</pre>\n\n<code>double vector_distance_threshold = 3 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@param value The vectorDistanceThreshold to set.\n@return This builder for chaining."}
     :f64]
    [:vectorSimilarityThreshold
     {:optional true,
      :getter-doc
        "<pre>\nOptional. Only returns contexts with vector similarity larger than the\nthreshold.\n</pre>\n\n<code>double vector_similarity_threshold = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The vectorSimilarityThreshold.",
      :setter-doc
        "<pre>\nOptional. Only returns contexts with vector similarity larger than the\nthreshold.\n</pre>\n\n<code>double vector_similarity_threshold = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@param value The vectorSimilarityThreshold to set.\n@return This builder for chaining."}
     :f64]]
   [:fn
    {:error/message
       "Only one of these keys may be present: #{:vectorDistanceThreshold :vectorSimilarityThreshold}"}
    (quote (fn [m]
             (<= (count (filter (set (keys m))
                          #{:vectorDistanceThreshold
                            :vectorSimilarityThreshold}))
                 1)))]])

(defn ^RagRetrievalConfig$Ranking$RankService Ranking$RankService-from-edn
  [arg]
  (let [builder (RagRetrievalConfig$Ranking$RankService/newBuilder)]
    (when (some? (get arg :modelName))
      (.setModelName builder (get arg :modelName)))
    (.build builder)))

(defn Ranking$RankService-to-edn
  [^RagRetrievalConfig$Ranking$RankService arg]
  (when arg
    (cond-> {} (.hasModelName arg) (assoc :modelName (.getModelName arg)))))

(def Ranking$RankService-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nConfig for Rank Service.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.RagRetrievalConfig.Ranking.RankService}",
    :gcp/category :nested/protobuf-message,
    :gcp/key :gcp.vertexai.api/RagRetrievalConfig.Ranking.RankService}
   [:modelName
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The model name of the rank service.\nFormat: `semantic-ranker-512&#64;latest`\n</pre>\n\n<code>optional string model_name = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The modelName.",
     :setter-doc
       "<pre>\nOptional. The model name of the rank service.\nFormat: `semantic-ranker-512&#64;latest`\n</pre>\n\n<code>optional string model_name = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The modelName to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]])

(defn ^RagRetrievalConfig$Ranking$LlmRanker Ranking$LlmRanker-from-edn
  [arg]
  (let [builder (RagRetrievalConfig$Ranking$LlmRanker/newBuilder)]
    (when (some? (get arg :modelName))
      (.setModelName builder (get arg :modelName)))
    (.build builder)))

(defn Ranking$LlmRanker-to-edn
  [^RagRetrievalConfig$Ranking$LlmRanker arg]
  (when arg
    (cond-> {} (.hasModelName arg) (assoc :modelName (.getModelName arg)))))

(def Ranking$LlmRanker-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nConfig for LlmRanker.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.RagRetrievalConfig.Ranking.LlmRanker}",
    :gcp/category :nested/protobuf-message,
    :gcp/key :gcp.vertexai.api/RagRetrievalConfig.Ranking.LlmRanker}
   [:modelName
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The model name used for ranking.\nFormat: `gemini-1.5-pro`\n</pre>\n\n<code>optional string model_name = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The modelName.",
     :setter-doc
       "<pre>\nOptional. The model name used for ranking.\nFormat: `gemini-1.5-pro`\n</pre>\n\n<code>optional string model_name = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The modelName to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]])

(def Ranking$RankingConfigCase-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.vertexai.api/RagRetrievalConfig.Ranking.RankingConfigCase}
   "RANK_SERVICE" "LLM_RANKER" "RANKINGCONFIG_NOT_SET"])

(defn ^RagRetrievalConfig$Ranking$RankService Ranking$RankService-from-edn
  [arg]
  (let [builder (RagRetrievalConfig$Ranking$RankService/newBuilder)]
    (when (some? (get arg :modelName))
      (.setModelName builder (get arg :modelName)))
    (.build builder)))

(defn Ranking$RankService-to-edn
  [^RagRetrievalConfig$Ranking$RankService arg]
  (when arg
    (cond-> {} (.hasModelName arg) (assoc :modelName (.getModelName arg)))))

(def Ranking$RankService-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nConfig for Rank Service.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.RagRetrievalConfig.Ranking.RankService}",
    :gcp/category :nested/protobuf-message,
    :gcp/key :gcp.vertexai.api/RagRetrievalConfig.Ranking.RankService}
   [:modelName
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The model name of the rank service.\nFormat: `semantic-ranker-512&#64;latest`\n</pre>\n\n<code>optional string model_name = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The modelName.",
     :setter-doc
       "<pre>\nOptional. The model name of the rank service.\nFormat: `semantic-ranker-512&#64;latest`\n</pre>\n\n<code>optional string model_name = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The modelName to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]])

(defn ^RagRetrievalConfig$Ranking$LlmRanker Ranking$LlmRanker-from-edn
  [arg]
  (let [builder (RagRetrievalConfig$Ranking$LlmRanker/newBuilder)]
    (when (some? (get arg :modelName))
      (.setModelName builder (get arg :modelName)))
    (.build builder)))

(defn Ranking$LlmRanker-to-edn
  [^RagRetrievalConfig$Ranking$LlmRanker arg]
  (when arg
    (cond-> {} (.hasModelName arg) (assoc :modelName (.getModelName arg)))))

(def Ranking$LlmRanker-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nConfig for LlmRanker.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.RagRetrievalConfig.Ranking.LlmRanker}",
    :gcp/category :nested/protobuf-message,
    :gcp/key :gcp.vertexai.api/RagRetrievalConfig.Ranking.LlmRanker}
   [:modelName
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The model name used for ranking.\nFormat: `gemini-1.5-pro`\n</pre>\n\n<code>optional string model_name = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The modelName.",
     :setter-doc
       "<pre>\nOptional. The model name used for ranking.\nFormat: `gemini-1.5-pro`\n</pre>\n\n<code>optional string model_name = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The modelName to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]])

(def Ranking$RankingConfigCase-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.vertexai.api/RagRetrievalConfig.Ranking.RankingConfigCase}
   "RANK_SERVICE" "LLM_RANKER" "RANKINGCONFIG_NOT_SET"])

(defn ^RagRetrievalConfig$Ranking Ranking-from-edn
  [arg]
  (let [builder (RagRetrievalConfig$Ranking/newBuilder)]
    (cond (contains? arg :llmRanker) (.setLlmRanker builder
                                                    (Ranking$LlmRanker-from-edn
                                                      (get arg :llmRanker)))
          (contains? arg :rankService) (.setRankService
                                         builder
                                         (Ranking$RankService-from-edn
                                           (get arg :rankService))))
    (.build builder)))

(defn Ranking-to-edn
  [^RagRetrievalConfig$Ranking arg]
  (when arg
    (let [res (cond-> {})
          res (case (.name (.getRankingConfigCase arg))
                "LLM_RANKER" (assoc res
                               :llmRanker (Ranking$LlmRanker-to-edn
                                            (.getLlmRanker arg)))
                "RANK_SERVICE" (assoc res
                                 :rankService (Ranking$RankService-to-edn
                                                (.getRankService arg)))
                res)]
      res)))

(def Ranking-schema
  [:and
   {:closed true,
    :doc
      "<pre>\nConfig for ranking and reranking.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.RagRetrievalConfig.Ranking}",
    :gcp/category :nested/union-protobuf-oneof,
    :gcp/key :gcp.vertexai.api/RagRetrievalConfig.Ranking}
   [:map {:closed true}
    [:llmRanker
     {:optional true,
      :getter-doc
        "<pre>\nOptional. Config for LlmRanker.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.RagRetrievalConfig.Ranking.LlmRanker llm_ranker = 3 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The llmRanker.",
      :setter-doc
        "<pre>\nOptional. Config for LlmRanker.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.RagRetrievalConfig.Ranking.LlmRanker llm_ranker = 3 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
     [:ref :gcp.vertexai.api/RagRetrievalConfig.Ranking.LlmRanker]]
    [:rankService
     {:optional true,
      :getter-doc
        "<pre>\nOptional. Config for Rank Service.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.RagRetrievalConfig.Ranking.RankService rank_service = 1 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The rankService.",
      :setter-doc
        "<pre>\nOptional. Config for Rank Service.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.RagRetrievalConfig.Ranking.RankService rank_service = 1 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
     [:ref :gcp.vertexai.api/RagRetrievalConfig.Ranking.RankService]]]
   [:fn
    {:error/message
       "Only one of these keys may be present: #{:rankService :llmRanker}"}
    (quote (fn [m]
             (<= (count (filter (set (keys m)) #{:rankService :llmRanker}))
                 1)))]])

(defn ^RagRetrievalConfig from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/RagRetrievalConfig arg)
  (let [builder (RagRetrievalConfig/newBuilder)]
    (when (some? (get arg :filter))
      (.setFilter builder (Filter-from-edn (get arg :filter))))
    (when (some? (get arg :ranking))
      (.setRanking builder (Ranking-from-edn (get arg :ranking))))
    (when (some? (get arg :topK)) (.setTopK builder (int (get arg :topK))))
    (.build builder)))

(defn to-edn
  [^RagRetrievalConfig arg]
  {:post [(global/strict! :gcp.vertexai.api/RagRetrievalConfig %)]}
  (when arg
    (cond-> {}
      (.hasFilter arg) (assoc :filter (Filter-to-edn (.getFilter arg)))
      (.hasRanking arg) (assoc :ranking (Ranking-to-edn (.getRanking arg)))
      (.getTopK arg) (assoc :topK (.getTopK arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nSpecifies the context retrieval config.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.RagRetrievalConfig}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/RagRetrievalConfig}
   [:filter
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Config for filters.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.RagRetrievalConfig.Filter filter = 3 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The filter.",
     :setter-doc
       "<pre>\nOptional. Config for filters.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.RagRetrievalConfig.Filter filter = 3 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    [:ref :gcp.vertexai.api/RagRetrievalConfig.Filter]]
   [:ranking
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Config for ranking and reranking.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.RagRetrievalConfig.Ranking ranking = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The ranking.",
     :setter-doc
       "<pre>\nOptional. Config for ranking and reranking.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.RagRetrievalConfig.Ranking ranking = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    [:ref :gcp.vertexai.api/RagRetrievalConfig.Ranking]]
   [:topK
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The number of contexts to retrieve.\n</pre>\n\n<code>int32 top_k = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The topK.",
     :setter-doc
       "<pre>\nOptional. The number of contexts to retrieve.\n</pre>\n\n<code>int32 top_k = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The topK to set.\n@return This builder for chaining."}
    :i32]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/RagRetrievalConfig schema,
              :gcp.vertexai.api/RagRetrievalConfig.Filter Filter-schema,
              :gcp.vertexai.api/RagRetrievalConfig.Filter.VectorDbThresholdCase
                Filter$VectorDbThresholdCase-schema,
              :gcp.vertexai.api/RagRetrievalConfig.Ranking Ranking-schema,
              :gcp.vertexai.api/RagRetrievalConfig.Ranking.LlmRanker
                Ranking$LlmRanker-schema,
              :gcp.vertexai.api/RagRetrievalConfig.Ranking.RankService
                Ranking$RankService-schema,
              :gcp.vertexai.api/RagRetrievalConfig.Ranking.RankingConfigCase
                Ranking$RankingConfigCase-schema}
    {:gcp.global/name "gcp.vertexai.api.RagRetrievalConfig"}))