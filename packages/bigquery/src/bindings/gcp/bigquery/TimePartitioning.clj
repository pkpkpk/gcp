;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.TimePartitioning
  {:doc
     "Objects of this class allow to configure table partitioning based on time. By dividing a large\ntable into smaller partitions, you can improve query performance and reduce the number of bytes\nbilled by restricting the amount of data scanned.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/partitioned-tables\">Partitioned Tables</a>"
   :file-git-sha "a738d198515acf729b157fc86d6cd124781e830e"
   :fqcn "com.google.cloud.bigquery.TimePartitioning"
   :gcp.dev/certification
     {:base-seed 1790034066301
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034066301 :standard 1790034066302 :stress 1790034066303}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:41:07.404372731Z"}}
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
    [:string {:min 1, :gen/max 1}]]
   [:requirePartitionFilter
    {:optional true,
     :getter-doc
       "If set to true, queries over this table require a partition filter (that can be used for\npartition elimination) to be specified.",
     :setter-doc nil} :boolean]
   [:type {:getter-doc "Returns the time partitioning type."}
    [:enum {:closed true} "DAY" "HOUR" "MONTH" "YEAR"]]])

(global/include-registry! "gcp.bigquery.TimePartitioning"
                          {:gcp.bigquery/TimePartitioning schema,
                           :gcp.bigquery/TimePartitioning.Type Type-schema})