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

(defn ^GenerateContentResponse from-edn
  [{:keys [candidates promptFeedback usageMetadata] :as arg}]
  (global/strict! :gcp/vertexai.api.GenerateContentResponse arg)
  (let [builder (GenerateContentResponse/newBuilder)]
    (some->> usageMetadata usageMetadata-from-edn (.setUsageMetadata builder))
    (some->> promptFeedback promptFeedback-from-edn (.setPromptFeedback builder))
    (.addAllCandidates builder (map Candidate/from-edn candidates))
    (.build builder)))

(defn to-edn
  [^GenerateContentResponse arg]
  {:post [(global/strict! :gcp/vertexai.api.GenerateContentResponse %)]}
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