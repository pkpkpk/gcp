;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.ExternalTableDefinition
  {:doc
     "Google BigQuery external table definition. BigQuery's external tables are tables whose data\nreside outside of BigQuery but can be queried as normal BigQuery tables. External tables are\nexperimental and might be subject to change or removed.\n\n@see <a href=\"https://cloud.google.com/bigquery/federated-data-sources\">Federated Data Sources\n    </a>"
   :file-git-sha "e5467c917c63ac066edcbcd902cc2093a39971a3"
   :fqcn "com.google.cloud.bigquery.ExternalTableDefinition"
   :gcp.dev/certification
     {:base-seed 1771352708953
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771352708953 :standard 1771352708954 :stress 1771352708955}
      :protocol-hash
        "32175171656ac95c72455bff25fe019346fb52c916efee805be149692e31cec4"
      :timestamp "2026-02-17T18:25:32.488874835Z"}}
  (:require [gcp.bindings.bigquery.FormatOptions :as FormatOptions]
            [gcp.bindings.bigquery.HivePartitioningOptions :as HivePartitioningOptions]
            [gcp.bindings.bigquery.Schema :as Schema]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery ExternalTableDefinition
            ExternalTableDefinition$SourceColumnMatch
            ExternalTableDefinition$Builder]))

(declare ExternalTableDefinition$SourceColumnMatch-from-edn
         ExternalTableDefinition$SourceColumnMatch-to-edn)

(defn
  ^ExternalTableDefinition$SourceColumnMatch ExternalTableDefinition$SourceColumnMatch-from-edn
  [arg]
  (ExternalTableDefinition$SourceColumnMatch/valueOf arg))

(defn
  ExternalTableDefinition$SourceColumnMatch-to-edn
  [^ExternalTableDefinition$SourceColumnMatch arg]
  (.name arg))

(def ExternalTableDefinition$SourceColumnMatch-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/string-enum,
    :gcp/key :gcp.bindings.bigquery/ExternalTableDefinition.SourceColumnMatch}
   "POSITION" "NAME"])

(defn ^ExternalTableDefinition from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/ExternalTableDefinition arg)
  (let [builder (ExternalTableDefinition/newBuilder
                  (seq (get arg :sourceUris))
                  (Schema/from-edn (get arg :schema))
                  (FormatOptions/from-edn (get arg :formatOptions)))]
    (when (some? (get arg :autodetect))
      (.setAutodetect builder (get arg :autodetect)))
    (when (some? (get arg :compression))
      (.setCompression builder (get arg :compression)))
    (when (some? (get arg :connectionId))
      (.setConnectionId builder (get arg :connectionId)))
    (when (some? (get arg :dateFormat))
      (.setDateFormat builder (get arg :dateFormat)))
    (when (some? (get arg :datetimeFormat))
      (.setDatetimeFormat builder (get arg :datetimeFormat)))
    (when (some? (get arg :decimalTargetTypes))
      (.setDecimalTargetTypes builder (seq (get arg :decimalTargetTypes))))
    (when (some? (get arg :fileSetSpecType))
      (.setFileSetSpecType builder (get arg :fileSetSpecType)))
    (when (some? (get arg :hivePartitioningOptions))
      (.setHivePartitioningOptions builder
                                   (HivePartitioningOptions/from-edn
                                     (get arg :hivePartitioningOptions))))
    (when (some? (get arg :ignoreUnknownValues))
      (.setIgnoreUnknownValues builder (get arg :ignoreUnknownValues)))
    (when (some? (get arg :maxBadRecords))
      (.setMaxBadRecords builder (int (get arg :maxBadRecords))))
    (when (some? (get arg :maxStaleness))
      (.setMaxStaleness builder (get arg :maxStaleness)))
    (when (some? (get arg :metadataCacheMode))
      (.setMetadataCacheMode builder (get arg :metadataCacheMode)))
    (when (some? (get arg :nullMarkers))
      (.setNullMarkers builder (seq (get arg :nullMarkers))))
    (when (some? (get arg :objectMetadata))
      (.setObjectMetadata builder (get arg :objectMetadata)))
    (when (some? (get arg :referenceFileSchemaUri))
      (.setReferenceFileSchemaUri builder (get arg :referenceFileSchemaUri)))
    (when (some? (get arg :sourceColumnMatch))
      (.setSourceColumnMatch builder
                             (ExternalTableDefinition$SourceColumnMatch-from-edn
                               (get arg :sourceColumnMatch))))
    (when (some? (get arg :timeFormat))
      (.setTimeFormat builder (get arg :timeFormat)))
    (when (some? (get arg :timeZone))
      (.setTimeZone builder (get arg :timeZone)))
    (when (some? (get arg :timestampFormat))
      (.setTimestampFormat builder (get arg :timestampFormat)))
    (.build builder)))

(defn to-edn
  [^ExternalTableDefinition arg]
  {:post [(global/strict! :gcp.bindings.bigquery/ExternalTableDefinition %)]}
  (cond-> {:formatOptions (FormatOptions/to-edn (.getFormatOptions arg)),
           :schema (Schema/to-edn (.getSchema arg)),
           :sourceUris (seq (.getSourceUris arg)),
           :type "EXTERNAL"}
    (.getAutodetect arg) (assoc :autodetect (.getAutodetect arg))
    (.getCompression arg) (assoc :compression (.getCompression arg))
    (.getConnectionId arg) (assoc :connectionId (.getConnectionId arg))
    (.getDateFormat arg) (assoc :dateFormat (.getDateFormat arg))
    (.getDatetimeFormat arg) (assoc :datetimeFormat (.getDatetimeFormat arg))
    (.getDecimalTargetTypes arg) (assoc :decimalTargetTypes
                                   (seq (.getDecimalTargetTypes arg)))
    (.getFileSetSpecType arg) (assoc :fileSetSpecType (.getFileSetSpecType arg))
    (.getHivePartitioningOptions arg) (assoc :hivePartitioningOptions
                                        (HivePartitioningOptions/to-edn
                                          (.getHivePartitioningOptions arg)))
    (.getIgnoreUnknownValues arg) (assoc :ignoreUnknownValues
                                    (.getIgnoreUnknownValues arg))
    (.getMaxBadRecords arg) (assoc :maxBadRecords (.getMaxBadRecords arg))
    (.getMaxStaleness arg) (assoc :maxStaleness (.getMaxStaleness arg))
    (.getMetadataCacheMode arg) (assoc :metadataCacheMode
                                  (.getMetadataCacheMode arg))
    (.getNullMarkers arg) (assoc :nullMarkers (seq (.getNullMarkers arg)))
    (.getObjectMetadata arg) (assoc :objectMetadata (.getObjectMetadata arg))
    (.getReferenceFileSchemaUri arg) (assoc :referenceFileSchemaUri
                                       (.getReferenceFileSchemaUri arg))
    (.getSourceColumnMatch arg)
      (assoc :sourceColumnMatch
        (ExternalTableDefinition$SourceColumnMatch-to-edn (.getSourceColumnMatch
                                                            arg)))
    (.getSourceUrisImmut arg) (assoc :sourceUrisImmut
                                (seq (.getSourceUrisImmut arg)))
    (.getTimeFormat arg) (assoc :timeFormat (.getTimeFormat arg))
    (.getTimeZone arg) (assoc :timeZone (.getTimeZone arg))
    (.getTimestampFormat arg) (assoc :timestampFormat
                                (.getTimestampFormat arg))))

(def schema
  [:map
   {:closed true,
    :doc
      "Google BigQuery external table definition. BigQuery's external tables are tables whose data\nreside outside of BigQuery but can be queried as normal BigQuery tables. External tables are\nexperimental and might be subject to change or removed.\n\n@see <a href=\"https://cloud.google.com/bigquery/federated-data-sources\">Federated Data Sources\n    </a>",
    :gcp/category :variant-accessor,
    :gcp/key :gcp.bindings.bigquery/ExternalTableDefinition}
   [:type [:= "EXTERNAL"]]
   [:schema {:getter-doc "Returns the table's schema."}
    :gcp.bindings.bigquery/Schema]
   [:formatOptions
    {:getter-doc
       "Returns the source format, and possibly some parsing options, of the external data. Supported\nformats are {@code CSV} and {@code NEWLINE_DELIMITED_JSON}."}
    :gcp.bindings.bigquery/FormatOptions]
   [:sourceUris
    {:getter-doc
       "Returns the fully-qualified URIs that point to your data in Google Cloud Storage. Each URI can\ncontain one '*' wildcard character that must come after the bucket's name. Size limits related\nto load jobs apply to external data sources, plus an additional limit of 10 GB maximum size\nacross all URIs.\n\n@see <a href=\"https://cloud.google.com/bigquery/loading-data-into-bigquery#quota\">Quota</a>"}
    [:seqable {:min 1} [:string {:min 1}]]]
   [:maxStaleness
    {:optional true,
     :getter-doc
       "Returns the maximum staleness of data that could be returned when the table is queried.\nStaleness encoded as a string encoding of sql IntervalValue type.\n\n@see <a\n    href=\"hhttps://cloud.google.com/bigquery/docs/reference/rest/v2/tables#resource:-table\">\n    MaxStaleness</a>",
     :setter-doc
       "[Optional] Metadata Cache Mode for the table. Set this to enable caching of metadata from\nexternal data source.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/rest/v2/tables#resource:-table\">\n    MaxStaleness</a>"}
    [:string {:min 1}]]
   [:timeZone
    {:optional true,
     :getter-doc
       "Returns the time zone used when parsing timestamp values that don't have specific time zone\ninformation.",
     :setter-doc
       "Time zone used when parsing timestamp values that do not have specific time zone information\n(e.g. 2024-04-20 12:34:56). The expected format is a IANA timezone string (e.g.\nAmerica/Los_Angeles)."}
    [:string {:min 1}]]
   [:connectionId
    {:optional true,
     :getter-doc
       "Returns the connection ID used to connect to external data source.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/v2/tables#externalDataConfiguration\">\n    ConnectionId</a>",
     :setter-doc
       "[Optional, Trusted Tester] connectionId for external data source. The value may be {@code\nnull}."}
    [:string {:min 1}]]
   [:fileSetSpecType
    {:optional true,
     :setter-doc
       "Defines how to interpret files denoted by URIs. By default the files are assumed to be data\nfiles (this can be specified explicitly via FILE_SET_SPEC_TYPE_FILE_SYSTEM_MATCH). A second\noption is \"FILE_SET_SPEC_TYPE_NEW_LINE_DELIMITED_MANIFEST\" which interprets each file as a\nmanifest file, where each line is a reference to a file."}
    [:string {:min 1}]]
   [:timestampFormat
    {:optional true,
     :getter-doc "Returns the format used to parse TIMESTAMP values.",
     :setter-doc
       "Format used to parse TIMESTAMP values. Supports C-style and SQL-style values."}
    [:string {:min 1}]]
   [:autodetect
    {:optional true,
     :getter-doc
       "[Experimental] Returns whether automatic detection of schema and format options should be\nperformed.",
     :setter-doc
       "[Experimental] Sets detection of schema and format options automatically. Any option\nspecified explicitly will be honored."}
    :boolean]
   [:compression
    {:optional true,
     :getter-doc
       "Returns the compression type of the data source.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/v2/tables#externalDataConfiguration.compression\">\n    Compression</a>",
     :setter-doc
       "Sets compression type of the data source. By default no compression is assumed.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/v2/tables#externalDataConfiguration.compression\">\n    Compression</a>"}
    [:string {:min 1}]]
   [:ignoreUnknownValues
    {:optional true,
     :setter-doc
       "Sets whether BigQuery should allow extra values that are not represented in the table schema.\nIf true, the extra values are ignored. If false, records with extra columns are treated as\nbad records, and if there are too many bad records, an invalid error is returned in the job\nresult. The default value is false. The value set with {@link\n#setFormatOptions(FormatOptions)} property determines what BigQuery treats as an extra value.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/v2/tables#externalDataConfiguration.ignoreUnknownValues\">\n    Ignore Unknown Values</a>"}
    :boolean]
   [:dateFormat
    {:optional true,
     :getter-doc "Returns the format used to parse DATE values.",
     :setter-doc
       "Format used to parse DATE values. Supports C-style and SQL-style values."}
    [:string {:min 1}]]
   [:hivePartitioningOptions
    {:optional true,
     :getter-doc
       "[Experimental] Returns the HivePartitioningOptions when the data layout follows Hive\npartitioning convention",
     :setter-doc "Sets the table Hive partitioning options."}
    :gcp.bindings.bigquery/HivePartitioningOptions]
   [:sourceColumnMatch
    {:optional true,
     :getter-doc
       "Returns the strategy used to match loaded columns to the schema, either POSITION or NAME.",
     :setter-doc
       "Controls the strategy used to match loaded columns to the schema. If not set, a sensible\ndefault is chosen based on how the schema is provided. If autodetect is used, then columns\nare matched by name. Otherwise, columns are matched by position. This is done to keep the\nbehavior backward-compatible. Acceptable values are: POSITION - matches by position. This\nassumes that the columns are ordered the same way as the schema. NAME - matches by name. This\nreads the header row as column names and reorders columns to match the field names in the\nschema."}
    :gcp.bindings.bigquery/ExternalTableDefinition.SourceColumnMatch]
   [:decimalTargetTypes
    {:optional true,
     :setter-doc
       "Defines the list of possible SQL data types to which the source decimal values are converted.\nThis list and the precision and the scale parameters of the decimal field determine the\ntarget type. In the order of NUMERIC, BIGNUMERIC, and STRING, a type is picked if it is in\nthe specified list and if it supports the precision and the scale. STRING supports all\nprecision and scale values.\n\n@param decimalTargetTypes decimalTargetType or {@code null} for none"}
    [:seqable {:min 1} [:string {:min 1}]]]
   [:maxBadRecords
    {:optional true,
     :getter-doc
       "Returns the maximum number of bad records that BigQuery can ignore when reading data. If the\nnumber of bad records exceeds this value, an invalid error is returned in the job result.",
     :setter-doc
       "Sets the maximum number of bad records that BigQuery can ignore when reading data. If the\nnumber of bad records exceeds this value, an invalid error is returned in the job result. The\ndefault value is 0, which requires that all records are valid."}
    [:int {:min -2147483648, :max 2147483647}]]
   [:nullMarkers
    {:optional true,
     :getter-doc
       "Returns a list of strings represented as SQL NULL value in a CSV file.",
     :setter-doc
       "A list of strings represented as SQL NULL value in a CSV file. null_marker and null_markers\ncan't be set at the same time. If null_marker is set, null_markers has to be not set. If\nnull_markers is set, null_marker has to be not set. If both null_marker and null_markers are\nset at the same time, a user error would be thrown. Any strings listed in null_markers,\nincluding empty string would be interpreted as SQL NULL. This applies to all column types."}
    [:seqable {:min 1} [:string {:min 1}]]]
   [:objectMetadata
    {:optional true,
     :getter-doc
       "Returns the object metadata.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/v2/tables#externalDataConfiguration\">\n    ObjectMetadata</a>"}
    [:string {:min 1}]]
   [:referenceFileSchemaUri
    {:optional true,
     :setter-doc
       "When creating an external table, the user can provide a reference file with the table schema.\nThis is enabled for the following formats: AVRO, PARQUET, ORC.\n\n@param referenceFileSchemaUri or {@code null} for none"}
    [:string {:min 1}]]
   [:metadataCacheMode
    {:optional true,
     :getter-doc
       "Returns the metadata cache mode.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/rest/v2/tables#metadatacachemode\">\n    MetadataCacheMode</a>",
     :setter-doc
       "[Optional] Metadata Cache Mode for the table. Set this to enable caching of metadata from\nexternal data source.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/rest/v2/tables#metadatacachemode\">\n    MetadataCacheMode</a>"}
    [:string {:min 1}]]
   [:timeFormat
    {:optional true,
     :getter-doc "Returns the format used to parse TIME values.",
     :setter-doc
       "Format used to parse TIME values. Supports C-style and SQL-style values."}
    [:string {:min 1}]]
   [:datetimeFormat
    {:optional true,
     :getter-doc "Returns the format used to parse DATETIME values.",
     :setter-doc
       "Format used to parse DATETIME values. Supports C-style and SQL-style values."}
    [:string {:min 1}]]
   [:sourceUrisImmut {:optional true, :read-only? true}
    [:seqable {:min 1} [:string {:min 1}]]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/ExternalTableDefinition schema,
              :gcp.bindings.bigquery/ExternalTableDefinition.SourceColumnMatch
                ExternalTableDefinition$SourceColumnMatch-schema}
    {:gcp.global/name "gcp.bindings.bigquery.ExternalTableDefinition"}))