;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.CitationMetadata
  {:doc
     "<pre>\nA collection of source attributions for a piece of content.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.CitationMetadata}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.CitationMetadata"
   :gcp.dev/certification
     {:base-seed 1775465648016
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1775465648016 :standard 1775465648017 :stress 1775465648018}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-06T08:54:09.159099107Z"}}
  (:require [gcp.global :as global]
            [gcp.vertexai.api.Citation :as Citation])
  (:import [com.google.cloud.vertexai.api CitationMetadata
            CitationMetadata$Builder]))

(declare from-edn to-edn)

(defn ^CitationMetadata from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/CitationMetadata arg)
  (let [builder (CitationMetadata/newBuilder)]
    (when (seq (get arg :citations))
      (.addAllCitations builder (map Citation/from-edn (get arg :citations))))
    (.build builder)))

(defn to-edn
  [^CitationMetadata arg]
  {:post [(global/strict! :gcp.vertexai.api/CitationMetadata %)]}
  (when arg
    (cond-> {}
      (seq (.getCitationsList arg))
        (assoc :citations (map Citation/to-edn (.getCitationsList arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nA collection of source attributions for a piece of content.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.CitationMetadata}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/CitationMetadata}
   [:citations
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. List of citations.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.Citation citations = 1 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>"}
    [:sequential {:min 1} :gcp.vertexai.api/Citation]]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/CitationMetadata schema}
    {:gcp.global/name "gcp.vertexai.api.CitationMetadata"}))