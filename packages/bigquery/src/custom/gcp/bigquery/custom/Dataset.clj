(ns gcp.bigquery.custom.Dataset
  (:require
   [gcp.bigquery.DatasetInfo :as DatasetInfo]
   [gcp.global :as g]
   [malli.util :as mu])
  (:import
   (com.google.cloud.bigquery Dataset)))

(defn to-edn [^Dataset arg]
  (when arg
    (assoc (DatasetInfo/to-edn arg) :bigquery (.getBigQuery arg))))

(def schema
  (mu/merge
    DatasetInfo/schema
    [:map [:bigquery {:optional true} :any]]
    (g/mopts)))

(g/include-schema-registry! (with-meta {:gcp.bigquery/Dataset schema}
                                       {:gcp.global/name "gcp.bigquery.Dataset"}))
