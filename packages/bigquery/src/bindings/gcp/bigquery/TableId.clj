;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.TableId
  {:doc "Google BigQuery Table identity."
   :file-git-sha "5410f6bbd78031621045db8ac9f56dc8ab6bb837"
   :fqcn "com.google.cloud.bigquery.TableId"
   :gcp.dev/certification
     {:base-seed 1790034057502
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034057502 :standard 1790034057503 :stress 1790034057504}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:40:58.577312866Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery TableId]))

(declare from-edn to-edn)

(defn ^TableId from-edn
  [arg]
  (global/strict! :gcp.bigquery/TableId arg)
  (if (get arg :project)
    (TableId/of (get arg :project) (get arg :dataset) (get arg :table))
    (TableId/of (get arg :dataset) (get arg :table))))

(defn to-edn
  [^TableId arg]
  {:post [(global/strict! :gcp.bigquery/TableId %)]}
  (when arg
    (cond-> {:dataset (.getDataset arg), :table (.getTable arg)}
      (.getIAMResourceName arg) (assoc :iAMResourceName
                                  (.getIAMResourceName arg))
      (.getProject arg) (assoc :project (.getProject arg)))))

(def schema
  [:map
   {:closed true,
    :doc "Google BigQuery Table identity.",
    :gcp/category :static-factory,
    :gcp/key :gcp.bigquery/TableId}
   [:dataset {:doc "Returns dataset's user-defined id."}
    [:string {:min 1, :gen/max 1}]]
   [:iAMResourceName
    {:doc "Returns the IAM resource name for the table. *", :optional true}
    [:string {:min 1, :gen/max 1}]]
   [:project {:doc "Returns project's user-defined id.", :optional true}
    [:string {:min 1, :gen/max 1}]]
   [:table {:doc "Returns table's user-defined id."}
    [:string {:min 1, :gen/max 1}]]])

(global/include-registry! "gcp.bigquery.TableId" {:gcp.bigquery/TableId schema})