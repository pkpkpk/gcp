(ns gcp.pubsub.v1.GetSubscriptionRequest
  (:require [gcp.global :as global])
  (:import (com.google.pubsub.v1 GetSubscriptionRequest)))

(defn ^GetSubscriptionRequest from-edn [arg] (throw (Exception. "unimplemented")))

(global/include-schema-registry! (with-meta {:gcp.pubsub.v1/GetSubscriptionRequest :any} {:gcp.global/name (str *ns*)}))