(ns gcp.bigquery.v2.InsertAllResponse
  (:require [gcp.bigquery.v2.BigQueryError :as BigQueryError]
            [gcp.global :as g])
  (:import (com.google.cloud.bigquery InsertAllResponse)))

(defn to-edn [^InsertAllResponse arg]
  {:post [(g/strict! :gcp/bigquery.InsertAllResponse %)]}
  (when (.hasErrors arg)
    (let [errors (.getInsertErrors arg)]
      (into {}
            (map
              (fn [[k v]]
                [k (mapv BigQueryError/to-edn v)]))
            errors))))