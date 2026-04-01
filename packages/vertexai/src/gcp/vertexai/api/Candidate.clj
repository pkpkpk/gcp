;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.Candidate
  {:doc
     "<pre>\nA response candidate generated from the model.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.Candidate}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.Candidate"
   :gcp.dev/certification
     {:base-seed 1774824796155
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1774824796155 :standard 1774824796156 :stress 1774824796157}
      :protocol-hash
        "b44d2581e3979d0a946f2f9d3973f275b05bb339485f3d5eb436c47b54f7dc70"
      :timestamp "2026-03-29T22:53:18.472374818Z"}}
  (:require [gcp.global :as global]
            [gcp.vertexai.api.CitationMetadata :as CitationMetadata]
            [gcp.vertexai.api.Content :as Content]
            [gcp.vertexai.api.GroundingMetadata :as GroundingMetadata]
            [gcp.vertexai.api.LogprobsResult :as LogprobsResult]
            [gcp.vertexai.api.SafetyRating :as SafetyRating]
            [gcp.vertexai.api.UrlContextMetadata :as UrlContextMetadata])
  (:import [com.google.cloud.vertexai.api Candidate Candidate$Builder
            Candidate$FinishReason]))

(declare from-edn to-edn FinishReason-from-edn FinishReason-to-edn)

(def FinishReason-schema
  [:enum
   {:closed true,
    :doc
      "<pre>\nThe reason why the model stopped generating tokens.\nIf empty, the model has not stopped generating the tokens.\n</pre>\n\nProtobuf enum {@code google.cloud.vertexai.v1.Candidate.FinishReason}",
    :gcp/category :nested/enum,
    :gcp/key :gcp.vertexai.api/Candidate.FinishReason}
   "FINISH_REASON_UNSPECIFIED" "STOP" "MAX_TOKENS" "SAFETY" "RECITATION" "OTHER"
   "BLOCKLIST" "PROHIBITED_CONTENT" "SPII" "MALFORMED_FUNCTION_CALL"
   "MODEL_ARMOR"])

(defn ^Candidate from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/Candidate arg)
  (let [builder (Candidate/newBuilder)]
    (when (some? (get arg :avgLogprobs))
      (.setAvgLogprobs builder (double (get arg :avgLogprobs))))
    (when (some? (get arg :citationMetadata))
      (.setCitationMetadata builder
                            (CitationMetadata/from-edn
                              (get arg :citationMetadata))))
    (when (some? (get arg :content))
      (.setContent builder (Content/from-edn (get arg :content))))
    (when (some? (get arg :finishMessage))
      (.setFinishMessage builder (get arg :finishMessage)))
    (when (some? (get arg :finishReason))
      (.setFinishReason builder
                        (Candidate$FinishReason/valueOf (get arg
                                                             :finishReason))))
    (when (some? (get arg :groundingMetadata))
      (.setGroundingMetadata builder
                             (GroundingMetadata/from-edn
                               (get arg :groundingMetadata))))
    (when (some? (get arg :index)) (.setIndex builder (int (get arg :index))))
    (when (some? (get arg :logprobsResult))
      (.setLogprobsResult builder
                          (LogprobsResult/from-edn (get arg :logprobsResult))))
    (when (seq (get arg :safetyRatings))
      (.addAllSafetyRatings builder
                            (map SafetyRating/from-edn
                              (get arg :safetyRatings))))
    (when (some? (get arg :score))
      (.setScore builder (double (get arg :score))))
    (when (some? (get arg :urlContextMetadata))
      (.setUrlContextMetadata builder
                              (UrlContextMetadata/from-edn
                                (get arg :urlContextMetadata))))
    (.build builder)))

(defn to-edn
  [^Candidate arg]
  {:post [(global/strict! :gcp.vertexai.api/Candidate %)]}
  (cond-> {}
    (.getAvgLogprobs arg) (assoc :avgLogprobs (.getAvgLogprobs arg))
    (.hasCitationMetadata arg) (assoc :citationMetadata
                                 (CitationMetadata/to-edn (.getCitationMetadata
                                                            arg)))
    (.hasContent arg) (assoc :content (Content/to-edn (.getContent arg)))
    (.hasFinishMessage arg) (assoc :finishMessage (.getFinishMessage arg))
    (.getFinishReason arg) (assoc :finishReason (.name (.getFinishReason arg)))
    (.hasGroundingMetadata arg) (assoc :groundingMetadata
                                  (GroundingMetadata/to-edn
                                    (.getGroundingMetadata arg)))
    (.getIndex arg) (assoc :index (.getIndex arg))
    (.hasLogprobsResult arg) (assoc :logprobsResult
                               (LogprobsResult/to-edn (.getLogprobsResult arg)))
    (seq (.getSafetyRatingsList arg)) (assoc :safetyRatings
                                        (map SafetyRating/to-edn
                                          (.getSafetyRatingsList arg)))
    (.getScore arg) (assoc :score (.getScore arg))
    (.hasUrlContextMetadata arg) (assoc :urlContextMetadata
                                   (UrlContextMetadata/to-edn
                                     (.getUrlContextMetadata arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nA response candidate generated from the model.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.Candidate}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/Candidate}
   [:avgLogprobs
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. Average log probability score of the candidate.\n</pre>\n\n<code>double avg_logprobs = 9 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>\n\n@return The avgLogprobs."}
    :f64]
   [:citationMetadata
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. Source attribution of the generated content.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.CitationMetadata citation_metadata = 6 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>\n\n@return The citationMetadata."}
    :gcp.vertexai.api/CitationMetadata]
   [:content
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. Content parts of the candidate.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.Content content = 2 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>\n\n@return The content."}
    :gcp.vertexai.api/Content]
   [:finishMessage
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. Describes the reason the mode stopped generating tokens in\nmore detail. This is only filled when `finish_reason` is set.\n</pre>\n\n<code>optional string finish_message = 5 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>\n\n@return The finishMessage."}
    [:string {:min 1}]]
   [:finishReason
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. The reason why the model stopped generating tokens.\nIf empty, the model has not stopped generating the tokens.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.Candidate.FinishReason finish_reason = 3 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>\n\n@return The finishReason."}
    [:enum {:closed true} "FINISH_REASON_UNSPECIFIED" "STOP" "MAX_TOKENS"
     "SAFETY" "RECITATION" "OTHER" "BLOCKLIST" "PROHIBITED_CONTENT" "SPII"
     "MALFORMED_FUNCTION_CALL" "MODEL_ARMOR"]]
   [:groundingMetadata
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. Metadata specifies sources used to ground generated content.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.GroundingMetadata grounding_metadata = 7 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>\n\n@return The groundingMetadata."}
    :gcp.vertexai.api/GroundingMetadata]
   [:index
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. Index of the candidate.\n</pre>\n\n<code>int32 index = 1 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>\n\n@return The index."}
    :i32]
   [:logprobsResult
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. Log-likelihood scores for the response tokens and top tokens\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.LogprobsResult logprobs_result = 10 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>\n\n@return The logprobsResult."}
    :gcp.vertexai.api/LogprobsResult]
   [:safetyRatings
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. List of ratings for the safety of a response candidate.\n\nThere is at most one rating per category.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.SafetyRating safety_ratings = 4 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>"}
    [:sequential {:min 1} :gcp.vertexai.api/SafetyRating]]
   [:score
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. Confidence score of the candidate.\n</pre>\n\n<code>double score = 8 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>\n\n@return The score."}
    :f64]
   [:urlContextMetadata
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. Metadata related to url context retrieval tool.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.UrlContextMetadata url_context_metadata = 11 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>\n\n@return The urlContextMetadata."}
    :gcp.vertexai.api/UrlContextMetadata]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/Candidate schema,
              :gcp.vertexai.api/Candidate.FinishReason FinishReason-schema}
    {:gcp.global/name "gcp.vertexai.api.Candidate"}))