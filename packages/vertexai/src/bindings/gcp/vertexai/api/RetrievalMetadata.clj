;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.RetrievalMetadata
  {:doc
     "<pre>\nMetadata related to retrieval in the grounding flow.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.RetrievalMetadata}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.RetrievalMetadata"
   :gcp.dev/certification
     {:base-seed 1775465680077
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1775465680077 :standard 1775465680078 :stress 1775465680079}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-06T08:54:41.134647785Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api RetrievalMetadata
            RetrievalMetadata$Builder]))

(declare from-edn to-edn)

(defn ^RetrievalMetadata from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/RetrievalMetadata arg)
  (let [builder (RetrievalMetadata/newBuilder)]
    (when (some? (get arg :googleSearchDynamicRetrievalScore))
      (.setGoogleSearchDynamicRetrievalScore
        builder
        (float (get arg :googleSearchDynamicRetrievalScore))))
    (.build builder)))

(defn to-edn
  [^RetrievalMetadata arg]
  {:post [(global/strict! :gcp.vertexai.api/RetrievalMetadata %)]}
  (when arg
    (cond-> {}
      (.getGoogleSearchDynamicRetrievalScore arg)
        (assoc :googleSearchDynamicRetrievalScore
          (.getGoogleSearchDynamicRetrievalScore arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nMetadata related to retrieval in the grounding flow.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.RetrievalMetadata}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/RetrievalMetadata}
   [:googleSearchDynamicRetrievalScore
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Score indicating how likely information from Google Search could\nhelp answer the prompt. The score is in the range `[0, 1]`, where 0 is the\nleast likely and 1 is the most likely. This score is only populated when\nGoogle Search grounding and dynamic retrieval is enabled. It will be\ncompared to the threshold to determine whether to trigger Google Search.\n</pre>\n\n<code>\nfloat google_search_dynamic_retrieval_score = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The googleSearchDynamicRetrievalScore.",
     :setter-doc
       "<pre>\nOptional. Score indicating how likely information from Google Search could\nhelp answer the prompt. The score is in the range `[0, 1]`, where 0 is the\nleast likely and 1 is the most likely. This score is only populated when\nGoogle Search grounding and dynamic retrieval is enabled. It will be\ncompared to the threshold to determine whether to trigger Google Search.\n</pre>\n\n<code>\nfloat google_search_dynamic_retrieval_score = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@param value The googleSearchDynamicRetrievalScore to set.\n@return This builder for chaining."}
    :f32]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/RetrievalMetadata schema}
    {:gcp.global/name "gcp.vertexai.api.RetrievalMetadata"}))