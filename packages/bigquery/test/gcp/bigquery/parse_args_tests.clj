(ns gcp.bigquery.parse-args-tests
  (:require [clojure.test :refer :all]
            [gcp.bigquery :as bq]
            [gcp.bigquery.core :as bqc]
            [gcp.global :as g]))

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

(defmacro catch-ex-data [form]
  `(try
     ~form
     (catch Exception e#
       (ex-data e#))))

(deftest Dataset-test
  (and
    (testing "DatasetList"
      (and
        (testing "good args"
          (and
            (is (= {:op ::bq/DatasetList :bigquery nil    :projectId nil :opts nil}            (bqc/->DatasetList [])))
            (is (= {:op ::bq/DatasetList :bigquery nil    :projectId "p" :opts nil}            (bqc/->DatasetList ["p"])))
            (is (= {:op ::bq/DatasetList :bigquery client :projectId "p" :opts {:pageSize 10}} (bqc/->DatasetList [client "p" {:pageSize 10}])))
            (is (= {:op ::bq/DatasetList :bigquery client :projectId nil :opts nil}            (bqc/->DatasetList [client])))
            (is (= {:op ::bq/DatasetList :bigquery nil    :projectId "p" :opts {:pageSize 10}} (bqc/->DatasetList ["p" {:pageSize 10}])))
            (is (= {:op ::bq/DatasetList :bigquery client :projectId "p" :opts nil}            (bqc/->DatasetList [client "p"])))
            (is (= {:op ::bq/DatasetList :bigquery client :projectId nil :opts {:pageSize 10}} (bqc/->DatasetList [client {:pageSize 10}])))
            (is (= {:op ::bq/DatasetList :bigquery client :projectId "p" :opts {:pageSize 10}} (bqc/->DatasetList [client "p" {:pageSize 10}])))
            (is (= {:op ::bq/DatasetList :bigquery nil    :projectId "p" :opts nil}            (bqc/->DatasetList [nil "p"])))
            (is (= {:op ::bq/DatasetList :bigquery nil    :projectId nil :opts nil}            (bqc/->DatasetList [nil nil])) "null client and null opts")
            (is (= {:op ::bq/DatasetList :bigquery client :projectId "p" :opts {:pageSize 10}} (bqc/->DatasetList [{:bigquery client :projectId "p" :opts {:pageSize 10}}])) "cmd")))
        (testing "bad args"
          (and
            (is (thrown? Exception (bqc/->DatasetList [nil nil nil])) "project cannot be null if provided")
            (is (thrown? Exception (bqc/->DatasetList [client nil {:pageSize 10}])) "project cannot be null if provided")
            (is (thrown? Exception (bqc/->DatasetList [nil "foo" (take 10000000 (iterate inc 0))]) "big lazy seqs are not realized during parse"))
            (is (thrown? Exception (bqc/->DatasetList [1 2 3 4 5])) "arity miss — too many args")
            (is (thrown? Exception (bqc/->DatasetList [nil "project" (apply str (repeat 1000000 \x))])) "giant string in opts slot")
            (is (thrown? Exception (bqc/->DatasetList [nil "project" (last (take 5000 (iterate vector :deep)))])) "deeply nested vector")
            (is (thrown-with-msg? Exception #"get-schema :gcp\.bigquery/DatasetList\)" (bqc/->DatasetList [1 2 3 4])) "arity error prompts schema discovery")
            (is (thrown-with-msg? Exception #"get-schema :gcp\.bigquery/DatasetList\)" (bqc/->DatasetList [42 "project" {}])) "parse error prompts schema discovery")))
        (testing "error model is queryable"
          (let [ex-data (catch-ex-data (bqc/->DatasetList [42]))]
            (and
              (is (= 1 (:arity ex-data)))
              (is (= 0 (get-in ex-data [:mismatch :index])))
              (is (contains? (set (get-in ex-data [:mismatch :expected])) :gcp.bigquery/clientable))
              (is (string? (:args ex-data)) "The ex-data itself is safe — no lazy seqs"))))))
    (testing "DatasetCreate"
      (and
        (is (= {:op ::bq/DatasetCreate :bigquery nil :datasetInfo {:datasetId "d"} :opts nil} (bqc/->DatasetCreate ["d"])))
        (is (= {:op ::bq/DatasetCreate :bigquery nil :datasetInfo {:datasetId "d"} :opts nil} (bqc/->DatasetCreate [{:datasetId "d"}])))
        (is (= {:op ::bq/DatasetCreate :bigquery nil :datasetInfo {:datasetId "d"} :opts {:fields ["DESCRIPTION"]}} (bqc/->DatasetCreate [{:datasetId "d"} {:fields ["DESCRIPTION"]}])))
        (is (= {:op ::bq/DatasetCreate :bigquery client :datasetInfo {:datasetId "d"} :opts nil} (bqc/->DatasetCreate [client {:datasetId "d"}])))
        (is (= {:op ::bq/DatasetCreate :bigquery client :datasetInfo {:datasetId "d"} :opts {:fields ["DESCRIPTION"]}} (bqc/->DatasetCreate [client {:datasetId "d"} {:fields ["DESCRIPTION"]}])))
        (let [call (bqc/->DatasetCreate [mock-dataset])]
          (is (and (= (:op call) ::bq/DatasetCreate) (g/valid? ::bq/DatasetCreate call)) "mock dataset create"))
        (is (thrown? Exception (bqc/->DatasetCreate [])))
        (is (= {:op ::bq/DatasetCreate :bigquery client :datasetInfo {:datasetId "d"} :opts {:fields ["DESCRIPTION"]}} (bqc/->DatasetCreate [{:bigquery client :datasetInfo {:datasetId "d"} :opts {:fields ["DESCRIPTION"]}}])) "cmd")))
    (testing "DatasetUpdate"
      (and
        (is (= {:op ::bq/DatasetUpdate :bigquery nil :datasetInfo {:datasetId "d"} :opts nil} (bqc/->DatasetUpdate [{:datasetId "d"}])))
        (is (= {:op ::bq/DatasetUpdate :bigquery nil :datasetInfo {:datasetId "d"} :opts {:fields ["DESCRIPTION"]}} (bqc/->DatasetUpdate [{:datasetId "d"} {:fields ["DESCRIPTION"]}])))
        (is (= {:op ::bq/DatasetUpdate :bigquery client :datasetInfo {:datasetId "d"} :opts nil} (bqc/->DatasetUpdate [client {:datasetId "d"}])))
        (is (= {:op ::bq/DatasetUpdate :bigquery client :datasetInfo {:datasetId "d"} :opts {:fields ["DESCRIPTION"]}} (bqc/->DatasetUpdate [client {:datasetId "d"} {:fields ["DESCRIPTION"]}])))
        (is (= {:op ::bq/DatasetUpdate :bigquery client :datasetInfo {:datasetId "d"} :opts {:fields ["DESCRIPTION"]}} (bqc/->DatasetUpdate [{:bigquery client :datasetInfo {:datasetId "d"} :opts {:fields ["DESCRIPTION"]}}])) "cmd")
        (let [call (bqc/->DatasetUpdate [mock-dataset])]
          (is (and (= (:op call) ::bq/DatasetUpdate) (g/valid? ::bq/DatasetUpdate call)) "mock dataset update"))
        (is (thrown? Exception (bqc/->DatasetUpdate [])))))
    (testing "DatasetGet"
      (and
        (is (= {:op ::bq/DatasetGet :bigquery nil :datasetId "d" :opts nil} (bqc/->DatasetGet ["d"])))
        (is (= {:op ::bq/DatasetGet :bigquery nil :datasetId {:dataset "d"} :opts nil} (bqc/->DatasetGet [{:dataset "d"}])))
        (is (= {:op ::bq/DatasetGet :bigquery nil :datasetId "d" :opts {:datasetView "FULL"}} (bqc/->DatasetGet ["d" {:datasetView "FULL"}])))
        (is (= {:op ::bq/DatasetGet :bigquery client :datasetId "d" :opts nil} (bqc/->DatasetGet [client "d"])))
        (is (= {:op ::bq/DatasetGet :bigquery client :datasetId "d" :opts {:datasetView "FULL"}} (bqc/->DatasetGet [client "d" {:datasetView "FULL"}])))
        (is (= {:op ::bq/DatasetGet :bigquery client :datasetId "d" :opts {:datasetView "FULL"}} (bqc/->DatasetGet [{:bigquery client :datasetId "d" :opts {:datasetView "FULL"}}])) "cmd")
        (let [call (bqc/->DatasetGet [mock-dataset])]
          (is (and (= (:op call) ::bq/DatasetGet) (g/valid? ::bq/DatasetGet call)) "mock dataset resolve"))))
    (testing "DatasetDelete"
      (and
        (is (= {:op ::bq/DatasetDelete :bigquery nil    :datasetId "d" :opts nil}                    (bqc/->DatasetDelete ["d"])))
        (is (= {:op ::bq/DatasetDelete :bigquery nil    :datasetId {:dataset "d"} :opts nil}         (bqc/->DatasetDelete [{:dataset "d"}])))
        (is (= {:op ::bq/DatasetDelete :bigquery nil    :datasetId "d" :opts {:deleteContents true}} (bqc/->DatasetDelete ["d" {:deleteContents true}])))
        (is (= {:op ::bq/DatasetDelete :bigquery client :datasetId "d" :opts nil}                    (bqc/->DatasetDelete [client "d"])))
        (is (= {:op ::bq/DatasetDelete :bigquery client :datasetId "d" :opts {:deleteContents true}} (bqc/->DatasetDelete [client "d" {:deleteContents true}])))
        (is (= {:op ::bq/DatasetDelete :bigquery client :datasetId "d" :opts {:deleteContents true}} (bqc/->DatasetDelete [{:bigquery client :datasetId "d" :opts {:deleteContents true}}])) "cmd")
        (let [call (bqc/->DatasetDelete [mock-dataset])]
          (is (and (= (:op call) ::bq/DatasetDelete) (g/valid? ::bq/DatasetDelete call)) "mock dataset resolve"))))))

(deftest table-test
  (testing "list-tables (->TableList)"
    (and
      (is (= {:op ::bq/TableList :bigquery nil :datasetId "d" :opts nil} (bqc/->TableList ["d"])))
      (is (= {:op ::bq/TableList :bigquery nil :datasetId dataset-id :opts nil} (bqc/->TableList ["p" "d"])))
      (is (= {:op ::bq/TableList :bigquery nil :datasetId "d" :opts {:pageSize 5}} (bqc/->TableList ["d" {:pageSize 5}])))
      (is (= {:op ::bq/TableList :bigquery client :datasetId "d" :opts nil} (bqc/->TableList [client "d"])))
      (is (= {:op ::bq/TableList :bigquery nil :datasetId dataset-id :opts {:pageSize 5}} (bqc/->TableList ["p" "d" {:pageSize 5}])))
      (is (= {:op ::bq/TableList :bigquery client :datasetId dataset-id :opts nil} (bqc/->TableList [client "p" "d"])))
      (is (= {:op ::bq/TableList :bigquery client :datasetId "d" :opts {:pageSize 5}} (bqc/->TableList [client "d" {:pageSize 5}])))
      (is (= {:op ::bq/TableList :bigquery client :datasetId dataset-id :opts {:pageSize 5}} (bqc/->TableList [client "p" "d" {:pageSize 5}])))
      (is (= {:op ::bq/TableList :bigquery client :datasetId dataset-id :opts {:pageSize 5}} (bqc/->TableList [{:bigquery client :datasetId dataset-id :opts {:pageSize 5}}])) "callRecord")
      (let [call (bqc/->TableList [mock-dataset])]
        (is (and (= (:op call) ::bq/TableList) (g/valid? ::bq/TableList call)) "mock dataset resolve"))))
  (testing "list-partitions (->TableListPartitions)"
    (and
      (is (= {:op ::bq/TableListPartitions :bigquery nil :tableId table-id} (bqc/->TableListPartitions [table-id])))
      (is (= {:op ::bq/TableListPartitions :bigquery client :tableId table-id} (bqc/->TableListPartitions [client table-id])))
      (is (= {:op ::bq/TableListPartitions :bigquery nil :tableId table-id} (bqc/->TableListPartitions ["d" "t"])))
      (is (= {:op ::bq/TableListPartitions :bigquery client :tableId table-id} (bqc/->TableListPartitions [client "d" "t"])))
      (is (= {:op ::bq/TableListPartitions :bigquery nil :tableId {:project "p" :dataset "d" :table "t"}} (bqc/->TableListPartitions ["p" "d" "t"])))
      (is (= {:op ::bq/TableListPartitions :bigquery client :tableId {:project "p" :dataset "d" :table "t"}} (bqc/->TableListPartitions [client "p" "d" "t"])))
      (is (= {:op ::bq/TableListPartitions :bigquery client :tableId table-id} (bqc/->TableListPartitions [{:bigquery client :tableId table-id}])) "callRecord")
      (let [call (bqc/->TableListPartitions [mock-table])]
        (is (and (= (:op call) ::bq/TableListPartitions) (g/valid? ::bq/TableListPartitions call)) "mock table resolve"))))
  (testing "create-table (->TableCreate)"
    (and
      (is (= {:op ::bq/TableCreate :bigquery nil :tableInfo {:tableId table-id :definition {:type "TABLE"}} :opts nil} (bqc/->TableCreate [{:tableId table-id :definition {:type "TABLE"}}])))
      (is (= {:op ::bq/TableCreate :bigquery nil :tableInfo {:tableId table-id :definition {:type "TABLE"}} :opts {:autodetectSchema true}} (bqc/->TableCreate [{:tableId table-id :definition {:type "TABLE"}} {:autodetectSchema true}])))
      (is (= {:op ::bq/TableCreate :bigquery client :tableInfo {:tableId table-id :definition {:type "TABLE"}} :opts nil} (bqc/->TableCreate [client {:tableId table-id :definition {:type "TABLE"}}])))
      (is (= {:op ::bq/TableCreate :bigquery client :tableInfo {:tableId table-id :definition {:type "TABLE"}} :opts {:autodetectSchema true}} (bqc/->TableCreate [client {:tableId table-id :definition {:type "TABLE"}} {:autodetectSchema true}])))
      (is (= {:op ::bq/TableCreate :bigquery client :tableInfo {:tableId table-id :definition {:type "TABLE"}} :opts {:autodetectSchema true}} (bqc/->TableCreate [{:bigquery client :tableInfo {:tableId table-id :definition {:type "TABLE"}} :opts {:autodetectSchema true}}])) "callRecord")
      (let [call (bqc/->TableCreate [mock-table])]
        (is (and (= (:op call) ::bq/TableCreate) (g/valid? ::bq/TableCreate call)) "mock table create"))))
  (testing "update-table (->TableUpdate)"
    (and
      (is (= {:op ::bq/TableUpdate :bigquery nil :tableInfo {:tableId table-id :definition {:type "TABLE"}} :opts nil} (bqc/->TableUpdate [{:tableId table-id :definition {:type "TABLE"}}])))
      (is (= {:op ::bq/TableUpdate :bigquery nil :tableInfo {:tableId table-id :definition {:type "TABLE"}} :opts {:autodetectSchema true}} (bqc/->TableUpdate [{:tableId table-id :definition {:type "TABLE"}} {:autodetectSchema true}])))
      (is (= {:op ::bq/TableUpdate :bigquery client :tableInfo {:tableId table-id :definition {:type "TABLE"}} :opts nil} (bqc/->TableUpdate [client {:tableId table-id :definition {:type "TABLE"}}])))
      (is (= {:op ::bq/TableUpdate :bigquery client :tableInfo {:tableId table-id :definition {:type "TABLE"}} :opts {:autodetectSchema true}} (bqc/->TableUpdate [client {:tableId table-id :definition {:type "TABLE"}} {:autodetectSchema true}])))
      (is (= {:op ::bq/TableUpdate :bigquery client :tableInfo {:tableId table-id :definition {:type "TABLE"}} :opts {:autodetectSchema true}} (bqc/->TableUpdate [{:bigquery client :tableInfo {:tableId table-id :definition {:type "TABLE"}} :opts {:autodetectSchema true}}])) "callRecord")
      (let [call (bqc/->TableUpdate [mock-table])]
        (is (and (= (:op call) ::bq/TableUpdate) (g/valid? ::bq/TableUpdate call)) "mock table update"))))
  (testing "get-table (->TableGet)"
    (and
      (is (= {:op ::bq/TableGet :bigquery nil :tableId table-id :opts nil} (bqc/->TableGet [table-id])))
      (is (= {:op ::bq/TableGet :bigquery nil :tableId table-id :opts nil} (bqc/->TableGet ["d" "t"])))
      (is (= {:op ::bq/TableGet :bigquery nil :tableId table-id :opts {:tableMetadataView "FULL"}} (bqc/->TableGet [table-id {:tableMetadataView "FULL"}])))
      (is (= {:op ::bq/TableGet :bigquery client :tableId table-id :opts nil} (bqc/->TableGet [client table-id])))
      (is (= {:op ::bq/TableGet :bigquery nil :tableId {:project "p" :dataset "d" :table "t"} :opts nil} (bqc/->TableGet ["p" "d" "t"])))
      (is (= {:op ::bq/TableGet :bigquery nil :tableId table-id :opts {:tableMetadataView "FULL"}} (bqc/->TableGet ["d" "t" {:tableMetadataView "FULL"}])))
      (is (= {:op ::bq/TableGet :bigquery client :tableId table-id :opts nil} (bqc/->TableGet [client "d" "t"])))
      (is (= {:op ::bq/TableGet :bigquery client :tableId table-id :opts {:tableMetadataView "FULL"}} (bqc/->TableGet [client table-id {:tableMetadataView "FULL"}])))
      (is (= {:op ::bq/TableGet :bigquery nil :tableId {:project "p" :dataset "d" :table "t"} :opts {:tableMetadataView "FULL"}} (bqc/->TableGet ["p" "d" "t" {:tableMetadataView "FULL"}])))
      (is (= {:op ::bq/TableGet :bigquery client :tableId {:project "p" :dataset "d" :table "t"} :opts nil} (bqc/->TableGet [client "p" "d" "t"])))
      (is (= {:op ::bq/TableGet :bigquery client :tableId table-id :opts {:tableMetadataView "FULL"}} (bqc/->TableGet [client "d" "t" {:tableMetadataView "FULL"}])))
      (is (= {:op ::bq/TableGet :bigquery client :tableId {:project "p" :dataset "d" :table "t"} :opts {:tableMetadataView "FULL"}} (bqc/->TableGet [client "p" "d" "t" {:tableMetadataView "FULL"}])))
      (is (= {:op ::bq/TableGet :bigquery client :tableId table-id :opts {:tableMetadataView "FULL"}} (bqc/->TableGet [{:bigquery client :tableId table-id :opts {:tableMetadataView "FULL"}}])) "callRecord")
      (let [call (bqc/->TableGet [mock-table])]
        (is (and (= (:op call) ::bq/TableGet) (g/valid? ::bq/TableGet call)) "mock table resolve"))))
  (testing "delete-table (->TableDelete)"
    (and
      (is (= {:op ::bq/TableDelete :bigquery nil :tableId table-id} (bqc/->TableDelete [table-id])))
      (is (= {:op ::bq/TableDelete :bigquery client :tableId table-id} (bqc/->TableDelete [client table-id])))
      (is (= {:op ::bq/TableDelete :bigquery nil :tableId table-id} (bqc/->TableDelete ["d" "t"])))
      (is (= {:op ::bq/TableDelete :bigquery client :tableId table-id} (bqc/->TableDelete [client "d" "t"])))
      (is (= {:op ::bq/TableDelete :bigquery nil :tableId {:project "p" :dataset "d" :table "t"}} (bqc/->TableDelete ["p" "d" "t"])))
      (is (= {:op ::bq/TableDelete :bigquery client :tableId {:project "p" :dataset "d" :table "t"}} (bqc/->TableDelete [client "p" "d" "t"])))
      (is (= {:op ::bq/TableDelete :bigquery client :tableId table-id} (bqc/->TableDelete [{:bigquery client :tableId table-id}])) "callRecord")
      (let [call (bqc/->TableDelete [mock-table])]
        (is (and (= (:op call) ::bq/TableDelete) (g/valid? ::bq/TableDelete call)) "mock table resolve"))))
  (testing "list-table-data (->TableListData)"
    (and
      (is (= {:op ::bq/TableListData :bigquery nil :tableId table-id :schema nil :opts nil} (bqc/->TableListData [table-id])))
      (is (= {:op ::bq/TableListData :bigquery client :tableId table-id :schema nil :opts nil} (bqc/->TableListData [client table-id])))
      (is (= {:op ::bq/TableListData :bigquery nil :tableId table-id :schema nil :opts nil} (bqc/->TableListData ["d" "t"])))
      (is (= {:op ::bq/TableListData :bigquery nil :tableId table-id :schema {:fields []} :opts nil} (bqc/->TableListData [table-id {:fields []}])))
      (is (= {:op ::bq/TableListData :bigquery nil :tableId table-id :schema nil :opts {:pageSize 10}} (bqc/->TableListData [table-id {:pageSize 10}])))
      (is (= {:op ::bq/TableListData :bigquery client :tableId table-id :schema nil :opts nil} (bqc/->TableListData [client "d" "t"])))
      (is (= {:op ::bq/TableListData :bigquery client :tableId table-id :schema {:fields []} :opts nil} (bqc/->TableListData [client table-id {:fields []}])))
      (is (= {:op ::bq/TableListData :bigquery client :tableId table-id :schema nil :opts {:pageSize 10}} (bqc/->TableListData [client table-id {:pageSize 10}])))
      (is (= {:op ::bq/TableListData :bigquery nil :tableId {:project "p" :dataset "d" :table "t"} :schema nil :opts nil} (bqc/->TableListData ["p" "d" "t"])))
      (is (= {:op ::bq/TableListData :bigquery nil :tableId table-id :schema {:fields []} :opts nil} (bqc/->TableListData ["d" "t" {:fields []}])))
      (is (= {:op ::bq/TableListData :bigquery nil :tableId table-id :schema nil :opts {:pageSize 10}} (bqc/->TableListData ["d" "t" {:pageSize 10}])))
      (is (= {:op ::bq/TableListData :bigquery nil :tableId table-id :schema {:fields []} :opts {:pageSize 10}} (bqc/->TableListData [table-id {:fields []} {:pageSize 10}])))
      (is (= {:op ::bq/TableListData :bigquery client :tableId {:project "p" :dataset "d" :table "t"} :schema nil :opts nil} (bqc/->TableListData [client "p" "d" "t"])))
      (is (= {:op ::bq/TableListData :bigquery client :tableId table-id :schema {:fields []} :opts nil} (bqc/->TableListData [client "d" "t" {:fields []}])))
      (is (= {:op ::bq/TableListData :bigquery client :tableId table-id :schema nil :opts {:pageSize 10}} (bqc/->TableListData [client "d" "t" {:pageSize 10}])))
      (is (= {:op ::bq/TableListData :bigquery client :tableId table-id :schema {:fields []} :opts {:pageSize 10}} (bqc/->TableListData [client table-id {:fields []} {:pageSize 10}])))
      (is (= {:op ::bq/TableListData :bigquery nil :tableId {:project "p" :dataset "d" :table "t"} :schema {:fields []} :opts nil} (bqc/->TableListData ["p" "d" "t" {:fields []}])))
      (is (= {:op ::bq/TableListData :bigquery nil :tableId {:project "p" :dataset "d" :table "t"} :schema nil :opts {:pageSize 10}} (bqc/->TableListData ["p" "d" "t" {:pageSize 10}])))
      (is (= {:op ::bq/TableListData :bigquery nil :tableId table-id :schema {:fields []} :opts {:pageSize 10}} (bqc/->TableListData ["d" "t" {:fields []} {:pageSize 10}])))
      (is (= {:op ::bq/TableListData :bigquery client :tableId {:project "p" :dataset "d" :table "t"} :schema {:fields []} :opts nil} (bqc/->TableListData [client "p" "d" "t" {:fields []}])))
      (is (= {:op ::bq/TableListData :bigquery client :tableId {:project "p" :dataset "d" :table "t"} :schema nil :opts {:pageSize 10}} (bqc/->TableListData [client "p" "d" "t" {:pageSize 10}])))
      (is (= {:op ::bq/TableListData :bigquery client :tableId table-id :schema {:fields []} :opts {:pageSize 10}} (bqc/->TableListData [client "d" "t" {:fields []} {:pageSize 10}])))
      (is (= {:op ::bq/TableListData :bigquery nil :tableId {:project "p" :dataset "d" :table "t"} :schema {:fields []} :opts {:pageSize 10}} (bqc/->TableListData ["p" "d" "t" {:fields []} {:pageSize 10}])))
      (is (= {:op ::bq/TableListData :bigquery client :tableId {:project "p" :dataset "d" :table "t"} :schema {:fields []} :opts {:pageSize 10}} (bqc/->TableListData [client "p" "d" "t" {:fields []} {:pageSize 10}])))
      (is (= {:op ::bq/TableListData :bigquery client :tableId table-id :schema {:fields []} :opts {:pageSize 10}} (bqc/->TableListData [{:bigquery client :tableId table-id :schema {:fields []} :opts {:pageSize 10}}])) "callRecord")
      (let [call (bqc/->TableListData [mock-table])]
        (is (and (= (:op call) ::bq/TableListData) (g/valid? ::bq/TableListData call)) "mock table resolve")))))

(deftest InsertAll-test
  (and
    (is (= {:op ::bq/InsertAll :bigquery nil    :insertAllRequest {:table table-id :rows [{:insertId "1"}]}} (bqc/->InsertAll [{:table table-id :rows [{:insertId "1"}]}])))
    (is (= {:op ::bq/InsertAll :bigquery client :insertAllRequest {:table table-id :rows [{:insertId "1"}]}} (bqc/->InsertAll [client {:table table-id :rows [{:insertId "1"}]}])))
    (is (= {:op ::bq/InsertAll :bigquery nil    :insertAllRequest {:table table-id :rows [{:insertId "1"}]}} (bqc/->InsertAll [table-id [{:insertId "1"}]])))
    (is (= {:op ::bq/InsertAll :bigquery client :insertAllRequest {:table table-id :rows [{:insertId "1"}]}} (bqc/->InsertAll [client table-id [{:insertId "1"}]])))
    (is (= {:op ::bq/InsertAll :bigquery nil    :insertAllRequest {:table table-id :rows [{:insertId "1"}]}} (bqc/->InsertAll ["d" "t" [{:insertId "1"}]])))
    (is (= {:op ::bq/InsertAll :bigquery client :insertAllRequest {:table table-id :rows [{:insertId "1"}]}} (bqc/->InsertAll [client "d" "t" [{:insertId "1"}]])))
    (is (= {:op ::bq/InsertAll :bigquery nil    :insertAllRequest {:table {:project "p" :dataset "d" :table "t"} :rows [{:insertId "1"}]}} (bqc/->InsertAll ["p" "d" "t" [{:insertId "1"}]])))
    (is (= {:op ::bq/InsertAll :bigquery client :insertAllRequest {:table {:project "p" :dataset "d" :table "t"} :rows [{:insertId "1"}]}} (bqc/->InsertAll [client "p" "d" "t" [{:insertId "1"}]])))
    (is (= {:op ::bq/InsertAll :bigquery client :insertAllRequest {:table table-id :rows [{:insertId "1"}]}} (bqc/->InsertAll [{:bigquery client :insertAllRequest {:table table-id :rows [{:insertId "1"}]}}])) "callRecord")
    (let [call (bqc/->InsertAll [mock-table [{:insertId "1"}]])]
      (is (and (= (:op call) ::bq/InsertAll) (g/valid? ::bq/InsertAll call)) "mock table resolve"))))

(deftest job-test
  (and
    (testing "list-jobs (->JobList)"
      (and
        (is (= {:op ::bq/JobList :bigquery nil :opts nil} (bqc/->JobList [])))
        (is (= {:op ::bq/JobList :bigquery nil :opts {:pageSize 10}} (bqc/->JobList [nil {:pageSize 10}])))
        (is (= {:op ::bq/JobList :bigquery client :opts nil} (bqc/->JobList [client])))
        (is (= {:op ::bq/JobList :bigquery client :opts {:pageSize 10}} (bqc/->JobList [client {:pageSize 10}])))
        (is (= {:op ::bq/JobList :bigquery client :opts {:pageSize 10}} (bqc/->JobList [{:bigquery client :opts {:pageSize 10}}])) "callRecord")))
    (testing "cancel-job (->JobCancel)"
      (and
        (is (= {:op ::bq/JobCancel :bigquery nil :jobId job-id} (bqc/->JobCancel ["j"])))
        (is (= {:op ::bq/JobCancel :bigquery nil :jobId {:job "j" :location "l"}} (bqc/->JobCancel [{:job "j" :location "l"}])))
        (is (= {:op ::bq/JobCancel :bigquery client :jobId job-id} (bqc/->JobCancel [client "j"])))
        (is (= {:op ::bq/JobCancel :bigquery client :jobId {:job "j" :location "l"}} (bqc/->JobCancel [client {:job "j" :location "l"}])))
        (is (= {:op ::bq/JobCancel :bigquery client :jobId job-id} (bqc/->JobCancel [{:bigquery client :jobId job-id}])) "callRecord")
        (let [call (bqc/->JobCancel [mock-job])]
          (is (and (= (:op call) ::bq/JobCancel) (g/valid? ::bq/JobCancel call)) "mock job resolve"))))
    (testing "create-job (->JobCreate)"
      (and
        (is (= {:op ::bq/JobCreate :bigquery nil :jobInfo {:configuration query-job} :opts nil}                  (bqc/->JobCreate [query-job])))
        (is (= {:op ::bq/JobCreate :bigquery nil :jobInfo {:jobId job-id :configuration query-job} :opts nil}    (bqc/->JobCreate [{:jobId job-id :configuration query-job}])))
        (is (= {:op ::bq/JobCreate :bigquery nil :jobInfo {:jobId job-id :configuration query-job} :opts {}}     (bqc/->JobCreate [{:jobId job-id :configuration query-job} {}])))
        (is (= {:op ::bq/JobCreate :bigquery client :jobInfo {:jobId job-id :configuration query-job} :opts nil} (bqc/->JobCreate [client {:jobId job-id :configuration query-job}])))
        (is (= {:op ::bq/JobCreate :bigquery client :jobInfo {:jobId job-id :configuration query-job} :opts {}}  (bqc/->JobCreate [client {:jobId job-id :configuration query-job} {}])))
        (is (= {:op ::bq/JobCreate :bigquery client :jobInfo {:jobId job-id :configuration query-job} :opts {}}  (bqc/->JobCreate [{:bigquery client :jobInfo {:jobId job-id :configuration query-job} :opts {}}])) "callRecord")))
    (testing "get-job (->JobGet)"
      (and
        (is (= {:op ::bq/JobGet :bigquery nil :jobId job-id :opts nil} (bqc/->JobGet ["j"])))
        (is (= {:op ::bq/JobGet :bigquery nil :jobId {:job "j" :location "l"} :opts nil} (bqc/->JobGet [{:job "j" :location "l"}])))
        (is (= {:op ::bq/JobGet :bigquery nil :jobId job-id :opts {}} (bqc/->JobGet ["j" {}])))
        (is (= {:op ::bq/JobGet :bigquery client :jobId job-id :opts nil} (bqc/->JobGet [client "j"])))
        (is (= {:op ::bq/JobGet :bigquery client :jobId job-id :opts {}} (bqc/->JobGet [client "j" {}])))
        (is (= {:op ::bq/JobGet :bigquery client :jobId job-id :opts {}} (bqc/->JobGet [{:bigquery client :jobId job-id :opts {}}])) "callRecord")
        (let [call (bqc/->JobGet [mock-job])]
          (is (and (= (:op call) ::bq/JobGet) (g/valid? ::bq/JobGet call)) "mock job resolve"))))
    (testing "delete-job (->JobDelete)"
      (and
        (is (= {:op ::bq/JobDelete :bigquery nil :jobId job-id} (bqc/->JobDelete ["j"])))
        (is (= {:op ::bq/JobDelete :bigquery nil :jobId {:job "j" :location "l"}} (bqc/->JobDelete [{:job "j" :location "l"}])))
        (is (= {:op ::bq/JobDelete :bigquery client :jobId job-id} (bqc/->JobDelete [client "j"])))
        (is (= {:op ::bq/JobDelete :bigquery client :jobId {:job "j" :location "l"}} (bqc/->JobDelete [client {:job "j" :location "l"}])))
        (is (= {:op ::bq/JobDelete :bigquery client :jobId job-id} (bqc/->JobDelete [{:bigquery client :jobId job-id}])) "callRecord")
        (let [call (bqc/->JobDelete [mock-job])]
          (is (and (= (:op call) ::bq/JobDelete) (g/valid? ::bq/JobDelete call)) "mock job resolve"))))))

(deftest routine-test
  (and
    (testing "list-routines (->RoutineList)"
      (and
        (is (= {:op ::bq/RoutineList :bigquery nil :datasetId "d" :opts nil} (bqc/->RoutineList ["d"])))
        (is (= {:op ::bq/RoutineList :bigquery nil :datasetId {:dataset "d"} :opts nil} (bqc/->RoutineList [{:dataset "d"}])))
        (is (= {:op ::bq/RoutineList :bigquery nil :datasetId dataset-id :opts nil} (bqc/->RoutineList ["p" "d"])))
        (is (= {:op ::bq/RoutineList :bigquery client :datasetId "d" :opts nil} (bqc/->RoutineList [client "d"])))
        (is (= {:op ::bq/RoutineList :bigquery nil :datasetId "d" :opts {:pageSize 10}} (bqc/->RoutineList ["d" {:pageSize 10}])))
        (is (= {:op ::bq/RoutineList :bigquery client :datasetId dataset-id :opts nil} (bqc/->RoutineList [client "p" "d"])))
        (is (= {:op ::bq/RoutineList :bigquery client :datasetId "d" :opts {:pageSize 10}} (bqc/->RoutineList [client "d" {:pageSize 10}])))
        (is (= {:op ::bq/RoutineList :bigquery nil :datasetId dataset-id :opts {:pageSize 10}} (bqc/->RoutineList ["p" "d" {:pageSize 10}])))
        (is (= {:op ::bq/RoutineList :bigquery client :datasetId dataset-id :opts {:pageSize 10}} (bqc/->RoutineList [client "p" "d" {:pageSize 10}])))
        (is (= {:op ::bq/RoutineList :bigquery client :datasetId dataset-id :opts {:pageSize 10}} (bqc/->RoutineList [{:bigquery client :datasetId dataset-id :opts {:pageSize 10}}])) "callRecord")))
    (testing "create-routine (->RoutineCreate)"
      (and
        (is (= {:op ::bq/RoutineCreate :bigquery nil :routineInfo {:routineId {:dataset "d" :routine "r"}} :opts nil} (bqc/->RoutineCreate [{:routineId {:dataset "d" :routine "r"}}])))
        (is (= {:op ::bq/RoutineCreate :bigquery client :routineInfo {:routineId {:dataset "d" :routine "r"}} :opts nil} (bqc/->RoutineCreate [client {:routineId {:dataset "d" :routine "r"}}])))
        (is (= {:op ::bq/RoutineCreate :bigquery nil :routineInfo {:routineId {:dataset "d" :routine "r"}} :opts {}} (bqc/->RoutineCreate [{:routineId {:dataset "d" :routine "r"}} {}])))
        (is (= {:op ::bq/RoutineCreate :bigquery client :routineInfo {:routineId {:dataset "d" :routine "r"}} :opts {}} (bqc/->RoutineCreate [client {:routineId {:dataset "d" :routine "r"}} {}])))
        (is (= {:op ::bq/RoutineCreate :bigquery client :routineInfo {:routineId {:dataset "d" :routine "r"}} :opts {}} (bqc/->RoutineCreate [{:bigquery client :routineInfo {:routineId {:dataset "d" :routine "r"}} :opts {}}])) "callRecord")
        (let [call (bqc/->RoutineCreate [mock-routine])]
          (is (and (= (:op call) ::bq/RoutineCreate) (g/valid? ::bq/RoutineCreate call)) "mock routine create"))))
    (testing "update-routine (->RoutineUpdate)"
      (and
        (is (= {:op ::bq/RoutineUpdate :bigquery nil :routineInfo {:routineId {:dataset "d" :routine "r"}} :opts nil} (bqc/->RoutineUpdate [{:routineId {:dataset "d" :routine "r"}}])))
        (is (= {:op ::bq/RoutineUpdate :bigquery client :routineInfo {:routineId {:dataset "d" :routine "r"}} :opts nil} (bqc/->RoutineUpdate [client {:routineId {:dataset "d" :routine "r"}}])))
        (is (= {:op ::bq/RoutineUpdate :bigquery nil :routineInfo {:routineId {:dataset "d" :routine "r"}} :opts {}} (bqc/->RoutineUpdate [{:routineId {:dataset "d" :routine "r"}} {}])))
        (is (= {:op ::bq/RoutineUpdate :bigquery client :routineInfo {:routineId {:dataset "d" :routine "r"}} :opts {}} (bqc/->RoutineUpdate [client {:routineId {:dataset "d" :routine "r"}} {}])))
        (is (= {:op ::bq/RoutineUpdate :bigquery client :routineInfo {:routineId {:dataset "d" :routine "r"}} :opts {}} (bqc/->RoutineUpdate [{:bigquery client :routineInfo {:routineId {:dataset "d" :routine "r"}} :opts {}}])) "callRecord")
        (let [call (bqc/->RoutineUpdate [mock-routine])]
          (is (and (= (:op call) ::bq/RoutineUpdate) (g/valid? ::bq/RoutineUpdate call)) "mock routine update"))))
    (testing "get-routine (->RoutineGet)"
      (and
        (is (= {:op ::bq/RoutineGet :bigquery nil :routineId {:dataset "d" :routine "r"} :opts nil} (bqc/->RoutineGet [{:dataset "d" :routine "r"}])))
        (is (= {:op ::bq/RoutineGet :bigquery client :routineId {:dataset "d" :routine "r"} :opts nil} (bqc/->RoutineGet [client {:dataset "d" :routine "r"}])))
        (is (= {:op ::bq/RoutineGet :bigquery nil :routineId {:dataset "d" :routine "r"} :opts nil} (bqc/->RoutineGet ["d" "r"])))
        (is (= {:op ::bq/RoutineGet :bigquery nil :routineId {:dataset "d" :routine "r"} :opts {}} (bqc/->RoutineGet [{:dataset "d" :routine "r"} {}])))
        (is (= {:op ::bq/RoutineGet :bigquery client :routineId {:dataset "d" :routine "r"} :opts nil} (bqc/->RoutineGet [client "d" "r"])))
        (is (= {:op ::bq/RoutineGet :bigquery client :routineId {:dataset "d" :routine "r"} :opts {}} (bqc/->RoutineGet [client {:dataset "d" :routine "r"} {}])))
        (is (= {:op ::bq/RoutineGet :bigquery nil :routineId {:project "p" :dataset "d" :routine "r"} :opts nil} (bqc/->RoutineGet ["p" "d" "r"])))
        (is (= {:op ::bq/RoutineGet :bigquery nil :routineId {:dataset "d" :routine "r"} :opts {}} (bqc/->RoutineGet ["d" "r" {}])))
        (is (= {:op ::bq/RoutineGet :bigquery client :routineId {:project "p" :dataset "d" :routine "r"} :opts nil} (bqc/->RoutineGet [client "p" "d" "r"])))
        (is (= {:op ::bq/RoutineGet :bigquery client :routineId {:dataset "d" :routine "r"} :opts {}} (bqc/->RoutineGet [client "d" "r" {}])))
        (is (= {:op ::bq/RoutineGet :bigquery client :routineId {:project "p" :dataset "d" :routine "r"} :opts {}} (bqc/->RoutineGet [client "p" "d" "r" {}])))
        (is (= {:op ::bq/RoutineGet :bigquery client :routineId {:project "p" :dataset "d" :routine "r"} :opts {}} (bqc/->RoutineGet [{:bigquery client :routineId {:project "p" :dataset "d" :routine "r"} :opts {}}])) "callRecord")
        (let [call (bqc/->RoutineGet [mock-routine])]
          (is (and (= (:op call) ::bq/RoutineGet) (g/valid? ::bq/RoutineGet call)) "mock routine resolve"))))
    (testing "delete-routine (->RoutineDelete)"
      (and
        (is (= {:op ::bq/RoutineDelete :bigquery nil    :routineId {:dataset "d" :routine "r"}} (bqc/->RoutineDelete [{:dataset "d" :routine "r"}])))
        (is (= {:op ::bq/RoutineDelete :bigquery client :routineId {:dataset "d" :routine "r"}} (bqc/->RoutineDelete [client {:dataset "d" :routine "r"}])))
        (is (= {:op ::bq/RoutineDelete :bigquery nil    :routineId {:dataset "d" :routine "r"}} (bqc/->RoutineDelete ["d" "r"])))
        (is (= {:op ::bq/RoutineDelete :bigquery client :routineId {:dataset "d" :routine "r"}} (bqc/->RoutineDelete [client "d" "r"])))
        (is (= {:op ::bq/RoutineDelete :bigquery nil    :routineId {:project "p" :dataset "d" :routine "r"}} (bqc/->RoutineDelete ["p" "d" "r"])))
        (is (= {:op ::bq/RoutineDelete :bigquery client :routineId {:project "p" :dataset "d" :routine "r"}} (bqc/->RoutineDelete [client "p" "d" "r"])))
        (is (= {:op ::bq/RoutineDelete :bigquery client :routineId {:project "p" :dataset "d" :routine "r"}}
               (bqc/->RoutineDelete [{:bigquery client :routineId {:project "p" :dataset "d" :routine "r"}}])) "callRecord")
        (let [call (bqc/->RoutineDelete [mock-routine])]
          (is (and (= (:op call) ::bq/RoutineDelete) (g/valid? ::bq/RoutineDelete call)) "supports StructPoly by accepting Routine instances"))))))

(deftest model-test
  (and
    (testing "list-models (->ModelList)"
      (and
        (is (= {:op ::bq/ModelList :bigquery nil    :datasetId "d"            :opts nil}            (bqc/->ModelList ["d"])))
        (is (= {:op ::bq/ModelList :bigquery nil    :datasetId {:dataset "d"} :opts nil}            (bqc/->ModelList [{:dataset "d"}])))
        (is (= {:op ::bq/ModelList :bigquery nil    :datasetId dataset-id     :opts nil}            (bqc/->ModelList ["p" "d"])))
        (is (= {:op ::bq/ModelList :bigquery client :datasetId "d"            :opts nil}            (bqc/->ModelList [client "d"])))
        (is (= {:op ::bq/ModelList :bigquery nil    :datasetId "d"            :opts {:pageSize 10}} (bqc/->ModelList ["d" {:pageSize 10}])))
        (is (= {:op ::bq/ModelList :bigquery client :datasetId dataset-id     :opts nil}            (bqc/->ModelList [client "p" "d"])))
        (is (= {:op ::bq/ModelList :bigquery client :datasetId "d"            :opts {:pageSize 10}} (bqc/->ModelList [client "d" {:pageSize 10}])))
        (is (= {:op ::bq/ModelList :bigquery nil    :datasetId dataset-id     :opts {:pageSize 10}} (bqc/->ModelList ["p" "d" {:pageSize 10}])))
        (is (= {:op ::bq/ModelList :bigquery client :datasetId dataset-id     :opts {:pageSize 10}} (bqc/->ModelList [client "p" "d" {:pageSize 10}])))
        (is (= {:op ::bq/ModelList :bigquery client :datasetId dataset-id     :opts {:pageSize 10}} (bqc/->ModelList [{:bigquery client :datasetId dataset-id :opts {:pageSize 10}}])) "callRecord")))
    (testing "update-model (->ModelUpdate)"
      (and
        (is (= {:op ::bq/ModelUpdate :bigquery nil :modelInfo {:modelId {:dataset "d" :model "m"}} :opts nil} (bqc/->ModelUpdate [{:modelId {:dataset "d" :model "m"}}])))
        (is (= {:op ::bq/ModelUpdate :bigquery client :modelInfo {:modelId {:dataset "d" :model "m"}} :opts nil} (bqc/->ModelUpdate [client {:modelId {:dataset "d" :model "m"}}])))
        (is (= {:op ::bq/ModelUpdate :bigquery nil :modelInfo {:modelId {:dataset "d" :model "m"}} :opts {}} (bqc/->ModelUpdate [{:modelId {:dataset "d" :model "m"}} {}])))
        (is (= {:op ::bq/ModelUpdate :bigquery client :modelInfo {:modelId {:dataset "d" :model "m"}} :opts {}} (bqc/->ModelUpdate [client {:modelId {:dataset "d" :model "m"}} {}])))
        (is (= {:op ::bq/ModelUpdate :bigquery client :modelInfo {:modelId {:dataset "d" :model "m"}} :opts {}} (bqc/->ModelUpdate [{:bigquery client :modelInfo {:modelId {:dataset "d" :model "m"}} :opts {}}])) "callRecord")
        (let [call (bqc/->ModelUpdate [mock-model])]
          (is (and (= (:op call) ::bq/ModelUpdate) (g/valid? ::bq/ModelUpdate call)) "mock model update"))))
    (testing "get-model (->ModelGet)"
      (and
        (is (= {:op ::bq/ModelGet :bigquery nil :modelId {:dataset "d" :model "m"} :opts nil} (bqc/->ModelGet [{:dataset "d" :model "m"}])))
        (is (= {:op ::bq/ModelGet :bigquery client :modelId {:dataset "d" :model "m"} :opts nil} (bqc/->ModelGet [client {:dataset "d" :model "m"}])))
        (is (= {:op ::bq/ModelGet :bigquery nil :modelId {:dataset "d" :model "m"} :opts nil} (bqc/->ModelGet ["d" "m"])))
        (is (= {:op ::bq/ModelGet :bigquery nil :modelId {:dataset "d" :model "m"} :opts {}} (bqc/->ModelGet [{:dataset "d" :model "m"} {}])))
        (is (= {:op ::bq/ModelGet :bigquery client :modelId {:dataset "d" :model "m"} :opts nil} (bqc/->ModelGet [client "d" "m"])))
        (is (= {:op ::bq/ModelGet :bigquery client :modelId {:dataset "d" :model "m"} :opts {}} (bqc/->ModelGet [client {:dataset "d" :model "m"} {}])))
        (is (= {:op ::bq/ModelGet :bigquery nil :modelId {:project "p" :dataset "d" :model "m"} :opts nil} (bqc/->ModelGet ["p" "d" "m"])))
        (is (= {:op ::bq/ModelGet :bigquery nil :modelId {:dataset "d" :model "m"} :opts {}} (bqc/->ModelGet ["d" "m" {}])))
        (is (= {:op ::bq/ModelGet :bigquery client :modelId {:project "p" :dataset "d" :model "m"} :opts nil} (bqc/->ModelGet [client "p" "d" "m"])))
        (is (= {:op ::bq/ModelGet :bigquery client :modelId {:dataset "d" :model "m"} :opts {}} (bqc/->ModelGet [client "d" "m" {}])))
        (is (= {:op ::bq/ModelGet :bigquery client :modelId {:project "p" :dataset "d" :model "m"} :opts {}} (bqc/->ModelGet [client "p" "d" "m" {}])))
        (is (= {:op ::bq/ModelGet :bigquery client :modelId {:project "p" :dataset "d" :model "m"} :opts {}} (bqc/->ModelGet [{:bigquery client :modelId {:project "p" :dataset "d" :model "m"} :opts {}}])) "callRecord")
        (let [call (bqc/->ModelGet [mock-model])]
          (is (and (= (:op call) ::bq/ModelGet) (g/valid? ::bq/ModelGet call)) "mock model resolve"))))
    (testing "delete-model (->ModelDelete)"
      (and
        (is (= {:op ::bq/ModelDelete :bigquery nil :modelId {:dataset "d" :model "m"}} (bqc/->ModelDelete [{:dataset "d" :model "m"}])))
        (is (= {:op ::bq/ModelDelete :bigquery client :modelId {:dataset "d" :model "m"}} (bqc/->ModelDelete [client {:dataset "d" :model "m"}])))
        (is (= {:op ::bq/ModelDelete :bigquery nil :modelId {:dataset "d" :model "m"}} (bqc/->ModelDelete ["d" "m"])))
        (is (= {:op ::bq/ModelDelete :bigquery client :modelId {:dataset "d" :model "m"}} (bqc/->ModelDelete [client "d" "m"])))
        (is (= {:op ::bq/ModelDelete :bigquery nil :modelId {:project "p" :dataset "d" :model "m"}} (bqc/->ModelDelete ["p" "d" "m"])))
        (is (= {:op ::bq/ModelDelete :bigquery client :modelId {:project "p" :dataset "d" :model "m"}} (bqc/->ModelDelete [client "p" "d" "m"])))
        (is (= {:op ::bq/ModelDelete :bigquery client :modelId {:project "p" :dataset "d" :model "m"}} (bqc/->ModelDelete [{:bigquery client :modelId {:project "p" :dataset "d" :model "m"}}])) "callRecord")
        (let [call (bqc/->ModelDelete [mock-model])]
          (is (and (= (:op call) ::bq/ModelDelete) (g/valid? ::bq/ModelDelete call)) "mock model resolve"))))))

(deftest iam-test
  (and
    (testing "get-iam-policy (->GetIamPolicy)"
      (and
        (is (= {:op ::bq/GetIamPolicy :bigquery nil :tableId table-id :opts nil} (bqc/->GetIamPolicy [table-id])))
        (is (= {:op ::bq/GetIamPolicy :bigquery client :tableId table-id :opts nil} (bqc/->GetIamPolicy [client table-id])))
        (is (= {:op ::bq/GetIamPolicy :bigquery nil :tableId table-id :opts nil} (bqc/->GetIamPolicy ["d" "t"])))
        (is (= {:op ::bq/GetIamPolicy :bigquery nil :tableId table-id :opts {:requestedPolicyVersion 1}} (bqc/->GetIamPolicy [table-id {:requestedPolicyVersion 1}])))
        (is (= {:op ::bq/GetIamPolicy :bigquery client :tableId table-id :opts nil} (bqc/->GetIamPolicy [client "d" "t"])))
        (is (= {:op ::bq/GetIamPolicy :bigquery client :tableId table-id :opts {:requestedPolicyVersion 1}} (bqc/->GetIamPolicy [client table-id {:requestedPolicyVersion 1}])))
        (is (= {:op ::bq/GetIamPolicy :bigquery nil :tableId {:project "p" :dataset "d" :table "t"} :opts nil} (bqc/->GetIamPolicy ["p" "d" "t"])))
        (is (= {:op ::bq/GetIamPolicy :bigquery nil :tableId table-id :opts {:requestedPolicyVersion 1}} (bqc/->GetIamPolicy ["d" "t" {:requestedPolicyVersion 1}])))
        (is (= {:op ::bq/GetIamPolicy :bigquery client :tableId {:project "p" :dataset "d" :table "t"} :opts nil} (bqc/->GetIamPolicy [client "p" "d" "t"])))
        (is (= {:op ::bq/GetIamPolicy :bigquery client :tableId table-id :opts {:requestedPolicyVersion 1}} (bqc/->GetIamPolicy [client "d" "t" {:requestedPolicyVersion 1}])))
        (is (= {:op ::bq/GetIamPolicy :bigquery client :tableId {:project "p" :dataset "d" :table "t"} :opts {:requestedPolicyVersion 1}} (bqc/->GetIamPolicy [client "p" "d" "t" {:requestedPolicyVersion 1}])))
        (is (= {:op ::bq/GetIamPolicy :bigquery client :tableId table-id :opts {:requestedPolicyVersion 1}} (bqc/->GetIamPolicy [{:bigquery client :tableId table-id :opts {:requestedPolicyVersion 1}}])) "callRecord")
        (let [call (bqc/->GetIamPolicy [mock-table])]
          (is (and (= (:op call) ::bq/GetIamPolicy) (g/valid? ::bq/GetIamPolicy call)) "mock table resolve"))))
    (testing "set-iam-policy (->SetIamPolicy)"
      (and
        (is (= {:op ::bq/SetIamPolicy :bigquery nil :tableId table-id :policy {:bindings [] :version 0} :opts nil} (bqc/->SetIamPolicy [table-id {:bindings [] :version 0}])))
        (is (= {:op ::bq/SetIamPolicy :bigquery client :tableId table-id :policy {:bindings [] :version 0} :opts nil} (bqc/->SetIamPolicy [client table-id {:bindings [] :version 0}])))
        (is (= {:op ::bq/SetIamPolicy :bigquery nil :tableId table-id :policy {:bindings [] :version 0} :opts nil} (bqc/->SetIamPolicy ["d" "t" {:bindings [] :version 0}])))
        (is (= {:op ::bq/SetIamPolicy :bigquery nil :tableId table-id :policy {:bindings [] :version 0} :opts {:requestedPolicyVersion 1}} (bqc/->SetIamPolicy [table-id {:bindings [] :version 0} {:requestedPolicyVersion 1}])))
        (is (= {:op ::bq/SetIamPolicy :bigquery client :tableId table-id :policy {:bindings [] :version 0} :opts nil} (bqc/->SetIamPolicy [client "d" "t" {:bindings [] :version 0}])))
        (is (= {:op ::bq/SetIamPolicy :bigquery nil :tableId {:project "p" :dataset "d" :table "t"} :policy {:bindings [] :version 0} :opts nil} (bqc/->SetIamPolicy ["p" "d" "t" {:bindings [] :version 0}])))
        (is (= {:op ::bq/SetIamPolicy :bigquery client :tableId table-id :policy {:bindings [] :version 0} :opts {:requestedPolicyVersion 1}} (bqc/->SetIamPolicy [client table-id {:bindings [] :version 0} {:requestedPolicyVersion 1}])))
        (is (thrown? Exception (bqc/->SetIamPolicy ["d" "t" {:bindings [] :version 0} {:requestedPolicyVersion 1}])))
        (is (= {:op ::bq/SetIamPolicy :bigquery client :tableId {:project "p" :dataset "d" :table "t"} :policy {:bindings [] :version 0} :opts nil} (bqc/->SetIamPolicy [client "p" "d" "t" {:bindings [] :version 0}])))
        (is (= {:op ::bq/SetIamPolicy :bigquery client :tableId table-id :policy {:bindings [] :version 0} :opts {:requestedPolicyVersion 1}} (bqc/->SetIamPolicy [client "d" "t" {:bindings [] :version 0} {:requestedPolicyVersion 1}])))
        (is (= {:op ::bq/SetIamPolicy :bigquery nil :tableId {:project "p" :dataset "d" :table "t"} :policy {:bindings [] :version 0} :opts {:requestedPolicyVersion 1}} (bqc/->SetIamPolicy ["p" "d" "t" {:bindings [] :version 0} {:requestedPolicyVersion 1}])))
        (is (= {:op ::bq/SetIamPolicy :bigquery client :tableId {:project "p" :dataset "d" :table "t"} :policy {:bindings [] :version 0} :opts {:requestedPolicyVersion 1}} (bqc/->SetIamPolicy [client "p" "d" "t" {:bindings [] :version 0} {:requestedPolicyVersion 1}])))
        (is (= {:op ::bq/SetIamPolicy :bigquery client :tableId table-id :policy {:bindings [] :version 0} :opts {:requestedPolicyVersion 1}} (bqc/->SetIamPolicy [{:bigquery client :tableId table-id :policy {:bindings [] :version 0} :opts {:requestedPolicyVersion 1}}])) "callRecord")
        (let [call (bqc/->SetIamPolicy [mock-table {:bindings [] :version 0}])]
          (is (and (= (:op call) ::bq/SetIamPolicy) (g/valid? ::bq/SetIamPolicy call)) "mock table resolve"))))
    (testing "test-iam-permissions (->TestIamPermissions)"
      (and
        (is (= {:op ::bq/TestIamPermissions :bigquery nil   :tableId table-id :permissions ["p1"] :opts nil} (bqc/->TestIamPermissions [table-id ["p1"]])))
        (is (= {:op ::bq/TestIamPermissions :bigquery client :tableId table-id :permissions ["p1"] :opts nil} (bqc/->TestIamPermissions [client table-id ["p1"]])))
        (is (= {:op ::bq/TestIamPermissions :bigquery nil    :tableId table-id :permissions ["p1"] :opts nil} (bqc/->TestIamPermissions ["d" "t" ["p1"]])))
        (is (= {:op ::bq/TestIamPermissions :bigquery nil :tableId table-id :permissions ["p1"] :opts {:requestedPolicyVersion 1}} (bqc/->TestIamPermissions [table-id ["p1"] {:requestedPolicyVersion 1}])))
        (is (= {:op ::bq/TestIamPermissions :bigquery client :tableId table-id :permissions ["p1"] :opts nil} (bqc/->TestIamPermissions [client "d" "t" ["p1"]])))
        (is (= {:op ::bq/TestIamPermissions :bigquery nil :tableId {:project "p" :dataset "d" :table "t"} :permissions ["p1"] :opts nil} (bqc/->TestIamPermissions ["p" "d" "t" ["p1"]])))
        (is (= {:op ::bq/TestIamPermissions :bigquery client :tableId table-id :permissions ["p1"] :opts {:requestedPolicyVersion 1}} (bqc/->TestIamPermissions [client table-id ["p1"] {:requestedPolicyVersion 1}])))
        (is (= {:op ::bq/TestIamPermissions :bigquery nil :tableId table-id :permissions ["p1"] :opts {:requestedPolicyVersion 1}} (bqc/->TestIamPermissions ["d" "t" ["p1"] {:requestedPolicyVersion 1}])))
        (is (= {:op ::bq/TestIamPermissions :bigquery client :tableId {:project "p" :dataset "d" :table "t"} :permissions ["p1"] :opts nil} (bqc/->TestIamPermissions [client "p" "d" "t" ["p1"]])))
        (is (= {:op ::bq/TestIamPermissions :bigquery client :tableId table-id :permissions ["p1"] :opts {:requestedPolicyVersion 1}} (bqc/->TestIamPermissions [client "d" "t" ["p1"] {:requestedPolicyVersion 1}])))
        (is (= {:op ::bq/TestIamPermissions :bigquery nil :tableId {:project "p" :dataset "d" :table "t"} :permissions ["p1"] :opts {:requestedPolicyVersion 1}} (bqc/->TestIamPermissions ["p" "d" "t" ["p1"] {:requestedPolicyVersion 1}])))
        (is (= {:op ::bq/TestIamPermissions :bigquery client :tableId {:project "p" :dataset "d" :table "t"} :permissions ["p1"] :opts {:requestedPolicyVersion 1}} (bqc/->TestIamPermissions [client "p" "d" "t" ["p1"] {:requestedPolicyVersion 1}])))
        (is (= {:op ::bq/TestIamPermissions :bigquery client :tableId table-id :permissions ["p1"] :opts {:requestedPolicyVersion 1}} (bqc/->TestIamPermissions [{:bigquery client :tableId table-id :permissions ["p1"] :opts {:requestedPolicyVersion 1}}])) "callRecord")
        (let [call (bqc/->TestIamPermissions [mock-table ["p1"]])]
          (is (and (= (:op call) ::bq/TestIamPermissions) (g/valid? ::bq/TestIamPermissions call)) "mock table resolve"))))))

(deftest query-test
  (and
    (testing "query (->Query)"
      (and
        (is (= {:op ::bq/Query :bigquery nil :configuration query-job :jobId nil :opts nil} (bqc/->Query [query-job])))
        (is (= {:op ::bq/Query :bigquery client :configuration query-job :jobId nil :opts nil} (bqc/->Query [client query-job])))
        (is (= {:op ::bq/Query :bigquery nil :configuration query-job :jobId job-id :opts nil} (bqc/->Query [query-job "j"])))
        (is (= {:op ::bq/Query :bigquery nil :configuration query-job :jobId job-id :opts nil} (bqc/->Query [query-job job-id])) "map-based jobId arity-2")
        (is (= {:op ::bq/Query :bigquery client :configuration query-job :jobId job-id :opts nil} (bqc/->Query [client query-job "j"])))
        (is (= {:op ::bq/Query :bigquery client :configuration query-job :jobId job-id :opts nil} (bqc/->Query [client query-job job-id])) "map-based jobId arity-3")
        (is (= {:op ::bq/Query :bigquery nil :configuration query-job :jobId job-id :opts {}} (bqc/->Query [query-job "j" {}])))
        (is (= {:op ::bq/Query :bigquery nil :configuration query-job :jobId job-id :opts {}} (bqc/->Query [query-job job-id {}])) "map-based jobId arity-3 with opts")
        (is (= {:op ::bq/Query :bigquery client :configuration query-job :jobId job-id :opts {}} (bqc/->Query [client query-job "j" {}])))
        (is (= {:op ::bq/Query :bigquery client :configuration query-job :jobId job-id :opts {}} (bqc/->Query [client query-job job-id {}])) "map-based jobId arity-4")
        (is (= {:op ::bq/Query :bigquery client :configuration query-job :jobId job-id :opts {}} (bqc/->Query [{:bigquery client :configuration query-job :jobId job-id :opts {}}])) "callRecord")
        (let [call (bqc/->Query [query-job mock-job])]
          (is (and (= (:op call) ::bq/Query) (g/valid? ::bq/Query call)) "mock job resolve"))))
    (testing "q (->Q)"
      (and
        (is (= {:op ::bq/Query :configuration {:type "QUERY" :query "SELECT 1"}} (bqc/->Q ["SELECT 1"])))
        (is (= {:op ::bq/Query :bigquery client :configuration {:type "QUERY" :query "SELECT 1"}} (bqc/->Q [client "SELECT 1"])))
        (is (= {:op ::bq/Query :configuration {:type "QUERY" :query "SELECT 1" :positionalParameters [{:value "A"}]}} (bqc/->Q ["SELECT 1" [{:value "A"}]])))
        (is (= {:op ::bq/Query :configuration {:type "QUERY" :query "SELECT 1" :namedParameters {:p {:value "A"}}}} (bqc/->Q ["SELECT 1" {:p {:value "A"}}])))
        (is (= {:op ::bq/Query :bigquery client :configuration {:type "QUERY" :query "SELECT 1" :positionalParameters [{:value "A"}]}} (bqc/->Q [client "SELECT 1" [{:value "A"}]])))
        (is (= {:op ::bq/Query :bigquery client :configuration {:type "QUERY" :query "SELECT 1" :namedParameters {:p {:value "A"}}}} (bqc/->Q [client "SELECT 1" {:p {:value "A"}}])))))
    (testing "query-with-timeout (->QueryWithTimeout)"
      (and
        (is (= {:op ::bq/QueryWithTimeout :bigquery nil :configuration query-job :jobId job-id :timeoutMs 1000 :opts nil} (bqc/->QueryWithTimeout [query-job "j" 1000])))
        (is (= {:op ::bq/QueryWithTimeout :bigquery client :configuration query-job :jobId job-id :timeoutMs 1000 :opts nil} (bqc/->QueryWithTimeout [client query-job "j" 1000])))
        (is (= {:op ::bq/QueryWithTimeout :bigquery nil :configuration query-job :jobId job-id :timeoutMs 1000 :opts {}} (bqc/->QueryWithTimeout [query-job "j" 1000 {}])))
        (is (= {:op ::bq/QueryWithTimeout :bigquery client :configuration query-job :jobId job-id :timeoutMs 1000 :opts {}} (bqc/->QueryWithTimeout [client query-job "j" 1000 {}])))
        (is (= {:op ::bq/QueryWithTimeout :bigquery client :configuration query-job :jobId job-id :timeoutMs 1000 :opts {}} (bqc/->QueryWithTimeout [{:bigquery client :configuration query-job :jobId job-id :timeoutMs 1000 :opts {}}])) "callRecord")
        (let [call (bqc/->QueryWithTimeout [query-job mock-job 1000])]
          (is (and (= (:op call) ::bq/QueryWithTimeout) (g/valid? ::bq/QueryWithTimeout call)) "mock job resolve"))))))

(deftest other-test
  (and
    (testing "create-connection (->ConnectionCreate)"
      (and
        (is (= {:op ::bq/ConnectionCreate :bigquery nil   }  (bqc/->ConnectionCreate [])))
        (is (= {:op ::bq/ConnectionCreate :bigquery client}  (bqc/->ConnectionCreate [client])))
        (is (= {:op ::bq/ConnectionCreate :bigquery nil    :connectionSettings {:maxResults 10}} (bqc/->ConnectionCreate [nil {:maxResults 10}])))
        (is (= {:op ::bq/ConnectionCreate :bigquery client :connectionSettings {:maxResults 10}} (bqc/->ConnectionCreate [client {:maxResults 10}])))
        (is (= {:op ::bq/ConnectionCreate :bigquery client :connectionSettings {:maxResults 10}} (bqc/->ConnectionCreate [{:bigquery client :connectionSettings {:maxResults 10}}])) "callRecord")))
    (testing "writer (->Writer)"
      (and
        (is (= {:op ::bq/Writer :bigquery nil    :writeChannelConfiguration {:destinationTable table-id} :jobId nil}    (bqc/->Writer [{:destinationTable table-id}])))
        (is (= {:op ::bq/Writer :bigquery client :writeChannelConfiguration {:destinationTable table-id} :jobId nil}    (bqc/->Writer [client {:destinationTable table-id}])))
        (is (= {:op ::bq/Writer :bigquery nil    :writeChannelConfiguration {:destinationTable table-id} :jobId job-id} (bqc/->Writer ["j" {:destinationTable table-id}])))
        (is (= {:op ::bq/Writer :bigquery nil    :writeChannelConfiguration {:destinationTable table-id} :jobId job-id} (bqc/->Writer [job-id {:destinationTable table-id}])) "map-based jobId arity-2")
        (is (= {:op ::bq/Writer :bigquery client :writeChannelConfiguration {:destinationTable table-id} :jobId job-id} (bqc/->Writer [client "j" {:destinationTable table-id}])))
        (is (= {:op ::bq/Writer :bigquery client :writeChannelConfiguration {:destinationTable table-id} :jobId job-id} (bqc/->Writer [client job-id {:destinationTable table-id}])) "map-based jobId arity-3")
        (is (= {:op ::bq/Writer :bigquery client :writeChannelConfiguration {:destinationTable table-id} :jobId job-id} (bqc/->Writer [{:bigquery client :writeChannelConfiguration {:destinationTable table-id} :jobId job-id}])) "callRecord")
        (let [call (bqc/->Writer [mock-job {:destinationTable table-id}])]
          (is (and (= (:op call) ::bq/Writer) (g/valid? ::bq/Writer call)) "mock job resolve"))))
    (testing "wait-for (->JobWaitFor)"
      (and
        (is (= {:op ::bq/JobWaitFor :bigquery nil :jobId job-id} (bqc/->JobWaitFor ["j"])))
        (is (= {:op ::bq/JobWaitFor :bigquery nil :jobId job-id} (bqc/->JobWaitFor [job-id])))
        (is (= {:op ::bq/JobWaitFor :bigquery client :jobId job-id} (bqc/->JobWaitFor [client "j"])))
        (is (= {:op ::bq/JobWaitFor :bigquery client :jobId job-id} (bqc/->JobWaitFor [client job-id])))
        (is (= {:op ::bq/JobWaitFor :bigquery nil :jobId job-id :retryOptions [{:maxAttempts 1}]} (bqc/->JobWaitFor [job-id {:retryOptions [{:maxAttempts 1}]}])))
        (let [call (bqc/->JobWaitFor [mock-job])]
          (is (and (= (:op call) ::bq/JobWaitFor) (g/valid? ::bq/JobWaitFor call)) "mock job resolve"))
        (is (= {:op ::bq/JobWaitFor :bigquery client :jobId job-id :retryOptions [{:maxAttempts 1}]} (bqc/->JobWaitFor [client job-id {:retryOptions [{:maxAttempts 1}]}])))))
    (testing "done? (->JobIsDone)"
      (and
        (is (= {:op ::bq/JobIsDone :bigquery nil :jobId job-id} (bqc/->JobIsDone ["j"])))
        (is (= {:op ::bq/JobIsDone :bigquery nil :jobId job-id} (bqc/->JobIsDone [job-id])))
        (is (= {:op ::bq/JobIsDone :bigquery client :jobId job-id} (bqc/->JobIsDone [client "j"])))
        (is (= {:op ::bq/JobIsDone :bigquery client :jobId job-id} (bqc/->JobIsDone [client job-id])))
        (let [call (bqc/->JobIsDone [mock-job])]
          (is (and (= (:op call) ::bq/JobIsDone) (g/valid? ::bq/JobIsDone call)) "mock job resolve"))
        (is (= {:op ::bq/JobIsDone :bigquery nil :jobId job-id :bigQueryRetryConfig {}} (bqc/->JobIsDone [job-id {:bigQueryRetryConfig {}}])) "job-id vs clientable ambiguity check")
        (is (= {:op ::bq/JobIsDone :bigquery client :jobId job-id :bigQueryRetryConfig {}} (bqc/->JobIsDone [client job-id {:bigQueryRetryConfig {}}])))))))
