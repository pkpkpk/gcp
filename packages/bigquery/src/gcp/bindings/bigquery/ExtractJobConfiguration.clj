;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.ExtractJobConfiguration
  {:doc
     "Google BigQuery extract job configuration. An extract job exports a BigQuery table to Google\nCloud Storage. The extract destination provided as URIs that point to objects in Google Cloud\nStorage. Extract job configurations have {@link JobConfiguration.Type#EXTRACT} type."
   :file-git-sha "3e97f7c0c4676fcdda0862929a69bbabc69926f2"
   :fqcn "com.google.cloud.bigquery.ExtractJobConfiguration"
   :gcp.dev/certification
     {:base-seed 1771391179617
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771391179617 :standard 1771391179618 :stress 1771391179619}
      :protocol-hash
        "32175171656ac95c72455bff25fe019346fb52c916efee805be149692e31cec4"
      :timestamp "2026-02-18T05:06:19.759375119Z"}}
  (:require [gcp.bindings.bigquery.ModelId :as ModelId]
            [gcp.bindings.bigquery.TableId :as TableId]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery ExtractJobConfiguration
            ExtractJobConfiguration$Builder]))

(defn ^ExtractJobConfiguration from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/ExtractJobConfiguration arg)
  (let [builder (ExtractJobConfiguration/newBuilder
                  (TableId/from-edn (get arg :sourceTable))
                  (seq (get arg :destinationUris)))]
    (when (some? (get arg :compression))
      (.setCompression builder (get arg :compression)))
    (when (some? (get arg :fieldDelimiter))
      (.setFieldDelimiter builder (get arg :fieldDelimiter)))
    (when (some? (get arg :format)) (.setFormat builder (get arg :format)))
    (when (some? (get arg :jobTimeoutMs))
      (.setJobTimeoutMs builder (get arg :jobTimeoutMs)))
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
  {:post [(global/strict! :gcp.bindings.bigquery/ExtractJobConfiguration %)]}
  (cond-> {:destinationUris (seq (.getDestinationUris arg)),
           :sourceTable (TableId/to-edn (.getSourceTable arg)),
           :type "EXTRACT"}
    (.getCompression arg) (assoc :compression (.getCompression arg))
    (.getFieldDelimiter arg) (assoc :fieldDelimiter (.getFieldDelimiter arg))
    (.getFormat arg) (assoc :format (.getFormat arg))
    (.getJobTimeoutMs arg) (assoc :jobTimeoutMs (.getJobTimeoutMs arg))
    (.getLabels arg)
      (assoc :labels
        (into {} (map (fn [[k v]] [(keyword k) v])) (.getLabels arg)))
    (.printHeader arg) (assoc :printHeader (.printHeader arg))
    (.getReservation arg) (assoc :reservation (.getReservation arg))
    (.getSourceModel arg) (assoc :sourceModel
                            (ModelId/to-edn (.getSourceModel arg)))
    (.getUseAvroLogicalTypes arg) (assoc :useAvroLogicalTypes
                                    (.getUseAvroLogicalTypes arg))))

(def schema
  [:map
   {:closed true,
    :doc
      "Google BigQuery extract job configuration. An extract job exports a BigQuery table to Google\nCloud Storage. The extract destination provided as URIs that point to objects in Google Cloud\nStorage. Extract job configurations have {@link JobConfiguration.Type#EXTRACT} type.",
    :gcp/category :variant-accessor,
    :gcp/key :gcp.bindings.bigquery/ExtractJobConfiguration}
   [:type [:= "EXTRACT"]]
   [:sourceTable {:getter-doc "Returns the table to export."}
    :gcp.bindings.bigquery/TableId]
   [:destinationUris
    {:getter-doc
       "Returns the list of fully-qualified Google Cloud Storage URIs where the extracted table should\nbe written.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/exporting-data-from-bigquery#exportingmultiple\">\n    Exporting Data Into One or More Files</a>"}
    [:seqable {:min 1} [:string {:min 1}]]]
   [:labels
    {:optional true,
     :getter-doc "Returns the labels associated with this job",
     :setter-doc
       "The labels associated with this job. You can use these to organize and group your jobs. Label\nkeys and values can be no longer than 63 characters, can only contain lowercase letters,\nnumeric characters, underscores and dashes. International characters are allowed. Label\nvalues are optional. Label keys must start with a letter and each label in the list must have\na different key.\n\n@param labels labels or {@code null} for none"}
    [:map-of [:or keyword? [:string {:min 1}]] [:string {:min 1}]]]
   [:printHeader
    {:optional true,
     :getter-doc "Returns whether an header row is printed with the result.",
     :setter-doc
       "Sets whether to print out a header row in the results. By default an header is printed."}
    :boolean]
   [:format
    {:optional true,
     :getter-doc "Returns the exported files format.",
     :setter-doc
       "Sets the exported file format. If not set table is exported in CSV format.\n\n<p><a\nhref=\"https://cloud.google.com/bigquery/docs/reference/v2/jobs#configuration.extract.destinationFormat\">\nDestination Format</a>"}
    [:string {:min 1}]]
   [:sourceModel
    {:optional true,
     :getter-doc "Returns the model to export.",
     :setter-doc "Sets the model to export."} :gcp.bindings.bigquery/ModelId]
   [:reservation
    {:optional true,
     :getter-doc "Returns the reservation associated with this job",
     :setter-doc
       "[Optional] The reservation that job would use. User can specify a reservation to execute the\njob. If reservation is not set, reservation is determined based on the rules defined by the\nreservation assignments. The expected format is\n`projects/{project}/locations/{location}/reservations/{reservation}`.\n\n@param reservation reservation or {@code null} for none"}
    [:string {:min 1}]]
   [:compression
    {:optional true,
     :getter-doc "Returns the compression value of exported files.",
     :setter-doc
       "Sets the compression value to use for exported files. If not set exported files are not\ncompressed.\n\n<p><a\nhref=\"https://cloud.google.com/bigquery/docs/reference/v2/jobs#configuration.extract.compression\">\nCompression</a>"}
    [:string {:min 1}]]
   [:useAvroLogicalTypes
    {:optional true,
     :getter-doc
       "Returns True/False. Indicates whether exported avro files include logical type annotations.",
     :setter-doc
       "[Optional] If destinationFormat is set to \"AVRO\", this flag indicates whether to enable\nextracting applicable column types (such as TIMESTAMP) to their corresponding AVRO logical\ntypes (timestamp-micros), instead of only using their raw types (avro-long).\n\n@param useAvroLogicalTypes useAvroLogicalTypes or {@code null} for none"}
    :boolean]
   [:fieldDelimiter
    {:optional true,
     :getter-doc
       "Returns the delimiter used between fields in the exported data.",
     :setter-doc
       "Sets the delimiter to use between fields in the exported data. By default \",\" is used."}
    [:string {:min 1}]]
   [:jobTimeoutMs
    {:optional true,
     :getter-doc "Returns the timeout associated with this job",
     :setter-doc
       "[Optional] Job timeout in milliseconds. If this time limit is exceeded, BigQuery may attempt\nto terminate the job.\n\n@param jobTimeoutMs jobTimeoutMs or {@code null} for none"}
    :int]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/ExtractJobConfiguration schema}
    {:gcp.global/name "gcp.bindings.bigquery.ExtractJobConfiguration"}))