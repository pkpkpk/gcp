(ns gcp.bigquery.v2.InsertAllResponse
  (:require [gcp.bigquery.v2.BigQueryError :as BigQueryError]
            [gcp.global :as g])
  (:import (com.google.cloud.bigquery InsertAllResponse)))

(defn to-edn [^InsertAllResponse arg]
  (when (.hasErrors arg)
    (let [errors (.getInsertErrors arg)
          response (into {}
                         (map
                           (fn [[k v]]
                             [k (mapv BigQueryError/to-edn v)]))
                         errors)
          response (g/coerce :gcp/bigquery.InsertAllResponse response)]
      (throw (ex-info (str (count errors) " insertion errors") response)))))