(ns gcp.storage.custom.Bucket
  (:require [gcp.storage.BucketInfo :as BucketInfo])
  (:import (com.google.cloud.storage Bucket)))

(defn to-edn [^Bucket arg]
  (when arg
    (assoc (BucketInfo/to-edn arg) :storage (.getStorage arg))))