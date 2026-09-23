;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.PrimaryKey
  {:doc nil
   :file-git-sha "90b1e1a8b33e2f0ceedc9cdba1410c0f0053f7e8"
   :fqcn "com.google.cloud.bigquery.PrimaryKey"
   :gcp.dev/certification
     {:base-seed 1790034031199
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034031199 :standard 1790034031200 :stress 1790034031201}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:40:32.227159044Z"}}
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

(global/include-registry! "gcp.bigquery.PrimaryKey"
                          {:gcp.bigquery/PrimaryKey schema})