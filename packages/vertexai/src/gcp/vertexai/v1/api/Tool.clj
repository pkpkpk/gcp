(ns gcp.vertexai.v1.api.Tool
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.EnterpriseWebSearch :as EnterpriseWebSearch]
            [gcp.vertexai.v1.api.FunctionDeclaration :as FunctionDeclaration]
            [gcp.vertexai.v1.api.GoogleSearchRetrieval :as GoogleSearchRetrieval]
            [gcp.vertexai.v1.api.Retrieval :as Retrieval])
  (:import (com.google.cloud.vertexai.api Tool Tool$GoogleSearch Tool$CodeExecution)))

(defn ^Tool$GoogleSearch Tool$GoogleSearch:from-edn [arg] (Tool$GoogleSearch/getDefaultInstance))
(defn Tool$GoogleSearch:to-edn [^Tool$GoogleSearch arg] {})

(defn ^Tool$CodeExecution Tool$CodeExecution:from-edn [arg] (Tool$CodeExecution/getDefaultInstance))
(defn Tool$CodeExecution:to-edn [^Tool$CodeExecution arg] {})

(defn ^Tool from-edn
  [{:keys [codeExecution
           enterpriseWebSearch
           functionDeclarations
           retrieval
           googleSearch
           googleSearchRetrieval] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/Tool arg)
  (let [builder (Tool/newBuilder)]
    (some->> codeExecution Tool$CodeExecution:from-edn (.setCodeExecution builder))
    (some->> enterpriseWebSearch EnterpriseWebSearch/from-edn (.setEnterpriseWebSearch builder))
    (some->> (not-empty functionDeclarations) (map FunctionDeclaration/from-edn) (.addAllFunctionDeclarations builder))
    (some->> googleSearch Tool$GoogleSearch:from-edn (.setGoogleSearch builder))
    (some->> googleSearchRetrieval GoogleSearchRetrieval/from-edn (.setGoogleSearchRetrieval builder))
    (some->> retrieval Retrieval/from-edn (.setRetrieval builder))
    (.build builder)))

(defn to-edn [^Tool tool]
  {:post [(global/strict! :gcp.vertexai.v1.api/Tool %)]}
  (cond-> {}
          (.hasGoogleSearchRetrieval tool)
          (assoc :googleSearchRetrieval (GoogleSearchRetrieval/to-edn (.getGoogleSearchRetrieval tool)))
          (.hasRetrieval tool)
          (assoc :retrieval (Retrieval/to-edn (.getRetrieval tool)))
          (pos? (.getFunctionDeclarationsCount tool))
          (assoc :functionDeclarations (mapv FunctionDeclaration/to-edn (.getFunctionDeclarationsList tool)))
          (.hasGoogleSearch tool)
          (assoc :googleSearch (Tool$GoogleSearch:to-edn (.getGoogleSearch tool)))
          (.hasCodeExecution tool)
          (assoc :codeExecution (Tool$CodeExecution:to-edn (.getCodeExecution tool)))
          (.hasEnterpriseWebSearch tool)
          (assoc :enterpriseWebSearch (EnterpriseWebSearch/to-edn (.getEnterpriseWebSearch tool)))))

(def schema
  [:map
   {:class         'com.google.cloud.vertexai.api.Tool
    :from-edn      'gcp.vertexai.v1.api.Tool/from-edn
    :to-edn        'gcp.vertexai.v1.api.Tool/to-edn
    :protobuf/type "google.cloud.vertexai.v1.Tool"}
   [:codeExecution {:optional true} :any]
   [:enterpriseWebSearch {:optional true} :gcp.vertexai.v1.api/EnterpriseWebSearch]
   [:functionDeclarations {:optional true} [:sequential :gcp.vertexai.v1.api/FunctionDeclaration]]
   [:googleSearchRetrieval {:optional true} :gcp.vertexai.v1.api/GoogleSearchRetrieval]
   [:googleSearch {:optional true} :any]
   [:retrieval {:optional true} :gcp.vertexai.v1.api/Retrieval]])

(global/register-schema! :gcp.vertexai.v1.api/Tool schema)
