(ns gcp.dev.test-runner
  (:require
   [clojure.test :as t]
   gcp.dev.test.packages-test
   gcp.dev.test.recursive-deps-test
   gcp.dev.test.toolchain.analyzer-test
   gcp.dev.test.toolchain.emitter-test
   gcp.dev.test.toolchain.emitter-foreign-handling-test))

(defn run-all-tests []
  (t/run-tests 'gcp.dev.test.packages-test
               'gcp.dev.test.recursive-deps-test
               'gcp.dev.test.toolchain.analyzer-test
               'gcp.dev.test.toolchain.emitter-test
               'gcp.dev.test.toolchain.emitter-foreign-handling-test))

(defn -main [& args]
  (let [results (run-all-tests)]
    (if (some pos? ((juxt :fail :error) results))
      (System/exit 1)
      (System/exit 0))))
