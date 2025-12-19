(ns gcp.vertexai.v1.api.StreamDirectRawPredictRequest
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import (com.google.cloud.vertexai.api StreamDirectRawPredictRequest)))

(defn ^StreamDirectRawPredictRequest from-edn
  [{:keys [endpoint methodName input] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/StreamDirectRawPredictRequest arg)
  (let [builder (StreamDirectRawPredictRequest/newBuilder)]
    (some->> endpoint (.setEndpoint builder))
    (some->> methodName (.setMethodName builder))
    (some->> input protobuf/bytestring-from-edn (.setInput builder))
    (.build builder)))

(defn to-edn [^StreamDirectRawPredictRequest arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/StreamDirectRawPredictRequest %)]}
  (cond-> {}
          (not (empty? (.getEndpoint arg)))
          (assoc :endpoint (.getEndpoint arg))
          (not (empty? (.getMethodName arg)))
          (assoc :methodName (.getMethodName arg))
          (not (.isEmpty (.getInput arg)))
          (assoc :input (protobuf/bytestring-to-edn (.getInput arg)))))

(def schema
  [:map
   {:doc              "Request message for [DirectPredictionService.StreamDirectRawPredict]"
    :class            'com.google.cloud.vertexai.api.StreamDirectRawPredictRequest
    :protobuf/type    "google.cloud.vertexai.v1.StreamDirectRawPredictRequest"}
   [:endpoint {:optional true} :string]
   [:methodName {:optional true} :string]
   [:input {:optional true} :gcp.protobuf/ByteString]])

(global/register-schema! :gcp.vertexai.v1.api/StreamDirectRawPredictRequest schema)
