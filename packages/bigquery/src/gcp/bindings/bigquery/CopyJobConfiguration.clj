;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.CopyJobConfiguration
  {:doc
     "Google BigQuery copy job configuration. A copy job copies an existing table to another new or\nexisting table. Copy job configurations have {@link JobConfiguration.Type#COPY} type."
   :file-git-sha "3e97f7c0c4676fcdda0862929a69bbabc69926f2"
   :fqcn "com.google.cloud.bigquery.CopyJobConfiguration"
   :gcp.dev/certification
     {:base-seed 1771391189563
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771391189563 :standard 1771391189564 :stress 1771391189565}
      :protocol-hash
        "32175171656ac95c72455bff25fe019346fb52c916efee805be149692e31cec4"
      :timestamp "2026-02-18T05:06:29.740661318Z"}}
  (:require [gcp.bindings.bigquery.EncryptionConfiguration :as
             EncryptionConfiguration]
            [gcp.bindings.bigquery.TableId :as TableId]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery CopyJobConfiguration
            CopyJobConfiguration$Builder JobInfo$CreateDisposition
            JobInfo$WriteDisposition]))

(defn ^CopyJobConfiguration from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/CopyJobConfiguration arg)
  (let [builder (CopyJobConfiguration/newBuilder
                  (TableId/from-edn (get arg :destinationTable))
                  (map TableId/from-edn (get arg :sourceTables)))]
    (when (some? (get arg :createDisposition))
      (.setCreateDisposition builder
                             (JobInfo$CreateDisposition/valueOf
                               (get arg :createDisposition))))
    (when (some? (get arg :destinationEncryptionConfiguration))
      (.setDestinationEncryptionConfiguration
        builder
        (EncryptionConfiguration/from-edn
          (get arg :destinationEncryptionConfiguration))))
    (when (some? (get arg :destinationExpirationTime))
      (.setDestinationExpirationTime builder
                                     (get arg :destinationExpirationTime)))
    (when (some? (get arg :jobTimeoutMs))
      (.setJobTimeoutMs builder (get arg :jobTimeoutMs)))
    (when (some? (get arg :labels))
      (.setLabels builder
                  (into {} (map (fn [[k v]] [(name k) v])) (get arg :labels))))
    (when (some? (get arg :operationType))
      (.setOperationType builder (get arg :operationType)))
    (when (some? (get arg :reservation))
      (.setReservation builder (get arg :reservation)))
    (when (some? (get arg :writeDisposition))
      (.setWriteDisposition builder
                            (JobInfo$WriteDisposition/valueOf
                              (get arg :writeDisposition))))
    (.build builder)))

(defn to-edn
  [^CopyJobConfiguration arg]
  {:post [(global/strict! :gcp.bindings.bigquery/CopyJobConfiguration %)]}
  (cond-> {:destinationTable (TableId/to-edn (.getDestinationTable arg)),
           :sourceTables (map TableId/to-edn (.getSourceTables arg)),
           :type "COPY"}
    (.getCreateDisposition arg) (assoc :createDisposition
                                  (.name (.getCreateDisposition arg)))
    (.getDestinationEncryptionConfiguration arg)
      (assoc :destinationEncryptionConfiguration
        (EncryptionConfiguration/to-edn (.getDestinationEncryptionConfiguration
                                          arg)))
    (.getDestinationExpirationTime arg) (assoc :destinationExpirationTime
                                          (.getDestinationExpirationTime arg))
    (.getJobTimeoutMs arg) (assoc :jobTimeoutMs (.getJobTimeoutMs arg))
    (.getLabels arg)
      (assoc :labels
        (into {} (map (fn [[k v]] [(keyword k) v])) (.getLabels arg)))
    (.getOperationType arg) (assoc :operationType (.getOperationType arg))
    (.getReservation arg) (assoc :reservation (.getReservation arg))
    (.getWriteDisposition arg) (assoc :writeDisposition
                                 (.name (.getWriteDisposition arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Google BigQuery copy job configuration. A copy job copies an existing table to another new or\nexisting table. Copy job configurations have {@link JobConfiguration.Type#COPY} type.",
    :gcp/category :variant-accessor,
    :gcp/key :gcp.bindings.bigquery/CopyJobConfiguration} [:type [:= "COPY"]]
   [:destinationTable
    {:getter-doc "Returns the destination table to load the data into."}
    :gcp.bindings.bigquery/TableId]
   [:sourceTables {:getter-doc "Returns the source tables to copy."}
    [:seqable {:min 1} :gcp.bindings.bigquery/TableId]]
   [:labels
    {:optional true,
     :getter-doc "Returns the labels associated with this job",
     :setter-doc
       "The labels associated with this job. You can use these to organize and group your jobs. Label\nkeys and values can be no longer than 63 characters, can only contain lowercase letters,\nnumeric characters, underscores and dashes. International characters are allowed. Label\nvalues are optional. Label keys must start with a letter and each label in the list must have\na different key.\n\n@param labels labels or {@code null} for none"}
    [:map-of [:or keyword? [:string {:min 1}]] [:string {:min 1}]]]
   [:reservation
    {:optional true,
     :getter-doc "Returns the reservation associated with this job",
     :setter-doc
       "[Optional] The reservation that job would use. User can specify a reservation to execute the\njob. If reservation is not set, reservation is determined based on the rules defined by the\nreservation assignments. The expected format is\n`projects/{project}/locations/{location}/reservations/{reservation}`.\n\n@param reservation reservation or {@code null} for none"}
    [:string {:min 1}]]
   [:destinationExpirationTime
    {:optional true,
     :getter-doc "Returns the time when the destination table expires",
     :setter-doc
       "Sets the time when the destination table expires. Expired tables will be deleted and their\nstorage reclaimed. More info:\nhttps://cloud.google.com/bigquery/docs/reference/rest/v2/Job#jobconfigurationtablecopy"}
    [:string {:min 1}]]
   [:createDisposition
    {:optional true,
     :getter-doc
       "Returns whether the job is allowed to create new tables.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/v2/jobs#configuration.copy.createDisposition\">\n    Create Disposition</a>",
     :setter-doc
       "Sets whether the job is allowed to create new tables.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/v2/jobs#configuration.copy.createDisposition\">\n    Create Disposition</a>"}
    [:enum {:closed true} "CREATE_IF_NEEDED" "CREATE_NEVER"]]
   [:writeDisposition
    {:optional true,
     :getter-doc
       "Returns the action that should occur if the destination table already exists.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/v2/jobs#configuration.copy.writeDisposition\">\n    Write Disposition</a>",
     :setter-doc
       "Sets the action that should occur if the destination table already exists.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/v2/jobs#configuration.copy.writeDisposition\">\n    Write Disposition</a>"}
    [:enum {:closed true} "WRITE_TRUNCATE" "WRITE_TRUNCATE_DATA" "WRITE_APPEND"
     "WRITE_EMPTY"]]
   [:jobTimeoutMs
    {:optional true,
     :getter-doc "Returns the timeout associated with this job",
     :setter-doc
       "[Optional] Job timeout in milliseconds. If this time limit is exceeded, BigQuery may attempt\nto terminate the job.\n\n@param jobTimeoutMs jobTimeoutMs or {@code null} for none"}
    :int]
   [:destinationEncryptionConfiguration {:optional true}
    :gcp.bindings.bigquery/EncryptionConfiguration]
   [:operationType
    {:optional true,
     :getter-doc "Returns the table copy job type",
     :setter-doc
       "Sets the supported operation types (COPY, CLONE, SNAPSHOT or RESTORE) in table copy job. More\ninfo: https://cloud.google.com/bigquery/docs/reference/rest/v2/Job#operationtype"}
    [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/CopyJobConfiguration schema}
    {:gcp.global/name "gcp.bindings.bigquery.CopyJobConfiguration"}))