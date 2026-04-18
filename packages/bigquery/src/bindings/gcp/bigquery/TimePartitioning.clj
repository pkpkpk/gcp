;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.TimePartitioning
  {:doc
     "Objects of this class allow to configure table partitioning based on time. By dividing a large\ntable into smaller partitions, you can improve query performance and reduce the number of bytes\nbilled by restricting the amount of data scanned.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/partitioned-tables\">Partitioned Tables</a>"
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.TimePartitioning"
   :gcp.dev/certification
     {:base-seed 1776499356727
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1776499356727 :standard 1776499356728 :stress 1776499356729}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:02:38.225221057Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery TimePartitioning TimePartitioning$Builder
            TimePartitioning$Type]))

(declare from-edn to-edn Type-from-edn Type-to-edn)

(def Type-schema
  [:enum
   {:closed true,
    :doc
      "[Optional] The supported types are DAY, HOUR, MONTH, and YEAR, which will generate one\npartition per day, hour, month, and year, respectively. When the interval is not specified, the\ndefault behavior is DAY.",
    :gcp/category :nested/enum,
    :gcp/key :gcp.bigquery/TimePartitioning.Type} "DAY" "HOUR" "MONTH" "YEAR"])

(defn ^TimePartitioning from-edn
  [arg]
  (global/strict! :gcp.bigquery/TimePartitioning arg)
  (let [builder (TimePartitioning/newBuilder (TimePartitioning$Type/valueOf
                                               (get arg :type)))]
    (when (some? (get arg :expirationMs))
      (.setExpirationMs builder (long (get arg :expirationMs))))
    (when (some? (get arg :requirePartitionFilter))
      (.setRequirePartitionFilter builder (get arg :requirePartitionFilter)))
    (.build builder)))

(defn to-edn
  [^TimePartitioning arg]
  {:post [(global/strict! :gcp.bigquery/TimePartitioning %)]}
  (when arg
    (cond-> {:type (.name (.getType arg))}
      (.getExpirationMs arg) (assoc :expirationMs (.getExpirationMs arg))
      (some->> (.getField arg)
               (not= ""))
        (assoc :field (.getField arg))
      (.getRequirePartitionFilter arg) (assoc :requirePartitionFilter
                                         (.getRequirePartitionFilter arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Objects of this class allow to configure table partitioning based on time. By dividing a large\ntable into smaller partitions, you can improve query performance and reduce the number of bytes\nbilled by restricting the amount of data scanned.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/partitioned-tables\">Partitioned Tables</a>",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/TimePartitioning}
   [:expirationMs
    {:optional true,
     :getter-doc
       "Returns the number of milliseconds for which to keep the storage for a partition. When expired,\nthe storage for the partition is reclaimed. If null, the partion does not expire.",
     :setter-doc nil} :i64]
   [:field
    {:optional true,
     :read-only? true,
     :getter-doc
       "If not set, the table is partitioned by pseudo column '_PARTITIONTIME'; if set, the table is\npartitioned by this field."}
    [:string {:min 1}]]
   [:requirePartitionFilter
    {:optional true,
     :getter-doc
       "If set to true, queries over this table require a partition filter (that can be used for\npartition elimination) to be specified.",
     :setter-doc nil} :boolean]
   [:type {:getter-doc "Returns the time partitioning type."}
    [:enum {:closed true} "DAY" "HOUR" "MONTH" "YEAR"]]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/TimePartitioning schema,
              :gcp.bigquery/TimePartitioning.Type Type-schema}
    {:gcp.global/name "gcp.bigquery.TimePartitioning"}))