(ns gcp.vertexai.v1.api.StreamingRawPredictRequest
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import (com.google.cloud.vertexai.api StreamingRawPredictRequest)))

(defn ^StreamingRawPredictRequest from-edn
  [{:keys [endpoint methodName input] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/StreamingRawPredictRequest arg)
  (let [builder (StreamingRawPredictRequest/newBuilder)]
    (some->> endpoint (.setEndpoint builder))
    (some->> methodName (.setMethodName builder))
    (some->> input protobuf/bytestring-from-edn (.setInput builder))
    (.build builder)))

(defn to-edn [^StreamingRawPredictRequest arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/StreamingRawPredictRequest %)]}
  (cond-> {}
          (not (empty? (.getEndpoint arg)))
          (assoc :endpoint (.getEndpoint arg))
          (not (empty? (.getMethodName arg)))
          (assoc :methodName (.getMethodName arg))
          (not (.isEmpty (.getInput arg)))
          (assoc :input (protobuf/bytestring-to-edn (.getInput arg)))))

(def schema
  [:map
   {:doc              "Request message for [DirectPredictionService.StreamingRawPredict]"
    :class            'com.google.cloud.vertexai.api.StreamingRawPredictRequest
    :protobuf/type    "google.cloud.vertexai.v1.StreamingRawPredictRequest"}
   [:endpoint {:optional true} :string]
   [:methodName {:optional true} :string]
   [:input {:optional true} :gcp.protobuf/ByteString]])

(global/register-schema! :gcp.vertexai.v1.api/StreamingRawPredictRequest schema)
