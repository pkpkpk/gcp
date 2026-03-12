;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.ForeignKey
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.ForeignKey"
   :gcp.dev/certification
     {:base-seed 1771291733482
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771291733482 :standard 1771291733483 :stress 1771291733484}
      :protocol-hash
        "7068af39aa0d55cb4d0e4eaceead6fd12f374863b361a9717f08a69d4bd12910"
      :timestamp "2026-02-17T01:28:53.616959902Z"}}
  (:require [gcp.bindings.bigquery.ColumnReference :as ColumnReference]
            [gcp.bindings.bigquery.TableId :as TableId]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery ForeignKey ForeignKey$Builder]))

(defn ^ForeignKey from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/ForeignKey arg)
  (let [builder (ForeignKey/newBuilder)]
    (when (some? (get arg :columnReferences))
      (.setColumnReferences builder
                            (map ColumnReference/from-edn
                              (get arg :columnReferences))))
    (when (some? (get arg :name)) (.setName builder (get arg :name)))
    (when (some? (get arg :referencedTable))
      (.setReferencedTable builder
                           (TableId/from-edn (get arg :referencedTable))))
    (.build builder)))

(defn to-edn
  [^ForeignKey arg]
  {:post [(global/strict! :gcp.bindings.bigquery/ForeignKey %)]}
  (cond-> {}
    (.getColumnReferences arg) (assoc :columnReferences
                                 (map ColumnReference/to-edn
                                   (.getColumnReferences arg)))
    (.getName arg) (assoc :name (.getName arg))
    (.getReferencedTable arg) (assoc :referencedTable
                                (TableId/to-edn (.getReferencedTable arg)))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/ForeignKey}
   [:columnReferences
    {:optional true,
     :setter-doc "The set of column references for this foreign key. *"}
    [:seqable {:min 1} :gcp.bindings.bigquery/ColumnReference]]
   [:name {:optional true, :setter-doc "The name of the foreign key. *"}
    [:string {:min 1}]]
   [:referencedTable
    {:optional true, :setter-doc "The table referenced by this foreign key. *"}
    :gcp.bindings.bigquery/TableId]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/ForeignKey schema}
    {:gcp.global/name "gcp.bindings.bigquery.ForeignKey"}))