(ns gcp.vertexai.v1.api.Explanation
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.Attribution :as Attribution]
            [gcp.vertexai.v1.api.Neighbor :as Neighbor])
  (:import (com.google.cloud.vertexai.api Explanation)))

(defn ^Explanation from-edn [arg]
  (global/strict! :gcp.vertexai.v1.api/Explanation arg)
  (let [builder (Explanation/newBuilder)]
    (some->> (:attributions arg) (map Attribution/from-edn) (.addAllAttributions builder))
    (some->> (:neighbors arg) (map Neighbor/from-edn) (.addAllNeighbors builder))
    (.build builder)))

(defn to-edn [^Explanation arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/Explanation %)]}
  (cond-> {}
          (pos? (.getAttributionsCount arg))
          (assoc :attributions (mapv Attribution/to-edn (.getAttributionsList arg)))
          (pos? (.getNeighborsCount arg))
          (assoc :neighbors (mapv Neighbor/to-edn (.getNeighborsList arg)))))

(def schema
  [:map
   {:doc              "Explanation results for a sample. This class is part of the [Explanation API](https://cloud.google.com/vertex-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.Explanation) of Vertex AI. The class is annotated with Protobuf message type for interoperability."
    :class            'com.google.cloud.vertexai.api.Explanation
    :protobuf/type    "google.cloud.vertexai.v1.Explanation"}
   [:attributions {:optional true} [:sequential :gcp.vertexai.v1.api/Attribution]]
   [:neighbors {:optional true} [:sequential :gcp.vertexai.v1.api/Neighbor]]])

(global/register-schema! :gcp.vertexai.v1.api/Explanation schema)
