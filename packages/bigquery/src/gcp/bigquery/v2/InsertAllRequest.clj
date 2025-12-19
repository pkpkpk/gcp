(ns gcp.bigquery.v2.InsertAllRequest
  (:require [clojure.walk :as walk]
            [gcp.bigquery.v2.TableId :as TableId]
            [gcp.global :as global])
  (:import (com.google.cloud.bigquery InsertAllRequest InsertAllRequest$RowToInsert)
           (java.util HashMap)))

(defn serialize-value [val]
  (if (sequential? val)
    (if (empty? val)
      (into-array Object val)
      (cond
        (global/valid? [:seqable :int] val)
        (into-array Long val)

        (global/valid? [:seqable :string] val)
        (into-array String val)

        :else
        (into-array Object (map serialize-value val))))
    (if (map? val)
      (into {}
            (map
              (fn [[k v]]
                [(name k) (serialize-value v)]))
            val)
      (str val))))

(defn ^HashMap serialize-row [row]
  (into {}
        (map
          (fn [[k v]]
            [k (serialize-value v)]))
        (walk/stringify-keys row)))

(defn ^InsertAllRequest from-edn
  [{:keys [table
           ignoreUnknownValues
           skipInvalidRows
           templateSuffix
           rows] :as arg}]
  (global/strict! :gcp.bigquery.v2/InsertAllRequest arg)
  (let [builder (InsertAllRequest/newBuilder (TableId/from-edn table))]
    (doseq [row rows]
      (.addRow builder (serialize-row row)))
    (when skipInvalidRows
      (.setSkipInvalidRows builder skipInvalidRows))
    (when templateSuffix
      (.setTemplateSuffix builder templateSuffix))
    (when ignoreUnknownValues
      (.setIgnoreUnknownValues builder ignoreUnknownValues))
    (.build builder)))

(defn to-edn [^InsertAllRequest arg]
  (throw (ex-info "unimplemented" {:arg arg})))

(def schemas
  {:gcp.bigquery.v2/InsertAllRequest
   [:map
    [:table :gcp.bigquery.v2/TableId]
    [:rows [:seqable :map]]
    [:ignoreUnknownValues {:optional true} :boolean]
    [:skipInvalidRows {:optional true} :boolean]
    [:templateSuffix {:optional true} :string]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))