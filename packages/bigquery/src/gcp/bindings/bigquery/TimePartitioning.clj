;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.TimePartitioning
  {:doc
     "Objects of this class allow to configure table partitioning based on time. By dividing a large\ntable into smaller partitions, you can improve query performance and reduce the number of bytes\nbilled by restricting the amount of data scanned.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/partitioned-tables\">Partitioned Tables</a>"
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.TimePartitioning"
   :gcp.dev/certification
     {:base-seed 1771291880119
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771291880119 :standard 1771291880120 :stress 1771291880121}
      :protocol-hash
        "7068af39aa0d55cb4d0e4eaceead6fd12f374863b361a9717f08a69d4bd12910"
      :timestamp "2026-02-17T01:31:20.135750327Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery TimePartitioning TimePartitioning$Type
            TimePartitioning$Builder]))

(declare TimePartitioning$Type-from-edn TimePartitioning$Type-to-edn)

(defn ^TimePartitioning$Type TimePartitioning$Type-from-edn
  [arg]
  (TimePartitioning$Type/valueOf arg))

(defn TimePartitioning$Type-to-edn
  [^TimePartitioning$Type arg]
  (.name arg))

(def TimePartitioning$Type-schema
  [:enum
   {:closed true,
    :doc
      "[Optional] The supported types are DAY, HOUR, MONTH, and YEAR, which will generate one\npartition per day, hour, month, and year, respectively. When the interval is not specified, the\ndefault behavior is DAY.",
    :gcp/category :nested/enum,
    :gcp/key :gcp.bindings.bigquery/TimePartitioning.Type} "DAY" "HOUR" "MONTH"
   "YEAR"])

(defn ^TimePartitioning from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/TimePartitioning arg)
  (let [builder (TimePartitioning/newBuilder (TimePartitioning$Type-from-edn
                                               (get arg :type)))]
    (when (some? (get arg :expirationMs))
      (.setExpirationMs builder (get arg :expirationMs)))
    (when (some? (get arg :field)) (.setField builder (get arg :field)))
    (when (some? (get arg :requirePartitionFilter))
      (.setRequirePartitionFilter builder (get arg :requirePartitionFilter)))
    (.build builder)))

(defn to-edn
  [^TimePartitioning arg]
  {:post [(global/strict! :gcp.bindings.bigquery/TimePartitioning %)]}
  (cond-> {:type (TimePartitioning$Type-to-edn (.getType arg))}
    (.getExpirationMs arg) (assoc :expirationMs (.getExpirationMs arg))
    (.getField arg) (assoc :field (.getField arg))
    (.getRequirePartitionFilter arg) (assoc :requirePartitionFilter
                                       (.getRequirePartitionFilter arg))))

(def schema
  [:map
   {:closed true,
    :doc
      "Objects of this class allow to configure table partitioning based on time. By dividing a large\ntable into smaller partitions, you can improve query performance and reduce the number of bytes\nbilled by restricting the amount of data scanned.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/partitioned-tables\">Partitioned Tables</a>",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/TimePartitioning}
   [:expirationMs
    {:optional true,
     :getter-doc
       "Returns the number of milliseconds for which to keep the storage for a partition. When expired,\nthe storage for the partition is reclaimed. If null, the partion does not expire."}
    :int]
   [:field
    {:optional true,
     :getter-doc
       "If not set, the table is partitioned by pseudo column '_PARTITIONTIME'; if set, the table is\npartitioned by this field."}
    [:string {:min 1}]]
   [:requirePartitionFilter
    {:optional true,
     :getter-doc
       "If set to true, queries over this table require a partition filter (that can be used for\npartition elimination) to be specified."}
    :boolean]
   [:type {:getter-doc "Returns the time partitioning type."}
    :gcp.bindings.bigquery/TimePartitioning.Type]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/TimePartitioning schema,
              :gcp.bindings.bigquery/TimePartitioning.Type
                TimePartitioning$Type-schema}
    {:gcp.global/name "gcp.bindings.bigquery.TimePartitioning"}))