(ns gcp.pubsub.v1.SubscriberStub
  (:require [gcp.global :as global])
  (:import (com.google.cloud.pubsub.v1.stub SubscriberStub)))

(defn ^SubscriberStub from-edn [arg] (throw (Exception. "unimplemented")))

(global/include-schema-registry! (with-meta {:gcp.pubsub.v1/SubscriberStub :any} {:gcp.global/name (str *ns*)}))