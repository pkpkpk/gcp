;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.ForeignKey
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.ForeignKey"
   :gcp.dev/certification
     {:base-seed 1775130901572
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1775130901572 :standard 1775130901573 :stress 1775130901574}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:55:02.891139771Z"}}
  (:require [gcp.bigquery.ColumnReference :as ColumnReference]
            [gcp.bigquery.TableId :as TableId]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery ForeignKey ForeignKey$Builder]))

(declare from-edn to-edn)

(defn ^ForeignKey from-edn
  [arg]
  (global/strict! :gcp.bigquery/ForeignKey arg)
  (let [builder (ForeignKey/newBuilder)]
    (when (seq (get arg :columnReferences))
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
  {:post [(global/strict! :gcp.bigquery/ForeignKey %)]}
  (when arg
    (cond-> {}
      (seq (.getColumnReferences arg)) (assoc :columnReferences
                                         (map ColumnReference/to-edn
                                           (.getColumnReferences arg)))
      (some->> (.getName arg)
               (not= ""))
        (assoc :name (.getName arg))
      (.getReferencedTable arg) (assoc :referencedTable
                                  (TableId/to-edn (.getReferencedTable arg))))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/ForeignKey}
   [:columnReferences
    {:optional true,
     :setter-doc "The set of column references for this foreign key. *"}
    [:sequential {:min 1} :gcp.bigquery/ColumnReference]]
   [:name {:optional true, :setter-doc "The name of the foreign key. *"}
    [:string {:min 1}]]
   [:referencedTable
    {:optional true, :setter-doc "The table referenced by this foreign key. *"}
    :gcp.bigquery/TableId]])

(global/include-schema-registry! (with-meta {:gcp.bigquery/ForeignKey schema}
                                   {:gcp.global/name
                                      "gcp.bigquery.ForeignKey"}))