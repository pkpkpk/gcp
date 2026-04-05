;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.PolicyTags
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.PolicyTags"
   :gcp.dev/certification
     {:base-seed 1775130855590
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1775130855590 :standard 1775130855591 :stress 1775130855592}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:54:16.705334216Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery PolicyTags PolicyTags$Builder]))

(declare from-edn to-edn)

(defn ^PolicyTags from-edn
  [arg]
  (global/strict! :gcp.bigquery/PolicyTags arg)
  (let [builder (PolicyTags/newBuilder)]
    (when (seq (get arg :names)) (.setNames builder (seq (get arg :names))))
    (.build builder)))

(defn to-edn
  [^PolicyTags arg]
  {:post [(global/strict! :gcp.bigquery/PolicyTags %)]}
  (when arg (cond-> {:names (seq (.getNames arg))})))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/PolicyTags}
   [:names {:getter-doc nil, :setter-doc nil}
    [:sequential {:min 1} [:string {:min 1}]]]])

(global/include-schema-registry! (with-meta {:gcp.bigquery/PolicyTags schema}
                                   {:gcp.global/name
                                      "gcp.bigquery.PolicyTags"}))