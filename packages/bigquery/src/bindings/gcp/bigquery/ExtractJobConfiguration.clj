;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.ExtractJobConfiguration
  {:doc
     "Google BigQuery extract job configuration. An extract job exports a BigQuery table to Google\nCloud Storage. The extract destination provided as URIs that point to objects in Google Cloud\nStorage. Extract job configurations have {@link JobConfiguration.Type#EXTRACT} type."
   :file-git-sha "3e97f7c0c4676fcdda0862929a69bbabc69926f2"
   :fqcn "com.google.cloud.bigquery.ExtractJobConfiguration"
   :gcp.dev/certification
     {:base-seed 1775130921653
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1775130921653 :standard 1775130921654 :stress 1775130921655}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:55:22.884468400Z"}}
  (:require [gcp.bigquery.ModelId :as ModelId]
            [gcp.bigquery.TableId :as TableId]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery ExtractJobConfiguration
            ExtractJobConfiguration$Builder]))

(declare from-edn to-edn)

(defn ^ExtractJobConfiguration from-edn
  [arg]
  (global/strict! :gcp.bigquery/ExtractJobConfiguration arg)
  (let [builder (ExtractJobConfiguration/newBuilder
                  (TableId/from-edn (get arg :sourceTable))
                  (seq (get arg :destinationUris)))]
    (when (some? (get arg :compression))
      (.setCompression builder (get arg :compression)))
    (when (some? (get arg :fieldDelimiter))
      (.setFieldDelimiter builder (get arg :fieldDelimiter)))
    (when (some? (get arg :format)) (.setFormat builder (get arg :format)))
    (when (some? (get arg :jobTimeoutMs))
      (.setJobTimeoutMs builder (long (get arg :jobTimeoutMs))))
    (when (some? (get arg :labels))
      (.setLabels builder
                  (into {} (map (fn [[k v]] [(name k) v])) (get arg :labels))))
    (when (some? (get arg :printHeader))
      (.setPrintHeader builder (get arg :printHeader)))
    (when (some? (get arg :reservation))
      (.setReservation builder (get arg :reservation)))
    (when (some? (get arg :sourceModel))
      (.setSourceModel builder (ModelId/from-edn (get arg :sourceModel))))
    (when (some? (get arg :useAvroLogicalTypes))
      (.setUseAvroLogicalTypes builder (get arg :useAvroLogicalTypes)))
    (.build builder)))

(defn to-edn
  [^ExtractJobConfiguration arg]
  {:post [(global/strict! :gcp.bigquery/ExtractJobConfiguration %)]}
  (when arg
    (cond-> {:destinationUris (seq (.getDestinationUris arg)),
             :sourceTable (TableId/to-edn (.getSourceTable arg)),
             :type "EXTRACT"}
      (some->> (.getCompression arg)
               (not= ""))
        (assoc :compression (.getCompression arg))
      (some->> (.getFieldDelimiter arg)
               (not= ""))
        (assoc :fieldDelimiter (.getFieldDelimiter arg))
      (some->> (.getFormat arg)
               (not= ""))
        (assoc :format (.getFormat arg))
      (.getJobTimeoutMs arg) (assoc :jobTimeoutMs (.getJobTimeoutMs arg))
      (seq (.getLabels arg))
        (assoc :labels
          (into {} (map (fn [[k v]] [(keyword k) v])) (.getLabels arg)))
      (.printHeader arg) (assoc :printHeader (.printHeader arg))
      (some->> (.getReservation arg)
               (not= ""))
        (assoc :reservation (.getReservation arg))
      (.getSourceModel arg) (assoc :sourceModel
                              (ModelId/to-edn (.getSourceModel arg)))
      (.getUseAvroLogicalTypes arg) (assoc :useAvroLogicalTypes
                                      (.getUseAvroLogicalTypes arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Google BigQuery extract job configuration. An extract job exports a BigQuery table to Google\nCloud Storage. The extract destination provided as URIs that point to objects in Google Cloud\nStorage. Extract job configurations have {@link JobConfiguration.Type#EXTRACT} type.",
    :gcp/category :variant-accessor,
    :gcp/key :gcp.bigquery/ExtractJobConfiguration} [:type [:= "EXTRACT"]]
   [:compression
    {:optional true,
     :getter-doc "Returns the compression value of exported files.",
     :setter-doc
       "Sets the compression value to use for exported files. If not set exported files are not\ncompressed.\n\n<p><a\nhref=\"https://cloud.google.com/bigquery/docs/reference/v2/jobs#configuration.extract.compression\">\nCompression</a>"}
    [:string {:min 1}]]
   [:destinationUris
    {:getter-doc
       "Returns the list of fully-qualified Google Cloud Storage URIs where the extracted table should\nbe written.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/exporting-data-from-bigquery#exportingmultiple\">\n    Exporting Data Into One or More Files</a>"}
    [:sequential {:min 1} [:string {:min 1}]]]
   [:fieldDelimiter
    {:optional true,
     :getter-doc
       "Returns the delimiter used between fields in the exported data.",
     :setter-doc
       "Sets the delimiter to use between fields in the exported data. By default \",\" is used."}
    [:string {:min 1}]]
   [:format
    {:optional true,
     :getter-doc "Returns the exported files format.",
     :setter-doc
       "Sets the exported file format. If not set table is exported in CSV format.\n\n<p><a\nhref=\"https://cloud.google.com/bigquery/docs/reference/v2/jobs#configuration.extract.destinationFormat\">\nDestination Format</a>"}
    [:string {:min 1}]]
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
   [:printHeader
    {:optional true,
     :getter-doc "Returns whether an header row is printed with the result.",
     :setter-doc
       "Sets whether to print out a header row in the results. By default an header is printed."}
    :boolean]
   [:reservation
    {:optional true,
     :getter-doc "Returns the reservation associated with this job",
     :setter-doc
       "[Optional] The reservation that job would use. User can specify a reservation to execute the\njob. If reservation is not set, reservation is determined based on the rules defined by the\nreservation assignments. The expected format is\n`projects/{project}/locations/{location}/reservations/{reservation}`.\n\n@param reservation reservation or {@code null} for none"}
    [:string {:min 1}]]
   [:sourceModel
    {:optional true,
     :getter-doc "Returns the model to export.",
     :setter-doc "Sets the model to export."} :gcp.bigquery/ModelId]
   [:sourceTable {:getter-doc "Returns the table to export."}
    :gcp.bigquery/TableId]
   [:useAvroLogicalTypes
    {:optional true,
     :getter-doc
       "Returns True/False. Indicates whether exported avro files include logical type annotations.",
     :setter-doc
       "[Optional] If destinationFormat is set to \"AVRO\", this flag indicates whether to enable\nextracting applicable column types (such as TIMESTAMP) to their corresponding AVRO logical\ntypes (timestamp-micros), instead of only using their raw types (avro-long).\n\n@param useAvroLogicalTypes useAvroLogicalTypes or {@code null} for none"}
    :boolean]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/ExtractJobConfiguration schema}
    {:gcp.global/name "gcp.bigquery.ExtractJobConfiguration"}))