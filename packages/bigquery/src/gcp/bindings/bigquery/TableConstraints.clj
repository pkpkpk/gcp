;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.TableConstraints
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.TableConstraints"
   :gcp.dev/certification
     {:base-seed 1771291780763
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771291780763 :standard 1771291780764 :stress 1771291780765}
      :protocol-hash
        "7068af39aa0d55cb4d0e4eaceead6fd12f374863b361a9717f08a69d4bd12910"
      :timestamp "2026-02-17T01:29:41.187784593Z"}}
  (:require [gcp.bindings.bigquery.ForeignKey :as ForeignKey]
            [gcp.bindings.bigquery.PrimaryKey :as PrimaryKey]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery TableConstraints
            TableConstraints$Builder]))

(defn ^TableConstraints from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/TableConstraints arg)
  (let [builder (TableConstraints/newBuilder)]
    (when (some? (get arg :foreignKeys))
      (.setForeignKeys builder
                       (map ForeignKey/from-edn (get arg :foreignKeys))))
    (when (some? (get arg :primaryKey))
      (.setPrimaryKey builder (PrimaryKey/from-edn (get arg :primaryKey))))
    (.build builder)))

(defn to-edn
  [^TableConstraints arg]
  {:post [(global/strict! :gcp.bindings.bigquery/TableConstraints %)]}
  (cond-> {}
    (.getForeignKeys arg) (assoc :foreignKeys
                            (map ForeignKey/to-edn (.getForeignKeys arg)))
    (.getPrimaryKey arg) (assoc :primaryKey
                           (PrimaryKey/to-edn (.getPrimaryKey arg)))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/TableConstraints}
   [:foreignKeys
    {:optional true,
     :setter-doc "The list of foreign keys for the table constraints. *"}
    [:seqable {:min 1} :gcp.bindings.bigquery/ForeignKey]]
   [:primaryKey
    {:optional true, :setter-doc "The primary key for the table constraints. *"}
    :gcp.bindings.bigquery/PrimaryKey]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/TableConstraints schema}
    {:gcp.global/name "gcp.bindings.bigquery.TableConstraints"}))