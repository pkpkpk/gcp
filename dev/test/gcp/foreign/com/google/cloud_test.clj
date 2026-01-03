(ns gcp.foreign.com.google.cloud-test
  (:require
   [clojure.test :refer :all]
   [gcp.foreign.com.google.cloud :as cloud])
  (:import
   (com.google.cloud MonitoredResource RetryOption)))

(deftest unimplemented-test
  (testing "Unimplemented bindings throw exception"
    (is (thrown? Exception (cloud/MonitoredResource-from-edn {})))
    (is (thrown? Exception (cloud/RetryOption-from-edn {})))))
