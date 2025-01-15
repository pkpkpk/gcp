(ns gcp.storage.v2.Bucket
  (:require [gcp.storage.v2.BucketInfo :as BucketInfo])
  (:import (com.google.cloud.storage Bucket)))

(defn to-edn [^Bucket arg]
  (assoc (BucketInfo/to-edn arg) :storage (.getStorage arg)))