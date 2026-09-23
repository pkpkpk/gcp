;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.ModelTableDefinition
  {:doc
     "A Google BigQuery Model table definition. This definition is used to represent a BigQuery ML\nmodel.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/standard-sql/bigqueryml-syntax-create#models_in_bqml_name\">BigQuery\n    ML Model</a>"
   :file-git-sha "36af39e222ccf992d15ddbf82148b98e377b40f3"
   :fqcn "com.google.cloud.bigquery.ModelTableDefinition"
   :gcp.dev/certification
     {:base-seed 1790034091010
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034091010 :standard 1790034091011 :stress 1790034091012}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:41:32.169829410Z"}}
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
    [:string {:min 1, :gen/max 1}]]
   [:numBytes
    {:optional true,
     :getter-doc
       "Returns the size of this table in bytes, excluding any data in the streaming buffer."}
    :i64]
   [:schema
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the table's schema."} :gcp.bigquery/Schema]])

(global/include-registry! "gcp.bigquery.ModelTableDefinition"
                          {:gcp.bigquery/ModelTableDefinition schema})