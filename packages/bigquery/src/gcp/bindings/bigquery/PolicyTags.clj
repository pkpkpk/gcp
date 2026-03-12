;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.PolicyTags
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.PolicyTags"
   :gcp.dev/certification
     {:base-seed 1770922219436
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1770922219436 :standard 1770922219437 :stress 1770922219438}
      :protocol-hash
        "777141ffa627adc01d6905e3612d06e94a14f62f076023bd260803be3b5490f7"
      :timestamp "2026-02-12T18:50:19.655729057Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery PolicyTags PolicyTags$Builder]))

(defn ^PolicyTags from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/PolicyTags arg)
  (let [builder (PolicyTags/newBuilder)]
    (when (get arg :names) (.setNames builder (seq (get arg :names))))
    (.build builder)))

(defn to-edn
  [^PolicyTags arg]
  {:post [(global/strict! :gcp.bindings.bigquery/PolicyTags %)]}
  (cond-> {} (.getNames arg) (assoc :names (seq (.getNames arg)))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/PolicyTags}
   [:names {:optional true} [:seqable {:min 1} :string]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/PolicyTags schema}
    {:gcp.global/name "gcp.bindings.bigquery.PolicyTags"}))