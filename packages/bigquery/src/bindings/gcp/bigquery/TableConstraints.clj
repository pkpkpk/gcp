;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.TableConstraints
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.TableConstraints"
   :gcp.dev/certification
     {:base-seed 1779204671606
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1779204671606 :standard 1779204671607 :stress 1779204671608}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-05-19T15:31:12.430412799Z"}}
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
                       (mapv ForeignKey/from-edn (get arg :foreignKeys))))
    (when (some? (get arg :primaryKey))
      (.setPrimaryKey builder (PrimaryKey/from-edn (get arg :primaryKey))))
    (.build builder)))

(defn to-edn
  [^TableConstraints arg]
  {:post [(global/strict! :gcp.bigquery/TableConstraints %)]}
  (when arg
    (cond-> {}
      (seq (.getForeignKeys arg))
        (assoc :foreignKeys (mapv ForeignKey/to-edn (.getForeignKeys arg)))
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
    [:sequential {:min 1, :gen/max 2} :gcp.bigquery/ForeignKey]]
   [:primaryKey
    {:optional true, :setter-doc "The primary key for the table constraints. *"}
    :gcp.bigquery/PrimaryKey]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/TableConstraints schema}
    {:gcp.global/name "gcp.bigquery.TableConstraints"}))