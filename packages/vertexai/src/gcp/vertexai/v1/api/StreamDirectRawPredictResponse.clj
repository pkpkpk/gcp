(ns gcp.vertexai.v1.api.StreamDirectRawPredictResponse
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import (com.google.cloud.vertexai.api StreamDirectRawPredictResponse)))

(defn ^StreamDirectRawPredictResponse from-edn
  [{:keys [output] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/StreamDirectRawPredictResponse arg)
  (let [builder (StreamDirectRawPredictResponse/newBuilder)]
    (some->> output protobuf/bytestring-from-edn (.setOutput builder))
    (.build builder)))

(defn to-edn [^StreamDirectRawPredictResponse arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/StreamDirectRawPredictResponse %)]}
  (cond-> {}
          (not (.isEmpty (.getOutput arg)))
          (assoc :output (protobuf/bytestring-to-edn (.getOutput arg)))))

(def schema
  [:map
   {:doc              "Response message for [DirectPredictionService.StreamDirectRawPredict]"
    :class            'com.google.cloud.vertexai.api.StreamDirectRawPredictResponse
    :protobuf/type    "google.cloud.vertexai.v1.StreamDirectRawPredictResponse"}
   [:output {:optional true} :gcp.protobuf/ByteString]])

(global/register-schema! :gcp.vertexai.v1.api/StreamDirectRawPredictResponse schema)
