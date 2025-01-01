(ns gcp.vertexai.v1.api.CountTokensResponse
  (:import (com.google.cloud.vertexai.api CountTokensResponse)))

(def schema
  [:map
   [:totalTokens :int]
   [:totalBillableCharacters :int]])

(defn ^CountTokensResponse from-edn
  [{:keys [totalTokens totalBillableCharacters] :as arg}]
  (gcp.global/strict! schema arg)
  (let [builder (CountTokensResponse/newBuilder)]
    (.setTotalTokens builder totalTokens)
    (.setTotalBillableCharacters builder totalBillableCharacters)
    (.build builder)))

(defn to-edn [^CountTokensResponse arg]
  {:post [(gcp.global/strict! schema %)]}
  {:totalTokens (.getTotalTokens arg)
   :totalBillableCharacters (.getTotalBillableCharacters arg)})