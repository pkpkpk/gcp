(ns gcp.bigquery.tests.util
  (:require [clojure.test :refer :all]
            [gcp.bigquery :as bq]))

(defn wipe-dataset!
  [dataset-id]
  (when (bq/get-dataset dataset-id)
    (let [deadline (+ (System/currentTimeMillis) 30000)]
      (run! bq/delete-table (bq/list-tables dataset-id))
      (run! bq/delete-table (bq/list-routines dataset-id))
      (run! bq/delete-model (bq/list-models dataset-id))
      (loop [delay-ms 1000]
        (let [[err ok] (try
                         [nil (bq/delete-dataset dataset-id)]
                         (catch com.google.cloud.bigquery.BigQueryException e
                           [e nil]))]
          (when err (def err err))
          (cond
            ok true
            (and (= "resourceInUse" (.getReason err)) (< (System/currentTimeMillis) deadline))
            (do
              (Thread/sleep delay-ms)
              (recur (min 2000 (* 2 delay-ms))))
            :else (throw err)))))))

(defn run-test-capture
  [test-var]
  (let [events (atom [])
        original-report clojure.test/report]
    (with-redefs [clojure.test/report
                  (fn [event]
                    (swap! events conj event)
                    (original-report event))]
      (let [result (clojure.test/run-test-var test-var)]
        {:result result
         :events @events}))))