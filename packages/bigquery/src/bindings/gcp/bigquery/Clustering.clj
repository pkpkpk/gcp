;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.Clustering
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.Clustering"
   :gcp.dev/certification
     {:base-seed 1775130832659
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1775130832659 :standard 1775130832660 :stress 1775130832661}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:53:54.053234024Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery Clustering Clustering$Builder]))

(declare from-edn to-edn)

(defn ^Clustering from-edn
  [arg]
  (global/strict! :gcp.bigquery/Clustering arg)
  (let [builder (Clustering/newBuilder)]
    (when (seq (get arg :fields)) (.setFields builder (seq (get arg :fields))))
    (.build builder)))

(defn to-edn
  [^Clustering arg]
  {:post [(global/strict! :gcp.bigquery/Clustering %)]}
  (when arg (cond-> {:fields (seq (.getFields arg))})))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/Clustering}
   [:fields {:getter-doc nil, :setter-doc nil}
    [:sequential {:min 1} [:string {:min 1}]]]])

(global/include-schema-registry! (with-meta {:gcp.bigquery/Clustering schema}
                                   {:gcp.global/name
                                      "gcp.bigquery.Clustering"}))