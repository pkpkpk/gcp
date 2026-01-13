(ns gcp.bigquery.custom.StandardSQL
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery StandardSQLDataType StandardSQLField StandardSQLStructType StandardSQLTableType StandardSQLTypeName)))

(declare DataType:from-edn DataType:to-edn)

(defn ^StandardSQLField Field:from-edn
  [{:keys [name dataType] :as arg}]
  (global/strict! :gcp.bigquery/StandardSQLField arg)
  (let [builder (StandardSQLField/newBuilder)]
    (when name
      (.setName builder name))
    (.setDataType builder (DataType:from-edn dataType))
    (.build builder)))

(defn Field:to-edn [^StandardSQLField arg]
  {:post [(global/strict! :gcp.bigquery/StandardSQLField %)]}
  (cond-> {:dataType (DataType:to-edn (.getDataType arg))}
          (some? (.getName arg))
          (assoc :name (.getName arg))))

(defn ^StandardSQLStructType StructType:from-edn
  [{:keys [fieldList] :as arg}]
  (global/strict! :gcp.bigquery/StandardSQLStructType arg)
  (let [builder (StandardSQLStructType/newBuilder)]
    (.setFields builder (map Field:from-edn fieldList))
    (.build builder)))

(defn StructType:to-edn [^StandardSQLStructType arg]
  {:post [(global/strict! :gcp.bigquery/StandardSQLStructType %)]}
  {:fieldList (map Field:to-edn (.getFields arg))})

(defn ^StandardSQLDataType DataType:from-edn
  [{:keys [typeName typeKind structType arrayElementType] :as arg}]
  (global/strict! :gcp.bigquery/StandardSQLDataType arg)
  (let [builder           (if typeKind
                            (StandardSQLDataType/newBuilder ^String typeKind)
                            (StandardSQLDataType/newBuilder ^StandardSQLTypeName (StandardSQLTypeName/valueOf typeName)))]
    (when structType
      (.setStructType builder (StructType:from-edn structType)))
    (some->> arrayElementType DataType:from-edn (.setArrayElementType builder))
    (.build builder)))

(defn DataType:to-edn [^StandardSQLDataType arg]
  {:post [(global/strict! :gcp.bigquery/StandardSQLDataType %)]}
  (cond-> {:typeKind (.getTypeKind arg)}
          (.getArrayElementType arg)
          (assoc :arrayElementType (DataType:to-edn (.getArrayElementType arg)))
          (.getStructType arg)
          (assoc :structType (StructType:to-edn (.getStructType arg)))))

(defn ^StandardSQLTableType TableType:from-edn
  [{:keys [columns] :as arg}]
  (global/strict! :gcp.bigquery/StandardSQLTableType arg)
  (let [builder (StandardSQLTableType/newBuilder)]
    (.setColumns builder (map Field:from-edn columns))
    (.build builder)))

(defn TableType:to-edn [^StandardSQLTableType arg]
  {:post [(global/strict! :gcp.bigquery/StandardSQLTableType %)]}
  {:columns (map Field:to-edn (.getColumns arg))})

(def schemas
  {:gcp.bigquery/StandardSQLDataType
   [:map {:closed true}
    [:typeKind {:optional true} :gcp.bigquery/StandardSQLTypeName]
    [:typeName {:optional true} :gcp.bigquery/StandardSQLTypeName]
    [:arrayElementType {:optional true} [:ref :gcp.bigquery/StandardSQLDataType]]
    [:structType {:optional true} [:ref :gcp.bigquery/StandardSQLStructType]]]

   :gcp.bigquery/StandardSQLField
   [:map {:closed true}
    [:name {:optional true} :string]
    [:dataType :gcp.bigquery/StandardSQLDataType]]

   :gcp.bigquery/StandardSQLStructType
   [:map {:closed true}
    [:fieldList [:sequential :gcp.bigquery/StandardSQLField]]]

   :gcp.bigquery/StandardSQLTableType
   [:map {:closed true}
    [:columns [:sequential :gcp.bigquery/StandardSQLField]]]

   :gcp.bigquery/StandardSQLTypeName
   [:enum "TIME" "FLOAT64" "INTERVAL" "BIGNUMERIC" "BOOL" "DATE" "BYTES" "GEOGRAPHY" "NUMERIC" "ARRAY" "JSON" "STRING" "DATETIME" "STRUCT" "TIMESTAMP" "INT64" "RANGE"]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))