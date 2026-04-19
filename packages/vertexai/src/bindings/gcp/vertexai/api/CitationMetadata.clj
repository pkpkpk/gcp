;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.CitationMetadata
  {:doc
     "<pre>\nA collection of source attributions for a piece of content.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.CitationMetadata}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.CitationMetadata"
   :gcp.dev/certification
     {:base-seed 1776627402231
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627402231 :standard 1776627402232 :stress 1776627402233}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:36:43.086037785Z"}}
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
      (.addAllCitations builder (mapv Citation/from-edn (get arg :citations))))
    (.build builder)))

(defn to-edn
  [^CitationMetadata arg]
  {:post [(global/strict! :gcp.vertexai.api/CitationMetadata %)]}
  (when arg
    (cond-> {}
      (seq (.getCitationsList arg))
        (assoc :citations (mapv Citation/to-edn (.getCitationsList arg))))))

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
    [:sequential {:min 1, :gen/max 2} :gcp.vertexai.api/Citation]]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/CitationMetadata schema}
    {:gcp.global/name "gcp.vertexai.api.CitationMetadata"}))