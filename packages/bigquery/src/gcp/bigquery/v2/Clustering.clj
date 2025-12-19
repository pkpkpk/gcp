(ns gcp.bigquery.v2.Clustering
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery Clustering)))

(defn ^Clustering from-edn [arg]
  (global/strict! :gcp.bigquery.v2/Clustering arg)
  (let [builder (Clustering/newBuilder)]
    (.setFields builder (:fields arg))
    (.build builder)))

(defn to-edn [arg]
  (throw (Exception. "unimplemented")))

(def schemas
  {:gcp.bigquery.v2/Clustering
   [:map {:closed true} [:fields [:sequential :string]]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))