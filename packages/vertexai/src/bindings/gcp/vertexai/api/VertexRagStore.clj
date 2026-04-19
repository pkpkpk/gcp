;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.VertexRagStore
  {:doc
     "<pre>\nRetrieve from Vertex RAG Store for grounding.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.VertexRagStore}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.VertexRagStore"
   :gcp.dev/certification
     {:base-seed 1776627417800
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627417800 :standard 1776627417801 :stress 1776627417802}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:36:58.758462452Z"}}
  (:require [gcp.foreign.com.google.protobuf :as protobuf]
            [gcp.global :as global]
            [gcp.vertexai.api.RagRetrievalConfig :as RagRetrievalConfig])
  (:import [com.google.cloud.vertexai.api VertexRagStore VertexRagStore$Builder
            VertexRagStore$RagResource VertexRagStore$RagResource$Builder]
           [com.google.protobuf ProtocolStringList]))

(declare from-edn to-edn RagResource-from-edn RagResource-to-edn)

(defn ^VertexRagStore$RagResource RagResource-from-edn
  [arg]
  (let [builder (VertexRagStore$RagResource/newBuilder)]
    (when (some? (get arg :ragCorpus))
      (.setRagCorpus builder (get arg :ragCorpus)))
    (when (seq (get arg :ragFileIds))
      (.addAllRagFileIds builder (seq (get arg :ragFileIds))))
    (.build builder)))

(defn RagResource-to-edn
  [^VertexRagStore$RagResource arg]
  (when arg
    (cond-> {}
      (some->> (.getRagCorpus arg)
               (not= ""))
        (assoc :ragCorpus (.getRagCorpus arg))
      (seq (.getRagFileIdsList arg)) (assoc :ragFileIds
                                       (protobuf/ProtocolStringList-to-edn
                                         (.getRagFileIdsList arg))))))

(def RagResource-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nThe definition of the Rag resource.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.VertexRagStore.RagResource}",
    :gcp/category :nested/protobuf-message,
    :gcp/key :gcp.vertexai.api/VertexRagStore.RagResource}
   [:ragCorpus
    {:optional true,
     :getter-doc
       "<pre>\nOptional. RagCorpora resource name.\nFormat:\n`projects/{project}/locations/{location}/ragCorpora/{rag_corpus}`\n</pre>\n\n<code>\nstring rag_corpus = 1 [(.google.api.field_behavior) = OPTIONAL, (.google.api.resource_reference) = { ... }\n</code>\n\n@return The ragCorpus.",
     :setter-doc
       "<pre>\nOptional. RagCorpora resource name.\nFormat:\n`projects/{project}/locations/{location}/ragCorpora/{rag_corpus}`\n</pre>\n\n<code>\nstring rag_corpus = 1 [(.google.api.field_behavior) = OPTIONAL, (.google.api.resource_reference) = { ... }\n</code>\n\n@param value The ragCorpus to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:ragFileIds
    {:optional true,
     :getter-doc
       "<pre>\nOptional. rag_file_id. The files should be in the same rag_corpus set in\nrag_corpus field.\n</pre>\n\n<code>repeated string rag_file_ids = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return A list containing the ragFileIds.",
     :setter-doc
       "<pre>\nOptional. rag_file_id. The files should be in the same rag_corpus set in\nrag_corpus field.\n</pre>\n\n<code>repeated string rag_file_ids = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param values The ragFileIds to add.\n@return This builder for chaining."}
    :gcp.foreign.com.google.protobuf/ProtocolStringList]])

(defn ^VertexRagStore from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/VertexRagStore arg)
  (let [builder (VertexRagStore/newBuilder)]
    (when (seq (get arg :ragResources))
      (.addAllRagResources builder
                           (mapv RagResource-from-edn (get arg :ragResources))))
    (when (some? (get arg :ragRetrievalConfig))
      (.setRagRetrievalConfig builder
                              (RagRetrievalConfig/from-edn
                                (get arg :ragRetrievalConfig))))
    (.build builder)))

(defn to-edn
  [^VertexRagStore arg]
  {:post [(global/strict! :gcp.vertexai.api/VertexRagStore %)]}
  (when arg
    (cond-> {}
      (seq (.getRagResourcesList arg)) (assoc :ragResources
                                         (mapv RagResource-to-edn
                                           (.getRagResourcesList arg)))
      (.hasRagRetrievalConfig arg) (assoc :ragRetrievalConfig
                                     (RagRetrievalConfig/to-edn
                                       (.getRagRetrievalConfig arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nRetrieve from Vertex RAG Store for grounding.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.VertexRagStore}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/VertexRagStore}
   [:ragResources
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The representation of the rag source. It can be used to specify\ncorpus only or ragfiles. Currently only support one corpus or multiple\nfiles from one corpus. In the future we may open up multiple corpora\nsupport.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.VertexRagStore.RagResource rag_resources = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>",
     :setter-doc
       "<pre>\nOptional. The representation of the rag source. It can be used to specify\ncorpus only or ragfiles. Currently only support one corpus or multiple\nfiles from one corpus. In the future we may open up multiple corpora\nsupport.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.VertexRagStore.RagResource rag_resources = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    [:sequential {:min 1, :gen/max 2}
     [:ref :gcp.vertexai.api/VertexRagStore.RagResource]]]
   [:ragRetrievalConfig
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The retrieval config for the Rag query.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.RagRetrievalConfig rag_retrieval_config = 6 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The ragRetrievalConfig.",
     :setter-doc
       "<pre>\nOptional. The retrieval config for the Rag query.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.RagRetrievalConfig rag_retrieval_config = 6 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.vertexai.api/RagRetrievalConfig]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/VertexRagStore schema,
              :gcp.vertexai.api/VertexRagStore.RagResource RagResource-schema}
    {:gcp.global/name "gcp.vertexai.api.VertexRagStore"}))