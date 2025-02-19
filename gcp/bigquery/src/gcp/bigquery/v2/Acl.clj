(ns gcp.bigquery.v2.Acl
  (:import (com.google.cloud.bigquery Acl)))

;https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery.Acl

(defn ^Acl from-edn
  [{:as arg}]
  (throw (Exception. "unimplemented")))

(defn to-edn [^Acl arg]
  {:entity {:type (.name (.getType (.getEntity arg)))}
   :role   (.name (.getRole arg))})
