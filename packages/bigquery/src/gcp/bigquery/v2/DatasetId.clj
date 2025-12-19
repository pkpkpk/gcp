(ns gcp.bigquery.v2.DatasetId
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery DatasetId)))

(defn ^DatasetId from-edn
  [{:keys [project dataset] :as arg}]
  (global/strict! :gcp.bigquery.v2/DatasetId arg)
  (if project
    (DatasetId/of project dataset)
    (DatasetId/of dataset)))

(defn to-edn
  [^DatasetId arg]
  {:project (.getProject arg)
   :dataset (.getDataset arg)})

(def schemas
  {:gcp.bigquery.v2/DatasetId
   [:map {:closed true}
    [:dataset {:optional true} [:string {:min 1}]]
    [:project {:optional true} [:string {:min 1}]]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))