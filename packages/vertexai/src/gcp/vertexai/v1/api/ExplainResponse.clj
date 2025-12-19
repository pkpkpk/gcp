(ns gcp.vertexai.v1.api.ExplainResponse
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf]
            [gcp.vertexai.v1.api.Explanation :as Explanation])
  (:import (com.google.cloud.vertexai.api ExplainResponse)))

(defn ^ExplainResponse from-edn
  [{:keys [explanations deployedModelId predictions] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/ExplainResponse arg)
  (let [builder (ExplainResponse/newBuilder)]
    (some->> explanations (map Explanation/from-edn) (.addAllExplanations builder))
    (some->> deployedModelId (.setDeployedModelId builder))
    (some->> predictions (map protobuf/value-from-edn) (.addAllPredictions builder))
    (.build builder)))

(defn to-edn [^ExplainResponse arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/ExplainResponse %)]}
  (cond-> {}
          (pos? (.getExplanationsCount arg))
          (assoc :explanations (mapv Explanation/to-edn (.getExplanationsList arg)))
          (not (empty? (.getDeployedModelId arg)))
          (assoc :deployedModelId (.getDeployedModelId arg))
          (pos? (.getPredictionsCount arg))
          (assoc :predictions (mapv protobuf/value-to-edn (.getPredictionsList arg)))))

(def schema
  [:map
   {:doc              "Response message for [PredictionService.Explain]"
    :class            'com.google.cloud.vertexai.api.ExplainResponse
    :protobuf/type    "google.cloud.vertexai.v1.ExplainResponse"}
   [:explanations {:optional true} [:sequential :gcp.vertexai.v1.api/Explanation]]
   [:deployedModelId {:optional true} :string]
   [:predictions {:optional true} [:sequential :gcp.protobuf/Value]]])

(global/register-schema! :gcp.vertexai.v1.api/ExplainResponse schema)
