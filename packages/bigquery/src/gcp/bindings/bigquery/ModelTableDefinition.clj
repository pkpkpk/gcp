;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.ModelTableDefinition
  {:doc
     "A Google BigQuery Model table definition. This definition is used to represent a BigQuery ML\nmodel.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/standard-sql/bigqueryml-syntax-create#models_in_bqml_name\">BigQuery\n    ML Model</a>"
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.ModelTableDefinition"
   :gcp.dev/certification
     {:base-seed 1771346691377
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771346691377 :standard 1771346691378 :stress 1771346691379}
      :protocol-hash
        "32175171656ac95c72455bff25fe019346fb52c916efee805be149692e31cec4"
      :timestamp "2026-02-17T16:44:59.252730397Z"}}
  (:require [gcp.bindings.bigquery.Schema :as Schema]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery ModelTableDefinition
            ModelTableDefinition$Builder]))

(defn ^ModelTableDefinition from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/ModelTableDefinition arg)
  (let [builder (ModelTableDefinition/newBuilder)]
    (when (some? (get arg :location))
      (.setLocation builder (get arg :location)))
    (when (some? (get arg :numBytes))
      (.setNumBytes builder (get arg :numBytes)))
    (.build builder)))

(defn to-edn
  [^ModelTableDefinition arg]
  {:post [(global/strict! :gcp.bindings.bigquery/ModelTableDefinition %)]}
  (cond-> {:type "MODEL"}
    (.getLocation arg) (assoc :location (.getLocation arg))
    (.getNumBytes arg) (assoc :numBytes (.getNumBytes arg))
    (.getSchema arg) (assoc :schema (Schema/to-edn (.getSchema arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "A Google BigQuery Model table definition. This definition is used to represent a BigQuery ML\nmodel.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/standard-sql/bigqueryml-syntax-create#models_in_bqml_name\">BigQuery\n    ML Model</a>",
    :gcp/category :variant-accessor,
    :gcp/key :gcp.bindings.bigquery/ModelTableDefinition} [:type [:= "MODEL"]]
   [:numBytes
    {:optional true,
     :getter-doc
       "Returns the size of this table in bytes, excluding any data in the streaming buffer."}
    :int]
   [:location
    {:optional true,
     :getter-doc
       "Returns the geographic location where the table should reside. This value is inherited from the\ndataset.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/managing_jobs_datasets_projects#dataset-location\">\n    Dataset Location</a>"}
    [:string {:min 1}]]
   [:schema
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the table's schema."} :gcp.bindings.bigquery/Schema]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/ModelTableDefinition schema}
    {:gcp.global/name "gcp.bindings.bigquery.ModelTableDefinition"}))