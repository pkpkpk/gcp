(ns gcp.bigquery.v2.TableId
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery TableId]))

(defn ^TableId from-edn
  [{:keys [dataset table project] :as arg}]
  (global/strict! :gcp.bigquery.v2/TableId arg)
  (if project
    (TableId/of project dataset table)
    (TableId/of dataset table)))

(defn get-iam-resource-name [arg]
  (if (instance? TableId arg)
    (.getIAMResourceName arg)
    (.getIAMResourceName (from-edn arg))))

(defn to-edn [^TableId arg]
  (cond-> {:dataset (.getDataset arg)
           :table   (.getTable arg)}
          (.getProject arg)
          (assoc :project (.getProject arg))))

(def schemas
  {:gcp.bigquery.v2/TableId
   [:map
    [:project {:optional true} [:string {:min 1}]]
    [:dataset [:string {:min 1}]]
    [:table [:string {:min 1}]]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))