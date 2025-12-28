(ns gcp.dev.test-runner
  (:require [clojure.test :as t]
            [gcp.dev.packages-test]))

(defn -main [& args]
  (let [results (t/run-tests 'gcp.dev.packages-test)]
    (if (some pos? ((juxt :fail :error) results))
      (System/exit 1)
      (System/exit 0))))
