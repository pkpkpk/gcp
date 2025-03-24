(ns gcp.pubsub.v1.MessageTransform
  (:import (com.google.pubsub.v1 MessageTransform)))

(defn to-edn [^MessageTransform arg] (throw (Exception. "unimplemented")))

(defn ^MessageTransform from-edn [arg] (throw (Exception. "unimplemented")))