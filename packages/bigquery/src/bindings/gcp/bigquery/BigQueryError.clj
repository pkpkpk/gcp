;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.BigQueryError
  {:doc
     "Google Cloud BigQuery Error. Objects of this class represent errors encountered by the BigQuery\nservice while executing a request. A BigQuery Job that terminated with an error has a non-null\n{@link JobStatus#getError()}. A job can also encounter errors during its execution that do not\ncause the whole job to fail (see {@link JobStatus#getExecutionErrors()}). Similarly, queries and\ninsert all requests can cause BigQuery errors that do not mean the whole operation failed (see\n{@link JobStatus#getExecutionErrors()} and {@link InsertAllResponse#getInsertErrors()}). When a\n{@link BigQueryException} is thrown the BigQuery Error that caused it, if any, can be accessed\nwith {@link BigQueryException#getError()}."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.BigQueryError"
   :gcp.dev/certification
     {:base-seed 1775130946476
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1775130946476 :standard 1775130946477 :stress 1775130946478}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:55:47.684069149Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery BigQueryError]))

(declare from-edn to-edn)

(defn ^BigQueryError from-edn
  [arg]
  (global/strict! :gcp.bigquery/BigQueryError arg)
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
  {:post [(global/strict! :gcp.bigquery/BigQueryError %)]}
  (when arg
    (cond-> {:reason (.getReason arg),
             :location (.getLocation arg),
             :message (.getMessage arg)}
      (some->> (.getDebugInfo arg)
               (not= ""))
        (assoc :debugInfo (.getDebugInfo arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Google Cloud BigQuery Error. Objects of this class represent errors encountered by the BigQuery\nservice while executing a request. A BigQuery Job that terminated with an error has a non-null\n{@link JobStatus#getError()}. A job can also encounter errors during its execution that do not\ncause the whole job to fail (see {@link JobStatus#getExecutionErrors()}). Similarly, queries and\ninsert all requests can cause BigQuery errors that do not mean the whole operation failed (see\n{@link JobStatus#getExecutionErrors()} and {@link InsertAllResponse#getInsertErrors()}). When a\n{@link BigQueryException} is thrown the BigQuery Error that caused it, if any, can be accessed\nwith {@link BigQueryException#getError()}.",
    :gcp/category :pojo,
    :gcp/key :gcp.bigquery/BigQueryError}
   [:debugInfo {:optional true} [:string {:min 1}]]
   [:location {:doc "Returns where the error occurred, if present."}
    [:string {:min 1}]]
   [:message {:doc "Returns a human-readable description of the error."}
    [:string {:min 1}]]
   [:reason
    {:doc
       "Returns short error code that summarizes the error.\n\n@see <a href=\"https://cloud.google.com/bigquery/troubleshooting-errors\">Troubleshooting\n    Errors</a>"}
    [:string {:min 1}]]])

(global/include-schema-registry! (with-meta {:gcp.bigquery/BigQueryError schema}
                                   {:gcp.global/name
                                      "gcp.bigquery.BigQueryError"}))