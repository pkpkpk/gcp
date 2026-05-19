;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.RangePartitioning
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.RangePartitioning"
   :gcp.dev/certification
     {:base-seed 1779204646091
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1779204646091 :standard 1779204646092 :stress 1779204646093}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-05-19T15:30:46.894321247Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery RangePartitioning
            RangePartitioning$Builder RangePartitioning$Range
            RangePartitioning$Range$Builder]))

(declare from-edn to-edn Range-from-edn Range-to-edn)

(defn ^RangePartitioning$Range Range-from-edn
  [arg]
  (let [builder (RangePartitioning$Range/newBuilder)]
    (when (some? (get arg :end)) (.setEnd builder (long (get arg :end))))
    (when (some? (get arg :interval))
      (.setInterval builder (long (get arg :interval))))
    (when (some? (get arg :start)) (.setStart builder (long (get arg :start))))
    (.build builder)))

(defn Range-to-edn
  [^RangePartitioning$Range arg]
  (when arg
    (cond-> {}
      (.getEnd arg) (assoc :end (.getEnd arg))
      (.getInterval arg) (assoc :interval (.getInterval arg))
      (.getStart arg) (assoc :start (.getStart arg)))))

(def Range-schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :nested/accessor-with-builder,
    :gcp/key :gcp.bigquery/RangePartitioning.Range}
   [:end
    {:optional true,
     :getter-doc "Returns the end of range partitioning.",
     :setter-doc
       "[Required] The end of range partitioning, exclusive. The value may be {@code null}."}
    :i64]
   [:interval
    {:optional true,
     :getter-doc "Returns the width of each interval.",
     :setter-doc
       "[Required] The width of each interval. The value may be {@code null}."}
    :i64]
   [:start
    {:optional true,
     :getter-doc "Returns the start of range partitioning.",
     :setter-doc
       "[Required] The start of range partitioning, inclusive. The value may be {@code null}."}
    :i64]])

(defn ^RangePartitioning from-edn
  [arg]
  (global/strict! :gcp.bigquery/RangePartitioning arg)
  (let [builder (RangePartitioning/newBuilder)]
    (when (some? (get arg :range))
      (.setRange builder (Range-from-edn (get arg :range))))
    (.build builder)))

(defn to-edn
  [^RangePartitioning arg]
  {:post [(global/strict! :gcp.bigquery/RangePartitioning %)]}
  (when arg
    (cond-> {}
      (some->> (.getField arg)
               (not= ""))
        (assoc :field (.getField arg))
      (.getRange arg) (assoc :range (Range-to-edn (.getRange arg))))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/RangePartitioning}
   [:field
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the range partitioning field."}
    [:string {:min 1, :gen/max 1}]]
   [:range
    {:optional true,
     :getter-doc "Returns the range of range partitioning.",
     :setter-doc
       "[Required] Defines the ranges for range partitioning.\n\n@param range range or {@code null} for none"}
    [:ref :gcp.bigquery/RangePartitioning.Range]]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/RangePartitioning schema,
              :gcp.bigquery/RangePartitioning.Range Range-schema}
    {:gcp.global/name "gcp.bigquery.RangePartitioning"}))