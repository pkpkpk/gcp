(ns gcp.dev.packages-test
  (:require [clojure.test :refer [deftest is testing]]
            [gcp.dev.packages :as p])
  (:import (clojure.lang ExceptionInfo)))

(deftest lookup-class-test
  (testing "Lookup BigQuery class (flat package)"
    (let [bq (p/lookup-class :bigquery "BigQuery")]
      (is (some? bq) "BigQuery class should be found")
      (is (= "com.google.cloud.bigquery.BigQuery" 
             (str (:package bq) "." (:name bq)))
          "FQCN should match")))

  (testing "Lookup VertexAI class (nested package) miss"
    (is (nil? (p/lookup-class :vertexai "GenerateContentRequest"))))

  (testing "Lookup VertexAI class (nested package) hit"
    (let [vx (p/lookup-class :vertexai "api.GenerateContentRequest")]
      (is (some? vx) "GenerateContentRequest class should be found")
      (is (= "com.google.cloud.vertexai.api.GenerateContentRequest"
             (str (:package vx) "." (:name vx)))
          "FQCN should match")))

  (testing "Lookup non-existent class returns nil"
    (is (nil? (p/lookup-class :bigquery "NonExistentClass"))))

  (testing "Lookup with invalid package keyword raises exception"
    (is (thrown? clojure.lang.ExceptionInfo (p/lookup-class :invalid-package "SomeClass"))))

  (testing "Class types extraction"
    (let [bq (p/lookup-class :bigquery "BigQuery")
          types (p/class-user-types bq)]
      (is (contains? types 'com.google.cloud.bigquery.TableId) "Should contain TableId")
      (is (not (contains? types 'void)) "Should not contain void")))

  (testing "Class foreign user types extraction"
    (let [bq (p/class-foreign-user-types :bigquery "BigQuery")]
      (is (not (contains? bq 'com.google.cloud.bigquery.TableId)) "Should NOT contain local TableId")
      (is (contains? bq 'java.lang.String) "Should contain foreign String"))))
