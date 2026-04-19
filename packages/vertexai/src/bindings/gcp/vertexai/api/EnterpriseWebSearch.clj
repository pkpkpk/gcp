;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.EnterpriseWebSearch
  {:doc
     "<pre>\nTool to search public web data, powered by Vertex AI Search and Sec4\ncompliance.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.EnterpriseWebSearch}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.EnterpriseWebSearch"
   :gcp.dev/certification
     {:base-seed 1776627483357
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627483357 :standard 1776627483358 :stress 1776627483359}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:38:04.260975336Z"}}
  (:require [gcp.foreign.com.google.protobuf :as protobuf]
            [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api EnterpriseWebSearch
            EnterpriseWebSearch$Builder Tool$PhishBlockThreshold]
           [com.google.protobuf ProtocolStringList]))

(declare from-edn to-edn)

(defn ^EnterpriseWebSearch from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/EnterpriseWebSearch arg)
  (let [builder (EnterpriseWebSearch/newBuilder)]
    (when (some? (get arg :blockingConfidence))
      (.setBlockingConfidence builder
                              (Tool$PhishBlockThreshold/valueOf
                                (get arg :blockingConfidence))))
    (when (seq (get arg :excludeDomains))
      (.addAllExcludeDomains builder (seq (get arg :excludeDomains))))
    (.build builder)))

(defn to-edn
  [^EnterpriseWebSearch arg]
  {:post [(global/strict! :gcp.vertexai.api/EnterpriseWebSearch %)]}
  (when arg
    (cond-> {}
      (.hasBlockingConfidence arg) (assoc :blockingConfidence
                                     (.name (.getBlockingConfidence arg)))
      (seq (.getExcludeDomainsList arg)) (assoc :excludeDomains
                                           (protobuf/ProtocolStringList-to-edn
                                             (.getExcludeDomainsList arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nTool to search public web data, powered by Vertex AI Search and Sec4\ncompliance.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.EnterpriseWebSearch}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/EnterpriseWebSearch}
   [:blockingConfidence
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Sites with confidence level chosen &amp; above this value will be\nblocked from the search results.\n</pre>\n\n<code>\noptional .google.cloud.vertexai.v1.Tool.PhishBlockThreshold blocking_confidence = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The blockingConfidence.",
     :setter-doc
       "<pre>\nOptional. Sites with confidence level chosen &amp; above this value will be\nblocked from the search results.\n</pre>\n\n<code>\noptional .google.cloud.vertexai.v1.Tool.PhishBlockThreshold blocking_confidence = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@param value The blockingConfidence to set.\n@return This builder for chaining."}
    [:enum {:closed true} "PHISH_BLOCK_THRESHOLD_UNSPECIFIED"
     "BLOCK_LOW_AND_ABOVE" "BLOCK_MEDIUM_AND_ABOVE" "BLOCK_HIGH_AND_ABOVE"
     "BLOCK_HIGHER_AND_ABOVE" "BLOCK_VERY_HIGH_AND_ABOVE"
     "BLOCK_ONLY_EXTREMELY_HIGH"]]
   [:excludeDomains
    {:optional true,
     :getter-doc
       "<pre>\nOptional. List of domains to be excluded from the search results.\nThe default limit is 2000 domains.\n</pre>\n\n<code>repeated string exclude_domains = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return A list containing the excludeDomains.",
     :setter-doc
       "<pre>\nOptional. List of domains to be excluded from the search results.\nThe default limit is 2000 domains.\n</pre>\n\n<code>repeated string exclude_domains = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param values The excludeDomains to add.\n@return This builder for chaining."}
    :gcp.foreign.com.google.protobuf/ProtocolStringList]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/EnterpriseWebSearch schema}
    {:gcp.global/name "gcp.vertexai.api.EnterpriseWebSearch"}))