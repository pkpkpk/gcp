(ns gcp.vertexai.v1.api.ExplanationMetadataOverride
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import (com.google.cloud.vertexai.api ExplanationMetadataOverride ExplanationMetadataOverride$InputMetadataOverride)))

(defn ^ExplanationMetadataOverride$InputMetadataOverride InputMetadataOverride-from-edn [arg]
  (let [builder (ExplanationMetadataOverride$InputMetadataOverride/newBuilder)]
    (some->> (:inputBaselines arg) (map protobuf/value-from-edn) (.addAllInputBaselines builder))
    (.build builder)))

(defn InputMetadataOverride-to-edn [^ExplanationMetadataOverride$InputMetadataOverride arg]
  (cond-> {}
          (pos? (.getInputBaselinesCount arg))
          (assoc :inputBaselines (mapv protobuf/value-to-edn (.getInputBaselinesList arg)))))

(defn ^ExplanationMetadataOverride from-edn
  [{:keys [inputs] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/ExplanationMetadataOverride arg)
  (let [builder (ExplanationMetadataOverride/newBuilder)]
    (some->> inputs (map (fn [[k v]] [k (InputMetadataOverride-from-edn v)])) (into {}) (.putAllInputs builder))
    (.build builder)))

(defn to-edn [^ExplanationMetadataOverride arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/ExplanationMetadataOverride %)]}
  (cond-> {}
          (pos? (.getInputsCount arg))
          (assoc :inputs (into {} (map (fn [[k v]] [k (InputMetadataOverride-to-edn v)])) (.getInputsMap arg)))))

(def schema
  [:map
   {:doc              "The [ExplanationMetadata.InputMetadata][google.cloud.aiplatform.v1.ExplanationMetadata.InputMetadata] can be overridden by this message. This class is part of the [Explanation API](https://cloud.google.com/vertex-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.ExplanationMetadataOverride) of Vertex AI. The class is annotated with Protobuf message type for interoperability."
    :class            'com.google.cloud.vertexai.api.ExplanationMetadataOverride
    :protobuf/type    "google.cloud.vertexai.v1.ExplanationMetadataOverride"}
   [:inputs {:optional true} [:map-of :string [:map
                                               [:inputBaselines {:optional true} [:sequential :gcp.protobuf/Value]]]]]])

(global/register-schema! :gcp.vertexai.v1.api/ExplanationMetadataOverride schema)
