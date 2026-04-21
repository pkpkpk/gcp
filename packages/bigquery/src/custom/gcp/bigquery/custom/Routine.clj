(ns gcp.bigquery.custom.Routine
  (:require
   [gcp.bigquery.RoutineInfo :as RoutineInfo]
   [gcp.global :as g]
   [malli.util :as mu])
  (:import
   (com.google.cloud.bigquery Routine)))

(defn to-edn [^Routine arg]
  (when arg
    (assoc (RoutineInfo/to-edn arg) :bigquery (.getBigQuery arg))))

(def schema
  (mu/merge
    RoutineInfo/schema
    [:map [:bigquery {:optional true} :any]]
    (g/mopts)))

(g/include-schema-registry! (with-meta {:gcp.bigquery/Routine schema}
                                       {:gcp.global/name "gcp.bigquery.Routine"}))
