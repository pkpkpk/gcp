;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.JobStatus
  {:doc
     "A Google BigQuery Job status. Objects of this class can be examined when polling an asynchronous\njob to see if the job completed."
   :file-git-sha "9a73160c782cbf532b3228c24e049bbee5ca7cd4"
   :fqcn "com.google.cloud.bigquery.JobStatus"
   :gcp.dev/certification
     {:base-seed 0
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :reason :read-only
      :skipped true
      :timestamp "2026-09-21T23:42:21.957835233Z"}}
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
    [:sequential {:min 1, :gen/max 2} :gcp.bigquery/BigQueryError]]
   [:state
    {:read-only? true,
     :optional true,
     :doc
       "Returns the state of the job. A {@link State#PENDING} job is waiting to be executed. A {@link\nState#RUNNING} is being executed. A {@link State#DONE} job has completed either succeeding or\nfailing. If failed {@link #getError()} will be non-null."}
    [:enum {:closed true} "PENDING" "RUNNING" "DONE"]]])

(global/include-registry! "gcp.bigquery.JobStatus"
                          {:gcp.bigquery/JobStatus schema,
                           :gcp.bigquery/JobStatus.State State-schema})