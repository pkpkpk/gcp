;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.TableId
  {:doc "Google BigQuery Table identity."
   :file-git-sha "c3548a2f521b19761c844c0b24fc8caab541aba7"
   :fqcn "com.google.cloud.bigquery.TableId"
   :gcp.dev/certification
     {:base-seed 1771383112743
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771383112743 :standard 1771383112744 :stress 1771383112745}
      :protocol-hash
        "32175171656ac95c72455bff25fe019346fb52c916efee805be149692e31cec4"
      :timestamp "2026-02-18T02:51:52.768566134Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery TableId]))

(defn ^TableId from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/TableId arg)
  (if (get arg :project)
    (TableId/of (get arg :project) (get arg :dataset) (get arg :table))
    (TableId/of (get arg :dataset) (get arg :table))))

(defn to-edn
  [^TableId arg]
  {:post [(global/strict! :gcp.bindings.bigquery/TableId %)]}
  (cond-> {:dataset (.getDataset arg), :table (.getTable arg)}
    (.getIAMResourceName arg) (assoc :iAMResourceName (.getIAMResourceName arg))
    (.getProject arg) (assoc :project (.getProject arg))))

(def schema
  [:map
   {:closed true,
    :doc "Google BigQuery Table identity.",
    :gcp/category :static-factory,
    :gcp/key :gcp.bindings.bigquery/TableId}
   [:dataset {:doc "Returns dataset's user-defined id."} [:string {:min 1}]]
   [:iAMResourceName
    {:doc "Returns the IAM resource name for the table. *", :optional true}
    [:string {:min 1}]]
   [:project {:doc "Returns project's user-defined id.", :optional true}
    [:string {:min 1}]]
   [:table {:doc "Returns table's user-defined id."} [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/TableId schema}
    {:gcp.global/name "gcp.bindings.bigquery.TableId"}))