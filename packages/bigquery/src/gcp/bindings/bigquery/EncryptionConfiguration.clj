;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.EncryptionConfiguration
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.EncryptionConfiguration"
   :gcp.dev/certification
     {:base-seed 1771344501844
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771344501844 :standard 1771344501845 :stress 1771344501846}
      :protocol-hash
        "32175171656ac95c72455bff25fe019346fb52c916efee805be149692e31cec4"
      :timestamp "2026-02-17T16:08:21.854383642Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery EncryptionConfiguration
            EncryptionConfiguration$Builder]))

(defn ^EncryptionConfiguration from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/EncryptionConfiguration arg)
  (let [builder (EncryptionConfiguration/newBuilder)]
    (when (some? (get arg :kmsKeyName))
      (.setKmsKeyName builder (get arg :kmsKeyName)))
    (.build builder)))

(defn to-edn
  [^EncryptionConfiguration arg]
  {:post [(global/strict! :gcp.bindings.bigquery/EncryptionConfiguration %)]}
  (cond-> {} (.getKmsKeyName arg) (assoc :kmsKeyName (.getKmsKeyName arg))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/EncryptionConfiguration}
   [:kmsKeyName {:optional true} [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/EncryptionConfiguration schema}
    {:gcp.global/name "gcp.bindings.bigquery.EncryptionConfiguration"}))