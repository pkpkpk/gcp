(ns gcp.vertexai.v1.api.Neighbor
  (:require [gcp.global :as global])
  (:import (com.google.cloud.vertexai.api Neighbor)))

(defn ^Neighbor from-edn
  [{:keys [neighborId neighborDistance] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/Neighbor arg)
  (let [builder (Neighbor/newBuilder)]
    (some->> neighborId (.setNeighborId builder))
    (some->> neighborDistance (.setNeighborDistance builder))
    (.build builder)))

(defn to-edn [^Neighbor arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/Neighbor %)]}
  (cond-> {}
          (not (empty? (.getNeighborId arg)))
          (assoc :neighborId (.getNeighborId arg))
          (.hasNeighborDistance arg)
          (assoc :neighborDistance (.getNeighborDistance arg))))

(def schema
  [:map
   {:doc              "Neighbors for the given instance. This class is part of the [Explanation API](https://cloud.google.com/vertex-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.Neighbor) of Vertex AI. The class is annotated with Protobuf message type for interoperability."
    :class            'com.google.cloud.vertexai.api.Neighbor
    :protobuf/type    "google.cloud.vertexai.v1.Neighbor"}
   [:neighborId {:optional true} :string]
   [:neighborDistance {:optional true} :double]])

(global/register-schema! :gcp.vertexai.v1.api/Neighbor schema)
