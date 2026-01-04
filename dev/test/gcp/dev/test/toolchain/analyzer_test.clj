(ns gcp.dev.test.toolchain.analyzer-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [gcp.dev.packages :as p]
   [gcp.dev.toolchain.analyzer :as ana]))

(def packages [:bigquery :storage :pubsub :logging :monitoring :vertexai :genai])

(deftest analyze-all-package-classes-smoke-test
  (doseq [pkg-kw packages]
    (testing (str "Analyzing all classes in " pkg-kw)
      (let [pkg (p/parse pkg-kw)
            types (p/package-user-types pkg)]
        (doseq [t types]
          (let [node (p/lookup-class pkg t)]
            (when node
              (try
                (ana/analyze-class-node node)
                (is true)
                (catch Exception e
                  (is false (str "Failed to analyze " t ": " (.getMessage e))))))))))))
