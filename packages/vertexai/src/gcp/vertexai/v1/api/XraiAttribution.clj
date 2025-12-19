(ns gcp.vertexai.v1.api.XraiAttribution
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.SmoothGradConfig :as SmoothGradConfig]
            [gcp.vertexai.v1.api.BlurBaselineConfig :as BlurBaselineConfig])
  (:import (com.google.cloud.vertexai.api XraiAttribution)))

(defn ^XraiAttribution from-edn
  [{:keys [stepCount smoothGradConfig blurBaselineConfig] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/XraiAttribution arg)
  (let [builder (XraiAttribution/newBuilder)]
    (some->> stepCount (int) (.setStepCount builder))
    (some->> smoothGradConfig SmoothGradConfig/from-edn (.setSmoothGradConfig builder))
    (some->> blurBaselineConfig BlurBaselineConfig/from-edn (.setBlurBaselineConfig builder))
    (.build builder)))

(defn to-edn [^XraiAttribution arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/XraiAttribution %)]}
  (cond-> {}
          (pos? (.getStepCount arg))
          (assoc :stepCount (.getStepCount arg))
          (.hasSmoothGradConfig arg)
          (assoc :smoothGradConfig (SmoothGradConfig/to-edn (.getSmoothGradConfig arg)))
          (.hasBlurBaselineConfig arg)
          (assoc :blurBaselineConfig (BlurBaselineConfig/to-edn (.getBlurBaselineConfig arg)))))

(def schema
  [:map
   {:doc              "Config for XRAI attribution method. This class is part of the [Explanation API](https://cloud.google.com/vertex-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.XraiAttribution) of Vertex AI. The class is annotated with Protobuf message type for interoperability."
    :class            'com.google.cloud.vertexai.api.XraiAttribution
    :protobuf/type    "google.cloud.vertexai.v1.XraiAttribution"}
   [:stepCount {:optional true} :int]
   [:smoothGradConfig {:optional true} :gcp.vertexai.v1.api/SmoothGradConfig]
   [:blurBaselineConfig {:optional true} :gcp.vertexai.v1.api/BlurBaselineConfig]])

(global/register-schema! :gcp.vertexai.v1.api/XraiAttribution schema)
