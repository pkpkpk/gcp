(ns gcp.storage.v2.Storage
  (:import (com.google.cloud.storage Storage$BlobGetOption Storage$BlobListOption Storage$BlobSourceOption Storage$BucketGetOption Storage$BucketListOption Storage$BucketTargetOption)))

(defn ^Storage$BlobListOption     BlobListOption-from-edn [arg] (throw (Exception. "unimplemented")))
(defn ^Storage$BlobSourceOption   BlobSourceOption-from-edn [arg] (throw (Exception. "unimplemented")))
(defn ^Storage$BlobGetOption      BlobGetOption-from-edn [arg] (throw (Exception. "unimplemented")))

(defn ^Storage$BucketGetOption    BucketGetOption-from-edn [arg] (throw (Exception. "unimplemented")))
(defn ^Storage$BucketListOption   BucketListOption-from-edn [arg] (throw (Exception. "unimplemented")))
(defn ^Storage$BucketTargetOption BucketTargetOption-from-edn [arg] (throw (Exception. "unimplemented")))
