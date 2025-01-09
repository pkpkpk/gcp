(ns gcp.bigquery.v2.TableId
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery TableId]))

(defn ^TableId from-edn
  [{:keys [dataset table project] :as arg}]
  (global/strict! :bigquery/TableId arg)
  (if project
    (TableId/of project dataset table)
    (TableId/of dataset table)))

(defn get-iam-resource-name [arg]
  (if (instance? TableId arg)
    (.getIAMResourceName arg)
    (.getIAMResourceName (from-edn arg))))