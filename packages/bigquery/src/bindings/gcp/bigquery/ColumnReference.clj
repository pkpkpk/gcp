;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.ColumnReference
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.ColumnReference"
   :gcp.dev/certification
     {:base-seed 1776499391340
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1776499391340 :standard 1776499391341 :stress 1776499391342}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:03:12.660332969Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery ColumnReference ColumnReference$Builder]))

(declare from-edn to-edn)

(defn ^ColumnReference from-edn
  [arg]
  (global/strict! :gcp.bigquery/ColumnReference arg)
  (let [builder (ColumnReference/newBuilder)]
    (when (some? (get arg :referencedColumn))
      (.setReferencedColumn builder (get arg :referencedColumn)))
    (when (some? (get arg :referencingColumn))
      (.setReferencingColumn builder (get arg :referencingColumn)))
    (.build builder)))

(defn to-edn
  [^ColumnReference arg]
  {:post [(global/strict! :gcp.bigquery/ColumnReference %)]}
  (when arg
    (cond-> {}
      (some->> (.getReferencedColumn arg)
               (not= ""))
        (assoc :referencedColumn (.getReferencedColumn arg))
      (some->> (.getReferencingColumn arg)
               (not= ""))
        (assoc :referencingColumn (.getReferencingColumn arg)))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/ColumnReference}
   [:referencedColumn
    {:optional true, :setter-doc "The target column of this reference. *"}
    [:string {:min 1}]]
   [:referencingColumn
    {:optional true, :setter-doc "The source column of this reference. *"}
    [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/ColumnReference schema}
    {:gcp.global/name "gcp.bigquery.ColumnReference"}))