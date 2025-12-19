(ns gcp.vertexai.v1.api.RetrievalMetadata
  (:require [gcp.global :as global])
  (:import (com.google.cloud.vertexai.api RetrievalMetadata)))

(defn ^RetrievalMetadata from-edn [arg]
  (global/strict! :gcp.vertexai.v1.api/RetrievalMetadata arg)
  (let [builder (RetrievalMetadata/newBuilder)]
    (some->> (:googleSearchDynamicRetrievalScore arg) (.setGoogleSearchDynamicRetrievalScore builder))
    (.build builder)))

(defn to-edn [^RetrievalMetadata arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/RetrievalMetadata %)]}
  {:googleSearchDynamicRetrievalScore (.getGoogleSearchDynamicRetrievalScore arg)})

(def schema
  [:map
   {:doc              "Metadata related to retrieval in the grounding flow."
    :class            'com.google.cloud.vertexai.api.RetrievalMetadata
    :protobuf/type    "google.cloud.vertexai.v1.RetrievalMetadata"}
   [:googleSearchDynamicRetrievalScore {:optional true} :float]])

(global/register-schema! :gcp.vertexai.v1.api/RetrievalMetadata schema)
