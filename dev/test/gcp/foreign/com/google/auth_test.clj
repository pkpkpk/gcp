(ns gcp.foreign.com.google.auth-test
  (:require
   [clojure.test :refer :all]
   [gcp.global :as global]
   [gcp.foreign.com.google.auth :as gauth])
  (:import
   (com.google.auth Credentials)))

(deftest credentials-schema-test
  (testing "Credentials schema"
    (is (global/get-schema :gcp.foreign.com.google.auth/Credentials))
    ;; Credentials is abstract, and concrete impls might not be on classpath or complex to mock here.
    ;; Just verifying the schema is registered is a good start.
    ))
