;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.TableConstraints
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.TableConstraints"
   :gcp.dev/certification
     {:base-seed 1775130903358
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1775130903358 :standard 1775130903359 :stress 1775130903360}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:55:04.759440544Z"}}
  (:require [gcp.bigquery.ForeignKey :as ForeignKey]
            [gcp.bigquery.PrimaryKey :as PrimaryKey]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery TableConstraints
            TableConstraints$Builder]))

(declare from-edn to-edn)

(defn ^TableConstraints from-edn
  [arg]
  (global/strict! :gcp.bigquery/TableConstraints arg)
  (let [builder (TableConstraints/newBuilder)]
    (when (seq (get arg :foreignKeys))
      (.setForeignKeys builder
                       (map ForeignKey/from-edn (get arg :foreignKeys))))
    (when (some? (get arg :primaryKey))
      (.setPrimaryKey builder (PrimaryKey/from-edn (get arg :primaryKey))))
    (.build builder)))

(defn to-edn
  [^TableConstraints arg]
  {:post [(global/strict! :gcp.bigquery/TableConstraints %)]}
  (when arg
    (cond-> {}
      (seq (.getForeignKeys arg))
        (assoc :foreignKeys (map ForeignKey/to-edn (.getForeignKeys arg)))
      (.getPrimaryKey arg) (assoc :primaryKey
                             (PrimaryKey/to-edn (.getPrimaryKey arg))))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/TableConstraints}
   [:foreignKeys
    {:optional true,
     :setter-doc "The list of foreign keys for the table constraints. *"}
    [:sequential {:min 1} :gcp.bigquery/ForeignKey]]
   [:primaryKey
    {:optional true, :setter-doc "The primary key for the table constraints. *"}
    :gcp.bigquery/PrimaryKey]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/TableConstraints schema}
    {:gcp.global/name "gcp.bigquery.TableConstraints"}))