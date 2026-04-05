;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.LoadJobConfiguration
  {:doc
     "Google BigQuery load job configuration. A load job loads data from one of several formats into a\ntable. Data is provided as URIs that point to objects in Google Cloud Storage. Load job\nconfigurations have {@link JobConfiguration.Type#LOAD} type."
   :file-git-sha "5cfdf855fa0cf206660fd89743cbaabf3afa75a3"
   :fqcn "com.google.cloud.bigquery.LoadJobConfiguration"
   :gcp.dev/certification
     {:base-seed 1775130923288
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1775130923288 :standard 1775130923289 :stress 1775130923290}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:55:24.918895599Z"}}
  (:require [gcp.bigquery.Clustering :as Clustering]
            [gcp.bigquery.ConnectionProperty :as ConnectionProperty]
            [gcp.bigquery.CsvOptions :as CsvOptions]
            [gcp.bigquery.EncryptionConfiguration :as EncryptionConfiguration]
            [gcp.bigquery.FormatOptions :as FormatOptions]
            [gcp.bigquery.HivePartitioningOptions :as HivePartitioningOptions]
            [gcp.bigquery.RangePartitioning :as RangePartitioning]
            [gcp.bigquery.Schema :as Schema]
            [gcp.bigquery.TableId :as TableId]
            [gcp.bigquery.TimePartitioning :as TimePartitioning]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery JobInfo$CreateDisposition
            JobInfo$SchemaUpdateOption JobInfo$WriteDisposition
            LoadJobConfiguration LoadJobConfiguration$Builder
            LoadJobConfiguration$SourceColumnMatch]))

(declare from-edn to-edn SourceColumnMatch-from-edn SourceColumnMatch-to-edn)

(def SourceColumnMatch-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/string-enum,
    :gcp/key :gcp.bigquery/LoadJobConfiguration.SourceColumnMatch}
   "SOURCE_COLUMN_MATCH_UNSPECIFIED" "POSITION" "NAME"])

(defn ^LoadJobConfiguration from-edn
  [arg]
  (global/strict! :gcp.bigquery/LoadJobConfiguration arg)
  (let [builder (LoadJobConfiguration/newBuilder (TableId/from-edn
                                                   (get arg :destinationTable))
                                                 (seq (get arg :sourceUris)))]
    (when (some? (get arg :autodetect))
      (.setAutodetect builder (get arg :autodetect)))
    (when (some? (get arg :clustering))
      (.setClustering builder (Clustering/from-edn (get arg :clustering))))
    (when (some? (get arg :columnNameCharacterMap))
      (.setColumnNameCharacterMap builder (get arg :columnNameCharacterMap)))
    (when (some? (get arg :connectionProperties))
      (.setConnectionProperties builder
                                (map ConnectionProperty/from-edn
                                  (get arg :connectionProperties))))
    (when (some? (get arg :createDisposition))
      (.setCreateDisposition builder
                             (JobInfo$CreateDisposition/valueOf
                               (get arg :createDisposition))))
    (when (some? (get arg :createSession))
      (.setCreateSession builder (get arg :createSession)))
    (when (some? (get arg :dateFormat))
      (.setDateFormat builder (get arg :dateFormat)))
    (when (some? (get arg :datetimeFormat))
      (.setDatetimeFormat builder (get arg :datetimeFormat)))
    (when (some? (get arg :decimalTargetTypes))
      (.setDecimalTargetTypes builder (seq (get arg :decimalTargetTypes))))
    (when (some? (get arg :destinationEncryptionConfiguration))
      (.setDestinationEncryptionConfiguration
        builder
        (EncryptionConfiguration/from-edn
          (get arg :destinationEncryptionConfiguration))))
    (when (some? (get arg :fileSetSpecType))
      (.setFileSetSpecType builder (get arg :fileSetSpecType)))
    (when (some? (get arg :formatOptions))
      (.setFormatOptions builder
                         (FormatOptions/from-edn (get arg :formatOptions))))
    (when (some? (get arg :hivePartitioningOptions))
      (.setHivePartitioningOptions builder
                                   (HivePartitioningOptions/from-edn
                                     (get arg :hivePartitioningOptions))))
    (when (some? (get arg :ignoreUnknownValues))
      (.setIgnoreUnknownValues builder (get arg :ignoreUnknownValues)))
    (when (some? (get arg :jobTimeoutMs))
      (.setJobTimeoutMs builder (long (get arg :jobTimeoutMs))))
    (when (some? (get arg :labels))
      (.setLabels builder
                  (into {} (map (fn [[k v]] [(name k) v])) (get arg :labels))))
    (when (some? (get arg :maxBadRecords))
      (.setMaxBadRecords builder (int (get arg :maxBadRecords))))
    (when (some? (get arg :nullMarker))
      (.setNullMarker builder (get arg :nullMarker)))
    (when (some? (get arg :nullMarkers))
      (.setNullMarkers builder (seq (get arg :nullMarkers))))
    (when (some? (get arg :rangePartitioning))
      (.setRangePartitioning builder
                             (RangePartitioning/from-edn
                               (get arg :rangePartitioning))))
    (when (some? (get arg :referenceFileSchemaUri))
      (.setReferenceFileSchemaUri builder (get arg :referenceFileSchemaUri)))
    (when (some? (get arg :reservation))
      (.setReservation builder (get arg :reservation)))
    (when (some? (get arg :schema))
      (.setSchema builder (Schema/from-edn (get arg :schema))))
    (when (some? (get arg :schemaUpdateOptions))
      (.setSchemaUpdateOptions builder
                               (map JobInfo$SchemaUpdateOption/valueOf
                                 (get arg :schemaUpdateOptions))))
    (when (some? (get arg :sourceColumnMatch))
      (.setSourceColumnMatch builder
                             (LoadJobConfiguration$SourceColumnMatch/valueOf
                               (get arg :sourceColumnMatch))))
    (when (some? (get arg :timeFormat))
      (.setTimeFormat builder (get arg :timeFormat)))
    (when (some? (get arg :timePartitioning))
      (.setTimePartitioning builder
                            (TimePartitioning/from-edn
                              (get arg :timePartitioning))))
    (when (some? (get arg :timeZone))
      (.setTimeZone builder (get arg :timeZone)))
    (when (some? (get arg :timestampFormat))
      (.setTimestampFormat builder (get arg :timestampFormat)))
    (when (some? (get arg :useAvroLogicalTypes))
      (.setUseAvroLogicalTypes builder (get arg :useAvroLogicalTypes)))
    (when (some? (get arg :writeDisposition))
      (.setWriteDisposition builder
                            (JobInfo$WriteDisposition/valueOf
                              (get arg :writeDisposition))))
    (.build builder)))

(defn to-edn
  [^LoadJobConfiguration arg]
  {:post [(global/strict! :gcp.bigquery/LoadJobConfiguration %)]}
  (when arg
    (cond-> {:destinationTable (TableId/to-edn (.getDestinationTable arg)),
             :sourceUris (seq (.getSourceUris arg)),
             :type "LOAD"}
      (.getAutodetect arg) (assoc :autodetect (.getAutodetect arg))
      (.getClustering arg) (assoc :clustering
                             (Clustering/to-edn (.getClustering arg)))
      (some->> (.getColumnNameCharacterMap arg)
               (not= ""))
        (assoc :columnNameCharacterMap (.getColumnNameCharacterMap arg))
      (seq (.getConnectionProperties arg)) (assoc :connectionProperties
                                             (map ConnectionProperty/to-edn
                                               (.getConnectionProperties arg)))
      (.getCreateDisposition arg) (assoc :createDisposition
                                    (.name (.getCreateDisposition arg)))
      (.getCreateSession arg) (assoc :createSession (.getCreateSession arg))
      (some->> (.getDateFormat arg)
               (not= ""))
        (assoc :dateFormat (.getDateFormat arg))
      (some->> (.getDatetimeFormat arg)
               (not= ""))
        (assoc :datetimeFormat (.getDatetimeFormat arg))
      (seq (.getDecimalTargetTypes arg)) (assoc :decimalTargetTypes
                                           (seq (.getDecimalTargetTypes arg)))
      (.getDestinationEncryptionConfiguration arg)
        (assoc :destinationEncryptionConfiguration
          (EncryptionConfiguration/to-edn
            (.getDestinationEncryptionConfiguration arg)))
      (some->> (.getFileSetSpecType arg)
               (not= ""))
        (assoc :fileSetSpecType (.getFileSetSpecType arg))
      (.getCsvOptions arg) (assoc :formatOptions
                             (CsvOptions/to-edn (.getCsvOptions arg)))
      (.getHivePartitioningOptions arg) (assoc :hivePartitioningOptions
                                          (HivePartitioningOptions/to-edn
                                            (.getHivePartitioningOptions arg)))
      (.ignoreUnknownValues arg) (assoc :ignoreUnknownValues
                                   (.ignoreUnknownValues arg))
      (.getJobTimeoutMs arg) (assoc :jobTimeoutMs (.getJobTimeoutMs arg))
      (seq (.getLabels arg))
        (assoc :labels
          (into {} (map (fn [[k v]] [(keyword k) v])) (.getLabels arg)))
      (.getMaxBadRecords arg) (assoc :maxBadRecords (.getMaxBadRecords arg))
      (some->> (.getNullMarker arg)
               (not= ""))
        (assoc :nullMarker (.getNullMarker arg))
      (seq (.getNullMarkers arg)) (assoc :nullMarkers
                                    (seq (.getNullMarkers arg)))
      (.getRangePartitioning arg) (assoc :rangePartitioning
                                    (RangePartitioning/to-edn
                                      (.getRangePartitioning arg)))
      (some->> (.getReferenceFileSchemaUri arg)
               (not= ""))
        (assoc :referenceFileSchemaUri (.getReferenceFileSchemaUri arg))
      (some->> (.getReservation arg)
               (not= ""))
        (assoc :reservation (.getReservation arg))
      (.getSchema arg) (assoc :schema (Schema/to-edn (.getSchema arg)))
      (seq (.getSchemaUpdateOptions arg)) (assoc :schemaUpdateOptions
                                            (map (fn [e] (.name e))
                                              (.getSchemaUpdateOptions arg)))
      (.getSourceColumnMatch arg) (assoc :sourceColumnMatch
                                    (.name (.getSourceColumnMatch arg)))
      (some->> (.getTimeFormat arg)
               (not= ""))
        (assoc :timeFormat (.getTimeFormat arg))
      (.getTimePartitioning arg) (assoc :timePartitioning
                                   (TimePartitioning/to-edn
                                     (.getTimePartitioning arg)))
      (some->> (.getTimeZone arg)
               (not= ""))
        (assoc :timeZone (.getTimeZone arg))
      (some->> (.getTimestampFormat arg)
               (not= ""))
        (assoc :timestampFormat (.getTimestampFormat arg))
      (.getUseAvroLogicalTypes arg) (assoc :useAvroLogicalTypes
                                      (.getUseAvroLogicalTypes arg))
      (.getWriteDisposition arg) (assoc :writeDisposition
                                   (.name (.getWriteDisposition arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "Google BigQuery load job configuration. A load job loads data from one of several formats into a\ntable. Data is provided as URIs that point to objects in Google Cloud Storage. Load job\nconfigurations have {@link JobConfiguration.Type#LOAD} type.",
    :gcp/category :variant-accessor,
    :gcp/key :gcp.bigquery/LoadJobConfiguration} [:type [:= "LOAD"]]
   [:autodetect {:optional true} :boolean]
   [:clustering {:optional true} :gcp.bigquery/Clustering]
   [:columnNameCharacterMap
    {:optional true,
     :getter-doc
       "Returns the column name character map used in CSV/Parquet loads.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/rest/v2/Job#columnnamecharactermap\">\n    ColumnNameCharacterMap</a>",
     :setter-doc
       "[Optional] Character map supported for column names in CSV/Parquet loads. Defaults to STRICT\nand can be overridden by Project Config Service. Using this option with unsupporting load\nformats will result in an error.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/rest/v2/Job#columnnamecharactermap\">\n    ColumnNameCharacterMap</a>"}
    [:string {:min 1}]]
   [:connectionProperties {:optional true}
    [:sequential {:min 1} :gcp.bigquery/ConnectionProperty]]
   [:createDisposition {:optional true}
    [:enum {:closed true} "CREATE_IF_NEEDED" "CREATE_NEVER"]]
   [:createSession {:optional true} :boolean]
   [:dateFormat
    {:optional true,
     :getter-doc "Returns the format used to parse DATE values.",
     :setter-doc "Date format used for parsing DATE values."}
    [:string {:min 1}]]
   [:datetimeFormat
    {:optional true,
     :getter-doc "Returns the format used to parse DATETIME values.",
     :setter-doc "Date format used for parsing DATETIME values."}
    [:string {:min 1}]]
   [:decimalTargetTypes
    {:optional true,
     :setter-doc
       "Defines the list of possible SQL data types to which the source decimal values are converted.\nThis list and the precision and the scale parameters of the decimal field determine the\ntarget type. In the order of NUMERIC, BIGNUMERIC, and STRING, a type is picked if it is in\nthe specified list and if it supports the precision and the scale. STRING supports all\nprecision and scale values.\n\n@param decimalTargetTypes decimalTargetType or {@code null} for none"}
    [:sequential {:min 1} [:string {:min 1}]]]
   [:destinationEncryptionConfiguration {:optional true}
    :gcp.bigquery/EncryptionConfiguration]
   [:destinationTable {:getter-doc nil} :gcp.bigquery/TableId]
   [:fileSetSpecType
    {:optional true,
     :setter-doc
       "Defines how to interpret files denoted by URIs. By default the files are assumed to be data\nfiles (this can be specified explicitly via FILE_SET_SPEC_TYPE_FILE_SYSTEM_MATCH). A second\noption is \"FILE_SET_SPEC_TYPE_NEW_LINE_DELIMITED_MANIFEST\" which interprets each file as a\nmanifest file, where each line is a reference to a file."}
    [:string {:min 1}]]
   [:formatOptions {:optional true} :gcp.bigquery/CsvOptions]
   [:hivePartitioningOptions {:optional true}
    :gcp.bigquery/HivePartitioningOptions]
   [:ignoreUnknownValues {:optional true} :boolean]
   [:jobTimeoutMs
    {:optional true,
     :getter-doc "Returns the timeout associated with this job",
     :setter-doc
       "[Optional] Job timeout in milliseconds. If this time limit is exceeded, BigQuery may attempt\nto terminate the job.\n\n@param jobTimeoutMs jobTimeoutMs or {@code null} for none"}
    :i64]
   [:labels
    {:optional true,
     :getter-doc "Returns the labels associated with this job",
     :setter-doc
       "The labels associated with this job. You can use these to organize and group your jobs. Label\nkeys and values can be no longer than 63 characters, can only contain lowercase letters,\nnumeric characters, underscores and dashes. International characters are allowed. Label\nvalues are optional. Label keys must start with a letter and each label in the list must have\na different key.\n\n@param labels labels or {@code null} for none"}
    [:map-of [:or simple-keyword? [:string {:min 1}]] [:string {:min 1}]]]
   [:maxBadRecords {:optional true} :i32]
   [:nullMarker {:optional true} [:string {:min 1}]]
   [:nullMarkers
    {:optional true,
     :getter-doc
       "Returns a list of strings represented as SQL NULL value in a CSV file.",
     :setter-doc
       "A list of strings represented as SQL NULL value in a CSV file. null_marker and null_markers\ncan't be set at the same time. If null_marker is set, null_markers has to be not set. If\nnull_markers is set, null_marker has to be not set. If both null_marker and null_markers are\nset at the same time, a user error would be thrown. Any strings listed in null_markers,\nincluding empty string would be interpreted as SQL NULL. This applies to all column types."}
    [:sequential {:min 1} [:string {:min 1}]]]
   [:rangePartitioning
    {:optional true,
     :getter-doc "Returns the range partitioning specification for the table",
     :setter-doc
       "Range partitioning specification for this table. Only one of timePartitioning and\nrangePartitioning should be specified.\n\n@param rangePartitioning rangePartitioning or {@code null} for none"}
    :gcp.bigquery/RangePartitioning]
   [:referenceFileSchemaUri
    {:optional true,
     :setter-doc
       "When creating an external table, the user can provide a reference file with the table schema.\nThis is enabled for the following formats: AVRO, PARQUET, ORC.\n\n@param referenceFileSchemaUri or {@code null} for none"}
    [:string {:min 1}]]
   [:reservation
    {:optional true,
     :getter-doc "Returns the reservation associated with this job",
     :setter-doc
       "[Optional] The reservation that job would use. User can specify a reservation to execute the\njob. If reservation is not set, reservation is determined based on the rules defined by the\nreservation assignments. The expected format is\n`projects/{project}/locations/{location}/reservations/{reservation}`.\n\n@param reservation reservation or {@code null} for none"}
    [:string {:min 1}]] [:schema {:optional true} :gcp.bigquery/Schema]
   [:schemaUpdateOptions {:optional true}
    [:sequential {:min 1}
     [:enum {:closed true} "ALLOW_FIELD_ADDITION" "ALLOW_FIELD_RELAXATION"]]]
   [:sourceColumnMatch
    {:optional true,
     :getter-doc
       "Returns the strategy used to match loaded columns to the schema, either POSITION or NAME.",
     :setter-doc
       "Controls the strategy used to match loaded columns to the schema. If not set, a sensible\ndefault is chosen based on how the schema is provided. If autodetect is used, then columns\nare matched by name. Otherwise, columns are matched by position. This is done to keep the\nbehavior backward-compatible."}
    [:enum {:closed true} "SOURCE_COLUMN_MATCH_UNSPECIFIED" "POSITION" "NAME"]]
   [:sourceUris
    {:getter-doc
       "Returns the fully-qualified URIs that point to source data in Google Cloud Storage (e.g.\ngs://bucket/path). Each URI can contain one '*' wildcard character and it must come after the\n'bucket' name."}
    [:sequential {:min 1} [:string {:min 1}]]]
   [:timeFormat
    {:optional true,
     :getter-doc "Returns the format used to parse TIME values.",
     :setter-doc "Date format used for parsing TIME values."}
    [:string {:min 1}]]
   [:timePartitioning {:optional true} :gcp.bigquery/TimePartitioning]
   [:timeZone
    {:optional true,
     :getter-doc
       "Returns the time zone used when parsing timestamp values that don't have specific time zone\ninformation.",
     :setter-doc
       "[Experimental] Default time zone that will apply when parsing timestamp values that have no\nspecific time zone."}
    [:string {:min 1}]]
   [:timestampFormat
    {:optional true,
     :getter-doc "Returns the format used to parse TIMESTAMP values.",
     :setter-doc "Date format used for parsing TIMESTAMP values."}
    [:string {:min 1}]] [:useAvroLogicalTypes {:optional true} :boolean]
   [:writeDisposition {:optional true}
    [:enum {:closed true} "WRITE_TRUNCATE" "WRITE_TRUNCATE_DATA" "WRITE_APPEND"
     "WRITE_EMPTY"]]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/LoadJobConfiguration schema,
              :gcp.bigquery/LoadJobConfiguration.SourceColumnMatch
                SourceColumnMatch-schema}
    {:gcp.global/name "gcp.bigquery.LoadJobConfiguration"}))