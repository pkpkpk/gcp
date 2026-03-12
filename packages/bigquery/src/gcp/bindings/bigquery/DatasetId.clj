;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.DatasetId
  {:doc "Google BigQuery Dataset identity."
   :file-git-sha "c3548a2f521b19761c844c0b24fc8caab541aba7"
   :fqcn "com.google.cloud.bigquery.DatasetId"
   :gcp.dev/certification
     {:base-seed 1771383058750
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771383058750 :standard 1771383058751 :stress 1771383058752}
      :protocol-hash
        "32175171656ac95c72455bff25fe019346fb52c916efee805be149692e31cec4"
      :timestamp "2026-02-18T02:50:58.771348043Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery DatasetId]))

(defn ^DatasetId from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/DatasetId arg)
  (if (string? arg)
    (DatasetId/of arg)
    (if (get arg :project)
      (DatasetId/of (get arg :project) (get arg :dataset))
      (DatasetId/of (get arg :dataset)))))

(defn to-edn
  [^DatasetId arg]
  {:post [(global/strict! :gcp.bindings.bigquery/DatasetId %)]}
  (cond-> {:dataset (.getDataset arg)}
    (.getProject arg) (assoc :project (.getProject arg))))

(def schema
  [:or
   {:closed true,
    :doc "Google BigQuery Dataset identity.",
    :gcp/category :static-factory,
    :gcp/key :gcp.bindings.bigquery/DatasetId} [:string {:min 1}]
   [:map {:closed true}
    [:dataset {:doc "Returns dataset's user-defined id."} [:string {:min 1}]]
    [:project {:doc "Returns project's user-defined id.", :optional true}
     [:string {:min 1}]]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/DatasetId schema}
    {:gcp.global/name "gcp.bindings.bigquery.DatasetId"}))