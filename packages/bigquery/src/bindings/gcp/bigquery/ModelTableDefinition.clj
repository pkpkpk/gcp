;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.ModelTableDefinition
  {:doc
     "A Google BigQuery Model table definition. This definition is used to represent a BigQuery ML\nmodel.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/standard-sql/bigqueryml-syntax-create#models_in_bqml_name\">BigQuery\n    ML Model</a>"
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.ModelTableDefinition"
   :gcp.dev/certification
     {:base-seed 1775130895257
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1775130895257 :standard 1775130895258 :stress 1775130895259}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:54:57.329036348Z"}}
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