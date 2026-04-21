(ns gcp.bigquery.parse-args-tests
  (:require [clojure.test :refer :all]
            [gcp.bigquery.core :as bqc]))

(comment
  (do
    (require 'gcp.bigquery.parse-args-tests)
    (clojure.test/run-tests 'gcp.bigquery.parse-args-tests))
  )

(def client (bqc/client))

(def dataset-id {:project "p" :dataset "d"})
(def table-id {:dataset "d" :table "t"})
(def job-id {:job "j"})
(def query-job {:type "QUERY" :query "SELECT 1"})

(def mock-job {:jobId job-id :configuration query-job})
(def mock-table {:tableId table-id :definition {:type "TABLE"}})
(def mock-dataset {:datasetId {:dataset "d"} :description "bar"})
(def mock-routine {:routineId {:dataset "d" :routine "r"} :description "baz"})
(def mock-model {:modelId {:dataset "d" :model "m"} :description "qux"})

(deftest dataset-test
  (testing "list-datasets (->DatasetList)"
    (is (= {:op ::bqc/DatasetList :bigquery nil :projectId nil :opts nil} (bqc/->DatasetList [])))
    (is (= {:op ::bqc/DatasetList :bigquery nil :projectId "p" :opts nil} (bqc/->DatasetList ["p"])))
    (is (= {:op ::bqc/DatasetList :bigquery nil :projectId nil :opts {:pageSize 10}} (bqc/->DatasetList [{:pageSize 10}])))
    (is (= {:op ::bqc/DatasetList :bigquery client :projectId nil :opts nil} (bqc/->DatasetList [client])))
    (is (= {:op ::bqc/DatasetList :bigquery nil :projectId "p" :opts {:pageSize 10}} (bqc/->DatasetList ["p" {:pageSize 10}])))
    (is (= {:op ::bqc/DatasetList :bigquery client :projectId "p" :opts nil} (bqc/->DatasetList [client "p"])))
    (is (= {:op ::bqc/DatasetList :bigquery client :projectId nil :opts {:pageSize 10}} (bqc/->DatasetList [client {:pageSize 10}])))
    (is (= {:op ::bqc/DatasetList :bigquery client :projectId "p" :opts {:pageSize 10}} (bqc/->DatasetList [client "p" {:pageSize 10}])))
    (is (= {:op ::bqc/DatasetList :bigquery nil :projectId "p" :opts nil} (bqc/->DatasetList [nil "p"])))
    (is (= {:op ::bqc/DatasetList :bigquery nil :projectId nil :opts nil} (bqc/->DatasetList [nil nil])) "null client and null opts")
    (is (= {:op ::bqc/DatasetList :bigquery client :projectId "p" :opts {:pageSize 10}} (bqc/->DatasetList [{:bigquery client :projectId "p" :opts {:pageSize 10}}])) "callRecord")
    (is (thrown? Exception (bqc/->DatasetList [nil nil nil])) "project cannot be null if provided")
    (is (thrown? Exception (bqc/->DatasetList [client nil {:pageSize 10}])) "project cannot be null if provided"))

  (testing "create-dataset (->DatasetCreate)"
    (is (= {:op ::bqc/DatasetCreate :bigquery nil :datasetInfo {:datasetId "d"} :opts nil}
           (bqc/->DatasetCreate ["d"])))
    (is (= {:op ::bqc/DatasetCreate :bigquery nil :datasetInfo {:datasetId "d"} :opts nil}
           (bqc/->DatasetCreate [{:datasetId "d"}])))
    (is (= {:op ::bqc/DatasetCreate :bigquery nil :datasetInfo {:datasetId "d"} :opts {:fields ["DESCRIPTION"]}}
           (bqc/->DatasetCreate [{:datasetId "d"} {:fields ["DESCRIPTION"]}])))
    (is (= {:op ::bqc/DatasetCreate :bigquery client :datasetInfo {:datasetId "d"} :opts nil}
           (bqc/->DatasetCreate [client {:datasetId "d"}])))
    (is (= {:op ::bqc/DatasetCreate :bigquery client :datasetInfo {:datasetId "d"} :opts {:fields ["DESCRIPTION"]}}
           (bqc/->DatasetCreate [client {:datasetId "d"} {:fields ["DESCRIPTION"]}])))
    (is (= {:op ::bqc/DatasetCreate :bigquery client :datasetInfo {:datasetId "d"} :opts {:fields ["DESCRIPTION"]}}
           (bqc/->DatasetCreate [{:bigquery client :datasetInfo {:datasetId "d"} :opts {:fields ["DESCRIPTION"]}}])) "callRecord")
    (is (= {:op ::bqc/DatasetCreate :bigquery nil :datasetInfo mock-dataset :opts nil}
           (bqc/->DatasetCreate [mock-dataset])) "mock dataset create")
    (is (thrown? Exception (bqc/->DatasetCreate []))))

  (testing "update-dataset (->DatasetUpdate)"
    (is (= {:op ::bqc/DatasetUpdate :bigquery nil :datasetInfo {:datasetId "d"} :opts nil}
           (bqc/->DatasetUpdate [{:datasetId "d"}])))
    (is (= {:op ::bqc/DatasetUpdate :bigquery nil :datasetInfo {:datasetId "d"} :opts {:fields ["DESCRIPTION"]}}
           (bqc/->DatasetUpdate [{:datasetId "d"} {:fields ["DESCRIPTION"]}])))
    (is (= {:op ::bqc/DatasetUpdate :bigquery client :datasetInfo {:datasetId "d"} :opts nil}
           (bqc/->DatasetUpdate [client {:datasetId "d"}])))
    (is (= {:op ::bqc/DatasetUpdate :bigquery client :datasetInfo {:datasetId "d"} :opts {:fields ["DESCRIPTION"]}}
           (bqc/->DatasetUpdate [client {:datasetId "d"} {:fields ["DESCRIPTION"]}])))
    (is (= {:op ::bqc/DatasetUpdate :bigquery client :datasetInfo {:datasetId "d"} :opts {:fields ["DESCRIPTION"]}}
           (bqc/->DatasetUpdate [{:bigquery client :datasetInfo {:datasetId "d"} :opts {:fields ["DESCRIPTION"]}}])) "callRecord")
    (is (= {:op ::bqc/DatasetUpdate :bigquery nil :datasetInfo mock-dataset :opts nil}
           (bqc/->DatasetUpdate [mock-dataset])) "mock dataset update")
    (is (thrown? Exception (bqc/->DatasetUpdate []))))

  (testing "get-dataset (->DatasetGet)"
    (is (= {:op ::bqc/DatasetGet :bigquery nil :datasetId "d" :opts nil}
           (bqc/->DatasetGet ["d"])))
    (is (= {:op ::bqc/DatasetGet :bigquery nil :datasetId {:dataset "d"} :opts nil}
           (bqc/->DatasetGet [{:dataset "d"}])))
    (is (= {:op ::bqc/DatasetGet :bigquery nil :datasetId "d" :opts {:datasetView "FULL"}}
           (bqc/->DatasetGet ["d" {:datasetView "FULL"}])))
    (is (= {:op ::bqc/DatasetGet :bigquery client :datasetId "d" :opts nil}
           (bqc/->DatasetGet [client "d"])))
    (is (= {:op ::bqc/DatasetGet :bigquery client :datasetId "d" :opts {:datasetView "FULL"}}
           (bqc/->DatasetGet [client "d" {:datasetView "FULL"}])))
    (is (= {:op ::bqc/DatasetGet :bigquery client :datasetId "d" :opts {:datasetView "FULL"}}
           (bqc/->DatasetGet [{:bigquery client :datasetId "d" :opts {:datasetView "FULL"}}])) "callRecord")
    (is (= {:op ::bqc/DatasetGet :bigquery nil :datasetId {:dataset "d"} :opts nil}
           (bqc/->DatasetGet [mock-dataset])) "mock dataset resolve"))

  (testing "delete-dataset (->DatasetDelete)"
    (is (= {:op ::bqc/DatasetDelete :bigquery nil :datasetId "d" :opts nil}
           (bqc/->DatasetDelete ["d"])))
    (is (= {:op ::bqc/DatasetDelete :bigquery nil :datasetId {:dataset "d"} :opts nil}
           (bqc/->DatasetDelete [{:dataset "d"}])))
    (is (= {:op ::bqc/DatasetDelete :bigquery nil :datasetId "d" :opts {:deleteContents true}}
           (bqc/->DatasetDelete ["d" {:deleteContents true}])))
    (is (= {:op ::bqc/DatasetDelete :bigquery client :datasetId "d" :opts nil}
           (bqc/->DatasetDelete [client "d"])))
    (is (= {:op ::bqc/DatasetDelete :bigquery client :datasetId "d" :opts {:deleteContents true}}
           (bqc/->DatasetDelete [client "d" {:deleteContents true}])))
    (is (= {:op ::bqc/DatasetDelete :bigquery client :datasetId "d" :opts {:deleteContents true}}
           (bqc/->DatasetDelete [{:bigquery client :datasetId "d" :opts {:deleteContents true}}])) "callRecord")
    (is (= {:op ::bqc/DatasetDelete :bigquery nil :datasetId {:dataset "d"} :opts nil}
           (bqc/->DatasetDelete [mock-dataset])) "mock dataset resolve")))

(deftest job-test
  (testing "list-jobs (->JobList)"
    (is (= {:op ::bqc/JobList :bigquery nil :opts nil} (bqc/->JobList [])))
    (is (= {:op ::bqc/JobList :bigquery nil :opts {:pageSize 10}} (bqc/->JobList [nil {:pageSize 10}])))
    (is (= {:op ::bqc/JobList :bigquery client :opts nil} (bqc/->JobList [client])))
    (is (= {:op ::bqc/JobList :bigquery client :opts {:pageSize 10}} (bqc/->JobList [client {:pageSize 10}])))
    (is (= {:op ::bqc/JobList :bigquery client :opts {:pageSize 10}} (bqc/->JobList [{:bigquery client :opts {:pageSize 10}}])) "callRecord"))

  (testing "cancel-job (->JobCancel)"
    (is (= {:op ::bqc/JobCancel :bigquery nil :jobId job-id} (bqc/->JobCancel ["j"])))
    (is (= {:op ::bqc/JobCancel :bigquery nil :jobId {:job "j" :location "l"}} (bqc/->JobCancel [{:job "j" :location "l"}])))
    (is (= {:op ::bqc/JobCancel :bigquery client :jobId job-id} (bqc/->JobCancel [client "j"])))
    (is (= {:op ::bqc/JobCancel :bigquery client :jobId {:job "j" :location "l"}} (bqc/->JobCancel [client {:job "j" :location "l"}])))
    (is (= {:op ::bqc/JobCancel :bigquery client :jobId job-id} (bqc/->JobCancel [{:bigquery client :jobId job-id}])) "callRecord")
    (is (= {:op ::bqc/JobCancel :bigquery nil :jobId job-id} (bqc/->JobCancel [mock-job])) "mock job resolve"))

  (testing "create-job (->JobCreate)"
    (is (= {:op ::bqc/JobCreate :bigquery nil :jobInfo {:configuration query-job} :opts nil} (bqc/->JobCreate [query-job])))
    (is (= {:op ::bqc/JobCreate :bigquery nil :jobInfo {:jobId job-id :configuration query-job} :opts nil} (bqc/->JobCreate [{:jobId job-id :configuration query-job}])))
    (is (= {:op ::bqc/JobCreate :bigquery nil :jobInfo {:jobId job-id :configuration query-job} :opts {}} (bqc/->JobCreate [{:jobId job-id :configuration query-job} {}])))
    (is (= {:op ::bqc/JobCreate :bigquery client :jobInfo {:jobId job-id :configuration query-job} :opts nil} (bqc/->JobCreate [client {:jobId job-id :configuration query-job}])))
    (is (= {:op ::bqc/JobCreate :bigquery client :jobInfo {:jobId job-id :configuration query-job} :opts {}} (bqc/->JobCreate [client {:jobId job-id :configuration query-job} {}])))
    (is (= {:op ::bqc/JobCreate :bigquery client :jobInfo {:jobId job-id :configuration query-job} :opts {}} (bqc/->JobCreate [{:bigquery client :jobInfo {:jobId job-id :configuration query-job} :opts {}}])) "callRecord"))

  (testing "update-job (->JobUpdate)"
    (is (= {:op ::bqc/JobUpdate :bigquery nil :jobInfo {:jobId job-id :configuration query-job} :opts nil} (bqc/->JobUpdate [{:jobId job-id :configuration query-job}])))
    (is (= {:op ::bqc/JobUpdate :bigquery nil :jobInfo {:jobId job-id :configuration query-job} :opts {}} (bqc/->JobUpdate [{:jobId job-id :configuration query-job} {}])))
    (is (= {:op ::bqc/JobUpdate :bigquery client :jobInfo {:jobId job-id :configuration query-job} :opts nil} (bqc/->JobUpdate [client {:jobId job-id :configuration query-job}])))
    (is (= {:op ::bqc/JobUpdate :bigquery client :jobInfo {:jobId job-id :configuration query-job} :opts {}} (bqc/->JobUpdate [client {:jobId job-id :configuration query-job} {}])))
    (is (= {:op ::bqc/JobUpdate :bigquery client :jobInfo {:jobId job-id :configuration query-job} :opts {}} (bqc/->JobUpdate [{:bigquery client :jobInfo {:jobId job-id :configuration query-job} :opts {}}])) "callRecord")
    (is (= {:op ::bqc/JobUpdate :bigquery nil :jobInfo mock-job :opts nil} (bqc/->JobUpdate [mock-job])) "mock job update"))

  (testing "get-job (->JobGet)"
    (is (= {:op ::bqc/JobGet :bigquery nil :jobId job-id :opts nil} (bqc/->JobGet ["j"])))
    (is (= {:op ::bqc/JobGet :bigquery nil :jobId {:job "j" :location "l"} :opts nil} (bqc/->JobGet [{:job "j" :location "l"}])))
    (is (= {:op ::bqc/JobGet :bigquery nil :jobId job-id :opts {}} (bqc/->JobGet ["j" {}])))
    (is (= {:op ::bqc/JobGet :bigquery client :jobId job-id :opts nil} (bqc/->JobGet [client "j"])))
    (is (= {:op ::bqc/JobGet :bigquery client :jobId job-id :opts {}} (bqc/->JobGet [client "j" {}])))
    (is (= {:op ::bqc/JobGet :bigquery client :jobId job-id :opts {}} (bqc/->JobGet [{:bigquery client :jobId job-id :opts {}}])) "callRecord")
    (is (= {:op ::bqc/JobGet :bigquery nil :jobId job-id :opts nil} (bqc/->JobGet [mock-job])) "mock job resolve"))

  (testing "delete-job (->JobDelete)"
    (is (= {:op ::bqc/JobDelete :bigquery nil :jobId job-id} (bqc/->JobDelete ["j"])))
    (is (= {:op ::bqc/JobDelete :bigquery nil :jobId {:job "j" :location "l"}} (bqc/->JobDelete [{:job "j" :location "l"}])))
    (is (= {:op ::bqc/JobDelete :bigquery client :jobId job-id} (bqc/->JobDelete [client "j"])))
    (is (= {:op ::bqc/JobDelete :bigquery client :jobId {:job "j" :location "l"}} (bqc/->JobDelete [client {:job "j" :location "l"}])))
    (is (= {:op ::bqc/JobDelete :bigquery client :jobId job-id} (bqc/->JobDelete [{:bigquery client :jobId job-id}])) "callRecord")
    (is (= {:op ::bqc/JobDelete :bigquery nil :jobId job-id} (bqc/->JobDelete [mock-job])) "mock job resolve")))

(deftest table-test
  (testing "list-tables (->TableList)"
    (is (= {:op ::bqc/TableList :bigquery nil :datasetId "d" :opts nil} (bqc/->TableList ["d"])))
    (is (= {:op ::bqc/TableList :bigquery nil :datasetId dataset-id :opts nil} (bqc/->TableList ["p" "d"])))
    (is (= {:op ::bqc/TableList :bigquery nil :datasetId "d" :opts {:pageSize 5}} (bqc/->TableList ["d" {:pageSize 5}])))
    (is (= {:op ::bqc/TableList :bigquery client :datasetId "d" :opts nil} (bqc/->TableList [client "d"])))
    (is (= {:op ::bqc/TableList :bigquery nil :datasetId dataset-id :opts {:pageSize 5}} (bqc/->TableList ["p" "d" {:pageSize 5}])))
    (is (= {:op ::bqc/TableList :bigquery client :datasetId dataset-id :opts nil} (bqc/->TableList [client "p" "d"])))
    (is (= {:op ::bqc/TableList :bigquery client :datasetId "d" :opts {:pageSize 5}} (bqc/->TableList [client "d" {:pageSize 5}])))
    (is (= {:op ::bqc/TableList :bigquery client :datasetId dataset-id :opts {:pageSize 5}} (bqc/->TableList [client "p" "d" {:pageSize 5}])))
    (is (= {:op ::bqc/TableList :bigquery client :datasetId dataset-id :opts {:pageSize 5}} (bqc/->TableList [{:bigquery client :datasetId dataset-id :opts {:pageSize 5}}])) "callRecord")
    (is (= {:op ::bqc/TableList :bigquery nil :datasetId {:dataset "d"} :opts nil} (bqc/->TableList [mock-dataset])) "mock dataset resolve"))

  (testing "list-partitions (->TableListPartitions)"
    (is (= {:op ::bqc/TableListPartitions :bigquery nil :tableId table-id} (bqc/->TableListPartitions [table-id])))
    (is (= {:op ::bqc/TableListPartitions :bigquery client :tableId table-id} (bqc/->TableListPartitions [client table-id])))
    (is (= {:op ::bqc/TableListPartitions :bigquery nil :tableId table-id} (bqc/->TableListPartitions ["d" "t"])))
    (is (= {:op ::bqc/TableListPartitions :bigquery client :tableId table-id} (bqc/->TableListPartitions [client "d" "t"])))
    (is (= {:op ::bqc/TableListPartitions :bigquery nil :tableId {:project "p" :dataset "d" :table "t"}} (bqc/->TableListPartitions ["p" "d" "t"])))
    (is (= {:op ::bqc/TableListPartitions :bigquery client :tableId {:project "p" :dataset "d" :table "t"}} (bqc/->TableListPartitions [client "p" "d" "t"])))
    (is (= {:op ::bqc/TableListPartitions :bigquery client :tableId table-id} (bqc/->TableListPartitions [{:bigquery client :tableId table-id}])) "callRecord")
    (is (= {:op ::bqc/TableListPartitions :bigquery nil :tableId table-id} (bqc/->TableListPartitions [mock-table])) "mock table resolve"))

  (testing "create-table (->TableCreate)"
    (is (= {:op ::bqc/TableCreate :bigquery nil :tableInfo {:tableId table-id :definition {:type "TABLE"}} :opts nil}
           (bqc/->TableCreate [{:tableId table-id :definition {:type "TABLE"}}])))
    (is (= {:op ::bqc/TableCreate :bigquery nil :tableInfo {:tableId table-id :definition {:type "TABLE"}} :opts {:autodetectSchema true}}
           (bqc/->TableCreate [{:tableId table-id :definition {:type "TABLE"}} {:autodetectSchema true}])))
    (is (= {:op ::bqc/TableCreate :bigquery client :tableInfo {:tableId table-id :definition {:type "TABLE"}} :opts nil}
           (bqc/->TableCreate [client {:tableId table-id :definition {:type "TABLE"}}])))
    (is (= {:op ::bqc/TableCreate :bigquery client :tableInfo {:tableId table-id :definition {:type "TABLE"}} :opts {:autodetectSchema true}}
           (bqc/->TableCreate [client {:tableId table-id :definition {:type "TABLE"}} {:autodetectSchema true}])))
    (is (= {:op ::bqc/TableCreate :bigquery client :tableInfo {:tableId table-id :definition {:type "TABLE"}} :opts {:autodetectSchema true}}
           (bqc/->TableCreate [{:bigquery client :tableInfo {:tableId table-id :definition {:type "TABLE"}} :opts {:autodetectSchema true}}])) "callRecord")
    (is (= {:op ::bqc/TableCreate :bigquery nil :tableInfo mock-table :opts nil}
           (bqc/->TableCreate [mock-table])) "mock table create"))

  (testing "update-table (->TableUpdate)"
    (is (= {:op ::bqc/TableUpdate :bigquery nil :tableInfo {:tableId table-id :definition {:type "TABLE"}} :opts nil}
           (bqc/->TableUpdate [{:tableId table-id :definition {:type "TABLE"}}])))
    (is (= {:op ::bqc/TableUpdate :bigquery nil :tableInfo {:tableId table-id :definition {:type "TABLE"}} :opts {:autodetectSchema true}}
           (bqc/->TableUpdate [{:tableId table-id :definition {:type "TABLE"}} {:autodetectSchema true}])))
    (is (= {:op ::bqc/TableUpdate :bigquery client :tableInfo {:tableId table-id :definition {:type "TABLE"}} :opts nil}
           (bqc/->TableUpdate [client {:tableId table-id :definition {:type "TABLE"}}])))
    (is (= {:op ::bqc/TableUpdate :bigquery client :tableInfo {:tableId table-id :definition {:type "TABLE"}} :opts {:autodetectSchema true}}
           (bqc/->TableUpdate [client {:tableId table-id :definition {:type "TABLE"}} {:autodetectSchema true}])))
    (is (= {:op ::bqc/TableUpdate :bigquery client :tableInfo {:tableId table-id :definition {:type "TABLE"}} :opts {:autodetectSchema true}}
           (bqc/->TableUpdate [{:bigquery client :tableInfo {:tableId table-id :definition {:type "TABLE"}} :opts {:autodetectSchema true}}])) "callRecord")
    (is (= {:op ::bqc/TableUpdate :bigquery nil :tableInfo mock-table :opts nil}
           (bqc/->TableUpdate [mock-table])) "mock table update"))

  (testing "get-table (->TableGet)"
    (is (= {:op ::bqc/TableGet :bigquery nil :tableId table-id :opts nil} (bqc/->TableGet [table-id])))
    (is (= {:op ::bqc/TableGet :bigquery nil :tableId table-id :opts nil} (bqc/->TableGet ["d" "t"])))
    (is (= {:op ::bqc/TableGet :bigquery nil :tableId table-id :opts {:tableMetadataView "FULL"}} (bqc/->TableGet [table-id {:tableMetadataView "FULL"}])))
    (is (= {:op ::bqc/TableGet :bigquery client :tableId table-id :opts nil} (bqc/->TableGet [client table-id])))
    (is (= {:op ::bqc/TableGet :bigquery nil :tableId {:project "p" :dataset "d" :table "t"} :opts nil} (bqc/->TableGet ["p" "d" "t"])))
    (is (= {:op ::bqc/TableGet :bigquery nil :tableId table-id :opts {:tableMetadataView "FULL"}} (bqc/->TableGet ["d" "t" {:tableMetadataView "FULL"}])))
    (is (= {:op ::bqc/TableGet :bigquery client :tableId table-id :opts nil} (bqc/->TableGet [client "d" "t"])))
    (is (= {:op ::bqc/TableGet :bigquery client :tableId table-id :opts {:tableMetadataView "FULL"}} (bqc/->TableGet [client table-id {:tableMetadataView "FULL"}])))
    (is (= {:op ::bqc/TableGet :bigquery nil :tableId {:project "p" :dataset "d" :table "t"} :opts {:tableMetadataView "FULL"}} (bqc/->TableGet ["p" "d" "t" {:tableMetadataView "FULL"}])))
    (is (= {:op ::bqc/TableGet :bigquery client :tableId {:project "p" :dataset "d" :table "t"} :opts nil} (bqc/->TableGet [client "p" "d" "t"])))
    (is (= {:op ::bqc/TableGet :bigquery client :tableId table-id :opts {:tableMetadataView "FULL"}} (bqc/->TableGet [client "d" "t" {:tableMetadataView "FULL"}])))
    (is (= {:op ::bqc/TableGet :bigquery client :tableId {:project "p" :dataset "d" :table "t"} :opts {:tableMetadataView "FULL"}} (bqc/->TableGet [client "p" "d" "t" {:tableMetadataView "FULL"}])))
    (is (= {:op ::bqc/TableGet :bigquery client :tableId table-id :opts {:tableMetadataView "FULL"}} (bqc/->TableGet [{:bigquery client :tableId table-id :opts {:tableMetadataView "FULL"}}])) "callRecord")
    (is (= {:op ::bqc/TableGet :bigquery nil :tableId table-id :opts nil} (bqc/->TableGet [mock-table])) "mock table resolve"))

  (testing "delete-table (->TableDelete)"
    (is (= {:op ::bqc/TableDelete :bigquery nil :tableId table-id} (bqc/->TableDelete [table-id])))
    (is (= {:op ::bqc/TableDelete :bigquery client :tableId table-id} (bqc/->TableDelete [client table-id])))
    (is (= {:op ::bqc/TableDelete :bigquery nil :tableId table-id} (bqc/->TableDelete ["d" "t"])))
    (is (= {:op ::bqc/TableDelete :bigquery client :tableId table-id} (bqc/->TableDelete [client "d" "t"])))
    (is (= {:op ::bqc/TableDelete :bigquery nil :tableId {:project "p" :dataset "d" :table "t"}} (bqc/->TableDelete ["p" "d" "t"])))
    (is (= {:op ::bqc/TableDelete :bigquery client :tableId {:project "p" :dataset "d" :table "t"}} (bqc/->TableDelete [client "p" "d" "t"])))
    (is (= {:op ::bqc/TableDelete :bigquery client :tableId table-id} (bqc/->TableDelete [{:bigquery client :tableId table-id}])) "callRecord")
    (is (= {:op ::bqc/TableDelete :bigquery nil :tableId table-id} (bqc/->TableDelete [mock-table])) "mock table resolve"))

  (testing "list-table-data (->TableListData)"
    (is (= {:op ::bqc/TableListData :bigquery nil :tableId table-id :schema nil :opts nil} (bqc/->TableListData [table-id])))
    (is (= {:op ::bqc/TableListData :bigquery client :tableId table-id :schema nil :opts nil} (bqc/->TableListData [client table-id])))
    (is (= {:op ::bqc/TableListData :bigquery nil :tableId table-id :schema nil :opts nil} (bqc/->TableListData ["d" "t"])))
    (is (= {:op ::bqc/TableListData :bigquery nil :tableId table-id :schema {:fields []} :opts nil} (bqc/->TableListData [table-id {:fields []}])))
    (is (= {:op ::bqc/TableListData :bigquery nil :tableId table-id :schema nil :opts {:pageSize 10}} (bqc/->TableListData [table-id {:pageSize 10}])))
    (is (= {:op ::bqc/TableListData :bigquery client :tableId table-id :schema nil :opts nil} (bqc/->TableListData [client "d" "t"])))
    (is (= {:op ::bqc/TableListData :bigquery client :tableId table-id :schema {:fields []} :opts nil} (bqc/->TableListData [client table-id {:fields []}])))
    (is (= {:op ::bqc/TableListData :bigquery client :tableId table-id :schema nil :opts {:pageSize 10}} (bqc/->TableListData [client table-id {:pageSize 10}])))
    (is (= {:op ::bqc/TableListData :bigquery nil :tableId {:project "p" :dataset "d" :table "t"} :schema nil :opts nil} (bqc/->TableListData ["p" "d" "t"])))
    (is (= {:op ::bqc/TableListData :bigquery nil :tableId table-id :schema {:fields []} :opts nil} (bqc/->TableListData ["d" "t" {:fields []}])))
    (is (= {:op ::bqc/TableListData :bigquery nil :tableId table-id :schema nil :opts {:pageSize 10}} (bqc/->TableListData ["d" "t" {:pageSize 10}])))
    (is (= {:op ::bqc/TableListData :bigquery nil :tableId table-id :schema {:fields []} :opts {:pageSize 10}} (bqc/->TableListData [table-id {:fields []} {:pageSize 10}])))
    (is (= {:op ::bqc/TableListData :bigquery client :tableId {:project "p" :dataset "d" :table "t"} :schema nil :opts nil} (bqc/->TableListData [client "p" "d" "t"])))
    (is (= {:op ::bqc/TableListData :bigquery client :tableId table-id :schema {:fields []} :opts nil} (bqc/->TableListData [client "d" "t" {:fields []}])))
    (is (= {:op ::bqc/TableListData :bigquery client :tableId table-id :schema nil :opts {:pageSize 10}} (bqc/->TableListData [client "d" "t" {:pageSize 10}])))
    (is (= {:op ::bqc/TableListData :bigquery client :tableId table-id :schema {:fields []} :opts {:pageSize 10}} (bqc/->TableListData [client table-id {:fields []} {:pageSize 10}])))
    (is (= {:op ::bqc/TableListData :bigquery nil :tableId {:project "p" :dataset "d" :table "t"} :schema {:fields []} :opts nil} (bqc/->TableListData ["p" "d" "t" {:fields []}])))
    (is (= {:op ::bqc/TableListData :bigquery nil :tableId {:project "p" :dataset "d" :table "t"} :schema nil :opts {:pageSize 10}} (bqc/->TableListData ["p" "d" "t" {:pageSize 10}])))
    (is (= {:op ::bqc/TableListData :bigquery nil :tableId table-id :schema {:fields []} :opts {:pageSize 10}} (bqc/->TableListData ["d" "t" {:fields []} {:pageSize 10}])))
    (is (= {:op ::bqc/TableListData :bigquery client :tableId {:project "p" :dataset "d" :table "t"} :schema {:fields []} :opts nil} (bqc/->TableListData [client "p" "d" "t" {:fields []}])))
    (is (= {:op ::bqc/TableListData :bigquery client :tableId {:project "p" :dataset "d" :table "t"} :schema nil :opts {:pageSize 10}} (bqc/->TableListData [client "p" "d" "t" {:pageSize 10}])))
    (is (= {:op ::bqc/TableListData :bigquery client :tableId table-id :schema {:fields []} :opts {:pageSize 10}} (bqc/->TableListData [client "d" "t" {:fields []} {:pageSize 10}])))
    (is (= {:op ::bqc/TableListData :bigquery nil :tableId {:project "p" :dataset "d" :table "t"} :schema {:fields []} :opts {:pageSize 10}} (bqc/->TableListData ["p" "d" "t" {:fields []} {:pageSize 10}])))
    (is (= {:op ::bqc/TableListData :bigquery client :tableId {:project "p" :dataset "d" :table "t"} :schema {:fields []} :opts {:pageSize 10}} (bqc/->TableListData [client "p" "d" "t" {:fields []} {:pageSize 10}])))
    (is (= {:op ::bqc/TableListData :bigquery client :tableId table-id :schema {:fields []} :opts {:pageSize 10}} (bqc/->TableListData [{:bigquery client :tableId table-id :schema {:fields []} :opts {:pageSize 10}}])) "callRecord")
    (is (= {:op ::bqc/TableListData :bigquery nil :tableId table-id :schema nil :opts nil} (bqc/->TableListData [mock-table])) "mock table resolve")))

(deftest routine-test
  (testing "list-routines (->RoutineList)"
    (is (= {:op ::bqc/RoutineList :bigquery nil :datasetId "d" :opts nil} (bqc/->RoutineList ["d"])))
    (is (= {:op ::bqc/RoutineList :bigquery nil :datasetId {:dataset "d"} :opts nil} (bqc/->RoutineList [{:dataset "d"}])))
    (is (= {:op ::bqc/RoutineList :bigquery nil :datasetId dataset-id :opts nil} (bqc/->RoutineList ["p" "d"])))
    (is (= {:op ::bqc/RoutineList :bigquery client :datasetId "d" :opts nil} (bqc/->RoutineList [client "d"])))
    (is (= {:op ::bqc/RoutineList :bigquery nil :datasetId "d" :opts {:pageSize 10}} (bqc/->RoutineList ["d" {:pageSize 10}])))
    (is (= {:op ::bqc/RoutineList :bigquery client :datasetId dataset-id :opts nil} (bqc/->RoutineList [client "p" "d"])))
    (is (= {:op ::bqc/RoutineList :bigquery client :datasetId "d" :opts {:pageSize 10}} (bqc/->RoutineList [client "d" {:pageSize 10}])))
    (is (= {:op ::bqc/RoutineList :bigquery nil :datasetId dataset-id :opts {:pageSize 10}} (bqc/->RoutineList ["p" "d" {:pageSize 10}])))
    (is (= {:op ::bqc/RoutineList :bigquery client :datasetId dataset-id :opts {:pageSize 10}} (bqc/->RoutineList [client "p" "d" {:pageSize 10}])))
    (is (= {:op ::bqc/RoutineList :bigquery client :datasetId dataset-id :opts {:pageSize 10}} (bqc/->RoutineList [{:bigquery client :datasetId dataset-id :opts {:pageSize 10}}])) "callRecord"))

  (testing "create-routine (->RoutineCreate)"
    (is (= {:op ::bqc/RoutineCreate :bigquery nil :routineInfo {:routineId {:dataset "d" :routine "r"}} :opts nil} (bqc/->RoutineCreate [{:routineId {:dataset "d" :routine "r"}}])))
    (is (= {:op ::bqc/RoutineCreate :bigquery client :routineInfo {:routineId {:dataset "d" :routine "r"}} :opts nil} (bqc/->RoutineCreate [client {:routineId {:dataset "d" :routine "r"}}])))
    (is (= {:op ::bqc/RoutineCreate :bigquery nil :routineInfo {:routineId {:dataset "d" :routine "r"}} :opts {}} (bqc/->RoutineCreate [{:routineId {:dataset "d" :routine "r"}} {}])))
    (is (= {:op ::bqc/RoutineCreate :bigquery client :routineInfo {:routineId {:dataset "d" :routine "r"}} :opts {}} (bqc/->RoutineCreate [client {:routineId {:dataset "d" :routine "r"}} {}])))
    (is (= {:op ::bqc/RoutineCreate :bigquery client :routineInfo {:routineId {:dataset "d" :routine "r"}} :opts {}} (bqc/->RoutineCreate [{:bigquery client :routineInfo {:routineId {:dataset "d" :routine "r"}} :opts {}}])) "callRecord")
    (is (= {:op ::bqc/RoutineCreate :bigquery nil :routineInfo mock-routine :opts nil} (bqc/->RoutineCreate [mock-routine])) "mock routine create"))

  (testing "update-routine (->RoutineUpdate)"
    (is (= {:op ::bqc/RoutineUpdate :bigquery nil :routineInfo {:routineId {:dataset "d" :routine "r"}} :opts nil} (bqc/->RoutineUpdate [{:routineId {:dataset "d" :routine "r"}}])))
    (is (= {:op ::bqc/RoutineUpdate :bigquery client :routineInfo {:routineId {:dataset "d" :routine "r"}} :opts nil} (bqc/->RoutineUpdate [client {:routineId {:dataset "d" :routine "r"}}])))
    (is (= {:op ::bqc/RoutineUpdate :bigquery nil :routineInfo {:routineId {:dataset "d" :routine "r"}} :opts {}} (bqc/->RoutineUpdate [{:routineId {:dataset "d" :routine "r"}} {}])))
    (is (= {:op ::bqc/RoutineUpdate :bigquery client :routineInfo {:routineId {:dataset "d" :routine "r"}} :opts {}} (bqc/->RoutineUpdate [client {:routineId {:dataset "d" :routine "r"}} {}])))
    (is (= {:op ::bqc/RoutineUpdate :bigquery client :routineInfo {:routineId {:dataset "d" :routine "r"}} :opts {}} (bqc/->RoutineUpdate [{:bigquery client :routineInfo {:routineId {:dataset "d" :routine "r"}} :opts {}}])) "callRecord")
    (is (= {:op ::bqc/RoutineUpdate :bigquery nil :routineInfo mock-routine :opts nil} (bqc/->RoutineUpdate [mock-routine])) "mock routine update"))

  (testing "get-routine (->RoutineGet)"
    (is (= {:op ::bqc/RoutineGet :bigquery nil :routineId {:dataset "d" :routine "r"} :opts nil} (bqc/->RoutineGet [{:dataset "d" :routine "r"}])))
    (is (= {:op ::bqc/RoutineGet :bigquery client :routineId {:dataset "d" :routine "r"} :opts nil} (bqc/->RoutineGet [client {:dataset "d" :routine "r"}])))
    (is (= {:op ::bqc/RoutineGet :bigquery nil :routineId {:dataset "d" :routine "r"} :opts nil} (bqc/->RoutineGet ["d" "r"])))
    (is (= {:op ::bqc/RoutineGet :bigquery nil :routineId {:dataset "d" :routine "r"} :opts {}} (bqc/->RoutineGet [{:dataset "d" :routine "r"} {}])))
    (is (= {:op ::bqc/RoutineGet :bigquery client :routineId {:dataset "d" :routine "r"} :opts nil} (bqc/->RoutineGet [client "d" "r"])))
    (is (= {:op ::bqc/RoutineGet :bigquery client :routineId {:dataset "d" :routine "r"} :opts {}} (bqc/->RoutineGet [client {:dataset "d" :routine "r"} {}])))
    (is (= {:op ::bqc/RoutineGet :bigquery nil :routineId {:project "p" :dataset "d" :routine "r"} :opts nil} (bqc/->RoutineGet ["p" "d" "r"])))
    (is (= {:op ::bqc/RoutineGet :bigquery nil :routineId {:dataset "d" :routine "r"} :opts {}} (bqc/->RoutineGet ["d" "r" {}])))
    (is (= {:op ::bqc/RoutineGet :bigquery client :routineId {:project "p" :dataset "d" :routine "r"} :opts nil} (bqc/->RoutineGet [client "p" "d" "r"])))
    (is (= {:op ::bqc/RoutineGet :bigquery client :routineId {:dataset "d" :routine "r"} :opts {}} (bqc/->RoutineGet [client "d" "r" {}])))
    (is (= {:op ::bqc/RoutineGet :bigquery client :routineId {:project "p" :dataset "d" :routine "r"} :opts {}} (bqc/->RoutineGet [client "p" "d" "r" {}])))
    (is (= {:op ::bqc/RoutineGet :bigquery client :routineId {:project "p" :dataset "d" :routine "r"} :opts {}} (bqc/->RoutineGet [{:bigquery client :routineId {:project "p" :dataset "d" :routine "r"} :opts {}}])) "callRecord")
    (is (= {:op ::bqc/RoutineGet :bigquery nil :routineId {:dataset "d" :routine "r"} :opts nil} (bqc/->RoutineGet [mock-routine])) "mock routine resolve"))

  (testing "delete-routine (->RoutineDelete)"
    (is (= {:op ::bqc/RoutineDelete :bigquery nil :routineId {:dataset "d" :routine "r"}} (bqc/->RoutineDelete [{:dataset "d" :routine "r"}])))
    (is (= {:op ::bqc/RoutineDelete :bigquery client :routineId {:dataset "d" :routine "r"}} (bqc/->RoutineDelete [client {:dataset "d" :routine "r"}])))
    (is (= {:op ::bqc/RoutineDelete :bigquery nil :routineId {:dataset "d" :routine "r"}} (bqc/->RoutineDelete ["d" "r"])))
    (is (= {:op ::bqc/RoutineDelete :bigquery client :routineId {:dataset "d" :routine "r"}} (bqc/->RoutineDelete [client "d" "r"])))
    (is (= {:op ::bqc/RoutineDelete :bigquery nil :routineId {:project "p" :dataset "d" :routine "r"}} (bqc/->RoutineDelete ["p" "d" "r"])))
    (is (= {:op ::bqc/RoutineDelete :bigquery client :routineId {:project "p" :dataset "d" :routine "r"}} (bqc/->RoutineDelete [client "p" "d" "r"])))
    (is (= {:op ::bqc/RoutineDelete :bigquery client :routineId {:project "p" :dataset "d" :routine "r"}} (bqc/->RoutineDelete [{:bigquery client :routineId {:project "p" :dataset "d" :routine "r"}}])) "callRecord")
    (is (= {:op ::bqc/RoutineDelete :bigquery nil :routineId {:dataset "d" :routine "r"}} (bqc/->RoutineDelete [mock-routine])) "mock routine resolve")))

(deftest model-test
  (testing "list-models (->ModelList)"
    (is (= {:op ::bqc/ModelList :bigquery nil :datasetId "d" :opts nil} (bqc/->ModelList ["d"])))
    (is (= {:op ::bqc/ModelList :bigquery nil :datasetId {:dataset "d"} :opts nil} (bqc/->ModelList [{:dataset "d"}])))
    (is (= {:op ::bqc/ModelList :bigquery nil :datasetId dataset-id :opts nil} (bqc/->ModelList ["p" "d"])))
    (is (= {:op ::bqc/ModelList :bigquery client :datasetId "d" :opts nil} (bqc/->ModelList [client "d"])))
    (is (= {:op ::bqc/ModelList :bigquery nil :datasetId "d" :opts {:pageSize 10}} (bqc/->ModelList ["d" {:pageSize 10}])))
    (is (= {:op ::bqc/ModelList :bigquery client :datasetId dataset-id :opts nil} (bqc/->ModelList [client "p" "d"])))
    (is (= {:op ::bqc/ModelList :bigquery client :datasetId "d" :opts {:pageSize 10}} (bqc/->ModelList [client "d" {:pageSize 10}])))
    (is (= {:op ::bqc/ModelList :bigquery nil :datasetId dataset-id :opts {:pageSize 10}} (bqc/->ModelList ["p" "d" {:pageSize 10}])))
    (is (= {:op ::bqc/ModelList :bigquery client :datasetId dataset-id :opts {:pageSize 10}} (bqc/->ModelList [client "p" "d" {:pageSize 10}])))
    (is (= {:op ::bqc/ModelList :bigquery client :datasetId dataset-id :opts {:pageSize 10}} (bqc/->ModelList [{:bigquery client :datasetId dataset-id :opts {:pageSize 10}}])) "callRecord"))

  (testing "update-model (->ModelUpdate)"
    (is (= {:op ::bqc/ModelUpdate :bigquery nil :modelInfo {:modelId {:dataset "d" :model "m"}} :opts nil} (bqc/->ModelUpdate [{:modelId {:dataset "d" :model "m"}}])))
    (is (= {:op ::bqc/ModelUpdate :bigquery client :modelInfo {:modelId {:dataset "d" :model "m"}} :opts nil} (bqc/->ModelUpdate [client {:modelId {:dataset "d" :model "m"}}])))
    (is (= {:op ::bqc/ModelUpdate :bigquery nil :modelInfo {:modelId {:dataset "d" :model "m"}} :opts {}} (bqc/->ModelUpdate [{:modelId {:dataset "d" :model "m"}} {}])))
    (is (= {:op ::bqc/ModelUpdate :bigquery client :modelInfo {:modelId {:dataset "d" :model "m"}} :opts {}} (bqc/->ModelUpdate [client {:modelId {:dataset "d" :model "m"}} {}])))
    (is (= {:op ::bqc/ModelUpdate :bigquery client :modelInfo {:modelId {:dataset "d" :model "m"}} :opts {}} (bqc/->ModelUpdate [{:bigquery client :modelInfo {:modelId {:dataset "d" :model "m"}} :opts {}}])) "callRecord")
    (is (= {:op ::bqc/ModelUpdate :bigquery nil :modelInfo mock-model :opts nil} (bqc/->ModelUpdate [mock-model])) "mock model update"))

  (testing "get-model (->ModelGet)"
    (is (= {:op ::bqc/ModelGet :bigquery nil :modelId {:dataset "d" :model "m"} :opts nil} (bqc/->ModelGet [{:dataset "d" :model "m"}])))
    (is (= {:op ::bqc/ModelGet :bigquery client :modelId {:dataset "d" :model "m"} :opts nil} (bqc/->ModelGet [client {:dataset "d" :model "m"}])))
    (is (= {:op ::bqc/ModelGet :bigquery nil :modelId {:dataset "d" :model "m"} :opts nil} (bqc/->ModelGet ["d" "m"])))
    (is (= {:op ::bqc/ModelGet :bigquery nil :modelId {:dataset "d" :model "m"} :opts {}} (bqc/->ModelGet [{:dataset "d" :model "m"} {}])))
    (is (= {:op ::bqc/ModelGet :bigquery client :modelId {:dataset "d" :model "m"} :opts nil} (bqc/->ModelGet [client "d" "m"])))
    (is (= {:op ::bqc/ModelGet :bigquery client :modelId {:dataset "d" :model "m"} :opts {}} (bqc/->ModelGet [client {:dataset "d" :model "m"} {}])))
    (is (= {:op ::bqc/ModelGet :bigquery nil :modelId {:project "p" :dataset "d" :model "m"} :opts nil} (bqc/->ModelGet ["p" "d" "m"])))
    (is (= {:op ::bqc/ModelGet :bigquery nil :modelId {:dataset "d" :model "m"} :opts {}} (bqc/->ModelGet ["d" "m" {}])))
    (is (= {:op ::bqc/ModelGet :bigquery client :modelId {:project "p" :dataset "d" :model "m"} :opts nil} (bqc/->ModelGet [client "p" "d" "m"])))
    (is (= {:op ::bqc/ModelGet :bigquery client :modelId {:dataset "d" :model "m"} :opts {}} (bqc/->ModelGet [client "d" "m" {}])))
    (is (= {:op ::bqc/ModelGet :bigquery client :modelId {:project "p" :dataset "d" :model "m"} :opts {}} (bqc/->ModelGet [client "p" "d" "m" {}])))
    (is (= {:op ::bqc/ModelGet :bigquery client :modelId {:project "p" :dataset "d" :model "m"} :opts {}} (bqc/->ModelGet [{:bigquery client :modelId {:project "p" :dataset "d" :model "m"} :opts {}}])) "callRecord")
    (is (= {:op ::bqc/ModelGet :bigquery nil :modelId {:dataset "d" :model "m"} :opts nil} (bqc/->ModelGet [mock-model])) "mock model resolve"))

  (testing "delete-model (->ModelDelete)"
    (is (= {:op ::bqc/ModelDelete :bigquery nil :modelId {:dataset "d" :model "m"}} (bqc/->ModelDelete [{:dataset "d" :model "m"}])))
    (is (= {:op ::bqc/ModelDelete :bigquery client :modelId {:dataset "d" :model "m"}} (bqc/->ModelDelete [client {:dataset "d" :model "m"}])))
    (is (= {:op ::bqc/ModelDelete :bigquery nil :modelId {:dataset "d" :model "m"}} (bqc/->ModelDelete ["d" "m"])))
    (is (= {:op ::bqc/ModelDelete :bigquery client :modelId {:dataset "d" :model "m"}} (bqc/->ModelDelete [client "d" "m"])))
    (is (= {:op ::bqc/ModelDelete :bigquery nil :modelId {:project "p" :dataset "d" :model "m"}} (bqc/->ModelDelete ["p" "d" "m"])))
    (is (= {:op ::bqc/ModelDelete :bigquery client :modelId {:project "p" :dataset "d" :model "m"}} (bqc/->ModelDelete [client "p" "d" "m"])))
    (is (= {:op ::bqc/ModelDelete :bigquery client :modelId {:project "p" :dataset "d" :model "m"}} (bqc/->ModelDelete [{:bigquery client :modelId {:project "p" :dataset "d" :model "m"}}])) "callRecord")
    (is (= {:op ::bqc/ModelDelete :bigquery nil :modelId {:dataset "d" :model "m"}} (bqc/->ModelDelete [mock-model])) "mock model resolve")))

(deftest iam-test
  (testing "get-iam-policy (->GetIamPolicy)"
    (is (= {:op ::bqc/GetIamPolicy :bigquery nil :tableId table-id :opts nil} (bqc/->GetIamPolicy [table-id])))
    (is (= {:op ::bqc/GetIamPolicy :bigquery client :tableId table-id :opts nil} (bqc/->GetIamPolicy [client table-id])))
    (is (= {:op ::bqc/GetIamPolicy :bigquery nil :tableId table-id :opts nil} (bqc/->GetIamPolicy ["d" "t"])))
    (is (= {:op ::bqc/GetIamPolicy :bigquery nil :tableId table-id :opts {:requestedPolicyVersion 1}} (bqc/->GetIamPolicy [table-id {:requestedPolicyVersion 1}])))
    (is (= {:op ::bqc/GetIamPolicy :bigquery client :tableId table-id :opts nil} (bqc/->GetIamPolicy [client "d" "t"])))
    (is (= {:op ::bqc/GetIamPolicy :bigquery client :tableId table-id :opts {:requestedPolicyVersion 1}} (bqc/->GetIamPolicy [client table-id {:requestedPolicyVersion 1}])))
    (is (= {:op ::bqc/GetIamPolicy :bigquery nil :tableId {:project "p" :dataset "d" :table "t"} :opts nil} (bqc/->GetIamPolicy ["p" "d" "t"])))
    (is (= {:op ::bqc/GetIamPolicy :bigquery nil :tableId table-id :opts {:requestedPolicyVersion 1}} (bqc/->GetIamPolicy ["d" "t" {:requestedPolicyVersion 1}])))
    (is (= {:op ::bqc/GetIamPolicy :bigquery client :tableId {:project "p" :dataset "d" :table "t"} :opts nil} (bqc/->GetIamPolicy [client "p" "d" "t"])))
    (is (= {:op ::bqc/GetIamPolicy :bigquery client :tableId table-id :opts {:requestedPolicyVersion 1}} (bqc/->GetIamPolicy [client "d" "t" {:requestedPolicyVersion 1}])))
    (is (= {:op ::bqc/GetIamPolicy :bigquery client :tableId {:project "p" :dataset "d" :table "t"} :opts {:requestedPolicyVersion 1}} (bqc/->GetIamPolicy [client "p" "d" "t" {:requestedPolicyVersion 1}])))
    (is (= {:op ::bqc/GetIamPolicy :bigquery client :tableId table-id :opts {:requestedPolicyVersion 1}} (bqc/->GetIamPolicy [{:bigquery client :tableId table-id :opts {:requestedPolicyVersion 1}}])) "callRecord")
    (is (= {:op ::bqc/GetIamPolicy :bigquery nil :tableId table-id :opts nil} (bqc/->GetIamPolicy [mock-table])) "mock table resolve"))

  (testing "set-iam-policy (->SetIamPolicy)"
    (is (= {:op ::bqc/SetIamPolicy :bigquery nil :tableId table-id :policy {:bindings [] :version 0} :opts nil} (bqc/->SetIamPolicy [table-id {:bindings [] :version 0}])))
    (is (= {:op ::bqc/SetIamPolicy :bigquery client :tableId table-id :policy {:bindings [] :version 0} :opts nil} (bqc/->SetIamPolicy [client table-id {:bindings [] :version 0}])))
    (is (= {:op ::bqc/SetIamPolicy :bigquery nil :tableId table-id :policy {:bindings [] :version 0} :opts nil} (bqc/->SetIamPolicy ["d" "t" {:bindings [] :version 0}])))
    (is (= {:op ::bqc/SetIamPolicy :bigquery nil :tableId table-id :policy {:bindings [] :version 0} :opts {:requestedPolicyVersion 1}} (bqc/->SetIamPolicy [table-id {:bindings [] :version 0} {:requestedPolicyVersion 1}])))
    (is (= {:op ::bqc/SetIamPolicy :bigquery client :tableId table-id :policy {:bindings [] :version 0} :opts nil} (bqc/->SetIamPolicy [client "d" "t" {:bindings [] :version 0}])))
    (is (= {:op ::bqc/SetIamPolicy :bigquery nil :tableId {:project "p" :dataset "d" :table "t"} :policy {:bindings [] :version 0} :opts nil} (bqc/->SetIamPolicy ["p" "d" "t" {:bindings [] :version 0}])))
    (is (= {:op ::bqc/SetIamPolicy :bigquery client :tableId table-id :policy {:bindings [] :version 0} :opts {:requestedPolicyVersion 1}} (bqc/->SetIamPolicy [client table-id {:bindings [] :version 0} {:requestedPolicyVersion 1}])))
    (is (= {:op ::bqc/SetIamPolicy :bigquery nil :tableId table-id :policy {:bindings [] :version 0} :opts {:requestedPolicyVersion 1}} (bqc/->SetIamPolicy ["d" "t" {:bindings [] :version 0} {:requestedPolicyVersion 1}])))
    (is (= {:op ::bqc/SetIamPolicy :bigquery client :tableId {:project "p" :dataset "d" :table "t"} :policy {:bindings [] :version 0} :opts nil} (bqc/->SetIamPolicy [client "p" "d" "t" {:bindings [] :version 0}])))
    (is (= {:op ::bqc/SetIamPolicy :bigquery client :tableId table-id :policy {:bindings [] :version 0} :opts {:requestedPolicyVersion 1}} (bqc/->SetIamPolicy [client "d" "t" {:bindings [] :version 0} {:requestedPolicyVersion 1}])))
    (is (= {:op ::bqc/SetIamPolicy :bigquery nil :tableId {:project "p" :dataset "d" :table "t"} :policy {:bindings [] :version 0} :opts {:requestedPolicyVersion 1}} (bqc/->SetIamPolicy ["p" "d" "t" {:bindings [] :version 0} {:requestedPolicyVersion 1}])))
    (is (= {:op ::bqc/SetIamPolicy :bigquery client :tableId {:project "p" :dataset "d" :table "t"} :policy {:bindings [] :version 0} :opts {:requestedPolicyVersion 1}} (bqc/->SetIamPolicy [client "p" "d" "t" {:bindings [] :version 0} {:requestedPolicyVersion 1}])))
    (is (= {:op ::bqc/SetIamPolicy :bigquery client :tableId table-id :policy {:bindings [] :version 0} :opts {:requestedPolicyVersion 1}} (bqc/->SetIamPolicy [{:bigquery client :tableId table-id :policy {:bindings [] :version 0} :opts {:requestedPolicyVersion 1}}])) "callRecord")
    (is (= {:op ::bqc/SetIamPolicy :bigquery nil :tableId table-id :policy {:bindings [] :version 0} :opts nil} (bqc/->SetIamPolicy [mock-table {:bindings [] :version 0}])) "mock table resolve"))

  (testing "test-iam-permissions (->TestIamPermissions)"
    (is (= {:op ::bqc/TestIamPermissions :bigquery nil :tableId table-id :permissions ["p1"] :opts nil} (bqc/->TestIamPermissions [table-id ["p1"]])))
    (is (= {:op ::bqc/TestIamPermissions :bigquery client :tableId table-id :permissions ["p1"] :opts nil} (bqc/->TestIamPermissions [client table-id ["p1"]])))
    (is (= {:op ::bqc/TestIamPermissions :bigquery nil :tableId table-id :permissions ["p1"] :opts nil} (bqc/->TestIamPermissions ["d" "t" ["p1"]])))
    (is (= {:op ::bqc/TestIamPermissions :bigquery nil :tableId table-id :permissions ["p1"] :opts {:requestedPolicyVersion 1}} (bqc/->TestIamPermissions [table-id ["p1"] {:requestedPolicyVersion 1}])))
    (is (= {:op ::bqc/TestIamPermissions :bigquery client :tableId table-id :permissions ["p1"] :opts nil} (bqc/->TestIamPermissions [client "d" "t" ["p1"]])))
    (is (= {:op ::bqc/TestIamPermissions :bigquery nil :tableId {:project "p" :dataset "d" :table "t"} :permissions ["p1"] :opts nil} (bqc/->TestIamPermissions ["p" "d" "t" ["p1"]])))
    (is (= {:op ::bqc/TestIamPermissions :bigquery client :tableId table-id :permissions ["p1"] :opts {:requestedPolicyVersion 1}} (bqc/->TestIamPermissions [client table-id ["p1"] {:requestedPolicyVersion 1}])))
    (is (= {:op ::bqc/TestIamPermissions :bigquery nil :tableId table-id :permissions ["p1"] :opts {:requestedPolicyVersion 1}} (bqc/->TestIamPermissions ["d" "t" ["p1"] {:requestedPolicyVersion 1}])))
    (is (= {:op ::bqc/TestIamPermissions :bigquery client :tableId {:project "p" :dataset "d" :table "t"} :permissions ["p1"] :opts nil} (bqc/->TestIamPermissions [client "p" "d" "t" ["p1"]])))
    (is (= {:op ::bqc/TestIamPermissions :bigquery client :tableId table-id :permissions ["p1"] :opts {:requestedPolicyVersion 1}} (bqc/->TestIamPermissions [client "d" "t" ["p1"] {:requestedPolicyVersion 1}])))
    (is (= {:op ::bqc/TestIamPermissions :bigquery nil :tableId {:project "p" :dataset "d" :table "t"} :permissions ["p1"] :opts {:requestedPolicyVersion 1}} (bqc/->TestIamPermissions ["p" "d" "t" ["p1"] {:requestedPolicyVersion 1}])))
    (is (= {:op ::bqc/TestIamPermissions :bigquery client :tableId {:project "p" :dataset "d" :table "t"} :permissions ["p1"] :opts {:requestedPolicyVersion 1}} (bqc/->TestIamPermissions [client "p" "d" "t" ["p1"] {:requestedPolicyVersion 1}])))
    (is (= {:op ::bqc/TestIamPermissions :bigquery client :tableId table-id :permissions ["p1"] :opts {:requestedPolicyVersion 1}} (bqc/->TestIamPermissions [{:bigquery client :tableId table-id :permissions ["p1"] :opts {:requestedPolicyVersion 1}}])) "callRecord")
    (is (= {:op ::bqc/TestIamPermissions :bigquery nil :tableId table-id :permissions ["p1"] :opts nil} (bqc/->TestIamPermissions [mock-table ["p1"]])) "mock table resolve")))

(deftest query-test
  (testing "query (->Query)"
    (is (= {:op ::bqc/Query :bigquery nil :configuration query-job :jobId nil :opts nil} (bqc/->Query [query-job])))
    (is (= {:op ::bqc/Query :bigquery client :configuration query-job :jobId nil :opts nil} (bqc/->Query [client query-job])))
    (is (= {:op ::bqc/Query :bigquery nil :configuration query-job :jobId job-id :opts nil} (bqc/->Query [query-job "j"])))
    (is (= {:op ::bqc/Query :bigquery nil :configuration query-job :jobId job-id :opts nil} (bqc/->Query [query-job job-id])) "map-based jobId arity-2")
    (is (= {:op ::bqc/Query :bigquery client :configuration query-job :jobId job-id :opts nil} (bqc/->Query [client query-job "j"])))
    (is (= {:op ::bqc/Query :bigquery client :configuration query-job :jobId job-id :opts nil} (bqc/->Query [client query-job job-id])) "map-based jobId arity-3")
    (is (= {:op ::bqc/Query :bigquery nil :configuration query-job :jobId job-id :opts {}} (bqc/->Query [query-job "j" {}])))
    (is (= {:op ::bqc/Query :bigquery nil :configuration query-job :jobId job-id :opts {}} (bqc/->Query [query-job job-id {}])) "map-based jobId arity-3 with opts")
    (is (= {:op ::bqc/Query :bigquery client :configuration query-job :jobId job-id :opts {}} (bqc/->Query [client query-job "j" {}])))
    (is (= {:op ::bqc/Query :bigquery client :configuration query-job :jobId job-id :opts {}} (bqc/->Query [client query-job job-id {}])) "map-based jobId arity-4")
    (is (= {:op ::bqc/Query :bigquery client :configuration query-job :jobId job-id :opts {}} (bqc/->Query [{:bigquery client :configuration query-job :jobId job-id :opts {}}])) "callRecord")
    (is (= {:op ::bqc/Query :bigquery nil :configuration query-job :jobId job-id :opts nil} (bqc/->Query [query-job mock-job])) "mock job resolve"))

  (testing "q (->Q)"
    (is (= {:op ::bqc/Query :configuration {:type "QUERY" :query "SELECT 1"}} (bqc/->Q ["SELECT 1"])))
    (is (= {:op ::bqc/Query :bigquery client :configuration {:type "QUERY" :query "SELECT 1"}} (bqc/->Q [client "SELECT 1"])))
    (is (= {:op ::bqc/Query :configuration {:type "QUERY" :query "SELECT 1" :positionalParameters [{:value "A"}]}} (bqc/->Q ["SELECT 1" [{:value "A"}]])))
    (is (= {:op ::bqc/Query :configuration {:type "QUERY" :query "SELECT 1" :namedParameters {:p {:value "A"}}}} (bqc/->Q ["SELECT 1" {:p {:value "A"}}])))
    (is (= {:op ::bqc/Query :bigquery client :configuration {:type "QUERY" :query "SELECT 1" :positionalParameters [{:value "A"}]}} (bqc/->Q [client "SELECT 1" [{:value "A"}]])))
    (is (= {:op ::bqc/Query :bigquery client :configuration {:type "QUERY" :query "SELECT 1" :namedParameters {:p {:value "A"}}}} (bqc/->Q [client "SELECT 1" {:p {:value "A"}}]))))

  (testing "query-with-timeout (->QueryWithTimeout)"
    (is (= {:op ::bqc/QueryWithTimeout :bigquery nil :configuration query-job :jobId job-id :timeoutMs 1000 :opts nil} (bqc/->QueryWithTimeout [query-job "j" 1000])))
    (is (= {:op ::bqc/QueryWithTimeout :bigquery client :configuration query-job :jobId job-id :timeoutMs 1000 :opts nil} (bqc/->QueryWithTimeout [client query-job "j" 1000])))
    (is (= {:op ::bqc/QueryWithTimeout :bigquery nil :configuration query-job :jobId job-id :timeoutMs 1000 :opts {}} (bqc/->QueryWithTimeout [query-job "j" 1000 {}])))
    (is (= {:op ::bqc/QueryWithTimeout :bigquery client :configuration query-job :jobId job-id :timeoutMs 1000 :opts {}} (bqc/->QueryWithTimeout [client query-job "j" 1000 {}])))
    (is (= {:op ::bqc/QueryWithTimeout :bigquery client :configuration query-job :jobId job-id :timeoutMs 1000 :opts {}} (bqc/->QueryWithTimeout [{:bigquery client :configuration query-job :jobId job-id :timeoutMs 1000 :opts {}}])) "callRecord")
    (is (= {:op ::bqc/QueryWithTimeout :bigquery nil :configuration query-job :jobId job-id :timeoutMs 1000 :opts nil} (bqc/->QueryWithTimeout [query-job mock-job 1000])) "mock job resolve")))



(deftest data-test
  (testing "insert-all (->InsertAll)"
    (is (= {:op ::bqc/InsertAll :bigquery nil :insertAllRequest {:table table-id :rows [{:insertId "1"}]}} (bqc/->InsertAll [{:table table-id :rows [{:insertId "1"}]}])))
    (is (= {:op ::bqc/InsertAll :bigquery client :insertAllRequest {:table table-id :rows [{:insertId "1"}]}} (bqc/->InsertAll [client {:table table-id :rows [{:insertId "1"}]}])))
    (is (= {:op ::bqc/InsertAll :bigquery nil :insertAllRequest {:table table-id :rows [{:insertId "1"}]}} (bqc/->InsertAll [table-id [{:insertId "1"}]])))
    (is (= {:op ::bqc/InsertAll :bigquery client :insertAllRequest {:table table-id :rows [{:insertId "1"}]}} (bqc/->InsertAll [client table-id [{:insertId "1"}]])))
    (is (= {:op ::bqc/InsertAll :bigquery nil :insertAllRequest {:table table-id :rows [{:insertId "1"}]}} (bqc/->InsertAll ["d" "t" [{:insertId "1"}]])))
    (is (= {:op ::bqc/InsertAll :bigquery client :insertAllRequest {:table table-id :rows [{:insertId "1"}]}} (bqc/->InsertAll [client "d" "t" [{:insertId "1"}]])))
    (is (= {:op ::bqc/InsertAll :bigquery nil :insertAllRequest {:table {:project "p" :dataset "d" :table "t"} :rows [{:insertId "1"}]}} (bqc/->InsertAll ["p" "d" "t" [{:insertId "1"}]])))
    (is (= {:op ::bqc/InsertAll :bigquery client :insertAllRequest {:table {:project "p" :dataset "d" :table "t"} :rows [{:insertId "1"}]}} (bqc/->InsertAll [client "p" "d" "t" [{:insertId "1"}]])))
    (is (= {:op ::bqc/InsertAll :bigquery client :insertAllRequest {:table table-id :rows [{:insertId "1"}]}} (bqc/->InsertAll [{:bigquery client :insertAllRequest {:table table-id :rows [{:insertId "1"}]}}])) "callRecord")
    (is (= {:op ::bqc/InsertAll :bigquery nil :insertAllRequest {:table table-id :rows [{:insertId "1"}]}} (bqc/->InsertAll [mock-table [{:insertId "1"}]])) "mock table resolve")))

(deftest other-test
  (testing "create-connection (->ConnectionCreate)"
    (is (= {:op ::bqc/ConnectionCreate :bigquery nil :connectionSettings nil} (bqc/->ConnectionCreate [])))
    (is (= {:op ::bqc/ConnectionCreate :bigquery client :connectionSettings nil} (bqc/->ConnectionCreate [client])))
    (is (= {:op ::bqc/ConnectionCreate :bigquery nil :connectionSettings {:maxResults 10}} (bqc/->ConnectionCreate [nil {:maxResults 10}])))
    (is (= {:op ::bqc/ConnectionCreate :bigquery client :connectionSettings {:maxResults 10}} (bqc/->ConnectionCreate [client {:maxResults 10}])))
    (is (= {:op ::bqc/ConnectionCreate :bigquery client :connectionSettings {:maxResults 10}} (bqc/->ConnectionCreate [{:bigquery client :connectionSettings {:maxResults 10}}])) "callRecord"))

  (testing "writer (->Writer)"
    (is (= {:op ::bqc/Writer :bigquery nil :writeChannelConfiguration {:destinationTable table-id} :jobId nil} (bqc/->Writer [{:destinationTable table-id}])))
    (is (= {:op ::bqc/Writer :bigquery client :writeChannelConfiguration {:destinationTable table-id} :jobId nil} (bqc/->Writer [client {:destinationTable table-id}])))
    (is (= {:op ::bqc/Writer :bigquery nil :writeChannelConfiguration {:destinationTable table-id} :jobId job-id} (bqc/->Writer ["j" {:destinationTable table-id}])))
    (is (= {:op ::bqc/Writer :bigquery nil :writeChannelConfiguration {:destinationTable table-id} :jobId job-id} (bqc/->Writer [job-id {:destinationTable table-id}])) "map-based jobId arity-2")
    (is (= {:op ::bqc/Writer :bigquery client :writeChannelConfiguration {:destinationTable table-id} :jobId job-id} (bqc/->Writer [client "j" {:destinationTable table-id}])))
    (is (= {:op ::bqc/Writer :bigquery client :writeChannelConfiguration {:destinationTable table-id} :jobId job-id} (bqc/->Writer [client job-id {:destinationTable table-id}])) "map-based jobId arity-3")
    (is (= {:op ::bqc/Writer :bigquery client :writeChannelConfiguration {:destinationTable table-id} :jobId job-id} (bqc/->Writer [{:bigquery client :writeChannelConfiguration {:destinationTable table-id} :jobId job-id}])) "callRecord")
    (is (= {:op ::bqc/Writer :bigquery nil :writeChannelConfiguration {:destinationTable table-id} :jobId job-id} (bqc/->Writer [mock-job {:destinationTable table-id}])) "mock job resolve"))

  (testing "wait-for (->JobWaitFor)"
    (is (= {:op ::bqc/JobWaitFor :bigquery nil :jobId job-id} (bqc/->JobWaitFor ["j"])))
    (is (= {:op ::bqc/JobWaitFor :bigquery nil :jobId job-id} (bqc/->JobWaitFor [job-id])))
    (is (= {:op ::bqc/JobWaitFor :bigquery client :jobId job-id} (bqc/->JobWaitFor [client "j"])))
    (is (= {:op ::bqc/JobWaitFor :bigquery client :jobId job-id} (bqc/->JobWaitFor [client job-id])))
    (is (= {:op ::bqc/JobWaitFor :bigquery nil :jobId job-id :retryOptions [{:maxAttempts 1}]} (bqc/->JobWaitFor [job-id {:retryOptions [{:maxAttempts 1}]}])))
    (is (= {:op ::bqc/JobWaitFor :bigquery client :jobId job-id :retryOptions [{:maxAttempts 1}]} (bqc/->JobWaitFor [client job-id {:retryOptions [{:maxAttempts 1}]}]))))
    (is (= {:op ::bqc/JobWaitFor :bigquery nil :jobId job-id} (bqc/->JobWaitFor [mock-job])) "mock job resolve")

  (testing "done? (->JobIsDone)"
    (is (= {:op ::bqc/JobIsDone :bigquery nil :jobId job-id} (bqc/->JobIsDone ["j"])))
    (is (= {:op ::bqc/JobIsDone :bigquery nil :jobId job-id} (bqc/->JobIsDone [job-id])))
    (is (= {:op ::bqc/JobIsDone :bigquery client :jobId job-id} (bqc/->JobIsDone [client "j"])))
    (is (= {:op ::bqc/JobIsDone :bigquery client :jobId job-id} (bqc/->JobIsDone [client job-id])))
    (is (= {:op ::bqc/JobIsDone :bigquery nil :jobId job-id :bigQueryRetryConfig {}} (bqc/->JobIsDone [job-id {:bigQueryRetryConfig {}}])) "job-id vs clientable ambiguity check")
    (is (= {:op ::bqc/JobIsDone :bigquery client :jobId job-id :bigQueryRetryConfig {}} (bqc/->JobIsDone [client job-id {:bigQueryRetryConfig {}}])))))
    (is (= {:op ::bqc/JobIsDone :bigquery nil :jobId job-id} (bqc/->JobIsDone [mock-job])) "mock job resolve")
