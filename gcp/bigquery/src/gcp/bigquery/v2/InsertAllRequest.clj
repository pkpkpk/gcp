(ns gcp.bigquery.v2.InsertAllRequest
  (:require [clojure.walk :as walk]
            [gcp.bigquery.v2.TableId :as TableId]
            [gcp.global :as g])
  (:import (com.google.cloud.bigquery InsertAllRequest InsertAllRequest$RowToInsert)
           (java.util HashMap)))

(defn serialize-value [val]
  (if (sequential? val)
    (if (empty? val)
      (into-array Object val)
      (cond
        (g/valid? [:seqable :int] val)
        (into-array Long val)

        (g/valid? [:seqable :string] val)
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
  (g/strict! :gcp/bigquery.InsertAllRequest arg)
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