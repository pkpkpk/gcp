;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.InsertAllResponse
  {:doc
     "Google Cloud BigQuery insert all response. Objects of this class possibly contain errors for an\n{@link InsertAllRequest}. If a row failed to be inserted, the non-empty list of errors associated\nto that row's index can be obtained with {@link InsertAllResponse#getErrorsFor(long)}. {@link\nInsertAllResponse#getInsertErrors()} can be used to return all errors caused by a {@link\nInsertAllRequest} as a map."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.InsertAllResponse"
   :gcp.dev/certification
     {:base-seed 0
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :protocol-hash
        "b8a5eb17212acb18f49dcb7f15a243eac9b32f54c0f054a99be660a3a25e0315"
      :reason :read-only
      :skipped true
      :timestamp "2026-02-25T20:56:04.988029707Z"}}
  (:require [gcp.bindings.bigquery.BigQueryError :as BigQueryError]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery InsertAllResponse]))

(defn ^InsertAllResponse from-edn
  [arg]
  (throw (Exception.
           "Class com.google.cloud.bigquery.InsertAllResponse is read-only")))

(defn to-edn
  [^InsertAllResponse arg]
  {:post [(global/strict! :gcp.bindings.bigquery/InsertAllResponse %)]}
  (cond-> {}
    (.hasErrors arg) (assoc :hasErrors (.hasErrors arg))
    (.getInsertErrors arg)
      (assoc :insertErrors
        (into {}
              (map (fn [[k v]] [k (map BigQueryError/to-edn v)]))
              (.getInsertErrors arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Google Cloud BigQuery insert all response. Objects of this class possibly contain errors for an\n{@link InsertAllRequest}. If a row failed to be inserted, the non-empty list of errors associated\nto that row's index can be obtained with {@link InsertAllResponse#getErrorsFor(long)}. {@link\nInsertAllResponse#getInsertErrors()} can be used to return all errors caused by a {@link\nInsertAllRequest} as a map.",
    :gcp/category :read-only,
    :gcp/key :gcp.bindings.bigquery/InsertAllResponse}
   [:hasErrors
    {:read-only? true,
     :doc
       "Returns {@code true} if no row insertion failed, {@code false} otherwise. If {@code false}\n{@link #getInsertErrors()} returns an empty map."}
    :boolean]
   [:insertErrors
    {:read-only? true,
     :doc
       "Returns all insertion errors as a map whose keys are indexes of rows that failed to insert.\nEach failed row index is associated with a non-empty list of {@link BigQueryError}."}
    [:map-of :int
     [:sequential {:min 1} :gcp.bindings.bigquery/BigQueryError]]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/InsertAllResponse schema}
    {:gcp.global/name "gcp.bindings.bigquery.InsertAllResponse"}))