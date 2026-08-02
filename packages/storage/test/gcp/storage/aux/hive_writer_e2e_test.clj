(ns gcp.storage.aux.hive-writer-e2e-test
  (:require
   [clojure.test :refer :all]
   [gcp.bigquery :as bq]
   [gcp.global :as g]
   [gcp.storage :as storage]
   [gcp.storage.aux.hive-writer :as hw]))
1
(def test-bucket (System/getenv "GCP_STORAGE_TEST_BUCKET"))
(def test-dataset (System/getenv "GCP_BQ_TEST_DATASET"))

;; ===============================================
;; BigQuery Integration Tests (Composed)
;; ===============================================

(defn- cleanup-test-artifacts!
  "Ensures a pristine test environment by removing any residual GCS blobs or BQ tables."
  [storage-client bq-client bucket prefix]
  (println "  -> Cleaning up BQ tables...")
  (try
    (bq/delete-table bq-client test-dataset "ext_test_requests")
    (catch Exception _ nil))
  (try
    (bq/delete-table bq-client test-dataset "load_test_requests")
    (catch Exception _ nil))
  (println "  -> Cleaning up GCS blobs at" (str "gs://" bucket "/" prefix "/..."))
  (when storage-client
    (let [blobs (storage/list-blobs storage-client bucket {:prefix prefix})]
      (doseq [blob blobs]
        (storage/delete-blob storage-client bucket (:name blob)))))
  true)

(defn- verify-external-table-path!
  "Validates Consumption Path A: BigQuery can read the JSONL files directly from GCS as an External Table with Hive Partitioning"
  [bq-client test-bucket prefix]
  (println "  -> 1. Configuring the External Table with Hive Partitioning enabled...")
  (let [uri-prefix (str "gs://" test-bucket "/" prefix "/")
        table-info {:tableId {:dataset test-dataset :table "ext_test_requests"}
                    :definition {:type "EXTERNAL"
                                 :autodetect true
                                 :schema {:fields [{:name "requestId" :type "STRING"}]}
                                 :sourceUris [(str uri-prefix "*")]
                                 :formatOptions {:type "NEWLINE_DELIMITED_JSON"}
                                 :hivePartitioningOptions {:mode "AUTO"
                                                           :sourceUriPrefix uri-prefix}}}
        _ (bq/create-table bq-client table-info)
        _ (println "  -> 2. Querying the table to assert `dt` and `hr` virtual columns exist and contain the dummy data...")
        query (str "SELECT requestId, dt, hr FROM `" test-dataset ".ext_test_requests` LIMIT 5")
        results (bq/q bq-client query)]
    (println "  -> 3. Query results successfully materialized via Zero-ETL:" results)
    (and (seq results)
         (contains? (first results) :dt)
         (contains? (first results) :hr)
         (contains? (first results) :requestId))))

(defn- verify-native-load-job-path!
  "Validates Consumption Path B: BigQuery can ingest the JSONL files via a Load Job, materializing the Hive partitions"
  [bq-client test-bucket prefix]
  (println "  -> 1. Configuring the Load Job to pull from GCS wildcard and respect Hive partitioning...")
  (let [uri-prefix (str "gs://" test-bucket "/" prefix "/")
        table-id   {:dataset test-dataset :table "load_test_requests"}
        job-config {:jobId         {:location "us-east1"
                                    :job (str "hive-test-load-" (System/currentTimeMillis))}
                    :configuration {:type                    "LOAD"
                                    :destinationTable        table-id
                                    :sourceUris              [(str uri-prefix "*")]
                                    :formatOptions           {:type "NEWLINE_DELIMITED_JSON"}
                                    :autodetect              true
                                    :hivePartitioningOptions {:mode            "AUTO"
                                                              :sourceUriPrefix uri-prefix}}}
        _ (println "  -> 2. Triggering the BigQuery Load Job and awaiting completion...")
        job        (bq/create-job bq-client job-config)
        _          (bq/wait-for bq-client job)]
    (println "  -> 3. Querying the materialized native table to ensure physical partitions (dt, hr) and data are mapped correctly...")
    (let [query (str "SELECT requestId, dt, hr FROM `" test-dataset ".load_test_requests` LIMIT 5")
          results (bq/q bq-client query)]
      (println "  -> 4. Query results exist natively in BigQuery storage:" results)
      (and (seq results)
           (contains? (first results) :dt)
           (contains? (first results) :hr)
           (contains? (first results) :requestId)))))

(deftest ^:integration hive-writer-e2e-test
  (testing "End-to-End: Writer successfully emits partitioned JSONL to GCS, which is subsequently readable by BigQuery via multiple paths"
    (if-not (and test-bucket test-dataset)
      (println "SKIPPING E2E TEST: GCP_STORAGE_TEST_BUCKET or GCP_BQ_TEST_DATASET not set in environment.")
      (let [storage-client (storage/client)
            bq-client (bq/client)
            prefix "test/hive-writer-e2e-test"]
        (testing "Phase 0: Setup & Teardown"
          (is (true? (cleanup-test-artifacts! storage-client bq-client test-bucket prefix))
              "Failed to sanitize test environment"))

        (testing "Phase 1: GCS Ingestion"
          (let [writer (hw/create-hive-writer
                         storage-client
                         {:bucketName test-bucket
                          :servicePrefix prefix
                          :replicaId "test-pod-1"
                          :batchingSettings {:flowControlSettings {:maxOutstandingElementCount 100
                                                                   :limitExceededBehavior :Ignore}
                                             :delayThreshold 10
                                             :requestByteThreshold 2048}})
                loop-future (future (hw/run-loop! writer))]
            ;; Offer some dummy requests using idiomatic Clojure keywords
            (hw/offer! writer {:requestId "req-1" :url "/api/v1/users" :userAgent "Mozilla/5.0" :ipAddress "192.168.1.1"})
            (hw/offer! writer {:requestId "req-2" :url "/api/v1/auth"  :userAgent "curl/7.68.0" :ipAddress "10.0.0.5"})
            (hw/offer! writer {:requestId "req-3" :url "/api/v1/data"  :userAgent "python-requests/2.25" :ipAddress "172.16.0.2"})
            ;; Stop the writer, forcing it to flush and finalize the BlobWriteSession
            (hw/stop! writer)
            @loop-future
            (is (= :stopped @(:state writer)) "GCS Write Phase Succeeded")))

        (testing "Phase 2: BigQuery External Table Consumption"
          (is (true? (verify-external-table-path! bq-client test-bucket prefix))
              "Failed to query GCS data as a partitioned External Table"))

        (testing "Phase 3: BigQuery Native Load Job Consumption"
          (is (true? (verify-native-load-job-path! bq-client test-bucket prefix))
              "Failed to ingest GCS data via a partitioned Load Job"))

        (testing "Phase 4: Final Teardown"
          (is (true? (cleanup-test-artifacts! storage-client bq-client test-bucket prefix))
              "Failed final teardown"))))))
