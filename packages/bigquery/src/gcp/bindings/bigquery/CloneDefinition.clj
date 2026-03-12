;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.CloneDefinition
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.CloneDefinition"
   :gcp.dev/certification
     {:base-seed 1772045139886
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1772045139886 :standard 1772045139887 :stress 1772045139888}
      :protocol-hash
        "b8a5eb17212acb18f49dcb7f15a243eac9b32f54c0f054a99be660a3a25e0315"
      :timestamp "2026-02-25T18:45:39.914368705Z"}}
  (:require [gcp.bindings.bigquery.TableId :as TableId]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery CloneDefinition CloneDefinition$Builder]))

(defn ^CloneDefinition from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/CloneDefinition arg)
  (let [builder (CloneDefinition/newBuilder)]
    (when (some? (get arg :baseTableId))
      (.setBaseTableId builder (TableId/from-edn (get arg :baseTableId))))
    (when (some? (get arg :cloneTime))
      (.setCloneTime builder (get arg :cloneTime)))
    (.build builder)))

(defn to-edn
  [^CloneDefinition arg]
  {:post [(global/strict! :gcp.bindings.bigquery/CloneDefinition %)]}
  (cond-> {}
    (.getBaseTableId arg) (assoc :baseTableId
                            (TableId/to-edn (.getBaseTableId arg)))
    (.getCloneTime arg) (assoc :cloneTime (.getCloneTime arg))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/CloneDefinition}
   [:baseTableId
    {:optional true,
     :setter-doc "Reference describing the ID of the table that was Cloned. *"}
    :gcp.bindings.bigquery/TableId]
   [:cloneTime
    {:optional true,
     :setter-doc
       "The time at which the base table was Cloned. This value is reported in the JSON response\nusing RFC3339 format. *"}
    [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/CloneDefinition schema}
    {:gcp.global/name "gcp.bindings.bigquery.CloneDefinition"}))