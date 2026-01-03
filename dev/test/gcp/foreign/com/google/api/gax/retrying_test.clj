(ns gcp.foreign.com.google.api.gax.retrying-test
  (:require
   [clojure.test :refer :all]
   [gcp.foreign.com.google.api.gax.retrying :as retrying])
  (:import
   (com.google.api.gax.retrying RetrySettings)
   (java.time Duration)))

(deftest retry-settings-test
  (testing "RetrySettings roundtrip"
    (let [v {:initialRetryDelay        (Duration/ofMillis 100)
             :initialRpcTimeout        (Duration/ofMillis 200)
             :maxAttempts              3
             :maxRetryDelay            (Duration/ofMillis 500)
             :maxRpcTimeout            (Duration/ofMillis 600)
             :retryDelayMultiplier     1.5
             :rpcTimeoutMultiplier     2.0
             :totalTimeout             (Duration/ofMillis 1000)}]
      (let [obj (retrying/RetrySettings-from-edn v)
            edn (retrying/RetrySettings-to-edn obj)]
        (is (instance? RetrySettings obj))
        ;; We check individual fields because Duration equality might be tricky or identity
        (is (= 3 (:maxAttempts edn)))
        (is (= 1.5 (:retryDelayMultiplier edn)))
        ;; Check durations
        (is (= 100 (.toMillis (:initialRetryDelay edn))))))))

