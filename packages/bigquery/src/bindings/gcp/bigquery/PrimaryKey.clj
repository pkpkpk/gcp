;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.PrimaryKey
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.PrimaryKey"
   :gcp.dev/certification
     {:base-seed 1779204621890
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1779204621890 :standard 1779204621891 :stress 1779204621892}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-05-19T15:30:22.860539473Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery PrimaryKey PrimaryKey$Builder]))

(declare from-edn to-edn)

(defn ^PrimaryKey from-edn
  [arg]
  (global/strict! :gcp.bigquery/PrimaryKey arg)
  (let [builder (PrimaryKey/newBuilder)]
    (when (seq (get arg :columns))
      (.setColumns builder (seq (get arg :columns))))
    (.build builder)))

(defn to-edn
  [^PrimaryKey arg]
  {:post [(global/strict! :gcp.bigquery/PrimaryKey %)]}
  (when arg
    (cond-> {}
      (seq (.getColumns arg)) (assoc :columns (seq (.getColumns arg))))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/PrimaryKey}
   [:columns
    {:optional true, :setter-doc "The column names that are primary keys. *"}
    [:sequential {:min 1, :gen/max 2} [:string {:min 1, :gen/max 1}]]]])

(global/include-schema-registry! (with-meta {:gcp.bigquery/PrimaryKey schema}
                                   {:gcp.global/name
                                      "gcp.bigquery.PrimaryKey"}))