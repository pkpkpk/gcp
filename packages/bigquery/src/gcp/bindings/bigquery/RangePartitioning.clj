;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.RangePartitioning
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.RangePartitioning"
   :gcp.dev/certification
     {:base-seed 1771291552498
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771291552498 :standard 1771291552499 :stress 1771291552500}
      :protocol-hash
        "7068af39aa0d55cb4d0e4eaceead6fd12f374863b361a9717f08a69d4bd12910"
      :timestamp "2026-02-17T01:25:52.538286273Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery RangePartitioning
            RangePartitioning$Builder RangePartitioning$Range
            RangePartitioning$Builder]))

(declare RangePartitioning$Range-from-edn RangePartitioning$Range-to-edn)

(defn ^RangePartitioning$Range RangePartitioning$Range-from-edn
  [arg]
  (let [builder (RangePartitioning$Range/newBuilder)]
    (when (some? (get arg :end)) (.setEnd builder (get arg :end)))
    (when (some? (get arg :interval))
      (.setInterval builder (get arg :interval)))
    (when (some? (get arg :start)) (.setStart builder (get arg :start)))
    (.build builder)))

(defn RangePartitioning$Range-to-edn
  [^RangePartitioning$Range arg]
  (cond-> {}
    (.getEnd arg) (assoc :end (.getEnd arg))
    (.getInterval arg) (assoc :interval (.getInterval arg))
    (.getStart arg) (assoc :start (.getStart arg))))

(def RangePartitioning$Range-schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :nested/accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/RangePartitioning.Range}
   [:end
    {:optional true,
     :getter-doc "Returns the end of range partitioning.",
     :setter-doc
       "[Required] The end of range partitioning, exclusive. The value may be {@code null}."}
    :int]
   [:interval
    {:optional true,
     :getter-doc "Returns the width of each interval.",
     :setter-doc
       "[Required] The width of each interval. The value may be {@code null}."}
    :int]
   [:start
    {:optional true,
     :getter-doc "Returns the start of range partitioning.",
     :setter-doc
       "[Required] The start of range partitioning, inclusive. The value may be {@code null}."}
    :int]])

(defn ^RangePartitioning from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/RangePartitioning arg)
  (let [builder (RangePartitioning/newBuilder)]
    (when (some? (get arg :field)) (.setField builder (get arg :field)))
    (when (some? (get arg :range))
      (.setRange builder (RangePartitioning$Range-from-edn (get arg :range))))
    (.build builder)))

(defn to-edn
  [^RangePartitioning arg]
  {:post [(global/strict! :gcp.bindings.bigquery/RangePartitioning %)]}
  (cond-> {}
    (.getField arg) (assoc :field (.getField arg))
    (.getRange arg) (assoc :range
                      (RangePartitioning$Range-to-edn (.getRange arg)))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/RangePartitioning}
   [:field
    {:optional true,
     :getter-doc "Returns the range partitioning field.",
     :setter-doc
       "[Required] The table is partitioned by this field. The field must be a top- level\nNULLABLE/REQUIRED field. The only supported type is INTEGER/INT64.\n\n@param field field or {@code null} for none"}
    [:string {:min 1}]]
   [:range
    {:optional true,
     :getter-doc "Returns the range of range partitioning.",
     :setter-doc
       "[Required] Defines the ranges for range partitioning.\n\n@param range range or {@code null} for none"}
    :gcp.bindings.bigquery/RangePartitioning.Range]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/RangePartitioning schema,
              :gcp.bindings.bigquery/RangePartitioning.Range
                RangePartitioning$Range-schema}
    {:gcp.global/name "gcp.bindings.bigquery.RangePartitioning"}))