(ns gcp.bigquery.v2.InsertAllResponse
  (:require [gcp.bigquery.v2.BigQueryError :as BigQueryError]
            [gcp.global :as global])
  (:import (com.google.cloud.bigquery InsertAllResponse)))

(defn to-edn [^InsertAllResponse arg]
  (when (.hasErrors arg)
    (let [errors (.getInsertErrors arg)
          response (into {}
                         (map
                           (fn [[k v]]
                             [k (mapv BigQueryError/to-edn v)]))
                         errors)
          response (global/coerce :gcp.bigquery.v2/InsertAllResponse response)]
      (throw (ex-info (str (count errors) " insertion errors") response)))))

(def schemas
  {:gcp.bigquery.v2/InsertAllResponse
   [:maybe [:map-of :int [:seqable :gcp.bigquery.v2/BigQueryError]]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))