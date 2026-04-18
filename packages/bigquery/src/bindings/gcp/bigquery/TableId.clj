;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.TableId
  {:doc "Google BigQuery Table identity."
   :file-git-sha "c3548a2f521b19761c844c0b24fc8caab541aba7"
   :fqcn "com.google.cloud.bigquery.TableId"
   :gcp.dev/certification
     {:base-seed 1776499346635
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1776499346635 :standard 1776499346636 :stress 1776499346637}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:02:28.138041191Z"}}
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
   [:dataset {:doc "Returns dataset's user-defined id."} [:string {:min 1}]]
   [:iAMResourceName
    {:doc "Returns the IAM resource name for the table. *", :optional true}
    [:string {:min 1}]]
   [:project {:doc "Returns project's user-defined id.", :optional true}
    [:string {:min 1}]]
   [:table {:doc "Returns table's user-defined id."} [:string {:min 1}]]])

(global/include-schema-registry! (with-meta {:gcp.bigquery/TableId schema}
                                   {:gcp.global/name "gcp.bigquery.TableId"}))