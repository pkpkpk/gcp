;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.LogprobsResult
  {:doc
     "<pre>\nLogprobs Result\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.LogprobsResult}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.LogprobsResult"
   :gcp.dev/certification
     {:base-seed 1774824776849
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1774824776849 :standard 1774824776850 :stress 1774824776851}
      :protocol-hash
        "b44d2581e3979d0a946f2f9d3973f275b05bb339485f3d5eb436c47b54f7dc70"
      :timestamp "2026-03-29T22:52:57.999078553Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api LogprobsResult LogprobsResult$Builder
            LogprobsResult$Candidate LogprobsResult$Candidate$Builder
            LogprobsResult$TopCandidates LogprobsResult$TopCandidates$Builder]))

(declare from-edn
         to-edn
         Candidate-from-edn
         Candidate-to-edn
         TopCandidates-from-edn
         TopCandidates-to-edn)

(defn ^LogprobsResult$Candidate Candidate-from-edn
  [arg]
  (let [builder (LogprobsResult$Candidate/newBuilder)]
    (when (some? (get arg :logProbability))
      (.setLogProbability builder (float (get arg :logProbability))))
    (when (some? (get arg :token)) (.setToken builder (get arg :token)))
    (when (some? (get arg :tokenId))
      (.setTokenId builder (int (get arg :tokenId))))
    (.build builder)))

(defn Candidate-to-edn
  [^LogprobsResult$Candidate arg]
  (cond-> {}
    (.hasLogProbability arg) (assoc :logProbability (.getLogProbability arg))
    (.hasToken arg) (assoc :token (.getToken arg))
    (.hasTokenId arg) (assoc :tokenId (.getTokenId arg))))

(def Candidate-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nCandidate for the logprobs token and score.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.LogprobsResult.Candidate}",
    :gcp/category :nested/protobuf-message,
    :gcp/key :gcp.vertexai.api/LogprobsResult.Candidate}
   [:logProbability
    {:optional true,
     :getter-doc
       "<pre>\nThe candidate's log probability.\n</pre>\n\n<code>optional float log_probability = 2;</code>\n\n@return The logProbability.",
     :setter-doc
       "<pre>\nThe candidate's log probability.\n</pre>\n\n<code>optional float log_probability = 2;</code>\n\n@param value The logProbability to set.\n@return This builder for chaining."}
    :f32]
   [:token
    {:optional true,
     :getter-doc
       "<pre>\nThe candidate’s token string value.\n</pre>\n\n<code>optional string token = 1;</code>\n\n@return The token.",
     :setter-doc
       "<pre>\nThe candidate’s token string value.\n</pre>\n\n<code>optional string token = 1;</code>\n\n@param value The token to set.\n@return This builder for chaining."}
    [:string {:min 1}]]
   [:tokenId
    {:optional true,
     :getter-doc
       "<pre>\nThe candidate’s token id value.\n</pre>\n\n<code>optional int32 token_id = 3;</code>\n\n@return The tokenId.",
     :setter-doc
       "<pre>\nThe candidate’s token id value.\n</pre>\n\n<code>optional int32 token_id = 3;</code>\n\n@param value The tokenId to set.\n@return This builder for chaining."}
    :i32]])

(defn ^LogprobsResult$TopCandidates TopCandidates-from-edn
  [arg]
  (let [builder (LogprobsResult$TopCandidates/newBuilder)]
    (when (seq (get arg :candidates))
      (.addAllCandidates builder
                         (map Candidate-from-edn (get arg :candidates))))
    (.build builder)))

(defn TopCandidates-to-edn
  [^LogprobsResult$TopCandidates arg]
  (cond-> {}
    (seq (.getCandidatesList arg))
      (assoc :candidates (map Candidate-to-edn (.getCandidatesList arg)))))

(def TopCandidates-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nCandidates with top log probabilities at each decoding step.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.LogprobsResult.TopCandidates}",
    :gcp/category :nested/protobuf-message,
    :gcp/key :gcp.vertexai.api/LogprobsResult.TopCandidates}
   [:candidates
    {:optional true,
     :getter-doc
       "<pre>\nSorted by log probability in descending order.\n</pre>\n\n<code>repeated .google.cloud.vertexai.v1.LogprobsResult.Candidate candidates = 1;</code>",
     :setter-doc
       "<pre>\nSorted by log probability in descending order.\n</pre>\n\n<code>repeated .google.cloud.vertexai.v1.LogprobsResult.Candidate candidates = 1;</code>"}
    [:sequential {:min 1} :gcp.vertexai.api/LogprobsResult.Candidate]]])

(defn ^LogprobsResult from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/LogprobsResult arg)
  (let [builder (LogprobsResult/newBuilder)]
    (when (seq (get arg :chosenCandidates))
      (.addAllChosenCandidates builder
                               (map Candidate-from-edn
                                 (get arg :chosenCandidates))))
    (when (seq (get arg :topCandidates))
      (.addAllTopCandidates builder
                            (map TopCandidates-from-edn
                              (get arg :topCandidates))))
    (.build builder)))

(defn to-edn
  [^LogprobsResult arg]
  {:post [(global/strict! :gcp.vertexai.api/LogprobsResult %)]}
  (cond-> {}
    (seq (.getChosenCandidatesList arg)) (assoc :chosenCandidates
                                           (map Candidate-to-edn
                                             (.getChosenCandidatesList arg)))
    (seq (.getTopCandidatesList arg)) (assoc :topCandidates
                                        (map TopCandidates-to-edn
                                          (.getTopCandidatesList arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nLogprobs Result\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.LogprobsResult}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/LogprobsResult}
   [:chosenCandidates
    {:optional true,
     :getter-doc
       "<pre>\nLength = total number of decoding steps.\nThe chosen candidates may or may not be in top_candidates.\n</pre>\n\n<code>repeated .google.cloud.vertexai.v1.LogprobsResult.Candidate chosen_candidates = 2;</code>",
     :setter-doc
       "<pre>\nLength = total number of decoding steps.\nThe chosen candidates may or may not be in top_candidates.\n</pre>\n\n<code>repeated .google.cloud.vertexai.v1.LogprobsResult.Candidate chosen_candidates = 2;\n</code>"}
    [:sequential {:min 1} :gcp.vertexai.api/LogprobsResult.Candidate]]
   [:topCandidates
    {:optional true,
     :getter-doc
       "<pre>\nLength = total number of decoding steps.\n</pre>\n\n<code>repeated .google.cloud.vertexai.v1.LogprobsResult.TopCandidates top_candidates = 1;\n</code>",
     :setter-doc
       "<pre>\nLength = total number of decoding steps.\n</pre>\n\n<code>repeated .google.cloud.vertexai.v1.LogprobsResult.TopCandidates top_candidates = 1;\n</code>"}
    [:sequential {:min 1} :gcp.vertexai.api/LogprobsResult.TopCandidates]]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/LogprobsResult schema,
              :gcp.vertexai.api/LogprobsResult.Candidate Candidate-schema,
              :gcp.vertexai.api/LogprobsResult.TopCandidates
                TopCandidates-schema}
    {:gcp.global/name "gcp.vertexai.api.LogprobsResult"}))