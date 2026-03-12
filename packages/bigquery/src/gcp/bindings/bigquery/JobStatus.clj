;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.JobStatus
  {:doc
     "A Google BigQuery Job status. Objects of this class can be examined when polling an asynchronous\njob to see if the job completed."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.JobStatus"
   :gcp.dev/certification
     {:base-seed 0
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :protocol-hash
        "32175171656ac95c72455bff25fe019346fb52c916efee805be149692e31cec4"
      :reason :read-only
      :skipped true
      :timestamp "2026-02-18T01:24:12.718398548Z"}}
  (:require [gcp.bindings.bigquery.BigQueryError :as BigQueryError]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery JobStatus JobStatus$State]))

(declare JobStatus$State-from-edn JobStatus$State-to-edn)

(defn ^JobStatus$State JobStatus$State-from-edn
  [arg]
  (JobStatus$State/valueOf arg))

(defn JobStatus$State-to-edn [^JobStatus$State arg] (.name arg))

(def JobStatus$State-schema
  [:enum
   {:closed true,
    :doc "Possible states that a BigQuery Job can assume.",
    :gcp/category :nested/string-enum,
    :gcp/key :gcp.bindings.bigquery/JobStatus.State} "PENDING" "RUNNING"
   "DONE"])

(defn ^JobStatus from-edn
  [arg]
  (throw (Exception. "Class com.google.cloud.bigquery.JobStatus is read-only")))

(defn to-edn
  [^JobStatus arg]
  {:post [(global/strict! :gcp.bindings.bigquery/JobStatus %)]}
  (cond-> {}
    (.getError arg) (assoc :error (BigQueryError/to-edn (.getError arg)))
    (.getExecutionErrors arg) (assoc :executionErrors
                                (map BigQueryError/to-edn
                                  (.getExecutionErrors arg)))
    (.getState arg) (assoc :state (JobStatus$State-to-edn (.getState arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "A Google BigQuery Job status. Objects of this class can be examined when polling an asynchronous\njob to see if the job completed.",
    :gcp/category :read-only,
    :gcp/key :gcp.bindings.bigquery/JobStatus}
   [:error
    {:read-only? true,
     :doc
       "Returns the final error result of the job. If present, indicates that the job has completed and\nwas unsuccessful.\n\n@see <a href=\"https://cloud.google.com/bigquery/troubleshooting-errors\">Troubleshooting\n    Errors</a>"}
    :gcp.bindings.bigquery/BigQueryError]
   [:executionErrors
    {:read-only? true,
     :doc
       "Returns all errors encountered during the running of the job. Errors here do not necessarily\nmean that the job has completed or was unsuccessful.\n\n@see <a href=\"https://cloud.google.com/bigquery/troubleshooting-errors\">Troubleshooting\n    Errors</a>"}
    [:seqable {:min 1} :gcp.bindings.bigquery/BigQueryError]]
   [:state
    {:read-only? true,
     :doc
       "Returns the state of the job. A {@link State#PENDING} job is waiting to be executed. A {@link\nState#RUNNING} is being executed. A {@link State#DONE} job has completed either succeeding or\nfailing. If failed {@link #getError()} will be non-null."}
    :gcp.bindings.bigquery/JobStatus.State]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/JobStatus schema,
              :gcp.bindings.bigquery/JobStatus.State JobStatus$State-schema}
    {:gcp.global/name "gcp.bindings.bigquery.JobStatus"}))