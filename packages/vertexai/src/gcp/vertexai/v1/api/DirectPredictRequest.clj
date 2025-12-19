(ns gcp.vertexai.v1.api.DirectPredictRequest
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.Tensor :as Tensor])
  (:import (com.google.cloud.vertexai.api DirectPredictRequest)))

(defn ^DirectPredictRequest from-edn
  [{:keys [endpoint inputs parameters] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/DirectPredictRequest arg)
  (let [builder (DirectPredictRequest/newBuilder)]
    (some->> endpoint (.setEndpoint builder))
    (some->> inputs (map Tensor/from-edn) (.addAllInputs builder))
    (some->> parameters Tensor/from-edn (.setParameters builder))
    (.build builder)))

(defn to-edn [^DirectPredictRequest arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/DirectPredictRequest %)]}
  (cond-> {}
          (not (empty? (.getEndpoint arg)))
          (assoc :endpoint (.getEndpoint arg))
          (pos? (.getInputsCount arg))
          (assoc :inputs (mapv Tensor/to-edn (.getInputsList arg)))
          (.hasParameters arg)
          (assoc :parameters (Tensor/to-edn (.getParameters arg)))))

(def schema
  [:map
   {:doc              "Request message for [DirectPredictionService.DirectPredict]"
    :class            'com.google.cloud.vertexai.api.DirectPredictRequest
    :protobuf/type    "google.cloud.vertexai.v1.DirectPredictRequest"}
   [:endpoint {:optional true} :string]
   [:inputs {:optional true} [:sequential :gcp.vertexai.v1.api/Tensor]]
   [:parameters {:optional true} :gcp.vertexai.v1.api/Tensor]])

(global/register-schema! :gcp.vertexai.v1.api/DirectPredictRequest schema)
