(ns gcp.dev.test.recursive-deps-test
  (:require [clojure.test :refer [deftest is testing]]
            [gcp.dev.packages :as p]))

(deftest class-deps-recursive-test
  (testing "class-deps with recursive? true"
    (let [deps (p/class-deps :bigquery "TableId" true)]
      (is (contains? (:internal deps) 'com.google.cloud.bigquery.TableId))
      (is (contains? (:foreign deps) 'java.lang.String))))

  (testing "class-deps recursive robustness (:logging Logging)"
    ;; This ensures we handle potential missing nodes gracefully
    (let [deps (p/class-deps :logging "Logging" true)]
      (is (seq (:internal deps)))
      (is (seq (:foreign deps)))))

  (testing "class-foreign-deps with recursive? true"
    (let [deps (p/class-foreign-deps :bigquery "TableId" true)]
      (is (contains? deps 'java.lang.String))
      (is (not (contains? deps 'com.google.cloud.bigquery.TableId))))))
