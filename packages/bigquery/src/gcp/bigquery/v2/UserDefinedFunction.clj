(ns gcp.bigquery.v2.UserDefinedFunction
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery UserDefinedFunction)))

(defn ^UserDefinedFunction from-edn [arg]
  (global/strict! :gcp.bigquery.v2/UserDefinedFunction arg)
  (if (= "FROM_URI" (:type arg))
    (UserDefinedFunction/fromUri (:functionDefinition arg))
    (UserDefinedFunction/inline (:functionDefinition arg))))

(defn to-edn [arg] (throw (Exception. "unimplemented")))

(def schemas
  {:gcp.bigquery.v2/UserDefinedFunction
   [:map
    {:doc "https://cloud.google.com/bigquery/docs/user-defined-functions"}
    [:type :gcp.bigquery.v2/UserDefinedFunction.Type]
    [:functionDefinition :string]]

   :gcp.bigquery.v2/UserDefinedFunction.Type [:enum "FROM_URI" "INLINE"]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))