(ns gcp.bigquery.custom.Model
  (:require
   [gcp.bigquery.ModelInfo :as ModelInfo]
   [gcp.global :as g]
   [malli.util :as mu])
  (:import
   (com.google.cloud.bigquery Model)))

(defn to-edn [^Model arg]
  (when arg
    (assoc (ModelInfo/to-edn arg) :bigquery (.getBigQuery arg))))

(def schema
  (mu/merge
    ModelInfo/schema
    [:map [:bigquery {:optional true} :any]]
    (g/mopts)))

(g/include-schema-registry! (with-meta {:gcp.bigquery/Model schema}
                                       {:gcp.global/name "gcp.bigquery.Model"}))
