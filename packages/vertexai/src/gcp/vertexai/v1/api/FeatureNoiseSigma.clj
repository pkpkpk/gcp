(ns gcp.vertexai.v1.api.FeatureNoiseSigma
  (:require [gcp.global :as global])
  (:import (com.google.cloud.vertexai.api FeatureNoiseSigma FeatureNoiseSigma$NoiseSigmaForFeature)))

(defn ^FeatureNoiseSigma$NoiseSigmaForFeature NoiseSigmaForFeature-from-edn [arg]
  (let [builder (FeatureNoiseSigma$NoiseSigmaForFeature/newBuilder)]
    (some->> (:name arg) (.setName builder))
    (some->> (:sigma arg) (.setSigma builder))
    (.build builder)))

(defn NoiseSigmaForFeature-to-edn [^FeatureNoiseSigma$NoiseSigmaForFeature arg]
  (cond-> {}
          (not (empty? (.getName arg)))
          (assoc :name (.getName arg))
          (.hasSigma arg)
          (assoc :sigma (.getSigma arg))))

(defn ^FeatureNoiseSigma from-edn
  [{:keys [noiseSigma] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/FeatureNoiseSigma arg)
  (let [builder (FeatureNoiseSigma/newBuilder)]
    (some->> noiseSigma (map NoiseSigmaForFeature-from-edn) (.addAllNoiseSigma builder))
    (.build builder)))

(defn to-edn [^FeatureNoiseSigma arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/FeatureNoiseSigma %)]}
  (cond-> {}
          (pos? (.getNoiseSigmaCount arg))
          (assoc :noiseSigma (mapv NoiseSigmaForFeature-to-edn (.getNoiseSigmaList arg)))))

(def schema
  [:map
   {:doc              "Noise sigma for a feature. This class is part of the [Explanation API](https://cloud.google.com/vertex-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.FeatureNoiseSigma) of Vertex AI. The class is annotated with Protobuf message type for interoperability."
    :class            'com.google.cloud.vertexai.api.FeatureNoiseSigma
    :protobuf/type    "google.cloud.vertexai.v1.FeatureNoiseSigma"}
   [:noiseSigma {:optional true} [:sequential [:map
                                               [:name {:optional true} :string]
                                               [:sigma {:optional true} :float]]]]])

(global/register-schema! :gcp.vertexai.v1.api/FeatureNoiseSigma schema)
