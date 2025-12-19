(ns gcp.vertexai.v1.api.VideoMetadata
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import (com.google.cloud.vertexai.api VideoMetadata)))

(defn ^VideoMetadata from-edn [arg]
  (global/strict! :gcp.vertexai.v1.api/VideoMetadata arg)
  (let [builder (VideoMetadata/newBuilder)]
    (some->> (:startOffset arg) protobuf/Duration-from-edn (.setStartOffset builder))
    (some->> (:endOffset arg) protobuf/Duration-from-edn (.setEndOffset builder))
    (.build builder)))

(defn to-edn [^VideoMetadata arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/VideoMetadata %)]}
  (cond-> {}
          (.hasStartOffset arg)
          (assoc :startOffset (protobuf/Duration-to-edn (.getStartOffset arg)))
          (.hasEndOffset arg)
          (assoc :endOffset (protobuf/Duration-to-edn (.getEndOffset arg)))))

(def schema
  [:map
   {:class 'com.google.cloud.vertexai.api.VideoMetadata}
   [:startOffset {:optional true} :gcp.protobuf/Duration]
   [:endOffset {:optional true} :gcp.protobuf/Duration]])

(global/register-schema! :gcp.vertexai.v1.api/VideoMetadata schema)
