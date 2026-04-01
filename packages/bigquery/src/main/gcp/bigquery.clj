(ns gcp.bigquery
  (:require [gcp.bigquery.core :as bqc]))

(def client bqc/client)

#!-----------------------------------------------------------------------------
#! DATASETS https://cloud.google.com/bigquery/docs/datasets

(defn list-datasets [& args]
  (bqc/execute! (bqc/->DatasetList (vec args))))

(defn create-dataset [& args]
  (bqc/execute! (bqc/->DatasetCreate (vec args))))

(defn update-dataset [& args]
  (bqc/execute! (bqc/->DatasetUpdate (vec args))))

(defn get-dataset [& args]
  (bqc/execute! (bqc/->DatasetGet (vec args))))

(defn ^boolean delete-dataset [& args]
  (bqc/execute! (bqc/->DatasetDelete (vec args))))

#!-----------------------------------------------------------------------------
#! TABLES https://cloud.google.com/bigquery/docs/tables

(defn list-tables [& args]
  (bqc/execute! (bqc/->TableList (vec args))))

(defn list-partitions
  "@return {List<String>}"
  [& args]
  (bqc/execute! (bqc/->TableListPartitions (vec args))))

(defn create-table [& args]
  (bqc/execute! (bqc/->TableCreate (vec args))))

(defn update-table [& args]
  (bqc/execute! (bqc/->TableUpdate (vec args))))

(defn get-table
  "1-arity: (tableId)
   2-arity: (dataset table) | (TableId Opts) | (clientable TableId)
   3-arity: (project dataset table) | (dataset table opts) | (clientable dataset table) | (clientable TableId opts)
   4-arity: (clientable project dataset table) | (clientable dataset table opts)
   5-arity: (clientable project dataset table opts)"
  [& args]
  (bqc/execute! (bqc/->TableGet (vec args))))

(defn ^boolean delete-table [& args]
  (bqc/execute! (bqc/->TableDelete (vec args))))

(defn insert-all [& args]
  (bqc/execute! (bqc/->InsertAll (vec args))))

(defn list-table-data
  "If you do not provide the schema, it will return the raw TableResult instance"
  [& args]
  (bqc/execute! (bqc/->TableListData (vec args))))

#!-----------------------------------------------------------------------------
#! JOBS https://cloud.google.com/bigquery/docs/jobs-overview

(defn list-jobs [& args]
  (bqc/execute! (bqc/->JobList (vec args))))

(defn ^boolean cancel-job [& args]
  (bqc/execute! (bqc/->JobCancel (vec args))))

(defn create-job [& args]
  (bqc/execute! (bqc/->JobCreate (vec args))))

(defn get-job [& args]
  (bqc/execute! (bqc/->JobGet (vec args))))

(defn ^boolean delete-job [& args]
  (bqc/execute! (bqc/->JobDelete (vec args))))

(defn query [& args]
  (bqc/execute! (bqc/->Query (vec args))))

(defn query-with-timeout [& args]
  (bqc/execute! (bqc/->QueryWithTimeout (vec args))))

(defn q [& args]
  (bqc/execute! (bqc/->Q (vec args))))

#!-----------------------------------------------------------------------------
#! ROUTINES https://cloud.google.com/bigquery/docs/routines

(defn list-routines [& args]
  (bqc/execute! (bqc/->RoutineList (vec args))))

(defn create-routine [& args]
  (bqc/execute! (bqc/->RoutineCreate (vec args))))

(defn update-routine [& args]
  (bqc/execute! (bqc/->RoutineUpdate (vec args))))

(defn get-routine
  "1-arity: (routineId)
   2-arity: (client, routineId) | (routineId, opts) | (String dataset, String routine)
   3-arity: (client, routineId, opts) | (client, String dataset, String routine) | (String dataset, String routine, opts)
   4-arity: (client, String dataset, String routine, opts)"
  [& args]
  (bqc/execute! (bqc/->RoutineGet (vec args))))

(defn ^boolean delete-routine [& args]
  (bqc/execute! (bqc/->RoutineDelete (vec args))))

#!-----------------------------------------------------------------------------
#! MODELS https://cloud.google.com/bigquery/docs/bqml-introduction

(defn list-models [& args]
  (bqc/execute! (bqc/->ModelList (vec args))))

(defn update-model [& args]
  (bqc/execute! (bqc/->ModelUpdate (vec args))))

(defn get-model [& args]
  (bqc/execute! (bqc/->ModelGet (vec args))))

(defn ^boolean delete-model [& args]
  (bqc/execute! (bqc/->ModelDelete (vec args))))

#!-----------------------------------------------------------------------------

(defn get-iam-policy [& args]
  (bqc/execute! (bqc/->GetIamPolicy (vec args))))

(defn set-iam-policy [& args]
  (bqc/execute! (bqc/->SetIamPolicy (vec args))))

(defn test-iam-permissions [& args]
  (bqc/execute! (bqc/->TestIamPermissions (vec args))))

#!-----------------------------------------------------------------------------

(defn create-connection [& args]
  (bqc/execute! (bqc/->ConnectionCreate (vec args))))

(defn writer [& args]
  (bqc/execute! (bqc/->Writer (vec args))))
