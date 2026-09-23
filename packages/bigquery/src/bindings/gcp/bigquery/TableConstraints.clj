;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.TableConstraints
  {:doc nil
   :file-git-sha "90b1e1a8b33e2f0ceedc9cdba1410c0f0053f7e8"
   :fqcn "com.google.cloud.bigquery.TableConstraints"
   :gcp.dev/certification
     {:base-seed 1790034096582
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034096582 :standard 1790034096583 :stress 1790034096584}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:41:37.637077781Z"}}
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

(global/include-registry! "gcp.bigquery.TableConstraints"
                          {:gcp.bigquery/TableConstraints schema})