;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.DatasetId
  {:doc "Google BigQuery Dataset identity."
   :file-git-sha "5410f6bbd78031621045db8ac9f56dc8ab6bb837"
   :fqcn "com.google.cloud.bigquery.DatasetId"
   :gcp.dev/certification
     {:base-seed 1790034035162
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034035162 :standard 1790034035163 :stress 1790034035164}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:40:36.408249907Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery DatasetId]))

(declare from-edn to-edn)

(defn ^DatasetId from-edn
  [arg]
  (global/strict! :gcp.bigquery/DatasetId arg)
  (if (string? arg)
    (DatasetId/of arg)
    (if (get arg :project)
      (DatasetId/of (get arg :project) (get arg :dataset))
      (DatasetId/of (get arg :dataset)))))

(defn to-edn
  [^DatasetId arg]
  {:post [(global/strict! :gcp.bigquery/DatasetId %)]}
  (when arg
    (cond-> {:dataset (.getDataset arg)}
      (.getProject arg) (assoc :project (.getProject arg)))))

(def schema
  [:or
   {:closed true,
    :doc "Google BigQuery Dataset identity.",
    :gcp/category :static-factory,
    :gcp/key :gcp.bigquery/DatasetId} [:string {:min 1, :gen/max 1}]
   [:map {:closed true}
    [:dataset {:doc "Returns dataset's user-defined id."}
     [:string {:min 1, :gen/max 1}]]
    [:project {:doc "Returns project's user-defined id.", :optional true}
     [:string {:min 1, :gen/max 1}]]]])

(global/include-registry! "gcp.bigquery.DatasetId"
                          {:gcp.bigquery/DatasetId schema})