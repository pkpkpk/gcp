(ns gcp.vertexai.v1.api.CitationMetadata
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.Citation :as Citation])
  (:import [com.google.cloud.vertexai.api CitationMetadata]))

(def ^{:class CitationMetadata} schema
  [:map {:closed true} ;[:ref :vertexai.api/Citation]
   [:citations [:sequential Citation/schema]]])

(defn ^CitationMetadata from-edn
  [{:keys [citations] :as arg}]
  (global/strict! schema arg)
  (let [builder (CitationMetadata/newBuilder)]
    (.addAllCitations builder (map Citation/from-edn citations))
    (.build builder)))

(defn to-edn [^CitationMetadata arg]
  {:post [(global/strict! schema %)]}
  {:citations (map Citation/to-edn (.getCitationsList arg))})