(ns gcp.pubsub.v1.ListTopicsRequest
  (:require [gcp.global :as global])
  (:import (com.google.pubsub.v1 ListTopicsRequest)))

(defn ^ListTopicsRequest from-edn
  [{:keys [project
           pageToken
           pageSize] :as arg}]
  (global/strict! :gcp.pubsub.v1/ListTopicsRequest arg)
  (let [builder (ListTopicsRequest/newBuilder)]
    (.setProject builder project)
    (some->> pageToken (.setPageToken builder))
    (some->> pageSize (.setPageSize builder))
    (.build builder)))

(def schemas
  {:gcp.pubsub.v1/ListTopicsRequest
   [:map
    [:project
     {:doc "The name of the project in which to list topics. Format is projects/{project-id}."}
     :string]
    [:pageToken {:optional true} :string]
    [:pageSize {:optional true} :int]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))