(ns gcp.storage.custom.Blob
  (:require [gcp.storage.BlobInfo :as BlobInfo])
  (:import (com.google.cloud.storage Blob)))

(defn to-edn [^Blob arg]
  (when arg
    (assoc (BlobInfo/to-edn arg) :storage (.getStorage arg))))