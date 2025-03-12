(ns gcp.storage.v2.StorageRetryStrategy
  (:import (com.google.cloud.storage StorageRetryStrategy)))

(defn to-edn [^StorageRetryStrategy arg]
  (throw (Exception. "unimplemented")))

(defn ^StorageRetryStrategy from-edn [arg]
  (throw (Exception. "unimplemented")))