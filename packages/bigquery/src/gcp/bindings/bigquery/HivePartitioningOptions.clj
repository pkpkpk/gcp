;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.HivePartitioningOptions
  {:doc
     "HivePartitioningOptions currently supported types include: AVRO, CSV, JSON, ORC and Parquet."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.HivePartitioningOptions"
   :gcp.dev/certification
     {:base-seed 1771347476647
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771347476647 :standard 1771347476648 :stress 1771347476649}
      :protocol-hash
        "32175171656ac95c72455bff25fe019346fb52c916efee805be149692e31cec4"
      :timestamp "2026-02-17T16:57:56.670072213Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery HivePartitioningOptions
            HivePartitioningOptions$Builder]))

(defn ^HivePartitioningOptions from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/HivePartitioningOptions arg)
  (let [builder (HivePartitioningOptions/newBuilder)]
    (when (some? (get arg :fields))
      (.setFields builder (seq (get arg :fields))))
    (when (some? (get arg :mode)) (.setMode builder (get arg :mode)))
    (when (some? (get arg :requirePartitionFilter))
      (.setRequirePartitionFilter builder (get arg :requirePartitionFilter)))
    (when (some? (get arg :sourceUriPrefix))
      (.setSourceUriPrefix builder (get arg :sourceUriPrefix)))
    (.build builder)))

(defn to-edn
  [^HivePartitioningOptions arg]
  {:post [(global/strict! :gcp.bindings.bigquery/HivePartitioningOptions %)]}
  (cond-> {}
    (.getFields arg) (assoc :fields (seq (.getFields arg)))
    (.getMode arg) (assoc :mode (.getMode arg))
    (.getRequirePartitionFilter arg) (assoc :requirePartitionFilter
                                       (.getRequirePartitionFilter arg))
    (.getSourceUriPrefix arg) (assoc :sourceUriPrefix
                                (.getSourceUriPrefix arg))))

(def schema
  [:map
   {:closed true,
    :doc
      "HivePartitioningOptions currently supported types include: AVRO, CSV, JSON, ORC and Parquet.",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/HivePartitioningOptions}
   [:fields
    {:optional true,
     :setter-doc
       "[Output-only] For permanent external tables, this field is populated with the hive partition\nkeys in the order they were inferred.\n\n<p>The types of the partition keys can be deduced by checking the table schema (which will\ninclude the partition keys). Not every API will populate this field in the output. For\nexample, Tables.Get will populate it, but Tables.List will not contain this field."}
    [:seqable {:min 1} [:string {:min 1}]]]
   [:mode
    {:optional true,
     :setter-doc
       "[Optional] When set, what mode of hive partitioning to use when reading data. Two modes are\nsupported. (1) AUTO: automatically infer partition key name(s) and type(s). (2) STRINGS:\nautomatically infer partition key name(s). All types are interpreted as strings. Not all\nstorage formats support hive partitioning. Requesting hive partitioning on an unsupported\nformat will lead to an error. Currently supported types include: AVRO, CSV, JSON, ORC and\nParquet."}
    [:string {:min 1}]]
   [:requirePartitionFilter
    {:optional true,
     :getter-doc
       "Returns true if a partition filter (that can be used for partition elimination) is required for\nqueries over this table.",
     :setter-doc
       "[Optional] If set to true, queries over this table require a partition filter that can be\nused for partition elimination to be specified. Note that this field should only be true when\ncreating a permanent external table or querying a temporary external table. Hive-partitioned\nloads with requirePartitionFilter explicitly set to true will fail."}
    :boolean]
   [:sourceUriPrefix
    {:optional true,
     :setter-doc
       "[Optional] When hive partition detection is requested, a common prefix for all source uris\nshould be supplied. The prefix must end immediately before the partition key encoding begins.\nFor example, consider files following this data layout.\ngs://bucket/path_to_table/dt=2019-01-01/country=BR/id=7/file.avro\ngs://bucket/path_to_table/dt=2018-12-31/country=CA/id=3/file.avro When hive partitioning is\nrequested with either AUTO or STRINGS detection, the common prefix can be either of\ngs://bucket/path_to_table or gs://bucket/path_to_table/ (trailing slash does not matter)."}
    [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/HivePartitioningOptions schema}
    {:gcp.global/name "gcp.bindings.bigquery.HivePartitioningOptions"}))