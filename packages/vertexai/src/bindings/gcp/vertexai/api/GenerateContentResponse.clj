;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.GenerateContentResponse
  {:doc
     "<pre>\nResponse message for [PredictionService.GenerateContent].\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.GenerateContentResponse}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.GenerateContentResponse"
   :gcp.dev/certification
     {:base-seed 1776627536391
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627536391 :standard 1776627536392 :stress 1776627536393}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:39:00.622445571Z"}}
  (:require [gcp.foreign.com.google.protobuf :as protobuf]
            [gcp.global :as global]
            [gcp.vertexai.api.Candidate :as Candidate]
            [gcp.vertexai.api.ModalityTokenCount :as ModalityTokenCount]
            [gcp.vertexai.api.SafetyRating :as SafetyRating])
  (:import [com.google.cloud.vertexai.api GenerateContentResponse
            GenerateContentResponse$Builder
            GenerateContentResponse$PromptFeedback
            GenerateContentResponse$PromptFeedback$BlockedReason
            GenerateContentResponse$PromptFeedback$Builder
            GenerateContentResponse$UsageMetadata
            GenerateContentResponse$UsageMetadata$Builder]
           [com.google.protobuf Timestamp]))

(declare from-edn
         to-edn
         PromptFeedback$BlockedReason-from-edn
         PromptFeedback$BlockedReason-to-edn
         PromptFeedback-from-edn
         PromptFeedback-to-edn
         PromptFeedback$BlockedReason-from-edn
         PromptFeedback$BlockedReason-to-edn
         UsageMetadata-from-edn
         UsageMetadata-to-edn)

(def PromptFeedback$BlockedReason-schema
  [:enum
   {:closed true,
    :doc
      "<pre>\nBlocked reason enumeration.\n</pre>\n\nProtobuf enum {@code\ngoogle.cloud.vertexai.v1.GenerateContentResponse.PromptFeedback.BlockedReason}",
    :gcp/category :nested/enum,
    :gcp/key
      :gcp.vertexai.api/GenerateContentResponse.PromptFeedback.BlockedReason}
   "BLOCKED_REASON_UNSPECIFIED" "SAFETY" "OTHER" "BLOCKLIST"
   "PROHIBITED_CONTENT" "MODEL_ARMOR" "JAILBREAK"])

(def PromptFeedback$BlockedReason-schema
  [:enum
   {:closed true,
    :doc
      "<pre>\nBlocked reason enumeration.\n</pre>\n\nProtobuf enum {@code\ngoogle.cloud.vertexai.v1.GenerateContentResponse.PromptFeedback.BlockedReason}",
    :gcp/category :nested/enum,
    :gcp/key
      :gcp.vertexai.api/GenerateContentResponse.PromptFeedback.BlockedReason}
   "BLOCKED_REASON_UNSPECIFIED" "SAFETY" "OTHER" "BLOCKLIST"
   "PROHIBITED_CONTENT" "MODEL_ARMOR" "JAILBREAK"])

(defn ^GenerateContentResponse$PromptFeedback PromptFeedback-from-edn
  [arg]
  (let [builder (GenerateContentResponse$PromptFeedback/newBuilder)]
    (when (some? (get arg :blockReason))
      (.setBlockReason
        builder
        (GenerateContentResponse$PromptFeedback$BlockedReason/valueOf
          (get arg :blockReason))))
    (when (some? (get arg :blockReasonMessage))
      (.setBlockReasonMessage builder (get arg :blockReasonMessage)))
    (when (seq (get arg :safetyRatings))
      (.addAllSafetyRatings builder
                            (mapv SafetyRating/from-edn
                              (get arg :safetyRatings))))
    (.build builder)))

(defn PromptFeedback-to-edn
  [^GenerateContentResponse$PromptFeedback arg]
  (when arg
    (cond-> {}
      (.getBlockReason arg) (assoc :blockReason (.name (.getBlockReason arg)))
      (some->> (.getBlockReasonMessage arg)
               (not= ""))
        (assoc :blockReasonMessage (.getBlockReasonMessage arg))
      (seq (.getSafetyRatingsList arg)) (assoc :safetyRatings
                                          (mapv SafetyRating/to-edn
                                            (.getSafetyRatingsList arg))))))

(def PromptFeedback-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nContent filter results for a prompt sent in the request.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.GenerateContentResponse.PromptFeedback}",
    :gcp/category :nested/protobuf-message,
    :gcp/key :gcp.vertexai.api/GenerateContentResponse.PromptFeedback}
   [:blockReason
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. Blocked reason.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.GenerateContentResponse.PromptFeedback.BlockedReason block_reason = 1 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>\n\n@return The blockReason."}
    [:enum {:closed true} "BLOCKED_REASON_UNSPECIFIED" "SAFETY" "OTHER"
     "BLOCKLIST" "PROHIBITED_CONTENT" "MODEL_ARMOR" "JAILBREAK"]]
   [:blockReasonMessage
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. A readable block reason message.\n</pre>\n\n<code>string block_reason_message = 3 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>\n\n@return The blockReasonMessage."}
    [:string {:min 1, :gen/max 1}]]
   [:safetyRatings
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. Safety ratings.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.SafetyRating safety_ratings = 2 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>"}
    [:sequential {:min 1, :gen/max 2} :gcp.vertexai.api/SafetyRating]]])

(defn ^GenerateContentResponse$UsageMetadata UsageMetadata-from-edn
  [arg]
  (let [builder (GenerateContentResponse$UsageMetadata/newBuilder)]
    (when (seq (get arg :cacheTokensDetails))
      (.addAllCacheTokensDetails builder
                                 (mapv ModalityTokenCount/from-edn
                                   (get arg :cacheTokensDetails))))
    (when (some? (get arg :cachedContentTokenCount))
      (.setCachedContentTokenCount builder
                                   (int (get arg :cachedContentTokenCount))))
    (when (some? (get arg :candidatesTokenCount))
      (.setCandidatesTokenCount builder (int (get arg :candidatesTokenCount))))
    (when (seq (get arg :candidatesTokensDetails))
      (.addAllCandidatesTokensDetails builder
                                      (mapv ModalityTokenCount/from-edn
                                        (get arg :candidatesTokensDetails))))
    (when (some? (get arg :promptTokenCount))
      (.setPromptTokenCount builder (int (get arg :promptTokenCount))))
    (when (seq (get arg :promptTokensDetails))
      (.addAllPromptTokensDetails builder
                                  (mapv ModalityTokenCount/from-edn
                                    (get arg :promptTokensDetails))))
    (when (some? (get arg :thoughtsTokenCount))
      (.setThoughtsTokenCount builder (int (get arg :thoughtsTokenCount))))
    (when (some? (get arg :totalTokenCount))
      (.setTotalTokenCount builder (int (get arg :totalTokenCount))))
    (.build builder)))

(defn UsageMetadata-to-edn
  [^GenerateContentResponse$UsageMetadata arg]
  (when arg
    (cond-> {}
      (seq (.getCacheTokensDetailsList arg))
        (assoc :cacheTokensDetails
          (mapv ModalityTokenCount/to-edn (.getCacheTokensDetailsList arg)))
      (seq (.getCandidatesTokensDetailsList arg))
        (assoc :candidatesTokensDetails
          (mapv ModalityTokenCount/to-edn
            (.getCandidatesTokensDetailsList arg)))
      (seq (.getPromptTokensDetailsList arg))
        (assoc :promptTokensDetails
          (mapv ModalityTokenCount/to-edn (.getPromptTokensDetailsList arg))))))

(def UsageMetadata-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nUsage metadata about response(s).\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.GenerateContentResponse.UsageMetadata}",
    :gcp/category :nested/protobuf-message,
    :gcp/key :gcp.vertexai.api/GenerateContentResponse.UsageMetadata}
   [:cacheTokensDetails
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. List of modalities of the cached content in the request\ninput.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.ModalityTokenCount cache_tokens_details = 10 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>"}
    [:sequential {:min 1, :gen/max 2} :gcp.vertexai.api/ModalityTokenCount]]
   [:candidatesTokensDetails
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. List of modalities that were returned in the response.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.ModalityTokenCount candidates_tokens_details = 11 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>"}
    [:sequential {:min 1, :gen/max 2} :gcp.vertexai.api/ModalityTokenCount]]
   [:promptTokensDetails
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. List of modalities that were processed in the request input.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.ModalityTokenCount prompt_tokens_details = 9 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>"}
    [:sequential {:min 1, :gen/max 2} :gcp.vertexai.api/ModalityTokenCount]]])

(defn ^GenerateContentResponse from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/GenerateContentResponse arg)
  (let [builder (GenerateContentResponse/newBuilder)]
    (when (seq (get arg :candidates))
      (.addAllCandidates builder
                         (mapv Candidate/from-edn (get arg :candidates))))
    (when (some? (get arg :createTime))
      (.setCreateTime builder
                      (protobuf/Timestamp-from-edn (get arg :createTime))))
    (when (some? (get arg :modelVersion))
      (.setModelVersion builder (get arg :modelVersion)))
    (when (some? (get arg :promptFeedback))
      (.setPromptFeedback builder
                          (PromptFeedback-from-edn (get arg :promptFeedback))))
    (when (some? (get arg :responseId))
      (.setResponseId builder (get arg :responseId)))
    (when (some? (get arg :usageMetadata))
      (.setUsageMetadata builder
                         (UsageMetadata-from-edn (get arg :usageMetadata))))
    (.build builder)))

(defn to-edn
  [^GenerateContentResponse arg]
  {:post [(global/strict! :gcp.vertexai.api/GenerateContentResponse %)]}
  (when arg
    (cond-> {}
      (seq (.getCandidatesList arg))
        (assoc :candidates (mapv Candidate/to-edn (.getCandidatesList arg)))
      (.hasCreateTime arg) (assoc :createTime
                             (protobuf/Timestamp-to-edn (.getCreateTime arg)))
      (some->> (.getModelVersion arg)
               (not= ""))
        (assoc :modelVersion (.getModelVersion arg))
      (.hasPromptFeedback arg)
        (assoc :promptFeedback (PromptFeedback-to-edn (.getPromptFeedback arg)))
      (some->> (.getResponseId arg)
               (not= ""))
        (assoc :responseId (.getResponseId arg))
      (.hasUsageMetadata arg)
        (assoc :usageMetadata (UsageMetadata-to-edn (.getUsageMetadata arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nResponse message for [PredictionService.GenerateContent].\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.GenerateContentResponse}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/GenerateContentResponse}
   [:candidates
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. Generated candidates.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.Candidate candidates = 2 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>"}
    [:sequential {:min 1, :gen/max 2} :gcp.vertexai.api/Candidate]]
   [:createTime
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. Timestamp when the request is made to the server.\n</pre>\n\n<code>.google.protobuf.Timestamp create_time = 12 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>\n\n@return The createTime."}
    :gcp.foreign.com.google.protobuf/Timestamp]
   [:modelVersion
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. The model version used to generate the response.\n</pre>\n\n<code>string model_version = 11 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>\n\n@return The modelVersion."}
    [:string {:min 1, :gen/max 1}]]
   [:promptFeedback
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. Content filter results for a prompt sent in the request.\nNote: Sent only in the first stream chunk.\nOnly happens when no candidates were generated due to content violations.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.GenerateContentResponse.PromptFeedback prompt_feedback = 3 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>\n\n@return The promptFeedback."}
    [:ref :gcp.vertexai.api/GenerateContentResponse.PromptFeedback]]
   [:responseId
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. response_id is used to identify each response. It is the\nencoding of the event_id.\n</pre>\n\n<code>string response_id = 13 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>\n\n@return The responseId."}
    [:string {:min 1, :gen/max 1}]]
   [:usageMetadata
    {:optional true,
     :getter-doc
       "<pre>\nUsage metadata about the response(s).\n</pre>\n\n<code>.google.cloud.vertexai.v1.GenerateContentResponse.UsageMetadata usage_metadata = 4;\n</code>\n\n@return The usageMetadata.",
     :setter-doc
       "<pre>\nUsage metadata about the response(s).\n</pre>\n\n<code>.google.cloud.vertexai.v1.GenerateContentResponse.UsageMetadata usage_metadata = 4;\n</code>"}
    [:ref :gcp.vertexai.api/GenerateContentResponse.UsageMetadata]]])

(global/include-schema-registry!
  (with-meta
    {:gcp.vertexai.api/GenerateContentResponse schema,
     :gcp.vertexai.api/GenerateContentResponse.PromptFeedback
       PromptFeedback-schema,
     :gcp.vertexai.api/GenerateContentResponse.PromptFeedback.BlockedReason
       PromptFeedback$BlockedReason-schema,
     :gcp.vertexai.api/GenerateContentResponse.UsageMetadata
       UsageMetadata-schema}
    {:gcp.global/name "gcp.vertexai.api.GenerateContentResponse"}))