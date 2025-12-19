(ns gcp.vertexai.v1.api.SmoothGradConfig
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.FeatureNoiseSigma :as FeatureNoiseSigma])
  (:import (com.google.cloud.vertexai.api SmoothGradConfig)))

(defn ^SmoothGradConfig from-edn
  [{:keys [noiseSigma featureNoiseSigma noiseSigmaParam] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/SmoothGradConfig arg)
  (let [builder (SmoothGradConfig/newBuilder)]
    (if featureNoiseSigma
      (.setFeatureNoiseSigma builder (FeatureNoiseSigma/from-edn featureNoiseSigma))
      (when noiseSigma
        (.setNoiseSigma builder noiseSigma)))
    (.build builder)))

(defn to-edn [^SmoothGradConfig arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/SmoothGradConfig %)]}
  (cond-> {}
          (.hasFeatureNoiseSigma arg)
          (assoc :featureNoiseSigma (FeatureNoiseSigma/to-edn (.getFeatureNoiseSigma arg)))
          (.hasNoiseSigma arg)
          (assoc :noiseSigma (.getNoiseSigma arg))))

(def schema
  [:map
   {:doc              "Config for SmoothGrad method. This class is part of the [Explanation API](https://cloud.google.com/vertex-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.SmoothGradConfig) of Vertex AI. The class is annotated with Protobuf message type for interoperability."
    :class            'com.google.cloud.vertexai.api.SmoothGradConfig
    :protobuf/type    "google.cloud.vertexai.v1.SmoothGradConfig"}
   [:noiseSigma {:optional true} :float]
   [:featureNoiseSigma {:optional true} :gcp.vertexai.v1.api/FeatureNoiseSigma]])

(global/register-schema! :gcp.vertexai.v1.api/SmoothGradConfig schema)
