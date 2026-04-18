(ns gcp.bigquery.custom.BigQueryException
  (:require
   [gcp.bigquery.BigQueryError :as BigQueryError]
   [gcp.global :as g])
  (:import
   (com.google.cloud.bigquery BigQueryException)))

(defn from-edn [arg]
  (throw (Exception. "Class com.google.cloud.bigquery.BigQueryException is read-only")))

(defn to-edn [^BigQueryException arg]
  (let [errs   (.getErrors arg)
        errors (when errs (mapv BigQueryError/to-edn errs))
        data   (cond-> {:cause  arg
                        :code   (.getCode arg)
                        :reason (.getReason arg)}
                 (seq errors) (assoc :errors errors))]
    (ex-info (.getMessage arg) data)))

(def schema (g/instance-schema {:gcp/category :read-only} clojure.lang.ExceptionInfo))

(g/include-schema-registry! (with-meta {:gcp.bigquery/BigQueryException schema}
                                       {:gcp.global/name "gcp.bigquery.custom.BigQueryException"}))
