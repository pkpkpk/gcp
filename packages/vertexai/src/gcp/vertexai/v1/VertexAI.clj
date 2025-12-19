(ns gcp.vertexai.v1.VertexAI
  (:require [gcp.auth]
            [gcp.global :as g])
  (:import [com.google.cloud.vertexai VertexAI
                                      VertexAI$Builder
                                      Transport]))

(defn ^VertexAI from-edn
  [{:keys [apiEndpoint customHeaders projectId location
           credentials llmClientSupplier predictionClientSupplier scopes transport]
    :as arg}]
  (g/strict! :gcp.vertexai.v1/VertexAI arg)
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

(def schema
  [:maybe
   {:doc      "This class holds default settings and credentials to make Vertex AI API calls."
    :class    'com.google.cloud.vertexai.VertexAI
    :from-edn 'gcp.vertexai.v1.VertexAI/from-edn}
   [:map {:closed true}
    [:apiEndpoint {:optional true} :string]
    [:credentials {:optional true} :gcp.auth/Credentials]
    [:customHeaders {:optional true} [:map-of :string :string]]
    [:llmClientSupplier {:optional true} any?]
    [:location {:optional true} :string]
    [:predictionClientSupplier {:optional true} any?]
    [:projectId {:optional true} :string]
    [:scopes {:optional true} [:sequential :string]]
    [:transport {:optional true}
     [:or
      [:= {:doc "When used, the clients will send gRPC to the backing service. This is usually more efficient and is the default transport."} "GRPC"]
      [:= {:doc "When used, the clients will send REST requests to the backing service."} "REST"]]]]])

(g/register-schema! :gcp.vertexai.v1/VertexAI schema)
