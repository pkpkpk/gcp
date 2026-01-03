(ns gcp.foreign.com.google.rpc-test
  (:require
   [clojure.test :refer :all]
   [clojure.test.check :as tc]
   [clojure.test.check.properties :as prop]
   [gcp.global :as global]
   [gcp.foreign.com.google.rpc :as grpc]
   [malli.generator :as mg])
  (:import
   (com.google.rpc Status)))

(deftest status-test
  (testing "Status roundtrip"
    (let [prop (prop/for-all [v (mg/generator :gcp.foreign.com.google.rpc/Status (global/mopts))]
                 (let [obj (grpc/Status-from-edn v)
                       edn (grpc/Status-to-edn obj)]
                   (if (and (instance? Status obj)
                            (= v edn))
                     true
                     (do (println "Status mismatch:" {:v v :edn edn}) false))))]
      (is (:pass? (tc/quick-check 50 prop))))))
