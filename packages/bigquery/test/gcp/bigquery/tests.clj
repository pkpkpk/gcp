(ns gcp.bigquery.tests
  (:require clojure.test
            [gcp.bigquery :as bq]
            gcp.bigquery.tests.crud-tests
            gcp.bigquery.tests.custom-tests
            gcp.bigquery.tests.dwim-tests
            gcp.bigquery.tests.field-tests
            gcp.bigquery.tests.iam-tests
            [gcp.global :as g]))

(defn run-tests []
  (clojure.test/run-tests 'gcp.bigquery.tests.dwim-tests
                          'gcp.bigquery.tests.custom-tests
                          'gcp.bigquery.tests.crud-tests
                          'gcp.bigquery.tests.field-tests
                          'gcp.bigquery.tests.iam-tests))

(comment
  (do (require :reload 'gcp.bigquery.tests) (in-ns 'gcp.bigquery.tests))

  (run-tests)

  (wipe-dataset! dataset-id)

  (testing "query/query-with-timeout/")
  (testing "list-table-data")
  (testing "insert-all")
  (testing "create-connection")
  (testing "writer")

  ;; TODO
  ;; lossless numerical + time precision
  ;; records, arrays, json
  ;; exotic scalars ie geography etc

  (g/get-schema :gcp.bigquery/Field)
  (g/get-schema :gcp.bigquery/StandardTableDefinition)
  (g/get-schema :gcp.bigquery/ViewDefinition)
  (g/get-schema :gcp.bigquery/MaterializedViewDefinition)
  (g/get-schema :gcp.bigquery/ModelTableDefinition)
  (g/get-schema :gcp.bigquery/ExternalTableDefinition)
  (g/get-schema :gcp.bigquery/SnapshotTableDefinition)
  )