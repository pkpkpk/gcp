(ns gcp.bigquery.v2.ColumnReference
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery ColumnReference)))

(defn ^ColumnReference from-edn
  [{:keys [referencedColumn referencingColumn] :as arg}]
  (global/strict! :gcp.bigquery.v2/ColumnReference arg)
  (let [builder (ColumnReference/newBuilder)]
    (.setReferencedColumn builder referencedColumn)
    (.setReferencingColumn builder referencingColumn)
    (.build builder)))

(defn to-edn [^ColumnReference arg]
  {:post [(global/strict! :gcp.bigquery.v2/ColumnReference %)]}
  {:referencedColumn (.getReferencedColumn arg)
   :referencingColumn (.getReferencingColumn arg)})

(def schemas
  {:gcp.bigquery.v2/ColumnReference
   [:map {:closed true}
    [:referencingColumn {:doc "The source column of this reference"} :string]
    [:referencedColumn {:doc "The target column of this reference"} :string]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))