;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.ModelId
  {:doc nil
   :file-git-sha "5410f6bbd78031621045db8ac9f56dc8ab6bb837"
   :fqcn "com.google.cloud.bigquery.ModelId"
   :gcp.dev/certification
     {:base-seed 1790034109281
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034109281 :standard 1790034109282 :stress 1790034109283}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:41:50.301038131Z"}}
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
    [:string {:min 1, :gen/max 1}]]
   [:model {:doc "Return corresponding model ID for this model. *"}
    [:string {:min 1, :gen/max 1}]]
   [:project
    {:doc "Return corresponding project ID for this model. *", :optional true}
    [:string {:min 1, :gen/max 1}]]])

(global/include-registry! "gcp.bigquery.ModelId" {:gcp.bigquery/ModelId schema})