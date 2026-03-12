;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.ViewDefinition
  {:doc
     "Google BigQuery view table definition. BigQuery's views are logical views, not materialized\nviews, which means that the query that defines the view is re-executed every time the view is\nqueried.\n\n@see <a href=\"https://cloud.google.com/bigquery/querying-data#views\">Views</a>"
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.ViewDefinition"
   :gcp.dev/certification
     {:base-seed 1771258324915
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771258324915 :standard 1771258324916 :stress 1771258324917}
      :protocol-hash
        "7068af39aa0d55cb4d0e4eaceead6fd12f374863b361a9717f08a69d4bd12910"
      :timestamp "2026-02-16T16:12:19.484700170Z"}}
  (:require [gcp.bindings.bigquery.Schema :as Schema]
            [gcp.bindings.bigquery.UserDefinedFunction :as UserDefinedFunction]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery ViewDefinition ViewDefinition$Builder]))

(defn ^ViewDefinition from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/ViewDefinition arg)
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
  {:post [(global/strict! :gcp.bindings.bigquery/ViewDefinition %)]}
  (cond-> {:query (.getQuery arg), :type "VIEW"}
    (.getSchema arg) (assoc :schema (Schema/to-edn (.getSchema arg)))
    (.useLegacySql arg) (assoc :useLegacySql (.useLegacySql arg))
    (.getUserDefinedFunctions arg) (assoc :userDefinedFunctions
                                     (map UserDefinedFunction/to-edn
                                       (.getUserDefinedFunctions arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Google BigQuery view table definition. BigQuery's views are logical views, not materialized\nviews, which means that the query that defines the view is re-executed every time the view is\nqueried.\n\n@see <a href=\"https://cloud.google.com/bigquery/querying-data#views\">Views</a>",
    :gcp/category :variant-accessor,
    :gcp/key :gcp.bindings.bigquery/ViewDefinition} [:type [:= "VIEW"]]
   [:query {:getter-doc "Returns the query used to create the view."} :string]
   [:schema
    {:optional true,
     :getter-doc "Returns the table's schema.",
     :setter-doc "Sets the table schema."} :gcp.bindings.bigquery/Schema]
   [:userDefinedFunctions
    {:optional true,
     :getter-doc
       "Returns user defined functions that can be used by {@link #getQuery()}. Returns {@code null} if\nnot set.\n\n@see <a href=\"https://cloud.google.com/bigquery/user-defined-functions\">User-Defined Functions\n    </a>",
     :setter-doc
       "Sets user defined functions that can be used by {@link #getQuery()}.\n\n@see <a href=\"https://cloud.google.com/bigquery/user-defined-functions\">User-Defined\n    Functions</a>"}
    [:seqable {:min 1} :gcp.bindings.bigquery/UserDefinedFunction]]
   [:useLegacySql
    {:optional true,
     :getter-doc
       "Returns whether to use BigQuery's legacy SQL dialect for this query. By default this property\nis set to {@code false}. If set to {@code false}, the query will use BigQuery's <a\nhref=\"https://cloud.google.com/bigquery/sql-reference/\">Standard SQL</a>. If set to {@code\nnull} or {@code true}, legacy SQL dialect is used. This property is experimental and might be\nsubject to change.",
     :setter-doc
       "Sets whether to use BigQuery's legacy SQL dialect for this query. By default this property is\nset to {@code false}. If set to {@code false}, the query will use BigQuery's <a\nhref=\"https://cloud.google.com/bigquery/sql-reference/\">Standard SQL</a>.\n\n<p>If set to {@code null} or {@code true}, legacy SQL dialect is used. This property is\nexperimental and might be subject to change."}
    :boolean]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/ViewDefinition schema}
    {:gcp.global/name "gcp.bindings.bigquery.ViewDefinition"}))