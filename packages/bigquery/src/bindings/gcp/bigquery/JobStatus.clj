;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.JobStatus
  {:doc
     "A Google BigQuery Job status. Objects of this class can be examined when polling an asynchronous\njob to see if the job completed."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.JobStatus"
   :gcp.dev/certification
     {:base-seed 0
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :reason :read-only
      :skipped true
      :timestamp "2026-04-18T08:03:59.493597521Z"}}
  (:require [gcp.bigquery.BigQueryError :as BigQueryError]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery JobStatus JobStatus$State]))

(declare from-edn to-edn State-from-edn State-to-edn)

(def State-schema
  [:enum
   {:closed true,
    :doc "Possible states that a BigQuery Job can assume.",
    :gcp/category :nested/string-enum,
    :gcp/key :gcp.bigquery/JobStatus.State} "PENDING" "RUNNING" "DONE"])

(defn ^JobStatus from-edn
  [arg]
  (throw (Exception. "Class com.google.cloud.bigquery.JobStatus is read-only")))

(defn to-edn
  [^JobStatus arg]
  {:post [(global/strict! :gcp.bigquery/JobStatus %)]}
  (when arg
    (cond-> {}
      (.getError arg) (assoc :error (BigQueryError/to-edn (.getError arg)))
      (seq (.getExecutionErrors arg)) (assoc :executionErrors
                                        (mapv BigQueryError/to-edn
                                          (.getExecutionErrors arg)))
      (.getState arg) (assoc :state (.name (.getState arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "A Google BigQuery Job status. Objects of this class can be examined when polling an asynchronous\njob to see if the job completed.",
    :gcp/category :read-only,
    :gcp/key :gcp.bigquery/JobStatus}
   [:error
    {:read-only? true,
     :optional true,
     :doc
       "Returns the final error result of the job. If present, indicates that the job has completed and\nwas unsuccessful.\n\n@see <a href=\"https://cloud.google.com/bigquery/troubleshooting-errors\">Troubleshooting\n    Errors</a>"}
    :gcp.bigquery/BigQueryError]
   [:executionErrors
    {:read-only? true,
     :optional true,
     :doc
       "Returns all errors encountered during the running of the job. Errors here do not necessarily\nmean that the job has completed or was unsuccessful.\n\n@see <a href=\"https://cloud.google.com/bigquery/troubleshooting-errors\">Troubleshooting\n    Errors</a>"}
    [:sequential {:min 1} :gcp.bigquery/BigQueryError]]
   [:state
    {:read-only? true,
     :optional true,
     :doc
       "Returns the state of the job. A {@link State#PENDING} job is waiting to be executed. A {@link\nState#RUNNING} is being executed. A {@link State#DONE} job has completed either succeeding or\nfailing. If failed {@link #getError()} will be non-null."}
    [:enum {:closed true} "PENDING" "RUNNING" "DONE"]]])

(global/include-schema-registry! (with-meta {:gcp.bigquery/JobStatus schema,
                                             :gcp.bigquery/JobStatus.State
                                               State-schema}
                                   {:gcp.global/name "gcp.bigquery.JobStatus"}))