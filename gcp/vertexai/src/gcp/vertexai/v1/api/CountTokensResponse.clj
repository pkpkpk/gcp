(ns gcp.vertexai.v1.api.CountTokensResponse
  (:import (com.google.cloud.vertexai.api CountTokensResponse)))

(defn ^CountTokensResponse from-edn
  [{:keys [totalTokens totalBillableCharacters] :as arg}]
  (gcp.global/strict! :vertexai.api/CountTokensResponse arg)
  (let [builder (CountTokensResponse/newBuilder)]
    (.setTotalTokens builder totalTokens)
    (.setTotalBillableCharacters builder totalBillableCharacters)
    (.build builder)))

(defn to-edn [^CountTokensResponse arg]
  {:post [(gcp.global/strict! :vertexai.api/CountTokensResponse %)]}
  {:totalTokens (.getTotalTokens arg)
   :totalBillableCharacters (.getTotalBillableCharacters arg)})