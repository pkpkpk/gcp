;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.EncryptionConfiguration
  {:doc nil
   :file-git-sha "36af39e222ccf992d15ddbf82148b98e377b40f3"
   :fqcn "com.google.cloud.bigquery.EncryptionConfiguration"
   :gcp.dev/certification
     {:base-seed 1790034036898
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034036898 :standard 1790034036899 :stress 1790034036900}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:40:38.103040487Z"}}
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

(global/include-registry! "gcp.bigquery.EncryptionConfiguration"
                          {:gcp.bigquery/EncryptionConfiguration schema})