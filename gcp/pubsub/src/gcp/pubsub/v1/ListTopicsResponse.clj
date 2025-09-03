(ns gcp.pubsub.v1.ListTopicsResponse
  (:import (com.google.pubsub.v1 ListTopicsResponse)))

(defn to-edn [^ListTopicsResponse arg]
  (when arg
    (not-empty
      (cond-> {}
              (some? (.getNextPageToken arg))
              (assoc :nextPageToken (.getNextPageToken arg))
              (pos? (.getTopicsCount arg))
              (assoc :topics (vec (.getTopicsList arg)))))))
