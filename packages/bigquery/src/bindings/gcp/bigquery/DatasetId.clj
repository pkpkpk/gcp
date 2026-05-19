;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.DatasetId
  {:doc "Google BigQuery Dataset identity."
   :file-git-sha "c3548a2f521b19761c844c0b24fc8caab541aba7"
   :fqcn "com.google.cloud.bigquery.DatasetId"
   :gcp.dev/certification
     {:base-seed 1779204625752
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1779204625752 :standard 1779204625753 :stress 1779204625754}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-05-19T15:30:26.541867404Z"}}
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

(global/include-schema-registry! (with-meta {:gcp.bigquery/DatasetId schema}
                                   {:gcp.global/name "gcp.bigquery.DatasetId"}))