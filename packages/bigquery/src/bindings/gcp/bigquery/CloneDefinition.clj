;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.CloneDefinition
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.CloneDefinition"
   :gcp.dev/certification
     {:base-seed 1776499515196
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1776499515196 :standard 1776499515197 :stress 1776499515198}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:05:16.620522991Z"}}
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
    [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/CloneDefinition schema}
    {:gcp.global/name "gcp.bigquery.CloneDefinition"}))