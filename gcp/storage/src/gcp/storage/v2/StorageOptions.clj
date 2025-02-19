(ns gcp.storage.v2.StorageOptions
  (:require [gcp.global :as g])
  (:import (com.google.cloud.storage Storage StorageOptions)))

(defn ^StorageOptions from-edn [arg]
  (g/strict! :storage/StorageOptions arg)
  (let [builder (StorageOptions/newBuilder)]
    (when (not (empty? arg))
      (throw (Exception. "unimplemented")))
    (.build builder)))

(defn to-edn [^StorageOptions arg] (throw (Exception. "unimplemented")))

(defn ^Storage get-service [arg]
  (if (instance? Storage arg)
    arg
    (if (instance? StorageOptions arg)
      (.getService arg)
      (.getService (from-edn arg)))))

