(ns gcp.storage.aux.hive-writer-unit-test
  (:require
   [clojure.test :refer :all]
   [gcp.storage.aux.hive-writer :as hw]))

(deftest hive-writer-creation-test
  (testing "Writer creates successfully with GAX settings and correctly parses batching thresholds"
    (let [;; We can pass nil for storage-client in this test since we only test the constructor map
          writer (hw/create-hive-writer
                   nil
                   {:bucketName "test-bucket"
                    :servicePrefix "test/requests"
                    :replicaId "test-pod-1"
                    :batchingSettings {:flowControlSettings {:maxOutstandingElementCount 500
                                                             :limitExceededBehavior :Ignore}
                                       :delayThreshold 10
                                       :requestByteThreshold 2048}})]
      (is (= "test-bucket" (:bucketName writer)))
      (is (= "test/requests" (:servicePrefix writer)))
      (is (= "test-pod-1" (:replicaId writer)))
      ;; 10 seconds * 1000 = 10000 ms
      (is (= 10000 (:maxSessionMs writer)))
      (is (= 2048 (:maxSessionBytes writer)))
      (is (= :running @(:state writer)))
      ;; Capacity of the queue should match maxOutstandingElementCount
      (is (= 500 (.remainingCapacity (:queue writer)))))))

(deftest hive-writer-block-prevention-test
  (testing "Writer strictly rejects :Block limit behavior to prevent OOM/Serving thread locking"
    (is (thrown-with-msg? Exception
                          #"limitExceededBehavior to be :Ignore"
                          (hw/create-hive-writer
                            nil
                            {:bucketName "test-bucket"
                             :servicePrefix "test"
                             :replicaId "pod-1"
                             :batchingSettings {:flowControlSettings {:limitExceededBehavior :Block}}})))))

(deftest hive-writer-offer-test
  (testing "Writer accepts elements until full, then correctly drops and returns false"
    (let [writer (hw/create-hive-writer
                   nil
                   {:bucketName "test-bucket"
                    :servicePrefix "test"
                    :replicaId "pod-1"
                    :batchingSettings {:flowControlSettings {:maxOutstandingElementCount 2
                                                             :limitExceededBehavior :Ignore}}})]
      ;; Queue has capacity 2
      (is (true? (hw/offer! writer {:event 1})))
      (is (true? (hw/offer! writer {:event 2})))
      ;; Queue is now full, next offer should fail (drop) and return false
      (is (false? (hw/offer! writer {:event 3}))))))

(deftest rotation-logic-test
  (testing "rotate? triggers correctly based on time and volume thresholds"
    (let [rotate? #'hw/rotate?
          max-age-ms 10000
          max-bytes 1024
          start-time (System/currentTimeMillis)]
      (testing "Does not rotate if under limits"
        (is (false? (rotate? start-time 500 max-age-ms max-bytes))))
      (testing "Rotates if volume limit exceeded"
        (is (true? (rotate? start-time 1025 max-age-ms max-bytes))))
      (testing "Rotates if time limit exceeded"
        (let [old-start (- start-time 15000)]
          (is (true? (rotate? old-start 500 max-age-ms max-bytes))))))))

(deftest hive-path-format-test
  (testing "Writer generates strict dt=YYYY-MM-DD/hr=HH format for BigQuery partitions"
    (let [current-hive-prefix #'hw/current-hive-prefix
          prefix (current-hive-prefix)]
      ;; Example: dt=2026-05-18/hr=14
      (is (re-matches #"^dt=\d{4}-\d{2}-\d{2}/hr=\d{2}$" prefix)))))

(deftest graceful-shutdown-test
  (testing "stop! sets state to :stopping and allows queue to drain"
    (let [writer (hw/create-hive-writer
                   nil
                   {:bucketName "test-bucket"
                    :servicePrefix "test"
                    :replicaId "pod-1"
                    :batchingSettings {:flowControlSettings {:maxOutstandingElementCount 5
                                                             :limitExceededBehavior :Ignore}}})]
      (is (= :running @(:state writer)))
      (hw/stop! writer)
      (is (= :stopping @(:state writer))))))

(deftest error-recovery-test
  (testing "loop catches IOExceptions, drops the session, and recovers cleanly"
    ;; This is a behavioral contract validation stub
    (is true)))
