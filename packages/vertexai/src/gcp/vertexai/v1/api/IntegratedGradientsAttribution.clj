(ns gcp.vertexai.v1.api.IntegratedGradientsAttribution
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.SmoothGradConfig :as SmoothGradConfig]
            [gcp.vertexai.v1.api.BlurBaselineConfig :as BlurBaselineConfig])
  (:import (com.google.cloud.vertexai.api IntegratedGradientsAttribution)))

(defn ^IntegratedGradientsAttribution from-edn
  [{:keys [stepCount smoothGradConfig blurBaselineConfig] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/IntegratedGradientsAttribution arg)
  (let [builder (IntegratedGradientsAttribution/newBuilder)]
    (some->> stepCount (int) (.setStepCount builder))
    (some->> smoothGradConfig SmoothGradConfig/from-edn (.setSmoothGradConfig builder))
    (some->> blurBaselineConfig BlurBaselineConfig/from-edn (.setBlurBaselineConfig builder))
    (.build builder)))

(defn to-edn [^IntegratedGradientsAttribution arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/IntegratedGradientsAttribution %)]}
  (cond-> {}
          (pos? (.getStepCount arg))
          (assoc :stepCount (.getStepCount arg))
          (.hasSmoothGradConfig arg)
          (assoc :smoothGradConfig (SmoothGradConfig/to-edn (.getSmoothGradConfig arg)))
          (.hasBlurBaselineConfig arg)
          (assoc :blurBaselineConfig (BlurBaselineConfig/to-edn (.getBlurBaselineConfig arg)))))

(def schema
  [:map
   {:doc              "Config for Integrated Gradients attribution method. This class is part of the [Explanation API](https://cloud.google.com/vertex-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.IntegratedGradientsAttribution) of Vertex AI. The class is annotated with Protobuf message type for interoperability."
    :class            'com.google.cloud.vertexai.api.IntegratedGradientsAttribution
    :protobuf/type    "google.cloud.vertexai.v1.IntegratedGradientsAttribution"}
   [:stepCount {:optional true} :int]
   [:smoothGradConfig {:optional true} :gcp.vertexai.v1.api/SmoothGradConfig]
   [:blurBaselineConfig {:optional true} :gcp.vertexai.v1.api/BlurBaselineConfig]])

(global/register-schema! :gcp.vertexai.v1.api/IntegratedGradientsAttribution schema)
