(ns gcp.iam.load-test
  (:require [clojure.test :refer :all]
            [gcp.iam :as iam]))

(deftest load-test
  (is (some? (find-ns 'gcp.iam))))
