(ns gcp.bigquery.tests
  (:require clojure.test
            gcp.bigquery.tests.crud-tests
            gcp.bigquery.tests.custom-tests
            gcp.bigquery.tests.dwim-tests
            gcp.bigquery.tests.iam-tests))

(defn run-tests []
  (clojure.test/run-tests 'gcp.bigquery.tests.dwim-tests
                          'gcp.bigquery.tests.custom-tests
                          'gcp.bigquery.tests.crud-tests
                          'gcp.bigquery.tests.iam-tests))

(comment

  (wipe-dataset! dataset-id)

  (testing "query/q/query-with-timeout/")
  (testing "list-table-data")
  (testing "insert-all")
  (testing "create-connection")
  (testing "writer")
  (testing "iam get/set/test")
  ;; wait-for, done?

  (and
    ;; standard view materializedview model external snapshot
    (testing ":gcp.bigquery/StandardTableDefinition")
    (testing ":gcp.bigquery/ViewDefinition")
    (testing ":gcp.bigquery/MaterializedViewDefinition")
    (testing ":gcp.bigquery/ModelTableDefinition")
    (testing ":gcp.bigquery/ExternalTableDefinition")
    (testing ":gcp.bigquery/SnapshotTableDefinition")
    )

  )