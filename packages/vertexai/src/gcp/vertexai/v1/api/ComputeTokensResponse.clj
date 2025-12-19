(ns gcp.vertexai.v1.api.ComputeTokensResponse
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.TokensInfo :as TokensInfo])
  (:import (com.google.cloud.vertexai.api ComputeTokensResponse)))

(defn ^ComputeTokensResponse from-edn
  [{:keys [tokensInfo] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/ComputeTokensResponse arg)
  (let [builder (ComputeTokensResponse/newBuilder)]
    (some->> tokensInfo (map TokensInfo/from-edn) (.addAllTokensInfo builder))
    (.build builder)))

(defn to-edn [^ComputeTokensResponse arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/ComputeTokensResponse %)]}
  (cond-> {}
          (pos? (.getTokensInfoCount arg))
          (assoc :tokensInfo (mapv TokensInfo/to-edn (.getTokensInfoList arg)))))

(def schema
  [:map
   {:doc              "Response message for [PredictionService.ComputeTokens]."}
   [:tokensInfo {:optional true} [:sequential :gcp.vertexai.v1.api/TokensInfo]]])

(global/register-schema! :gcp.vertexai.v1.api/ComputeTokensResponse schema)
