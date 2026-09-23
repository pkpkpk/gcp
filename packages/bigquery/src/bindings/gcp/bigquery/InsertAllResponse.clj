;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.InsertAllResponse
  {:doc
     "Google Cloud BigQuery insert all response. Objects of this class possibly contain errors for an\n{@link InsertAllRequest}. If a row failed to be inserted, the non-empty list of errors associated\nto that row's index can be obtained with {@link InsertAllResponse#getErrorsFor(long)}. {@link\nInsertAllResponse#getInsertErrors()} can be used to return all errors caused by a {@link\nInsertAllRequest} as a map."
   :file-git-sha "36af39e222ccf992d15ddbf82148b98e377b40f3"
   :fqcn "com.google.cloud.bigquery.InsertAllResponse"
   :gcp.dev/certification
     {:base-seed 0
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :reason :read-only
      :skipped true
      :timestamp "2026-09-21T23:43:25.854639240Z"}}
  (:require [gcp.bigquery.BigQueryError :as BigQueryError]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery InsertAllResponse]))

(declare from-edn to-edn)

(defn ^InsertAllResponse from-edn
  [arg]
  (throw (Exception.
           "Class com.google.cloud.bigquery.InsertAllResponse is read-only")))

(defn to-edn
  [^InsertAllResponse arg]
  {:post [(global/strict! :gcp.bigquery/InsertAllResponse %)]}
  (when arg
    (cond-> {}
      (seq (.getInsertErrors arg))
        (assoc :insertErrors
          (into {}
                (map (fn [[k v]] [k (mapv BigQueryError/to-edn v)]))
                (.getInsertErrors arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "Google Cloud BigQuery insert all response. Objects of this class possibly contain errors for an\n{@link InsertAllRequest}. If a row failed to be inserted, the non-empty list of errors associated\nto that row's index can be obtained with {@link InsertAllResponse#getErrorsFor(long)}. {@link\nInsertAllResponse#getInsertErrors()} can be used to return all errors caused by a {@link\nInsertAllRequest} as a map.",
    :gcp/category :read-only,
    :gcp/key :gcp.bigquery/InsertAllResponse}
   [:insertErrors
    {:read-only? true,
     :optional true,
     :doc
       "Returns all insertion errors as a map whose keys are indexes of rows that failed to insert.\nEach failed row index is associated with a non-empty list of {@link BigQueryError}."}
    [:map-of :i64
     [:sequential {:min 1, :gen/max 2} :gcp.bigquery/BigQueryError]]]])

(global/include-registry! "gcp.bigquery.InsertAllResponse"
                          {:gcp.bigquery/InsertAllResponse schema})