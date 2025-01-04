(ns gcp.vertexai.v1.api.VertexAISearch
  (:import (com.google.cloud.vertexai.api VertexAISearch)))

(defn ^VertexAISearch from-edn
  [{:keys [datastore]}]
  (-> (VertexAISearch/newBuilder)
      (.setDatastore datastore)
      (.build)))

(defn to-edn [^VertexAISearch arg]
  {:datastore (.getDatastore arg)})
