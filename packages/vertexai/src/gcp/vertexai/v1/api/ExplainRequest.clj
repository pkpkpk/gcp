(ns gcp.vertexai.v1.api.ExplainRequest
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf]
            [gcp.vertexai.v1.api.ExplanationSpecOverride :as ExplanationSpecOverride])
  (:import (com.google.cloud.vertexai.api ExplainRequest)))

(defn ^ExplainRequest from-edn
  [{:keys [endpoint instances parameters explanationSpecOverride deployedModelId] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/ExplainRequest arg)
  (let [builder (ExplainRequest/newBuilder)]
    (some->> endpoint (.setEndpoint builder))
    (some->> instances (map protobuf/value-from-edn) (.addAllInstances builder))
    (some->> parameters protobuf/value-from-edn (.setParameters builder))
    (some->> explanationSpecOverride ExplanationSpecOverride/from-edn (.setExplanationSpecOverride builder))
    (some->> deployedModelId (.setDeployedModelId builder))
    (.build builder)))

(defn to-edn [^ExplainRequest arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/ExplainRequest %)]}
  (cond-> {}
          (not (empty? (.getEndpoint arg)))
          (assoc :endpoint (.getEndpoint arg))
          (pos? (.getInstancesCount arg))
          (assoc :instances (mapv protobuf/value-to-edn (.getInstancesList arg)))
          (.hasParameters arg)
          (assoc :parameters (protobuf/value-to-edn (.getParameters arg)))
          (.hasExplanationSpecOverride arg)
          (assoc :explanationSpecOverride (ExplanationSpecOverride/to-edn (.getExplanationSpecOverride arg)))
          (not (empty? (.getDeployedModelId arg)))
          (assoc :deployedModelId (.getDeployedModelId arg))))

(def schema
  [:map
   {:doc              "Request message for [PredictionService.Explain]"
    :class            'com.google.cloud.vertexai.api.ExplainRequest
    :protobuf/type    "google.cloud.vertexai.v1.ExplainRequest"}
   [:endpoint {:optional true} :string]
   [:instances {:optional true} [:sequential :gcp.protobuf/Value]]
   [:parameters {:optional true} :gcp.protobuf/Value]
   [:explanationSpecOverride {:optional true} :gcp.vertexai.v1.api/ExplanationSpecOverride]
   [:deployedModelId {:optional true} :string]])

(global/register-schema! :gcp.vertexai.v1.api/ExplainRequest schema)
