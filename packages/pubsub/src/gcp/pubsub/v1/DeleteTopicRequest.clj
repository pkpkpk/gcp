(ns gcp.pubsub.v1.DeleteTopicRequest
  (:require [gcp.global :as global])
  (:import (com.google.pubsub.v1 DeleteTopicRequest)))

(defn ^DeleteTopicRequest from-edn [arg] (throw (Exception. "unimplemented")))

(global/include-schema-registry! (with-meta {:gcp.pubsub.v1/DeleteTopicRequest :any} {:gcp.global/name (str *ns*)}))