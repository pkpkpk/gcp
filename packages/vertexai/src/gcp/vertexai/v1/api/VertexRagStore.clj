(ns gcp.vertexai.v1.api.VertexRagStore
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf]
            [gcp.vertexai.v1.api.RagRetrievalConfig :as RagRetrievalConfig])
  (:import (com.google.cloud.vertexai.api VertexRagStore VertexRagStore$RagResource)))

(defn ^VertexRagStore$RagResource RagResource-from-edn [arg]
  (let [builder (VertexRagStore$RagResource/newBuilder)]
    (some->> (:ragCorpus arg) (.setRagCorpus builder))
    (some->> (:ragFileIds arg) (.addAllRagFileIds builder))
    (.build builder)))

(defn RagResource-to-edn [^VertexRagStore$RagResource arg]
  (cond-> {}
          (not (empty? (.getRagCorpus arg)))
          (assoc :ragCorpus (.getRagCorpus arg))
          (pos? (.getRagFileIdsCount arg))
          (assoc :ragFileIds (protobuf/protocolstringlist-to-edn (.getRagFileIdsList arg)))))

(defn ^VertexRagStore from-edn
  [{:keys [ragResources ragRetrievalConfig] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/VertexRagStore arg)
  (let [builder (VertexRagStore/newBuilder)]
    (some->> ragResources (map RagResource-from-edn) (.addAllRagResources builder))
    (some->> ragRetrievalConfig RagRetrievalConfig/from-edn (.setRagRetrievalConfig builder))
    (.build builder)))

(defn to-edn [^VertexRagStore arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/VertexRagStore %)]}
  (cond-> {}
          (pos? (.getRagResourcesCount arg))
          (assoc :ragResources (mapv RagResource-to-edn (.getRagResourcesList arg)))
          (.hasRagRetrievalConfig arg)
          (assoc :ragRetrievalConfig (RagRetrievalConfig/to-edn (.getRagRetrievalConfig arg)))))

(def schema
  [:map
   {:doc              "Configuration for Vertex RAG store."
    :class            'com.google.cloud.vertexai.api.VertexRagStore
    :protobuf/type    "google.cloud.vertexai.v1.VertexRagStore"}
   [:ragResources {:optional true} [:sequential [:map
                                                [:ragCorpus {:optional true} :string]
                                                [:ragFileIds {:optional true} [:sequential :string]]]]]
   [:ragRetrievalConfig {:optional true} :gcp.vertexai.v1.api/RagRetrievalConfig]])

(global/register-schema! :gcp.vertexai.v1.api/VertexRagStore schema)
