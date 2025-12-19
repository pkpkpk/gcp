(ns gcp.vertexai.v1.api.PredictRequest
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import (com.google.cloud.vertexai.api PredictRequest)))

(defn ^PredictRequest from-edn
  [{:keys [endpoint instances parameters] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/PredictRequest arg)
  (let [builder (PredictRequest/newBuilder)]
    (some->> endpoint (.setEndpoint builder))
    (some->> instances (map protobuf/value-from-edn) (.addAllInstances builder))
    (some->> parameters protobuf/value-from-edn (.setParameters builder))
    (.build builder)))

(defn to-edn [^PredictRequest arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/PredictRequest %)]}
  (cond-> {}
          (not (empty? (.getEndpoint arg)))
          (assoc :endpoint (.getEndpoint arg))
          (pos? (.getInstancesCount arg))
          (assoc :instances (mapv protobuf/value-to-edn (.getInstancesList arg)))
          (.hasParameters arg)
          (assoc :parameters (protobuf/value-to-edn (.getParameters arg)))))

(def schema
  [:map
   {:doc              "Request message for [PredictionService.Predict]"
    :class            'com.google.cloud.vertexai.api.PredictRequest
    :protobuf/type    "google.cloud.vertexai.v1.PredictRequest"}
   [:endpoint {:optional true} :string]
   [:instances {:optional true} [:sequential :gcp.protobuf/Value]]
   [:parameters {:optional true} :gcp.protobuf/Value]])

(global/register-schema! :gcp.vertexai.v1.api/PredictRequest schema)
