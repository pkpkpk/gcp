;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.ForeignKey
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.ForeignKey"
   :gcp.dev/certification
     {:base-seed 1779204670508
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1779204670508 :standard 1779204670509 :stress 1779204670510}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-05-19T15:31:11.341709867Z"}}
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
                            (mapv ColumnReference/from-edn
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
                                         (mapv ColumnReference/to-edn
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
    [:sequential {:min 1, :gen/max 2} :gcp.bigquery/ColumnReference]]
   [:name {:optional true, :setter-doc "The name of the foreign key. *"}
    [:string {:min 1, :gen/max 1}]]
   [:referencedTable
    {:optional true, :setter-doc "The table referenced by this foreign key. *"}
    :gcp.bigquery/TableId]])

(global/include-schema-registry! (with-meta {:gcp.bigquery/ForeignKey schema}
                                   {:gcp.global/name
                                      "gcp.bigquery.ForeignKey"}))