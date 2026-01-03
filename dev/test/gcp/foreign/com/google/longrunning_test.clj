(ns gcp.foreign.com.google.longrunning-test
  (:require
   [clojure.test :refer :all]
   [clojure.test.check :as tc]
   [clojure.test.check.properties :as prop]
   [gcp.global :as global]
   [gcp.foreign.com.google.longrunning :as lro]
   [malli.generator :as mg])
  (:import
   (com.google.longrunning Operation)))

(deftest operation-test
  (testing "Operation roundtrip"
    (let [prop (prop/for-all [v (mg/generator :gcp.foreign.com.google.longrunning/Operation (global/mopts))]
                 (let [obj (lro/Operation-from-edn v)
                       edn (lro/Operation-to-edn obj)]
                   (and (instance? Operation obj)
                        (= v edn))))]
      (is (:pass? (tc/quick-check 50 prop))))))
