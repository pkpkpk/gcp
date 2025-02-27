(ns gcp.bigquery.v2.BigQueryError
  (:require [clojure.string :as string])
  (:import (com.google.cloud.bigquery BigQueryError)))

(defn to-edn [^BigQueryError arg]
  (cond-> {}
          (and (some? (.getDebugInfo arg))
               (not (string/blank? (.getDebugInfo arg))))
          (assoc :debugInfo (.getDebugInfo arg))

          (and (some? (.getLocation arg))
               (not (string/blank? (.getLocation arg))))
          (assoc :location (.getLocation arg))

          (and (some? (.getMessage arg))
               (not (string/blank? (.getMessage arg))))
          (assoc :message (.getMessage arg))

          (and (some? (.getReason arg))
               (not (string/blank? (.getReason arg))))
          (assoc :reason (.getReason arg))))

