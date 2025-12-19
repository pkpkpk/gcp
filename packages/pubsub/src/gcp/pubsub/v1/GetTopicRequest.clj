(ns gcp.pubsub.v1.GetTopicRequest
  (:require [gcp.global :as global])
  (:import (com.google.pubsub.v1 GetTopicRequest)))

(defn ^GetTopicRequest from-edn [arg] (throw (Exception. "unimplemented")))

(global/include-schema-registry! (with-meta {:gcp.pubsub.v1/GetTopicRequest :any} {:gcp.global/name (str *ns*)}))