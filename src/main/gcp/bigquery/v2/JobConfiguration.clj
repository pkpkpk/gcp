(ns gcp.bigquery.v2.JobConfiguration
  (:require [gcp.bigquery.v2.CopyJobConfiguration :as CopyJobConfiguration]
            [gcp.bigquery.v2.ExtractJobConfiguration :as ExtractJobConfiguration]
            [gcp.bigquery.v2.LoadJobConfiguration :as LoadJobConfiguration]
            [gcp.bigquery.v2.QueryJobConfiguration :as QueryJobConfiguration]
            [gcp.global :as global])
  (:import (com.google.cloud.bigquery JobConfiguration)))

(defn from-edn [{t :type :as arg}]
  (global/strict! :gcp/bigquery.JobConfiguration arg)
  (case t
    "COPY"    (CopyJobConfiguration/from-edn arg)
    "EXTRACT" (ExtractJobConfiguration/from-edn arg)
    "LOAD"    (LoadJobConfiguration/from-edn arg)
    "QUERY"   (QueryJobConfiguration/from-edn arg)
    (throw (ex-info "unimplemented JobConfiguration/from-edn type" {:type t :arg arg}))))

(defn to-edn [^JobConfiguration arg]
  {:post [(global/strict! :gcp/bigquery.JobConfiguration %)]}
  (case (.name (.getType arg))
    "COPY"    (assoc (CopyJobConfiguration/to-edn arg)    :type "COPY")
    "EXTRACT" (assoc (ExtractJobConfiguration/to-edn arg) :type "EXTRACT")
    "LOAD"    (assoc (LoadJobConfiguration/to-edn arg)    :type "LOAD")
    "QUERY"   (assoc (QueryJobConfiguration/to-edn arg)   :type "QUERY")
    (throw (ex-info "unimplemented JobConfiguration/to-edn type" {:type (.name (.getType arg)) :arg arg}))))