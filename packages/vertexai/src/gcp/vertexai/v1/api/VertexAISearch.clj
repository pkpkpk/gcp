(ns gcp.vertexai.v1.api.VertexAISearch
  (:require [gcp.global :as global])
  (:import (com.google.cloud.vertexai.api VertexAISearch)))

(defn ^VertexAISearch from-edn
  [{:keys [datastore] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/VertexAISearch arg)
  (-> (VertexAISearch/newBuilder)
      (.setDatastore datastore)
      (.build)))

(defn to-edn [^VertexAISearch arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/VertexAISearch %)]}
  {:datastore (.getDatastore arg)})

(global/register-schema! :gcp.vertexai.v1.api/datastore-resource-id
                         [:and
                          :string
                          [:fn
                           {:error/message "datastore-resource-id must conform to format 'projects/{project}/locations/{location}/collections/{collection}/dataStores/{dataStore}'"}
                           '(fn [s]
                              (let [parts (clojure.string/split s (re-pattern "/"))]
                                (and
                                  (= "projects" (nth parts 0))
                                  (some? (nth parts 1))
                                  (= "locations" (nth parts 2))
                                  (some? (nth parts 3))
                                  (= "collections" (nth parts 4))
                                  (some? (nth parts 5))
                                  (= "dataStores" (nth parts 6))
                                  (some? (nth parts 7)))))]])

(def schema
  [:map
   {:closed   true
    :class    'com.google.cloud.vertexai.api.VertexAISearch
    :from-edn 'gcp.vertexai.v1.api.VertexAISearch/from-edn
    :to-edn   'gcp.vertexai.v1.api.VertexAISearch/to-edn
    :protobuf/type    "google.cloud.vertexai.v1.VertexAISearch"}
   [:datastore {:optional false} :gcp.vertexai.v1.api/datastore-resource-id]])

(global/register-schema! :gcp.vertexai.v1.api/VertexAISearch schema)
