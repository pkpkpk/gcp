(ns gcp.dev.test.toolchain.emitter-test
  (:require
   [clojure.test :refer :all]
   [gcp.dev.toolchain.emitter :as emitter]
   [gcp.dev.util :as u]))

;; Mock u/ns-meta for testing
(defn mock-ns-meta [certified?]
  (if certified?
    {:gcp.dev/certification {:some-type {:passed-stages {:smoke 123}}}}
    {}))

(deftest test-check-certification
  (testing "throws when not certified"
    (with-redefs [u/ns-meta (constantly (mock-ns-meta false))]
      (is (thrown-with-msg? clojure.lang.ExceptionInfo #"Foreign namespace NOT CERTIFIED"
                            (#'emitter/check-certification 'gcp.foreign.uncertified "com.example.Uncertified")))))

  (testing "passes when certified"
    (with-redefs [u/ns-meta (constantly (mock-ns-meta true))]
      (is (nil? (#'emitter/check-certification 'gcp.foreign.certified "com.example.Certified"))))))
