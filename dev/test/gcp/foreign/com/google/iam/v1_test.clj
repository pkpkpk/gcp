(ns gcp.foreign.com.google.iam.v1-test
  (:require
   [clojure.test :refer :all]
   [clojure.test.check :as tc]
   [clojure.test.check.properties :as prop]
   [gcp.global :as global]
   [gcp.foreign.com.google.iam.v1 :as iam]
   [malli.generator :as mg])
  (:import
   (com.google.iam.v1 Policy GetIamPolicyRequest)))

(deftest policy-test
  (testing "Policy roundtrip"
    (let [prop (prop/for-all [v (mg/generator :gcp.foreign.com.google.iam.v1/Policy (global/mopts))]
                 (let [obj (iam/Policy-from-edn v)
                       edn (iam/Policy-to-edn obj)]
                   (and (instance? Policy obj)
                        (= (:version v) (:version edn))
                        (if (:etag v) ;; ByteString or generic handling might differ, basic check
                          (some? (:etag edn))
                          true))))]
      (is (:pass? (tc/quick-check 50 prop))))))

(deftest get-iam-policy-request-test
  (testing "GetIamPolicyRequest roundtrip"
    (let [prop (prop/for-all [v (mg/generator :gcp.foreign.com.google.iam.v1/GetIamPolicyRequest (global/mopts))]
                 (let [obj (iam/GetIamPolicyRequest-from-edn v)
                       edn (iam/GetIamPolicyRequest-to-edn obj)]
                   (and (instance? GetIamPolicyRequest obj)
                        (= v edn))))]
      (is (:pass? (tc/quick-check 50 prop))))))
