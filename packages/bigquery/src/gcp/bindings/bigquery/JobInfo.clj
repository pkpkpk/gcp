;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.JobInfo
  {:doc
     "Google BigQuery Job information. Jobs are objects that manage asynchronous tasks such as running\nqueries, loading data, and exporting data. Use {@link CopyJobConfiguration} for a job that copies\nan existing table. Use {@link ExtractJobConfiguration} for a job that exports a table to Google\nCloud Storage. Use {@link LoadJobConfiguration} for a job that loads data from Google Cloud\nStorage into a table. Use {@link QueryJobConfiguration} for a job that runs a query.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/reference/v2/jobs\">Jobs</a>"
   :file-git-sha "acea61c20b69b44c8612ca22745458ad04bc6be4"
   :fqcn "com.google.cloud.bigquery.JobInfo"
   :gcp.dev/certification
     {:base-seed 1772044584785
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1772044584785 :standard 1772044584786 :stress 1772044584787}
      :protocol-hash
        "b8a5eb17212acb18f49dcb7f15a243eac9b32f54c0f054a99be660a3a25e0315"
      :timestamp "2026-02-25T18:38:15.154157222Z"}}
  (:require [gcp.bigquery.custom.JobStatistics :as JobStatistics]
            [gcp.bindings.bigquery.JobConfiguration :as JobConfiguration]
            [gcp.bindings.bigquery.JobId :as JobId]
            [gcp.bindings.bigquery.JobStatus :as JobStatus]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery JobInfo JobInfo$Builder
            JobInfo$CreateDisposition JobInfo$SchemaUpdateOption
            JobInfo$WriteDisposition]))

(declare JobInfo$CreateDisposition-from-edn
         JobInfo$CreateDisposition-to-edn
         JobInfo$WriteDisposition-from-edn
         JobInfo$WriteDisposition-to-edn
         JobInfo$SchemaUpdateOption-from-edn
         JobInfo$SchemaUpdateOption-to-edn)

(def JobInfo$CreateDisposition-schema
  [:enum
   {:closed true,
    :doc "Specifies whether the job is allowed to create new tables.",
    :gcp/category :nested/enum,
    :gcp/key :gcp.bindings.bigquery/JobInfo.CreateDisposition}
   "CREATE_IF_NEEDED" "CREATE_NEVER"])

(def JobInfo$WriteDisposition-schema
  [:enum
   {:closed true,
    :doc
      "Specifies the action that occurs if the destination table already exists.",
    :gcp/category :nested/enum,
    :gcp/key :gcp.bindings.bigquery/JobInfo.WriteDisposition} "WRITE_TRUNCATE"
   "WRITE_TRUNCATE_DATA" "WRITE_APPEND" "WRITE_EMPTY"])

(def JobInfo$SchemaUpdateOption-schema
  [:enum
   {:closed true,
    :doc
      "Specifies options relating to allowing the schema of the destination table to be updated as a\nside effect of the load or query job.",
    :gcp/category :nested/enum,
    :gcp/key :gcp.bindings.bigquery/JobInfo.SchemaUpdateOption}
   "ALLOW_FIELD_ADDITION" "ALLOW_FIELD_RELAXATION"])

(defn ^JobInfo from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/JobInfo arg)
  (let [builder (JobInfo/newBuilder (JobConfiguration/from-edn
                                      (get arg :configuration)))]
    (when (some? (get arg :jobId))
      (.setJobId builder (JobId/from-edn (get arg :jobId))))
    (.build builder)))

(defn to-edn
  [^JobInfo arg]
  {:post [(global/strict! :gcp.bindings.bigquery/JobInfo %)]}
  (cond-> {:configuration (JobConfiguration/to-edn (.getConfiguration arg))}
    (.getEtag arg) (assoc :etag (.getEtag arg))
    (.getGeneratedId arg) (assoc :generatedId (.getGeneratedId arg))
    (.getJobId arg) (assoc :jobId (JobId/to-edn (.getJobId arg)))
    (.getSelfLink arg) (assoc :selfLink (.getSelfLink arg))
    (.getStatistics arg) (assoc :statistics
                           (JobStatistics/to-edn (.getStatistics arg)))
    (.getStatus arg) (assoc :status (JobStatus/to-edn (.getStatus arg)))
    (.getUserEmail arg) (assoc :userEmail (.getUserEmail arg))))

(def schema
  [:map
   {:closed true,
    :doc
      "Google BigQuery Job information. Jobs are objects that manage asynchronous tasks such as running\nqueries, loading data, and exporting data. Use {@link CopyJobConfiguration} for a job that copies\nan existing table. Use {@link ExtractJobConfiguration} for a job that exports a table to Google\nCloud Storage. Use {@link LoadJobConfiguration} for a job that loads data from Google Cloud\nStorage into a table. Use {@link QueryJobConfiguration} for a job that runs a query.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/reference/v2/jobs\">Jobs</a>",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/JobInfo}
   [:configuration {:getter-doc "Returns the job's configuration."}
    :gcp.bindings.bigquery/JobConfiguration]
   [:etag
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the hash of the job resource."} [:string {:min 1}]]
   [:generatedId
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the service-generated id for the job."}
    [:string {:min 1}]]
   [:jobId
    {:optional true,
     :getter-doc "Returns the job identity.",
     :setter-doc "Sets the job identity."} :gcp.bindings.bigquery/JobId]
   [:selfLink
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns an URL that can be used to access the resource again. The returned URL can be used for\nGET requests."}
    [:string {:min 1}]]
   [:statistics
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns information about the job, including starting time and ending time of the job."}
    :gcp.bigquery.custom/JobStatistics]
   [:status
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns the status of this job. Examine this value when polling an asynchronous job to see if\nthe job is complete."}
    :gcp.bindings.bigquery/JobStatus]
   [:userEmail
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the email address of the user who ran the job."}
    [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/JobInfo schema,
              :gcp.bindings.bigquery/JobInfo.CreateDisposition
                JobInfo$CreateDisposition-schema,
              :gcp.bindings.bigquery/JobInfo.SchemaUpdateOption
                JobInfo$SchemaUpdateOption-schema,
              :gcp.bindings.bigquery/JobInfo.WriteDisposition
                JobInfo$WriteDisposition-schema}
    {:gcp.global/name "gcp.bindings.bigquery.JobInfo"}))