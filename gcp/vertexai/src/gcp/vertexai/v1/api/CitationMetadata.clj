(ns gcp.vertexai.v1.api.CitationMetadata
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.Citation :as Citation])
  (:import [com.google.cloud.vertexai.api CitationMetadata]))

(defn ^CitationMetadata from-edn
  [{:keys [citations] :as arg}]
  (global/strict! :gcp/vertexai.api.CitationMetadata arg)
  (let [builder (CitationMetadata/newBuilder)]
    (.addAllCitations builder (map Citation/from-edn citations))
    (.build builder)))

(defn to-edn [^CitationMetadata arg]
  {:post [(global/strict! :gcp/vertexai.api.CitationMetadata %)]}
  {:citations (map Citation/to-edn (.getCitationsList arg))})