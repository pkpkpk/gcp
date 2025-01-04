(ns gcp.vertexai.v1.api.Tool
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.FunctionDeclaration :as FunctionDeclaration]
            [gcp.vertexai.v1.api.GoogleSearchRetrieval :as GoogleSearchRetrieval]
            [gcp.vertexai.v1.api.Retrieval :as Retrieval])
  (:import (com.google.cloud.vertexai.api Tool)))

(defn ^Tool from-edn
  [{:keys [functionDeclarations retrieval googleSearchRetrieval] :as arg}]
  (global/strict! :vertexai.api/Tool arg)
  (let [builder (Tool/newBuilder)]
    (some->> (not-empty functionDeclarations) (map FunctionDeclaration/from-edn) (.addAllFunctionDeclarations builder))
    (some->> googleSearchRetrieval GoogleSearchRetrieval/from-edn (.setGoogleSearchRetrieval builder))
    (some->> retrieval Retrieval/from-edn (.setRetrieval builder))
    (.build builder)))

(defn to-edn [^Tool tool]
  {:post [(global/strict! :vertexai.api/Tool %)]}
  (cond-> {}
          (.hasGoogleSearchRetrieval tool)
          (assoc :googleSearchRetrieval (GoogleSearchRetrieval/to-edn (.getGoogleSearchRetrieval tool)))
          (.hasRetrieval tool)
          (assoc :retrieval (Retrieval/to-edn (.getRetrieval tool)))
          (pos? (.getFunctionDeclarationsCount tool))
          (assoc :functionDeclarations (mapv FunctionDeclaration/to-edn (.getFunctionDeclarationsList tool)))))
