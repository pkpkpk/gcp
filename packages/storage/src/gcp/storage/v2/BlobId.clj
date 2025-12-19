(ns gcp.storage.v2.BlobId
  (:require [gcp.global :as global])
  (:import (com.google.cloud.storage BlobId)))

(defn ^BlobId from-edn
  [{:keys [bucket name generation] :as arg}]
  (global/strict! :gcp.storage.v2/BlobId arg)
  (if (some? generation)
    (BlobId/of bucket name (long generation))
    (BlobId/of bucket name)))

(defn to-edn [^BlobId arg]
  {:post [(global/strict! :gcp.storage.v2/BlobId %)]}
  (cond-> {:bucket (.getBucket arg)
           :name   (.getName arg)}
          (some? (.getGeneration arg))
          (assoc :generation (.getGeneration arg))))

(def schema
  [:map
   {:closed   true
    :class    'com.google.cloud.storage.BlobId
    :from-edn 'gcp.storage.v2.BlobId/from-edn
    :to-edn   'gcp.storage.v2.BlobId/to-edn}
   [:bucket :string]
   [:name :string]
   [:generation {:optional true} :int]])

(global/register-schema! :gcp.storage.v2/BlobId schema)