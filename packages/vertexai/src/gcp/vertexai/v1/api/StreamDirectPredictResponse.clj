(ns gcp.vertexai.v1.api.StreamDirectPredictResponse
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.Tensor :as Tensor])
  (:import (com.google.cloud.vertexai.api StreamDirectPredictResponse)))

(defn ^StreamDirectPredictResponse from-edn
  [{:keys [outputs parameters] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/StreamDirectPredictResponse arg)
  (let [builder (StreamDirectPredictResponse/newBuilder)]
    (some->> outputs (map Tensor/from-edn) (.addAllOutputs builder))
    (some->> parameters Tensor/from-edn (.setParameters builder))
    (.build builder)))

(defn to-edn [^StreamDirectPredictResponse arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/StreamDirectPredictResponse %)]}
  (cond-> {}
          (pos? (.getOutputsCount arg))
          (assoc :outputs (mapv Tensor/to-edn (.getOutputsList arg)))
          (.hasParameters arg)
          (assoc :parameters (Tensor/to-edn (.getParameters arg)))))

(def schema
  [:map
   {:doc              "Response message for [DirectPredictionService.StreamDirectPredict]"
    :class            'com.google.cloud.vertexai.api.StreamDirectPredictResponse
    :protobuf/type    "google.cloud.vertexai.v1.StreamDirectPredictResponse"}
   [:outputs {:optional true} [:sequential :gcp.vertexai.v1.api/Tensor]]
   [:parameters {:optional true} :gcp.vertexai.v1.api/Tensor]])

(global/register-schema! :gcp.vertexai.v1.api/StreamDirectPredictResponse schema)
