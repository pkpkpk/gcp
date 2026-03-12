;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.UserDefinedFunction
  {:doc
     "Google BigQuery User Defined Function. BigQuery supports user-defined functions (UDFs) written in\nJavaScript. A UDF is similar to the \"Map\" function in a MapReduce: it takes a single row as input\nand produces zero or more rows as output. The output can potentially have a different schema than\nthe input.\n\n@see <a href=\"https://cloud.google.com/bigquery/user-defined-functions\">User-Defined Functions\n    </a>"
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.UserDefinedFunction"
   :gcp.dev/certification
     {:base-seed 1770933182701
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1770933182701 :standard 1770933182702 :stress 1770933182703}
      :protocol-hash
        "5e9ef6ce3c61f1705b38fa182cc7b09ba0fe1c94b8093ef6ce8d61bff196dbcb"
      :timestamp "2026-02-12T21:53:02.825266788Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery UserDefinedFunction
            UserDefinedFunction$Type]))

(declare UserDefinedFunction$Type-from-edn UserDefinedFunction$Type-to-edn)

(defn ^UserDefinedFunction$Type UserDefinedFunction$Type-from-edn
  [arg]
  (UserDefinedFunction$Type/valueOf arg))

(defn UserDefinedFunction$Type-to-edn
  [^UserDefinedFunction$Type arg]
  (.name arg))

(def UserDefinedFunction$Type-schema
  [:enum
   {:closed true,
    :doc
      "Type of user-defined function. User defined functions can be provided inline as code blobs\n({@link #INLINE}) or as a Google Cloud Storage URI ({@link #FROM_URI}).",
    :gcp/category :nested/enum,
    :gcp/key :gcp.bindings.bigquery/UserDefinedFunction.Type} "INLINE"
   "FROM_URI"])

(defn ^UserDefinedFunction from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/UserDefinedFunction arg)
  (case (get arg :type)
    "INLINE" (UserDefinedFunction/inline (get arg :content))
    "FROM_URI" (UserDefinedFunction/fromUri (get arg :content))))

(defn to-edn
  [^UserDefinedFunction arg]
  {:post [(global/strict! :gcp.bindings.bigquery/UserDefinedFunction %)]}
  {:type (.name (.getType arg)), :content (.getContent arg)})

(def schema
  [:map
   {:closed true,
    :doc
      "Google BigQuery User Defined Function. BigQuery supports user-defined functions (UDFs) written in\nJavaScript. A UDF is similar to the \"Map\" function in a MapReduce: it takes a single row as input\nand produces zero or more rows as output. The output can potentially have a different schema than\nthe input.\n\n@see <a href=\"https://cloud.google.com/bigquery/user-defined-functions\">User-Defined Functions\n    </a>",
    :gcp/category :union-tagged,
    :gcp/key :gcp.bindings.bigquery/UserDefinedFunction}
   [:content
    {:doc
       "If {@link #getType()} is {@link Type#INLINE} this method returns a code blob. If {@link\n#getType()} is {@link Type#FROM_URI} the method returns a Google Cloud Storage URI (e.g.\ngs://bucket/path)."}
    :string]
   [:type {:doc "Returns the type of user defined function."}
    :gcp.bindings.bigquery/UserDefinedFunction.Type]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/UserDefinedFunction schema,
              :gcp.bindings.bigquery/UserDefinedFunction.Type
                UserDefinedFunction$Type-schema}
    {:gcp.global/name "gcp.bindings.bigquery.UserDefinedFunction"}))