(ns gcp.bigquery-examples
  (:require [gcp.bigquery :as bq]))

#_ (do (require :reload 'gcp.bigquery.bigquery-examples) (in-ns 'gcp.bigquery.bigquery-examples))

(comment

  (bq/create-dataset "gcp_samples")

  ;; routines
  (bq/create-routine {:routineId   {:dataset "gcp_samples"
                                    :routine "sample0"}
                      :routineType "SCALAR_FUNCTION"
                      :language    "SQL"
                      :body        "x * 3"
                      :arguments   [{:name "x" :dataType {:typeName "INT64"}}]})

  (bq/list-routines "gcp_samples")

  (bq/get-routine {:dataset "gcp_samples" :routine "sample0"})

  (bq/delete-routine {:dataset "gcp_samples" :routine "sample0"})

  ;Page<Routine> routines = bigquery.listRoutines(datasetName, BigQuery.RoutineListOption.pageSize(100));


  )
