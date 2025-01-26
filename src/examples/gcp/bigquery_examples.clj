(ns gcp.bigquery-examples
  (:require [gcp.bigquery :as bq]))

#_ (do (require :reload 'gcp.bigquery.bigquery-examples) (in-ns 'gcp.bigquery.bigquery-examples))

;; TODO
;;--  Page<Routine> routines = bigquery.listRoutines(datasetName, BigQuery.RoutineListOption.pageSize(100));

(comment
  (bq/create-dataset "gcp_samples")
  ;;--  Routines ---------------------------------------------------------
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
  )
