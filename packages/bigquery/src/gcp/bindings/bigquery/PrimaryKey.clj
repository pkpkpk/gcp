;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.PrimaryKey
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.PrimaryKey"
   :gcp.dev/certification
     {:base-seed 1771291775731
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771291775731 :standard 1771291775732 :stress 1771291775733}
      :protocol-hash
        "7068af39aa0d55cb4d0e4eaceead6fd12f374863b361a9717f08a69d4bd12910"
      :timestamp "2026-02-17T01:29:35.757785957Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery PrimaryKey PrimaryKey$Builder]))

(defn ^PrimaryKey from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/PrimaryKey arg)
  (let [builder (PrimaryKey/newBuilder)]
    (when (some? (get arg :columns))
      (.setColumns builder (seq (get arg :columns))))
    (.build builder)))

(defn to-edn
  [^PrimaryKey arg]
  {:post [(global/strict! :gcp.bindings.bigquery/PrimaryKey %)]}
  (cond-> {} (.getColumns arg) (assoc :columns (seq (.getColumns arg)))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/PrimaryKey}
   [:columns
    {:optional true, :setter-doc "The column names that are primary keys. *"}
    [:seqable {:min 1} [:string {:min 1}]]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/PrimaryKey schema}
    {:gcp.global/name "gcp.bindings.bigquery.PrimaryKey"}))