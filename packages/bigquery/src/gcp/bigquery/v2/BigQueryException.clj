(ns gcp.bigquery.v2.BigQueryException
  (:require [gcp.bigquery.v2.BigQueryError :as BigQueryError]
            [gcp.global :as global])
  (:import (com.google.cloud.bigquery BigQueryException)))

; https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery.BigQueryException
(defn to-edn [^BigQueryException arg]
  (when-let [errors (not-empty (.getErrors arg))]
    (global/coerce :gcp.bigquery.v2/BigQueryException {:errors (mapv BigQueryError/to-edn errors)})))

(def schemas
  {:gcp.bigquery.v2/BigQueryException
   [:map {:closed true}
    [:errors [:sequential :gcp.bigquery.v2/BigQueryError]]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))