(ns gcp.bigquery.v2.JobConfiguration
  (:import [com.google.cloud.bigquery JobConfiguration])
  (:require [gcp.bigquery.v2.CopyJobConfiguration :as CopyJobConfiguration]
            [gcp.bigquery.v2.ExtractJobConfiguration :as
             ExtractJobConfiguration]
            [gcp.bigquery.v2.LoadJobConfiguration :as LoadJobConfiguration]
            [gcp.bigquery.v2.QueryJobConfiguration :as QueryJobConfiguration]
            [gcp.global :as global]))

(defn to-edn
  [^JobConfiguration arg]
  {:post [(global/strict! :gcp.bigquery.v2/JobConfiguration %)]}
  (case (.name (.getType arg))
    "EXTRACT" (assoc (ExtractJobConfiguration/to-edn arg) :type "EXTRACT")
    "LOAD" (assoc (LoadJobConfiguration/to-edn arg) :type "LOAD")
    "QUERY" (assoc (QueryJobConfiguration/to-edn arg) :type "QUERY")
    "COPY" (assoc (CopyJobConfiguration/to-edn arg) :type "COPY")))

(defn ^JobConfiguration from-edn
  [arg]
  (global/strict! :gcp.bigquery.v2/JobConfiguration arg)
  (or
    (and (or (= "COPY" (get arg :type))
             (global/valid? :gcp.bigquery.v2/CopyJobConfiguration arg))
         (CopyJobConfiguration/from-edn arg))
    (and (or (= "EXTRACT" (get arg :type))
             (global/valid? :gcp.bigquery.v2/ExtractJobConfiguration arg))
         (ExtractJobConfiguration/from-edn arg))
    (and (or (= "LOAD" (get arg :type))
             (global/valid? :gcp.bigquery.v2/LoadJobConfiguration arg))
         (LoadJobConfiguration/from-edn arg))
    (and (or (= "QUERY" (get arg :type))
             (global/valid? :gcp.bigquery.v2/QueryJobConfiguration arg))
         (QueryJobConfiguration/from-edn arg))
    (throw
      (clojure.core/ex-info
        "failed to match variant for union com.google.cloud.bigquery.JobConfiguration"
        {:arg arg, :expected #{"EXTRACT" "LOAD" "QUERY" "COPY"}}))))

(def schemas
  {:gcp.bigquery.v2/JobConfiguration
   [:or
    {:gcp/type :abstract-union
     :class 'com.google.cloud.bigquery.JobConfiguration}
    :gcp.bigquery.v2/CopyJobConfiguration
    :gcp.bigquery.v2/ExtractJobConfiguration
    :gcp.bigquery.v2/LoadJobConfiguration
    :gcp.bigquery.v2/QueryJobConfiguration]

   :gcp.bigquery.v2/JobConfiguration.Type [:enum "EXTRACT" "QUERY" "COPY" "LOAD"]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))