(ns gcp.bigquery.v2.BigQueryError
  (:import (com.google.cloud.bigquery BigQueryError)))

(defn to-edn [^BigQueryError arg]
  (cond-> {}
          (some? (.getDebugInfo arg))
          (assoc :debugInfo (.getDebugInfo arg))

          (some? (.getLocation arg))
          (assoc :location (.getLocation arg))

          (some? (.getMessage arg))
          (assoc :message (.getMessage arg))

          (some? (.getReason arg))
          (assoc :reason (.getReason arg))))

