;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.ViewDefinition
  {:doc
     "Google BigQuery view table definition. BigQuery's views are logical views, not materialized\nviews, which means that the query that defines the view is re-executed every time the view is\nqueried.\n\n@see <a href=\"https://cloud.google.com/bigquery/querying-data#views\">Views</a>"
   :file-git-sha "36af39e222ccf992d15ddbf82148b98e377b40f3"
   :fqcn "com.google.cloud.bigquery.ViewDefinition"
   :gcp.dev/certification
     {:base-seed 1790034099684
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034099684 :standard 1790034099685 :stress 1790034099686}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:41:41.063965536Z"}}
  (:require [gcp.bigquery.Schema :as Schema]
            [gcp.bigquery.UserDefinedFunction :as UserDefinedFunction]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery ViewDefinition ViewDefinition$Builder]))

(declare from-edn to-edn)

(defn ^ViewDefinition from-edn
  [arg]
  (global/strict! :gcp.bigquery/ViewDefinition arg)
  (let [builder (ViewDefinition/newBuilder (get arg :query))]
    (when (some? (get arg :schema))
      (.setSchema builder (Schema/from-edn (get arg :schema))))
    (when (some? (get arg :useLegacySql))
      (.setUseLegacySql builder (get arg :useLegacySql)))
    (when (some? (get arg :userDefinedFunctions))
      (.setUserDefinedFunctions builder
                                (mapv UserDefinedFunction/from-edn
                                  (get arg :userDefinedFunctions))))
    (.build builder)))

(defn to-edn
  [^ViewDefinition arg]
  {:post [(global/strict! :gcp.bigquery/ViewDefinition %)]}
  (when arg
    (cond-> {:query (.getQuery arg), :type "VIEW"}
      (.getSchema arg) (assoc :schema (Schema/to-edn (.getSchema arg)))
      (.useLegacySql arg) (assoc :useLegacySql (.useLegacySql arg))
      (seq (.getUserDefinedFunctions arg))
        (assoc :userDefinedFunctions
          (mapv UserDefinedFunction/to-edn (.getUserDefinedFunctions arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "Google BigQuery view table definition. BigQuery's views are logical views, not materialized\nviews, which means that the query that defines the view is re-executed every time the view is\nqueried.\n\n@see <a href=\"https://cloud.google.com/bigquery/querying-data#views\">Views</a>",
    :gcp/category :variant-accessor,
    :gcp/key :gcp.bigquery/ViewDefinition} [:type [:= "VIEW"]]
   [:query {:getter-doc "Returns the query used to create the view."}
    [:string {:min 1, :gen/max 1}]]
   [:schema
    {:optional true,
     :getter-doc "Returns the table's schema.",
     :setter-doc "Sets the table schema."} :gcp.bigquery/Schema]
   [:useLegacySql
    {:optional true,
     :getter-doc
       "Returns whether to use BigQuery's legacy SQL dialect for this query. By default this property\nis set to {@code false}. If set to {@code false}, the query will use BigQuery's <a\nhref=\"https://cloud.google.com/bigquery/sql-reference/\">Standard SQL</a>. If set to {@code\nnull} or {@code true}, legacy SQL dialect is used. This property is experimental and might be\nsubject to change.",
     :setter-doc
       "Sets whether to use BigQuery's legacy SQL dialect for this query. By default this property is\nset to {@code false}. If set to {@code false}, the query will use BigQuery's <a\nhref=\"https://cloud.google.com/bigquery/sql-reference/\">Standard SQL</a>.\n\n<p>If set to {@code null} or {@code true}, legacy SQL dialect is used. This property is\nexperimental and might be subject to change."}
    :boolean]
   [:userDefinedFunctions
    {:optional true,
     :getter-doc
       "Returns user defined functions that can be used by {@link #getQuery()}. Returns {@code null} if\nnot set.\n\n@see <a href=\"https://cloud.google.com/bigquery/user-defined-functions\">User-Defined Functions\n    </a>",
     :setter-doc
       "Sets user defined functions that can be used by {@link #getQuery()}.\n\n@see <a href=\"https://cloud.google.com/bigquery/user-defined-functions\">User-Defined\n    Functions</a>"}
    [:sequential {:min 1, :gen/max 2} :gcp.bigquery/UserDefinedFunction]]])

(global/include-registry! "gcp.bigquery.ViewDefinition"
                          {:gcp.bigquery/ViewDefinition schema})