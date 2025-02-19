(ns gcp.bigquery.v2.BigQueryRetryConfig
  (:import (com.google.cloud.bigquery BigQueryRetryConfig)))

(defn ^BigQueryRetryConfig from-edn
  [{:keys [errorMessages regExPatterns] :as arg}]
  (let [builder (BigQueryRetryConfig/newBuilder)]
    (when (seq errorMessages)
      (.retryOnMessage builder (into-array String errorMessages)))
    (when (seq regExPatterns)
      (.retryOnRegEx builder (into-array String regExPatterns)))
    (.build builder)))