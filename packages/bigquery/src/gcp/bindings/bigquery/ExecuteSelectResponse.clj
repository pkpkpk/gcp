;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.ExecuteSelectResponse
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.ExecuteSelectResponse"
   :gcp.dev/certification
     {:base-seed 1772497768330
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1772497768330 :standard 1772497768331 :stress 1772497768332}
      :protocol-hash
        "f4effb663e7e6af6dfc9051b90dfeb17820045df78c3901c15d91ac97dbdc861"
      :timestamp "2026-03-03T00:29:28.963534982Z"}}
  (:require [gcp.bigquery.custom.BigQuerySQLException :as BigQuerySQLException]
            [gcp.bindings.bigquery.BigQueryResult :as BigQueryResult]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery ExecuteSelectResponse
            ExecuteSelectResponse$Builder]))

(declare from-edn to-edn)

(defn ^ExecuteSelectResponse from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/ExecuteSelectResponse arg)
  (let [builder (ExecuteSelectResponse/newBuilder)]
    (when (some? (get arg :bigQuerySQLException))
      (.setBigQuerySQLException builder
                                (BigQuerySQLException/from-edn
                                  (get arg :bigQuerySQLException))))
    (when (some? (get arg :isSuccessful))
      (.setIsSuccessful builder (get arg :isSuccessful)))
    (when (some? (get arg :resultSet))
      (.setResultSet builder (BigQueryResult/from-edn (get arg :resultSet))))
    (.build builder)))

(defn to-edn
  [^ExecuteSelectResponse arg]
  {:post [(global/strict! :gcp.bindings.bigquery/ExecuteSelectResponse %)]}
  (cond-> {:isSuccessful (.getIsSuccessful arg)}
    (.getBigQuerySQLException arg) (assoc :bigQuerySQLException
                                     (BigQuerySQLException/to-edn
                                       (.getBigQuerySQLException arg)))
    (.getResultSet arg) (assoc :resultSet
                          (BigQueryResult/to-edn (.getResultSet arg)))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/ExecuteSelectResponse}
   [:bigQuerySQLException {:optional true}
    :gcp.bigquery.custom/BigQuerySQLException]
   [:isSuccessful {:getter-doc nil, :setter-doc nil} :boolean]
   [:resultSet {:optional true} :gcp.bindings.bigquery/BigQueryResult]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/ExecuteSelectResponse schema}
    {:gcp.global/name "gcp.bindings.bigquery.ExecuteSelectResponse"}))