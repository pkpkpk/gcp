(ns gcp.bigquery.v2.ColumnReference
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery ColumnReference)))

(defn ^ColumnReference from-edn
  [{:keys [referencedColumn referencingColumn] :as arg}]
  (global/strict! :gcp/bigquery.ColumnReference arg)
  (let [builder (ColumnReference/newBuilder)]
    (.setReferencedColumn builder referencedColumn)
    (.setReferencingColumn builder referencingColumn)
    (.build builder)))

(defn to-edn [^ColumnReference arg]
  {:post [(global/strict! :gcp/bigquery.ColumnReference %)]}
  {:referencedColumn (.getReferencedColumn arg)
   :referencingColumn (.getReferencingColumn arg)})