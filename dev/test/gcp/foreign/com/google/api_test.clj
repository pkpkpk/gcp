(ns gcp.foreign.com.google.api-test
  (:require
   [clojure.test :refer :all]
   [clojure.test.check :as tc]
   [clojure.test.check.properties :as prop]
   [gcp.global :as global]
   [gcp.foreign.com.google.api :as gapi]
   [malli.generator :as mg])
  (:import
   (com.google.api MonitoredResource MonitoredResourceDescriptor HttpBody)))

(deftest monitored-resource-test
  (testing "MonitoredResource roundtrip"
    (let [prop (prop/for-all [v (mg/generator :gcp.foreign.com.google.api/MonitoredResource (global/mopts))]
                 (let [obj (gapi/MonitoredResource-from-edn v)
                       edn (gapi/MonitoredResource-to-edn obj)]
                   (and (instance? MonitoredResource obj)
                        (= v edn))))]
      (is (:pass? (tc/quick-check 50 prop))))))

(deftest monitored-resource-descriptor-test
  (testing "MonitoredResourceDescriptor roundtrip"
    (let [prop (prop/for-all [v (mg/generator :gcp.foreign.com.google.api/MonitoredResourceDescriptor (global/mopts))]
                 (let [obj (gapi/MonitoredResourceDescriptor-from-edn v)
                       edn (gapi/MonitoredResourceDescriptor-to-edn obj)]
                   (and (instance? MonitoredResourceDescriptor obj)
                        (= v edn))))]
      (is (:pass? (tc/quick-check 50 prop))))))
