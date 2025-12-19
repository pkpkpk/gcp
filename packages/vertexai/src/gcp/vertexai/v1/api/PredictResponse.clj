(ns gcp.vertexai.v1.api.PredictResponse
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import (com.google.cloud.vertexai.api PredictResponse)))

(defn ^PredictResponse from-edn
  [{:keys [predictions deployedModelId model modelVersionId modelDisplayName metadata] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/PredictResponse arg)
  (let [builder (PredictResponse/newBuilder)]
    (some->> predictions (map protobuf/value-from-edn) (.addAllPredictions builder))
    (some->> deployedModelId (.setDeployedModelId builder))
    (some->> model (.setModel builder))
    (some->> modelVersionId (.setModelVersionId builder))
    (some->> modelDisplayName (.setModelDisplayName builder))
    (some->> metadata protobuf/value-from-edn (.setMetadata builder))
    (.build builder)))

(defn to-edn [^PredictResponse arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/PredictResponse %)]}
  (cond-> {}
          (pos? (.getPredictionsCount arg))
          (assoc :predictions (mapv protobuf/value-to-edn (.getPredictionsList arg)))
          (not (empty? (.getDeployedModelId arg)))
          (assoc :deployedModelId (.getDeployedModelId arg))
          (not (empty? (.getModel arg)))
          (assoc :model (.getModel arg))
          (not (empty? (.getModelVersionId arg)))
          (assoc :modelVersionId (.getModelVersionId arg))
          (not (empty? (.getModelDisplayName arg)))
          (assoc :modelDisplayName (.getModelDisplayName arg))
          (.hasMetadata arg)
          (assoc :metadata (protobuf/value-to-edn (.getMetadata arg)))))

(def schema
  [:map
   {:doc              "Response message for [PredictionService.Predict]"
    :class            'com.google.cloud.vertexai.api.PredictResponse
    :protobuf/type    "google.cloud.vertexai.v1.PredictResponse"}
   [:predictions {:optional true} [:sequential :gcp.protobuf/Value]]
   [:deployedModelId {:optional true} :string]
   [:model {:optional true} :string]
   [:modelVersionId {:optional true} :string]
   [:modelDisplayName {:optional true} :string]
   [:metadata {:optional true} :gcp.protobuf/Value]])

(global/register-schema! :gcp.vertexai.v1.api/PredictResponse schema)
