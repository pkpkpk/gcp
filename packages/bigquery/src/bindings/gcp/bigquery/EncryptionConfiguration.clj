;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.EncryptionConfiguration
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.EncryptionConfiguration"
   :gcp.dev/certification
     {:base-seed 1775130837727
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1775130837727 :standard 1775130837728 :stress 1775130837729}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:53:58.807880272Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery EncryptionConfiguration
            EncryptionConfiguration$Builder]))

(declare from-edn to-edn)

(defn ^EncryptionConfiguration from-edn
  [arg]
  (global/strict! :gcp.bigquery/EncryptionConfiguration arg)
  (let [builder (EncryptionConfiguration/newBuilder)]
    (when (some? (get arg :kmsKeyName))
      (.setKmsKeyName builder (get arg :kmsKeyName)))
    (.build builder)))

(defn to-edn
  [^EncryptionConfiguration arg]
  {:post [(global/strict! :gcp.bigquery/EncryptionConfiguration %)]}
  (when arg
    (cond-> {}
      (some->> (.getKmsKeyName arg)
               (not= ""))
        (assoc :kmsKeyName (.getKmsKeyName arg)))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/EncryptionConfiguration}
   [:kmsKeyName {:optional true} [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/EncryptionConfiguration schema}
    {:gcp.global/name "gcp.bigquery.EncryptionConfiguration"}))