(ns gcp.storage.v2.Blob
  (:require [gcp.global :as global]
            [gcp.storage.v2.BlobInfo :as BlobInfo]
            [gcp.storage.v2.Storage])
  (:import (com.google.cloud.storage Blob)))

(defn to-edn [^Blob arg]
  (when arg
    (assoc (BlobInfo/to-edn arg) :storage (.getStorage arg))))

(def schema
  [:and
   {:class 'com.google.cloud.storage.Blob}
   [:map [:storage :gcp.storage.v2/Storage]]
   :gcp.storage.v2/BlobInfo])

(global/register-schema! :gcp.storage.v2/Blob schema)