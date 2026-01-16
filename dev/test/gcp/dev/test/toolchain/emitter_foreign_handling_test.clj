(ns gcp.dev.test.toolchain.emitter-foreign-handling-test
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [clojure.test :refer [deftest is run-tests testing]]
   [gcp.dev.digest :as digest]
   [gcp.dev.toolchain.emitter :as e]
   [gcp.dev.util :as u]))

(defn make-node [fqcn type-fqcn]
  (let [pkg (or (first (string/split fqcn #"\.[^.]+$")) "gcp.test")
        cls (last (string/split fqcn #"\."))]
    {:package pkg
     :className cls
     :doc "service v1 for test"
     :type :accessor
     :nested []
     :typeDependencies [type-fqcn]
     :fields {"field1" {:type type-fqcn
                        :setterMethod "setField1"
                        :getterMethod "getField1"}}}))

(deftest test-foreign-resolution
  (testing "Namespace missing"
    (let [node (make-node "gcp.test.Test" "com.google.fake.Missing")]
      (try
        (e/compile-class-forms node)
        (is false "Should have thrown exception for missing namespace")
        (catch Exception e
          (is (re-find #"Foreign namespace missing" (.getMessage e))
              (str "Unexpected message: " (.getMessage e)))))))

  (testing "Namespace exists but NOT certified"
    (let [node (make-node "gcp.test.Test" "dev.test.fixtures.foreign_uncertified.Uncertified")]
      ;; Mock infer-foreign-ns to return our test fixture namespace
      (with-redefs [u/infer-foreign-ns (fn [fqcn]
                                         (if (string/includes? fqcn "foreign_uncertified")
                                           'gcp.dev.test.fixtures.foreign-uncertified
                                           (#'u/infer-foreign-ns fqcn)))
                    u/foreign-binding-exists? (fn [ns-sym]
                                                (if (= ns-sym 'gcp.dev.test.fixtures.foreign-uncertified)
                                                  true
                                                  (#'u/foreign-binding-exists? ns-sym)))]
        (try
          (e/compile-class-forms node)
          (is false "Should have thrown exception for uncertified namespace")
          (catch Exception e
            (is (re-find #"Foreign namespace NOT CERTIFIED" (.getMessage e))
                (str "Unexpected message: " (.getMessage e))))))))

  (testing "Function missing in existing certified namespace"
    (let [node (make-node "gcp.test.Test" "com.google.protobuf.NonExistent")]
      (try
        (e/compile-class-forms node)
        (is false "Should have thrown exception for missing function")
        (catch Exception e
          (is (re-find #"Foreign function missing" (.getMessage e))
              (str "Unexpected message: " (.getMessage e)))))))

  (testing "Success case"
    (let [node (make-node "gcp.test.Test" "com.google.protobuf.ByteString")]
      (let [forms (e/compile-class-forms node)]
        (is (some? forms) "Should compile successfully")))))

(deftest test-hashing-consistency
  (testing "Hash is consistent with and without metadata"
    (let [root (u/get-gcp-repo-root)
          original-path (str root "/packages/global/src/gcp/foreign/com/google/protobuf.clj")
          original-content (slurp original-path)
          ;; Compute hash of current file (which has metadata)
          hash-with-meta (digest/compute-foreign-source-hash 'gcp.foreign.com.google.protobuf)
          ;; Create a version without metadata
          clean-content (u/update-ns-metadata original-content :gcp.dev/certification nil)
          temp-file (java.io.File/createTempFile "protobuf_clean" ".clj")]
      (try
        (spit temp-file clean-content)
        ;; Verify hashes match by mocking slurp to return the clean content for the protobuf file
        (let [original-slurp clojure.core/slurp]
          (with-redefs [clojure.core/slurp (fn [f]
                                             (if (and (string? f) (string/includes? f "/packages/global/src/gcp/foreign/com/google/protobuf.clj"))
                                               (original-slurp temp-file)
                                               (original-slurp f)))]
            (let [hash-without-meta (digest/compute-foreign-source-hash 'gcp.foreign.com.google.protobuf)]
              (is (= hash-with-meta hash-without-meta) "Hash should be identical regardless of certification metadata"))))
        (finally
          (when (.exists temp-file)
            (.delete temp-file)))))))
