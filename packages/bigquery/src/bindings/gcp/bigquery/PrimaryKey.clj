;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.PrimaryKey
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.PrimaryKey"
   :gcp.dev/certification
     {:base-seed 1776499317750
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1776499317750 :standard 1776499317751 :stress 1776499317752}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:01:59.175892503Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery PrimaryKey PrimaryKey$Builder]))

(declare from-edn to-edn)

(defn ^PrimaryKey from-edn
  [arg]
  (global/strict! :gcp.bigquery/PrimaryKey arg)
  (let [builder (PrimaryKey/newBuilder)]
    (when (seq (get arg :columns))
      (.setColumns builder (seq (get arg :columns))))
    (.build builder)))

(defn to-edn
  [^PrimaryKey arg]
  {:post [(global/strict! :gcp.bigquery/PrimaryKey %)]}
  (when arg
    (cond-> {}
      (seq (.getColumns arg)) (assoc :columns (seq (.getColumns arg))))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/PrimaryKey}
   [:columns
    {:optional true, :setter-doc "The column names that are primary keys. *"}
    [:sequential {:min 1} [:string {:min 1}]]]])

(global/include-schema-registry! (with-meta {:gcp.bigquery/PrimaryKey schema}
                                   {:gcp.global/name
                                      "gcp.bigquery.PrimaryKey"}))