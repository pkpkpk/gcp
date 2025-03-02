(ns gcp.bigquery.v2.UserDefinedFunction
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery UserDefinedFunction)))

(defn ^UserDefinedFunction from-edn [arg]
  (global/strict! :gcp/bigquery.UserDefinedFunctions arg)
  (if (= "FROM_URI" (:type arg))
    (UserDefinedFunction/fromUri (:functionDefinition arg))
    (UserDefinedFunction/inline (:functionDefinition arg))))

(defn to-edn [arg] (throw (Exception. "unimplemented")))