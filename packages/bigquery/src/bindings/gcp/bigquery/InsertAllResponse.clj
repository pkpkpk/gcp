;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.InsertAllResponse
  {:doc
     "Google Cloud BigQuery insert all response. Objects of this class possibly contain errors for an\n{@link InsertAllRequest}. If a row failed to be inserted, the non-empty list of errors associated\nto that row's index can be obtained with {@link InsertAllResponse#getErrorsFor(long)}. {@link\nInsertAllResponse#getInsertErrors()} can be used to return all errors caused by a {@link\nInsertAllRequest} as a map."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.InsertAllResponse"
   :gcp.dev/certification
     {:base-seed 0
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :reason :read-only
      :skipped true
      :timestamp "2026-04-09T22:56:38.436308735Z"}}
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
                (map (fn [[k v]] [k (map BigQueryError/to-edn v)]))
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
    [:map-of :i64 [:sequential {:min 1} :gcp.bigquery/BigQueryError]]]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/InsertAllResponse schema}
    {:gcp.global/name "gcp.bigquery.InsertAllResponse"}))