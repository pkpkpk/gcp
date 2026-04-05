(ns gcp.storage.custom.StorageRetryStrategy
  (:require [gcp.global :as g])
  (:import (com.google.cloud.storage StorageRetryStrategy)))

(defn ^StorageRetryStrategy from-edn [arg]
  (g/strict! :gcp.storage/StorageRetryStrategy arg)
  (if (= :uniform (get arg :strategy))
    (StorageRetryStrategy/getUniformStorageRetryStrategy)
    (if (= :legacy (get arg :strategy))
      (StorageRetryStrategy/getLegacyStorageRetryStrategy)
      (StorageRetryStrategy/getDefaultStorageRetryStrategy))))

(defn to-edn [_] (throw (Exception. "StorageRetryStrategy is write-only")))

(def schema
  [:maybe {:write-only? true}
   [:map {:closed true}
    [:strategy [:maybe [:enum :uniform :default :legacy]]]]])

(g/include-schema-registry!
  (with-meta {:gcp.storage/StorageRetryStrategy schema}
             {::name "gcp.storage.custom.StorageRetryStrategy"}))