;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.Clustering
  {:doc nil
   :file-git-sha "36af39e222ccf992d15ddbf82148b98e377b40f3"
   :fqcn "com.google.cloud.bigquery.Clustering"
   :gcp.dev/certification
     {:base-seed 1790034032540
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034032540 :standard 1790034032541 :stress 1790034032542}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:40:33.514954566Z"}}
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

(global/include-registry! "gcp.bigquery.Clustering"
                          {:gcp.bigquery/Clustering schema})