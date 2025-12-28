(ns gcp.examples.bigquery-examples
  (:require
   [clojure.java.io :as io]
   [gcp.bigquery :as bq])
  (:import
   (java.nio.channels Channels)))

#_ (do (require :reload 'gcp.bigquery.bigquery-examples) (in-ns 'gcp.bigquery.bigquery-examples))

;; TODO
;; --  Page<Routine> routines = bigquery.listRoutines(datasetName, BigQuery.RoutineListOption.pageSize(100));

(comment
  (bq/create-dataset "gcp_samples")
  ;; --  Routines ---------------------------------------------------------
  (def routine {:routineId   {:dataset "gcp_samples"
                              :routine "sample0"}
                :routineType "SCALAR_FUNCTION"
                :language    "SQL"
                :body        "x * 3"
                :arguments   [{:name "x" :dataType {:typeName "INT64"}}]})
  (bq/create-routine routine)
  (bq/list-routines {:dataset "gcp_samples"})
  (bq/list-routines "gcp_samples")
  (bq/get-routine {:dataset "gcp_samples" :routine "sample0"})
  (bq/get-routine "gcp_samples" "sample0")
  (bq/delete-routine {:dataset "gcp_samples" :routine "sample0"})
  (bq/create-routine routine)
  (bq/update-routine (assoc routine :body "x * 42"))
  (bq/get-routine "gcp_samples" "sample0")
  (bq/delete-routine "gcp_samples" "sample0")
  ;; -- Load Data ---------------------------------------------------------

  (def csv-autodetect-job {:configuration {:type "LOAD"
                                           :tableId {:dataset "gcp_samples" :table "csv_table"}
                                           :formatOptions {}}}))
