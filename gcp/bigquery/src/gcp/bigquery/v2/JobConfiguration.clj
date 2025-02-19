(ns gcp.bigquery.v2.JobConfiguration
  (:import [com.google.cloud.bigquery JobConfiguration])
  (:require [gcp.bigquery.v2.CopyJobConfiguration :as CopyJobConfiguration]
            [gcp.bigquery.v2.ExtractJobConfiguration :as
             ExtractJobConfiguration]
            [gcp.bigquery.v2.LoadJobConfiguration :as LoadJobConfiguration]
            [gcp.bigquery.v2.QueryJobConfiguration :as QueryJobConfiguration]
            gcp.global))

(defn to-edn
  [^JobConfiguration arg]
  {:post [(gcp.global/strict! :gcp/bigquery.JobConfiguration %)]}
  (case (.name (.getType arg))
    "EXTRACT" (assoc (ExtractJobConfiguration/to-edn arg) :type "EXTRACT")
    "LOAD" (assoc (LoadJobConfiguration/to-edn arg) :type "LOAD")
    "QUERY" (assoc (QueryJobConfiguration/to-edn arg) :type "QUERY")
    "COPY" (assoc (CopyJobConfiguration/to-edn arg) :type "COPY")))

(defn ^JobConfiguration from-edn
  [arg]
  (gcp.global/strict! :gcp/bigquery.JobConfiguration arg)
  (or
    (and (or (= "COPY" (get arg :type))
             (gcp.global/valid? :gcp/bigquery.CopyJobConfiguration arg))
         (CopyJobConfiguration/from-edn arg))
    (and (or (= "EXTRACT" (get arg :type))
             (gcp.global/valid? :gcp/bigquery.ExtractJobConfiguration arg))
         (ExtractJobConfiguration/from-edn arg))
    (and (or (= "LOAD" (get arg :type))
             (gcp.global/valid? :gcp/bigquery.LoadJobConfiguration arg))
         (LoadJobConfiguration/from-edn arg))
    (and (or (= "QUERY" (get arg :type))
             (gcp.global/valid? :gcp/bigquery.QueryJobConfiguration arg))
         (QueryJobConfiguration/from-edn arg))
    (throw
      (clojure.core/ex-info
        "failed to match variant for union com.google.cloud.bigquery.JobConfiguration"
        {:arg arg, :expected #{"EXTRACT" "LOAD" "QUERY" "COPY"}}))))