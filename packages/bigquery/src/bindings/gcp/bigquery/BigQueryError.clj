;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.BigQueryError
  {:doc
     "Google Cloud BigQuery Error. Objects of this class represent errors encountered by the BigQuery\nservice while executing a request. A BigQuery Job that terminated with an error has a non-null\n{@link JobStatus#getError()}. A job can also encounter errors during its execution that do not\ncause the whole job to fail (see {@link JobStatus#getExecutionErrors()}). Similarly, queries and\ninsert all requests can cause BigQuery errors that do not mean the whole operation failed (see\n{@link JobStatus#getExecutionErrors()} and {@link InsertAllResponse#getInsertErrors()}). When a\n{@link BigQueryException} is thrown the BigQuery Error that caused it, if any, can be accessed\nwith {@link BigQueryException#getError()}."
   :file-git-sha "e73bbcc4d71182490f54209f996316a6d4ab3dbb"
   :fqcn "com.google.cloud.bigquery.BigQueryError"
   :gcp.dev/certification
     {:base-seed 1790034140566
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034140566 :standard 1790034140567 :stress 1790034140568}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:42:21.622700019Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery BigQueryError]))

(declare from-edn to-edn)

(defn ^BigQueryError from-edn
  [arg]
  (global/strict! :gcp.bigquery/BigQueryError arg)
  (let [provided-ctor-keys (clojure.set/intersection (set (keys arg))
                                                     #{:debugInfo :reason
                                                       :location :message})]
    (cond
      (and (clojure.set/subset? provided-ctor-keys
                                #{:reason :location :message}))
        (new BigQueryError
             (get arg :reason)
             (get arg :location)
             (get arg :message))
      (and (clojure.set/subset? provided-ctor-keys
                                #{:debugInfo :reason :location :message}))
        (new BigQueryError
             (get arg :reason)
             (get arg :location)
             (get arg :message)
             (get arg :debugInfo))
      :else
        (throw
          (ex-info
            "No matching constructor found for com.google.cloud.bigquery.BigQueryError"
            {:arg arg})))))

(defn to-edn
  [^BigQueryError arg]
  {:post [(global/strict! :gcp.bigquery/BigQueryError %)]}
  (when arg
    (cond-> {}
      (some->> (.getDebugInfo arg)
               (not= ""))
        (assoc :debugInfo (.getDebugInfo arg))
      (some->> (.getLocation arg)
               (not= ""))
        (assoc :location (.getLocation arg))
      (some->> (.getMessage arg)
               (not= ""))
        (assoc :message (.getMessage arg))
      (some->> (.getReason arg)
               (not= ""))
        (assoc :reason (.getReason arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Google Cloud BigQuery Error. Objects of this class represent errors encountered by the BigQuery\nservice while executing a request. A BigQuery Job that terminated with an error has a non-null\n{@link JobStatus#getError()}. A job can also encounter errors during its execution that do not\ncause the whole job to fail (see {@link JobStatus#getExecutionErrors()}). Similarly, queries and\ninsert all requests can cause BigQuery errors that do not mean the whole operation failed (see\n{@link JobStatus#getExecutionErrors()} and {@link InsertAllResponse#getInsertErrors()}). When a\n{@link BigQueryException} is thrown the BigQuery Error that caused it, if any, can be accessed\nwith {@link BigQueryException#getError()}.",
    :gcp/category :pojo,
    :gcp/key :gcp.bigquery/BigQueryError}
   [:debugInfo {:optional true} [:string {:min 1, :gen/max 1}]]
   [:location
    {:doc "Returns where the error occurred, if present.", :optional true}
    [:string {:min 1, :gen/max 1}]]
   [:message
    {:doc "Returns a human-readable description of the error.", :optional true}
    [:string {:min 1, :gen/max 1}]]
   [:reason
    {:doc
       "Returns short error code that summarizes the error.\n\n@see <a href=\"https://cloud.google.com/bigquery/troubleshooting-errors\">Troubleshooting\n    Errors</a>",
     :optional true} [:string {:min 1, :gen/max 1}]]])

(global/include-registry! "gcp.bigquery.BigQueryError"
                          {:gcp.bigquery/BigQueryError schema})