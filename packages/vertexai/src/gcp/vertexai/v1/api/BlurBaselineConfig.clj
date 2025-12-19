(ns gcp.vertexai.v1.api.BlurBaselineConfig
  (:require [gcp.global :as global])
  (:import (com.google.cloud.vertexai.api BlurBaselineConfig)))

(defn ^BlurBaselineConfig from-edn
  [{:keys [maxBlurSigma] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/BlurBaselineConfig arg)
  (let [builder (BlurBaselineConfig/newBuilder)]
    (some->> maxBlurSigma (.setMaxBlurSigma builder))
    (.build builder)))

(defn to-edn [^BlurBaselineConfig arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/BlurBaselineConfig %)]}
  (cond-> {}
          (.hasMaxBlurSigma arg)
          (assoc :maxBlurSigma (.getMaxBlurSigma arg))))

(def schema
  [:map
   {:doc              "Config for blur baseline."
    :class            'com.google.cloud.vertexai.api.BlurBaselineConfig
    :protobuf/type    "google.cloud.vertexai.v1.BlurBaselineConfig"}
   [:maxBlurSigma {:optional true} :float]])

(global/register-schema! :gcp.vertexai.v1.api/BlurBaselineConfig schema)
