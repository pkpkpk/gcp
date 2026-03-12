;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.BigQueryResult
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.BigQueryResult"
   :gcp.dev/certification
     {:base-seed 0
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :protocol-hash
        "f4effb663e7e6af6dfc9051b90dfeb17820045df78c3901c15d91ac97dbdc861"
      :reason :read-only
      :skipped true
      :timestamp "2026-03-03T00:27:51.640360431Z"}}
  (:require [gcp.bindings.bigquery.BigQueryResultStats :as BigQueryResultStats]
            [gcp.bindings.bigquery.Schema :as Schema]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery BigQueryResult]))

(declare from-edn to-edn)

(defn ^BigQueryResult from-edn
  [arg]
  (throw (Exception.
           "Class com.google.cloud.bigquery.BigQueryResult is read-only")))

(defn to-edn
  [^BigQueryResult arg]
  {:post [(global/strict! :gcp.bindings.bigquery/BigQueryResult %)]}
  (cond-> {}
    (.getBigQueryResultStats arg) (assoc :bigQueryResultStats
                                    (BigQueryResultStats/to-edn
                                      (.getBigQueryResultStats arg)))
    (.getResultSet arg) (assoc :resultSet (.getResultSet arg))
    (.getSchema arg) (assoc :schema (Schema/to-edn (.getSchema arg)))
    (.getTotalRows arg) (assoc :totalRows (.getTotalRows arg))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :interface,
    :gcp/key :gcp.bindings.bigquery/BigQueryResult}
   [:bigQueryResultStats {:read-only? true}
    :gcp.bindings.bigquery/BigQueryResultStats]
   [:resultSet {:read-only? true}
    (gcp.global/instance-schema java.sql.ResultSet)]
   [:schema {:read-only? true, :doc "Returns the schema of the results."}
    :gcp.bindings.bigquery/Schema]
   [:totalRows
    {:read-only? true,
     :doc
       "Returns the total number of rows in the complete result set, which can be more than the number\nof rows in the first page of results. This might return -1 if the query is long running and the\njob is not complete at the time this object is returned."}
    :int]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/BigQueryResult schema}
    {:gcp.global/name "gcp.bindings.bigquery.BigQueryResult"}))