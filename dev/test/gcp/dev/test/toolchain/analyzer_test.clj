(ns gcp.dev.test.toolchain.analyzer-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [gcp.dev.packages :as p]))

(deftest analyze-all-package-classes-smoke-test
  (doseq [pkg-kw (keys p/packages)]
    (testing (str "Analyzing all classes in " pkg-kw)
      (let [pkg (p/parse pkg-kw)
            types (p/package-api-types pkg)]
        (doseq [t types]
          (try
            (p/analyze-class pkg t)
            (is true)
            (catch Exception e
              (is false (str "Failed to analyze " t ": " (.getMessage e))))))))))
