(ns gcp.vertexai.v1.api.DirectRawPredictResponse
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import (com.google.cloud.vertexai.api DirectRawPredictResponse)))

(defn ^DirectRawPredictResponse from-edn
  [{:keys [output] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/DirectRawPredictResponse arg)
  (let [builder (DirectRawPredictResponse/newBuilder)]
    (some->> output protobuf/bytestring-from-edn (.setOutput builder))
    (.build builder)))

(defn to-edn [^DirectRawPredictResponse arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/DirectRawPredictResponse %)]}
  (cond-> {}
          (not (.isEmpty (.getOutput arg)))
          (assoc :output (protobuf/bytestring-to-edn (.getOutput arg)))))

(def schema
  [:map
   {:doc              "Response message for [DirectPredictionService.DirectRawPredict]"
    :class            'com.google.cloud.vertexai.api.DirectRawPredictResponse
    :protobuf/type    "google.cloud.vertexai.v1.DirectRawPredictResponse"}
   [:output {:optional true} :gcp.protobuf/ByteString]])

(global/register-schema! :gcp.vertexai.v1.api/DirectRawPredictResponse schema)
