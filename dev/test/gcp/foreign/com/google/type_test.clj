(ns gcp.foreign.com.google.type-test
  (:require
   [clojure.test :refer :all]
   [clojure.test.check :as tc]
   [clojure.test.check.generators :as gen]
   [clojure.test.check.properties :as prop]
   [gcp.global :as global]
   [gcp.foreign.com.google.type :as gtype]
   [malli.generator :as mg])
  (:import
   (com.google.type Date LatLng)))

(deftest date-test
  (testing "Date roundtrip"
    (let [prop (prop/for-all [v (mg/generator :gcp.foreign.com.google.type/Date (global/mopts))]
                 (let [obj (gtype/Date-from-edn v)
                       edn (gtype/Date-to-edn obj)]
                   (and (instance? Date obj)
                        (= v edn))))]
      (is (:pass? (tc/quick-check 50 prop))))))

(deftest latlng-test
  (testing "LatLng roundtrip"
    (let [prop (prop/for-all [v (mg/generator :gcp.foreign.com.google.type/LatLng (global/mopts))]
                 (let [obj (gtype/LatLng-from-edn v)
                       edn (gtype/LatLng-to-edn obj)]
                   (and (instance? LatLng obj)
                        (= v edn))))]
      (is (:pass? (tc/quick-check 50 prop))))))
