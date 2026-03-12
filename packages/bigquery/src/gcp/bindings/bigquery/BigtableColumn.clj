;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.BigtableColumn
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.BigtableColumn"
   :gcp.dev/certification
     {:base-seed 1771013562838
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771013562838 :standard 1771013562839 :stress 1771013562840}
      :protocol-hash
        "600a262ece6bd21dc98250ea6f25d9fa1a7ab0d8840c5d6ce9608615488fe05f"
      :timestamp "2026-02-13T20:12:42.961792476Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery BigtableColumn BigtableColumn$Builder]))

(defn ^BigtableColumn from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/BigtableColumn arg)
  (let [builder (BigtableColumn/newBuilder)]
    (when (get arg :encoding) (.setEncoding builder (get arg :encoding)))
    (when (get arg :fieldName) (.setFieldName builder (get arg :fieldName)))
    (when (get arg :onlyReadLatest)
      (.setOnlyReadLatest builder (get arg :onlyReadLatest)))
    (when (get arg :qualifierEncoded)
      (.setQualifierEncoded builder (get arg :qualifierEncoded)))
    (when (get arg :type) (.setType builder (get arg :type)))
    (.build builder)))

(defn to-edn
  [^BigtableColumn arg]
  {:post [(global/strict! :gcp.bindings.bigquery/BigtableColumn %)]}
  (cond-> {}
    (.getEncoding arg) (assoc :encoding (.getEncoding arg))
    (.getFieldName arg) (assoc :fieldName (.getFieldName arg))
    (.getOnlyReadLatest arg) (assoc :onlyReadLatest (.getOnlyReadLatest arg))
    (.getQualifierEncoded arg) (assoc :qualifierEncoded
                                 (.getQualifierEncoded arg))
    (.getType arg) (assoc :type (.getType arg))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/BigtableColumn}
   [:encoding
    {:optional true,
     :setter-doc
       "The encoding of the values when the type is not STRING. Acceptable encoding values are: TEXT\n- indicates values are alphanumeric text strings. BINARY - indicates values are encoded using\nHBase Bytes.toBytes family of functions.\n\n<p>Encoding can also be set at the column family level. However, the setting at the column\nlevel takes precedence if 'encoding' is set at both levels."}
    :string]
   [:fieldName
    {:optional true,
     :setter-doc
       "If the qualifier is not a valid BigQuery field identifier, a valid identifier must be\nprovided as the column field name and is used as field name in queries."}
    :string]
   [:onlyReadLatest
    {:optional true,
     :setter-doc
       "If this is set, only the latest version of value in this column are exposed.\n\n<p>'onlyReadLatest' can also be set at the column family level. However, the setting at the\ncolumn level takes precedence if 'onlyReadLatest' is set at both levels."}
    :boolean]
   [:qualifierEncoded
    {:optional true,
     :setter-doc
       "Qualifier of the column.\n\n<p>Columns in the parent column family that has this exact qualifier are exposed as . field.\nIf the qualifier is valid UTF-8 string, it can be specified in the qualifier_string field.\nOtherwise, a base-64 encoded value must be set to qualifier_encoded. The column field name is\nthe same as the column qualifier. However, if the qualifier is not a valid BigQuery field\nidentifier, a valid identifier must be provided as field_name."}
    :string]
   [:type
    {:optional true,
     :setter-doc
       "The type to convert the value in cells of this column.\n\n<p>The values are expected to be encoded using HBase Bytes.toBytes function when using the\nBINARY encoding value. Following BigQuery types are allowed (case-sensitive): BYTES STRING\nINTEGER FLOAT BOOLEAN Default type is BYTES.\n\n<p>'type' can also be set at the column family level. However, the setting at the column\nlevel takes precedence if 'type' is set at both levels."}
    :string]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/BigtableColumn schema}
    {:gcp.global/name "gcp.bindings.bigquery.BigtableColumn"}))