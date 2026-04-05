;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.ViewDefinition
  {:doc
     "Google BigQuery view table definition. BigQuery's views are logical views, not materialized\nviews, which means that the query that defines the view is re-executed every time the view is\nqueried.\n\n@see <a href=\"https://cloud.google.com/bigquery/querying-data#views\">Views</a>"
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.ViewDefinition"
   :gcp.dev/certification
     {:base-seed 1775130907907
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1775130907907 :standard 1775130907908 :stress 1775130907909}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:55:09.769429671Z"}}
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
                                (map UserDefinedFunction/from-edn
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
          (map UserDefinedFunction/to-edn (.getUserDefinedFunctions arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "Google BigQuery view table definition. BigQuery's views are logical views, not materialized\nviews, which means that the query that defines the view is re-executed every time the view is\nqueried.\n\n@see <a href=\"https://cloud.google.com/bigquery/querying-data#views\">Views</a>",
    :gcp/category :variant-accessor,
    :gcp/key :gcp.bigquery/ViewDefinition} [:type [:= "VIEW"]]
   [:query {:getter-doc "Returns the query used to create the view."}
    [:string {:min 1}]]
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
    [:sequential {:min 1} :gcp.bigquery/UserDefinedFunction]]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/ViewDefinition schema}
    {:gcp.global/name "gcp.bigquery.ViewDefinition"}))