;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.UserDefinedFunction
  {:doc
     "Google BigQuery User Defined Function. BigQuery supports user-defined functions (UDFs) written in\nJavaScript. A UDF is similar to the \"Map\" function in a MapReduce: it takes a single row as input\nand produces zero or more rows as output. The output can potentially have a different schema than\nthe input.\n\n@see <a href=\"https://cloud.google.com/bigquery/user-defined-functions\">User-Defined Functions\n    </a>"
   :file-git-sha "36af39e222ccf992d15ddbf82148b98e377b40f3"
   :fqcn "com.google.cloud.bigquery.UserDefinedFunction"
   :gcp.dev/certification
     {:base-seed 1790034067743
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034067743 :standard 1790034067744 :stress 1790034067745}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:41:08.803967654Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery UserDefinedFunction
            UserDefinedFunction$InlineFunction UserDefinedFunction$Type
            UserDefinedFunction$UriFunction]))

(declare from-edn
         to-edn
         Type-from-edn
         Type-to-edn
         InlineFunction-from-edn
         InlineFunction-to-edn
         UriFunction-from-edn
         UriFunction-to-edn)

(def Type-schema
  [:enum
   {:closed true,
    :doc
      "Type of user-defined function. User defined functions can be provided inline as code blobs\n({@link #INLINE}) or as a Google Cloud Storage URI ({@link #FROM_URI}).",
    :gcp/category :nested/enum,
    :gcp/key :gcp.bigquery/UserDefinedFunction.Type} "INLINE" "FROM_URI"])

(defn ^UserDefinedFunction$InlineFunction InlineFunction-from-edn
  [arg]
  (throw
    (Exception.
      "Class com.google.cloud.bigquery.UserDefinedFunction.InlineFunction is read-only")))

(defn InlineFunction-to-edn
  [^UserDefinedFunction$InlineFunction arg]
  (when arg {:type "INLINE"}))

(def InlineFunction-schema
  [:map
   {:closed true,
    :doc "A Google Cloud BigQuery user-defined function, as a code blob.",
    :gcp/category :nested/variant-read-only,
    :gcp/key :gcp.bigquery/UserDefinedFunction.InlineFunction}
   [:type [:= "INLINE"]]])

(defn ^UserDefinedFunction$UriFunction UriFunction-from-edn
  [arg]
  (throw
    (Exception.
      "Class com.google.cloud.bigquery.UserDefinedFunction.UriFunction is read-only")))

(defn UriFunction-to-edn
  [^UserDefinedFunction$UriFunction arg]
  (when arg {:type "FROM_URI"}))

(def UriFunction-schema
  [:map
   {:closed true,
    :doc
      "A Google Cloud BigQuery user-defined function, as an URI to Google Cloud Storage.",
    :gcp/category :nested/variant-read-only,
    :gcp/key :gcp.bigquery/UserDefinedFunction.UriFunction}
   [:type [:= "FROM_URI"]]])

(defn ^UserDefinedFunction from-edn
  [arg]
  (global/strict! :gcp.bigquery/UserDefinedFunction arg)
  (case (get arg :type)
    "INLINE" (UserDefinedFunction/inline (get arg :content))
    "FROM_URI" (UserDefinedFunction/fromUri (get arg :content))))

(defn to-edn
  [^UserDefinedFunction arg]
  {:post [(global/strict! :gcp.bigquery/UserDefinedFunction %)]}
  (when arg {:type (.name (.getType arg)), :content (.getContent arg)}))

(def schema
  [:map
   {:closed true,
    :doc
      "Google BigQuery User Defined Function. BigQuery supports user-defined functions (UDFs) written in\nJavaScript. A UDF is similar to the \"Map\" function in a MapReduce: it takes a single row as input\nand produces zero or more rows as output. The output can potentially have a different schema than\nthe input.\n\n@see <a href=\"https://cloud.google.com/bigquery/user-defined-functions\">User-Defined Functions\n    </a>",
    :gcp/category :union-tagged,
    :gcp/key :gcp.bigquery/UserDefinedFunction}
   [:content
    {:doc
       "If {@link #getType()} is {@link Type#INLINE} this method returns a code blob. If {@link\n#getType()} is {@link Type#FROM_URI} the method returns a Google Cloud Storage URI (e.g.\ngs://bucket/path)."}
    [:string {:min 1, :gen/max 1}]]
   [:type {:doc "Returns the type of user defined function."}
    [:enum {:closed true} "INLINE" "FROM_URI"]]])

(global/include-registry!
  "gcp.bigquery.UserDefinedFunction"
  {:gcp.bigquery/UserDefinedFunction schema,
   :gcp.bigquery/UserDefinedFunction.InlineFunction InlineFunction-schema,
   :gcp.bigquery/UserDefinedFunction.Type Type-schema,
   :gcp.bigquery/UserDefinedFunction.UriFunction UriFunction-schema})