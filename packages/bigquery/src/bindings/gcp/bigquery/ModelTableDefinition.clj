;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.ModelTableDefinition
  {:doc
     "A Google BigQuery Model table definition. This definition is used to represent a BigQuery ML\nmodel.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/standard-sql/bigqueryml-syntax-create#models_in_bqml_name\">BigQuery\n    ML Model</a>"
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.ModelTableDefinition"
   :gcp.dev/certification
     {:base-seed 1776499387968
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1776499387968 :standard 1776499387969 :stress 1776499387970}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:03:09.427139226Z"}}
  (:require [gcp.bigquery.Schema :as Schema]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery ModelTableDefinition
            ModelTableDefinition$Builder]))

(declare from-edn to-edn)

(defn ^ModelTableDefinition from-edn
  [arg]
  (global/strict! :gcp.bigquery/ModelTableDefinition arg)
  (let [builder (ModelTableDefinition/newBuilder)]
    (when (some? (get arg :location))
      (.setLocation builder (get arg :location)))
    (when (some? (get arg :numBytes))
      (.setNumBytes builder (long (get arg :numBytes))))
    (.build builder)))

(defn to-edn
  [^ModelTableDefinition arg]
  {:post [(global/strict! :gcp.bigquery/ModelTableDefinition %)]}
  (when arg
    (cond-> {:type "MODEL"}
      (some->> (.getLocation arg)
               (not= ""))
        (assoc :location (.getLocation arg))
      (.getNumBytes arg) (assoc :numBytes (.getNumBytes arg))
      (.getSchema arg) (assoc :schema (Schema/to-edn (.getSchema arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "A Google BigQuery Model table definition. This definition is used to represent a BigQuery ML\nmodel.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/standard-sql/bigqueryml-syntax-create#models_in_bqml_name\">BigQuery\n    ML Model</a>",
    :gcp/category :variant-accessor,
    :gcp/key :gcp.bigquery/ModelTableDefinition} [:type [:= "MODEL"]]
   [:location
    {:optional true,
     :getter-doc
       "Returns the geographic location where the table should reside. This value is inherited from the\ndataset.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/managing_jobs_datasets_projects#dataset-location\">\n    Dataset Location</a>"}
    [:string {:min 1}]]
   [:numBytes
    {:optional true,
     :getter-doc
       "Returns the size of this table in bytes, excluding any data in the streaming buffer."}
    :i64]
   [:schema
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the table's schema."} :gcp.bigquery/Schema]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/ModelTableDefinition schema}
    {:gcp.global/name "gcp.bigquery.ModelTableDefinition"}))