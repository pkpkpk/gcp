(ns gcp.vertexai.v1.api.Attribution
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import (com.google.cloud.vertexai.api Attribution)))

(defn ^Attribution from-edn
  [{:keys [baselineScore instanceOutputValue featureAttributions outputIndex outputDisplayName approximationError outputName] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/Attribution arg)
  (let [builder (Attribution/newBuilder)]
    (some->> baselineScore (.setBaselineScore builder))
    (some->> instanceOutputValue (.setInstanceOutputValue builder))
    (some->> featureAttributions protobuf/value-from-edn (.setFeatureAttributions builder))
    (some->> outputIndex (int) (.setOutputIndex builder))
    (some->> outputDisplayName (.setOutputDisplayName builder))
    (some->> approximationError (.setApproximationError builder))
    (some->> outputName (.setOutputName builder))
    (.build builder)))

(defn to-edn [^Attribution arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/Attribution %)]}
  (cond-> {}
          (.hasBaselineScore arg)
          (assoc :baselineScore (.getBaselineScore arg))
          (.hasInstanceOutputValue arg)
          (assoc :instanceOutputValue (.getInstanceOutputValue arg))
          (.hasFeatureAttributions arg)
          (assoc :featureAttributions (protobuf/value-to-edn (.getFeatureAttributions arg)))
          (some? (.getOutputIndex arg))
          (assoc :outputIndex (.getOutputIndex arg))
          (not (empty? (.getOutputDisplayName arg)))
          (assoc :outputDisplayName (.getOutputDisplayName arg))
          (.hasApproximationError arg)
          (assoc :approximationError (.getApproximationError arg))
          (not (empty? (.getOutputName arg)))
          (assoc :outputName (.getOutputName arg))))

(def schema
  [:map
   {:doc              "A feature attribution object describing a feature's contribution to an output. This class is part of the [Explanation API](https://cloud.google.com/vertex-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.Explanation) of Vertex AI. The class is annotated with Protobuf message type for interoperability."
    :class            'com.google.cloud.vertexai.api.Attribution
    :protobuf/type    "google.cloud.vertexai.v1.Attribution"}
   [:baselineScore {:optional true} :double]
   [:instanceOutputValue {:optional true} :gcp.protobuf/Value]
   [:featureAttributions {:optional true} :gcp.protobuf/Value]
   [:outputIndex {:optional true} :int]
   [:outputDisplayName {:optional true} :string]
   [:approximationError {:optional true} :double]
   [:outputName {:optional true} :string]])

(global/register-schema! :gcp.vertexai.v1.api/Attribution schema)
