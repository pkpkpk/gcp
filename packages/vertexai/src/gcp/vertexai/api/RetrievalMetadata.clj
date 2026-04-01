;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.RetrievalMetadata
  {:doc
     "<pre>\nMetadata related to retrieval in the grounding flow.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.RetrievalMetadata}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.RetrievalMetadata"
   :gcp.dev/certification
     {:base-seed 1774824767679
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1774824767679 :standard 1774824767680 :stress 1774824767681}
      :protocol-hash
        "b44d2581e3979d0a946f2f9d3973f275b05bb339485f3d5eb436c47b54f7dc70"
      :timestamp "2026-03-29T22:52:48.766250961Z"}}
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
  (cond-> {}
    (.getGoogleSearchDynamicRetrievalScore arg)
      (assoc :googleSearchDynamicRetrievalScore
        (.getGoogleSearchDynamicRetrievalScore arg))))

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