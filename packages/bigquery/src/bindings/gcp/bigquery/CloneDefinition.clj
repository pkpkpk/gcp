;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.CloneDefinition
  {:doc nil
   :file-git-sha "48ef654bca49c82c24b8bddcda0422691c03d230"
   :fqcn "com.google.cloud.bigquery.CloneDefinition"
   :gcp.dev/certification
     {:base-seed 1790034206127
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034206127 :standard 1790034206128 :stress 1790034206129}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:43:27.182984559Z"}}
  (:require [gcp.bigquery.TableId :as TableId]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery CloneDefinition CloneDefinition$Builder]))

(declare from-edn to-edn)

(defn ^CloneDefinition from-edn
  [arg]
  (global/strict! :gcp.bigquery/CloneDefinition arg)
  (let [builder (CloneDefinition/newBuilder)]
    (when (some? (get arg :baseTableId))
      (.setBaseTableId builder (TableId/from-edn (get arg :baseTableId))))
    (when (some? (get arg :cloneTime))
      (.setCloneTime builder (get arg :cloneTime)))
    (.build builder)))

(defn to-edn
  [^CloneDefinition arg]
  {:post [(global/strict! :gcp.bigquery/CloneDefinition %)]}
  (when arg
    (cond-> {}
      (.getBaseTableId arg) (assoc :baseTableId
                              (TableId/to-edn (.getBaseTableId arg)))
      (some->> (.getCloneTime arg)
               (not= ""))
        (assoc :cloneTime (.getCloneTime arg)))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/CloneDefinition}
   [:baseTableId
    {:optional true,
     :setter-doc "Reference describing the ID of the table that was Cloned. *"}
    :gcp.bigquery/TableId]
   [:cloneTime
    {:optional true,
     :setter-doc
       "The time at which the base table was Cloned. This value is reported in the JSON response\nusing RFC3339 format. *"}
    [:string {:min 1, :gen/max 1}]]])

(global/include-registry! "gcp.bigquery.CloneDefinition"
                          {:gcp.bigquery/CloneDefinition schema})