(ns gcp.bigquery.v2.PrimaryKey
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery PrimaryKey)))

(defn ^PrimaryKey from-edn [{:keys [columns]}]
  (let [builder (PrimaryKey/newBuilder)]
    (.setColumns builder (seq columns))
    (.build builder)))

(defn to-edn [^PrimaryKey arg]
  {:columns (vec (.getColumns arg))})

(def schemas
  {:gcp.bigquery.v2/PrimaryKey
   [:map {:closed true}
    [:columns [:sequential {:min 1} :string]]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))