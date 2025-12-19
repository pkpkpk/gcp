(ns gcp.vertexai.v1.api.SampledShapleyAttribution
  (:require [gcp.global :as global])
  (:import (com.google.cloud.vertexai.api SampledShapleyAttribution)))

(defn ^SampledShapleyAttribution from-edn
  [{:keys [pathCount] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/SampledShapleyAttribution arg)
  (let [builder (SampledShapleyAttribution/newBuilder)]
    (some->> pathCount (int) (.setPathCount builder))
    (.build builder)))

(defn to-edn [^SampledShapleyAttribution arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/SampledShapleyAttribution %)]}
  {:pathCount (.getPathCount arg)})

(def schema
  [:map
   {:doc              "Config for Sampled Shapley attribution method. This class is part of the [Explanation API](https://cloud.google.com/vertex-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.SampledShapleyAttribution) of Vertex AI. The class is annotated with Protobuf message type for interoperability."
    :class            'com.google.cloud.vertexai.api.SampledShapleyAttribution
    :protobuf/type    "google.cloud.vertexai.v1.SampledShapleyAttribution"}
   [:pathCount {:optional true} :int]])

(global/register-schema! :gcp.vertexai.v1.api/SampledShapleyAttribution schema)
