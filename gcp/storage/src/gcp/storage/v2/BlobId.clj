(ns gcp.storage.v2.BlobId
  (:require [gcp.global :as g])
  (:import (com.google.cloud.storage BlobId)))

(defn ^BlobId from-edn
  [{:keys [bucket name generation] :as arg}]
  (g/strict! :gcp/storage.BlobId arg)
  (if (some? generation)
    (BlobId/of bucket name (long generation))
    (BlobId/of bucket name)))

(defn to-edn [^BlobId arg]
  {:post [(g/strict! :gcp/storage.BlobId %)]}
  (cond-> {:bucket (.getBucket arg)
           :name   (.getName arg)}
          (some? (.getGeneration arg))
          (assoc :generation (.getGeneration arg))))