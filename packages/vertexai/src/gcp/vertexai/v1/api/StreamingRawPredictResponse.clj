(ns gcp.vertexai.v1.api.StreamingRawPredictResponse
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import (com.google.cloud.vertexai.api StreamingRawPredictResponse)))

(defn ^StreamingRawPredictResponse from-edn
  [{:keys [output] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/StreamingRawPredictResponse arg)
  (let [builder (StreamingRawPredictResponse/newBuilder)]
    (some->> output protobuf/bytestring-from-edn (.setOutput builder))
    (.build builder)))

(defn to-edn [^StreamingRawPredictResponse arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/StreamingRawPredictResponse %)]}
  (cond-> {}
          (not (.isEmpty (.getOutput arg)))
          (assoc :output (protobuf/bytestring-to-edn (.getOutput arg)))))

(def schema
  [:map
   {:doc              "Response message for [DirectPredictionService.StreamingRawPredict]"
    :class            'com.google.cloud.vertexai.api.StreamingRawPredictResponse
    :protobuf/type    "google.cloud.vertexai.v1.StreamingRawPredictResponse"}
   [:output {:optional true} :gcp.protobuf/ByteString]])

(global/register-schema! :gcp.vertexai.v1.api/StreamingRawPredictResponse schema)
