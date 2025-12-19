(ns gcp.vertexai.v1.api.ExplanationParameters
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.SampledShapleyAttribution :as SampledShapleyAttribution]
            [gcp.vertexai.v1.api.IntegratedGradientsAttribution :as IntegratedGradientsAttribution]
            [gcp.vertexai.v1.api.XraiAttribution :as XraiAttribution]
            [gcp.protobuf :as protobuf])
  (:import (com.google.cloud.vertexai.api ExplanationParameters)))

(defn ^ExplanationParameters from-edn
  [{:keys [sampledShapleyAttribution integratedGradientsAttribution xraiAttribution topK outputIndices] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/ExplanationParameters arg)
  (let [builder (ExplanationParameters/newBuilder)]
    (some->> sampledShapleyAttribution SampledShapleyAttribution/from-edn (.setSampledShapleyAttribution builder))
    (some->> integratedGradientsAttribution IntegratedGradientsAttribution/from-edn (.setIntegratedGradientsAttribution builder))
    (some->> xraiAttribution XraiAttribution/from-edn (.setXraiAttribution builder))
    (some->> topK (int) (.setTopK builder))
    (some->> outputIndices (map protobuf/value-from-edn) (.addAllOutputIndices builder))
    (.build builder)))

(defn to-edn [^ExplanationParameters arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/ExplanationParameters %)]}
  (cond-> {}
          (.hasSampledShapleyAttribution arg)
          (assoc :sampledShapleyAttribution (SampledShapleyAttribution/to-edn (.getSampledShapleyAttribution arg)))
          (.hasIntegratedGradientsAttribution arg)
          (assoc :integratedGradientsAttribution (IntegratedGradientsAttribution/to-edn (.getIntegratedGradientsAttribution arg)))
          (.hasXraiAttribution arg)
          (assoc :xraiAttribution (XraiAttribution/to-edn (.getXraiAttribution arg)))
          (.hasTopK arg)
          (assoc :topK (.getTopK arg))
          (pos? (.getOutputIndicesCount arg))
          (assoc :outputIndices (mapv protobuf/value-to-edn (.getOutputIndicesList arg)))))

(def schema
  [:map
   {:doc              "Parameters for explaining the predictions. This class is part of the [Explanation API](https://cloud.google.com/vertex-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.ExplanationParameters) of Vertex AI. The class is annotated with Protobuf message type for interoperability."
    :class            'com.google.cloud.vertexai.api.ExplanationParameters
    :protobuf/type    "google.cloud.vertexai.v1.ExplanationParameters"}
   [:sampledShapleyAttribution {:optional true} :gcp.vertexai.v1.api/SampledShapleyAttribution]
   [:integratedGradientsAttribution {:optional true} :gcp.vertexai.v1.api/IntegratedGradientsAttribution]
   [:xraiAttribution {:optional true} :gcp.vertexai.v1.api/XraiAttribution]
   [:topK {:optional true} :int]
   [:outputIndices {:optional true} [:sequential :gcp.protobuf/Value]]])

(global/register-schema! :gcp.vertexai.v1.api/ExplanationParameters schema)
