(ns gcp.vertexai.v1.VertexAI
  (:require [gcp.global :as g])
  (:import [com.google.cloud.vertexai VertexAI
                                      VertexAI$Builder
                                      Transport]))

(defn ^VertexAI from-edn
  [{:keys [apiEndpoint customHeaders projectId location
           credentials llmClientSupplier predictionClientSupplier scopes transport]
    :as arg}]
  (g/strict! :gcp/vertexai.VertexAI arg)
  (let [builder (VertexAI$Builder.)]
    (some->> apiEndpoint (.setApiEndpoint builder))
    (some->> credentials (.setCredentials builder))
    (some->> customHeaders (.setCustomHeaders builder))
    (some->> projectId (.setProjectId builder))
    (some->> llmClientSupplier (.setLlmClientSupplier builder))
    (some->> location (.setLocation builder))
    (some->> predictionClientSupplier (.setPredictionClientSupplier builder))
    (some->> scopes into-array (.setScopes builder))
    (some->> transport Transport/valueOf (.setTransport builder))
    (.build builder)))

