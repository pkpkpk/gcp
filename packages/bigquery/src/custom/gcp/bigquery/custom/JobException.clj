(ns gcp.bigquery.custom.JobException
  {:doc "Exception describing a failure of a job."
   :file-git-sha "a335927e16d0907d62e584f08fa8393daae40354"
   :fqcn "com.google.cloud.bigquery.JobException"}
  (:require
   [gcp.bigquery.BigQueryError :as BigQueryError]
   [gcp.bigquery.JobId :as JobId]
   [gcp.global :as g])
  (:import
   (com.google.cloud.bigquery JobException)))

(defn from-edn [arg]
  (throw (Exception. "Class com.google.cloud.bigquery.JobException is read-only")))

(defn to-edn [^JobException arg]
  (let [errors (mapv BigQueryError/to-edn (.getErrors arg))
        data (cond-> {:cause arg
                      :id (JobId/to-edn (.getId arg))}
                     (not-empty errors) (assoc :errors errors))]
    (ex-info (.getMessage arg) data)))

(def schema (g/instance-schema {:gcp/category :read-only} clojure.lang.ExceptionInfo))

(g/include-schema-registry!
  (with-meta {:gcp.bigquery/JobException schema}
    {:gcp.global/name "gcp.bigquery.custom.JobException"}))
