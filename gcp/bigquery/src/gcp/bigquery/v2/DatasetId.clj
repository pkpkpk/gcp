(ns gcp.bigquery.v2.DatasetId
  (:import (com.google.cloud.bigquery DatasetId)))

(defn ^DatasetId from-edn
  [{:keys [project dataset]}]
  (if project
    (DatasetId/of project dataset)
    (DatasetId/of dataset)))

(defn to-edn
  [^DatasetId arg]
  {:project (.getProject arg)
   :dataset (.getDataset arg)})