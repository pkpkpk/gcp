(ns gcp.storage.v2.Blob
  (:require [gcp.storage.v2.BlobInfo :as BlobInfo])
  (:import (com.google.cloud.storage Blob)))

(defn to-edn [^Blob arg]
  (when arg
    (assoc (BlobInfo/to-edn arg) :storage (.getStorage arg))))