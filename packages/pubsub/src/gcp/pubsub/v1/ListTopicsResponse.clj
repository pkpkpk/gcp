(ns gcp.pubsub.v1.ListTopicsResponse
  (:require [gcp.global :as global])
  (:import (com.google.pubsub.v1 ListTopicsResponse)))

(defn to-edn [^ListTopicsResponse arg]
  (when arg
    (not-empty
      (cond-> {}
              (some? (.getNextPageToken arg))
              (assoc :nextPageToken (.getNextPageToken arg))
              (pos? (.getTopicsCount arg))
              (assoc :topics (vec (.getTopicsList arg)))))))

(def schemas
  {:gcp.pubsub.v1/ListTopicsResponse
   [:map
    [:nextPageToken {:optional true} :string]
    [:topics {:optional true} [:sequential :any]]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))
