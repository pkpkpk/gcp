(ns gcp.bigquery.v2.JobStatus
  (:require [gcp.bigquery.v2.BigQueryError :as BigQueryError]
            [gcp.global :as global])
  (:import (com.google.cloud.bigquery JobStatus)))

(defn to-edn [^JobStatus arg]
  (cond-> {:state (.name (.getState arg))}
          (seq (.getExecutionErrors arg))
          (assoc :executionErrors (map BigQueryError/to-edn (.getExecutionErrors arg)))
          (some? (.getError arg))
          (assoc :error (BigQueryError/to-edn (.getError arg)))))

(def schemas
  {:gcp.bigquery.v2/JobStatus
   [:map {:closed true}
    [:error {:optional true} :gcp.bigquery.v2/BigQueryError]
    [:executionErrors {:optional true} [:sequential :gcp.bigquery.v2/BigQueryError]]
    [:state [:enum "DONE" "PENDING" "RUNNING"]]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))