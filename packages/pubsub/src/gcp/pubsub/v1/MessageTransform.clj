(ns gcp.pubsub.v1.MessageTransform
  (:require [gcp.global :as global])
  (:import (com.google.pubsub.v1 MessageTransform)))

(defn to-edn [^MessageTransform arg] (throw (Exception. "unimplemented")))

(defn ^MessageTransform from-edn [arg] (throw (Exception. "unimplemented")))

(global/include-schema-registry! (with-meta {:gcp.pubsub.v1/MessageTransform :any} {:gcp.global/name (str *ns*)}))