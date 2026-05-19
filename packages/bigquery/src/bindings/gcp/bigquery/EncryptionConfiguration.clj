;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.EncryptionConfiguration
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.EncryptionConfiguration"
   :gcp.dev/certification
     {:base-seed 1779204626821
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1779204626821 :standard 1779204626822 :stress 1779204626823}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-05-19T15:30:27.580137597Z"}}
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
   [:kmsKeyName {:optional true, :setter-doc nil}
    [:string {:min 1, :gen/max 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/EncryptionConfiguration schema}
    {:gcp.global/name "gcp.bigquery.EncryptionConfiguration"}))