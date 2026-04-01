;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.CitationMetadata
  {:doc
     "<pre>\nA collection of source attributions for a piece of content.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.CitationMetadata}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.CitationMetadata"
   :gcp.dev/certification
     {:base-seed 1774824740755
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1774824740755 :standard 1774824740756 :stress 1774824740757}
      :protocol-hash
        "b44d2581e3979d0a946f2f9d3973f275b05bb339485f3d5eb436c47b54f7dc70"
      :timestamp "2026-03-29T22:52:21.801095230Z"}}
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
  (cond-> {}
    (seq (.getCitationsList arg))
      (assoc :citations (map Citation/to-edn (.getCitationsList arg)))))

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