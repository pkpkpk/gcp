(ns gcp.bigquery.custom.BigQuerySQLException
  (:require [gcp.bindings.bigquery.BigQueryError :as BigQueryError]
            [gcp.global :as g])
  (:import (com.google.cloud.bigquery BigQuerySQLException)))

(defn from-edn [arg]
  (throw (Exception. "Class com.google.cloud.bigquery.BigQuerySQLException is read-only")))

(defn to-edn [^BigQuerySQLException arg]
  (let [errors (mapv BigQueryError/to-edn (.getErrors arg))
        data (cond-> {:cause arg}
                     (not-empty errors) (assoc :errors errors))]
    (ex-info (.getMessage arg) data)))

(def schema (g/instance-schema {:gcp/category :read-only} clojure.lang.ExceptionInfo))

(g/include-schema-registry! (with-meta {:gcp.bigquery.custom/BigQuerySQLException schema}
                                       {:gcp.global/name "gcp.bigquery.custom.BigQuerySQLException"}))