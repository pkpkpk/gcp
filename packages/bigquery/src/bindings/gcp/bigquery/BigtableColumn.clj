;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.BigtableColumn
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.BigtableColumn"
   :gcp.dev/certification
     {:base-seed 1779204628909
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1779204628909 :standard 1779204628910 :stress 1779204628911}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-05-19T15:30:29.656390073Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery BigtableColumn BigtableColumn$Builder]))

(declare from-edn to-edn)

(defn ^BigtableColumn from-edn
  [arg]
  (global/strict! :gcp.bigquery/BigtableColumn arg)
  (let [builder (BigtableColumn/newBuilder)]
    (when (some? (get arg :encoding))
      (.setEncoding builder (get arg :encoding)))
    (when (some? (get arg :fieldName))
      (.setFieldName builder (get arg :fieldName)))
    (when (some? (get arg :onlyReadLatest))
      (.setOnlyReadLatest builder (get arg :onlyReadLatest)))
    (when (some? (get arg :qualifierEncoded))
      (.setQualifierEncoded builder (get arg :qualifierEncoded)))
    (when (some? (get arg :type)) (.setType builder (get arg :type)))
    (.build builder)))

(defn to-edn
  [^BigtableColumn arg]
  {:post [(global/strict! :gcp.bigquery/BigtableColumn %)]}
  (when arg
    (cond-> {}
      (some->> (.getEncoding arg)
               (not= ""))
        (assoc :encoding (.getEncoding arg))
      (some->> (.getFieldName arg)
               (not= ""))
        (assoc :fieldName (.getFieldName arg))
      (.getOnlyReadLatest arg) (assoc :onlyReadLatest (.getOnlyReadLatest arg))
      (some->> (.getQualifierEncoded arg)
               (not= ""))
        (assoc :qualifierEncoded (.getQualifierEncoded arg))
      (some->> (.getType arg)
               (not= ""))
        (assoc :type (.getType arg)))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/BigtableColumn}
   [:encoding
    {:optional true,
     :setter-doc
       "The encoding of the values when the type is not STRING. Acceptable encoding values are: TEXT\n- indicates values are alphanumeric text strings. BINARY - indicates values are encoded using\nHBase Bytes.toBytes family of functions.\n\n<p>Encoding can also be set at the column family level. However, the setting at the column\nlevel takes precedence if 'encoding' is set at both levels."}
    [:string {:min 1, :gen/max 1}]]
   [:fieldName
    {:optional true,
     :setter-doc
       "If the qualifier is not a valid BigQuery field identifier, a valid identifier must be\nprovided as the column field name and is used as field name in queries."}
    [:string {:min 1, :gen/max 1}]]
   [:onlyReadLatest
    {:optional true,
     :setter-doc
       "If this is set, only the latest version of value in this column are exposed.\n\n<p>'onlyReadLatest' can also be set at the column family level. However, the setting at the\ncolumn level takes precedence if 'onlyReadLatest' is set at both levels."}
    :boolean]
   [:qualifierEncoded
    {:optional true,
     :setter-doc
       "Qualifier of the column.\n\n<p>Columns in the parent column family that has this exact qualifier are exposed as . field.\nIf the qualifier is valid UTF-8 string, it can be specified in the qualifier_string field.\nOtherwise, a base-64 encoded value must be set to qualifier_encoded. The column field name is\nthe same as the column qualifier. However, if the qualifier is not a valid BigQuery field\nidentifier, a valid identifier must be provided as field_name."}
    [:string {:min 1, :gen/max 1}]]
   [:type
    {:optional true,
     :setter-doc
       "The type to convert the value in cells of this column.\n\n<p>The values are expected to be encoded using HBase Bytes.toBytes function when using the\nBINARY encoding value. Following BigQuery types are allowed (case-sensitive): BYTES STRING\nINTEGER FLOAT BOOLEAN Default type is BYTES.\n\n<p>'type' can also be set at the column family level. However, the setting at the column\nlevel takes precedence if 'type' is set at both levels."}
    [:string {:min 1, :gen/max 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/BigtableColumn schema}
    {:gcp.global/name "gcp.bigquery.BigtableColumn"}))