;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.BigQueryError
  {:doc
     "Google Cloud BigQuery Error. Objects of this class represent errors encountered by the BigQuery\nservice while executing a request. A BigQuery Job that terminated with an error has a non-null\n{@link JobStatus#getError()}. A job can also encounter errors during its execution that do not\ncause the whole job to fail (see {@link JobStatus#getExecutionErrors()}). Similarly, queries and\ninsert all requests can cause BigQuery errors that do not mean the whole operation failed (see\n{@link JobStatus#getExecutionErrors()} and {@link InsertAllResponse#getInsertErrors()}). When a\n{@link BigQueryException} is thrown the BigQuery Error that caused it, if any, can be accessed\nwith {@link BigQueryException#getError()}."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.BigQueryError"
   :gcp.dev/certification
     {:base-seed 1771377767008
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771377767008 :standard 1771377767009 :stress 1771377767010}
      :protocol-hash
        "32175171656ac95c72455bff25fe019346fb52c916efee805be149692e31cec4"
      :timestamp "2026-02-18T01:22:47.147317721Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery BigQueryError]))

(defn ^BigQueryError from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/BigQueryError arg)
  (cond
    (and (contains? arg :debugInfo)
         (contains? arg :reason)
         (contains? arg :location)
         (contains? arg :message))
      (new BigQueryError
           (get arg :reason)
           (get arg :location)
           (get arg :message)
           (get arg :debugInfo))
    (and (contains? arg :reason)
         (contains? arg :location)
         (contains? arg :message))
      (new BigQueryError
           (get arg :reason)
           (get arg :location)
           (get arg :message))
    :else
      (throw
        (ex-info
          "No matching constructor found for com.google.cloud.bigquery.BigQueryError"
          {:arg arg}))))

(defn to-edn
  [^BigQueryError arg]
  {:post [(global/strict! :gcp.bindings.bigquery/BigQueryError %)]}
  (cond-> {:reason (.getReason arg),
           :location (.getLocation arg),
           :message (.getMessage arg)}
    (.getDebugInfo arg) (assoc :debugInfo (.getDebugInfo arg))))

(def schema
  [:map
   {:closed true,
    :doc
      "Google Cloud BigQuery Error. Objects of this class represent errors encountered by the BigQuery\nservice while executing a request. A BigQuery Job that terminated with an error has a non-null\n{@link JobStatus#getError()}. A job can also encounter errors during its execution that do not\ncause the whole job to fail (see {@link JobStatus#getExecutionErrors()}). Similarly, queries and\ninsert all requests can cause BigQuery errors that do not mean the whole operation failed (see\n{@link JobStatus#getExecutionErrors()} and {@link InsertAllResponse#getInsertErrors()}). When a\n{@link BigQueryException} is thrown the BigQuery Error that caused it, if any, can be accessed\nwith {@link BigQueryException#getError()}.",
    :gcp/category :pojo,
    :gcp/key :gcp.bindings.bigquery/BigQueryError}
   [:debugInfo {:optional true} [:string {:min 1}]]
   [:location {:doc "Returns where the error occurred, if present."}
    [:string {:min 1}]]
   [:message {:doc "Returns a human-readable description of the error."}
    [:string {:min 1}]]
   [:reason
    {:doc
       "Returns short error code that summarizes the error.\n\n@see <a href=\"https://cloud.google.com/bigquery/troubleshooting-errors\">Troubleshooting\n    Errors</a>"}
    [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/BigQueryError schema}
    {:gcp.global/name "gcp.bindings.bigquery.BigQueryError"}))