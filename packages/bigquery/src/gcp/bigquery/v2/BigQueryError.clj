(ns gcp.bigquery.v2.BigQueryError
  (:require [clojure.string :as string]
            [gcp.global :as global])
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

(def schemas
  {:gcp.bigquery.v2/BigQueryError
   [:map {:closed true}
    [:debugInfo {:optional true} :string]
    [:location {:optional true} [:string {:min 1}]]
    [:message {:optional true} :string]
    [:reason {:optional true
              :doc      "https://cloud.google.com/bigquery/docs/error-messages"} :string]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))

