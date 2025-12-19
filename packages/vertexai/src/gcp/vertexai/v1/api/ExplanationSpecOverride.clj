(ns gcp.vertexai.v1.api.ExplanationSpecOverride
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.ExplanationMetadataOverride :as ExplanationMetadataOverride]
            [gcp.vertexai.v1.api.ExplanationParameters :as ExplanationParameters])
  (:import (com.google.cloud.vertexai.api ExplanationSpecOverride)))

(defn ^ExplanationSpecOverride from-edn
  [{:keys [parameters metadata] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/ExplanationSpecOverride arg)
  (let [builder (ExplanationSpecOverride/newBuilder)]
    (some->> parameters ExplanationParameters/from-edn (.setParameters builder))
    (some->> metadata ExplanationMetadataOverride/from-edn (.setMetadata builder))
    (.build builder)))

(defn to-edn [^ExplanationSpecOverride arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/ExplanationSpecOverride %)]}
  (cond-> {}
          (.hasParameters arg)
          (assoc :parameters (ExplanationParameters/to-edn (.getParameters arg)))
          (.hasMetadata arg)
          (assoc :metadata (ExplanationMetadataOverride/to-edn (.getMetadata arg)))))

(def schema
  [:map
   {:doc              "The explanationSpec of the Model can be overridden by this message. This class is part of the [Explanation API](https://cloud.google.com/vertex-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.ExplanationSpecOverride) of Vertex AI. The class is annotated with Protobuf message type for interoperability."
    :class            'com.google.cloud.vertexai.api.ExplanationSpecOverride
    :protobuf/type    "google.cloud.vertexai.v1.ExplanationSpecOverride"}
   [:parameters {:optional true} :gcp.vertexai.v1.api/ExplanationParameters]
   [:metadata {:optional true} :gcp.vertexai.v1.api/ExplanationMetadataOverride]])

(global/register-schema! :gcp.vertexai.v1.api/ExplanationSpecOverride schema)
