;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.Clustering
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.Clustering"
   :gcp.dev/certification
     {:base-seed 1779204623511
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1779204623511 :standard 1779204623512 :stress 1779204623513}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-05-19T15:30:24.289021914Z"}}
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
    [:sequential {:min 1, :gen/max 2} [:string {:min 1, :gen/max 1}]]]])

(global/include-schema-registry! (with-meta {:gcp.bigquery/Clustering schema}
                                   {:gcp.global/name
                                      "gcp.bigquery.Clustering"}))