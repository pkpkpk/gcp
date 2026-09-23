;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.PolicyTags
  {:doc nil
   :file-git-sha "e17ad6f3b917cc04d2433378494d5680121c4457"
   :fqcn "com.google.cloud.bigquery.PolicyTags"
   :gcp.dev/certification
     {:base-seed 1790034055823
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034055823 :standard 1790034055824 :stress 1790034055825}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:40:57.119427355Z"}}
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

(global/include-registry! "gcp.bigquery.PolicyTags"
                          {:gcp.bigquery/PolicyTags schema})