(ns gcp.genai.load-test
  (:require [clojure.test :refer :all]
            [gcp.genai :as genai]))

(deftest load-test
  (is (some? (find-ns 'gcp.genai))))
