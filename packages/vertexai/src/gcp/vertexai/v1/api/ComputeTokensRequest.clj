(ns gcp.vertexai.v1.api.ComputeTokensRequest
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.Content :as Content]
            [gcp.protobuf :as protobuf])
  (:import (com.google.cloud.vertexai.api ComputeTokensRequest)))

(defn ^ComputeTokensRequest from-edn
  [{:keys [endpoint instances model contents] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/ComputeTokensRequest arg)
  (let [builder (ComputeTokensRequest/newBuilder)]
    (some->> endpoint (.setEndpoint builder))
    (some->> instances (map protobuf/value-from-edn) (.addAllInstances builder))
    (some->> model (.setModel builder))
    (some->> contents (map Content/from-edn) (.addAllContents builder))
    (.build builder)))

(defn to-edn [^ComputeTokensRequest arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/ComputeTokensRequest %)]}
  (cond-> {}
          (not (empty? (.getEndpoint arg)))
          (assoc :endpoint (.getEndpoint arg))
          (pos? (.getInstancesCount arg))
          (assoc :instances (mapv protobuf/value-to-edn (.getInstancesList arg)))
          (not (empty? (.getModel arg)))
          (assoc :model (.getModel arg))
          (pos? (.getContentsCount arg))
          (assoc :contents (mapv Content/to-edn (.getContentsList arg)))))

(def schema
  [:map
   {:doc              "Request message for [PredictionService.ComputeTokens]."}
   [:endpoint {:optional true} :string]
   [:instances {:optional true} [:sequential :gcp.protobuf/Value]]
   [:model {:optional true} :string]
   [:contents {:optional true} [:sequential :gcp.vertexai.v1.api/Content]]])

(global/register-schema! :gcp.vertexai.v1.api/ComputeTokensRequest schema)
