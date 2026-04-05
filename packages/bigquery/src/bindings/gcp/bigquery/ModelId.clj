;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.ModelId
  {:doc nil
   :file-git-sha "c3548a2f521b19761c844c0b24fc8caab541aba7"
   :fqcn "com.google.cloud.bigquery.ModelId"
   :gcp.dev/certification
     {:base-seed 1775130920083
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1775130920083 :standard 1775130920084 :stress 1775130920085}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:55:21.177734979Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery ModelId]))

(declare from-edn to-edn)

(defn ^ModelId from-edn
  [arg]
  (global/strict! :gcp.bigquery/ModelId arg)
  (if (get arg :project)
    (ModelId/of (get arg :project) (get arg :dataset) (get arg :model))
    (ModelId/of (get arg :dataset) (get arg :model))))

(defn to-edn
  [^ModelId arg]
  {:post [(global/strict! :gcp.bigquery/ModelId %)]}
  (when arg
    (cond-> {:dataset (.getDataset arg), :model (.getModel arg)}
      (.getProject arg) (assoc :project (.getProject arg)))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :static-factory,
    :gcp/key :gcp.bigquery/ModelId}
   [:dataset {:doc "Return corresponding dataset ID for this model. *"}
    [:string {:min 1}]]
   [:model {:doc "Return corresponding model ID for this model. *"}
    [:string {:min 1}]]
   [:project
    {:doc "Return corresponding project ID for this model. *", :optional true}
    [:string {:min 1}]]])

(global/include-schema-registry! (with-meta {:gcp.bigquery/ModelId schema}
                                   {:gcp.global/name "gcp.bigquery.ModelId"}))