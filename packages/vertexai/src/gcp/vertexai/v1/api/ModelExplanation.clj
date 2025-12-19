(ns gcp.vertexai.v1.api.ModelExplanation
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.Attribution :as Attribution])
  (:import (com.google.cloud.vertexai.api ModelExplanation)))

(defn ^ModelExplanation from-edn
  [{:keys [meanAttributions] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/ModelExplanation arg)
  (let [builder (ModelExplanation/newBuilder)]
    (some->> meanAttributions (map Attribution/from-edn) (.addAllMeanAttributions builder))
    (.build builder)))

(defn to-edn [^ModelExplanation arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/ModelExplanation %)]}
  (cond-> {}
          (pos? (.getMeanAttributionsCount arg))
          (assoc :meanAttributions (mapv Attribution/to-edn (.getMeanAttributionsList arg)))))

(def schema
  [:map
   {:doc              "Aggregated explanation results for the Model. This class is part of the [Explanation API](https://cloud.google.com/vertex-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.ModelExplanation) of Vertex AI. The class is annotated with Protobuf message type for interoperability."
    :class            'com.google.cloud.vertexai.api.ModelExplanation
    :protobuf/type    "google.cloud.vertexai.v1.ModelExplanation"}
   [:meanAttributions {:optional true} [:sequential :gcp.vertexai.v1.api/Attribution]]])

(global/register-schema! :gcp.vertexai.v1.api/ModelExplanation schema)
