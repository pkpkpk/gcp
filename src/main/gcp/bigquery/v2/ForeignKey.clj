(ns gcp.bigquery.v2.ForeignKey
  (:require [gcp.bigquery.v2.ColumnReference :as ColumnReference]
            [gcp.bigquery.v2.TableId         :as TableId]
            [gcp.global :as global])
  (:import (com.google.cloud.bigquery ForeignKey)))

(defn ^ForeignKey from-edn
  [{:keys [referencedTable columnReferences] :as arg}]
  (global/strict! :bigquery/ForeignKey arg)
  (let [builder (ForeignKey/newBuilder)]
    (some->> (:name arg) (.setName builder))
    (.setReferencedTable builder (TableId/from-edn referencedTable))
    (.setColumnReferences builder (map ColumnReference/from-edn columnReferences))
    (.build builder)))

(defn to-edn [^ForeignKey arg]
  {:post [(global/strict! :bigquery/ForeignKey %)]}
  (cond-> {:referencedTable (TableId/to-edn (.getReferencedTable arg))}
          (some? (.getName arg))
          (assoc :name (.getName arg))
          (pos? (count (.getColumnReferences arg)))
          (assoc :columnReferences (map ColumnReference/to-edn (.getColumnReferences arg)))))