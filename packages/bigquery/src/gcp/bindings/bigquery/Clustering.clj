;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.Clustering
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.Clustering"
   :gcp.dev/certification
     {:base-seed 1771261172984
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771261172984 :standard 1771261172985 :stress 1771261172986}
      :protocol-hash
        "7068af39aa0d55cb4d0e4eaceead6fd12f374863b361a9717f08a69d4bd12910"
      :timestamp "2026-02-16T16:59:33.018371543Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery Clustering Clustering$Builder]))

(defn ^Clustering from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/Clustering arg)
  (let [builder (Clustering/newBuilder)]
    (when (some? (get arg :fields))
      (.setFields builder (seq (get arg :fields))))
    (.build builder)))

(defn to-edn
  [^Clustering arg]
  {:post [(global/strict! :gcp.bindings.bigquery/Clustering %)]}
  (cond-> {:fields (seq (.getFields arg))}))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/Clustering}
   [:fields {:getter-doc nil, :setter-doc nil} [:seqable {:min 1} :string]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/Clustering schema}
    {:gcp.global/name "gcp.bindings.bigquery.Clustering"}))