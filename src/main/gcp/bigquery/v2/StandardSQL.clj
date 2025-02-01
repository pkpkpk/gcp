(ns gcp.bigquery.v2.StandardSQL
  (:require [gcp.global :as g])
  (:import (com.google.cloud.bigquery StandardSQLDataType StandardSQLField StandardSQLStructType StandardSQLTableType StandardSQLTypeName)))

;; TODO gcp.bigquery.v2.QueryParameterValue
;;      gcp.bigquery.v2.Field

(declare DataType-from-edn DataType-to-edn)

(defn ^StandardSQLField Field-from-edn
  [{:keys [name dataType] :as arg}]
  (g/strict! :gcp/bigquery.StandardSQLField arg)
  (let [builder (StandardSQLField/newBuilder)]
    (when name
      (.setName builder name))
    (.setDataType builder (DataType-from-edn dataType))
    (.build builder)))

(defn Field-to-edn [^StandardSQLField arg]
  {:post [(g/strict! :gcp/bigquery.StandardSQLField %)]}
  (cond-> {:dataType (DataType-to-edn (.getDataType arg))}
          (some? (.getName arg))
          (assoc :name (.getName arg))))

(defn ^StandardSQLStructType StructType-from-edn
  [{:keys [fieldList] :as arg}]
  (g/strict! :gcp/bigquery.StandardSQLStructType arg)
  (let [builder (StandardSQLStructType/newBuilder)]
    (.setFields builder (map Field-from-edn fieldList))
    (.build builder)))

(defn StructType-to-edn [^StandardSQLStructType arg]
  {:post [(g/strict! :gcp/bigquery.StandardSQLStructType %)]}
  {:fieldList (map Field-to-edn (.getFields arg))})

(defn ^StandardSQLDataType DataType-from-edn
  [{:keys [typeName typeKind structType arrayElementType] :as arg}]
  (g/strict! :gcp/bigquery.StandardSQLDataType arg)
  (let [builder           (if typeKind
                            (StandardSQLDataType/newBuilder ^String typeKind)
                            (StandardSQLDataType/newBuilder ^StandardSQLTypeName (StandardSQLTypeName/valueOf typeName)))]
    (when structType
      (.setStructType builder (StructType-from-edn structType)))
    (some->> arrayElementType DataType-from-edn (.setArrayElementType builder))
    (.build builder)))

(defn DataType-to-edn [^StandardSQLDataType arg]
  {:post [(g/strict! :gcp/bigquery.StandardSQLDataType %)]}
  (cond-> {:typeKind (.getTypeKind arg)}
          (.getArrayElementType arg)
          (assoc :arrayElementType (DataType-to-edn (.getArrayElementType arg)))
          (.getStructType arg)
          (assoc :structType (StructType-to-edn (.getStructType arg)))))

(defn ^StandardSQLTableType TableType-from-edn
  [{:keys [columns] :as arg}]
  (g/strict! :gcp/bigquery.StandardSQLTableType arg)
  (let [builder (StandardSQLTableType/newBuilder)]
    (.setColumns builder (map Field-from-edn columns))
    (.build builder)))

(defn TableType-to-edn [^StandardSQLTableType arg]
  {:post [(g/strict! :gcp/bigquery.StandardSQLTableType %)]}
  {:columns (map Field-to-edn (.getColumns arg))})