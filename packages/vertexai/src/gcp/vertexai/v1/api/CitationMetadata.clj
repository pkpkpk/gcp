(ns gcp.vertexai.v1.api.CitationMetadata
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.Citation :as Citation])
  (:import [com.google.cloud.vertexai.api CitationMetadata]))

(defn ^CitationMetadata from-edn
  [{:keys [citations] :as arg}]
  ;; (global/strict! :gcp.vertexai.v1.api/CitationMetadata arg)
  (let [builder (CitationMetadata/newBuilder)]
    (.addAllCitations builder (map Citation/from-edn citations))
    (.build builder)))

(defn to-edn [^CitationMetadata arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/CitationMetadata %)]}
  {:citations (map Citation/to-edn (.getCitationsList arg))})

(def schema
  [:map
   {:closed           true
    :ns               'gcp.vertexai.v1.api.CitationMetadata
    :from-edn         'gcp.vertexai.v1.api.CitationMetadata/from-edn
    :to-edn           'gcp.vertexai.v1.api.CitationMetadata/to-edn
    :doc              "A collection of source attributions for a piece of content"
    :generativeai/url "https://ai.google.dev/api/generate-content#citationmetadata"
    :protobuf/type    "google.cloud.vertexai.v1.CitationMetadata"
    :class            'com.google.cloud.vertexai.api.CitationMetadata
    :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.CitationMetadata"}
   [:citations [:sequential :gcp.vertexai.v1.api/Citation]]])

(global/register-schema! :gcp.vertexai.v1.api/CitationMetadata schema)
