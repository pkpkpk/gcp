(ns gcp.pubsub.v1.ListTopicSubscriptionsRequest
  (:require [gcp.global :as global])
  (:import (com.google.pubsub.v1 ListTopicSubscriptionsRequest)))

(defn ^ListTopicSubscriptionsRequest from-edn [arg] (throw (Exception. "unimplemented")))

(global/include-schema-registry! (with-meta {:gcp.pubsub.v1/ListTopicSubscriptionsRequest :any} {:gcp.global/name (str *ns*)}))