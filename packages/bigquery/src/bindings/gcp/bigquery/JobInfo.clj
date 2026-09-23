;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.JobInfo
  {:doc
     "Google BigQuery Job information. Jobs are objects that manage asynchronous tasks such as running\nqueries, loading data, and exporting data. Use {@link CopyJobConfiguration} for a job that copies\nan existing table. Use {@link ExtractJobConfiguration} for a job that exports a table to Google\nCloud Storage. Use {@link LoadJobConfiguration} for a job that loads data from Google Cloud\nStorage into a table. Use {@link QueryJobConfiguration} for a job that runs a query.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/reference/v2/jobs\">Jobs</a>"
   :file-git-sha "5924c3b950691084ee117331bbbea8701050c974"
   :fqcn "com.google.cloud.bigquery.JobInfo"
   :gcp.dev/certification
     {:base-seed 1790034142307
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034142307 :standard 1790034142308 :stress 1790034142309}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:42:35.579649051Z"}}
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
     :getter-doc "Returns the hash of the job resource."}
    [:string {:min 1, :gen/max 1}]]
   [:generatedId
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the service-generated id for the job."}
    [:string {:min 1, :gen/max 1}]]
   [:jobId
    {:optional true,
     :getter-doc "Returns the job identity.",
     :setter-doc "Sets the job identity."} :gcp.bigquery/JobId]
   [:selfLink
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns an URL that can be used to access the resource again. The returned URL can be used for\nGET requests."}
    [:string {:min 1, :gen/max 1}]]
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
    [:string {:min 1, :gen/max 1}]]])

(global/include-registry!
  "gcp.bigquery.JobInfo"
  {:gcp.bigquery/JobInfo schema,
   :gcp.bigquery/JobInfo.CreateDisposition CreateDisposition-schema,
   :gcp.bigquery/JobInfo.SchemaUpdateOption SchemaUpdateOption-schema,
   :gcp.bigquery/JobInfo.WriteDisposition WriteDisposition-schema})