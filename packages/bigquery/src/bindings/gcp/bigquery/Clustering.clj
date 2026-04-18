;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.Clustering
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.Clustering"
   :gcp.dev/certification
     {:base-seed 1776499319468
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1776499319468 :standard 1776499319469 :stress 1776499319470}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:02:00.827679641Z"}}
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