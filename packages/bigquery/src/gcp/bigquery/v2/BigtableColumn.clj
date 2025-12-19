(ns gcp.bigquery.v2.BigtableColumn
  (:import [com.google.cloud.bigquery BigtableColumn])
  (:require [gcp.global :as global]))

(defn ^BigtableColumn from-edn
  [arg]
  (global/strict! :gcp.bigquery.v2/BigtableColumn arg)
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
  {:post [(global/strict! :gcp.bigquery.v2/BigtableColumn %)]}
  (cond-> {}
    (get arg :encoding) (assoc :encoding (.getEncoding arg))
    (get arg :fieldName) (assoc :fieldName (.getFieldName arg))
    (get arg :onlyReadLatest) (assoc :onlyReadLatest (.getOnlyReadLatest arg))
    (get arg :qualifierEncoded) (assoc :qualifierEncoded
                                  (.getQualifierEncoded arg))
    (get arg :type) (assoc :type (.getType arg))))

(def schemas
  {:gcp.bigquery.v2/BigtableColumn
   [:map {:closed true}
    [:encoding {:optional true} :string]
    [:fieldName {:optional true} :string]
    [:onlyReadLatest {:optional true} :boolean]
    [:qualifierEncoded {:optional true} :string]
    [:type {:optional true} :string]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))