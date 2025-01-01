(ns gcp.vertexai.v1.VertexAI
  (:require [gcp.auth :as auth])
  (:import [com.google.cloud.vertexai VertexAI
                                      VertexAI$Builder
                                      Transport]))

(def ^{:class VertexAI} schema
  [:map {:closed true}
   [:apiEndpoint {:optional true} :string]
   [:credentials {:optional true} auth/credentials-schema]
   [:customHeaders {:optional true} [:map-of :string :string]]
   [:llmClientSupplier {:optional true} :any]
   [:location {:optional true} :string]
   [:predictionClientSupplier {:optional true} :any]
   [:projectId {:optional true} :string]
   [:scopes {:optional true} [:sequential :string]]
   [:transport {:optional true}
    [:or
     [:=
      {:doc "When used, the clients will send gRPC to the backing service. This is usually more efficient and is the default transport."}
      "GRPC"]
     [:=
      {:doc "When used, the clients will send REST requests to the backing service."}
      "REST"]]]])

  (defn halt! [^VertexAI client] (.close client))

  (defn ^VertexAI from-edn
    [{:keys [apiEndpoint customHeaders projectId location
             credentials llmClientSupplier predictionClientSupplier scopes transport]}]
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

(defn to-edn [^VertexAI client] (throw (Exception. "unimplemented")))
