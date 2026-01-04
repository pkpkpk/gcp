(ns gcp.dev.test.packages-test
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.string :as string]
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
      (is (contains? bq 'java.lang.String) "Should contain foreign String")))

  (testing "Foreign user types by package"
    (let [types-by-pkg (p/foreign-user-types-by-package :bigquery)]
      (is (contains? types-by-pkg "java.lang") "Should contain java.lang package")
      (is (contains? (get types-by-pkg "java.lang") 'java.lang.String) "Should contain String in java.lang")
      (is (contains? types-by-pkg :primitive) "Should contain primitives")
      (is (contains? (get types-by-pkg :primitive) 'boolean) "Should contain boolean primitive"))))

(deftest package-foreign-dependencies-test
  (doseq [pkg-kw [:bigquery :storage :pubsub :logging :monitoring :vertexai :genai]]
    (testing (str "Foreign dependencies for " pkg-kw)
      (let [pkg (p/parse pkg-kw)
            pkg-name (:package-name pkg)
            foreign-deps (p/package-foreign-deps pkg)]
        
        (testing "Should not be empty"
          (is (seq foreign-deps) (str pkg-kw " foreign deps should not be empty")))
        
        (testing "Should not contain local types"
          (let [local-leaks (filter #(string/starts-with? (str %) pkg-name) foreign-deps)]
            (is (empty? local-leaks) 
                (str pkg-kw " foreign deps should not contain local types: " (vec local-leaks)))))
        
        (testing "Should contain primitives or common types (sanity check)"
           (is (or (contains? foreign-deps 'int)
                   (contains? foreign-deps 'boolean)
                   (contains? foreign-deps 'java.lang.String)
                   (contains? foreign-deps 'java.util.List))
               (str pkg-kw " should contain some common types")))))))
