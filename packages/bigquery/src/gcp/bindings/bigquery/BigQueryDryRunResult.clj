;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.BigQueryDryRunResult
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.BigQueryDryRunResult"
   :gcp.dev/certification
     {:base-seed 0
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :protocol-hash
        "f4effb663e7e6af6dfc9051b90dfeb17820045df78c3901c15d91ac97dbdc861"
      :reason :read-only
      :skipped true
      :timestamp "2026-03-03T00:28:06.172826398Z"}}
  (:require [gcp.bindings.bigquery.BigQueryResultStats :as BigQueryResultStats]
            [gcp.bindings.bigquery.Parameter :as Parameter]
            [gcp.bindings.bigquery.Schema :as Schema]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery BigQueryDryRunResult]))

(declare from-edn to-edn)

(defn ^BigQueryDryRunResult from-edn
  [arg]
  (throw
    (Exception.
      "Class com.google.cloud.bigquery.BigQueryDryRunResult is read-only")))

(defn to-edn
  [^BigQueryDryRunResult arg]
  {:post [(global/strict! :gcp.bindings.bigquery/BigQueryDryRunResult %)]}
  (cond-> {}
    (.getQueryParameters arg)
      (assoc :queryParameters (map Parameter/to-edn (.getQueryParameters arg)))
    (.getSchema arg) (assoc :schema (Schema/to-edn (.getSchema arg)))
    (.getStatistics arg) (assoc :statistics
                           (BigQueryResultStats/to-edn (.getStatistics arg)))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :interface,
    :gcp/key :gcp.bindings.bigquery/BigQueryDryRunResult}
   [:queryParameters
    {:read-only? true,
     :doc
       "Returns query parameters for standard SQL queries by extracting undeclare query parameters from\nthe dry run job. See more information:\nhttps://developers.google.com/resources/api-libraries/documentation/bigquery/v2/java/latest/com/google/api/services/bigquery/model/JobStatistics2.html#getUndeclaredQueryParameters--"}
    [:sequential {:min 1} :gcp.bindings.bigquery/Parameter]]
   [:schema
    {:read-only? true,
     :doc
       "Returns the schema of the results. Null if the schema is not supplied."}
    :gcp.bindings.bigquery/Schema]
   [:statistics {:read-only? true, :doc "Returns some processing statistics"}
    :gcp.bindings.bigquery/BigQueryResultStats]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/BigQueryDryRunResult schema}
    {:gcp.global/name "gcp.bindings.bigquery.BigQueryDryRunResult"}))