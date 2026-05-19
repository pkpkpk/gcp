;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.PolicyTags
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.PolicyTags"
   :gcp.dev/certification
     {:base-seed 1779204638628
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1779204638628 :standard 1779204638629 :stress 1779204638630}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-05-19T15:30:39.410144022Z"}}
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
    [:sequential {:min 1, :gen/max 2} [:string {:min 1, :gen/max 1}]]]])

(global/include-schema-registry! (with-meta {:gcp.bigquery/PolicyTags schema}
                                   {:gcp.global/name
                                      "gcp.bigquery.PolicyTags"}))