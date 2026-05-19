;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.TableId
  {:doc "Google BigQuery Table identity."
   :file-git-sha "c3548a2f521b19761c844c0b24fc8caab541aba7"
   :fqcn "com.google.cloud.bigquery.TableId"
   :gcp.dev/certification
     {:base-seed 1779204639671
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1779204639671 :standard 1779204639672 :stress 1779204639673}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-05-19T15:30:40.437329686Z"}}
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

(global/include-schema-registry! (with-meta {:gcp.bigquery/TableId schema}
                                   {:gcp.global/name "gcp.bigquery.TableId"}))