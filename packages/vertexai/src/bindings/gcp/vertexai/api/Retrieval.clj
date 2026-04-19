;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.Retrieval
  {:doc
     "<pre>\nDefines a retrieval tool that model can call to access external knowledge.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.Retrieval}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.Retrieval"
   :gcp.dev/certification
     {:base-seed 1776627457442
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627457442 :standard 1776627457443 :stress 1776627457444}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:37:38.505643075Z"}}
  (:require [gcp.global :as global]
            [gcp.vertexai.api.VertexAISearch :as VertexAISearch]
            [gcp.vertexai.api.VertexRagStore :as VertexRagStore])
  (:import [com.google.cloud.vertexai.api Retrieval Retrieval$Builder
            Retrieval$SourceCase]))

(declare from-edn to-edn SourceCase-from-edn SourceCase-to-edn)

(def SourceCase-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.vertexai.api/Retrieval.SourceCase} "VERTEX_AI_SEARCH"
   "VERTEX_RAG_STORE" "SOURCE_NOT_SET"])

(defn ^Retrieval from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/Retrieval arg)
  (let [builder (Retrieval/newBuilder)]
    (cond (contains? arg :vertexAiSearch) (.setVertexAiSearch
                                            builder
                                            (VertexAISearch/from-edn
                                              (get arg :vertexAiSearch)))
          (contains? arg :vertexRagStore) (.setVertexRagStore
                                            builder
                                            (VertexRagStore/from-edn
                                              (get arg :vertexRagStore))))
    (.build builder)))

(defn to-edn
  [^Retrieval arg]
  {:post [(global/strict! :gcp.vertexai.api/Retrieval %)]}
  (when arg
    (let [res (cond-> {})
          res
            (case (.name (.getSourceCase arg))
              "VERTEX_AI_SEARCH" (assoc res
                                   :vertexAiSearch (VertexAISearch/to-edn
                                                     (.getVertexAiSearch arg)))
              "VERTEX_RAG_STORE" (assoc res
                                   :vertexRagStore (VertexRagStore/to-edn
                                                     (.getVertexRagStore arg)))
              res)]
      res)))

(def schema
  [:and
   {:closed true,
    :doc
      "<pre>\nDefines a retrieval tool that model can call to access external knowledge.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.Retrieval}",
    :gcp/category :union-protobuf-oneof,
    :gcp/key :gcp.vertexai.api/Retrieval}
   [:map {:closed true}
    [:vertexAiSearch
     {:optional true,
      :getter-doc
        "<pre>\nSet to use data source powered by Vertex AI Search.\n</pre>\n\n<code>.google.cloud.vertexai.v1.VertexAISearch vertex_ai_search = 2;</code>\n\n@return The vertexAiSearch.",
      :setter-doc
        "<pre>\nSet to use data source powered by Vertex AI Search.\n</pre>\n\n<code>.google.cloud.vertexai.v1.VertexAISearch vertex_ai_search = 2;</code>"}
     :gcp.vertexai.api/VertexAISearch]
    [:vertexRagStore
     {:optional true,
      :getter-doc
        "<pre>\nSet to use data source powered by Vertex RAG store.\nUser data is uploaded via the VertexRagDataService.\n</pre>\n\n<code>.google.cloud.vertexai.v1.VertexRagStore vertex_rag_store = 4;</code>\n\n@return The vertexRagStore.",
      :setter-doc
        "<pre>\nSet to use data source powered by Vertex RAG store.\nUser data is uploaded via the VertexRagDataService.\n</pre>\n\n<code>.google.cloud.vertexai.v1.VertexRagStore vertex_rag_store = 4;</code>"}
     :gcp.vertexai.api/VertexRagStore]]
   [:fn
    {:error/message
       "Only one of these keys may be present: #{:vertexAiSearch :vertexRagStore}"}
    (quote (fn [m]
             (<= (count (filter (set (keys m))
                          #{:vertexAiSearch :vertexRagStore}))
                 1)))]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/Retrieval schema,
              :gcp.vertexai.api/Retrieval.SourceCase SourceCase-schema}
    {:gcp.global/name "gcp.vertexai.api.Retrieval"}))