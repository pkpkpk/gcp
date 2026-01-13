(ns gcp.artifactregistry.load-test
  (:require [clojure.test :refer :all]
            [gcp.artifactregistry :as artifactregistry]))

(deftest load-test
  (is (some? (find-ns 'gcp.artifactregistry))))
