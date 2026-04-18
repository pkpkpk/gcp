;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.CopyJobConfiguration
  {:doc
     "Google BigQuery copy job configuration. A copy job copies an existing table to another new or\nexisting table. Copy job configurations have {@link JobConfiguration.Type#COPY} type."
   :file-git-sha "3e97f7c0c4676fcdda0862929a69bbabc69926f2"
   :fqcn "com.google.cloud.bigquery.CopyJobConfiguration"
   :gcp.dev/certification
     {:base-seed 1776499409649
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1776499409649 :standard 1776499409650 :stress 1776499409651}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:03:31.058076823Z"}}
  (:require [gcp.bigquery.EncryptionConfiguration :as EncryptionConfiguration]
            [gcp.bigquery.TableId :as TableId]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery CopyJobConfiguration
            CopyJobConfiguration$Builder JobInfo$CreateDisposition
            JobInfo$WriteDisposition]))

(declare from-edn to-edn)

(defn ^CopyJobConfiguration from-edn
  [arg]
  (global/strict! :gcp.bigquery/CopyJobConfiguration arg)
  (let [builder (CopyJobConfiguration/newBuilder
                  (TableId/from-edn (get arg :destinationTable))
                  (mapv TableId/from-edn (get arg :sourceTables)))]
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
      (.setJobTimeoutMs builder (long (get arg :jobTimeoutMs))))
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
  {:post [(global/strict! :gcp.bigquery/CopyJobConfiguration %)]}
  (when arg
    (cond-> {:destinationTable (TableId/to-edn (.getDestinationTable arg)),
             :sourceTables (mapv TableId/to-edn (.getSourceTables arg)),
             :type "COPY"}
      (.getCreateDisposition arg) (assoc :createDisposition
                                    (.name (.getCreateDisposition arg)))
      (.getDestinationEncryptionConfiguration arg)
        (assoc :destinationEncryptionConfiguration
          (EncryptionConfiguration/to-edn
            (.getDestinationEncryptionConfiguration arg)))
      (some->> (.getDestinationExpirationTime arg)
               (not= ""))
        (assoc :destinationExpirationTime (.getDestinationExpirationTime arg))
      (.getJobTimeoutMs arg) (assoc :jobTimeoutMs (.getJobTimeoutMs arg))
      (seq (.getLabels arg))
        (assoc :labels
          (into {} (map (fn [[k v]] [(keyword k) v])) (.getLabels arg)))
      (some->> (.getOperationType arg)
               (not= ""))
        (assoc :operationType (.getOperationType arg))
      (some->> (.getReservation arg)
               (not= ""))
        (assoc :reservation (.getReservation arg))
      (.getWriteDisposition arg) (assoc :writeDisposition
                                   (.name (.getWriteDisposition arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "Google BigQuery copy job configuration. A copy job copies an existing table to another new or\nexisting table. Copy job configurations have {@link JobConfiguration.Type#COPY} type.",
    :gcp/category :variant-accessor,
    :gcp/key :gcp.bigquery/CopyJobConfiguration} [:type [:= "COPY"]]
   [:createDisposition
    {:optional true,
     :getter-doc
       "Returns whether the job is allowed to create new tables.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/v2/jobs#configuration.copy.createDisposition\">\n    Create Disposition</a>",
     :setter-doc
       "Sets whether the job is allowed to create new tables.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/v2/jobs#configuration.copy.createDisposition\">\n    Create Disposition</a>"}
    [:enum {:closed true} "CREATE_IF_NEEDED" "CREATE_NEVER"]]
   [:destinationEncryptionConfiguration {:optional true}
    :gcp.bigquery/EncryptionConfiguration]
   [:destinationExpirationTime
    {:optional true,
     :getter-doc "Returns the time when the destination table expires",
     :setter-doc
       "Sets the time when the destination table expires. Expired tables will be deleted and their\nstorage reclaimed. More info:\nhttps://cloud.google.com/bigquery/docs/reference/rest/v2/Job#jobconfigurationtablecopy"}
    [:string {:min 1}]]
   [:destinationTable
    {:getter-doc "Returns the destination table to load the data into."}
    :gcp.bigquery/TableId]
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
   [:operationType
    {:optional true,
     :getter-doc "Returns the table copy job type",
     :setter-doc
       "Sets the supported operation types (COPY, CLONE, SNAPSHOT or RESTORE) in table copy job. More\ninfo: https://cloud.google.com/bigquery/docs/reference/rest/v2/Job#operationtype"}
    [:string {:min 1}]]
   [:reservation
    {:optional true,
     :getter-doc "Returns the reservation associated with this job",
     :setter-doc
       "[Optional] The reservation that job would use. User can specify a reservation to execute the\njob. If reservation is not set, reservation is determined based on the rules defined by the\nreservation assignments. The expected format is\n`projects/{project}/locations/{location}/reservations/{reservation}`.\n\n@param reservation reservation or {@code null} for none"}
    [:string {:min 1}]]
   [:sourceTables {:getter-doc "Returns the source tables to copy."}
    [:sequential {:min 1} :gcp.bigquery/TableId]]
   [:writeDisposition
    {:optional true,
     :getter-doc
       "Returns the action that should occur if the destination table already exists.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/v2/jobs#configuration.copy.writeDisposition\">\n    Write Disposition</a>",
     :setter-doc
       "Sets the action that should occur if the destination table already exists.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/v2/jobs#configuration.copy.writeDisposition\">\n    Write Disposition</a>"}
    [:enum {:closed true} "WRITE_TRUNCATE" "WRITE_TRUNCATE_DATA" "WRITE_APPEND"
     "WRITE_EMPTY"]]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/CopyJobConfiguration schema}
    {:gcp.global/name "gcp.bigquery.CopyJobConfiguration"}))