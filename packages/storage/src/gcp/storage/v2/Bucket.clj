(ns gcp.storage.v2.Bucket
  (:require [gcp.global :as global]
            [gcp.storage.v2.BucketInfo :as BucketInfo]
            [gcp.storage.v2.Storage])
  (:import (com.google.cloud.storage Bucket)))

(defn to-edn [^Bucket arg]
  (when arg
    (assoc (BucketInfo/to-edn arg) :storage (.getStorage arg))))

(def schema
  [:and
   {:class 'com.google.cloud.storage.Bucket}
   [:map [:storage :gcp.storage.v2/Storage]]
   :gcp.storage.v2/BucketInfo])

(global/register-schema! :gcp.storage.v2/Bucket schema)