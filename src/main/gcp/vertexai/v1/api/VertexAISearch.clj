(ns gcp.vertexai.v1.api.VertexAISearch
  (:require [gcp.vertexai.resources :as resources])
  (:import (com.google.cloud.vertexai.api VertexAISearch)))

(def schema
  [:map {:closed true}
   [:datastore {:optional false} resources/datastore-resource-id-schema]])

(defn ^VertexAISearch from-edn
  [{:keys [datastore]}]
  (-> (VertexAISearch/newBuilder)
      (.setDatastore datastore)
      (.build)))

(defn to-edn [^VertexAISearch arg]
  {:datastore (.getDatastore arg)})
