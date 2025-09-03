(ns gcp.pubsub.v1.ListTopicsRequest
  (:require [gcp.global :as g])
  (:import (com.google.pubsub.v1 ListTopicsRequest)))

(defn ^ListTopicsRequest from-edn
  [{:keys [project
           pageToken
           pageSize] :as arg}]
  (g/strict! :gcp/pubsub.ListTopicsRequest arg)
  (let [builder (ListTopicsRequest/newBuilder)]
    (.setProject builder project)
    (some->> pageToken (.setPageToken builder))
    (some->> pageSize (.setPageSize builder))
    (.build builder)))