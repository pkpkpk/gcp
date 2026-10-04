(ns gcp.bigquery.crud-tests
  (:require [clojure.test :refer :all]
            [gcp.bigquery :as bq]
            [gcp.global :as g]))

(comment
  (do
    (require :reload
             'gcp.dwim
             '[gcp.bigquery.core :as bqc]
             '[gcp.bigquery :as bq]
             'gcp.bigquery.crud-tests)
    (in-ns 'gcp.bigquery.crud-tests)
    (clojure.test/run-tests 'gcp.bigquery.crud-tests))
  )

(def dataset-id {:dataset "crud_test"})

(deftest crud-test
  (let []
    (try
      (and
        (testing "dataset list/create/get/update"
          (and
            (is (sequential? (bq/list-datasets)))
            (is (nil? (bq/get-dataset dataset-id)))
            (is (g/valid? :gcp.bigquery/DatasetInfo (bq/create-dataset dataset-id)))
            (is (g/valid? :gcp.bigquery/DatasetInfo (bq/update-dataset {:datasetId dataset-id
                                                                        :description "crud test"})))
            (is (= "crud test" (get (bq/get-dataset dataset-id) :description)))))
        (testing "table   list/list-partitions/create/get/update/delete"
          ;; standard view materializedview model external snapshot
          )
        (testing "job     list/create/get/cancel/delete/wait-for/done?")
        (testing "routine list/create/update/get/delete")
        (testing "model   list/update/get/delete")
        (testing "iam     get/set/test")
        )
      (finally
        (bq/delete-dataset dataset-id)
        ))))

(comment
  (testing "query/q/query-with-timeout/")
  (testing "list-table-data")
  (testing "insert-all")
  (testing "create-connection")
  (testing "writer")
  ;; wait-for, done?
  )