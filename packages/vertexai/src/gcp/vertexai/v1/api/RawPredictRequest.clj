(ns gcp.vertexai.v1.api.RawPredictRequest
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import (com.google.cloud.vertexai.api RawPredictRequest)
           (com.google.api HttpBody)))

(defn ^HttpBody HttpBody-from-edn [arg]
  (let [builder (HttpBody/newBuilder)]
    (some->> (:contentType arg) (.setContentType builder))
    (some->> (:data arg) protobuf/bytestring-from-edn (.setData builder))
    (some->> (:extensions arg) (map protobuf/value-from-edn) (into-array com.google.protobuf.Any) (.addAllExtensions builder)) ;; Assuming Any is wrapped in Value for simplicity, but HttpBody uses Any. This might be tricky.
    ;; Actually, standard HttpBody usually just takes bytes.
    (.build builder)))

(defn HttpBody-to-edn [^HttpBody arg]
  {:contentType (.getContentType arg)
   :data (protobuf/bytestring-to-edn (.getData arg))})

(defn ^RawPredictRequest from-edn
  [{:keys [endpoint httpBody] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/RawPredictRequest arg)
  (let [builder (RawPredictRequest/newBuilder)]
    (some->> endpoint (.setEndpoint builder))
    (some->> httpBody HttpBody-from-edn (.setHttpBody builder))
    (.build builder)))

(defn to-edn [^RawPredictRequest arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/RawPredictRequest %)]}
  (cond-> {}
          (not (empty? (.getEndpoint arg)))
          (assoc :endpoint (.getEndpoint arg))
          (.hasHttpBody arg)
          (assoc :httpBody (HttpBody-to-edn (.getHttpBody arg)))))

(def schema
  [:map
   {:doc              "Request message for [PredictionService.RawPredict]"
    :class            'com.google.cloud.vertexai.api.RawPredictRequest
    :protobuf/type    "google.cloud.vertexai.v1.RawPredictRequest"}
   [:endpoint {:optional true} :string]
   [:httpBody {:optional true} [:map
                                [:contentType {:optional true} :string]
                                [:data {:optional true} :gcp.protobuf/ByteString]]]])

(global/register-schema! :gcp.vertexai.v1.api/RawPredictRequest schema)
