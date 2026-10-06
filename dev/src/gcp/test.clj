(ns gcp.test
  (:require [clojure.repl :refer :all]
            [gcp.bigquery :as bq]
            ;gcp.bigquery.parse-args-tests
            ;gcp.bigquery.custom-tests
            ;gcp.bigquery.crud-tests

            ))

#_ (do (require :reload 'gcp.test) (in-ns 'gcp.test))


;(defn bigquery-tests []
;  (clojure.test/run-tests 'gcp.bigquery.parse-args-tests
;                          'gcp.bigquery.custom-tests
;                          'gcp.bigquery.crud-tests))

(comment
  (bigquery-tests)

  )
