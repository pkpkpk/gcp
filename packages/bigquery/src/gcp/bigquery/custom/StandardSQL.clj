(ns gcp.bigquery.custom.StandardSQL
  (:require [gcp.global :as g])
  (:import (com.google.cloud.bigquery StandardSQLDataType StandardSQLField StandardSQLStructType StandardSQLTableType StandardSQLTypeName)))

(declare StandardSQLDataType-from-edn StandardSQLDataType-to-edn)

(defn ^StandardSQLField StandardSQLField-from-edn
  [{:keys [name dataType] :as arg}]
  (g/strict! ::StandardSQLField arg)
  (let [builder (StandardSQLField/newBuilder)]
    (when name
      (.setName builder name))
    (.setDataType builder (StandardSQLDataType-from-edn dataType))
    (.build builder)))

(defn StandardSQLField-to-edn [^StandardSQLField arg]
  {:post [(g/strict! ::StandardSQLField %)]}
  (cond-> {:dataType (StandardSQLDataType-to-edn (.getDataType arg))}
          (some? (.getName arg))
          (assoc :name (.getName arg))))

(defn ^StandardSQLStructType StandardSQLStructType-from-edn
  [{:keys [fieldList] :as arg}]
  (g/strict! ::StandardSQLStructType arg)
  (let [builder (StandardSQLStructType/newBuilder)]
    (.setFields builder (map StandardSQLField-from-edn fieldList))
    (.build builder)))

(defn StandardSQLStructType-to-edn [^StandardSQLStructType arg]
  {:post [(g/strict! ::StandardSQLStructType %)]}
  {:fieldList (map StandardSQLField-to-edn (.getFields arg))})

(defn ^StandardSQLDataType StandardSQLDataType-from-edn
  [{:keys [typeName typeKind structType arrayElementType] :as arg}]
  (g/strict! ::StandardSQLDataType arg)
  (let [builder           (if typeKind
                            (StandardSQLDataType/newBuilder ^String typeKind)
                            (StandardSQLDataType/newBuilder ^StandardSQLTypeName (StandardSQLTypeName/valueOf typeName)))]
    (when structType
      (.setStructType builder (StandardSQLStructType-from-edn structType)))
    (some->> arrayElementType StandardSQLDataType-from-edn (.setArrayElementType builder))
    (.build builder)))

(defn StandardSQLDataType-to-edn [^StandardSQLDataType arg]
  {:post [(g/strict! ::StandardSQLDataType %)]}
  (cond-> {:typeKind (.getTypeKind arg)}
          (.getArrayElementType arg)
          (assoc :arrayElementType (StandardSQLDataType-to-edn (.getArrayElementType arg)))
          (.getStructType arg)
          (assoc :structType (StandardSQLStructType-to-edn (.getStructType arg)))))

(defn ^StandardSQLTableType StandardSQLTableType-from-edn
  [{:keys [columns] :as arg}]
  (g/strict! ::StandardSQLTableType arg)
  (let [builder (StandardSQLTableType/newBuilder)]
    (.setColumns builder (map StandardSQLField-from-edn columns))
    (.build builder)))

(defn StandardSQLTableType-to-edn [^StandardSQLTableType arg]
  {:post [(g/strict! ::StandardSQLTableType %)]}
  {:columns (map StandardSQLField-to-edn (.getColumns arg))})

(def schemas
  {::StandardSQLDataType
   [:map {:closed true}
    [:typeKind [:ref ::StandardSQLTypeName]]
    [:arrayElementType {:optional true} [:ref ::StandardSQLDataType]]
    [:structType {:optional true} [:ref ::StandardSQLStructType]]]

   ::StandardSQLField
   [:map {:closed true}
    [:name {:optional true} :string]
    [:dataType ::StandardSQLDataType]]

   ::StandardSQLStructType
   [:map {:closed true}
    [:fieldList [:sequential ::StandardSQLField]]]

   ::StandardSQLTableType
   [:map {:closed true}
    [:columns [:sequential ::StandardSQLField]]]

   ::StandardSQLTypeName
   [:enum "TIME" "FLOAT64" "INTERVAL" "BIGNUMERIC" "BOOL" "DATE" "BYTES" "GEOGRAPHY" "NUMERIC" "ARRAY" "JSON" "STRING" "DATETIME" "STRUCT" "TIMESTAMP" "INT64" "RANGE"]})

(g/include-schema-registry! (with-meta schemas {::g/name "gcp.bigquery.custom.StandardSQL"}))