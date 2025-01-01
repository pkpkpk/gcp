(ns gcp.vertexai.v1.api.GenerateContentResponse
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.Candidate :as Candidate]
            [gcp.vertexai.v1.api.SafetyRating :as SafetyRating])
  (:import [com.google.cloud.vertexai.api GenerateContentResponse
                                          GenerateContentResponse$PromptFeedback
                                          GenerateContentResponse$PromptFeedback$BlockedReason
                                          GenerateContentResponse$UsageMetadata]))

(defn ^GenerateContentResponse$PromptFeedback promptFeedback-from-edn
  [{:keys [safetyRatings blockReason]}]
  (let [builder (GenerateContentResponse$PromptFeedback/newBuilder)]
    (some->> (not-empty safetyRatings)
             (map SafetyRating/from-edn)
             (.addAllSafetyRatings builder))
    (some->> blockReason (.setBlockReason builder))
    (.build builder)))

(defn promptFeedback-to-edn
  [^GenerateContentResponse$PromptFeedback arg]
  (cond-> {}
          (pos? (.getSafetyRatingsCount arg))
          (assoc :safetyRatings (mapv SafetyRating/to-edn (.getSafetyRatingsList arg)))
          (some? (.getBlockReason arg))
          (assoc :blockReason (if (int? (.getBlockReason arg))
                                (GenerateContentResponse$PromptFeedback$BlockedReason/forNumber ^int (.getBlockReason arg))
                                (.name (.getBlockReason arg))))))

(defn ^GenerateContentResponse$UsageMetadata usageMetadata-from-edn
  [{:keys [candidatesTokenCount promptTokenCount tokenCount]}]
  (-> (GenerateContentResponse$UsageMetadata/newBuilder)
      (.setCandidatesTokenCount candidatesTokenCount)
      (.setPromptTokenCount promptTokenCount)
      (.setTotalTokenCount tokenCount)
      (.build)))

(defn usageMetadata-to-edn
  [^GenerateContentResponse$UsageMetadata arg]
  {:totalTokenCount (.getTotalTokenCount arg)
   :candidatesTokenCount (.getCandidatesTokenCount arg)
   :promptTokenCount (.getPromptTokenCount arg)})

(def ^{:class GenerateContentResponse} schema
  [:map {:doc "Response from the model supporting multiple candidate responses Safety ratings and content filtering are reported for both prompt in GenerateContentResponse.prompt_feedback and for each candidate in finishReason and in safetyRatings. The API: - Returns either all requested candidates or none of them - Returns no candidates at all only if there was something wrong with the prompt (check promptFeedback) - Reports feedback on each candidate in finishReason and safetyRatings."}
   [:candidates
    {:doc "Candidate responses from the model."}
    ;; TODO count should be exactly= 1
    [:seqable Candidate/schema]]
   [:promptFeedback
    {:optional true
     :doc "Returns the prompt's feedback related to the content filters."}
    [:map {:doc "A set of the feedback metadata the prompt specified in GenerateContentRequest.content"}
     [:blockReason
      {:optional true
       :doc      "If set, the prompt was blocked and no candidates are returned. Rephrase the prompt."}
      [:enum "BLOCK_REASON_UNSPECIFIED" "SAFETY" "OTHER" "BLOCKLIST" "PROHIBITED_CONTENT"]]
     [:safetyRatings
      {:doc "Ratings for safety of the prompt. There is at most one rating per category."}
      [:seqable SafetyRating/schema]]]]
   [:usageMetadata
    {:optional true
     :doc "Output only. Metadata on the generation requests' token usage."}
    [:map
     [:promptTokenCount
      {:doc "Number of tokens in the prompt. When cachedContent is set, this is still the total effective prompt size meaning this includes the number of tokens in the cached content."}
      :int]
     #_[:cachedContentTokenCount
      {:optional true
       :doc "Number of tokens in the cached part of the prompt (the cached content)"}
      :int]
     [:candidatesTokenCount
      {:doc "Total number of tokens across all the generated response candidates"}
      :int]
     [:totalTokenCount
      {:doc "Total token count for the generation request (prompt + response candidates)"}
      :int]]]])


(defn ^GenerateContentResponse from-edn
  [{:keys [candidates promptFeedback usageMetadata] :as arg}]
  (global/strict! schema arg)
  (let [builder (GenerateContentResponse/newBuilder)]
    (some->> usageMetadata usageMetadata-from-edn (.setUsageMetadata builder))
    (some->> promptFeedback promptFeedback-from-edn (.setPromptFeedback builder))
    (.addAllCandidates builder (map Candidate/from-edn candidates))
    (.build builder)))

(defn to-edn
  [^GenerateContentResponse arg]
  {:post [(global/strict! schema %)]}
  (cond-> {}
          (.hasPromptFeedback arg)
          (assoc :promptFeedback (promptFeedback-to-edn (.getPromptFeedback arg)))
          (.hasUsageMetadata arg)
          (assoc :usageMetadata (usageMetadata-to-edn (.getUsageMetadata arg)))
          (pos? (.getCandidatesCount arg))
          (assoc :candidates (mapv Candidate/to-edn (.getCandidatesList arg)))))

(defn extract-text
  [{[{{[{text :text}] :parts} :content}] :candidates}]
  text)