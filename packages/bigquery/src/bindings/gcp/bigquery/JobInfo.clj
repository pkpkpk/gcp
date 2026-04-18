;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.JobInfo
  {:doc
     "Google BigQuery Job information. Jobs are objects that manage asynchronous tasks such as running\nqueries, loading data, and exporting data. Use {@link CopyJobConfiguration} for a job that copies\nan existing table. Use {@link ExtractJobConfiguration} for a job that exports a table to Google\nCloud Storage. Use {@link LoadJobConfiguration} for a job that loads data from Google Cloud\nStorage into a table. Use {@link QueryJobConfiguration} for a job that runs a query.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/reference/v2/jobs\">Jobs</a>"
   :file-git-sha "acea61c20b69b44c8612ca22745458ad04bc6be4"
   :fqcn "com.google.cloud.bigquery.JobInfo"
   :gcp.dev/certification
     {:base-seed 1776499439809
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1776499439809 :standard 1776499439810 :stress 1776499439811}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:04:08.647963336Z"}}
  (:require [gcp.bigquery.JobConfiguration :as JobConfiguration]
            [gcp.bigquery.JobId :as JobId]
            [gcp.bigquery.JobStatus :as JobStatus]
            [gcp.bigquery.custom.JobStatistics :as JobStatistics]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery JobInfo JobInfo$Builder
            JobInfo$CreateDisposition JobInfo$SchemaUpdateOption
            JobInfo$WriteDisposition]))

(declare from-edn
         to-edn
         CreateDisposition-from-edn
         CreateDisposition-to-edn
         WriteDisposition-from-edn
         WriteDisposition-to-edn
         SchemaUpdateOption-from-edn
         SchemaUpdateOption-to-edn)

(def CreateDisposition-schema
  [:enum
   {:closed true,
    :doc "Specifies whether the job is allowed to create new tables.",
    :gcp/category :nested/enum,
    :gcp/key :gcp.bigquery/JobInfo.CreateDisposition} "CREATE_IF_NEEDED"
   "CREATE_NEVER"])

(def WriteDisposition-schema
  [:enum
   {:closed true,
    :doc
      "Specifies the action that occurs if the destination table already exists.",
    :gcp/category :nested/enum,
    :gcp/key :gcp.bigquery/JobInfo.WriteDisposition} "WRITE_TRUNCATE"
   "WRITE_TRUNCATE_DATA" "WRITE_APPEND" "WRITE_EMPTY"])

(def SchemaUpdateOption-schema
  [:enum
   {:closed true,
    :doc
      "Specifies options relating to allowing the schema of the destination table to be updated as a\nside effect of the load or query job.",
    :gcp/category :nested/enum,
    :gcp/key :gcp.bigquery/JobInfo.SchemaUpdateOption} "ALLOW_FIELD_ADDITION"
   "ALLOW_FIELD_RELAXATION"])

(defn ^JobInfo from-edn
  [arg]
  (global/strict! :gcp.bigquery/JobInfo arg)
  (let [builder (JobInfo/newBuilder (JobConfiguration/from-edn
                                      (get arg :configuration)))]
    (when (some? (get arg :jobId))
      (.setJobId builder (JobId/from-edn (get arg :jobId))))
    (.build builder)))

(defn to-edn
  [^JobInfo arg]
  {:post [(global/strict! :gcp.bigquery/JobInfo %)]}
  (when arg
    (cond-> {:configuration (JobConfiguration/to-edn (.getConfiguration arg))}
      (some->> (.getEtag arg)
               (not= ""))
        (assoc :etag (.getEtag arg))
      (some->> (.getGeneratedId arg)
               (not= ""))
        (assoc :generatedId (.getGeneratedId arg))
      (.getJobId arg) (assoc :jobId (JobId/to-edn (.getJobId arg)))
      (some->> (.getSelfLink arg)
               (not= ""))
        (assoc :selfLink (.getSelfLink arg))
      (.getStatistics arg) (assoc :statistics
                             (JobStatistics/to-edn (.getStatistics arg)))
      (.getStatus arg) (assoc :status (JobStatus/to-edn (.getStatus arg)))
      (some->> (.getUserEmail arg)
               (not= ""))
        (assoc :userEmail (.getUserEmail arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Google BigQuery Job information. Jobs are objects that manage asynchronous tasks such as running\nqueries, loading data, and exporting data. Use {@link CopyJobConfiguration} for a job that copies\nan existing table. Use {@link ExtractJobConfiguration} for a job that exports a table to Google\nCloud Storage. Use {@link LoadJobConfiguration} for a job that loads data from Google Cloud\nStorage into a table. Use {@link QueryJobConfiguration} for a job that runs a query.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/reference/v2/jobs\">Jobs</a>",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/JobInfo}
   [:configuration {:getter-doc "Returns the job's configuration."}
    :gcp.bigquery/JobConfiguration]
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
     :setter-doc "Sets the job identity."} :gcp.bigquery/JobId]
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
    :gcp.bigquery/JobStatistics]
   [:status
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns the status of this job. Examine this value when polling an asynchronous job to see if\nthe job is complete."}
    :gcp.bigquery/JobStatus]
   [:userEmail
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the email address of the user who ran the job."}
    [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/JobInfo schema,
              :gcp.bigquery/JobInfo.CreateDisposition CreateDisposition-schema,
              :gcp.bigquery/JobInfo.SchemaUpdateOption
                SchemaUpdateOption-schema,
              :gcp.bigquery/JobInfo.WriteDisposition WriteDisposition-schema}
    {:gcp.global/name "gcp.bigquery.JobInfo"}))