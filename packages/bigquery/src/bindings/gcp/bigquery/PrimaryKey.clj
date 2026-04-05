;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.PrimaryKey
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.PrimaryKey"
   :gcp.dev/certification
     {:base-seed 1775130830255
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1775130830255 :standard 1775130830256 :stress 1775130830257}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:53:51.803880003Z"}}
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
    [:sequential {:min 1} [:string {:min 1}]]]])

(global/include-schema-registry! (with-meta {:gcp.bigquery/PrimaryKey schema}
                                   {:gcp.global/name
                                      "gcp.bigquery.PrimaryKey"}))