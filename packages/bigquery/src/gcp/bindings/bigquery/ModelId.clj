;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.ModelId
  {:doc nil
   :file-git-sha "c3548a2f521b19761c844c0b24fc8caab541aba7"
   :fqcn "com.google.cloud.bigquery.ModelId"
   :gcp.dev/certification
     {:base-seed 1770905410013
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1770905410013 :standard 1770905410014 :stress 1770905410015}
      :protocol-hash
        "12be2957e350878c667952b725a3aa0cead5b4ccecfda54c534822e9bb380b94"
      :timestamp "2026-02-12T14:10:10.040508838Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery ModelId]))

(defn ^ModelId from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/ModelId arg)
  (if (get arg :project)
    (ModelId/of (get arg :project) (get arg :dataset) (get arg :model))
    (ModelId/of (get arg :dataset) (get arg :model))))

(defn to-edn
  [^ModelId arg]
  {:post [(global/strict! :gcp.bindings.bigquery/ModelId %)]}
  (cond-> {:dataset (.getDataset arg), :model (.getModel arg)}
    (.getProject arg) (assoc :project (.getProject arg))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :static-factory,
    :gcp/key :gcp.bindings.bigquery/ModelId}
   [:dataset {:doc "Return corresponding dataset ID for this model. *"} :string]
   [:model {:doc "Return corresponding model ID for this model. *"} :string]
   [:project
    {:doc "Return corresponding project ID for this model. *", :optional true}
    :string]])

(global/include-schema-registry! (with-meta {:gcp.bindings.bigquery/ModelId
                                               schema}
                                   {:gcp.global/name (str *ns*)}))