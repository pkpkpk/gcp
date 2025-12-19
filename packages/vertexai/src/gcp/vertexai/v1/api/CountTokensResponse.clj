(ns gcp.vertexai.v1.api.CountTokensResponse
  (:require [gcp.global :as global])
  (:import (com.google.cloud.vertexai.api CountTokensResponse)))

(defn ^CountTokensResponse from-edn
  [{:keys [totalTokens totalBillableCharacters] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/CountTokensResponse arg)
  (let [builder (CountTokensResponse/newBuilder)]
    (.setTotalTokens builder totalTokens)
    (.setTotalBillableCharacters builder totalBillableCharacters)
    (.build builder)))

(defn to-edn [^CountTokensResponse arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/CountTokensResponse %)]}
  {:totalTokens (.getTotalTokens arg)
   :totalBillableCharacters (.getTotalBillableCharacters arg)})

(def schema
  [:map
   {:class    'com.google.cloud.vertexai.api.CountTokensResponse
    :from-edn 'gcp.vertexai.v1.api.CountTokensResponse/from-edn
    :to-edn   'gcp.vertexai.v1.api.CountTokensResponse/to-edn}
   [:totalTokens :int]
   [:totalBillableCharacters :int]])

(global/register-schema! :gcp.vertexai.v1.api/CountTokensResponse schema)
