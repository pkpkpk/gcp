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

(defn delete-dataset-until-gone!
  [dataset-id]
  (when (bq/get-dataset dataset-id)
    (let [deadline (+ (System/currentTimeMillis) 30000)]
      (doseq [table (bq/list-tables dataset-id)]
        (bq/delete-table table))
      (loop [delay-ms 1000]
        (let [[err ok] (try
                         [nil (bq/delete-dataset dataset-id)]
                         (catch com.google.cloud.bigquery.BigQueryException e
                           [e nil]))]
          (when err (def err err))
          (cond
            ok true
            (and (= "resourceInUse" (.getReason err)) (< (System/currentTimeMillis) deadline))
            (do
              (Thread/sleep delay-ms)
              (recur (min 2000 (* 2 delay-ms))))
            :else (throw err)))))))

(def dataset-id {:dataset "crud_test"})
(def table-id {:dataset "crud_test" :table "crud_test_table"})
(def routine-id (assoc dataset-id :routine "crud_test_routine"))

(deftest crud-test
  (let []
    (try
      (and
        (testing "dataset list/create/get/update"
          (and
            (is (sequential? (bq/list-datasets)))
            (is (nil? (bq/get-dataset dataset-id)))
            (is (g/valid? :gcp.bigquery/DatasetInfo (bq/create-dataset {:datasetId dataset-id :location "us-east1"})))
            (is (contains? (into #{} (map (comp :dataset :datasetId)) (bq/list-datasets)) "crud_test"))
            (is (g/valid? :gcp.bigquery/DatasetInfo (bq/update-dataset {:datasetId dataset-id :description "crud_test"})))
            (is (= "crud_test" (get (bq/get-dataset dataset-id) :description)))))
        (testing "table list/list-partitions/create/get/update/delete"
          (and
            (is (empty? (bq/list-tables dataset-id)))
            (is (nil? (bq/get-table table-id)))
            (is (g/valid? :gcp.bigquery/TableInfo (bq/create-table {:tableId table-id :definition {:type "TABLE"}})))
            (is (= 1 (count (bq/list-tables dataset-id))))
            (is (g/valid? :gcp.bigquery/TableInfo (bq/update-table {:tableId table-id :description "crud_test" :definition {:type "TABLE"}})))
            (is (= "crud_test" (get (bq/get-table table-id) :description)))))
        (testing "job list/create/get/wait-for/done?/delete"
          (let [job-id   {:job (str "crud_test_job_" (random-uuid))
                          :location "us-east1"}
                job-info {:jobId job-id
                          :configuration
                          {:type              "LOAD"
                           :destinationTable  {:dataset "crud_test"
                                               :table   "crud_test_job_table"}
                           :schema [{:name "name" :type "STRING"}
                                    {:name "post_abbr" :type "STRING"}]
                           :sourceUris        ["gs://cloud-samples-data/bigquery/us-states/us-states.json"]
                           :formatOptions     {:type "NEWLINE_DELIMITED_JSON"}
                           :writeDisposition  "WRITE_TRUNCATE"
                           :createDisposition "CREATE_IF_NEEDED"}}]
            (and
              (is (g/valid? :gcp.bigquery/JobInfo job-info))
              (is (sequential? (bq/list-jobs)))
              (is (nil? (bq/get-job job-id)))
              (is (g/valid? :gcp.bigquery/JobInfo (bq/create-job job-info)))
              (is (g/valid? :gcp.bigquery/JobInfo (bq/get-job job-id)))
              (is (g/valid? :gcp.bigquery/JobInfo (bq/wait-for job-id)))
              (is (true? (bq/done? job-id)))
              (is (true? (bq/delete-job job-id)))
              (is (nil? (bq/get-job job-id))))))
        (testing "routine list/create/update/get/delete"
          (let [routineInfo {:routineId routine-id
                             :routineType "PROCEDURE"
                             :body "CREATE TEMP FUNCTION AddFourAndDivide(x INT64, y INT64)\nRETURNS FLOAT64\nAS (\n  (x + 4) / y\n);\n\nSELECT\n  val, AddFourAndDivide(val, 2)\nFROM\n  UNNEST([2,3,5,8]) AS val;"}]
            (and
              (is (empty? (bq/list-routines dataset-id)))
              (is (g/valid? :gcp.bigquery/RoutineInfo (bq/create-routine routineInfo)))
              (is (not (empty? (bq/list-routines dataset-id))))
              (is (true? (bq/delete-routine routine-id))))))
        ;(testing "model   list/update/get/delete")
        ;(testing "iam     get/set/test")
        )
      (finally
        (delete-dataset-until-gone! dataset-id)))))

(comment
  (testing "query/q/query-with-timeout/")
  (testing "list-table-data")
  (testing "insert-all")
  (testing "create-connection")
  (testing "writer")
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

