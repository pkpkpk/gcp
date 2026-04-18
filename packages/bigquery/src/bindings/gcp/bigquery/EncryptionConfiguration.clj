;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.EncryptionConfiguration
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.EncryptionConfiguration"
   :gcp.dev/certification
     {:base-seed 1776499324540
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1776499324540 :standard 1776499324541 :stress 1776499324542}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:02:05.856789838Z"}}
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
   [:kmsKeyName {:optional true, :setter-doc nil} [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/EncryptionConfiguration schema}
    {:gcp.global/name "gcp.bigquery.EncryptionConfiguration"}))