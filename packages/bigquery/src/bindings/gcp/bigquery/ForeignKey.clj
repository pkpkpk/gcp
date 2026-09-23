;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.ForeignKey
  {:doc nil
   :file-git-sha "90b1e1a8b33e2f0ceedc9cdba1410c0f0053f7e8"
   :fqcn "com.google.cloud.bigquery.ForeignKey"
   :gcp.dev/certification
     {:base-seed 1790034095176
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034095176 :standard 1790034095177 :stress 1790034095178}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:41:36.235375604Z"}}
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

(global/include-registry! "gcp.bigquery.ForeignKey"
                          {:gcp.bigquery/ForeignKey schema})